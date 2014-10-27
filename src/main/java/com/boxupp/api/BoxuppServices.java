package com.boxupp.api;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.ConfigurationGenerator;
import com.boxupp.FileManager;
import com.boxupp.PropertyReader;
import com.boxupp.VagrantOutputStream;
import com.boxupp.beans.SnapshotManager;
import com.boxupp.dao.MachineConfigDAOManager;
import com.boxupp.dao.ProjectDAOManager;
import com.boxupp.dao.PuppetModuleDAOManager;
import com.boxupp.dao.ShellScriptDAOManager;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.PuppetModuleMapping;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMapping;
import com.boxupp.responseBeans.BoxURLResponse;
import com.boxupp.responseBeans.ProjectConfig;
import com.boxupp.responseBeans.SnapshotData;
import com.boxupp.responseBeans.SnapshotStatus;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.responseBeans.VagrantFile;
import com.boxupp.responseBeans.VagrantFileStatus;
import com.boxupp.responseBeans.VagrantOutput;
import com.boxupp.responseBeans.VagrantStatus;
import com.boxupp.utilities.OSProperties;
import com.boxupp.utilities.Utilities;
import com.boxupp.windows.WindowsShellProcessor;
import com.boxupp.ws.OutputConsole;

@Path("/")
public class BoxuppServices {
	
	private static Logger logger = LogManager.getLogger(BoxuppServices.class.getName());
	
	@GET
	@Path("/selectProject")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean activateProject(@Context HttpServletRequest request){
		Integer projectID = Integer.parseInt(request.getParameter("projectID"));
		StatusBean statusBean = new StatusBean();
		System.out.println("Call received for project ID : "+projectID);
		if(Utilities.getInstance().changeActiveDirectory(projectID)){
			statusBean.setStatusCode(0);
		}else{
			statusBean.setStatusCode(1);
			statusBean.setStatusMessage("Project with ID "+projectID+" could not be made active;");
			logger.debug("Project with ID "+projectID+" could not be made active;");
		}
		return statusBean;
	}
	
	@GET
	@Path("/getStream")
	@Produces(MediaType.APPLICATION_JSON)
	public VagrantOutput getStream() throws IOException{
		return VagrantOutputStream.pop();
	}
	
	@GET
	@Path("/getVagrantFile")
	public VagrantFile getVagrantFile() throws IOException{
		FileManager manager = new FileManager();
		return manager.fetchVagrantFileData();
	}
	
	@POST
	@Path("/persistData")
	@Consumes(MediaType.APPLICATION_JSON)
	public SnapshotStatus persistData(JsonNode boxuppMappings) throws IOException{
		return SnapshotManager.persistBoxuppMappings(boxuppMappings);
	}
	
	@GET
	@Path("/retrieveData")
	@Produces(MediaType.APPLICATION_JSON)
	public SnapshotData retrieveFormSnapshot() throws ClassNotFoundException, IOException{
		return SnapshotManager.retrieveBoxuppMappings();
	}

/*	@POST
	@Path("/saveAsFile")
	@Consumes(MediaType.APPLICATION_JSON)
	public VagrantFileStatus saveAsFile(JsonNode mappings) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./trial")));
		String data = mappings.get("shellScripts").get(0).get("scriptSource").getValueAsText().replaceAll("\n","\r\n"); 
		bw.write(data);
		bw.close();
		BoxuppVMData boxuppVMData = new BoxuppVMData();
		boxuppVMData = Utilities.getInstance().populateVMDataBean(mappings);
		
		BoxuppScriptsData boxuppScripts = new BoxuppScriptsData();
		boxuppScripts = Utilities.getInstance().populateScriptsBean(mappings);
		
		BoxuppPuppetData boxuppPuppetData = new BoxuppPuppetData();
		boxuppPuppetData = Utilities.getInstance().populatePuppetData(mappings.get("puppetData"));
		
		PuppetUtilities.getInstance().commitPuppetDataToDisk(boxuppPuppetData);
		//Utilities.getInstance().commitScriptsToDisk(boxuppScripts);
		Utilities.getInstance().commitFoldersToDisk(boxuppVMData);
		
		boolean configFileData = ConfigurationGenerator.generateConfig(boxuppVMData);
		VagrantFileStatus fileStatus = new VagrantFileStatus();
		
		if(configFileData){
			logger.info("Started saving vagrant file");
			FileManager fileManager = new FileManager();
			String renderedTemplate = ConfigurationGenerator.getVelocityFinalTemplate();
			fileStatus = fileManager.writeFileToDisk(renderedTemplate);
			logger.info("Vagrant file save completed");
		}
		else{
			logger.info("Failed to save vagrant file !!");
		}
		persistData(mappings);
		return fileStatus;
	}*/
	
