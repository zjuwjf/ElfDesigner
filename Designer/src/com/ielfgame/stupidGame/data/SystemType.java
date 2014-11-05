package com.ielfgame.stupidGame.data;


public enum SystemType {
	Mac, Windows;
	public final static SystemType CURRENT_SYSTEM;
	static {
		final String sys = System.getProperty("os.name");
		if(sys.contains("Windows")) {
			CURRENT_SYSTEM = Windows;
		} else {
			CURRENT_SYSTEM = Mac;
		}
		
		
	}
	
	public static void main(String [] args) {
		String s = "{\"Role\":{\"ID\":1683,\"Name\":\"Player1683\"},\"Account\":{\"ID\":38,\"UserName\":\"1348107948801\",\"Password\":\"1a980a522e0b67d8b00663c4c845af73\",\"UUID\":\"3c075410a37d\",\"IsDirect\":true,\"CategoryID\":0},\"Server\":{\"ID\":30,\"Name\":\"Android\",\"Url\":\"http://dev.herox.mosoga.net/Android/Server.ashx\",\"CreateAt\":\"2013-07-23 16:09:36\",\"StatusID\":0,\"PreName\":\"HeroX\",\"TimeZoneID\":\"China Standard Time\"},\"Token\":\"NzVFNTg0MEUxREVFOTcyMkUzOUQ2NUE3MTIyQTBDNUI0ODM0QzQ1QTk2NUQzQ0E3\",\"Timestamp\":1377768725224,\"Messages\":[],\"Code\":200,\"ResponseCode\":200}";
		System.err.println("size="+s.length() + "," +s.substring(99));
	}
}
