package com.paxcel.responseBeans;

public class VagrantOutput {
	
	private String output;
	private boolean dataEnd = false;
	private boolean vagrantFileExists = true;
	
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public boolean isDataEnd() {
		return dataEnd;
	}
	public void setDataEnd(boolean dataEnd) {
		this.dataEnd = dataEnd;
	}
	public boolean isVagrantFileExists() {
		return vagrantFileExists;
	}
	public void setVagrantFileExists(boolean vagrantFileExists) {
		this.vagrantFileExists = vagrantFileExists;
	}


}
