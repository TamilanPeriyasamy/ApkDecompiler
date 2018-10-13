/**
 * 
 */
package com.apkdecompiler.resources;

/**
 * @author ${Periyasamy C}
 *
 * 23-Sep-2018
 */
public class ColorResources extends Resources{
	
	
	public ColorResources() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param currentLine
	 * @param mResRawType
	 * @param mResConstType
	 */
	public void parseColorResources(String currentLine,String mResConstType) {
		
		if(currentLine.trim().startsWith("(") && !currentLine.trim().startsWith("(string8)")) {
			
			String mResValue=ResourcesManager.getResourcesValue(currentLine,mResConstType);
			mChildElement.setAttribute("name", mResName);
			mChildElement.setTextContent(mResValue);
			mRootElement.appendChild(mChildElement);
		}
	}
}
