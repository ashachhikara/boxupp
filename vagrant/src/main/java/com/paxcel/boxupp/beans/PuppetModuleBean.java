package com.paxcel.boxupp.beans;

import java.util.ArrayList;

public class PuppetModuleBean {
	
	private String moName;
	private ArrayList<PuppetModuleFolderBean> moFolders;
	
	public String getMoName() {
		return moName;
	}
	public void setMoName(String moName) {
		this.moName = moName;
	}
	public ArrayList<PuppetModuleFolderBean> getMoFolders() {
		return moFolders;
	}
	public void setMoFolders(ArrayList<PuppetModuleFolderBean> moFolders) {
		this.moFolders = moFolders;
	}
	
	
	
}
