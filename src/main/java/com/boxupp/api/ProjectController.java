package com.boxupp.api;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.boxupp.VagrantOutputStream;
import com.boxupp.responseBeans.VagrantOutput;

@Path("/project/")
public class ProjectController {
	
	@GET
	@Path("/getStream")
	@Produces(MediaType.APPLICATION_JSON)
	public VagrantOutput getStream() throws IOException{
		return VagrantOutputStream.pop();
	}

}
