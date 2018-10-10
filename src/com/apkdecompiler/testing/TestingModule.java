/**
 * 
 */
package com.apkdecompiler.testing;

import com.apkdecompiler.filemanager.Files;
import com.apkdecompiler.logger.LogFile;
import com.apkdecompiler.main.BuildManager;
import com.apkdecompiler.main.DecompileManager;

/**
 * @author ${Periyasamy C}
 *
 * 08-Oct-2018
 *
decompile
-in /home/MyDev/ActivityDeveloper.apk
-out /home/workspace/ApkDecompiler-master/ApkDecompiler/debug
 
 build
 sign
-in /home/workspace/ApkDecompiler-master/ApkDecompiler/debug/ActivityDeveloper
-out /home/workspace/ApkDecompiler-master/ApkDecompiler/output/ActivityDeveloper.apk
 */
public class TestingModule {

	public void runTestBuild() throws Exception {
		LogFile.toStartWrite();
		Files.refreshFilePath();//Files.mUserDir+
		String mInputApkPath=Files.mUserDir+"/test/ActivityDeveloper.apk";
		String mOutputDirPath=Files.mUserDir+"/debug";
		new DecompileManager(mInputApkPath,mOutputDirPath);
		
		Files.refreshFilePath();
		String mInputDirPath=Files.mUserDir+"/debug/ActivityDeveloper";
		String mOutputApkPath=Files.mUserDir+"/output/ActivityDeveloper.apk";
		new BuildManager(mInputDirPath,mOutputApkPath,true);
		LogFile.toStopWrite();
		System.out.println("=== ApkDecompiler ");
		System.exit(0);
	}
}
