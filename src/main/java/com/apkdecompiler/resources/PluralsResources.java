/**
 * 
 */
package com.apkdecompiler.resources;

import org.w3c.dom.Element;

import com.apkdecompiler.conversion.Convert;

/**
 * @author ${Periyasamy C}
 *
 * 24-Sep-2018
 */
public class PluralsResources extends Resources{
	
	
	public PluralsResources() {
		// TODO Auto-generated constructor stub
	}

	public static final int PLURALS_START = 0x01000004;
	public static final int PLURALS_END   = 0x01000009;
	public static final String[] QUANTITY_MAP = new String[] { "other", "zero", "one", "two", "few", "many" };
	
	/**
	 * @param keyValue 
	 * @return
	 */
	private String getQuantity(String keyValue) {
		return  QUANTITY_MAP[ Convert.hex2Decimal(keyValue)-PLURALS_START ];
	}

	/**
	 * @param parseLine
	 * @param mResRawType
	 * @param mResConstType
	 */
	public void parsePluralsResources(String parseLine, String mResConstType) {

		if(parseLine.trim().startsWith("Parent=0x")) {
			String mResName=mResRawType.substring(mResRawType.lastIndexOf("/")+1);
			mChildElement.setAttribute("name", mResName);
			mRootElement.appendChild(mChildElement);
		}

		if(parseLine.contains(" (Key=") && parseLine.contains("(string8)")) {
			/**  #0 (Key=0x01000004): (string8) "in %d seconds" */
			String keyValue=parseLine.substring(parseLine.indexOf(" (Key=0x")+6, parseLine.indexOf("): "));
			Element newElement = mDocument.createElement("item");
			newElement.setAttribute("quantity", getQuantity(keyValue));
			String stringValue=parseLine.substring(parseLine.indexOf("(string8) \"")+11, parseLine.lastIndexOf("\""));
			//String stringValue=parseLine.substring(parseLine.indexOf("(string8) ")+10);
			newElement.setTextContent(new String(stringValue));
			mChildElement.appendChild(newElement);
		}
	}	
}
