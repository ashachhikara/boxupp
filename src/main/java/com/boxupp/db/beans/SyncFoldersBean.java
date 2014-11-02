package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sync_folder")
public class SyncFoldersBean {
	public static final String MACHINE_ID_FIELD_NAME = "machineID";
	
	@DatabaseField(canBeNull = false, generatedId= true, useGetSet = true)
	private Integer syncFolderID;
	
	@DatabaseField(foreign = true, foreignAutoCreate=true, foreignAutoRefresh = true, columnName = MACHINE_ID_FIELD_NAME)
	MachineConfigurationBean machineConfig;
	
	@DatabaseField(useGetSet = true)
	private String hostFolder;
	
	@DatabaseField(useGetSet = true)
	private String vmFolder;
	
	public Integer getSyncFolderID() {
		return syncFolderID;
	}
	public void setSyncFolderID(Integer syncFolderID) {
		this.syncFolderID = syncFolderID;
	}
	public String getHostFolder() {
		return hostFolder;
	}
	public void setHostFolder(String hostFolder) {
		this.hostFolder = hostFolder;
	}
	public String getVmFolder() {
		return vmFolder;
	}
	public void setVmFolder(String vmFolder) {
		this.vmFolder = vmFolder;
	}
	public MachineConfigurationBean getMachineConfig() {
		return machineConfig;
	}
	public void setMachineConfig(MachineConfigurationBean machineConfig) {
		this.machineConfig = machineConfig;
	}
	
}
