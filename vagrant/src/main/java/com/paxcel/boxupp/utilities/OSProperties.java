package com.paxcel.boxupp.utilities;

public class OSProperties {
	
	private static OSProperties osProperties = null;
	private String OSFileSeparator = "";
	private String OSName = "";
	private String OSLineSeparator = "";
	private String UserHomeDirectory = "";
	private String vagrantFileName = "Vagrantfile";
	private String scriptsDirName = "scripts";
	private String primaryFolderName="Boxupp";
	private String serializedFileName = "serData";
			
	private OSProperties(){
			OSFileSeparator = System.getProperty("file.separator");
			OSName = System.getProperty("os.name").toLowerCase();
			OSLineSeparator = System.getProperty("line.separator");
			UserHomeDirectory = System.getProperty("user.home");
	}
	
	public static OSProperties getInstance(){
		if(osProperties == null){
			osProperties = new OSProperties();
		}
		return osProperties;
	}

	public String getOSFileSeparator() {
		return OSFileSeparator;
	}

	public String getOSName() {
		return OSName;
	}

	public String getOSLineSeparator() {
		return OSLineSeparator;
	}

	public String getUserHomeDirectory() {
		return UserHomeDirectory;
	}

	public String getVagrantFileName() {
		return vagrantFileName;
	}

	public void setVagrantFileName(String vagrantFileName) {
		this.vagrantFileName = vagrantFileName;
	}

	public String getScriptsDirName() {
		return scriptsDirName;
	}

	public void setScriptsDirName(String shellDirName) {
		this.scriptsDirName = shellDirName;
	}

	public String getPrimaryFolderName() {
		return primaryFolderName;
	}

	public void setPrimaryFolderName(String primaryFolderName) {
		this.primaryFolderName = primaryFolderName;
	}

	public String getSerializedFileName() {
		return serializedFileName;
	}

	public void setSerializedFileName(String serializedFileName) {
		this.serializedFileName = serializedFileName;
	}
	
	
}
