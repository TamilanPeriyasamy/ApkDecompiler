/**
 * 
 */
package com.apkdecompiler.resources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author ${Periyasamy C}
 *
 * 29-Sep-2018
 */
public class Resources {
	
	static Document mDocument       = null;
	static Element  mRootElement    = null;
	static Element  mChildElement   = null;
	
	static String mResRawType      = null;
	static String mResId           = null;
	static String mResTagName      = null;
	static String mResName         = null;
	static String mResConstType    = null;
	
	/**
	 * 
	 */
	public Resources() {
		// TODO Auto-generated constructor stub
	} 

	public Resources(Document mDocument, Element mRootElement, String parseLine, String mResId) {
		// TODO Auto-generated constructor stub
				Resources.mDocument    = mDocument;
				Resources.mRootElement = mRootElement;
				Resources.mResId       = mResId;
				Resources.mResRawType  = AppResources.mAppResourcesMap.get(mResId);
				
				mResTagName=mResRawType.substring(0,mResRawType.lastIndexOf("/")).trim();
				mResName=mResRawType.substring(mResRawType.lastIndexOf("/")+1);
				
				if(mResRawType.contains("id/") || mResRawType.contains("drawable/")) {
					mResTagName="item";
				}
				mChildElement=mDocument.createElement(mResTagName);
	}
}
