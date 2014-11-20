package com.boxupp.vagrant;

import java.io.IOException;

import com.boxupp.VagrantOutputStream;
import com.boxupp.responseBeans.VagrantStatus;
import com.boxupp.ws.OutputConsole;

public class VagrantCommandProcessor {
	private static VagrantCommandExecutor shellExec = new VagrantCommandExecutor();
	private static VagrantCommandParser shellParser = new VagrantCommandParser();
	
	public VagrantStatus checkVagrantStatus(String location){
		shellExec.setCMDExecDir(location);
		StringBuffer cmdOutput;
		cmdOutput = shellExec.checkVagrantStatusCMD("vagrant","status");
		return shellParser.parseVagrantStatusCMD(cmdOutput);
	}
	
	public VagrantStatus checkMachineStatus(String location, String vagrantID){
		shellExec.setCMDExecDir(location);
		StringBuffer cmdOutput;
		cmdOutput = shellExec.checkVagrantStatusCMD("vagrant","status",vagrantID);
		return shellParser.parseVagrantStatusCMD(cmdOutput);
	}
	
	public String executeVagrantFile(String location, String command, Integer userID,OutputConsole consoleType) throws IOException, InterruptedException{
		
//		VagrantOutputStream.flushData();
		command = filterCommand(command);
		shellExec.setCMDExecDir(location);
		shellExec.bootVagrantMachine(consoleType, userID, command.split(" "));
		return "";
		
	}
	
	public String filterCommand(String command){
		//vagrant destroy
		StringBuilder stringConcat = new StringBuilder();
		int indexOfDestroy = command.indexOf("destroy");
		int indexOfForce = command.indexOf("--force");
		if((indexOfDestroy > -1) && (indexOfForce == -1)){
			stringConcat.append(command.substring(0, indexOfDestroy + 7));
			stringConcat.append(" --force");
			stringConcat.append(command.substring(indexOfDestroy + 7, command.length()));
			command = stringConcat.toString();
		}
		return command;
	}
	
	public static void main(String args[]) throws IOException, InterruptedException{
		VagrantCommandProcessor proc = new VagrantCommandProcessor();
		System.out.println(proc.filterCommand("vagrant destroy mysql")+"'");
	}
}
