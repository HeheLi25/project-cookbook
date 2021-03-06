package com.amazon.ask.helloworld.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.helloworld.database.DBConnect;
import com.amazon.ask.helloworld.database.Dish;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

/**
 * Handler for the questions about a specific special effect. 
 */
public class FindEffectIntentHandler implements IntentRequestHandler{
	@Override
	public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
		return input.matches(intentName("FindEffect"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
		final Slot slot = intentRequest.getIntent().getSlots().get("effect");
		String dish = "";
		if (slot != null && slot.getResolutions() != null
				&& slot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
			dish = String.valueOf(slot.getValue());
		}
		String speechText = "";
		Dish dishObj = null;

		dishObj = DBConnect.findEffect(dish);


		//if the slot does not match
		if (dishObj == null) {
			speechText = "Sorry, " + String.valueOf(slot.getValue()) + " is beyond the capability of the dishes I know. "
					+ "Please try another one or try to use a more common wording.";
		} 
		//if the result is returned successfully
		else {
			speechText = "Try this one: " + dishObj.getName() + ". Its effect is "+ dishObj.getEffect() +" and it is made of ";
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
		}
		return input.getResponseBuilder()
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .withSimpleCard("HelloWorld", speechText)
                .build();
	}

}
