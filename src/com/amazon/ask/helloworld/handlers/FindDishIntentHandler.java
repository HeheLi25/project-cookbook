package com.amazon.ask.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.helloworld.database.DBConnect;
import com.amazon.ask.helloworld.database.Dish;
import com.amazon.ask.helloworld.database.Tool;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handler for the questions about a specific dish. 
 * @author YirongLi
 *
 */
public class FindDishIntentHandler implements IntentRequestHandler {

	@Override
	public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
		return input.matches(intentName("FindDish"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
		final Slot slot = intentRequest.getIntent().getSlots().get("dish");
		String dish = "";
		if (slot != null && slot.getResolutions() != null
				&& slot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
			dish = String.valueOf(slot.getValue());
		}
		dish = dish.replace("Alexa", "elixir");
		dish = dish.replace("election", "elixir");
		String speechText = "";
		Dish dishObj = null;

		dishObj = DBConnect.getDish(dish); //Query the database
		String cardText = "";

		//if the slot does not match
		if (dishObj == null) {
			speechText = "Hmm, you got me this time. I don't know about "+String.valueOf(slot.getValue())+ ", but I promise I will learn. ";
		} 
		//if the result is returned successfully
		else {
			speechText = dishObj.getName() + " is made of ";
			ArrayList<String> ings = dishObj.getIngredients();
			if(ings.size() == 1)
				speechText += ings.get(0) + ". ";
			else {
				for(int i = 0; i < ings.size(); i++) {
					if(i == ings.size() - 1) {
						speechText = speechText.substring(0, speechText.length()-2);
						speechText += " and " + ings.get(i) + ". ";
					}
					else
						speechText += ings.get(i) + ", ";
				}
			}
			
			for(String s: ings) {
				cardText += s + "\n";
			}
			speechText += "Its effect is " + dishObj.getEffect() + ". ";
			if (!dishObj.isHeal()) {
				speechText += "But it can not heal you.";
			}
		}
		speechText = Tool.firstUpperCase(speechText);
		
	    String cardTitle = dish;
	    
		return input.getResponseBuilder()
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .withStandardCard(cardTitle, cardText, null)
                .withReprompt(speechText)
                .build();
	}

}
