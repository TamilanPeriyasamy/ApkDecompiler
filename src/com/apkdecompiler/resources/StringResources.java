/**
 * 
 */
package com.apkdecompiler.resources;


/**
 * @author ${Periyasamy C}
 *
 * 22-Sep-2018
 */
public class StringResources extends Resources{

	public StringResources() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param currentLine
	 * @param mResRawType
	 * @param mResConstType
	 */
	public void parseStringResources(String currentLine,String mResConstType) {
         
		if(currentLine.trim().startsWith("(string8) ")) {
			
			mChildElement.setAttribute("name", mResName);
			String stringValue=currentLine.replace("(string8) ", "").trim();
			if(stringValue.contains("%")) {
				mChildElement.setAttribute("formatted", "false");	
			}
			mChildElement.setTextContent(new String(stringValue));
			mRootElement.appendChild(mChildElement);
			
		}else {
			
			mChildElement.setAttribute("name", mResName);
			String stringValue=ResourcesManager.getResourcesValue(currentLine,mResConstType);
			mChildElement.setTextContent(stringValue);
			mRootElement.appendChild(mChildElement);
		}
	}
	
}
