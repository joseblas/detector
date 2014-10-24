package com.wh.integration.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TweetData;

import com.wh.integration.model.dao.TwitterMessageDao;
import com.wh.integration.model.entity.TwitterMessage;
import com.wh.integration.service.StakeService;

public class StakeServiceImpl implements StakeService {

	// @Autowired
	// @Qualifier("controlBusChannel")
	// private DirectChannel channel;

	@Autowired
	protected TwitterMessageDao dao;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	private static Logger log = LoggerFactory.getLogger(StakeServiceImpl.class);
	

	
	public TweetData send(TwitterMessage t) {
		String response = String.format("Hombre %s!, lorem upsum %s ", t.getFromUser(), new Random().nextInt());
		
		TweetData td = new TweetData(response);
//		td.atPlace("Where is  my bus?");
//		td.atLocation(34, 32);
		td.inReplyToStatus(t.getId());
		System.out.println("sent " + t.getText() +  " " + t.getId() + " " + t.getIdentifier());
		return td;
		
	}
	
	public com.wh.integration.model.entity.TwitterMessage transformAndSave(Tweet t) {
		
//		log.info("dao " + dao);
		com.wh.integration.model.entity.TwitterMessage findById = dao.findById(t.getId());
		if( findById == null){
			findById = new com.wh.integration.model.entity.TwitterMessage();
			findById.setId(t.getId());
			findById.setFromUser(t.getFromUser());
			findById.setText(t.getText());
			findById.setProfileImageUrl(t.getProfileImageUrl());
			dao.save(findById);
		}
		
//		
//		log.info("msg " + t.getToUserId() + " " + t.getUser().getScreenName()
//				+ " " + t.getCreatedAt());
		
		
		
		return findById;
	}
	
	
	

	public String fetchLiveData(String bus, String stopcode, int up) {
		String data = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1";
		data = String
				.format("http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1?ReturnList=StopCode1,StopPointName,LineName,DestinationText,EstimatedTime,DirectionId,Bearing,LineName,VehicleID&LineID=%s&DirectionID=%s&StopCode1=%s",
						bus, up, stopcode);
		URL url = null;

		try {

			url = new URL(data);
			List<String> readLines = IOUtils.readLines(url.openStream());
			for (String str : readLines) {
				System.out.println("String " + str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return "fetLiveData done!";
	}

	
	public String fetLiveData() {
		String data = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1";
		data = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1?ReturnList=StopCode1,StopPointName,LineName,DestinationText,EstimatedTime,DirectionId,Bearing,LineName,VehicleID&LineID=133&DirectionID=1&StopCode1=50338";
		URL url = null;

		try {

			url = new URL(data);
			List<String> readLines = IOUtils.readLines(url.openStream());
			for (String str : readLines) {
				System.out.println("String " + str);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "fetLiveData done!";
	}

	

	public static void main(String[] args) {

		System.out.println("Date " + new Date(1413887894396L));
		System.out.println("Date " + new Date(1413888084000L));
		System.out.println("Date " + new Date(1413887968000L));

	}
}
