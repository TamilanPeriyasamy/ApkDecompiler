/**
 * 
 */
package com.apkdecompiler.resources;
import com.apkdecompiler.conversion.Constant;
import com.apkdecompiler.conversion.Convert;

public class ResourcesManager {

	private final static int TYPE_REFERENCE  = 0x01;
	private final static int TYPE_STRING     = 0x02;
	private final static int TYPE_INT        = 0x04;
	private final static int TYPE_BOOL       = 0x08;
	private final static int TYPE_COLOR      = 0x10;
	private final static int TYPE_FLOAT      = 0x20;
	private final static int TYPE_DIMEN      = 0x40;
	private final static int TYPE_FRACTION   = 0x80;
	public final static int  TYPE_ANY_STRING  = 0xee;
	public static final int TYPE_ENUM       = 0x00010000;
	public static final int TYPE_FLAGS      = 0x00020000;


	public static  String getCombinationValues(int intVal,int mFlags[],String attributeRawType) {

		if (intVal == 0) {
			return null;
		}
		int[] flagItems = new int[mFlags.length];
		int[] flags = new int[mFlags.length];
		int flagsCount = 0;
		for (int i = 0; i < mFlags.length; i++) {
			int flagItem = mFlags[i];
			int flag = flagItem;
			if ((intVal & flag) != flag) {
				continue;
			}

			if (!isSubpartOf(flag, flags)) {
				flags[flagsCount] = flag;
				flagItems[flagsCount++] =flagItem;
			}
		}
		String formatType = "";
		for(int count=0;count<flagItems.length;count++) {

			if(flagItems[count]!=0) {	
				String mType=getResValues(attributeRawType,Convert.decimal2Hex(flagItems[count]));
				formatType=formatType+"|"+mType;
			}
		}
		//System.out.println(" "+formatType);
		return formatType.substring(1);
	}

	private static boolean isSubpartOf(int flag, int[] flags) {
		for (int i = 0; i < flags.length; i++) {
			if ((flags[i] & flag) == flag) {
				return true;
			}
		}
		return false;
	}


