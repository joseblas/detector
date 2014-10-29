package com.wh.integration.service.impl;

import com.wh.integration.model.dao.TwitterMessageDao;
import com.wh.integration.model.entity.TwitterMessage;
import com.wh.integration.service.StakeService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TweetData;

import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class StakeServiceImpl implements StakeService {

	// @Autowired
	// @Qualifier("controlBusChannel")
	// private DirectChannel channel;

	@Autowired
	protected TwitterMessageDao dao;
	
	@Autowired 
	private ApplicationContext applicationContext;
	
	private static Logger log = LoggerFactory.getLogger(StakeServiceImpl.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	
	public TweetData send(TwitterMessage t) {

        String data =  fetchLiveData("133","50338",1);
		String response = String.format("Hombre %s!, %s %s ", t.getFromUser(), data, new Random().nextInt());

        heartbeat("send");
		TweetData td = new TweetData(response);
//		td.atPlace("Where is  my bus?");
//		td.atLocation(34, 32);
		td.inReplyToStatus(t.getId());
		System.out.println("sent " + t.getText() +  " " + t.getId() + " " + t.getIdentifier());
		return td;
		
	}
	
	public com.wh.integration.model.entity.TwitterMessage transformAndSave(Tweet t) {


        heartbeat("transformAndSave");
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

        String respuesta = "";

		try {

			url = new URL(data);
			List<String> readLines = IOUtils.readLines(url.openStream());
            if(readLines!= null && readLines.size()>0) {
                StringBuilder response = new StringBuilder(readLines.size() * 25);
                boolean first = true;
                for (String str : readLines) {
                    if (first) {
                        first = false;
                    } else {
                        respuesta += str;
                        System.out.println("String " + str);
                        String datos = str.substring(1, str.length() - 1);
                        System.out.println("String " + datos);
                        String[] splitted = datos.split(",");
                        System.out.println("String datos splitted " + splitted);
                        System.out.println("String datos splitted " + splitted.length);

                        System.out.println("String datos splitted " + splitted[8]);
                        System.out.println("String datos splitted " + splitted[6]);
                        Date leavesIn = new Date(Long.parseLong(splitted[8]));
                        response.append(String.format("dest: %s at %s", splitted[6], sdf.format(leavesIn)));
                        response.append(System.lineSeparator());

                    }
                }
                respuesta += System.lineSeparator() + response.toString();
                if(respuesta.length()> 140){
                    respuesta = respuesta.substring(0,135);
                }
                log.info("res " + respuesta + " " + respuesta.length());
            }

		} catch (Exception e) {
			e.printStackTrace();
		}
        log.info("res " + respuesta + " " + respuesta.length());


		return respuesta;
	}


    private void heartbeat(String id){
        try {
            Path path = java.nio.file.Paths.get("/tmp/tfl.txt");
            try {
                Files.createFile(path);
            } catch (FileAlreadyExistsException e) {
                System.err.println("already exists: " + e.getMessage());
            }
            Files.write(path, (new Date().toString() + "::" + id + "").getBytes());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public String fetLiveData() {
		String data = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1";
		data = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1?ReturnList=StopCode1,StopPointName,LineName,DestinationText,EstimatedTime,DirectionId,Bearing,LineName,VehicleID&LineID=133&DirectionID=1&StopCode1=50338";
		URL url = null;

        heartbeat("fetLiveData");

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

		System.out.println("Date " + new Date(1414328965000L));

	}
}
