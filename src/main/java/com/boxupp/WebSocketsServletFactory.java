package com.boxupp;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebSocket
public class WebSocketsServletFactory extends WebSocketServlet {
	
	private Class<?> webSocket = null;
	public WebSocketsServletFactory( Class<?> webSocket){
		this.webSocket = webSocket;
	}
	
	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(this.webSocket);
		
	}
	
}


