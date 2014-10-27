package com.boxupp.responseBeans;

public class BoxURLResponse {

	private int statusCode;
	private String contentLength;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getContentLength() {
		return contentLength;
	}
	public void setContentLength(long contentLength) {
		//To Change bytes to MB
		this.contentLength = contentLength/1048576 + "MB";
	}
}
