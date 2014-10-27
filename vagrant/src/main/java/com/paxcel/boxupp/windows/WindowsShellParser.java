package com.paxcel.boxupp.windows;

import com.paxcel.responseBeans.VagrantStatus;

public class WindowsShellParser {
	
	public VagrantStatus parseVagrantStatusCMD(StringBuffer cmdOutput){
		VagrantStatus statusBean = new VagrantStatus();
		String output = cmdOutput.toString();
		String vagrantFileNotPresent = "vagrant init";
		String vagrantEnvUninitialized = "not created";
		String vagrantEnvRunning = "running";
		String vagrantEnvOff = "poweroff";
		/* Case for 'vagrant init' will probably not occur ever */
		if(output.indexOf(vagrantFileNotPresent) != -1){
			statusBean.setStatusCode(3);
			statusBean.setVagrantStatus("Vagrant file not present");
		} 
		else if(output.indexOf(vagrantEnvUninitialized) != -1){
			statusBean.setStatusCode(3);
			statusBean.setVagrantStatus("Vagrant file present but uninitialized");
		}
		else if(output.indexOf(vagrantEnvRunning) != -1){
			statusBean.setStatusCode(1);
			statusBean.setVagrantStatus("Vagrant environment running");
		}
		else if(output.indexOf(vagrantEnvOff) != -1){
			statusBean.setStatusCode(0);
			statusBean.setVagrantStatus("Vagrant environment not running");
		}
		return statusBean;
	}
}
