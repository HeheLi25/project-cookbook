package com.amazon.ask.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.helloworld.database.DBConnect;
import com.amazon.ask.helloworld.database.Dish;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.List;
import java.util.Optional;

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
		String speechText = "";
		Dish dishObj = null;
		try {
			dishObj = DBConnect.getDish(dish);
		} catch (Exception e) {
			speechText = "Sorry, something wrong happened with the database. I'll fix it as soon as I can. ";
		}
		if (dishObj == null) {
			speechText = "Currently, I don't know how to make "+ dish +", I promise I will keep learning. ";
		} else {
			speechText = dishObj.getName() + " is made of these ingredients: ";
			for (String s : dishObj.getIngredients()) {
				speechText = speechText + s + ", ";
			}
			speechText += "and its effect is " + dishObj.getEffect() + ". ";
			if (!dishObj.isHeal()) {
				speechText += "But it can not heal you.";
			}
		}
		return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .build();
	}

}
