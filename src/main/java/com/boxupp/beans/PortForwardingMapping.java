package com.boxupp.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "portMappings")
public class PortForwardingMapping {
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer id; 
	@DatabaseField(useGetSet = true)
	private String hostPort;
	
	@DatabaseField(useGetSet = true)
	private String vmPort;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
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
	
	
}
