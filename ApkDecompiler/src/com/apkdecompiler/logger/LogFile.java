package com.apkdecompiler.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.apkdecompiler.conversion.Convert;

public class LogFile extends OutputStream {

	OutputStream[] outputStreams;
	public static String mDateAndTime=null;
	public static int days_Count=0;
	public static File logFile=null;
	static FileOutputStream fout_ferr=null;

	public LogFile(OutputStream... outputStreams){
		this.outputStreams= outputStreams; 
	}

	@Override
	public void write(int b) throws IOException
	{
		for (OutputStream out: outputStreams)
			out.write(b);			
	} 

	@Override
	public void write(byte[] b) throws IOException
	{
		for (OutputStream out: outputStreams)
			out.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		for (OutputStream out: outputStreams)
			out.write(b, off, len);
	}

	@Override
	public void flush() throws IOException
	{
		for (OutputStream out: outputStreams)
			out.flush();
	}

	@Override
	public void close() throws IOException
	{
		for (OutputStream out: outputStreams)
			out.close();
	}

	public static String getDateAndTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		return dateFormat.format(new Date());  
	}

	public static void toStartWrite(){
		try{  
			mDateAndTime=getDateAndTime();
			mDateAndTime=mDateAndTime.replace(" ","-");
			System.out.println(""+mDateAndTime);
			
			boolean fileAppendMode=false;
			File logFileDir=new File(System.getProperty("user.dir")+File.separator+"logs"+File.separator);
			if(!logFileDir.exists()){
				logFileDir.mkdirs();
			}
			
			String logFileName=mDateAndTime.substring(0, mDateAndTime.indexOf("_"))+".log";
			logFile=new File(logFileDir,logFileName);
			if(!logFile.exists()){ 
				logFile.createNewFile();	
			} 
			fout_ferr= new FileOutputStream(logFile.getAbsoluteFile(),fileAppendMode);
			PrintStream  stdout= new PrintStream(new LogFile(System.out, fout_ferr)); 
			PrintStream  stderr= new PrintStream(new LogFile(System.err, fout_ferr));

			System.setOut(stdout); 
			System.setErr(stderr);  

		}catch (IOException ex){ 
			ex.printStackTrace();  
		}  
	}

	public static void toStopWrite() throws IOException {
		fout_ferr.close();
		System.out.println(""+mDateAndTime);
	}
}