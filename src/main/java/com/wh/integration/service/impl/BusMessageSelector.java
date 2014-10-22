package com.wh.integration.service.impl;

import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.social.twitter.api.Tweet;

public class BusMessageSelector implements MessageSelector {

	@Override
	public boolean accept(Message<?> m) {
		Tweet payload = (Tweet) m.getPayload();
		System.out.println(" " + payload.getText());
		if(payload.getFromUser().equals("joseblas")){
			if(!StakeServiceImpl.persisted.contains(payload.getId()))
				return true;
		}
		return false;
	}

}
