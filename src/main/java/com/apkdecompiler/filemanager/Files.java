/**
 * 
 */
package com.apkdecompiler.filemanager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.apkdecompiler.main.ApkDecompiler;

/**
 * @author ${Periyasamy C}
 *
 * 08-Oct-2018
 */

public class Files {
	
	
	public static String mUserDir         = System.getProperty("user.dir");
	public static String mToolsPath       = mUserDir+File.separator+"tools";
	public static String mResourcesDir    = mUserDir+File.separator+"src/main/resources";
	public static String mBuildToolsPath  = mResourcesDir+File.separator+"build-tools";
	public static String mAAPTPath        = mBuildToolsPath+File.separator+"aapt";
	public static String mZipalign        = mBuildToolsPath+File.separator+"zipalign";
	public static String mApkSigner       = mBuildToolsPath+File.separator+"apksigner";
	public static String mKeyStorePath    = mBuildToolsPath+File.separator+"key-store.jks";
	public static String mAndroidSdkPath  = ApkDecompiler.mAndSdkPath;
	public static String mAndroidJarPath  = mBuildToolsPath+File.separator+"lib"+File.separator+"android.jar";
	/*mAndroidAAPTPath = mAndroidSdkPath+"/build-tools/28.0.2/aapt";
	mZipalign        = mAndroidSdkPath+"/build-tools/28.0.2/zipalign";
	mApkSigner       = mAndroidSdkPath+"/build-tools/28.0.2/apksigner";
	mAndroidJarPath  = mAndroidSdkPath+"/platforms/android-28/android.jar";*/
	
	public static File mInputApkFile     = null;
	public static File mOutputApkFile    = null;
	public static File mInputDirectory   = null;
	public static File mOutputDirectory  = null;
	public static File mApkBuildDir      = null;
	public static File mApkResDir        = null;
	public static String mApkFileName    = null;
	
	public static void refreshFilePath(){
		mInputApkFile     = null;
		mOutputApkFile    = null;
		mInputDirectory   = null;
		mOutputDirectory  = null;
		mApkBuildDir      = null;
		mApkResDir        = null;
		mApkFileName    = null;
	}
	
	/**
	 * @param directory
	 * @throws IOException 
	 * 
	 */
	public static void cleanDirectory(File directory) throws IOException {
		FileUtils.cleanDirectory(directory);
	}

	/**
	 * @param file
	 * @throws IOException 
	 */
	public static void delete(File file) throws IOException {
		if(file.isDirectory() ) {
			FileUtils.deleteDirectory(file);
		}
		if(file.isFile()) {
			FileUtils.forceDelete(file);
		}
	}
	
	//public static File mInputPath   = new File(ApkDecompiler.mInputPath);
	//public static File mOutputPath  = new File(ApkDecompiler.mOutputPath);
	//public static String mApkFileName    = mInputPath.getName().replace(".apk","");
	
	
}