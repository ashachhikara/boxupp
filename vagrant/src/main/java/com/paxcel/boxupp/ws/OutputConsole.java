package com.paxcel.boxupp.ws;


public interface OutputConsole {
	
	public void pushOutput(String data);
	public void pushError(String data);
	public void pushDataTermination();
	
}
