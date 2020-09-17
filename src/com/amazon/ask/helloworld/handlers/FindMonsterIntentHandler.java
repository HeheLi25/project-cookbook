package com.amazon.ask.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.helloworld.database.DBConnect;
import com.amazon.ask.helloworld.database.Tool;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Handler for the questions about what a monster drops. 
 * @author YirongLi
 *
 */
public class FindMonsterIntentHandler implements IntentRequestHandler {

	@Override
	public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
		return input.matches(intentName("FindMonster"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
		final Slot slot = intentRequest.getIntent().getSlots().get("monster");
		String monster = "";
		if (slot != null && slot.getResolutions() != null
				&& slot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
			monster = String.valueOf(slot.getValue());
		}
		String speechText = "";
		ArrayList<String> drop = DBConnect.findMonster(monster);  //Query the database. 
		if (drop == null || drop.size() == 0 || drop.size() > 5) {
			speechText = "Sorry, I don't know a lot about " + String.valueOf(slot.getValue())
					+ ". Please make sure it is a kind of monster that exist on the land of Hyrule.";
		}else {

				speechText = speechText + monster + " can probably drop ";
				if (drop.size() == 1)
					speechText += drop.get(0) + ". ";
				else {
					for (int i = 0; i < drop.size(); i++) {
						if (i == drop.size() - 1) {
							speechText = speechText.substring(0, speechText.length() - 2);
							speechText += " and " + drop.get(i) + ". ";
						} else
							speechText += drop.get(i) + ", ";
					}
				}
		}
		speechText = Tool.firstUpperCase(speechText);
		return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(false)
				.withSimpleCard("HelloWorld", speechText).build();
	}

}
