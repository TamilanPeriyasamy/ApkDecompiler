package com.apkdecompiler.xmlbuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.apkdecompiler.main.ApkDecompiler;
import com.apkdecompiler.main.CommandExecutor;
import com.apkdecompiler.xmlbuilder.XMLBuilder;

public class DecodeResXMLFiles {

	File mApkFile=null;
	File mApkExtractPath=null;
	public static String  mCurrentXmlName=null;
	public DecodeResXMLFiles() {
		mApkFile=ApkDecompiler.mInputFile;
		mApkExtractPath=ApkDecompiler.mApkBuildDir;
	}

	public void decodeResourcesXMLFiles() throws Exception {
		System.out.println("Decompile AndroidManifest.xml ...");
		ZipFile zipFile = new ZipFile(mApkFile.getAbsolutePath());
		Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
		while(enumeration.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)enumeration.nextElement();
			if(!zipEntry.isDirectory()){ 
				if(zipEntry.getName().equals("AndroidManifest.xml")){
					decodeResourcesXmlFiles(zipEntry.getName(),mApkExtractPath);
				}
			}  
		} 
		zipFile.close(); 
		
		System.out.println("Decompile resources xmls...");
		zipFile = new ZipFile(mApkFile.getAbsolutePath());
		enumeration = zipFile.entries();
		while(enumeration.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)enumeration.nextElement();
			if(!zipEntry.isDirectory()){ 
				if(zipEntry.getName().startsWith("res") && zipEntry.getName().endsWith(".xml") ){
					decodeResourcesXmlFiles(zipEntry.getName(),mApkExtractPath);
				}
			}  
		} 
		zipFile.close();
	}

	private void decodeResourcesXmlFiles(String zipEntryName, File appBuildDir) throws Exception {
		mCurrentXmlName=zipEntryName;
		//System.out.println(""+zipEntryName);
		HashMap<Integer,Element> mElementList = new HashMap<Integer,Element>();
		ArrayList<String> mXmlnsLinks=new ArrayList<String>();
		ArrayList<String> inputStream=new CommandExecutor(mApkFile).getXmlTree("dump xmltree",zipEntryName);
		XMLBuilder XMLBuilder=new XMLBuilder();
		Document document =XMLBuilder.newDocument();

		Element mRootElement    = null;
		Element	mNewElement     = null;
		int mCurrentIndent   = 0;
		String currentline      = null;
		for(int lineCount=0;lineCount<inputStream.size();lineCount++) {
			currentline =inputStream.get(lineCount);
			//System.out.println(""+currentline);

			if(currentline.contains("E: uses-sdk ") || currentline.contains("android:minSdkVersion(") || currentline.contains("android:targetSdkVersion(") || currentline.contains("android:versionCode(") || currentline.contains("android:versionName(") ) {
				//continue;
			}

			if(currentline.trim().startsWith("N:")) {
				/** N: android=http://schemas.android.com/apk/res/android */
				//System.out.println(" "+currentline);
				String xmlnsLink=currentline.replace("N: ","xmlns:").trim();
				mXmlnsLinks.add(xmlnsLink);

			}else if(currentline.trim().startsWith("E:")) {
				String mTagName = null;
				if(mRootElement==null) {
					mCurrentIndent=currentline.indexOf("E:");
					mTagName=currentline.substring(currentline.indexOf("E:")+2, currentline.indexOf(" (line=")).trim();
					mRootElement= mNewElement = document.createElement(mTagName);
					if(mXmlnsLinks.size()!=0) {
						XMLBuilder.setXmlnskAttributes(mNewElement,mXmlnsLinks);
					}
					document.appendChild(mNewElement);
					mElementList.put(new Integer(mCurrentIndent), mNewElement);

				}else {
					mCurrentIndent=currentline.indexOf("E:");
					mTagName=currentline.substring(currentline.indexOf("E:")+2, currentline.indexOf(" (line=")).trim();
					mNewElement = document.createElement(mTagName);
					Element mPreElement= getParentElement(mElementList,mCurrentIndent);
					if(mXmlnsLinks.size()!=0) {
						XMLBuilder.setXmlnskAttributes(mNewElement,mXmlnsLinks);
					}
					mPreElement.appendChild(mNewElement);
					mElementList.put(new Integer(mCurrentIndent), mNewElement);
				}
				mXmlnsLinks.clear();

			}else if(currentline.trim().startsWith("C:")) {
				if(document!=null && mNewElement!=null) {
					Comment comment = document.createComment(currentline.trim());
					mNewElement.getParentNode().insertBefore(comment, mNewElement);
				}

			}else if(currentline.trim().startsWith("A:") ) {
				XMLBuilder.setAttributes(mNewElement,currentline.replace("A:", "").trim());

			}else {
				System.err.println("Missing Element ("+currentline.trim()+") for "+mCurrentXmlName);
			}
		}
		XMLBuilder.createXMLFile(document,new File(appBuildDir+File.separator+zipEntryName),false);
	}

	private Element getParentElement(HashMap<Integer, Element> mElementList, int mPreElementIndent) {
		int indentValue=mPreElementIndent-2;
		while(mElementList.get(indentValue)==null) {
			indentValue=indentValue-2;
		}
		return mElementList.get(indentValue);
	}
}
