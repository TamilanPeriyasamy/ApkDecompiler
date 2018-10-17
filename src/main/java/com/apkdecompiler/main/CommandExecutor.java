package com.apkdecompiler.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.apkdecompiler.filemanager.Files;
import com.apkdecompiler.filemanager.JarFiles;

public class CommandExecutor {

	File mApkFile = null;
	public CommandExecutor() {
	}

	public CommandExecutor(File apkfile){
		mApkFile=apkfile;
    }

	public boolean executeCommand(String runCommand,boolean printLog) throws Exception {
		Runtime rt	= Runtime.getRuntime();
		Process process   = rt.exec(runCommand);
		BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String infoLine=null;
		while((infoLine=inputStreamReader.readLine())!=null) {
			if(printLog) {
				System.out.println(infoLine);
			}
		}
		inputStreamReader.close();
		BufferedReader errorStreamReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String errorLine=null;
		while((errorLine=errorStreamReader.readLine())!=null) {
			System.out.println(errorLine);
		}
		errorStreamReader.close();
		process.waitFor();
		process.destroy();  
		final int exitValue = process.waitFor();
		process.destroy() ; 
		if(exitValue == 0){
			return true;
		}
		return false;
	}	

	public boolean executeCommand(String[] runcommands,boolean printLog) throws Exception {
		Runtime rt	= Runtime.getRuntime();
		Process process   = rt.exec(runcommands);

		BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String infoLine=null;
		while((infoLine=inputStreamReader.readLine())!=null) {
			if(printLog) {
				System.out.println(infoLine);
			}
		}
		inputStreamReader.close();

		BufferedReader errorStreamReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String errorLine=null;
		while((errorLine=errorStreamReader.readLine())!=null) {
			System.err.println(errorLine);
		}
		errorStreamReader.close();
		process.waitFor();
		process.destroy();  
		final int exitValue = process.waitFor();
		process.destroy() ; 
		if(exitValue == 0){
			return true;
		}
		return false;
	}

	private ArrayList<String> getInputStream(String command)throws IOException, InterruptedException{
		ArrayList<String> inputStream= new ArrayList<String>();
		String runCommand=Files.mAAPTPath+" "+command+" "+mApkFile.getAbsolutePath();
		Runtime rt	= Runtime.getRuntime();
		Process process = rt.exec(runCommand);
		BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=null;
		while((line=inputStreamReader.readLine())!=null) {
			inputStream.add(line);	
		}
		process.waitFor(); 
		process.destroy();
		inputStreamReader.close();
		return inputStream;
	}

	private ArrayList<String> getInputStream(String command1,String command2)throws IOException, InterruptedException{
		ArrayList<String> inputStream= new ArrayList<String>();
		String runCommand=Files.mAAPTPath+" "+command1+" "+mApkFile.getAbsolutePath()+" "+command2;
		Runtime rt	= Runtime.getRuntime();
		Process process = rt.exec(runCommand);
		BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line=null;
		while((line=inputStreamReader.readLine())!=null) {
			inputStream.add(line);	
		}
		process.waitFor(); 
		process.destroy();
		inputStreamReader.close();
		return inputStream;
	}

	public ArrayList<String> getXmlTree(String command1,String command2) throws IOException, InterruptedException {
		return getInputStream(command1,command2);
	}

	public ArrayList<String> getStringsValues(String command) throws IOException, InterruptedException {
		return getInputStream(command);
	}

	public ArrayList<String> getAppConfig(String command) throws IOException, InterruptedException {
		return getInputStream(command);
	}

	public ArrayList<String> getResources(String command) throws IOException, InterruptedException {
		return getInputStream(command);
	}
}
