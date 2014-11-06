package com.boxupp.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.boxupp.beans.BoxuppPuppetData;
import com.boxupp.beans.PuppetManifestFileBean;
import com.boxupp.beans.PuppetModuleBean;
import com.boxupp.beans.PuppetModuleFileBean;
import com.boxupp.beans.PuppetModuleFolderBean;
import com.boxupp.responseBeans.StatusBean;


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
	
	public StatusBean downloadModules(String fileURL){
		StatusBean statusBean = new StatusBean();

		URL url = null;
		int responseCode = 0;
		HttpURLConnection httpConn = null;
		String fileSeparator = OSProperties.getInstance().getOSFileSeparator();
		String moduleDirPath = OSProperties.getInstance().getUserHomeDirectory()+fileSeparator
				+OSProperties.getInstance().getPrimaryFolderName()+fileSeparator
				+OSProperties.getInstance().getModuleDirName()+fileSeparator;
		checkIfDirExists(new File(moduleDirPath));
		try {
			url = new URL(fileURL);
			httpConn = (HttpURLConnection) url.openConnection();
			responseCode = httpConn.getResponseCode();

			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String fileName = "";
				String disposition = httpConn.getHeaderField("Content-Disposition");
				String contentType = httpConn.getContentType();
				int contentLength = httpConn.getContentLength();

				if (disposition != null) {
					int index = disposition.indexOf("filename=");
					if (index > 0) {
						fileName = disposition.substring(index + 9,
								disposition.length());
					}
				} else {
					fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
				}
				InputStream inputStream = httpConn.getInputStream();
				String saveFilePath = moduleDirPath + fileName;
				FileOutputStream outputStream = new FileOutputStream(saveFilePath);

				int bytesRead = -1;
				byte[] buffer = new byte[4096];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				outputStream.close();
				inputStream.close();
				extrectFile(saveFilePath, moduleDirPath);
				File file = new File(saveFilePath);
				file.delete();
			
			} else {
				logger.error("No file to download. Server replied HTTP code: "+ responseCode);
			}
			httpConn.disconnect();
			statusBean.setStatusCode(0);
			statusBean.setStatusMessage(" Module Downloaded successfully ");
		} catch (IOException e) {
			logger.error("Error in loading module :" +e.getMessage());
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Error in loading module :" +e.getMessage());
		}
		return statusBean;
	}
	
	private void extrectFile(String saveFilePath, String moduleDirPath) {
		try {
			File file = new File(saveFilePath);
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fin);
			GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
			TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);
			TarArchiveEntry entry = null;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					File f = new File(moduleDirPath + entry.getName());
					f.mkdirs();
				} else {
					int count;
					byte data[] = new byte[4096];
					FileOutputStream fos;
					fos = new FileOutputStream(moduleDirPath + entry.getName());
					BufferedOutputStream dest = new BufferedOutputStream(fos, 4096);
					while ((count = tarIn.read(data, 0, 4096)) != -1) {
						dest.write(data, 0, count);
					}
					dest.close();
				}
			}
			tarIn.close();
		} catch (IOException e) {
			logger.error("Error in unzip the module file :"+e.getMessage());
		}
	}
}




















