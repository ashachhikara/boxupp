package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "machineProjectMapping")
public class MachineProjectMapping {
	public final static String MACHINE_ID_FIELD_NAME = "machineID";
	public final static String PROJECT_ID_FIELD_NAME = "projectID";
	public MachineProjectMapping( ProjectBean project,
			MachineConfigurationBean machineConfig) {
		super();
		this.project = project;
		this.machineConfig = machineConfig;
	}
	
	public MachineProjectMapping(){
		
	}
	
	@DatabaseField(canBeNull = false, generatedId = true, useGetSet = true)
	private Integer ID;

	@DatabaseField(foreign = true, useGetSet = true, columnName =PROJECT_ID_FIELD_NAME)
	private ProjectBean project;

	@DatabaseField(foreign = true, useGetSet = true, columnName = MACHINE_ID_FIELD_NAME)
	private MachineConfigurationBean machineConfig;

	public Integer getID() {
		return ID;
	}
	public void setID(Integer ID) {
		this.ID = ID;
	}
	public ProjectBean getProject() {
		return project;
	}
	public void setProject(ProjectBean project) {
		this.project = project;
	}
	public MachineConfigurationBean getMachineConfig() {
		return machineConfig;
	}
	public void setMachineConfig(MachineConfigurationBean machineConfig) {
		this.machineConfig = machineConfig;
	}
	
}
