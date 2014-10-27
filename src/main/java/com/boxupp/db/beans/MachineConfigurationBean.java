package com.boxupp.db.beans;

import org.eclipse.jetty.util.security.Password;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "machineConfiguration")
public class MachineConfigurationBean {
	public static final String ID_FIELD_NAME = "machineId";

	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true, columnName=ID_FIELD_NAME)
	private Integer machineId;

	@DatabaseField(useGetSet = true)
	private String vagrantId;

	@DatabaseField(useGetSet = true)
	private String hostName;

	@DatabaseField(useGetSet = true)
	private String boxType;

	@DatabaseField(useGetSet = true)
	private String boxUrl;

	@DatabaseField(useGetSet = true)
	private String networkIP;

	@ForeignCollectionField
	private ForeignCollection<ForwardedPortsBean> portMappings;
	@ForeignCollectionField
	private ForeignCollection<SyncFoldersBean> syncFolders;
	
	@ForeignCollectionField
	private ForeignCollection<DockerLinkBean> dockerLinks;

	@DatabaseField(useGetSet = true)
	private String provisionerName;

	@DatabaseField(useGetSet = true)
	private String cpuExecCap;

	@DatabaseField(useGetSet = true)
	private String memory;

	@DatabaseField(useGetSet = true)
	private String bootTimeout;

	@DatabaseField(useGetSet = true)
	private boolean guiMode;

	@DatabaseField(useGetSet = true)
	private boolean puppetMasterStatus;

	@DatabaseField(useGetSet = true)
	private String providerType;

	@DatabaseField(useGetSet = true)
	private boolean shellMappedStatus;

	@DatabaseField(useGetSet = true)
	private boolean puppetMappedStatus;

	@DatabaseField(useGetSet = true)
	private boolean chefMappedStatus;

	@DatabaseField(useGetSet = true)
	private String dockerImage;

	@DatabaseField(useGetSet = true)
	private String username;

	@DatabaseField(useGetSet = true)
	private String password;

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public String getVagrantId() {
		return vagrantId;
	}

	public void setVagrantId(String vagrantId) {
		this.vagrantId = vagrantId;
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

	public ForeignCollection<ForwardedPortsBean> getPortMappings() {
		return portMappings;
	}

	public void setPortMappings(
			ForeignCollection<ForwardedPortsBean> portMappings) {
		this.portMappings = portMappings;
	}

	public ForeignCollection<SyncFoldersBean> getSyncFolders() {
		return syncFolders;
	}

	public void setSyncFolders(ForeignCollection<SyncFoldersBean> syncFolders) {
		this.syncFolders = syncFolders;
	}
	public ForeignCollection<DockerLinkBean> getDockerLinks() {
		return dockerLinks;
	}

	public void setDockerLinks(ForeignCollection<DockerLinkBean> dockerLinks) {
		this.dockerLinks = dockerLinks;
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

	public boolean getGuiMode() {
		return guiMode;
	}

	public void setGuiMode(boolean guiMode) {
		this.guiMode = guiMode;
	}
	
	
	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public boolean getPuppetMasterStatus() {
		return puppetMasterStatus;
	}

	public void setPuppetMasterStatus(boolean puppetMasterStatus) {
		this.puppetMasterStatus = puppetMasterStatus;
	}

	public boolean getShellMappedStatus() {
		return shellMappedStatus;
	}

	public void setShellMappedStatus(boolean shellMappedStatus) {
		this.shellMappedStatus = shellMappedStatus;
	}

	public boolean getPuppetMappedStatus() {
		return puppetMappedStatus;
	}

	public void setPuppetMappedStatus(boolean puppetMappedStatus) {
		this.puppetMappedStatus = puppetMappedStatus;
	}

	public boolean getChefMappedStatus() {
		return chefMappedStatus;
	}

	public void setChefMappedStatus(boolean chefMappedStatus) {
		this.chefMappedStatus = chefMappedStatus;
	}

	public String getDockerImage() {
		return dockerImage;
	}

	public void setDockerImage(String dockerImage) {
		this.dockerImage = dockerImage;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

}
