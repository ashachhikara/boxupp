package com.paxcel.responseBeans;

import org.codehaus.jackson.JsonNode;

public class SnapshotData {
	
	private JsonNode data;
	private boolean fileExists;
	
	public JsonNode getData() {
		return data;
	}
	public void setData(JsonNode data) {
		this.data = data;
	}
	public boolean isFileExists() {
		return fileExists;
	}
	public void setFileExists(boolean fileExists) {
		this.fileExists = fileExists;
	}	
}
