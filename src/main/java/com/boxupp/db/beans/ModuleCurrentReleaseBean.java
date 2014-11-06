package com.boxupp.db.beans;

public class ModuleCurrentReleaseBean {

	private String uri;
	private String version;
	private String file_uri;
	private Integer file_size;
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
	public String getFile_uri() {
		return file_uri;
	}
	public void setFile_uri(String file_uri) {
		this.file_uri = file_uri;
	}
	public Integer getFile_size() {
		return file_size;
	}
	public void setFile_size(Integer file_size) {
		this.file_size = file_size;
	}
}
