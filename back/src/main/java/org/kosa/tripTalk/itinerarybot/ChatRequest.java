package org.kosa.tripTalk.itinerarybot;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChatRequest {
	//대화를 요청(보냄) 
	//: 양방향이기 때문에 List에 메세지를 담아서 보냄
	private List<Message> contents = new ArrayList<>();
	
	@Data
	public static class Message{
		private String role;
		private List<Part> parts = new ArrayList<>();
	}
	
	@Data
	public static class Part{
		private String text;
	}
	
	//user의 대화를 추가함
	public void addUserMessage(String userText) {
		Message message = new Message();
		message.setRole("user");
		
		Part part = new Part();
		part.setText(userText);
		
		message.getParts().add(part);
		contents.add(message);
	}
	
	//model의 대화를 추가함
	public void addModelMessage(String modelText) {
		Message message = new Message();
		message.setRole("model");
		
		Part part = new Part();
		part.setText(modelText);
		
		message.getParts().add(part);
		contents.add(message);
	}
	
	
	/*
	 * 단일 대화
	private List<Content> contents;
	
	@Data
	public static class Content{
		private List<Part> parts;
	}
	
	@Data
	public static class Part{
		private String text;
	}
	*/
		
	/*
	 * Open AI 용
	private String model = "";
	private List<Message> messages;
	
	@Data
	public static class Message{
		String role;
		String content;
	}
	*/
}
