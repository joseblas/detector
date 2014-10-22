package com.wh.integration.service.impl;

import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.social.twitter.api.TweetData;

import com.wh.integration.model.entity.TwitterMessage;

public class BusMessageSelector implements MessageSelector {

	@Override
	public boolean accept(Message<?> m) {
		if(m.getPayload() instanceof TweetData){
			return false;
		}
		TwitterMessage payload = (TwitterMessage) m.getPayload();
		System.out.println(" " + payload.getText());
		if(payload.getText().contains("WIMB")){
				return true;
		}
		return false;
	}

}
