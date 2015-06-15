package com.ielfgame.stupidGame.platform;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

public class TimeManager {
	
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
		        InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);  
		        TimeInfo timeInfo = timeClient.getTime(timeServerAddress);  
		        TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();  
		        
		        timeStamp.getTime();
		        
		        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		        System.out.println(dateFormat.format(timeStamp.getDate()));  
		        System.out.println(timeServerUrl);  
		    } catch (UnknownHostException e) {  
		    } catch (IOException e) {  
		    }  
		}
	}  
}
