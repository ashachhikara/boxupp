package com.paxcel.boxupp.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.paxcel.boxupp.utilities.OSProperties;
import com.paxcel.boxupp.utilities.Utilities;
import com.paxcel.responseBeans.SnapshotData;
import com.paxcel.responseBeans.SnapshotStatus;

public class SnapshotManager{
	
	private static Logger logger = LogManager.getLogger(SnapshotManager.class.getName());
	
	public static SnapshotStatus persistBoxuppMappings(JsonNode mappings){
		OSProperties osProperties = OSProperties.getInstance();
		String projectDirectory = Utilities.getInstance().fetchActiveProjectDirectory();
		String fileLocation = projectDirectory + osProperties.getOSFileSeparator() + 
							  osProperties.getSerializedFileName();
		SnapshotStatus statusBean = new SnapshotStatus();
		try{
			FileOutputStream fileStream = new FileOutputStream(new File(fileLocation));
			ObjectOutputStream formOutputStream = new ObjectOutputStream(fileStream);
			formOutputStream.writeObject(mappings.toString());
			fileStream.close();
			formOutputStream.close();
			statusBean.setSaveLocation(fileLocation);
			statusBean.setStatus(0);
			
		}
		catch(IOException e){
			statusBean.setSaveLocation(fileLocation);
			statusBean.setStatus(1);
			logger.error("Boxupp Mappings could not be persisted : " + e.getMessage());
		}
		return statusBean;
	}

	
	public static SnapshotData retrieveBoxuppMappings() throws FileNotFoundException, ClassNotFoundException{
		
		String projectDirectory = Utilities.getInstance().fetchActiveProjectDirectory();
		OSProperties osProperties = OSProperties.getInstance();
		String fileLocation = projectDirectory + osProperties.getOSFileSeparator() + 
								osProperties.getSerializedFileName();
		File dataSnapshot = new File(fileLocation);
		SnapshotData snapshotData = new SnapshotData();
		JsonNode jsonNode;
		try{
			if(dataSnapshot.exists()){
				FileInputStream fileStream = new FileInputStream(dataSnapshot);
				ObjectInputStream inputStream = new ObjectInputStream(fileStream);
				String mappings = (String)inputStream.readObject();
				
				ObjectMapper mapper = new ObjectMapper();
				jsonNode = mapper.readTree(mappings); 
				fileStream.close();
				inputStream.close();

				snapshotData.setData(jsonNode);
				snapshotData.setFileExists(true);
			}
			else{
				snapshotData.setFileExists(false);
			}
		}
		catch(IOException e){
			logger.error("Error retrieving boxupp mappings : " + e.getMessage());
			snapshotData.setFileExists(false);
		}
		return snapshotData;
	}
}