package com.paxcel.boxupp;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.paxcel.boxupp.api.AppDataUpdater;
import com.paxcel.boxupp.ws.VagrantConsole;



public class SocketManager {	
	/*private WebSocketHandler context;
	
	public SocketManager(WebSocketHandler context){
		this.context = context;
	}
	
	public ContextHandler initiate(){
		
		this.addWebSocket(VagrantConsole.class, "/appConsole/");
		this.addWebSocket(list,AppDataUpdater.class, "/appDataUpdater/");
	}*/
	
	/*public void addWebSocket(WebSocketHandler contextHandler,final Class<?> webSocket, String contextPath){
		contextHandler.addServlet(new ServletHolder(new WebSocketsServletFactory(webSocket)),contextPath);
		
	}*/
	
	public ContextHandler addWebSocket(final Class<?> webSocket, String contextPath){
		WebSocketHandler wsHandler = new WebSocketHandler(){
			@Override
            public void configure(WebSocketServletFactory webSocketServletFactory) {
                webSocketServletFactory.register(webSocket);
            }
		};
		
		ContextHandler wsContextHandler = new ContextHandler();
		wsContextHandler.setHandler(wsHandler);
		wsContextHandler.setContextPath(contextPath);
		return wsContextHandler;
	}
	
}
