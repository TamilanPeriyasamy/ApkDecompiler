package com.apkdecompiler.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.apkdecompiler.conversion.Constant;
import com.apkdecompiler.conversion.Convert;
import com.apkdecompiler.main.ApkDecompiler;
import com.apkdecompiler.main.CommandExecutor;
import com.apkdecompiler.resources.AndroidResources;
import com.apkdecompiler.xmlbuilder.XMLBuilder;

public class AppResources {

	String mPackageName    = null;
	String mPackageId      = null;
	File mApkResourceDir   = null;
	File mMainValuesDir    = null;
	File mApkFile          = null;
	public static HashMap<String,String>  mZipEntries = new HashMap<String,String>();
	public static HashMap<Integer,String> mStringsValues = new HashMap<Integer,String>();
	public static HashMap<String,String>  mAppResourcesMap = new HashMap<String,String>();
	public static HashMap<String,String>  mAppResourcekeys = new HashMap<String,String>();

	public static HashMap<String,String>  mAppAttrResourceTypes  = new HashMap<String,String>();
	public static HashMap<String,String>  mAppAttrResourceArray  = new HashMap<String,String>();



	public AppResources(){
		mApkFile         = ApkDecompiler.mInputFile;
		mPackageName     = AppConfig.mPackageName;
		mApkResourceDir  = ApkDecompiler.mApkResourceDir;
		mMainValuesDir   = new File(mApkResourceDir+File.separator+"values");
		if(!mMainValuesDir.exists()) {
			mMainValuesDir.mkdirs();
		}
		/** attr ,drawable ,layout ,anim ,animator ,interpolator 
		 ,raw ,plurals ,string ,dimen ,style ,bool ,integer ,color ,array ,id ,menu */
	}


	public static  String getAppAttributeValue(String attributeRawType,String hexValue){ 
		int decimal=Convert.hex2Decimal(hexValue);
		attributeRawType=attributeRawType+"="+Convert.getHexFormat(decimal);
		//System.out.println("AttributeRawType "+attributeRawType);
		String mAttributeId=mAppResourcekeys.get(attributeRawType);
		//System.out.println("AttributeId "+mAttributeId+"\n");
		return getAttributeValue(mAttributeId); 
	}

	public static  String getAttributeValue(String mAttributeId){
		return mAppResourcesMap.get(mAttributeId); 
	}


	/**
	 * @param attributeRawType
	 * @param hexaDecimal
	 * @return
	 */
	public static String getFlagTypeValues(String attributeRawType, String hexaDecimal) {
		String finalCombinationValues=null;
		//System.out.println(" "+attributeRawType);
		//System.out.println(" "+hexaDecimal);
		String arrayValues=mAppAttrResourceArray.get(attributeRawType);
		//System.out.println(" "+arrayValues);
		if(arrayValues!=null) {
			int intFalgs[]=convertIntegerArray(arrayValues);
			int myType=Convert.hex2Decimal(hexaDecimal);
			finalCombinationValues=ResourcesManager.getCombinationValues(myType,intFalgs,attributeRawType);
		}
		return finalCombinationValues;
	}

	/**
	 * @param arrayValues
	 */
	private static int[] convertIntegerArray(String arrayValues) {
		// TODO Auto-generated method stub
		//System.out.println("arrayValues "+arrayValues);
		String values[]=arrayValues.split(",");
		int intFalgs[]=new int[values.length];
		for(int count=0;count<values.length;count++) {
			intFalgs[count]=Convert.hex2Decimal(values[count]);
		}
		return intFalgs;
	}