	public static String getFormatValues(String mRawType,String keyValue, String itemValue){

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

	    }else if (valueFormat.contains("float")) {
				itemValue=Convert.getTypedValue(Constant.FLOAT, itemValue);

	    }else if (valueFormat.contains("dimension")) {
				itemValue=Convert.getTypedValue(Constant.DIMENSION, itemValue);

		}else if (valueFormat.contains("string")) {
				itemValue=Convert.getTypedValue(Constant.STRING, itemValue);

		}else 	if (valueFormat.contains("reference") ) {
				itemValue=Convert.getTypedValue(Constant.REFERENCE, itemValue);

		}else  if (valueFormat.contains("fraction")) {
				itemValue=Convert.getTypedValue(Constant.FRACTION, itemValue);
		}else {
			System.err.println(mRawType +" = "+itemValue+" "+valueFormat);
			itemValue="@null";
		}
		return itemValue;
	}


	/**
	 * @param mItemRawType
	 * @param decimal2Hex
	 */
	public static String getResValues(String mItemRawType, String mHexaValue) {

		String mItemValue=null;
		String mItemId=mItemRawType.substring(mItemRawType.indexOf("(")+1, mItemRawType.lastIndexOf(")"));
		if(mItemId.trim().startsWith(AppConfig.mPackageId)) { 
			if(!mItemRawType.startsWith("app:")) {
				mItemRawType="app:"+mItemRawType;
			}
			mItemValue=AppResources.getAppAttributeValue(mItemRawType,mHexaValue);
		}

		if(mItemId.trim().startsWith(AppConfig.mAndPackageId)) {
			if(!mItemRawType.startsWith("android:")) {
				mItemRawType="android:"+mItemRawType;
			}
			mItemValue=AndroidResources.getAndroidAttributeValue(mItemRawType,mHexaValue);	
		}
		///System.out.println(" "+mResValue+"\n");
		if(mItemValue==null) {
			return null;
		}
		return mItemValue.substring(mItemValue.lastIndexOf("/")+1);
	}

	/**
	 * @param mItemRawType
	 * @param mHexaValue
	 */
	public static String getResFlagValues(String mItemRawType, String mHexaValue) {
		String mItemValue=null;
		String mItemId=mItemRawType.substring(mItemRawType.indexOf("(")+1, mItemRawType.lastIndexOf(")"));
		if(mItemId.trim().startsWith(AppConfig.mPackageId)) {
			mItemValue=AppResources.getFlagTypeValues(mItemRawType,mHexaValue);
		}

		if(mItemId.trim().startsWith(AppConfig.mAndPackageId)) {
			mItemValue=AndroidResources.getFlagTypeValues(mItemRawType,mHexaValue);
		}

		if(mItemValue==null) {
			return null;
		}
		//System.out.println(""+mResValue+"\n");
		return mItemValue.substring(mItemValue.lastIndexOf("/")+1);
	}


	/** 
	 * @param mHexaValue
	 */
	public static String getResValues(String mHexaValue) {

		String mItemValue=null;
		if(mHexaValue.startsWith(AppConfig.mPackageId)) {
			mItemValue=AppResources.mAppResourcesMap.get(mHexaValue);
		}

		if(mHexaValue.startsWith(AppConfig.mAndPackageId)) {
			mItemValue=AndroidResources.mAndroidResourcesMap.get(mHexaValue);
		}

		if(mItemValue==null) {
			return null;
		}
		return mItemValue;
	}

	public static String getFormatType(int mItemType) {
		String formatType = "";
		if ((mItemType & TYPE_REFERENCE) != 0) {
			formatType += "|reference";
		}
		if ((mItemType & TYPE_STRING) != 0) {
			formatType += "|string";
		}
		if ((mItemType & TYPE_INT) != 0) {
			formatType += "|integer";
		}
		if ((mItemType & TYPE_BOOL) != 0) {
			formatType += "|boolean";
		}
		if ((mItemType & TYPE_COLOR) != 0) {
			formatType += "|color";
		}
		if ((mItemType & TYPE_FLOAT) != 0) {
			formatType += "|float";
		}
		if ((mItemType & TYPE_DIMEN) != 0) {
			formatType += "|dimension";
		}
		if ((mItemType & TYPE_FRACTION) != 0) {
			formatType += "|fraction";
		}

		if (formatType.isEmpty()) {
			return null;
		}
		return formatType.substring(1);
	}


	public static String getArrayType(int mType) {

		String arrayType="";
		if(( mType & TYPE_ENUM) !=0) {
			/** 0x00010000 */ 
			arrayType="enum";

		}else if(( mType & TYPE_FLAGS) !=0) {
			/** 0x00020000 */
			arrayType="flag";

		}else {
			//System.err.println("arrayType "+arrayType+" "+Convert.hex2Decimal(arrayType));	
		}
		return arrayType;
	}

	/**
	 * @param currentLine
	 * @param mResConstType
	 * @return
	 */
	public static String getResourcesValue(String parseLine, String mItemConstType) {

		//System.out.println(" "+parseLine);
		String mItemValue="";
		if(parseLine.trim().startsWith("(dimension)")) {
			/** (dimension) 8.000000dp */
			mItemValue=parseLine.substring(parseLine.indexOf(") ")+2).trim();

		}else if(parseLine.trim().startsWith("(float)")){
			/**  (float) 0.3 */
			mItemValue=parseLine.substring(parseLine.indexOf(") ")+2).trim();

		}else if(parseLine.trim().startsWith("(fraction)")){	
			/**  (fraction) 0.550000% */
			mItemValue=parseLine.substring(parseLine.indexOf(") ")+2).trim();

		}else if( parseLine.trim().startsWith("(reference)") ){
			/**  (reference) 0x7f0a0025 */
			mItemValue=parseLine.substring(parseLine.indexOf(") ")+2).trim();
			String hexaValue=Convert.getHexFormat(mItemValue);
			mItemValue="@"+ResourcesManager.getResValues(hexaValue);

		}else if( parseLine.trim().startsWith("(attribute)") ){
			/** (attribute) 0x01010039 */
			mItemValue=parseLine.substring(parseLine.indexOf(") ")+2).trim();
			String hexaValue=Convert.getHexFormat(mItemValue);
			if(hexaValue.startsWith(AppConfig.mPackageId)) {
				mItemValue=""+AppResources.mAppResourcesMap.get(hexaValue);
				mItemValue="?"+mItemValue.substring(mItemValue.lastIndexOf("/")+1);

			}else if(hexaValue.startsWith(AppConfig.mAndPackageId)) {
				mItemValue=""+AndroidResources.mAndroidResourcesMap.get(hexaValue);	
				mItemValue="?android:"+mItemValue.substring(mItemValue.lastIndexOf("/")+1);
			}

		}else if(parseLine.trim().startsWith("(color)")){	
			/**  (color) #80ffffff */
			mItemValue=parseLine.substring(parseLine.indexOf(") ")+2).trim();
			if(mItemConstType!=null && (mItemConstType.contains(Constant.INT_DEC) || mItemConstType.contains(Constant.INT_BOOLEAN) )) {
				mItemValue=mItemValue.replace("#","");
				mItemValue=Convert.getTypedValue(mItemConstType,mItemValue);
			}		
		}
		if(mItemValue.equals("")) {
			System.err.println("getResourcesValue() "+parseLine);
		}
		return mItemValue;
	}
}
