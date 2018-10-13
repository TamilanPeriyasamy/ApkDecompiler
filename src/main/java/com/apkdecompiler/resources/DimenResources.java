/**
 * 
 */
package com.apkdecompiler.resources;

import org.w3c.dom.Element;

/**
 * @author ${Periyasamy C}
 *
 * 23-Sep-2018
 */
public class DimenResources extends Resources{
	
	
	public DimenResources() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param currentLine
	 * @param mResRawType
	 * @param mResConstType
	 */
	public void parseDimenResources(String currentLine, String mResConstType) {
		
		if(currentLine.trim().startsWith("(") && !currentLine.trim().startsWith("(string8)")) { 
			
			String mResValue=ResourcesManager.getResourcesValue(currentLine,mResConstType);
			if(currentLine.trim().startsWith("(dimension)")) {
				/** <dimen name="abc_alert_dialog_button_bar_height">48.0dip</dimen> */
				mChildElement.setAttribute("name", mResName);
				mChildElement.setTextContent(mResValue);
				mRootElement.appendChild(mChildElement);
				
			}else {
				/**  <item type="dimen" name="abc_dialog_fixed_height_minor">100.0%</item> */
				Element mItemElement = mDocument.createElement("item");
				mItemElement.setAttribute("type", "dimen");
				mItemElement.setAttribute("name", mResName);
				mItemElement.setTextContent(mResValue);
				mRootElement.appendChild(mItemElement);
				
			}
		}
	}

}
