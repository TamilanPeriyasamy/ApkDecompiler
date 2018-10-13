/**
 * 
 */
package com.apkdecompiler.resources;

/**
 * @author ${Periyasamy C}
 *
 * 23-Sep-2018
 */
public class BooleanResources  extends Resources{
	
	
	
	public BooleanResources() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param currentLine
	 * @param mResRawType
	 * @param mResConstType
	 */
	public void parseBooleanResources(String currentLine,String mResConstType) {
		// TODO Auto-generated method stub
		if(currentLine.trim().startsWith("(") && !currentLine.trim().startsWith("(string8)")) {
			//System.out.println(" "+currentLine);
			String mResValue=ResourcesManager.getResourcesValue(currentLine,mResConstType);
			mChildElement.setAttribute("name", mResName);
			mChildElement.setTextContent(mResValue);
			mRootElement.appendChild(mChildElement);
		}
	}
}
