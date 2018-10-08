package com.apkdecompiler.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.apkdecompiler.conversion.Convert;
import com.apkdecompiler.main.ApkDecompiler;
import com.apkdecompiler.main.CommandExecutor;
import com.apkdecompiler.xmlbuilder.XMLBuilder;

public class AndroidResources {

	File mApkFile = null;
	public static HashMap<String,String> mAndroidResourcesMap      = new HashMap<String,String>();
	public static HashMap<String,String> mAndroidResourceKeys      = new HashMap<String,String>();
	
	public static HashMap<String,String> mAndroidAttrResourceTypes = new HashMap<String,String>();
	public static HashMap<String,String> mAndroidAttrResourceArray = new HashMap<String,String>();


	//public static String mAttributeRawType = null;
	//public static String mAttributeValue   = null;
	public AndroidResources(){
		mApkFile=new File(ApkDecompiler.mBuildToolsPath+"/framework/1.apk");
	}

	public void parseAndroidSdkResources() throws Exception {

		mAndroidResourcesMap.clear();
		mAndroidResourceKeys.clear();
		String mAttributeId        = null;
		String mAttributeName      = null;
		String mAttributeValue     = null;

		String mAttributeKey       = null;
		String mAttributeRawType   = null;
		String mArrayType          = null;
		String mAttributeValues    = "";
		String valueType=null;

		ArrayList<String> dumpResourcesInputStream  = new CommandExecutor(mApkFile).getResources("dump --values resources");
		for(int parserCount=0;parserCount<dumpResourcesInputStream.size();parserCount++) {

			String currentParser=dumpResourcesInputStream.get(parserCount);
			//System.out.println(" "+currentLine.trim());
			if(currentParser.trim().startsWith("Package ") && currentParser.contains("id=") && currentParser.contains(" packageCount=")) {
				/*** Package Group 0 id=0x7f packageCount=1 name=com.quixxi.audiodemo */
				AppConfig.mAndPackageId=currentParser.substring(currentParser.indexOf("id=")+3, currentParser.indexOf(" packageCount=")).trim();
			}

			if(currentParser.trim().startsWith("resource 0x")) {
				/** resource 0x010100f4 android:attr/layout_width: <bag> (PUBLIC)
		          resource 0x01020000 android:id/background: t=0x12 d=0x00000000 (s=0x0008 r=0x00) (PUBLIC) */

				mAttributeId=currentParser.substring(currentParser.indexOf("resource 0x")+9, currentParser.indexOf(" android:"));
				if(currentParser.contains(": t=0x")) {
					mAttributeName=currentParser.substring(currentParser.indexOf(" android:")+1,currentParser.indexOf(": t=0x"));
				}

				if(currentParser.contains(": <bag> (")) {
					mAttributeName=currentParser.substring(currentParser.indexOf(" android:")+1,currentParser.indexOf(": <bag> ("));
				}
				mAndroidResourcesMap.put(mAttributeId,mAttributeName);

				if( mAttributeRawType!=null && mAttributeValues.length()!=0 ) {
					mAttributeRawType=mAttributeRawType.substring(0, mAttributeRawType.indexOf(")=")+1);
					mAndroidAttrResourceArray.put(mAttributeRawType, mAttributeValues.substring(1));
					mAttributeValues="";
				}
				//mArrayRawType=mAttributeName+"("+mAttributeId+")";
			}

			if(currentParser.trim().startsWith("#")) {
				/** #0 (Key=0x01000000): (color) #00010040 */

				String nameEndswith=mAttributeName.substring(mAttributeName.indexOf("/")+1);
				mAttributeKey=currentParser.substring(currentParser.indexOf(" (Key=0x")+6,currentParser.indexOf("): ")).trim();
				mAttributeValue=currentParser.substring(currentParser.lastIndexOf(" ")).trim();
				mAttributeValue=mAttributeValue.replace("#","0x");
				mAttributeRawType="android:"+nameEndswith+"("+mAttributeId+")="+mAttributeValue;
				mAndroidResourceKeys.put(mAttributeRawType,mAttributeKey);

				if(currentParser.trim().startsWith("#0 (Key=0x") && mAttributeName.contains("attr/")) {
					mArrayType=ResourcesManager.getArrayType(Convert.hex2Decimal(mAttributeValue));
					valueType=ResourcesManager.getFormatType(Convert.hex2Decimal(mAttributeValue));
					String mAttRawType="android:"+nameEndswith+"("+mAttributeId+")";
					if(valueType!=null) {
						mAndroidAttrResourceTypes.put(mAttRawType,valueType);
						//System.out.println(""+mAttRawType);
						//System.out.println(""+mAttributeValue);
						//System.out.println(""+valueType+"\n");
					}
				}
				mAndroidResourceKeys.put(mAttributeRawType.substring(mAttributeRawType.indexOf("=")), mAttributeKey);
				
				if(mArrayType.equals("flag")) { 
					mAttributeValues=mAttributeValues+","+mAttributeValue;
				}
			}
		}
	}

	public static  String getAndroidAttributeValue(String attributeRawType,String hexaDecimal){ 
		int decimal=Convert.hex2Decimal(hexaDecimal);
		attributeRawType=attributeRawType+"="+Convert.getHexFormat(decimal);
		//System.out.println("attributeRawType  "+attributeRawType);
		String mAttributeId=mAndroidResourceKeys.get(attributeRawType);
		//System.out.println("mAttributeId  "+mAttributeId+"\n");
		return getAttributeValue(mAttributeId); 
	}

	public static  String getAttributeValue(String mAttributeId){
		return mAndroidResourcesMap.get(mAttributeId); 
	}

	/**
	 * @param attributeRawType
	 * @param hexaDecimal
	 * @return
	 */
	public static String getFlagTypeValues(String attributeRawType, String hexaDecimal) {
		String finalCombinationValues=null;
		//System.out.println(" "+attributeRawType);
		//System.out.println(" "+hexaDecimal);
		String arrayValues=mAndroidAttrResourceArray.get(attributeRawType);
		//System.out.println(" "+arrayValues);
		if(arrayValues!=null) {
			int intFalgs[]=convertIntegerArray(arrayValues);
			int myType=Convert.hex2Decimal(hexaDecimal);
			finalCombinationValues=ResourcesManager.getCombinationValues(myType,intFalgs,attributeRawType);
		}
		return finalCombinationValues;
	}

	/**
	 * @param arrayValues
	 */
	private static int[] convertIntegerArray(String arrayValues) {
		// TODO Auto-generated method stub
		//System.out.println("arrayValues "+arrayValues);
		String values[]=arrayValues.split(",");
		int intFalgs[]=new int[values.length];
		for(int count=0;count<values.length;count++) {
			intFalgs[count]=Convert.hex2Decimal(values[count]);
		}
		return intFalgs;
	}
}
