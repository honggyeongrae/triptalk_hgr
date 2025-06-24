package org.kosa.tripTalk.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kosa.tripTalk.category.Category;
import org.kosa.tripTalk.category.CategoryRepository;
import org.kosa.tripTalk.common.dto.PageRequestDTO;
import org.kosa.tripTalk.common.dto.Search;
import org.kosa.tripTalk.exception.NotFoundException;
import org.kosa.tripTalk.product.discount.DiscountDTO;
import org.kosa.tripTalk.product.discount.DiscountType;
import org.kosa.tripTalk.seller.Seller;
import org.kosa.tripTalk.seller.SellerRepository;
import org.kosa.tripTalk.user.User;
import org.kosa.tripTalk.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("상품 테스트")
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProductServiceTest {

	@Autowired
	ProductService productService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ProductRepository productRepository;
	
	private User savedUser;
    private Seller savedSeller;
    private Category savedCategory;

    @BeforeEach
    void setup() {
        savedUser = userRepository.save(User.builder()
                .userId("user123")
                .name("홍길동")
                .email("user@test.com")
                .password("1234")
                .nickname("길동이")
                .role(User.Role.SELLER)
                .phone("010-1234-5678")
                .build());

        savedSeller = sellerRepository.save(Seller.builder()
                .user(savedUser)
                .userid(savedUser.getUserId())
                .businessNumber("111-22-33333")
                .businessName("길동상점")
                .contact("070-1234-5678")
                .build());

        savedCategory = categoryRepository.save(Category.builder()
                .kind("여행")
                .description("여행 카테고리")
                .iconUrl("https://example.com/default.png")
                .build());
    }

    @Test
    @DisplayName("정상 상품 등록")
    void createProduct_success() {
        ProductRequestDTO request = createProductRequest(
                "제주도 여행", "힐링 3박 4일", 299000,
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(6)
        );

        productService.create(request);

        List<Product> result = productRepository.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("제주도 여행");
    }

    @Test
    @DisplayName("상품 정상 수정")
    void updateProduct_success() {
        Product savedProduct = saveProduct("기존 제목", 100000);

        ProductRequestDTO updateDto = createProductRequest(
                "수정된 제목", "수정된 설명", 200000,
                LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(13)
        );

        productService.update(savedProduct.getId(), updateDto);

        Product updated = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("수정된 제목");
        assertThat(updated.getAddress()).isEqualTo("기본 주소");
        assertThat(updated.getPrice()).isEqualTo(200000);
    }

    @Test
    @DisplayName("상품 목록 페이징 + 정렬 + 검색 조회 성공")
    void getAllProducts_withPagingSortingAndSearch_success() {
        for (int i = 1; i <= 15; i++) {
            saveProduct("제주 상품 " + i, 10000 * i);
        }

        PageRequestDTO pageRequestDTO = new PageRequestDTO(1, 10, "price,asc");
        Pageable pageable = pageRequestDTO.toPageable();

        Search search = new Search();
        search.setSearchKey("title");
        search.setSearchValue("제주");

        Page<ProductResponseDTO> result = productService.getAllProducts(pageable, search);

        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isFalse();
        assertThat(result.getContent().get(0).getPrice()).isEqualTo(10000);
    }

    @Test
    @DisplayName("상품 조회 실패 - 존재하지 않는 ID")
    void getProduct_fail_whenProductNotFound() {
        Long invalidId = 9999L;

        assertThatThrownBy(() -> productService.getProductById(invalidId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("해당 상품을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("상품 수정 시 할인 정보도 변경된다")
    void updateProduct_discountOnly() {
        DiscountDTO discount1 = createDiscountDTO(DiscountType.FIXED, 5000, 0, "초기 프로모션", 7);

        ProductRequestDTO request = createProductRequest(
                "부산 여행", "기존 상품", 150000,
                LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(5)
        ).toBuilder().discount(discount1).build();

        Long productId = productService.create(request);

        DiscountDTO discount2 = createDiscountDTO(DiscountType.RATE, 0, 0.2, "20% 여름 할인", 10);

        ProductRequestDTO updateDTO = createProductRequest(
                "부산 여행", "기존 상품", 150000,
                request.getStartDate(), request.getEndDate()
        ).toBuilder().discount(discount2).build();

        productService.update(productId, updateDTO);

        Product updated = productRepository.findById(productId).orElseThrow();
        assertThat(updated.getDiscount().getDiscountType()).isEqualTo(DiscountType.RATE);
        assertThat(updated.getDiscountedPrice()).isEqualTo(120000);
        assertThat(updated.getDiscount().getName()).isEqualTo("20% 여름 할인");
    }

    // ========== 공통 유틸 메서드 ==========

    private ProductRequestDTO createProductRequest(String title, String description, int price, LocalDateTime start, LocalDateTime end) {
        return ProductRequestDTO.builder()
                .title(title)
                .description(description)
                .address("기본 주소")
                .price(price)
                .startDate(start)
                .endDate(end)
                .sellerId(savedUser.getId())
                .categoryId(savedCategory.getId())
                .build();
    }

    private Product saveProduct(String title, int price) {
        return productRepository.save(Product.builder()
                .title(title)
                .description("기본 설명")
                .address("기본 주소")
                .price(price)
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(3))
                .category(savedCategory)
                .seller(savedSeller)
                .build());
    }

    private DiscountDTO createDiscountDTO(DiscountType type, int amount, double rate, String name, int plusDays) {
        return DiscountDTO.builder()
                .discountType(type)
                .discountAmount(amount)
                .discountRate(rate)
                .name(name)
                .startAt(LocalDateTime.now().minusDays(1))
                .endAt(LocalDateTime.now().plusDays(plusDays))
                .build();
    }

}
