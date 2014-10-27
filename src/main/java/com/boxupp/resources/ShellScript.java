package com.boxupp.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import com.boxupp.dao.ShellScriptDAOManager;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.responseBeans.StatusBean;

public class ShellScript {
	@POST 
	@Path("/shellScript")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveShellScript(JsonNode newShellScriptData) {
		return ShellScriptDAOManager.getInstance().create(newShellScriptData);
	}
	@GET
	@Path("/shellScript/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShellScriptBean> getShellScripts(@PathParam("id") String projectId) {
		return ShellScriptDAOManager.getInstance().read(projectId);
	}
	
	@GET 
	@Path("/updateShellScript")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updateShellScript(JsonNode updatedShellScriptData) {
		
		return ShellScriptDAOManager.getInstance().update(updatedShellScriptData);
	}
	
	@DELETE
	@Path("/deleteShellScript/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteShellScript(@PathParam("id") String shellScriptId) {
		return ShellScriptDAOManager.getInstance().delete(shellScriptId);
	}
	
}
