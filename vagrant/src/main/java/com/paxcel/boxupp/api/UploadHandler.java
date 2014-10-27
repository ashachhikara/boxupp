package com.paxcel.boxupp.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.paxcel.boxupp.beans.BoxuppVMData;
import com.paxcel.boxupp.utilities.OSProperties;
import com.paxcel.boxupp.utilities.Utilities;
import com.paxcel.responseBeans.VagrantParserOutput;
import com.paxcel.trial.VagrantParser;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/uploadHandler")
public class UploadHandler {
	
	private static Logger logger = LogManager.getLogger(UploadHandler.class.getName());
	private static String selectedModule = "";
	private static boolean isModuleDefined;
	
	@POST
	@Path("/destination")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void setDestinationFolder(@Context HttpServletRequest request, @Context HttpServletResponse response){
		String destinationFolder = request.getParameter("loc");
		if(destinationFolder != null){
			UploadHandler.selectedModule = destinationFolder;
			UploadHandler.isModuleDefined = true;
		}else{
			UploadHandler.isModuleDefined = false;
		}
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("file") InputStream inputStream,
		@FormDataParam("file") FormDataContentDisposition file) throws IOException{

		String response = "";
		try {
			String fileSeparator = OSProperties.getInstance().getOSFileSeparator();
		  String moduleDirectoryPath = "";
		  if(UploadHandler.isModuleDefined){
			  moduleDirectoryPath = Utilities.getInstance().fetchActiveProjectDirectory()+
					  				fileSeparator+"modules"+fileSeparator+UploadHandler.selectedModule+fileSeparator+
					  				"files"+fileSeparator;
		  }else{
			  moduleDirectoryPath = Utilities.getInstance().fetchActiveProjectDirectory()+fileSeparator+"files"+fileSeparator;
		  }
		  new File(moduleDirectoryPath).mkdirs();
		  final String FILE_DESTINATION = moduleDirectoryPath + file.getFileName();
		  File f = new File(FILE_DESTINATION);
			   
		  OutputStream outputStream = new FileOutputStream(f);
		  int size = 0;
		  byte[] bytes = new byte[1024];
		  while ((size = inputStream.read(bytes)) != -1) {
		   outputStream.write(bytes, 0, size);
		  }
		  outputStream.flush();
		  outputStream.close();
		  response = "File uploaded " + FILE_DESTINATION;
		} catch (Exception e) {
		  e.printStackTrace();
		}
	  return Response.status(200).entity(response).build();
	}

	@POST
	@Path("/parseVagrantFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadVagrantFile(@FormDataParam("dataFile") InputStream inputStream,
		@FormDataParam("dataFile") FormDataContentDisposition file) throws IOException{
		
		VagrantParser parser = new VagrantParser();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String data = "";
			  data = br.readLine();
			  while(data != null){
				  parser.addData(data);
				  data = br.readLine();
			  }
			  br.close();
			} 
		catch (Exception e) {
			  e.printStackTrace();
		}
		BoxuppVMData vmData = new BoxuppVMData();
		vmData = parser.fetchConfigurations();
		Gson gSon = new Gson();
		String parsedOutput = gSon.toJson(vmData);
		VagrantParserOutput output = new VagrantParserOutput();
		if(parser.fetchConfigurations().getVmData().size()>0){
			output.setStatusCode(0);
			output.setValidVagrantFile(true);
			output.setParsedData(parsedOutput);
		}
		return gSon.toJson(output);
	}
}
