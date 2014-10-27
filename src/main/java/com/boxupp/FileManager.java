package com.boxupp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.boxupp.responseBeans.VagrantFile;
import com.boxupp.responseBeans.VagrantFileStatus;
import com.boxupp.utilities.OSProperties;
import com.boxupp.utilities.Utilities;


/*
 * For producing a Vagrant Syntactic file 
 * */
public class FileManager {
	private static Logger logger = LogManager.getLogger(FileManager.class.getName());
	
	public VagrantFileStatus writeFileToDisk(String data){
		
		String projectDir = Utilities.getInstance().fetchActiveProjectDirectory();
		String fileOutputDir = projectDir + OSProperties.getInstance().getOSFileSeparator() + 
								OSProperties.getInstance().getVagrantFileName();
		VagrantFileStatus vagrantFileStatus = new VagrantFileStatus();
		
		File file = new File(fileOutputDir);
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			file.createNewFile();
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(data);
			bw.close();
			vagrantFileStatus.setFileCreated(true);
			vagrantFileStatus.setFileCreationPath(fileOutputDir);
		} catch (IOException e) {
			logger.error("Error writing to the Vagrant File : "+e.getMessage());
			file.delete();
			vagrantFileStatus.setFileCreated(false);
		}
		
		return vagrantFileStatus;
	}
	
	public VagrantFile fetchVagrantFileData(){
		
		String projectDir = Utilities.getInstance().fetchActiveProjectDirectory();
		String fileLocation = projectDir + OSProperties.getInstance().getOSFileSeparator() + 
								OSProperties.getInstance().getVagrantFileName();
		File vagrantFile = new File(fileLocation);
		StringBuffer fileData = new StringBuffer();
		String lineData;
		VagrantFile fileResponse = new VagrantFile();
		try{
			if(vagrantFile.exists()){
				fileResponse.setFileExists(true);
				BufferedReader bufferedReader = new BufferedReader(new FileReader(vagrantFile));
				while((lineData = bufferedReader.readLine()) != null){
					fileData.append(lineData + OSProperties.getInstance().getOSLineSeparator());
				}
				bufferedReader.close();
				fileResponse.setFileContent(fileData.toString());
				fileResponse.setStatusCode(0);
			}else{
				fileResponse.setStatusCode(0);
				fileResponse.setFileContent("Vagrant file does not exist");
				fileResponse.setFileExists(false);
			}
			return fileResponse;
		}catch(Exception e){
			fileResponse.setStatusCode(1);
			return fileResponse;
		}
	}
}
