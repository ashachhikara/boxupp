package com.boxupp.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.boxupp.dao.LoginDAOManager;
import com.boxupp.dao.MachineConfigDAOManager;
import com.boxupp.dao.ProjectDAOManager;
import com.boxupp.dao.ProviderDAOManager;
import com.boxupp.dao.PuppetModuleDAOManager;
import com.boxupp.dao.ShellScriptDAOManager;
import com.boxupp.dao.UserDAOManager;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.ProviderBean;
import com.paxcel.responseBeans.StatusBean;

@Path("/resources/")
public class Resources {
	
	private static Logger logger = LogManager.getLogger(Resources.class.getName());
	
	
	
	@POST 
	@Path("/saveMachineConfig/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveMachineConfiguration(JsonNode mappings) {
		return MachineConfigDAOManager.getInstance().create(mappings);
	}
	
	@GET 
	@Path("/updateMachineConfig")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updateMachineConfiguration( JsonNode updatedmachineConfigData) {
		return MachineConfigDAOManager.getInstance().update(updatedmachineConfigData);
	}
	
	@GET 
	@Path("/deleteMachineConfig/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteMachineConfiguration(@PathParam("id") String machineID) {
		return MachineConfigDAOManager.getInstance().delete(machineID);
	}
	
	@GET 
	@Path("/saveShellScript")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveShellScript(JsonNode newShellScriptData) {
		return ShellScriptDAOManager.getInstance().create(newShellScriptData);
	}
	
	@GET 
	@Path("/updateShellScript")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updateShellScript(JsonNode updatedShellScriptData) {
		
		return ShellScriptDAOManager.getInstance().update(updatedShellScriptData);
	}
	
	@GET 
	@Path("/deleteShellScript/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteShellScript(@PathParam("id") String shellScriptId) {
		return ShellScriptDAOManager.getInstance().delete(shellScriptId);
	}
	
	@GET 
	@Path("/savePuppetModule")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean savePuppetModule(JsonNode newPuppetModuleData) {
		return PuppetModuleDAOManager.getInstance().create(newPuppetModuleData);
	}
	
	@GET 
	@Path("/updatePuppetModule")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updatePuppetModule(JsonNode updatedPuppetModuleData) {
		
		return PuppetModuleDAOManager.getInstance().update(updatedPuppetModuleData);
	}
	
	@GET 
	@Path("/deletePuppetModule/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deletePuppetModule(@PathParam("id") String puppetModuleId) {
		return PuppetModuleDAOManager.getInstance().delete(puppetModuleId);
	}
	

	
}
