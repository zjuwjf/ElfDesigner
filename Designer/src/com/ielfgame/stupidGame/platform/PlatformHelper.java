package com.ielfgame.stupidGame.platform;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.eclipse.swt.SWT;

import com.ielfgame.stupidGame.utils.FileHelper;

public class PlatformHelper {

	public static final String DECOLLATOR;
	public static final boolean IS_WINDOWS;
	public static final int CTRL;
	public static final int SHIFT;
	public static final int SPACE;
	public static final int DEL;

	static {
		final String sys = System.getProperty("os.name");
		System.err.println("System " + sys);
		IS_WINDOWS = sys.contains("Windows");
		if (IS_WINDOWS) {
			DECOLLATOR = "\\";
			CTRL = SWT.CTRL;
			DEL = SWT.DEL;
		} else {
			DECOLLATOR = "/";
			CTRL = SWT.COMMAND;
			DEL = SWT.BS;
		}
		SHIFT = SWT.SHIFT;
		SPACE = SWT.SPACE;
	}
	
	private static boolean sChangeUnderline = false;
	public static void setChangeUnderline(boolean set) {
		sChangeUnderline = set;
	}
	public static boolean getChangeUnderline() {
		return sChangeUnderline;
	}

	public static String toCurrentPath(String key) {
		final String simple = FileHelper.getSimpleName(key);
		if(sChangeUnderline) {
			if(simple != null) {
				return simple.replace("-", "_");
			}
		}
		return simple;
	}

	public static String getUserDir() {
		return System.getProperty("user.dir");
	}
	
	/*
	 * 
	 * server 133.100.11.8 prefer
     server 210.72.145.44
     server 203.117.180.36 //程序中所用的
     server 131.107.1.10
     server time.asia.apple.com
     server 64.236.96.53
     server 130.149.17.21
     server 66.92.68.246
     server www.freebsd.org
     server 18.145.0.30
     server clock.via.net
     server 137.92.140.80
     server 133.100.9.2
     server 128.118.46.3
     server ntp.nasa.gov
     server 129.7.1.66
     server ntp-sop.inria.frserver 210.72.145.44(国家授时中心服务器IP地址)
	 * 
	 */
	
	private static String [] sServerList = {
		"133.100.11.8",
		"210.72.145.44",
		"203.117.180.36",
		"131.107.1.10",
		"time.asia.apple.com",
		"64.236.96.53",
		"130.149.17.21",
		"66.92.68.246",
		"www.freebsd.org",
		"18.145.0.30",
		"clock.via.net",
		"137.92.140.80",
		"133.100.9.2",
		"128.118.46.3",
		"ntp.nasa.gov",
		"129.7.1.66",
		"210.72.145.44",
		"time-a.nist.gov",
		"131.107.1.10",
		"time.asia.apple.com",
	};
	
	public static void main(String[] args) { 
		for (int i=0; i<sServerList.length; i++) {
			final String timeServerUrl = sServerList[i];
			try {  
		        NTPUDPClient timeClient = new NTPUDPClient();  
		        timeClient.setDefaultTimeout(3000);
//		        String timeServerUrl = "time-a.nist.gov";  
		        InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);  
		        TimeInfo timeInfo = timeClient.getTime(timeServerAddress);  
		        TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();  
		        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		        System.out.println(dateFormat.format(timeStamp.getDate()));  
		        System.out.println(timeServerUrl);  
//		        break;
		    } catch (UnknownHostException e) {  
		    } catch (IOException e) {  
		    }  
		}
		
	    try {  
	        NTPUDPClient timeClient = new NTPUDPClient();  
	        timeClient.setDefaultTimeout(10000);
//	        String timeServerUrl = "time-a.nist.gov";  
	        String timeServerUrl = sServerList[0];  
	        InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);  
	        TimeInfo timeInfo = timeClient.getTime(timeServerAddress);  
	        TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();  
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	        System.out.println(dateFormat.format(timeStamp.getDate()));  
	    } catch (UnknownHostException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}  

}
