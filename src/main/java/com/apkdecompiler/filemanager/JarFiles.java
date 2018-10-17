package com.apkdecompiler.filemanager;

import org.apache.commons.io.IOUtils;

import java.io.*;

public class JarFiles {


    public String mAAPTPath       = "/build-tools/aapt";
    public String mZipalign       = "/build-tools/zipalign";
    public String mKeyStorePath   = "/build-tools/key-store.jks";
    public String mLibcPath       = "/build-tools/lib64/libc++.so";
    public String mAndroidJarPath = "/build-tools/lib/android.jar";
    public String mFrameworkPath  = "/framework/decode.apk";

    public void loadBuildTools() throws Exception {
        String toolsPath[] = {mAAPTPath, mZipalign, mKeyStorePath, mLibcPath, mAndroidJarPath,mFrameworkPath};
        String tmpDirPath = "/tmp/";
        for (int count = 0; count < toolsPath.length; count++) {
            dumpBuildTools(tmpDirPath, toolsPath[count], getClass());
        }
    }

    public File dumpBuildTools(String tmpDirPath, String resourcePath, Class clazz) throws Exception {

        File fileOut = new File(tmpDirPath+resourcePath);
        //System.out.println(""+fileOut.getAbsolutePath());
        if(!fileOut.exists()){
            new File(fileOut.getParent()).mkdirs();
            InputStream in = clazz.getResourceAsStream(resourcePath);
            if (in == null) {
                throw new FileNotFoundException(resourcePath);
            }
            OutputStream out = new FileOutputStream(fileOut);
            IOUtils.copy(in, out);
            in.close();
            out.close();
            fileOut.setExecutable(true);
        }
        return fileOut;
    }
}
