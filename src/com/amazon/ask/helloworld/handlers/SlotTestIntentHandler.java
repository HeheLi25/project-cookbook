package com.amazon.ask.helloworld.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.List;
import java.util.Optional;
public class SlotTestIntentHandler implements IntentRequestHandler{
	
	 @Override
	    public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
	        return input.matches(intentName("SlotTest"));
	    }

	    @Override
	    public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
	        final Slot slot = intentRequest.getIntent().getSlots().get("word");
	        String word = "";
	        if (slot != null
	                && slot.getResolutions() != null
	                && slot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
	            word = String.valueOf(slot.getValue());
	        }
	        String speechText = "You just said " + word + ". I'm I right?";
	       return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .withSimpleCard("HelloWorld", speechText)
	                .build();
	    }

}
