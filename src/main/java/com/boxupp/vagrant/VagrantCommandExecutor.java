package com.boxupp.vagrant;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.boxupp.VagrantOutputStream;
import com.boxupp.utilities.OSProperties;
import com.boxupp.utilities.Utilities;
import com.boxupp.ws.OutputConsole;


public class VagrantCommandExecutor {
	
	private static Logger logger = LogManager.getLogger(VagrantCommandExecutor.class.getName());
	
	public static String WIN_CMD_PROCESSOR = "cmd";
	public static String OPTION_REG_VALUENAME = " /v";
	public static String OPTION_RUN_AND_TERMINATE = " /C";
	public static String REG_QUERY_CMD = "reg query ";
	public static String VBOX_MANAGE_CMD = "VBoxManage";
	public static String VBOX_LIST_CMD = " list";
	public static String VBOX_LIST_VMS_CMD = " vms";
	public static String VBOX_LIST_RUNNINGVMS_CMD = " runningvms";
	public static String VBOX_LIST_HOSTINFO_CMD = " hostinfo";
	public static String VBOX_VM_INFO_CMD = " showvminfo";
	public static String REG_SOFTWARE_PATH_CMD = "HKLM\\Software";
	public String cmdExecDir;
	
	
	public String getCMDExecDir(){
		return cmdExecDir;
	}
	
	public void setCMDExecDir(String dir){
		cmdExecDir = dir;
	}
	
	public void bootVagrantMachine(OutputConsole console, Integer userID, String... commands ) throws IOException, InterruptedException{
		
		StringBuffer vagrantCommand = new StringBuffer();
		File vagrantFile = new File(Utilities.getInstance().fetchActiveProjectDirectory(userID) 
						   +OSProperties.getInstance().getOSFileSeparator()+
						   OSProperties.getInstance().getVagrantFileName());
		for(int counter=0; counter<commands.length; counter++){
			if(counter == commands.length-1){
				vagrantCommand.append(commands[counter]);
			}
			else{
				vagrantCommand.append(commands[counter]).append(" ");
			}
		}
		if(vagrantFile.exists()){
			ProcessBuilder processBuilder;
			String osType = OSProperties.getInstance().getOSName();
			
			String windowsCmd = "cmd /C " + vagrantCommand.toString();
			if(osType.indexOf("windows") != -1){
				String commandArray[] = windowsCmd.split(" ");
				processBuilder = new ProcessBuilder(commandArray);
			}else{
				String command = vagrantCommand.toString().replaceAll("vagrant", "/opt/vagrant/bin/vagrant");
				processBuilder = new ProcessBuilder(command.split(" "));
			}
			
			processBuilder.directory(new File(cmdExecDir));
			Process proc = processBuilder.start();
			InputStream is = proc.getInputStream();
			InputStream es = proc.getErrorStream();
			BufferedReader errReader = new BufferedReader(new InputStreamReader(es));
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String data;
			console.pushOutput(cmdExecDir + ">" + vagrantCommand.toString());
			while((data = reader.readLine())!=null){
				console.pushOutput(data);
			}
			int exitCode = proc.waitFor();
//			if(exitCode != 0){
				while((data = errReader.readLine())!=null){
					console.pushError(data);
				}
//			}
			console.pushDataTermination();
		}else{
			console.pushOutput("VagrantFile not found on your machine.. !!");
			console.pushDataTermination();
		}
	}
	
	public StringBuffer checkVagrantStatusCMD(String... commands){
		
		StringBuffer vagrantCommand = new StringBuffer();
		StringBuffer commandOutput = new StringBuffer();
		try{

			ProcessBuilder processBuilder;
			String osType = OSProperties.getInstance().getOSName();
			
			if(osType.indexOf("windows") != -1){
				for(int counter=0; counter<commands.length; counter++){
					if(counter == commands.length-1){
						vagrantCommand.append(commands[counter]);
					}
					else{
						vagrantCommand.append(commands[counter]).append(" ");
					}
				}
				vagrantCommand.insert(0, "/C ");
				vagrantCommand.insert(0, "cmd ");
				String commandArray[] = vagrantCommand.toString().split(" ");
				processBuilder = new ProcessBuilder(commandArray);
			}else{
				
				for(int counter=0; counter<commands.length; counter++){
					if(counter == commands.length-1){
						vagrantCommand.append(commands[counter]);
					}
					else{
						vagrantCommand.append(commands[counter]).append(" ");
					}
				}
				String command = vagrantCommand.toString().replaceAll("vagrant", "/opt/vagrant/bin/vagrant");
				processBuilder = new ProcessBuilder(command.split(" "));
			}
			processBuilder.directory(new File(cmdExecDir));
			Process proc = processBuilder.start();
			InputStream is = proc.getInputStream();
			InputStream es = proc.getErrorStream();
			BufferedReader errReader = new BufferedReader(new InputStreamReader(es));
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String data;
			while((data = reader.readLine())!=null){
				commandOutput.append(data);
			}
			int exitCode = proc.waitFor();
			if(exitCode != 0){
				while((data = errReader.readLine())!=null){
					commandOutput.append(data);
				}
			}
		}catch(IOException e){
			logger.error("Error reading command output : "+e.getMessage());
		}
		catch(InterruptedException e){
			logger.error("Interruption occured while waiting for command execution process : "+e.getMessage());
		}
		return commandOutput;
	}
		
	public static void main(String args[]) throws IOException, InterruptedException{
		VagrantCommandExecutor executor = new VagrantCommandExecutor();
		String[] array = {"vagrant","status"};
		executor.setCMDExecDir(OSProperties.getInstance().getUserHomeDirectory());
		executor.checkVagrantStatusCMD(array);
		System.out.println("completed");
	}
}
	
	
