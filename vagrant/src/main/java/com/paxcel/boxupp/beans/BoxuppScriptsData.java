package com.paxcel.boxupp.beans;

import java.util.ArrayList;

public class BoxuppScriptsData {
	
	private ArrayList<ShellScriptBean> shellScripts;

	public ArrayList<ShellScriptBean> getScriptsList() {
		return shellScripts;
	}

	public void setScriptsList(ArrayList<ShellScriptBean> scriptsList) {
		this.shellScripts = scriptsList;
	}
}
