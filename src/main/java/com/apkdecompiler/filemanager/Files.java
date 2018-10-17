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
	public static String mAAPTPath        = "/tmp/build-tools/aapt";
	public static String mZipalign        = "/tmp/build-tools/zipalign";
	public static String mKeyStorePath    = "/tmp/build-tools/key-store.jks";
	public static String mAndroidJarPath  = "/tmp/build-tools/lib/android.jar";
	public static String mAndroidSdkPath  = ApkDecompiler.mAndSdkPath;

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
		mApkFileName      = null;
	}

	public  String getPath(String findPath,Class mClass) throws IOException {
		return  mClass.getResource(findPath).getPath();
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

}
