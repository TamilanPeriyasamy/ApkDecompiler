/**
 * 
 */
package com.apkdecompiler.resources;

import org.w3c.dom.Element;

import com.apkdecompiler.conversion.Convert;

/**
 * @author ${Periyasamy C}
 *
 * 21-Sep-2018
 */
public class AttrResources  extends Resources {
	

	private static final int TYPE_ENUM       = 0x00010000;
	private static final int TYPE_FLAGS      = 0x00020000;


	private String getArrayType(int mType) {
		String arrayType="";
		if(( mType & TYPE_ENUM) !=0) {
			/** 0x00010000 */ 
			arrayType="enum";

		}else if(( mType & TYPE_FLAGS) !=0) {
			/** 0x00020000 */
			arrayType="flag";

		}else {
			System.err.println("arrayType "+arrayType+" "+Convert.hex2Decimal(arrayType));	
		}
		return arrayType;
	}


	/**
	 * @param currentLine
	 * @param mResConstType
	 * @param itemName 
	 * @param arrayType 
	 * @return
	 */
	private String getAttrResourcesValue(String parseLine,String arrayType) {
		// TODO Auto-generated method stub
		//System.out.println(" "+parseLine);
		String itemValue="";
		if(parseLine.contains("(color)") ) {
			/**  #0 (Key=0x01010097): (color) #00000000 */
			parseLine=parseLine.substring(parseLine.indexOf(": (")+2);
			itemValue=parseLine.substring(parseLine.indexOf(") #")+2).trim();
			itemValue=itemValue.replace("#", "0x");
			itemValue=Convert.getHexFormat(itemValue);
			if(arrayType.contains("enum")) {
				itemValue=""+Convert.hex2Decimal(itemValue);
			}
		}else {
			System.err.println("AttrResources: "+parseLine);
		}
		return itemValue;
	}

	/**
	 * @param currentLine
	 * @param mResRawType
	 * @param mResConstType
	 */

	public static String mResValue = null;
	public void parseAttrResources(String currentLine) {

		if(currentLine.trim().startsWith("#0 (Key=0x") ) { 
			/**  #0 (Key=0x01000000): (color) #00000001 */
			mResValue= currentLine.substring(currentLine.lastIndexOf(" #")+2);
			String formatType=ResourcesManager.getFormatType(Convert.hex2Decimal(mResValue));
			mChildElement.setAttribute("name", mResName);
			if(formatType!=null) {
				mChildElement.setAttribute("format",formatType);
			}
			mRootElement.appendChild(mChildElement);
			String mAttrRawType=mResName+"("+mResId+")";
			if(formatType!=null) {
				//System.out.println(mAttrRawType+" "+formatType);
				AppResources.mAppAttrResourceTypes.put(mAttrRawType,formatType);
			}
				
		}else if( currentLine.trim().contains(" (Key=0x") ) { 
			/**  #1 (Key=0x7f0b0009): (color) #00000001
                 #2 (Key=0x7f0b000a): (color) #00000000
                 #3 (Key=0x7f0b000b): (color) #00000002 */
			String itemName  ="";
			String itemValue ="";
			String keyValue=currentLine.substring(currentLine.indexOf(" (Key=0x")+6, currentLine.indexOf("): "));
			String arrayType=getArrayType(Convert.hex2Decimal(mResValue));
            
			if(keyValue.startsWith(AppConfig.mPackageId)) {
				itemName=""+AppResources.mAppResourcesMap.get(keyValue);
				itemName=itemName.substring(itemName.indexOf("/")+1);
			}
			if(keyValue.startsWith(AppConfig.mAndPackageId)) {
				itemName=AndroidResources.mAndroidResourcesMap.get(keyValue);
				itemName="android:"+itemName.substring(itemName.indexOf("/")+1);
			}
			
			itemValue=getAttrResourcesValue(currentLine,arrayType);
			Element newElement = mDocument.createElement(arrayType);
			newElement.setAttribute("name", itemName);
			newElement.setAttribute("value", itemValue);
			mChildElement.appendChild(newElement);
		}else {
			System.err.println(" "+currentLine);
		}
	}
}
