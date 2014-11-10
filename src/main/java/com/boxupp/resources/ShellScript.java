package com.boxupp.resources;

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
	public ShellScriptBean getScriptData(@PathParam("id") String shellScriptId) {
		return ShellScriptDAOManager.getInstance().read(shellScriptId);
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean updateShellScript(JsonNode shellScriptData) {
		return ShellScriptDAOManager.getInstance().update(shellScriptData);
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusBean deleteShellScript(@PathParam("id") String shellScriptId) {
		return ShellScriptDAOManager.getInstance().delete(shellScriptId);
	}
	@POST
	@Path("/updateScriptMapping/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public StatusBean saveScriptMapping(JsonNode shellScriptMapping){
		return ShellScriptDAOManager.getInstance().linkScriptMachine(shellScriptMapping);
	}
	
	@POST
	@Path("/updateScriptMappings")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Consumes("application/json")
	public StatusBean updateShellScriptMappings(JsonNode shellScriptMapping){
		return ShellScriptDAOManager.getInstance().updateScriptMapping(shellScriptMapping);
	}
}
