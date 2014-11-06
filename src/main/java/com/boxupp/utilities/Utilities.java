package com.boxupp.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.beans.BoxuppPuppetData;
import com.boxupp.beans.BoxuppScriptsData;
import com.boxupp.beans.BoxuppVMData;
import com.boxupp.beans.SyncFolderMapping;
import com.boxupp.beans.VMConfiguration;
import com.boxupp.db.DAOProvider;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.SyncFoldersBean;
import com.google.gson.Gson;
import com.j256.ormlite.dao.ForeignCollection;

public class Utilities {
	
	private static Logger logger = LogManager.getLogger(Utilities.class.getName());
	private static Utilities utilities = null;
	private String activeProjectDirectory = "";
	public OSProperties osProperties = null;
	
	public Utilities(){
		osProperties = OSProperties.getInstance();
	}
	
	public static Utilities getInstance(){
		if(utilities == null){
			try{
				utilities = new Utilities();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return utilities;
	}
	
	public BoxuppVMData populateVMDataBean(JsonNode mappings){
		
		Gson boxuppConfigurations = new Gson();
		BoxuppVMData boxuppVMData = new BoxuppVMData();
		try{
			boxuppVMData = boxuppConfigurations.fromJson(mappings.toString(),BoxuppVMData.class);
		}catch(NumberFormatException e){
			System.out.println("Number format exception for : "+mappings.toString());
		}
		return boxuppVMData;
	}
	
	public BoxuppScriptsData populateScriptsBean(JsonNode mappings){
		
		Gson scriptsConfigurations = new Gson();
		BoxuppScriptsData boxuppScriptsData = new BoxuppScriptsData();
		boxuppScriptsData = scriptsConfigurations.fromJson(mappings.toString(),BoxuppScriptsData.class);
		return boxuppScriptsData;
	}
	
	public boolean createProjectDirectory(ProjectBean projectBean){
//		File projectDirectory = new File()
		return true;
	}
	
	public BoxuppPuppetData populatePuppetData (JsonNode mappings){
		Gson puppetConfigurations = new Gson();
		BoxuppPuppetData boxuppPuppetData = new BoxuppPuppetData();
		boxuppPuppetData = puppetConfigurations.fromJson(mappings.toString(), BoxuppPuppetData.class);
		return boxuppPuppetData;
	}
	
	public void initializeDirectory(){
		
		String userHomeDir = osProperties.getUserHomeDirectory() + 
							 osProperties.getOSFileSeparator() + "Boxupp";
		File projectDir = new File(userHomeDir);
		if(projectDir.exists()){
			logger.debug("Project Directory found at : "+userHomeDir);
		}else{
			logger.debug("Project Directory does not exist");
			projectDir.mkdir();
			logger.debug("Project Directory initialized at : " + userHomeDir);
		}
//		activeProjectDirectory = userHomeDir;
	}
	
	/*public void commitScriptsToDisk(BoxuppScriptsData scriptsData){
		ArrayList<ShellScriptBean> scriptBeanList = scriptsData.getScriptsList();
		for(int counter=0; counter<scriptBeanList.size(); counter++){
			writeScriptToDisk(scriptBeanList.get(counter));
		}
	}
	*/
	
	public void commitFoldersToDisk(BoxuppVMData vmData){
		ArrayList<VMConfiguration> vmConfigurations = vmData.getVmData();
		for(int counter=0; counter<vmConfigurations.size(); counter++){
			ArrayList<SyncFolderMapping> folderMappings = vmConfigurations.get(counter).getSyncFolders();
			for(int folderCounter = 0 ; folderCounter<folderMappings.size(); folderCounter++){
				createFolderOnDisk(folderMappings.get(folderCounter).getHostFolder());
			}
		}
	}
	public void commitSyncFoldersToDisk(List<MachineConfigurationBean> machineConfigs){
	
		for(MachineConfigurationBean machineConfig : machineConfigs){
			ForeignCollection<SyncFoldersBean> syncFolders = machineConfig.getSyncFolders();
			for(SyncFoldersBean syncFolder : syncFolders){
				createFolderOnDisk(syncFolder.getHostFolder());
			}
		}
	}
	public void createFolderOnDisk(String folderName){
		String folderLocation = fetchActiveProjectDirectory() + osProperties.getOSFileSeparator() + folderName; 
		File directory = new File(folderLocation);
		if(!directory.exists()){
			directory.mkdir();
		}
	}
	
	public void writeScriptToDisk(ShellScriptBean scriptBean){
		String scriptsDir = osProperties.getUserHomeDirectory() + osProperties.getOSFileSeparator()
							+osProperties.getPrimaryFolderName()+osProperties.getOSFileSeparator()
							+osProperties.getScriptsDirName()+osProperties.getOSFileSeparator();
		checkIfDirExists(new File(scriptsDir));
		try{
			BufferedWriter scriptWriter = new BufferedWriter(new FileWriter(new File(scriptsDir + scriptBean.getScriptName())));
			scriptWriter.write(scriptBean.getScriptContent());
			scriptWriter.close();
		}
		catch(IOException e){
			logger.error("Error writing script : "+scriptBean.getScriptName() + " : "+e.getMessage());
		}
	}
	public void updateScriptData(ShellScriptBean scriptBean){
		String scriptsDir = osProperties.getUserHomeDirectory() + osProperties.getOSFileSeparator()
				+osProperties.getPrimaryFolderName()+osProperties.getOSFileSeparator()
				+osProperties.getScriptsDirName()+osProperties.getOSFileSeparator();
		checkIfDirExists(new File(scriptsDir));
		try{
			BufferedWriter scriptWriter = new BufferedWriter(new FileWriter(new File(scriptsDir + scriptBean.getScriptName())));
			scriptWriter.write(scriptBean.getScriptContent());
			scriptWriter.close();
		}
		catch(IOException e){
			logger.error("Error writing script : "+scriptBean.getScriptName() + " : "+e.getMessage());
		}
	}
	
	public String fetchActiveProjectDirectory(){
		if(activeProjectDirectory.isEmpty()){
			initializeDirectory();
		}
		return activeProjectDirectory;
	}
	
	public boolean changeActiveDirectory(Integer projectID){
		OSProperties osProperties = OSProperties.getInstance();
		try {
			String projectName = DAOProvider.getInstance().fetchProjectDao().queryForId(projectID).getName();
			activeProjectDirectory = osProperties.getUserHomeDirectory() + 
									 osProperties.getOSFileSeparator() +
									 "Boxupp" + osProperties.getOSFileSeparator() + projectName;
		} catch (SQLException e) {
			logger.error("Error setting the active project directory : "+e.getMessage());
			return false;
		}
		return true;
	}
	
	public void checkIfDirExists(File dirLocation){
		if(!dirLocation.isDirectory()){
			dirLocation.mkdir();
		}
	}
	
	public void deleteScriptfileOnDisk(String fileName){
		String scriptFilepath = fetchActiveProjectDirectory() + osProperties.getOSFileSeparator()
				+osProperties.getScriptsDirName()+osProperties.getOSFileSeparator()+fileName;
		File file  = new File(scriptFilepath);
		deleteFile(file);
    
	}
	
	public void deleteProjectFile(String projectName){
		String projectDir = osProperties.getUserHomeDirectory() + 
				 osProperties.getOSFileSeparator() +
				 "Boxupp" + osProperties.getOSFileSeparator() + projectName;
		File file  = new File(projectDir);
		deleteFile(file);
		
	}
	
	
	public void deleteFile(File file){
		
		if(file.isDirectory()){
			 
    		//directory is empty, then delete it
    		if(file.list().length==0){
 
    		   file.delete();
    		   System.out.println("Directory is deleted : " 
                                                 + file.getAbsolutePath());
 
    		}else{
 
    		   //list all the directory contents
        	   String files[] = file.list();
 
        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);
 
        	      //recursive delete
        	      deleteFile(fileDelete);
        	   }
 
        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
        	     System.out.println("Directory is deleted : " 
                                                  + file.getAbsolutePath());
        	   }
    		}
 
    	}else{
    		//if file, then delete it
    		file.delete();
    		System.out.println("File is deleted : " + file.getAbsolutePath());
    	}
		
	}
	
}
