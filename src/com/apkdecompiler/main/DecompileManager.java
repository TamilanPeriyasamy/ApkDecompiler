package com.apkdecompiler.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;

import com.apkdecompiler.filemanager.Files;
import com.apkdecompiler.resources.AndroidResources;
import com.apkdecompiler.resources.AppConfig;
import com.apkdecompiler.resources.AppResources;
import com.apkdecompiler.xmlbuilder.DecodeResXMLFiles;

public class DecompileManager extends Files {


	public String mApkExtractPath  = null;

	public DecompileManager() {		
	}

	/**
	 * @param mInputPath
	 * @param mOutputPath
	 * @throws Exception 
	 */
	public DecompileManager(String mInputPath, String mOutputPath) throws Exception {
		if(new File(mInputPath).isFile() && new File(mInputPath).getName().endsWith(".apk")) {
			if(new File(mOutputPath).isDirectory()) {

				Files.mInputApkFile    = new File(mInputPath);
				Files.mOutputDirectory = new File(mOutputPath);
				Files.mApkFileName     = new File(mInputPath).getName().replace(".apk", "");
				Files.mApkBuildDir     = new File(mOutputPath+File.separator+Files.mApkFileName);
				Files.mApkResDir       = new File(mApkBuildDir+File.separator+"res");
				if(!Files.mApkBuildDir.exists()) {
					Files.mApkBuildDir.mkdirs();
				}
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

	public void extractingApkFiles() throws Exception {

		mApkExtractPath  = Files.mApkBuildDir.getAbsolutePath();
		if(!new File(mApkExtractPath).exists()) {
			new File(mApkExtractPath).mkdirs();
		}	
		//System.out.println(" "+mApkExtractPath);
		//System.out.println(" "+mApkFile);
		int bytes=0;
		byte buffer[] = new byte[1024];
		ZipFile zipFile = new ZipFile(Files.mInputApkFile);
		Enumeration<? extends ZipEntry> e = zipFile.entries();
		while (e.hasMoreElements()) {
			ZipEntry entry = e.nextElement();
			File destinationFile = new File(mApkExtractPath, entry.getName());
			destinationFile.getParentFile().mkdirs();
			if (!entry.isDirectory()) {
				//System.out.println(" extract... " + destinationFile);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(entry));
				FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);
				while ((bytes = bufferedInputStream.read(buffer, 0, 1024)) != -1) {
					bufferedOutputStream.write(buffer, 0, bytes);
				}
				bufferedOutputStream.close();
				bufferedInputStream.close();
			}
		}
		zipFile.close();
	}
	
	private void decompileApkFile() throws Exception {

		AppConfig mApplicationConfig=new AppConfig();
		mApplicationConfig.getAppDetails();

		System.out.println("Decompile apk ...");
		DecompileManager apkManager=new DecompileManager();
		apkManager.extractingApkFiles();

		System.out.println("Load android sdk resources ...");
		AndroidResources androidSdkResources=new AndroidResources();
		androidSdkResources.parseAndroidSdkResources();

		AppResources mApplicationResources=new AppResources();
		mApplicationResources.parseApplicationResources();

		DecodeResXMLFiles decodeXMLFiles=new DecodeResXMLFiles();
		decodeXMLFiles.decodeResourcesXMLFiles();
	}
}
