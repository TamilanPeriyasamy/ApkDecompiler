/**
 * 
 */
package com.apkdecompiler.resources;

import com.apkdecompiler.conversion.Convert;

/**
 * @author ${Periyasamy C}
 *
 * 23-Sep-2018
 */
public class IdResources extends Resources{
	
	
	public IdResources() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param currentLine
	 * @param mResRawType
	 * @param mResConstType
	 */
	public void parseIdResources(String currentLine,String mResConstType) {
	
		mChildElement.setAttribute("type", "id");
		mChildElement.setAttribute("name", mResName);
		String hexDecimal=currentLine.substring(currentLine.indexOf(") ")+2);
		if(hexDecimal.length()!=2) {	
			String mBoolean=""+Convert.hex2Boolean(hexDecimal);
			mChildElement.setTextContent(mBoolean);
		}
		mRootElement.appendChild(mChildElement);
	}
}
