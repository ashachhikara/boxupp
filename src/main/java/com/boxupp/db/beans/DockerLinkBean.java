package com.boxupp.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "container_link")
public class DockerLinkBean {
	public static final String MACHINE_ID_FIELD_NAME = "machineID";
	
		@DatabaseField(foreign = true,foreignAutoRefresh = true, columnName = MACHINE_ID_FIELD_NAME)
		private MachineConfigurationBean machineConfig;
	
		@DatabaseField(useGetSet = true)
		private Integer linkedMachineID;
		
		public MachineConfigurationBean getMachineConfig() {
			return machineConfig;
		}
		public void setMachineConfig(MachineConfigurationBean machineConfig) {
			this.machineConfig = machineConfig;
		}
		public Integer getLinkedMachineID() {
			return linkedMachineID;
		}
		public void setLinkedMachineID(Integer linkedMachineID) {
			this.linkedMachineID = linkedMachineID;
		}
		
		
}
