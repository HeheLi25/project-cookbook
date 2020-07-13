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

public class FindElixirIntentHandler implements IntentRequestHandler {

	@Override
	public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
		return input.matches(intentName("FindElixir"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
		String speechText = "";
		ArrayList<String> elixirs = DBConnect.findElixir();
		if(elixirs != null) {
			speechText = "There are "+ elixirs.size() +" kinds of elixirs, including ";
			if(elixirs.size() == 1)
				speechText += elixirs.get(0) + ". ";
			else {
				for(int i = 0; i < elixirs.size(); i++) {
					if(i == elixirs.size() - 1) {
						speechText = speechText.substring(0, speechText.length()-2);
						speechText += " and " + elixirs.get(i) + ". ";
					}
					else
						speechText += elixirs.get(i) + ", ";
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
