/**
 * 
 */
package com.apkdecompiler.resources;
import org.w3c.dom.Element;
import com.apkdecompiler.conversion.Convert;

/**
 * @author ${Periyasamy C}
 *
 * 25-Sep-2018
 */
public class ArrayResources extends Resources {


	public ArrayResources() {
		// TODO Auto-generated constructor stub
	}


	private String getItemValue(String parseEndsWith, String keyValue) {
		// TODO Auto-generated method stub
		//System.out.println(" "+parseLine);
		String itemValue="";
		if(parseEndsWith.trim().startsWith("(reference)")){
			/**  (reference) 0x7f0a0025 */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf(") ")+2).trim();
			String hexaValue=Convert.getHexFormat(itemValue);
			if(hexaValue.startsWith(AppConfig.mPackageId)) {
				itemValue="@"+AppResources.mAppResourcesMap.get(hexaValue);	

			}else if(hexaValue.startsWith(AppConfig.mAndPackageId)) {
				itemValue="@"+AndroidResources.mAndroidResourcesMap.get(hexaValue);	

			}else {
				itemValue="@null";
			}//: (string8) "#
		}else  if(parseEndsWith.contains("(string8) \"#") ) {
			/** #6 (Key=0x7f010004): (string8) "fonts/Lato-Regular.ttf" */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf("(string8) \"")+11, parseEndsWith.lastIndexOf("\""));
			itemValue="\\"+itemValue.trim();
			
		}else  if(parseEndsWith.contains("(string8) \"") ) {
			/** #6 (Key=0x7f010004): (string8) "fonts/Lato-Regular.ttf" */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf("(string8) \"")+11, parseEndsWith.lastIndexOf("\""));
			
		}else  if(parseEndsWith.contains("(color)") ) {
			itemValue=parseEndsWith.replace("(color)", "").trim();
		}else {
			
			System.err.println("ArrayResources: "+parseEndsWith);
		}
		return itemValue;
	}

	/**
	 * @param parseLine
	 * @param mResRawType
	 * @param mResConstType
	 */
	public void parseArrayResources(String parseLine) {
		// TODO Auto-generated method stub
		
		if(parseLine.trim().startsWith("#0 (Key=0x")) {
			String resTagName=mResTagName;
			if(parseLine.contains(" (string8) ")) {//string-
				resTagName="string-"+resTagName;
			}
			mChildElement=mDocument.createElement(resTagName);
			mChildElement.setAttribute("name", mResName);
			mRootElement.appendChild(mChildElement);
			String keyValue=parseLine.substring(parseLine.indexOf(" (Key=0x")+6, parseLine.indexOf("): "));
			Element newElement = mDocument.createElement("item");
			String parseEndsWith=parseLine.substring(parseLine.indexOf(": (")+2);
			String itemValue=getItemValue(parseEndsWith, keyValue);
			newElement.setTextContent(new String(itemValue));
			mChildElement.appendChild(newElement);
			
		}else if(parseLine.contains(" (Key=")) {
			/**  #0 (Key=0x01000004): (string8) "in %d seconds" */
			String keyValue=parseLine.substring(parseLine.indexOf(" (Key=0x")+6, parseLine.indexOf("): "));
			Element newElement = mDocument.createElement("item");
			String parseEndsWith=parseLine.substring(parseLine.indexOf(": (")+2);
			String itemValue=getItemValue(parseEndsWith, keyValue);
			newElement.setTextContent(new String(itemValue));
			mChildElement.appendChild(newElement);	
		}

	}
}
