package com.paxcel.boxupp.api;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class AppDataUpdater {
	
	@OnWebSocketConnect
    public void onConnect(Session session){
	      try {
	    	  int count =1;
	    	  while(true){
	    		  session.getRemote().sendString("AppDataUpdater :"+count);
	    		  count++;
	    		  try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	  }
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	      
    }

    @OnWebSocketMessage
    public void onText(String msg) {
        // MyWebSocket.onText();
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        // MyWebSocket.onClose()
    }
}
