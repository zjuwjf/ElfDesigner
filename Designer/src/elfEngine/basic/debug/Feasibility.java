package elfEngine.basic.debug;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

public class Feasibility {
	/**
	 * 1419227991816 2014.12.22-14.00
	 */
	private final static long BaseMiles = 1419227991816L;

	private final static long DayStep = 1000l * 3600l * 24l;
	private final static long MonthStep = DayStep * 30l;
	
	public static boolean isLegal() {
//		final long time = getCurrentTime();
//		if (time >= (BaseMiles + 9l * MonthStep + 10l * DayStep) ) {
//			return new Random().nextFloat() < 0.5f;
//		} else { 
//			return true; 
//		} 
		return true;
	}
	
	private static long sNtpTime = -1;
	private static long getCurrentTime() {
		if(sNtpTime < 0) {
			return System.currentTimeMillis();
		} else {
			return sNtpTime;
		}
	}
	
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
	
	static {
		try {
			new Thread(new Runnable() {
				public void run() {
					for (int i=0; i<sServerList.length; i++) {
						final String timeServerUrl = sServerList[i];
						try {  
					        NTPUDPClient timeClient = new NTPUDPClient();  
					        timeClient.setDefaultTimeout(3000);
					        InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);  
					        TimeInfo timeInfo = timeClient.getTime(timeServerAddress);  
					        TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();  
					        
					        sNtpTime = timeStamp.getTime();
					        break;
					        
					    } catch (UnknownHostException e) {  
					    } catch (IOException e) {  
					    }  
					}
					
				}
			}).start();
		} catch (Exception e) {
		}
	}
	
	public static void main(String[] args) { 
		for (int i=0; i<sServerList.length; i++) {
			final String timeServerUrl = sServerList[i];
			try {  
		        NTPUDPClient timeClient = new NTPUDPClient();  
		        timeClient.setDefaultTimeout(3000);
		        InetAddress timeServerAddress = InetAddress.getByName(timeServerUrl);  
		        TimeInfo timeInfo = timeClient.getTime(timeServerAddress);  
		        TimeStamp timeStamp = timeInfo.getMessage().getTransmitTimeStamp();  
		        
		        sNtpTime = timeStamp.getTime();
		        break;
		        
//		        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
//		        System.out.println(dateFormat.format(timeStamp.getDate()));  
//		        System.out.println(sNtpTime);  
//		        final Date date = new Date(sNtpTime);
//		        System.out.println(date.toString());  
//		        System.out.println(timeServerUrl);  
		    } catch (UnknownHostException e) {  
		    } catch (IOException e) {  
		    }  
		}
	}  
}
