package com.boxupp.db.beans;

public class SearchModuleBean {
	
	private String uri;
	private String name;	
	private Integer downloads;
	private String created_at;
	private String updated_at;
	private Boolean supported;
	private ModuleOwnerBean owner;
	private ModuleCurrentReleaseBean current_release;
	
	
	public String getFileUri() {
		return uri;
	}
	public void setFileUri(String fileUri) {
		this.uri = fileUri;
	}
	public String getModuleName() {
		return name;
	}
	public void setModuleName(String moduleName) {
		this.name = moduleName;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDownloads() {
		return downloads;
	}
	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
	public Boolean getSupported() {
		return supported;
	}
	public void setSupported(Boolean supported) {
		this.supported = supported;
	}
	public ModuleOwnerBean getOwner() {
		return owner;
	}
	public void setOwner(ModuleOwnerBean owner) {
		this.owner = owner;
	}
	public ModuleCurrentReleaseBean getCurrent_release() {
		return current_release;
	}
	public void setCurrent_release(ModuleCurrentReleaseBean current_release) {
		this.current_release = current_release;
	}
}
