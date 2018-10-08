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
import com.apkdecompiler.resources.AppResources;

public class ApkManager {

	public File mOutputApk         = null;
	public File mApkBuildDir       = null;
	public File mOutputDir         = null;
	public String mApkExtractPath  = null;
	public String outputApkPath    = null;
	public String debugApkPath     = null;
	public String zipalign         = null;
	public String KeyStorePath     = null;
	public String apkSigner        = null;
	public String mAliasName ="key0".trim() ,mKeyStorePass="Test@123".trim() ,mKeyPass="Test@123".trim();
	ArrayList<String> mRawFileList = new ArrayList<String>();
	
	public ApkManager() {
	
		mApkBuildDir     = Files.mApkBuildDir;
		mOutputApk       = Files.mOutputFile;
		zipalign         = Files.mZipalign;
		apkSigner        = Files.mApkSigner;
		KeyStorePath     = Files.mBuildToolsPath+File.separator+"key-store.jks";
		mApkExtractPath  = Files.mApkBuildDir.getAbsolutePath();
		mOutputDir       = new File(mOutputApk.getParent());
		outputApkPath    = mOutputApk.getAbsolutePath();
		debugApkPath     = mOutputApk.getAbsolutePath().replace(mOutputApk.getName(),"debug-"+mOutputApk.getName());
	}


	public static void cleanAppDir() throws IOException {
		//FileUtils.cleanDirectory(new File(Files.mUserDir+"/build/"));
		//FileUtils.cleanDirectory(new File(Files.mUserDir+"/output/"));
	}

	public void extractingApkFiles() throws Exception {
		if(!new File(mApkExtractPath).exists()) {
			new File(mApkExtractPath).mkdirs();
		}	
		//System.out.println(" "+mApkExtractPath);
		//System.out.println(" "+mApkFile);
		int bytes=0;
		byte buffer[] = new byte[1024];
		ZipFile zipFile = new ZipFile(Files.mInputFile);
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

	public void buildApkFiles() throws Exception {
		System.out.println("\nBuild apk ...");
		String unsingApkFilePath=mOutputDir.getAbsolutePath()+File.separator+mOutputApk.getName();
		if(new File(unsingApkFilePath).exists()) {
			new File(unsingApkFilePath).delete();
		}

		mRawFileList.clear();
		copyApkRawFiles(mApkBuildDir,mOutputDir);
		renamedPngImageFiles(mApkBuildDir.getAbsolutePath());
		
		String runcommand=Files.mAAPTPath+" p -f -F "+unsingApkFilePath+" -I "+Files.mAndroidJarPath+" -S "+mApkBuildDir.getAbsolutePath()+"/res "+" -M "+mApkBuildDir.getAbsolutePath()+"/AndroidManifest.xml";
		System.out.println("build apk resources ... ");
		System.out.println(""+runcommand);
		if(!new CommandExecutor().executeCommand(runcommand,false)) {
			System.err.println("Apk build failed... ");
			System.exit(0);
		}

		System.out.println("added apk Raw resources ... ");
		for(int fileCount=0;fileCount<mRawFileList.size();fileCount++) {
			String filePath=mRawFileList.get(fileCount);
			String runcommand1 =Files.mAAPTPath+" add -v "+mOutputApk.getName()+" \""+filePath+"\"";
			//System.out.println("add file... "+runcommand1);
			String[] runcommands = { "/bin/sh", "-c", "cd "+mOutputDir+File.separator+";"+ runcommand1 };
			if(!new CommandExecutor().executeCommand(runcommands,false)) {
				System.err.println("added Raw resources failed... ");
				System.exit(0);
			}
		}
		//cleanOutputDir(mOutputDir);
	}




	public void signApkFile() throws Exception{
		String zipalignApkPath=apkZipalign(outputApkPath);
		String runcommand=apkSigner+" sign --ks "+KeyStorePath+" --ks-key-alias "+mAliasName+" --ks-pass pass:"+mKeyStorePass+" --key-pass pass:"+mKeyPass+" --out "+debugApkPath+" "+zipalignApkPath;
		System.out.println("Sign apk file ... ");
		//System.out.println(""+runcommand);
		if(!new CommandExecutor().executeCommand(runcommand,false)) {
			System.err.println("Apk sign failed... ");
			System.exit(0);
		}
		if(new File(zipalignApkPath).exists()) {
			new File(zipalignApkPath).delete();
		}
	}

	/**
	 * @throws Exception 
	 * 
	 */
	private String apkZipalign(String apkFilePath) throws Exception {
		//$ZipalignToolPath -f -v 4 $InputApkPath $tempOutput
		String zipalignApkPath=apkFilePath.replace(".apk", "1.apk");
		String runcommand=zipalign+" -f -v 4 "+apkFilePath+" "+zipalignApkPath;
		//System.out.println("apk zipalign "+runcommand);
		if(!new CommandExecutor().executeCommand(runcommand,false)) {
			System.err.println("Apk zipalign failed... ");
			System.exit(0);
		}
		return zipalignApkPath;
	}

	/**
	 * @param mOutputDir2
	 * @throws IOException 
	 */
	private void cleanOutputDir(File mOutputDir) throws IOException {
		String[] listofFiles = mOutputDir.list();
		for (String fileNme : listofFiles) {
			if( new File(mOutputDir+File.separator+fileNme).exists()) {
				File currentFile=new File(mOutputDir+File.separator+fileNme);
				if(currentFile.isDirectory()) {
					FileUtils.deleteDirectory(currentFile);
				}else {
					if(currentFile.isFile() && !currentFile.getName().equals(mOutputApk.getName())) {
						currentFile.delete();
					}
				}
			}
		}
	}

	private void copyApkRawFiles(File buildDir,File outPutDir) throws IOException {
		File inputPath =  new File(buildDir.getAbsolutePath());
		if(inputPath.isDirectory()){
			File[] listofFiles = inputPath.listFiles();
			for (File file : listofFiles) {
				if( file.isFile() && !file.getAbsolutePath().contains("/res/")  && !file.getAbsolutePath().endsWith("AndroidManifest.xml")  && !file.getAbsolutePath().endsWith("resources.arsc") ){
					String sourceFilePath=file.getAbsolutePath();
					String desFilePath=sourceFilePath.replace(mApkBuildDir.getAbsolutePath(), mOutputDir.getAbsolutePath());
					String currentFilePath=sourceFilePath.replace(mApkBuildDir.getAbsolutePath()+"/","");
					//currentFilePath=currentFilePath.replaceAll(" ","\\ ");
					mRawFileList.add(currentFilePath);
					new File(new File(desFilePath).getParent()).mkdirs();
					if(new File(sourceFilePath).exists()) {
						FileUtils.copyFile(new File(sourceFilePath),new File(desFilePath));	
					}	
				}else {
					copyApkRawFiles(file,outPutDir);
				}
			} 
		}
	}


	private void renamedPngImageFiles(String resDirPath) throws IOException {
		File inputPath =  new File(resDirPath);
		if(inputPath.isDirectory()){
			File[] listofFiles = inputPath.listFiles();
			for (File file : listofFiles) {
				if( file.isFile() && file.getAbsolutePath().endsWith(".9.png")){
					String filePath = file.getAbsolutePath();
					File newOutputFile = new File(filePath.replace(".9.png", ".jpeg"));
					if(!file.renameTo(newOutputFile)) {
						throw new IOException("File does not exist "+file.getAbsolutePath());
					} 
				}else {
					renamedPngImageFiles(file.getAbsolutePath());
				}
			} 
		}
	}
}