	@POST
	@Path("/saveAsFile")
	@Consumes(MediaType.APPLICATION_JSON)
	public VagrantFileStatus saveAsFile(String projectId) throws IOException
	{
		
		List<MachineConfigurationBean>  machineConfigList = MachineConfigDAOManager.getInstance().read(projectId);
		List<PuppetModuleBean>  puppetModuleList = PuppetModuleDAOManager.getInstance().read(projectId);
		List<ShellScriptBean> shellScriptList = ShellScriptDAOManager.getInstance(). read(projectId);
		List<ShellScriptMapping> shellScriptMappingList = null;
		List<PuppetModuleMapping> puppetModuleMappingList = null;
		try {
			shellScriptMappingList = ShellScriptDAOManager.getInstance().shellScriptMappingDao.queryForEq("project", ProjectDAOManager.getInstance().projectDao.queryForId(Integer.parseInt(projectId)));
		
			puppetModuleMappingList = PuppetModuleDAOManager.getInstance().puppetModuleMappingDao.queryForEq("project",ProjectDAOManager.getInstance().projectDao.queryForId(Integer.parseInt(projectId)));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String provider  = ProjectDAOManager.getInstance().getProviderForProject(projectId);
	
		Utilities.getInstance().commitSyncFoldersToDisk(machineConfigList);
		boolean configFileData = ConfigurationGenerator.generateConfig(machineConfigList, puppetModuleList,  shellScriptList, shellScriptMappingList, puppetModuleMappingList, provider );
		VagrantFileStatus fileStatus = new VagrantFileStatus();
		
		if(configFileData){
			logger.info("Started saving vagrant file");
			FileManager fileManager = new FileManager();
			String renderedTemplate = ConfigurationGenerator.getVelocityFinalTemplate();
			fileStatus = fileManager.writeFileToDisk(renderedTemplate);
			logger.info("Vagrant file save completed");
		}
		else{
			logger.info("Failed to save vagrant file !!");
		}
		//persistData(mappings);
		return fileStatus;
	}
	
	@GET
	@Path("/boxupp")
	public String runVagrantFile(@Context HttpServletRequest request) throws IOException, InterruptedException{
		WindowsShellProcessor shellProcessor = new WindowsShellProcessor();
		String location = Utilities.getInstance().fetchActiveProjectDirectory();
		String command = request.getParameter("command");
		if(command.toLowerCase(Locale.ENGLISH).indexOf("vagrant")!= -1 ){
			shellProcessor.executeVagrantFile(location,command, new VagrantOutputStream());
		}else{
			OutputConsole console = new VagrantOutputStream();
			console.pushError("Not a valid Vagrant command");
			console.pushDataTermination();
		}
		return "";
	}

	@GET
	@Path("/projectConfig")
	@Produces(MediaType.APPLICATION_JSON)
	
	public ProjectConfig checkProjectConfig(@Context HttpServletRequest request,@Context HttpServletResponse response){
		ProjectConfig config = new ProjectConfig();
		String port = PropertyReader.getInstance().getProperty("port");
		if(port != null){
			config.setProjectPort(Integer.parseInt(port));
		}else{
			config.setProjectPort(8585);
		}
		return config;
	}

	@GET
	@Path("/checkVagrantStatus")
	@Produces(MediaType.APPLICATION_JSON)
	
	public VagrantStatus checkVagrantStatus(){
		WindowsShellProcessor shellProcessor = new WindowsShellProcessor();
		String location = Utilities.getInstance().fetchActiveProjectDirectory();
		return shellProcessor.checkVagrantStatus(location);
		
	}
	
	@GET
	@Path("/checkURL")
	@Produces(MediaType.APPLICATION_JSON)
	
	public BoxURLResponse checkDownloadURL(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		
		BoxURLResponse urlResponse = null;
		String url = request.getParameter("boxURL");
		try{
			
			URL boxURL = new URL(url);
			HttpURLConnection boxURLConnection = (HttpURLConnection) boxURL.openConnection();
	
			urlResponse = new BoxURLResponse();
			urlResponse.setContentLength(boxURLConnection.getContentLength());
			urlResponse.setStatusCode(boxURLConnection.getResponseCode());
			return urlResponse;
		}
		catch(Exception e){
			urlResponse = new BoxURLResponse();
			urlResponse.setContentLength(0);
			urlResponse.setStatusCode(-1);
		}
		return urlResponse;
	}
	@GET
	@Path("/downloadPuppetModule")
	@Produces(MediaType.APPLICATION_JSON)
	public String dowunloadPuppetModule(@Context HttpServletRequest request) {
		String fileURL = request.getParameter("fileUrl");
		URL url = null;
		int responseCode = 0;
		HttpURLConnection httpConn = null;
		String fileSeparator = OSProperties.getInstance().getOSFileSeparator();
		String saveDir = Utilities.getInstance().fetchActiveProjectDirectory()
				+ fileSeparator + request.getParameter("loc") + fileSeparator;
		try {
			url = new URL(fileURL);
			httpConn = (HttpURLConnection) url.openConnection();
			responseCode = httpConn.getResponseCode();

			// always check HTTP response code first
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String fileName = "";
				String disposition = httpConn
						.getHeaderField("Content-Disposition");
				String contentType = httpConn.getContentType();
				int contentLength = httpConn.getContentLength();

				if (disposition != null) {
					// extracts file name from header field
					int index = disposition.indexOf("filename=");
					if (index > 0) {
						fileName = disposition.substring(index + 9,
								disposition.length());

					}
				} else {
					// extracts file name from URL
					fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
							fileURL.length());
				}

				// opens input stream from the HTTP connection
				InputStream inputStream = httpConn.getInputStream();
				String saveFilePath = saveDir + fileName;

				// opens an output stream to save into file
				FileOutputStream outputStream = new FileOutputStream(
						saveFilePath);

				int bytesRead = -1;
				byte[] buffer = new byte[4096];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}

				outputStream.close();
				inputStream.close();
				
				System.out.println("File downloaded");
				extrectFile(saveFilePath, saveDir);
				File file = new File(saveFilePath);
				file.delete();
			
			} else {
				System.out
						.println("No file to download. Server replied HTTP code: "
								+ responseCode);
			}
			httpConn.disconnect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;

	}

	private void extrectFile(String saveFilePath, String saveDir) {
		/** create a TarArchiveInputStream object. **/
		try {
			File file = new File(saveFilePath);
			FileInputStream fin = new FileInputStream(file);

			BufferedInputStream in = new BufferedInputStream(fin);

			GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);

			TarArchiveInputStream tarIn = new TarArchiveInputStream(gzIn);

			TarArchiveEntry entry = null;

			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {

				System.out.println("Extracting: " + entry.getName());

				/** If the entry is a directory, create the directory. **/

				if (entry.isDirectory()) {

					File f = new File(saveDir + entry.getName());

					f.mkdirs();

				} else {

					int count;
					byte data[] = new byte[4096];
					FileOutputStream fos;

					fos = new FileOutputStream(saveDir + entry.getName());
					BufferedOutputStream dest = new BufferedOutputStream(fos,
							4096);
					while ((count = tarIn.read(data, 0, 4096)) != -1) {
						dest.write(data, 0, count);
					}
					dest.close();

					/** Close the input stream **/
				}
			}
			tarIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("untar completed successfully!!");

	}

}
