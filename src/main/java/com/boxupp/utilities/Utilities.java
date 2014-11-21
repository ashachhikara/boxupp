package com.boxupp.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.ConfigurationGenerator;
import com.boxupp.FileManager;
import com.boxupp.beans.BoxuppPuppetData;
import com.boxupp.beans.BoxuppScriptsData;
import com.boxupp.beans.BoxuppVMData;
import com.boxupp.dao.MachineConfigDAOManager;
import com.boxupp.dao.ProjectDAOManager;
import com.boxupp.dao.PuppetModuleDAOManager;
import com.boxupp.dao.ShellScriptDAOManager;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.PuppetModuleMapping;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMapping;
import com.boxupp.db.beans.SyncFoldersBean;
import com.boxupp.responseBeans.VagrantFileStatus;
import com.google.gson.Gson;

public class Utilities { 
	
	private static Logger logger = LogManager.getLogger(Utilities.class.getName());
	private static Utilities utilities = null;
	private String activeProjectDirectory = "";
	public OSProperties osProperties = null;
	private static HashMap<Integer,Integer> activeMappings = null;
	
	public Utilities(){
		osProperties = OSProperties.getInstance();
		activeMappings = new HashMap<Integer,Integer>();
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
	
	public BoxuppPuppetData populatePuppetData (JsonNode mappings){
		Gson puppetConfigurations = new Gson();
		BoxuppPuppetData boxuppPuppetData = new BoxuppPuppetData();
		boxuppPuppetData = puppetConfigurations.fromJson(mappings.toString(), BoxuppPuppetData.class);
		return boxuppPuppetData;
	}
	
	public boolean createBoxuppFolderIfNotExists(){
		String boxuppDirPath = osProperties.getUserHomeDirectory() + 
				 osProperties.getOSFileSeparator() + "Boxupp";
		File boxuppDir = new File(boxuppDirPath);
		if(!boxuppDir.exists()){
			boxuppDir.mkdirs();
		}
		return true;
	}
	public void initializeDirectory(Integer projectID){
		
		String userHomeDir = osProperties.getUserHomeDirectory() + 
							 osProperties.getOSFileSeparator() + "Boxupp" + 
							 osProperties.getOSFileSeparator() + projectID;
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
	/*
	public void commitFoldersToDisk(BoxuppVMData vmData){
		ArrayList<VMConfiguration> vmConfigurations = vmData.getVmData();
		for(int counter=0; counter<vmConfigurations.size(); counter++){
			ArrayList<SyncFolderMapping> folderMappings = vmConfigurations.get(counter).getSyncFolders();
			for(int folderCounter = 0 ; folderCounter<folderMappings.size(); folderCounter++){
				createFolderOnDisk(folderMappings.get(folderCounter).getHostFolder());
			}
		}
	}*/
	public void commitSyncFoldersToDisk(List<MachineConfigurationBean> machineConfigs, Integer userID){
	
		for(MachineConfigurationBean machineConfig : machineConfigs){
			ArrayList<SyncFoldersBean> syncFolders = null;
			try {
				syncFolders = machineConfig.getSyncFolders();
			} catch (SQLException e) {
				logger.error("Error getting syncFolders : "+e.getMessage());
			}
			for(SyncFoldersBean syncFolder : syncFolders){
				createFolderOnDisk(syncFolder.getHostFolder(),userID);
			}
		}
	}
	public void createFolderOnDisk(String folderName, Integer userID){
		String folderLocation = fetchActiveProjectDirectory(userID) + osProperties.getOSFileSeparator() + folderName; 
		File directory = new File(folderLocation);
		if(!directory.exists()){
			directory.mkdir();
		}
	}
	
	public void writeScriptToDisk(ShellScriptBean scriptBean, Integer userID){
		String scriptsDir = fetchActiveProjectDirectory(userID)+osProperties.getOSFileSeparator()
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
	public void updateScriptData(ShellScriptBean scriptBean, Integer userID){
		String scriptsDir = fetchActiveProjectDirectory(userID) + osProperties.getOSFileSeparator()
				+osProperties.getScriptsDirName()+osProperties.getOSFileSeparator();
		checkIfDirExists(new File(scriptsDir));
		try{
			deleteFile(new File(scriptsDir + scriptBean.getScriptName()));
			BufferedWriter scriptWriter = new BufferedWriter(new FileWriter(new File(scriptsDir + scriptBean.getScriptName())));
			scriptWriter.write(scriptBean.getScriptContent());
			scriptWriter.close();
		}
		catch(IOException e){
			logger.error("Error updating script : "+scriptBean.getScriptName() + " : "+e.getMessage());
		}
	}
	
	public String fetchActiveProjectDirectory(Integer userID){
		/*if(activeProjectDirectory.isEmpty()){
			initializeDirectory();
		}
		return activeProjectDirectory;*/
		return constructProjectDirectory(activeMappings.get(userID));
	}
	
	public String constructProjectDirectory(Integer projectID){
		return  osProperties.getUserHomeDirectory() + 
				osProperties.getOSFileSeparator() +
				"Boxupp" + osProperties.getOSFileSeparator() + projectID;
	}
	
	public void changeActiveDirectory(Integer userID, Integer projectID){
		activeMappings.put(userID, projectID);
		/*OSProperties osProperties = OSProperties.getInstance();
		try {
			String projectName = DAOProvider.getInstance().fetchProjectDao().queryForId(projectID).getName();
			activeProjectDirectory = osProperties.getUserHomeDirectory() + 
									 osProperties.getOSFileSeparator() +
									osProperties.getPrimaryFolderName() +
									osProperties.getOSFileSeparator() + projectName;
		} catch (SQLException e) {
			logger.error("Error setting the active project directory : "+e.getMessage());
			return false;
		}
		return true;*/
	}
	
	public void checkIfDirExists(File dirLocation){
		if(!dirLocation.isDirectory()){
			dirLocation.mkdirs();
		}
	}
	
	public void deleteScriptfileOnDisk(String fileName, Integer userID){
		String scriptFilepath = fetchActiveProjectDirectory(userID) + osProperties.getOSFileSeparator()
				+osProperties.getScriptsDirName()+osProperties.getOSFileSeparator()+fileName;
		File file  = new File(scriptFilepath);
		deleteFile(file);
    
	}
	
	public void deleteProjectFile(Integer projectID){
		String projectDir = constructProjectDirectory(projectID);
		File file  = new File(projectDir);
		deleteFile(file);
	}
	
	
	public void deleteFile(File file){
		
		if(file.isDirectory()){
			 
    		//directory is empty, then delete it
    		if(file.list().length==0){
 
    		   file.delete();
    		   System.out.println("Directory is deleted : " + file.getAbsolutePath());
 
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
	public VagrantFileStatus saveVagrantFile(JsonNode vargantFileData){
		String projectID = vargantFileData.get("projectID").getTextValue();
		String userID = vargantFileData.get("userID").getTextValue();
		List<MachineConfigurationBean>  machineConfigList = MachineConfigDAOManager.getInstance().retireveBoxesForProject(projectID);
		List<PuppetModuleBean>  puppetModuleList = PuppetModuleDAOManager.getInstance().retireveModulesForProject(projectID);
		List<ShellScriptBean> shellScriptList = ShellScriptDAOManager.getInstance().retireveScriptsForProject(projectID);
		List<ShellScriptMapping> shellScriptMappingList = ProjectDAOManager.getInstance().retireveScriptsMapping(projectID);
		List<PuppetModuleMapping> puppetModuleMappingList = ProjectDAOManager.getInstance().retireveModulesMapping(projectID);
		String provider  = ProjectDAOManager.getInstance().getProviderForProject(projectID);
		Utilities.getInstance().commitSyncFoldersToDisk(machineConfigList, Integer.parseInt(userID));
		boolean configFileData = ConfigurationGenerator.generateConfig(machineConfigList, puppetModuleList,  shellScriptList, shellScriptMappingList, puppetModuleMappingList, provider, projectID);
		VagrantFileStatus fileStatus = new VagrantFileStatus();
		if(configFileData){
			logger.info("Started saving vagrant file");
			FileManager fileManager = new FileManager();
			String renderedTemplate = ConfigurationGenerator.getVelocityFinalTemplate();
			fileStatus = fileManager.writeFileToDisk(renderedTemplate, Integer.parseInt(userID));
			logger.info("Vagrant file save completed");
		}
		else{
			logger.info("Failed to save vagrant file !!");
		}
		//persistData(mappings);
		return fileStatus;
	}
	
}
