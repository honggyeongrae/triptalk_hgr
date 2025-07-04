//package org.kosa.tripTalk.chat;
//
//import org.kosa.tripTalk.user.User;
//import org.kosa.tripTalk.user.UserRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//
//@Transactional
//@Controller
//@RequiredArgsConstructor
//public class ChatMessageController {
//
//    private final ChatMessageService chatMessageService;
//    private final UserRepository userRepository;
//
//    @MessageMapping("/chat/message")
//    public void sendMessage(@Payload ChatPayload payload,  Authentication authentication) {
//      
//      //헤더에서 user의 id 추출
////      User customer  = (User) authentication.getPrincipal();
////      Long customerId = customer.getId();
//      
//      //테스트 상 인증불가로 하드코딩
//      Long customerId = (long) 1;
//      User customer = userRepository.findById(customerId) .orElseThrow(() -> new IllegalArgumentException("보낸 사람 없음"));
//
//      //sellerId로 판매자 User 객체 조회
//      Long sellerId = payload.getReceiverId();
//      
//      System.out.println("========="+customer);
//      System.out.println("========="+customerId);
//
//      //채팅방 생성
//      ChatRoom room = chatMessageService.getOrCreateRoom(customerId, sellerId);
//      
//      //메시지 저장이나 전송 처리 (필요 시)
//      chatMessageService.processMessage(room, customer, payload.getMessage());
//     
//    }
//    
//    @GetMapping("/chat/room/{roomId}")
//    public ResponseEntity<String> getRoomId(@RequestParam Long customerId, @RequestParam Long sellerId) {
//        String roomId = chatMessageService.generateRoomId(customerId, sellerId);
//        return ResponseEntity.ok(roomId);
//    }
//    
//    
//    
//    
//}