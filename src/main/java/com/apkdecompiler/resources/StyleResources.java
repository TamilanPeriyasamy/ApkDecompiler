/**
 * 
 */
package com.apkdecompiler.resources;

import org.w3c.dom.Element;

import com.apkdecompiler.conversion.Constant;
import com.apkdecompiler.conversion.Convert;

/**
 * @author ${Periyasamy C}
 *
 * 21-Sep-2018
 */
public class StyleResources extends Resources {


	public StyleResources() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param currentLine
	 * @param mResConstType
	 * @param itemName 
	 * @return
	 */
	private String getStyleResourcesValue(String parseEndsWith, String keyValue, String itemName) {
		// TODO Auto-generated method stub
		//System.out.println(" "+parseLine);
		String itemValue="";
		if(parseEndsWith.trim().startsWith("(dimension)")) {
			/** (dimension) 8.000000dp */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf(") ")+2).trim();

		}else if(parseEndsWith.trim().startsWith("(float)")){
			/**  (float) 0.3 */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf(") ")+2).trim();

		}else if(parseEndsWith.trim().startsWith("(fraction)")){	
			/**  (fraction) 0.550000% */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf(") ")+2).trim();

		}else if(parseEndsWith.trim().startsWith("(reference)")){
			/**  (reference) 0x7f0a0025 */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf(") ")+2).trim();
			String hexaValue=Convert.getHexFormat(itemValue);
			if(hexaValue.startsWith(AppConfig.mPackageId)) {
				itemValue="@"+AppResources.mAppResourcesMap.get(hexaValue);	

			}else if(hexaValue.startsWith(AppConfig.mAndPackageId)) {
				itemValue="@"+AndroidResources.mAndroidResourcesMap.get(hexaValue);	

			}else {
				itemValue="@null";
			}

		}else if(parseEndsWith.trim().startsWith("(attribute)")){
			/**  (reference) 0x7f0a0025 */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf(") ")+2).trim();
			String hexaValue=Convert.getHexFormat(itemValue);
			if(hexaValue.startsWith(AppConfig.mPackageId)) {
				itemValue=""+AppResources.mAppResourcesMap.get(hexaValue);	
				itemValue="?"+itemValue.substring(itemValue.lastIndexOf("/")+1);

			}else if(hexaValue.startsWith(AppConfig.mAndPackageId)) {
				itemValue=""+AndroidResources.mAndroidResourcesMap.get(hexaValue);	
				itemValue="?android:"+itemValue.substring(itemValue.lastIndexOf("/")+1);
			}else {
				itemValue="?null";
			}
		}else  if(parseEndsWith.contains("(string8)") ) {
			/** #6 (Key=0x7f010004): (string8) "fonts/Lato-Regular.ttf" */
			itemValue=parseEndsWith.replace("(string8)", "").trim();

		}else  if(parseEndsWith.contains("(color)") ) {


			/**  #0 (Key=0x01010097): (color) #00000000 */
			itemValue=parseEndsWith.substring(parseEndsWith.indexOf(") #")+3).trim();
			String hexaValue=Convert.getHexFormat(itemValue);	
			String mRawType=itemName+"("+keyValue+")";

			//System.out.println(" "+mRawType+" "+hexaValue);
			itemValue=ResourcesManager.getResValues(mRawType, hexaValue);

			if(itemValue==null) {
				// System.out.println(" "+mRawType+" "+hexaValue);
				itemValue=ResourcesManager.getResFlagValues(mRawType, hexaValue);
			}

			if(itemValue==null) {
				itemValue=parseEndsWith.substring(parseEndsWith.indexOf(") #")+3).trim();
				String valueFormat="";
				if(keyValue.startsWith(AppConfig.mPackageId)) {
					valueFormat="@"+AppResources.mAppAttrResourceTypes.get(mRawType);

				}else if(keyValue.startsWith(AppConfig.mAndPackageId)) {
					valueFormat="@"+AndroidResources.mAndroidAttrResourceTypes.get(mRawType);	
				}

				if (valueFormat.contains("integer")) {
					itemValue=Convert.getTypedValue(Constant.INT_DEC, itemValue);

				}else if (valueFormat.contains("boolean")) {
					itemValue=Convert.getTypedValue(Constant.INT_BOOLEAN, itemValue);

				}else if (valueFormat.contains("color")) {
					itemValue=Convert.getTypedValue(Constant.INT_COLOR_ARGB4, itemValue);	

					/*}else if (valueFormat.contains("float")) {
						itemValue=Convert.getTypedValue(Constant.FLOAT, itemValue);

					}else if (valueFormat.contains("dimension")) {
						itemValue=Convert.getTypedValue(Constant.DIMENSION, itemValue);

					}else if (valueFormat.contains("string")) {
						itemValue=Convert.getTypedValue(Constant.STRING, itemValue);

					}else 	if (valueFormat.contains("reference") ) {
						itemValue=Convert.getTypedValue(Constant.REFERENCE, itemValue);

					}else  if (valueFormat.contains("fraction")) {
						itemValue=Convert.getTypedValue(Constant.FRACTION, itemValue);*/
				}else {
					System.err.println(mRawType +" = "+itemValue+" "+valueFormat);
					itemValue="@null";
				}
			}

		}else {
			System.err.println("StyleResources: "+parseEndsWith);
		}
		return itemValue;
	}


	/**
	 * @param parseLine
	 * @param mResRawType
	 */
	public void parseStyleResources(String parseLine) {

		if(parseLine.trim().startsWith("Parent=0x") && !parseLine.trim().startsWith("(string8)")) {
			String parentValue=null;
			String parentId=parseLine.substring(parseLine.indexOf("(Resolved=0x")+10, parseLine.indexOf("), ")).trim();
			parentValue="@"+ResourcesManager.getResValues(parentId);
			if(parentValue.contains("@null")) {
				parentValue="";
			}
			mChildElement.setAttribute("name", mResName);
			mChildElement.setAttribute("parent",parentValue);
			mRootElement.appendChild(mChildElement);	
		}

		if(parseLine.trim().startsWith("#") && parseLine.contains(" (Key=0x") && !parseLine.trim().startsWith("(string8)") ) {
			/** #0 (Key=0x7f0100a6): (dimension) 24.000000dp */

			String itemName  ="";
			String itemValue ="";
			String keyValue=parseLine.substring(parseLine.indexOf(" (Key=0x")+6, parseLine.indexOf("): "));

			if(keyValue.startsWith(AppConfig.mPackageId)) {
				itemName=""+AppResources.mAppResourcesMap.get(keyValue);
				itemName=itemName.substring(itemName.indexOf("/")+1);
			}

			if(keyValue.startsWith(AppConfig.mAndPackageId)) {
				itemName=AndroidResources.mAndroidResourcesMap.get(keyValue);
				itemName="android:"+itemName.substring(itemName.indexOf("/")+1);
			}

			String parseEndsWith=parseLine.substring(parseLine.indexOf(": (")+2);
			itemValue=getStyleResourcesValue(parseEndsWith,keyValue,itemName);
			Element newElement = mDocument.createElement("item");
			newElement.setAttribute("name", itemName);
			newElement.setTextContent(itemValue);
			mChildElement.appendChild(newElement);
		}
	}

}
