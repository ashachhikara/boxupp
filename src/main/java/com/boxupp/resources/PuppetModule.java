package com.boxupp.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import com.boxupp.dao.PuppetModuleDAOManager;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.responseBeans.StatusBean;

@Path("/puppetModule/")
public class PuppetModule {
	@POST 
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean savePuppetModule(JsonNode newPuppetModuleData) {
		return PuppetModuleDAOManager.getInstance().create(newPuppetModuleData);
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PuppetModuleBean> getPuppetModules(@PathParam("id") String puppetModuleId) {
		return PuppetModuleDAOManager.getInstance().read(puppetModuleId);
	}
	
	@POST 
	@Path("/updatePuppetModule")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updatePuppetModule(JsonNode updatedPuppetModuleData) {
		return PuppetModuleDAOManager.getInstance().update(updatedPuppetModuleData);
	}
	
	@DELETE 
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deletePuppetModule(@PathParam("id") String puppetModuleId) {
		return PuppetModuleDAOManager.getInstance().delete(puppetModuleId);
	}
	
	@POST
	@Path("/linkModule")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean savePuppetMapping(JsonNode moduleMachineMapping){
		return PuppetModuleDAOManager.getInstance().linkModuleWithMachine(moduleMachineMapping);
	}
	
	@POST
	@Path("/deLinkModule")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteModuleMapping(JsonNode moduleMachineMapping) {
		return PuppetModuleDAOManager.getInstance().deLinkModuleWithMachine(moduleMachineMapping);
	}
	

}
