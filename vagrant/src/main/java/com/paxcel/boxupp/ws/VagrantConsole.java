package com.paxcel.boxupp.ws;

import java.io.IOException;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.paxcel.boxupp.VagrantOutputStream;
import com.paxcel.boxupp.utilities.Utilities;
import com.paxcel.boxupp.windows.WindowsShellProcessor;
import com.paxcel.responseBeans.VagrantOutput;
import com.paxcel.responseBeans.VagrantStreamError;
import com.paxcel.responseBeans.VagrantStreamOutput;

@WebSocket
public class VagrantConsole implements OutputConsole{
	
	private static Logger logger = LogManager.getLogger(VagrantConsole.class.getName());
	private RemoteEndpoint remote;
	private static Gson gson = new Gson();
	
	@OnWebSocketConnect
    public void onConnect(Session session) {
		logger.info(session.getRemote()+ " : connected");
		this.remote = session.getRemote();
    }

	@OnWebSocketMessage
    public void onMessage(Session session,String command) throws IOException, InterruptedException {
        WindowsShellProcessor shellProcessor = new WindowsShellProcessor();
		String location = Utilities.getInstance().fetchActiveProjectDirectory();
        if(command.toLowerCase(Locale.ENGLISH).indexOf("vagrant")!= -1 ){
			shellProcessor.executeVagrantFile(location,command,this);
		}else{
			this.pushError("Not a valid Vagrant command");
			this.pushDataTermination();
		}
        
    }
	
	@OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        logger.info("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        logger.error("Error: " + t.getMessage());
    }

	@Override
	public void pushOutput(String data) {
		
		VagrantStreamOutput vagrantStreamOutput = new VagrantStreamOutput();
		vagrantStreamOutput.setOutput(data);
		commitOutput(vagrantStreamOutput);
	}

	@Override
	public void pushError(String data) {
		
		VagrantStreamError vagrantStreamError = new VagrantStreamError();
		vagrantStreamError.setOutput(data);
		commitOutput(vagrantStreamError);
	}

	@Override
	public void pushDataTermination() {
		
		VagrantStreamOutput vagrantStreamOutput = new VagrantStreamOutput();
		vagrantStreamOutput.setOutput("Execution Completed");
		vagrantStreamOutput.setDataEnd(true);
		commitOutput(vagrantStreamOutput);
	}
	
	public void commitOutput(VagrantOutput output){
		try{
			remote.sendString(gson.toJson(output));
		}
		catch(IOException e){
			logger.error("Error committing output to console : "+e.getMessage());
		}
	}

}
