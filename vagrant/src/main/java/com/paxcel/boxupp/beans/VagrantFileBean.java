package com.paxcel.boxupp.beans;

public class VagrantFileBean {

	private String machineName;
	private String hostName;
	private String box;
	private String boxURL;
	private String IP;
	private boolean isMaster = false;
	
	public void setMachineName(String machineName){
		this.machineName = machineName;
	}

	public String getMachineName(){
		return machineName;
	}

	public void setHostName(String hostName){
		this.hostName = hostName;
	}

	public String getHostName(){
		return hostName;
	}

	public void setBox(String box){
		this.box = box;
	}

	public String getBox(){
		return box;
	}

	public void setBoxURL(String boxURL){
		this.boxURL = boxURL;
	}

	public String getBoxURL(){
		return boxURL;
	}

	public void setIP(String IP){
		this.IP = IP;
	}

	public String getIP(){
		return IP;
	}
	
	public void setAsMaster(){
		isMaster = true;
	}

	public boolean isMaster(){
		return isMaster;
	}
}
