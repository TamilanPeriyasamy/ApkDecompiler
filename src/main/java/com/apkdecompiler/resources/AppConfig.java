package com.apkdecompiler.resources;

import java.io.File;
import java.util.ArrayList;

import com.apkdecompiler.filemanager.Files;
import com.apkdecompiler.main.ApkDecompiler;
import com.apkdecompiler.main.CommandExecutor;

public class AppConfig {

	public static String mPackageName      = null;
	public static String mPackageId        = null;
	public static String mAndPackageId     = null;
	public static File   mApkFile          = null;
	public static String version_Code      = null;
	public static String version_Name      = null;
	public static String min_SdkVersion    = null;
	public static String target_SdkVersion = null;
	public static String app_name          = null;
	
	public AppConfig(){
	  mApkFile=Files.mInputApkFile;
	}

	public void getAppDetails() throws Exception {
		
		ArrayList<String> inputStreamList=new CommandExecutor(mApkFile).getAppConfig("dump badging");
		for(int lineCount=0;lineCount<inputStreamList.size();lineCount++) {
			System.out.println(""+inputStreamList.get(lineCount));
			String line=inputStreamList.get(lineCount);
			if(line.trim().startsWith("package: ")) {
				System.out.println("App Details = "+inputStreamList.get(lineCount));
				String subLine=line.substring(line.indexOf(" name='")+7);
				mPackageName=subLine.substring(0, subLine.indexOf("'"));
				//System.out.println(" package_name = "+mPackageName);

				subLine=subLine.substring(subLine.indexOf(" versionCode='")+14);
				version_Code=subLine.substring(0, subLine.indexOf("'"));
				//System.out.println(" version_Code = "+version_Code);

				subLine=subLine.substring(subLine.indexOf(" versionName='")+14);
				version_Name=subLine.substring(0, subLine.indexOf("'"));
				//System.out.println(" version_Name = "+version_Name);
			}

			if(line.trim().startsWith("sdkVersion:'")) {
				String subLine=line.substring(line.indexOf("sdkVersion:'")+12);
				min_SdkVersion=subLine.substring(0, subLine.indexOf("'"));
				//System.out.println(" min_SdkVersion = "+min_SdkVersion);
			}

			if(line.trim().startsWith("targetSdkVersion:'")) {
				String subLine=line.substring(line.indexOf("targetSdkVersion:'")+18);
				target_SdkVersion=subLine.substring(0, subLine.indexOf("'"));
				//System.out.println(" target_SdkVersion = "+target_SdkVersion);
			}

			if(line.trim().startsWith("application-label:'")) {
				String subLine=line.substring(line.indexOf("application-label:'")+19);
				app_name=subLine.substring(0, subLine.indexOf("'"));
				//System.out.println(" app_name = "+app_name);
				break;
			}
		}
	}
}