	public void parseApplicationResources() throws Exception {
		if(!mMainValuesDir.exists()) {
			mMainValuesDir.mkdirs();
		}
		
		System.out.println("Decompile values xmls...");
		ArrayList<String>  zipEntryParseStream=new CommandExecutor(mApkFile).getStringsValues("list");
		for(int parseCount=0;parseCount<zipEntryParseStream.size();parseCount++) {
			String currentParse=zipEntryParseStream.get(parseCount);
			String entryWithoutExtension=currentParse.trim();
			if(currentParse.contains(".")) {
				entryWithoutExtension=currentParse.substring(0,currentParse.indexOf(".")).trim();
			}
			//System.out.println(currentParse);
			mZipEntries.put(entryWithoutExtension,currentParse);
		}

		ArrayList<String>  stringsInputStream=new CommandExecutor(mApkFile).getStringsValues("dump strings");
		//String currentLine = null;
		for(int lineCount=0;lineCount<stringsInputStream.size();lineCount++) {
			String currentLine = stringsInputStream.get(lineCount);
			if(currentLine.contains("res/")) {
				// System.out.println(""+currentLine);
			}
			if(currentLine.trim().startsWith("String #")) {
				//String #3: ActivityDeveloper
				String stringIndex=currentLine.substring(currentLine.indexOf("String #")+8, currentLine.indexOf(": "));
				String stringValue=currentLine.substring(currentLine.indexOf(stringIndex+": ")+(stringIndex.length()+2));
				mStringsValues.put(Convert.string2Decimal(stringIndex), stringValue.trim());
			}	
		}
		//Create public.xml to values dir...
		ArrayList<String> mainResourcesInputStream    = new CommandExecutor(mApkFile).getResources("dump --values resources");
		ArrayList<ArrayList<String>> subResourcesInputStream = createMainPublicXmlFile(mMainValuesDir.getAbsolutePath(),mainResourcesInputStream);
		createOtherXmlFiles(subResourcesInputStream);

	}

