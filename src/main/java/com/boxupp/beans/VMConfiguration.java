package com.boxupp.beans;

import java.util.ArrayList;

public class VMConfiguration {
	
	private String vagrantID;
	private String hostName;
	private String boxType;
	private String boxUrl;
	private String networkIP;
	private ArrayList<PortForwardingMapping> portMappings;
	private ArrayList<SyncFolderMapping> syncFolders;
	private String provisionerName;
	private String cpuExecCap;
	private String memory;
	private String bootTimeout;
	private boolean guiMode;
	private boolean isPuppetMaster;
	private ArrayList<String> linkedScripts;
	private ArrayList<String> linkedModules;
	private ArrayList<String> linkedCookbooks;
	private String providerType;
	private boolean isShellMapped;
	private boolean isPuppetMapped;
	private boolean isChefMapped;
	
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
	public String getBoxType() {
		return boxType;
	}
	public void setBoxType(String boxType) {
		this.boxType = boxType;
	}
	public String getBoxUrl() {
		return boxUrl;
	}
	public void setBoxUrl(String boxUrl) {
		this.boxUrl = boxUrl;
	}
	public String getNetworkIP() {
		return networkIP;
	}
	public void setNetworkIP(String networkIP) {
		this.networkIP = networkIP;
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
	public String getProvisionerName() {
		return provisionerName;
	}
	public void setProvisionerName(String provisionerName) {
		this.provisionerName = provisionerName;
	}
	public String getCpuExecCap() {
		return cpuExecCap;
	}
	public void setCpuExecCap(String cpuExecCap) {
		this.cpuExecCap = cpuExecCap;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getBootTimeout() {
		return bootTimeout;
	}
	public void setBootTimeout(String bootTimeout) {
		this.bootTimeout = bootTimeout;
	}
	public boolean isGuiMode() {
		return guiMode;
	}
	public void setGuiMode(boolean guiMode) {
		this.guiMode = guiMode;
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
		
}
