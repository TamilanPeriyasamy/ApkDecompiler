package com.apkdecompiler.main;

import java.io.File;

import com.apkdecompiler.conversion.Convert;
import com.apkdecompiler.logger.LogFile;
import com.apkdecompiler.resources.AndroidResources;
import com.apkdecompiler.resources.AppConfig;
import com.apkdecompiler.resources.AppResources;
import com.apkdecompiler.resources.PluralsResources;
import com.apkdecompiler.resources.ResourcesManager;
import com.apkdecompiler.xmlbuilder.DecodeResXMLFiles;

public class ApkDecompiler {
	public static String mUserDir         = System.getProperty("user.dir");
	public static String mAndroidSdkPath  = null;
	public static String mAndroidAAPTPath = null;
	public static String mAndroidZipalign = null;
	public static String mAndroidJarPath  = null;
	public static String mInputApkName    = null;
	public static File mInputFile         = null;
	public static File mApkResourceDir    = null;
	public static File mApkBuildDir       = null;
	public static String mZipalign        = null;
	public static String mApkSigner       = null;
	public static String mKeyStorePath    = null;
	public static String mBuildToolsPath  = mUserDir+"/tools/";
	public static File mOutputDirectory   = new File(mUserDir+"/output/");
	public static File mBuildDirectory    = new File(mUserDir+"/build/");

	public static void main(String[] args) throws Exception {

		System.out.println("ApkDecompiler decompile started...");
		LogFile.toStartWrite();
		ApkManager.cleanAppDir();
		if(args.length==0) {
			System.err.println("args mismatched... ");
			System.exit(0);
		}
		//String command     = args[0];
		String apkFilePath = args[0];
		String sdkDirPath  = args[1];
		mInputFile       = new File(apkFilePath);
		mAndroidSdkPath  = sdkDirPath;
		mInputApkName    = mInputFile.getName().replace(".apk", "");
		mApkBuildDir     = new File(mBuildDirectory+File.separator+mInputApkName);
		mApkResourceDir  = new File(mApkBuildDir+File.separator+"res");
		
		/*mAndroidAAPTPath = mAndroidSdkPath+"/build-tools/28.0.2/aapt";
		mZipalign        = mAndroidSdkPath+"/build-tools/28.0.2/zipalign";
		mApkSigner       = mAndroidSdkPath+"/build-tools/28.0.2/apksigner";
		mAndroidJarPath  = mAndroidSdkPath+"/platforms/android-28/android.jar";*/
		
		mAndroidAAPTPath = mBuildToolsPath+"/build-tools/aapt";
		mZipalign        = mBuildToolsPath+"/build-tools/zipalign";
		mApkSigner       = mBuildToolsPath+"/build-tools/apksigner";
		mAndroidJarPath  = mBuildToolsPath+"/build-tools/lib/android.jar";
		
		//if(command.equals("decompile") || command.equals("d"))
		decompileApkFile();
		//if(command.equals("build") || command.equals("b"))
		buildApkFile();
		signApkFile();
		LogFile.toStopWrite();
		System.out.println("ApkDecompiler decompile completed..");
	}

	private static void decompileApkFile() throws Exception {
	
		AppConfig mApplicationConfig=new AppConfig();
		mApplicationConfig.getAppDetails();
		
		System.out.println("Decompile apk ...");
		ApkManager apkManager=new ApkManager();
		apkManager.extractingApkFiles();
		
		System.out.println("Load android sdk resources ...");
		AndroidResources androidSdkResources=new AndroidResources();
		androidSdkResources.parseAndroidSdkResources();
		
		AppResources mApplicationResources=new AppResources();
		mApplicationResources.parseApplicationResources();

		DecodeResXMLFiles decodeXMLFiles=new DecodeResXMLFiles();
		decodeXMLFiles.decodeResourcesXMLFiles();
	}


	private static void buildApkFile() throws Exception {
		ApkManager apkManager=new ApkManager();
		apkManager.buildApkFiles();
	}

	private static void signApkFile() throws Exception {			
		ApkManager apkManager=new ApkManager();
		apkManager.signApkFile();
	}
}