	private ArrayList<ArrayList<String>> createMainPublicXmlFile(String resourcesValuesDir,ArrayList<String> mainResourcesInputStream) throws Exception {

		String resId             = null;
		String resRawType        = null;
		String mAttributeId      = null;
		String mAttributeName    = null;
		String mAttributeValue   = null;
		String mAttributeKey     = null;
		String mAttributeRawType = null;
		String mArrayType          = null;
		String mAttributeValues    = "";

		XMLBuilder mXMLBuilder = new XMLBuilder();
		Document document    = mXMLBuilder.newDocument();
		Element mRootElement = document.createElement("resources");

		ArrayList<ArrayList<String>> subResourcesInputStream = new ArrayList<ArrayList<String>>(mainResourcesInputStream.size());
		ArrayList<String> currentResourcesList=new ArrayList<String>();
		File publicXml= new File(resourcesValuesDir+File.separator+"public.xml");
		document.appendChild(mRootElement);
		for(int lineCount=0;lineCount<mainResourcesInputStream.size();lineCount++) {
			//System.out.println(""+inputStream.get(lineCount).trim());
			String currentLine=mainResourcesInputStream.get(lineCount);

			if(currentLine.trim().startsWith("Package ") && currentLine.contains("id=") && currentLine.contains(" packageCount=")) {
				/** Package Group 0 id=0x7f packageCount=1 name=com.quixxi.audiodemo */
				mPackageId=currentLine.substring(currentLine.indexOf("id=")+3, currentLine.indexOf(" packageCount=")).trim();
				AppConfig.mPackageId=mPackageId;
			}

			if(currentLine.trim().startsWith("spec resource 0x") && currentLine.contains(" "+mPackageName+":")) {
				/** spec resource 0x7f030000 com.dianping.example.activity:string/app_name: flags=0x00000000 */
				resId=currentLine.substring(currentLine.indexOf("spec resource 0x")+14, currentLine.indexOf(" "+mPackageName+":"));
				String subLine=currentLine.substring(currentLine.indexOf(" "+mPackageName+":")+(mPackageName.length()+2));
				resRawType=subLine.substring(0,subLine.indexOf(": flags="));
				mAppResourcesMap.put(resId, resRawType);
				Element childElement = document.createElement("public");
				String resType=resRawType.substring(0,resRawType.lastIndexOf("/"));
				String resName=resRawType.substring(resRawType.lastIndexOf("/")+1);
				childElement.setAttribute("id", resId);
				childElement.setAttribute("name", resName);
				childElement.setAttribute("type", resType);
				mRootElement.appendChild(childElement);
			}

			if(currentLine.trim().startsWith("config ") && currentResourcesList.size()!=0) {
				subResourcesInputStream.add(currentResourcesList);
				currentResourcesList=new ArrayList<String>();
			}

			if(!currentLine.trim().startsWith("spec resource 0x") && !currentLine.trim().startsWith("Package ") && !currentLine.trim().startsWith("type ")) {
				currentResourcesList.add(currentLine);
				if(currentLine.trim().startsWith("resource 0x")) {
					/** resource 0x7f0c0000 com.example:integer/abc_config_activityDefaultDur: t=0x10 d=0x000000dc (s=0x0008 r=0x00) 
					    resource 0x7f010000 com.example:attr/drawerArrowStyle: <bag>  */
					mAttributeId=currentLine.substring(currentLine.indexOf("resource 0x")+9, currentLine.indexOf(" "+mPackageName+":"));
					if(currentLine.contains(": t=0x")) {
						mAttributeName=currentLine.substring(currentLine.indexOf(" "+mPackageName+":")+(mPackageName.length()+2));
						mAttributeName=mAttributeName.substring(0,mAttributeName.indexOf(": t=0x"));
					}
					if(currentLine.contains(": <bag>")) {
						mAttributeName=currentLine.substring(currentLine.indexOf(" "+mPackageName+":")+(mPackageName.length()+2));
						mAttributeName=mAttributeName.substring(0,mAttributeName.indexOf(": <bag>"));
					}
					//System.out.println("..."+currentLine);

					if( mAttributeRawType!=null && mAttributeValues.length()!=0 ) {
						//System.out.println(""+mAttributeRawType);
						//System.out.println(""+mAttributeValues);
						mAttributeRawType=mAttributeRawType.substring(0, mAttributeRawType.indexOf(")=")+1);
						mAppAttrResourceArray.put(mAttributeRawType, mAttributeValues.substring(1));
						mAttributeValues="";
					}
				}

				if(currentLine.trim().startsWith("#")) {
					/** #0 (Key=0x01000000): (color) #00010040 */
					String nameEndswith=mAttributeName.substring(mAttributeName.indexOf("/")+1);
					mAttributeKey=currentLine.substring(currentLine.indexOf(" (Key=0x")+6,currentLine.indexOf("): ")).trim();
					mAttributeValue=currentLine.substring(currentLine.lastIndexOf(" ")).trim();
					mAttributeValue=mAttributeValue.replace("#","0x");
					mAttributeRawType="app:"+nameEndswith+"("+mAttributeId+")="+mAttributeValue;
					//System.out.println(mAttributeRawType+"...");
					//System.out.println(mAttributeKey+"\n");
					mAppResourcekeys.put(mAttributeRawType,mAttributeKey);

					if(currentLine.trim().startsWith("#0 (Key=0x")) {
						mArrayType=ResourcesManager.getArrayType(Convert.hex2Decimal(mAttributeValue));
					}	

					if(mArrayType.equals("flag")) { 
						mAttributeValues=mAttributeValues+","+mAttributeValue;
					}
				}
			}
		}
		subResourcesInputStream.add(currentResourcesList);
		mXMLBuilder.createXMLFile(document,publicXml,false);
		return subResourcesInputStream;
	}

	private File createValuesXmlFile(String resRawType, String configType) throws Exception {

		boolean createValuesFileFlag=true;
		File currentXmlFile   = null;
		String startWith="res/"+resRawType.substring(0,resRawType.indexOf("/"));
		String endsWith="/"+resRawType.substring(resRawType.indexOf("/")+1).trim();

		for (String resFilePath : AppResources.mZipEntries.keySet()) {
			if(resFilePath.startsWith(startWith) && resFilePath.endsWith(endsWith)) {
				createValuesFileFlag=false;
				break;
			}
		}
		//System.out.println(startWith+" "+endsWith+" "+configType);
		//System.out.println(" "+createValuesFileFlag);
		String resName=resRawType.substring(0,resRawType.lastIndexOf("/"));
		if(!resName.endsWith("s")) {
			resName=resName+"s";
		}
		if(createValuesFileFlag) {
			if(configType=="" || configType==null) {
				currentXmlFile=new File(mMainValuesDir.getAbsolutePath()+File.separator+resName+".xml");
				if(!currentXmlFile.exists()) {
					currentXmlFile.createNewFile();
				}
			}
			if(configType!="" && configType!=null) {
				File valuesConfigDir=new File(mApkResourceDir.getAbsolutePath()+File.separator+"values"+configType);
				if(!valuesConfigDir.exists()) {
					valuesConfigDir.mkdirs();
				}
				currentXmlFile=new File(valuesConfigDir+File.separator+resName+".xml");
				if(!currentXmlFile.exists()) {
					currentXmlFile.createNewFile();
				}
			}
		}
		return currentXmlFile;
	}

