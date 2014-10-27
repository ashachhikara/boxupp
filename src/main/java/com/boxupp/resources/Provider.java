package com.boxupp.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.boxupp.dao.ProviderDAOManager;
import com.boxupp.db.beans.ProviderBean;

@Path("/provider/")
public class Provider {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProviderBean> getAllProviders(){
		return ProviderDAOManager.getInstance().readAllDB();
	}
	
	@GET
	@Path("/providers/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProviderBean getProviderData(@PathParam("id") String providerID){
		return ProviderDAOManager.getInstance().readProvider(providerID);
		
		
	}
}
