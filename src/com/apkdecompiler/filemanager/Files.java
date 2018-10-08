/**
 * 
 */
package com.apkdecompiler.filemanager;

import java.io.File;

import com.apkdecompiler.main.ApkDecompiler;

/**
 * @author ${Periyasamy C}
 *
 * 08-Oct-2018
 */

public class Files {
	public static String mUserDir         = System.getProperty("user.dir");
	public static String mToolsPath       = mUserDir+File.separator+"tools";
	public static String mBuildToolsPath  = mToolsPath+File.separator+"build-tools";
	public static String mAAPTPath        = mBuildToolsPath+File.separator+"aapt";
	public static String mZipalign        = mBuildToolsPath+File.separator+"zipalign";
	public static String mApkSigner       = mBuildToolsPath+File.separator+"apksigner";
	public static String mAndroidSdkPath  = ApkDecompiler.mAndSdkPath;
	public static String mAndroidJarPath  = mBuildToolsPath+File.separator+"lib"+File.separator+"android.jar";
	/*mAndroidAAPTPath = mAndroidSdkPath+"/build-tools/28.0.2/aapt";
	mZipalign        = mAndroidSdkPath+"/build-tools/28.0.2/zipalign";
	mApkSigner       = mAndroidSdkPath+"/build-tools/28.0.2/apksigner";
	mAndroidJarPath  = mAndroidSdkPath+"/platforms/android-28/android.jar";*/
	
	
	public static File mInputFile   = new File(ApkDecompiler.mInputFile);
	public static File mInputDir    = new File(ApkDecompiler.mInputDir);
	public static File mOutputFile  = new File(ApkDecompiler.mOutputFile);
	public static File mOutputDir   = new File(ApkDecompiler.mOutputDir);
	public static File mInputApk    = mInputFile;
	public static File mOutputApk   = mOutputFile;
	
	public static String mApkName    = mInputFile.getName().replace(".apk","");
	public static File mApkBuildDir  = new File(mOutputDir+File.separator+mApkName);
	public static File mApkResDir    = new File(mApkBuildDir+File.separator+"res");
}
