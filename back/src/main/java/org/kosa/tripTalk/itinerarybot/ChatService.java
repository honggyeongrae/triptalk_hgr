package org.kosa.tripTalk.itinerarybot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
	@Value("${gemini.api-key}")
	private String apiKey;
	
	@Value("${gemini.api-url}")
	private String apiUrl; 
	
	//restTemplate : HTTP요청 보내기 도구(Spring)
	private final RestTemplate restTemplate = new RestTemplate();
	//ObjectMapper : JSON<-> Java 객체 변환 도구 
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	
	public ChatRequest chat(ChatRequest request, String userInput) {
		//사용자가 입력한 내용 ChatRequest객체에 추가
		request.addUserMessage(userInput);
		
		//HTTP 요청 준비 (JSON 형태로 보낼 것)
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);
		
		//외부 API 호출
		String urlWithKey = apiUrl + "?key=" + apiKey;
		ResponseEntity<ChatResponse> responseEntity = restTemplate.postForEntity(
				urlWithKey, entity, ChatResponse.class);
		
		//응답 처리 
		ChatResponse response = responseEntity.getBody();
		if(response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
			//응답이 유효하다면, 모델이 생성한 메세지 꺼냄
			ChatResponse.Candidate candidate = response.getCandidates().get(0);
            ChatResponse.Message modelMessage = candidate.getContent();

            StringBuilder modelTextBuilder = new StringBuilder();
            for (ChatResponse.Part part : modelMessage.getParts()) {
                modelTextBuilder.append(part.getText());
            }
            
            //모델의 응답도 저장
            request.addModelMessage(modelTextBuilder.toString());
		}
		
		return request;
	
	}
	
}
