package com.paxcel.responseBeans;

public class VagrantStatus {
	
	//0 : OFF
	//1 : RUNNING
	//3 : VAGRANT_FILE_NOT_PRESENT OR VAGRANT_UNINITIALIZED
	public int statusCode;  
	public String vagrantStatusMessage;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getVagrantStatus() {
		return vagrantStatusMessage;
	}

	public void setVagrantStatus(String vagrantStatus) {
		this.vagrantStatusMessage = vagrantStatus;
	}
}
