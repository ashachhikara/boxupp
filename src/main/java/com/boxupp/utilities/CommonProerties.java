package com.boxupp.utilities;

public class CommonProerties {
	private static CommonProerties commonProperties = null;

	private String puppetForgeDownloadAPIPath = "https://forgeapi.puppetlabs.com:443";
	
	public static CommonProerties getInstance(){
		if(commonProperties == null){
			commonProperties = new CommonProerties();
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
