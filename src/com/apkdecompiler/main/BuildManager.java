/**
 * 
 */
package com.apkdecompiler.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.apkdecompiler.filemanager.Files;

/**
 * @author ${Periyasamy C}
 *
 * 09-Oct-2018
 */
public class BuildManager extends Files {


	public File mOutputApk         = null;
	public File mOutputDir         = null;
	public String mApkExtractPath  = null;
	public String outputApkPath    = null;
	public String debugApkPath     = null;
	ArrayList<String> mRawFileList = new ArrayList<String>();

	/**
	 * @param mInputPath
	 * @param mOutputPath
	 * @param mSign 
	 * @throws Exception 
	 */
	public BuildManager(String mInputPath, String mOutputPath, boolean mSign) throws Exception {

		if(new File(mInputPath).isDirectory() && new File(mInputPath).exists()) {
			if(new File(mOutputPath).getName().endsWith(".apk")) {
				Files.mOutputApkFile  = new File(mOutputPath);
				Files.mInputDirectory = new File(mInputPath);
				Files.mApkFileName    = Files.mOutputApkFile.getName().replace(".apk", "");
				Files.mApkBuildDir    = new File(mInputPath);
				buildApkFile();
			}else {
				System.err.println(" output file is invalid "+mOutputPath);
				System.exit(0);
			}
		}else {
			System.err.println(" input Directory is invalid "+mInputPath);
			System.exit(0);
		}
		if(mSign) {
			signApkFile();
		}
	}

	public void buildApkFile() throws Exception {
		System.out.println("\nBuild apk ...");
		mOutputApk       = Files.mOutputApkFile;
		mOutputDir       = new File(mOutputApk.getParent());
		outputApkPath    = mOutputApk.getAbsolutePath();
		debugApkPath     = mOutputApk.getAbsolutePath().replace(mOutputApk.getName(),"debug-"+mOutputApk.getName());

		String unsingApkFilePath=mOutputDir.getAbsolutePath()+File.separator+mOutputApk.getName();
		if(new File(unsingApkFilePath).exists()) {
			new File(unsingApkFilePath).delete();
		}

		mRawFileList.clear();
		copyApkRawFiles(Files.mApkBuildDir,mOutputDir);
		renamedPngImageFiles(Files.mApkBuildDir.getAbsolutePath());
		System.out.println(" "+unsingApkFilePath);
		String runcommand=Files.mAAPTPath+" p -f -F "+unsingApkFilePath+" -I "+Files.mAndroidJarPath+" -S "+Files.mApkBuildDir.getAbsolutePath()+"/res "+" -M "+Files.mApkBuildDir.getAbsolutePath()+"/AndroidManifest.xml";
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
		cleanOutputDir(mOutputDir);
	}

	public void signApkFile() throws Exception{
		// "Sign an android apk file use a test certificate.");
		String mAliasName ="key0".trim() ,mKeyStorePass="Test@123".trim() ,mKeyPass="Test@123".trim();
		String zipalignApkPath=apkZipalign(outputApkPath);
		String runcommand=Files.mApkSigner+" sign --ks "+Files.mKeyStorePath+" --ks-key-alias "+mAliasName+" --ks-pass pass:"+mKeyStorePass+" --key-pass pass:"+mKeyPass+" --out "+debugApkPath+" "+zipalignApkPath;
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
		String runcommand=Files.mZipalign+" -f -v 4 "+apkFilePath+" "+zipalignApkPath;
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
				if(!currentFile.getAbsolutePath().equals(outputApkPath.trim()) 
						&& !currentFile.getAbsolutePath().equals(outputApkPath.trim())) {
					Files.delete(currentFile);
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
					String desFilePath=sourceFilePath.replace(Files.mApkBuildDir.getAbsolutePath(), mOutputDir.getAbsolutePath());
					String currentFilePath=sourceFilePath.replace(Files.mApkBuildDir.getAbsolutePath()+"/","");
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
