package com.paxcel.responseBeans;

public class VagrantFileStatus {
	
	private boolean fileCreated = false;
	private String fileCreationPath;
	
	public boolean isFileCreated() {
		return fileCreated;
	}
	public void setFileCreated(boolean fileCreated) {
		this.fileCreated = fileCreated;
	}
	public String getFileCreationPath() {
		return fileCreationPath;
	}
	public void setFileCreationPath(String fileCreationPath) {
		this.fileCreationPath = fileCreationPath;
	}
}
