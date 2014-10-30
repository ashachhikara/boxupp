package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "dockerLinks")
public class DockerLinkBean {
	public static final String MACHINE_ID_FIELD_NAME = "machineID";
	
		@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = MACHINE_ID_FIELD_NAME)
		MachineConfigurationBean machineConfig;
	
		@DatabaseField(useGetSet = true)
		private String linkContainer;
		
		public String getLinkContainer() {
			return linkContainer;
		}

		public void setLinkContainer(String linkContainer) {
			this.linkContainer = linkContainer;
		}
		public MachineConfigurationBean getMachineConfig() {
			return machineConfig;
		}
		public void setMachineConfig(MachineConfigurationBean machineConfig) {
			this.machineConfig = machineConfig;
		}
		
}
