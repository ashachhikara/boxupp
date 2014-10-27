package com.paxcel.boxupp.api;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.Path;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;



@WebServlet("/echo")
public class WSServices extends WebSocketServlet {
	
	@Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(WSTrial.class);
    }
}
