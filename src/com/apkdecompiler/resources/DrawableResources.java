/**
 * 
 */
package com.apkdecompiler.resources;

/**
 * @author ${Periyasamy C}
 *
 * 23-Sep-2018
 */
public class DrawableResources extends Resources{
	
	
	
	public DrawableResources() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param currentLine
	 * @param mResRawType
	 * @param mResConstType
	 */
	public void parseDrawableResources(String currentLine, String mResConstType) {
		
		if(!currentLine.trim().startsWith("(string8)")) {
			String	mResValue=ResourcesManager.getResourcesValue(currentLine,mResConstType);
			mChildElement.setAttribute("type", "drawable");
			mChildElement.setAttribute("name", mResName);
			mChildElement.setTextContent(mResValue);
			mRootElement.appendChild(mChildElement);
		}
	}
}
