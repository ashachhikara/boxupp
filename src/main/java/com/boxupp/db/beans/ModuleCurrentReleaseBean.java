package com.boxupp.db.beans;

public class ModuleCurrentReleaseBean {

	private String uri;
	private String version;
	private ModuleMetaDataBean metadata;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public ModuleMetaDataBean getMetadata() {
		return metadata;
	}
	public void setMetadata(ModuleMetaDataBean metadata) {
		this.metadata = metadata;
	}
	
	
}
