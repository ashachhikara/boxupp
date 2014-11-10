package com.boxupp.api;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.boxupp.ConfigurationGenerator;
import com.boxupp.FileManager;
import com.boxupp.PropertyReader;
import com.boxupp.VagrantOutputStream;
import com.boxupp.dao.MachineConfigDAOManager;
import com.boxupp.dao.ProjectDAOManager;
import com.boxupp.dao.PuppetModuleDAOManager;
import com.boxupp.dao.ShellScriptDAOManager;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.PuppetModuleMapping;
import com.boxupp.db.beans.SearchModuleBean;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMapping;
import com.boxupp.responseBeans.BoxURLResponse;
import com.boxupp.responseBeans.ProjectConfig;
import com.boxupp.responseBeans.StatusBean;
import com.boxupp.responseBeans.VagrantFileStatus;
import com.boxupp.responseBeans.VagrantOutput;
import com.boxupp.responseBeans.VagrantStatus;
import com.boxupp.utilities.PuppetUtilities;
import com.boxupp.utilities.Utilities;
import com.boxupp.windows.WindowsShellProcessor;
import com.boxupp.ws.OutputConsole;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/")
public class BoxuppServices {
	
	private static Logger logger = LogManager.getLogger(BoxuppServices.class.getName());
	
	@GET
	@Path("/selectProject")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean activateProject(@Context HttpServletRequest request){
		Integer projectID = Integer.parseInt(request.getParameter("projectID"));
		Integer userID = Integer.parseInt(request.getParameter("userID"));
		StatusBean statusBean = new StatusBean();
		System.out.println("Call received for project ID : "+projectID);
		Utilities.getInstance().changeActiveDirectory(userID,projectID);
		statusBean.setStatusCode(0);
		return statusBean;
	}
	
	@GET
	@Path("/getStream")
	@Produces(MediaType.APPLICATION_JSON)
	public VagrantOutput getStream() throws IOException{
		return VagrantOutputStream.pop();
	}
	
	/*@GET
	@Path("/getVagrantFile")
	public VagrantFile getVagrantFile() throws IOException{
		FileManager manager = new FileManager();
		return manager.fetchVagrantFileData();
	}*/
	
	/*@POST
	@Path("/persistData")
	@Consumes(MediaType.APPLICATION_JSON)
	public SnapshotStatus persistData(JsonNode boxuppMappings) throws IOException{
		return SnapshotManager.persistBoxuppMappings(boxuppMappings);
	}*/
	
	/*@GET
	@Path("/retrieveData")
	@Produces(MediaType.APPLICATION_JSON)
	public SnapshotData retrieveFormSnapshot() throws ClassNotFoundException, IOException{
		return SnapshotManager.retrieveBoxuppMappings();
	}*/

	/*@POST
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
	}
	*/
	
	@POST
	@Path("/saveAsFile")
	@Consumes(MediaType.APPLICATION_JSON)
	public VagrantFileStatus saveAsFile(@Context HttpServletRequest request) throws IOException
	{
		String projectID = request.getParameter("projectID");
		String userID = request.getParameter("userID");
		List<MachineConfigurationBean>  machineConfigList = MachineConfigDAOManager.getInstance().read(projectID);
		List<PuppetModuleBean>  puppetModuleList = PuppetModuleDAOManager.getInstance().read(projectID);
		List<ShellScriptBean> shellScriptList = ShellScriptDAOManager.getInstance(). read(projectID);
		List<ShellScriptMapping> shellScriptMappingList = null;
		List<PuppetModuleMapping> puppetModuleMappingList = null;
		try {
			shellScriptMappingList = ShellScriptDAOManager.getInstance().shellScriptMappingDao.queryForEq("project", ProjectDAOManager.getInstance().projectDao.queryForId(Integer.parseInt(projectID)));
		
			puppetModuleMappingList = PuppetModuleDAOManager.getInstance().puppetModuleMappingDao.queryForEq("project",ProjectDAOManager.getInstance().projectDao.queryForId(Integer.parseInt(projectID)));
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		String provider  = ProjectDAOManager.getInstance().getProviderForProject(projectID);
	
		Utilities.getInstance().commitSyncFoldersToDisk(machineConfigList, Integer.parseInt(userID));
		boolean configFileData = ConfigurationGenerator.generateConfig(machineConfigList, puppetModuleList,  shellScriptList, shellScriptMappingList, puppetModuleMappingList, provider );
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
	
	@GET
	@Path("/boxupp")
	public String runVagrantFile(@Context HttpServletRequest request) throws IOException, InterruptedException{
		Integer userID = Integer.parseInt(request.getParameter("userID"));
		WindowsShellProcessor shellProcessor = new WindowsShellProcessor();
		String location = Utilities.getInstance().fetchActiveProjectDirectory(userID);
		String command = request.getParameter("command");
		if(command.toLowerCase(Locale.ENGLISH).indexOf("vagrant")!= -1 ){
			shellProcessor.executeVagrantFile(location,command,userID, new VagrantOutputStream());
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
	
	public VagrantStatus checkVagrantStatus(@Context HttpServletRequest request){
		Integer userID = Integer.parseInt(request.getParameter("userID"));
		WindowsShellProcessor shellProcessor = new WindowsShellProcessor();
		String location = Utilities.getInstance().fetchActiveProjectDirectory(userID);
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
	
}
