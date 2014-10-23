package com.wh.integration.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.social.twitter.api.TweetData;

import com.wh.integration.model.dao.TwitterMessageDao;
import com.wh.integration.model.entity.TwitterMessage;

public class BusMessageSelector implements MessageSelector {

	private static Logger log = LoggerFactory.getLogger(BusMessageSelector.class);
	
	@Autowired
	protected TwitterMessageDao dao;
	
	@Override
	public boolean accept(Message<?> m) {
		
		
		
		if(m.getPayload() instanceof TweetData){
			log.error("Error ************* " + m.getPayload());
			return false;
		}
		TwitterMessage payload = (TwitterMessage) m.getPayload();
//		System.out.println(" " + payload.getText());
		TwitterMessage findById = dao.findById(payload.getId());
		if(payload.getText().contains("WIMB")&& findById.getSentAt() == null ){
				//
				findById.setSentAt(new Date());
				dao.save(findById);
				
				return true;
		}
		return false;
	}

}