	private void createOtherXmlFiles(ArrayList<ArrayList<String>> subResourcesInputStream) throws Exception {

		for(int listCount=0;listCount<subResourcesInputStream.size();listCount++) {

			ArrayList<String> resourcesList=subResourcesInputStream.get(listCount);
			XMLBuilder mXMLBuilder = new XMLBuilder();
			Document mDocument = mXMLBuilder.newDocument();
			Element  mRootElement = mDocument.createElement("resources");
			mDocument.appendChild(mRootElement);	
			File currentXmlFile          = null;
			String configType            = null;
			String mResRawType           = null;
			String mResConstType         = null;

			for(int lineCount=0;lineCount<resourcesList.size();lineCount++) {
				String parseLine=resourcesList.get(lineCount);
				//System.out.println(""+currentLine.trim());
				
				if(parseLine.trim().startsWith("config ")) {
					/** config xxhdpi-v4: config (default): */
					configType="-"+parseLine.substring(parseLine.indexOf("config ")+7,parseLine.indexOf(":")).trim();
					configType=configType.contains("(default)")?"":configType;

				}else if(parseLine.trim().startsWith("resource 0x") && parseLine.contains(" "+mPackageName+":")) {
					/** resource 0x7f030000 com.dianping.example.activity:string/app_name: t=0x03 d=0x00000003 (s=0x0008 r=0x00) */
					String mResId=parseLine.substring(parseLine.indexOf("resource 0x")+9, parseLine.indexOf(" "+mPackageName+":"));
					mResRawType=mAppResourcesMap.get(mResId);
					
					if(parseLine.contains(": t=0x")) {
						mResConstType=parseLine.substring(parseLine.indexOf(": t=")+4, parseLine.indexOf(" d=0x"));
					}
					if(currentXmlFile==null) {
						currentXmlFile=createValuesXmlFile(mResRawType, configType);	
					}
					new Resources(mDocument,mRootElement,parseLine,mResId);

				}else if(mResRawType.startsWith("attr/")) {
					new AttrResources().parseAttrResources(parseLine);

				}else if(mResRawType.startsWith("style/")) {
					new StyleResources().parseStyleResources(parseLine);		

				}else if(mResRawType.startsWith("string/") ) {
					new StringResources().parseStringResources(parseLine,mResConstType);

				}else if(mResRawType.startsWith("id/")) {
					new IdResources().parseIdResources(parseLine,mResConstType);

				}else if(mResRawType.startsWith("drawable/")) {
					new DrawableResources().parseDrawableResources(parseLine,mResConstType);

				}else if(mResRawType.startsWith("dimen/") ) {
					new DimenResources().parseDimenResources(parseLine,mResConstType);

				}else if(mResRawType.startsWith("color/")) {
					new ColorResources().parseColorResources(parseLine,mResConstType);

				}else if( mResRawType.startsWith("integer/")){
					new IntegerResources().parseIntegerResources(parseLine,mResConstType);

				}else if(mResRawType.startsWith("bool/") ){
					new BooleanResources().parseBooleanResources(parseLine,mResConstType);

				}else if(mResRawType.startsWith("plurals/") ){
					new PluralsResources().parsePluralsResources(parseLine,mResConstType);

				}else if(mResRawType.startsWith("array/") ){
					new ArrayResources().parseArrayResources(parseLine);
				}else {
					//System.err.println(" "+parseLine);
				}
			}
			if(currentXmlFile!=null) {
				//System.out.println(""+currentXmlFile.getAbsolutePath());
				mXMLBuilder.createXMLFile(mDocument,currentXmlFile,false);
			}
		}

	}
}
