package com.amazon.ask.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.helloworld.database.DBConnect;
import com.amazon.ask.helloworld.database.Dish;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import static com.amazon.ask.request.Predicates.intentName;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handler for the questions about what kinds of mushroom are there. 
 * @author YirongLi
 *
 */
public class FindMushroomIntentHandler implements IntentRequestHandler {

	@Override
	public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
		return input.matches(intentName("FindMushroom"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
		String speechText = "";
		ArrayList<String> shrooms = DBConnect.findMushroom();
		if(shrooms != null) {
			speechText = "There are "+ shrooms.size() +" kinds of mushrooms growing on the land of Hyrule, including ";
			if(shrooms.size() == 1)
				speechText += shrooms.get(0) + ". ";
			else {
				for(int i = 0; i < shrooms.size(); i++) {
					if(i == shrooms.size() - 1) {
						speechText = speechText.substring(0, speechText.length()-2);
						speechText += " and " + shrooms.get(i) + ". ";
					}
					else
						speechText += shrooms.get(i) + ", ";
				}
			}
		}
		else
			speechText = "Sorry, there is something wrong with the database. I promise I will fix it as soon as possible.";
		
		
		return input.getResponseBuilder()
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .withSimpleCard("HelloWorld", speechText)
                .build();
	}

}
