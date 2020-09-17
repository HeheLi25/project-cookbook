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
 * Handler for the questions about monster parts. 
 * @author YirongLi
 *
 */
public class MonsterPartIntentHandler implements IntentRequestHandler {

	@Override
	public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
		return input.matches(intentName("FindMonsterPart"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
		final Slot slot = intentRequest.getIntent().getSlots().get("monsterPart");
		String part = "";
		if (slot != null && slot.getResolutions() != null
				&& slot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
			part = String.valueOf(slot.getValue());
		}
		String speechText = "";

		String monster = DBConnect.findMonsterPart(part);


		//if the slot does not match
		if (monster == "" || part == "" || monster == null) {
			speechText = "Hmm, I don't know about "+String.valueOf(slot.getValue())+ ", please make sure it is the right name of a monster part. ";
		} 
		//if the result is returned successfully
		else {
			speechText += part + " can dropped from "+ monster + ". ";
		}
		speechText = Tool.firstUpperCase(speechText);
		return input.getResponseBuilder()
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .withSimpleCard("HelloWorld", speechText)
                .build();
	}

}
