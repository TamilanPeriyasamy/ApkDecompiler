package com.apkdecompiler.main;

import com.apkdecompiler.filemanager.Files;
import com.apkdecompiler.logger.LogFile;
import com.apkdecompiler.resources.AndroidResources;
import com.apkdecompiler.resources.AppConfig;
import com.apkdecompiler.resources.AppResources;
import com.apkdecompiler.xmlbuilder.DecodeResXMLFiles;

public class ApkDecompiler {


	public static String  mInputFile   = null;
	public static String  mInputDir    = null;
	public static String  mOutputFile  = null;
	public static String  mOutputDir   = null;
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
				mInputFile = args[count].replace("-i:", "").trim();
				mInputDir  = args[count].replace("-i:", "").trim();
			}


			if(args[count].startsWith("-o:")) { // mOutputFile or mOutputDir
				mOutputFile = args[count].replace("-o:", "").trim();
				mOutputDir  = args[count].replace("-o:", "").trim();
			}	

			if(args[count].startsWith("-sign")) { // mSign
				mSign=true;
			}	
		}


		if(mDecompile) {

			if(Files.mInputFile.isFile() && Files.mInputFile.getName().endsWith(".apk")) {
				if(Files.mOutputDir.isDirectory()) {
					if(!Files.mOutputDir.exists()) {
						Files.mOutputDir.mkdirs();
					}
					decompileApkFile();
				}else {
					System.err.println(" output dir is invalid "+Files.mOutputDir);
					System.exit(0);
				}
			}else {
				System.err.println(" input file is invalid "+Files.mInputFile);
				System.exit(0);
			}
		}

		if(mBuuild) {
			if(Files.mInputDir.isDirectory() && Files.mInputDir.exists()) {
				if(Files.mOutputFile.getName().endsWith(".apk")) {
					buildApkFile();
				}else {
					System.err.println(" output file is invalid "+Files.mOutputFile);
					System.exit(0);
				}
			}else {
				System.err.println(" input dir is invalid "+Files.mInputDir);
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


