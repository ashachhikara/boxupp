package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "forwordedPort")
public class ForwardedPortsBean {
	
	public static final String MACHINE_ID_FIELD_NAME = "machine_id";
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = MACHINE_ID_FIELD_NAME)
	MachineConfigurationBean machineConfig;
	
	@DatabaseField(useGetSet = true)
	private String hostPort;
	
	@DatabaseField(useGetSet = true)
	private String vmPort;
	
	public String getHostPort() {
		return hostPort;
	}
	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}
	public String getVmPort() {
		return vmPort;
	}
	public void setVmPort(String vmPort) {
		this.vmPort = vmPort;
	}
	public MachineConfigurationBean getMachineConfig() {
		return machineConfig;
	}
	public void setMachineConfig(MachineConfigurationBean machineConfig) {
		this.machineConfig = machineConfig;
	}
	
}
