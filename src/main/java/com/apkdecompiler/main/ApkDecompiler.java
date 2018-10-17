package com.apkdecompiler.main;

import com.apkdecompiler.filemanager.Files;
import com.apkdecompiler.filemanager.JarFiles;


public class ApkDecompiler {


	public static String  mInputPath    = null;
	public static String  mOutputPath   = null;
	public static boolean mApkDecompile = false;
	public static boolean mApkBuild     = false;
	public static boolean mSign         = false;
	public static String  mAndSdkPath   = null;

	public static void main(String[] args) throws Exception {

		System.out.println("ApkDecompiler decompile started...");
		new JarFiles().loadBuildTools();
		Files.refreshFilePath();
		if(args.length<=2 ) {
			System.err.println("args mismatched... ");
			System.exit(0);
		}

		for(int count=0;count<args.length;count++) {

			if( args[0].equals("decompile") || args[0].equals("d") ) { // build or decompile
				mApkDecompile=true;
			}else if( args[0].equals("build") || args[0].equals("b")) { // build or decompile
				mApkBuild=true;
			}else {
				System.err.println("invalid  args[]... ");
				System.exit(0);
			}

			if(args[count].equals("-sign")) { // mSign 
				mSign=true;
			}	

			if(args[count].equals("-in")) { // mInputFile or mInputDir
				if(args[count+1]!=null && !args[count+1].isEmpty()) {
					mInputPath = args[count+1].trim();
				}else {
					System.err.println("invalid  args[]... ");
					System.exit(0);
				}

			}

			if(args[count].equals("-out")) { // mOutputFile or mOutputDir
				if(args[count+1]!=null && !args[count+1].isEmpty()) {
					mOutputPath = args[count+1].trim();
				}else {
					System.err.println("invalid  args[]... ");
					System.exit(0);
				}
			}
		}

		if(mApkDecompile) {
			new DecompileManager(mInputPath,mOutputPath);
		}else if(mApkBuild) {
			new BuildManager(mInputPath,mOutputPath,mSign); 
		}else {
			System.err.println("args mismatched... ");
			System.exit(0);
		}
		System.out.println("ApkDecompiler decompile completed..");
	}
}


