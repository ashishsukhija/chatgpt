package com.chatgpt.completion;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ChatGptMain {
	
	static String APIKey ="Specify the API key here";
	static ChatRequest request = new ChatRequest();
	

	public static void main(String[] args) throws UnirestException, JsonProcessingException {
		request.model ="gpt-3.5-turbo";
		request.temperature = 0.2;
		
		request.messages = new ArrayList<>();
		request.messages.add(new Message("system","Hey OpenAi, Lets talk about something interesting today!"));
		
		
		letsTalk("Hello !");
		letsTalk("How many requests CHATGPT serves in a day ?");
		letsTalk("ok, Where do you see most of the requests ? On UI or on API ?");
	}

	public static void letsTalk(String text) {
		
		Unirest.setTimeouts(0, 0);		
		request.messages.add(new Message("user", text));
		
		ChatResponse chatResp = null;
		HttpResponse<String> response  =  callAi(request);
			
			if(response!=null ) {
				if(response.getStatus()==200) {					
					try {
						chatResp = new ObjectMapper().readValue(response.getBody().toString(), ChatResponse.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(chatResp!=null) {
						
						request.messages.add(chatResp.getChoices().get(0).getMessage());
					}
				}
			}
	}

	private static HttpResponse<String> callAi(ChatRequest request) {
		System.out.println("--------------------ChatGpt4ChatModel.callAi()--------------------");
		HttpResponse<String> response = null;
		try {
			System.out.println(new ObjectMapper().writeValueAsString(request).toString());
			response = Unirest.post("https://api.openai.com/v1/chat/completions").header("Authorization", "Bearer "+APIKey)
					  .header("Content-Type", "application/json").body(new ObjectMapper().writeValueAsString(request)).asObject(String.class);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnirestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(response.getBody().toString());
		return response;
	}

}
