package com.wh.integration.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.social.twitter.api.Tweet;

import com.wh.integration.model.TwitterMessage;
import com.wh.integration.service.StakeService;

public class StakeServiceImpl implements StakeService {

	// @Autowired
	// @Qualifier("controlBusChannel")
	// private DirectChannel channel;

	@Autowired 
	private ApplicationContext applicationContext;
	
	private static Logger log = LoggerFactory.getLogger(StakeServiceImpl.class);
	static Set<Long> persisted ;

	static {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void init() throws Exception {
		persisted = new HashSet<Long>();
		Path path = Paths.get("/home/joseta/tws.txt");
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		List<String> readAllLines = Files.readAllLines(path,
				StandardCharsets.UTF_8);
		for (String tmp : readAllLines) {
			persisted.add(Long.parseLong(tmp));
		}
	}

	static void end() {
		List<String> data = new ArrayList<>();
		for (Long id : persisted) {
			data.add(String.valueOf(id));
		}
		Path path = Paths.get("/home/joseta/tws.txt");
		try {
			Files.delete(path);

			Files.createFile(path);
			Files.write(path, data, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public TwitterMessage send(Tweet t) {
		String response = String.format("Hola %s, lorem upsum %s blah blah blah %s", t.getFromUser(),t.getInReplyToStatusId(),t.getLanguageCode());
		TwitterMessage tm = new TwitterMessage();
		tm.setText(response);
		tm.setCreatedAt(new Date());
		tm.setFromUser("joseblas");
		
		return tm;
		
	}
	
	public Tweet read(Tweet t) {
		
		
		log.info("msg " + t.getToUserId() + " " + t.getUser().getScreenName()
				+ " " + t.getCreatedAt());
		
		// System.out.println("msg " + t.getText());
		if(!persisted.contains(t.getId())){
			persisted.add(t.getId());
			
			
			
			
			
			
			
		}
		return t;
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
		
		end();

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

			end();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "fetLiveData done!";
	}

	public String fetchData() {
		String data = "http://data.tfl.gov.uk/tfl/syndication/feeds/journey-planner-timetables.zip?app_id=11fb4058&app_key=98eede397ce3580e4be444b78653d1ec";
		try {
			File dest = new File("/tmp/tfl");
			unZip(new ZipInputStream(new URL(data).openStream()), dest);

			File[] listFiles = dest.listFiles();
			for (File f : listFiles) {
				unZip(f, new File(dest.getAbsolutePath() + File.separator
						+ "unzipped" + File.separator + f.getName()));
			}
			// get live data
			// http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1
		} catch (Exception e) {
			log.error("Ex", e);
		}

		return "data";
	}

	private void unZip(File zipFile, File folder) throws FileNotFoundException {
		unZip(new ZipInputStream(new FileInputStream(zipFile)), folder);
	}

	private void unZip(ZipInputStream zis, File folder) {

		byte[] buffer = new byte[5 * 1024];

		try {

			// create output directory is not exists

			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(folder, fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public static void main(String[] args) {

		System.out.println("Date " + new Date(1413887894396L));
		System.out.println("Date " + new Date(1413888084000L));
		System.out.println("Date " + new Date(1413887968000L));

	}
}
