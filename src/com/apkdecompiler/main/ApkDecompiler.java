package com.apkdecompiler.main;

import java.io.File;

import com.apkdecompiler.filemanager.Files;
import com.apkdecompiler.logger.LogFile;
import com.apkdecompiler.resources.AndroidResources;
import com.apkdecompiler.resources.AppConfig;
import com.apkdecompiler.resources.AppResources;
import com.apkdecompiler.xmlbuilder.DecodeResXMLFiles;

public class ApkDecompiler {


	public static String  mInputPath   = null;
	//public static String  mInputDir    = null;
	public static String  mOutputPath  = null;
	//public static String  mOutputDir   = null;
	public static boolean mDecompile   = false;
	public static boolean mBuuild      = false;
	public static boolean mSign        = false;
	public static String  mAndSdkPath  = null;

	public static void main(String[] args) throws Exception {

		System.out.println("ApkDecompiler decompile started...");
		LogFile.toStartWrite();

		//ApkManager.cleanAppDir();
		if(args.length<=2 ) {
			System.err.println("args mismatched... ");
			System.exit(0);
		}

		for(int count=0;count<args.length;count++) {
			System.out.println(""+args[count]);
			if(args[0].contains("d") || args[0].contains("decompile")) { // build or decompile
				mDecompile=true;
			}

			if(args[0].contains("b") || args[0].contains("build")) { // build or decompile
				mBuuild=true;
			}


			if(args[count].startsWith("-i:")) { // mInputFile or mInputDir
				mInputPath = args[count].replace("-i:", "").trim();
			}


			if(args[count].startsWith("-o:")) { // mOutputFile or mOutputDir
				mOutputPath = args[count].replace("-o:", "").trim();
			}	

			if(args[count].startsWith("-sign")) { // mSign
				mSign=true;
			}	
		}


		if(mDecompile) {

			if(new File(mInputPath).isFile() && new File(mInputPath).getName().endsWith(".apk")) {
				if(new File(mOutputPath).isDirectory()) {
					if(!new File(mOutputPath).exists()) {
						new File(mOutputPath).mkdirs();
					}
					Files.mInputApkFile    = new File(mInputPath);
					Files.mOutputDirectory = new File(mOutputPath);
					Files.mApkFileName     = Files.mInputApkFile.getName().replace(".apk", "");
					Files.mApkBuildDir     = new File(mOutputPath+File.separator+Files.mApkFileName);
					decompileApkFile();
					
				}else {
					System.err.println(" output dir is invalid "+mOutputPath);
					System.exit(0);
				}
			}else {
				System.err.println(" input file is invalid "+mInputPath);
				System.exit(0);
			}
		}

		if(mBuuild) {
			if(new File(mInputPath).isDirectory() && new File(mInputPath).exists()) {
				if(new File(mOutputPath).getName().endsWith(".apk")) {
					
					Files.mOutputApkFile    =new File(mOutputPath);
					Files.mInputDirectory =new File(mInputPath);
					Files.mApkFileName=Files.mInputApkFile.getName().replace(".apk", "");
					Files.mApkBuildDir     = new File(mInputPath+File.separator+Files.mApkFileName);
					buildApkFile();
					
				}else {
					System.err.println(" output file is invalid "+mOutputPath);
					System.exit(0);
				}
			}else {
				System.err.println(" input dir is invalid "+mInputPath);
				System.exit(0);
			}
			if(mSign) {
				signApkFile();
			}
		}
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


