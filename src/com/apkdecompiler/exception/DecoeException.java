package com.apkdecompiler.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("serial")
public class DecoeException extends Exception{

	public static String exceptionMessage;

	@Override
	public String getMessage() {
		return DecoeException.exceptionMessage;
	}

	public String getStackTraceAsString(){
		StringWriter errors = new StringWriter();
		this.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	public static String getStackTraceAsString(DecoeException e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	} 

	public static enum ExceptionCode{
		// Compilation
		
		ANDROID_APP_BUILD_FAILED,
		INVALID_UPLOADED_FILE,
		INVALID_SDK_PATH, 
		INVALID_APKTOOL_PATH,
		UNSUPPORTED_PLATOFRM,

		//Security Framework
		INVALID_PACKAGE_NAME,
		APKTOOL_UNPACK_FAILD,
		APKTOOL_REPACK_FAILD,
		AAR_EXTRACT_FAILED,
		AAR_COMPRESS_FAILED,
		DEX_TO_JAR_CONVERSION_FAILED,
		JAR_TO_DEX_CONVERSION_FAILED,
		JAR_EXTRACTION_FAILED,
		JAR_MERGED_FAILED,
		APK_SIGNING_FAILED,
		APK_ZIPALIGN_FAILED,
        APK_EXTRACT_FAILED,
		APK_COMPRESS_FAILED,
        AAPT_FILE_REMOVE_FAILED,
		AAPT_FILE_ADDED_FAILED,
		ALREADY_OBFUSCATED,
		UNKNOWN_EXCEPTION,
	};   
  
	public DecoeException(ExceptionCode exceptionCode ){

		switch(exceptionCode){
		
		case ANDROID_APP_BUILD_FAILED:
			DecoeException.exceptionMessage = "'ANDROID APP BUILD FAILED'";
			break;
		
		case INVALID_UPLOADED_FILE:
			DecoeException.exceptionMessage = "'INVALID UPLOADED FILE'\n";	
			break;		
		case UNSUPPORTED_PLATOFRM:
			DecoeException.exceptionMessage = "'UNSUPPORTED PLATOFRM'\n";
			break;
		case INVALID_PACKAGE_NAME:
			DecoeException.exceptionMessage = "'INVALID PACKAGE NAME'\n";
			break;	
		
		case INVALID_SDK_PATH:
			DecoeException.exceptionMessage = "'INVALID SDK PATH'\n";
			break;
		case INVALID_APKTOOL_PATH:
			DecoeException.exceptionMessage = "'INVALID APKTOOL PATH'\n";
			break;	
		case APKTOOL_UNPACK_FAILD:
			DecoeException.exceptionMessage = "'APKTOOL UNPACK FAILED'\n";	
			break;
		case APKTOOL_REPACK_FAILD:
			DecoeException.exceptionMessage = "'APKTOOL REPACK FAILED'\n";	
			break;
		case APK_EXTRACT_FAILED:
			DecoeException.exceptionMessage = "'APK EXTRACT FAILED'\n";	
			break;
		case APK_COMPRESS_FAILED:
			DecoeException.exceptionMessage = "'APK COMPRESS FAILED'\n";	
			break;
		case DEX_TO_JAR_CONVERSION_FAILED:
			DecoeException.exceptionMessage = "'DEX TO JAR CONVERSION FAILED'\n";	
			break;
		case JAR_TO_DEX_CONVERSION_FAILED:
			DecoeException.exceptionMessage = "'JAR TO DEX CONVERSION FAILED'\n";	
			break;
		case JAR_EXTRACTION_FAILED:
			DecoeException.exceptionMessage = "'JAR EXTRACTION FAILED'\n";	
			break;
		case JAR_MERGED_FAILED:
			DecoeException.exceptionMessage = "'JAR MERGED FAILED'\n";	
			break;
		case ALREADY_OBFUSCATED:
			DecoeException.exceptionMessage = "'apk is already obfuscated'\n";	
			break;
		case AAPT_FILE_REMOVE_FAILED:
			DecoeException.exceptionMessage = "'AAPT FILE REMOVE FAILED'\n";	
			break;
		case AAPT_FILE_ADDED_FAILED:
			DecoeException.exceptionMessage = "'AAPT FILE ADDED FAILED'\n";	
			break;
		default: 
			DecoeException.exceptionMessage = "Unknown exception has occured\n";
			break;
		}
	}  
} 
