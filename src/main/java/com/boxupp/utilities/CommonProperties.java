package com.boxupp.utilities;

public class CommonProperties {
	private static CommonProperties commonProperties = null;

	private String puppetForgeDownloadAPIPath = "https://forgeapi.puppetlabs.com:443";
	
	public static CommonProperties getInstance(){
		if(commonProperties == null){
			commonProperties = new CommonProperties();
		}
		return commonProperties;
	}
	
	public String getPuppetForgeDownloadAPIPath() {
		return puppetForgeDownloadAPIPath;
	}

	public void setPuppetForgeDownloadAPIPath(String puppetForgeDownloadAPIPath) {
		this.puppetForgeDownloadAPIPath = puppetForgeDownloadAPIPath;
	}
	
	
}
