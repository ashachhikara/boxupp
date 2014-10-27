package com.boxupp.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.boxupp.beans.BoxuppPuppetData;
import com.boxupp.beans.PuppetManifestFileBean;
import com.boxupp.beans.PuppetModuleBean;
import com.boxupp.beans.PuppetModuleFileBean;
import com.boxupp.beans.PuppetModuleFolderBean;


public class PuppetUtilities extends Utilities {
	
	private static Logger logger = LogManager.getLogger(PuppetUtilities.class.getName());
	private static PuppetUtilities puppetUtilities = null;
	private static String OSFileSeparator;
	
	private PuppetUtilities(){
		OSFileSeparator = osProperties.getOSFileSeparator();
	}
	
	public static PuppetUtilities getInstance(){
		if(puppetUtilities == null){
			try{
				puppetUtilities = new PuppetUtilities();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return puppetUtilities;
	}
	
	public void commitPuppetDataToDisk(BoxuppPuppetData puppetData){
		commitManifestsToDisk(puppetData.getManifests());
		commitModulesToDisk(puppetData.getModules());
		createFilesFolderOnDisk();
	}
	
	public void commitManifestsToDisk(ArrayList<PuppetManifestFileBean> manifestBeansList){
//		String fileSeparator = OSProperties.getInstance().getOSFileSeparator();
		String manifestsDir = fetchActiveProjectDirectory() + OSFileSeparator +"manifests" + OSFileSeparator;
		checkIfDirExists(new File(manifestsDir));
		PuppetManifestFileBean manifestFile = null;
		try{
			for(int counter = 0 ; counter<manifestBeansList.size(); counter++){
				 manifestFile = manifestBeansList.get(counter);
				 BufferedWriter manifestsWriter = new BufferedWriter(new FileWriter(new File(
						 							manifestsDir + manifestFile.getMoFileName())));
				 manifestsWriter.write(manifestFile.getMoFileSource());
				 manifestsWriter.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
			logger.error("Error writing manifests : " + manifestFile.getMoFileName() + " : "+e.getMessage());
		}
	}
	
	public void createFilesFolderOnDisk(){
		String filesDir = fetchActiveProjectDirectory() + OSFileSeparator + "files" + OSFileSeparator;
		File directory = new File(filesDir);
		if(!directory.exists()){
			directory.mkdir();
		}
	}
	
	public void commitModulesToDisk(ArrayList<PuppetModuleBean> modulesList){
		String modulesDir = fetchActiveProjectDirectory() + OSFileSeparator + "modules" + OSFileSeparator;
		checkIfDirExists(new File(modulesDir));
		PuppetModuleBean moduleBean = null;
		try{
			for(int counter = 0 ; counter<modulesList.size(); counter++){
				 moduleBean = modulesList.get(counter);
				 String modulePathName = modulesDir + moduleBean.getMoName(); 
				 new File(modulePathName).mkdir();
				 if(moduleBean.getMoFolders().size()>0){
					 commitModuleFolderToDisk(modulePathName,moduleBean.getMoFolders());
				 }
			}
		}
		catch(IOException e){
			logger.error("Error writing modules : " + moduleBean.getMoName() + " : "+e.getMessage());
		}
	}
	
	public void commitModuleFolderToDisk(String parentFolder,ArrayList<PuppetModuleFolderBean> folderBeanList) throws IOException{
		
		for(int counter=0; counter<folderBeanList.size(); counter++){
			PuppetModuleFolderBean folderBean = folderBeanList.get(counter);
			String folderDirectory = parentFolder + OSFileSeparator +  folderBean.getMoFolderName(); 
			checkIfDirExists(new File(folderDirectory));
			ArrayList<PuppetModuleFileBean> moduleFiles = folderBean.getMoFolderFiles();
			for(int fileCounter = 0; fileCounter < moduleFiles.size(); fileCounter++){
				PuppetModuleFileBean fileBean = moduleFiles.get(fileCounter);
				if(!fileBean.isPreventModifications()){
					BufferedWriter moduleFileWriter = new BufferedWriter(new FileWriter(new File(
								folderDirectory + OSFileSeparator + fileBean.getMoFileName())));
					moduleFileWriter.write(fileBean.getMoFileSource());
					moduleFileWriter.close();
				}
			}
			if(folderBean.getMoFolders().size() > 0){
				String childFoldersDirectory = folderBean.getMoFolderName();
				commitModuleFolderToDisk(folderDirectory,folderBean.getMoFolders());
			}
		}
	}
	
	public void deletePuppetModule(String puppetModuleName){
		String modulesDir = fetchActiveProjectDirectory() + OSFileSeparator + "modules" + OSFileSeparator;
		File file  = new File(modulesDir+puppetModuleName);
		Utilities.getInstance().deleteFile(file);
		
	}
	
	
}




















