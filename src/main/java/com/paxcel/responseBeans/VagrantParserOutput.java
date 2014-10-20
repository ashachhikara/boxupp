package com.paxcel.responseBeans;

public class VagrantParserOutput {
	
	private int statusCode = 1;
	private boolean validVagrantFile = false;
	private String parsedData;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public boolean isValidVagrantFile() {
		return validVagrantFile;
	}
	public void setValidVagrantFile(boolean validVagrantFile) {
		this.validVagrantFile = validVagrantFile;
	}
	public String getParsedData() {
		return parsedData;
	}
	public void setParsedData(String parsedData) {
		this.parsedData = parsedData;
	}
	
	

}
