package com.paxcel.boxupp.beans;

import java.util.ArrayList;

import org.eclipse.jetty.util.security.Password;

public class DockerConfiguration {
	private String vagrantID;
	private String hostName;
	private String dockerImage;
	private ArrayList<PortForwardingMapping> portMappings;
	private ArrayList<SyncFolderMapping> syncFolders;
	private String username;
	private Password password;
	private boolean isPuppetMaster;
	private ArrayList<String> linkedScripts;
	private ArrayList<String> linkedModules;
	private ArrayList<String> linkedCookbooks;
	private String providerType;
	private boolean isShellMapped;
	private boolean isPuppetMapped;
	private boolean isChefMapped;
	
	public String getDockerImage() {
		return dockerImage;
	}
	public void setDockerImage(String dockerImage) {
		this.dockerImage = dockerImage;
	}
	public ArrayList<PortForwardingMapping> getPortMappings() {
		return portMappings;
	}
	public void setPortMappings(ArrayList<PortForwardingMapping> portMappings) {
		this.portMappings = portMappings;
	}
	public ArrayList<SyncFolderMapping> getSyncFolders() {
		return syncFolders;
	}
	public void setSyncFolders(ArrayList<SyncFolderMapping> syncFolders) {
		this.syncFolders = syncFolders;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isPuppetMaster() {
		return isPuppetMaster;
	}
	public void setPuppetMaster(boolean isPuppetMaster) {
		this.isPuppetMaster = isPuppetMaster;
	}
	public ArrayList<String> getLinkedScripts() {
		return linkedScripts;
	}
	public void setLinkedScripts(ArrayList<String> linkedScripts) {
		this.linkedScripts = linkedScripts;
	}
	public ArrayList<String> getLinkedModules() {
		return linkedModules;
	}
	public void setLinkedModules(ArrayList<String> linkedModules) {
		this.linkedModules = linkedModules;
	}
	public ArrayList<String> getLinkedCookbooks() {
		return linkedCookbooks;
	}
	public void setLinkedCookbooks(ArrayList<String> linkedCookbooks) {
		this.linkedCookbooks = linkedCookbooks;
	}
	public String getProviderType() {
		return providerType;
	}
	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}
	public boolean isShellMapped() {
		return isShellMapped;
	}
	public void setShellMapped(boolean isShellMapped) {
		this.isShellMapped = isShellMapped;
	}
	public boolean isPuppetMapped() {
		return isPuppetMapped;
	}
	public void setPuppetMapped(boolean isPuppetMapped) {
		this.isPuppetMapped = isPuppetMapped;
	}
	public boolean isChefMapped() {
		return isChefMapped;
	}
	public void setChefMapped(boolean isChefMapped) {
		this.isChefMapped = isChefMapped;
	}
	
	public String getVagrantID() {
		return vagrantID;
	}
	public void setVagrantID(String vagrantID) {
		this.vagrantID = vagrantID;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public Password getPassword() {
		return password;
	}
	public void setPassword(Password password) {
		this.password = password;
	}
	
}
