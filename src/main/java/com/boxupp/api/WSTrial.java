/*package com.boxupp.api;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


@WebSocket
public class WSTrial{
	 
		@OnWebSocketConnect
	    public void onConnect(Session session){
	        System.out.println("MyWebSocket.onConnect()");
	    }

	    @OnWebSocketMessage
	    public void onText(String msg) {
	        System.out.println("MyWebSocket.onText()");
	    }

	    @OnWebSocketClose
	    public void onClose(int statusCode, String reason) {
	        System.out.println("MyWebSocket.onClose()");
	    }
}
*/