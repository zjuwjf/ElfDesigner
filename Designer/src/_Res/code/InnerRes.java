package _Res.code;

import java.io.InputStream;

public class InnerRes {

	public static InputStream getXMLInputStream(String name) {
		return InnerRes.class.getResourceAsStream("/_Res/xml/"+name);
	}

	public static InputStream getFontInputStream(String name) {
		return InnerRes.class.getResourceAsStream("/_Res/font/"+name);
	}
	
	public static InputStream getImageInputStream(String name) {
		return InnerRes.class.getResourceAsStream("/_Res/image/"+name);
	}
}
