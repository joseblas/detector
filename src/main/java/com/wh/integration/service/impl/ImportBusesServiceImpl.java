package com.wh.integration.service.impl;

import com.kurtraschke.tfl.tools.AgencyExtraData;
import com.kurtraschke.tfl.tools.Translator;
import com.wh.integration.service.ImportBusesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import uk.org.transxchange.TransXChange;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ImportBusesServiceImpl implements ImportBusesService,InitializingBean {

	private static final String TMP_TFL = "/tmp/tfl";

	@Autowired 
	private ApplicationContext applicationContext;
	
	private static Logger log = LoggerFactory.getLogger(ImportBusesServiceImpl.class);
	
	private String url;
	
	private String appId;
	
	private String secret;
	
	@Override
	public String importBuses() {
		log.info("Importing data from tfl");
		fetchData();
	    readData();
		return new Date().toString();
	}

	public String processData(){
        fetchData();
        readData();
        return "ok";
    }
	
	private String readData() {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(TransXChange.class);
		
	    Unmarshaller u = jc.createUnmarshaller();

	    List<File> inputFiles = new ArrayList<>();
	    File file = new File(TMP_TFL+ File.separator+ "unzipped" + File.separator );

        if(file == null){
            return "No files";
        }

	    for (String s : file.list()) {
	      File f = new File(TMP_TFL+ File.separator+ "unzipped" + File.separator );

	      if (f.exists()) {
	        if (f.isDirectory()) {
	          inputFiles.addAll(Arrays.asList(f.listFiles()));
	        } else {
	          inputFiles.add(f);
	        }
	      }
	    }

	    Translator tr = new Translator(new AgencyExtraData("http://www.tfl.gov.uk", "Europe/London", null, null, null));

	    for (File f : inputFiles) {
	      log.info("Loading file " + f.getName());
	      TransXChange txc = (TransXChange) u.unmarshal(f);
	      tr.load(txc);

	      tr.status();
	    }

	    System.out.println("Writing GTFS...");
//	    tr.write(new File("/Users/kurt/Desktop/test/"));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ok";
	}
		
	
	
	




	public String fetchData() {
//		String data = "http://data.tfl.gov.uk/tfl/syndication/feeds/journey-planner-timetables.zip?app_id=11fb4058&app_key=98eede397ce3580e4be444b78653d1ec";
		String data = String.format("%s?app_id=%s&app_key=%s",url,appId,secret);
		try {
			File dest = new File(TMP_TFL);
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
	
	
	public String getUrl() {
		return url;
	}






	public void setUrl(String url) {
		this.url = url;
	}






	public String getAppId() {
		return appId;
	}






	public void setAppId(String appId) {
		this.appId = appId;
	}






	public String getSecret() {
		return secret;
	}






	public void setSecret(String secret) {
		this.secret = secret;
	}










	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
