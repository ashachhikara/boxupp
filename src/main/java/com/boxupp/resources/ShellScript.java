package com.boxupp.resources;

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
@Path("/shellScript/")
public class ShellScript {
	@POST 
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveShellScript(JsonNode newShellScriptData) {
		return ShellScriptDAOManager.getInstance().create(newShellScriptData);
	}
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShellScriptBean getShellScripts(@PathParam("id") String shellScriptId) {
		return ShellScriptDAOManager.getInstance().read(shellScriptId);
	}
	
	@GET 
	@Path("/updateShellScript")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updateShellScript(JsonNode updatedShellScriptData) {
		
		return ShellScriptDAOManager.getInstance().update(updatedShellScriptData);
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteShellScript(@PathParam("id") String shellScriptId) {
		return ShellScriptDAOManager.getInstance().delete(shellScriptId);
	}
	
}
