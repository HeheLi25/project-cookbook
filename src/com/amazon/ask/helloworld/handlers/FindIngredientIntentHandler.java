package com.amazon.ask.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.helloworld.database.DBConnect;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.Optional;

public class FindIngredientIntentHandler implements IntentRequestHandler {

	@Override
	public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
		return input.matches(intentName("FindIngredient"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
		final Slot slot = intentRequest.getIntent().getSlots().get("ingredient");
		String ingredient = "";
		if (slot != null && slot.getResolutions() != null
				&& slot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
			ingredient = String.valueOf(slot.getValue());
		}
		ArrayList<String> dishes = null;
		String speechText = "";
		try {
			dishes = DBConnect.searchIngredient(ingredient);
			if(dishes == null)
				speechText = "Sorry, I don't know a lot about "+ String.valueOf(slot.getValue()) 
				+ ". If it is a monster part or an animal, try to ask me about animal types, "
				+ "such as \"tough animal\", \"mighty animal\" or just \"monster part\".";
			else {
				speechText = "With " + ingredient + ", you can make ";
				if(dishes.size() == 1)
					speechText += dishes.get(0) + ". ";
				else {
					for(int i = 0; i < dishes.size(); i++) {
						if(i == dishes.size() - 1) {
							speechText = speechText.substring(0, speechText.length()-2);
							speechText += " and " + dishes.get(i) + ". ";
						}
						else
							speechText += dishes.get(i) + ", ";
					}
				}
				speechText += "Ask me if you want to know more about any dish. ";
			}
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		
		
		
		return input.getResponseBuilder()
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .withSimpleCard("HelloWorld", speechText)
                .build();
	}

}
