package com.ielfgame.stupidGame.config;

import com.ielfgame.stupidGame.data.ElfDataXML;
import com.ielfgame.stupidGame.data.ElfPointf;

public class ProjectSetting extends ElfDataXML {
	/***
	 * 
	 */
	public String configName = "NewConfig.xml";
	
	public boolean Full_Name_Reduced = false;
	public String Designer_Resource_REF_DIR;
	public String language_plist = "Localizable_cn.plist";
	public String XCode_Resource_REF_DIR;
	public String script = "script";
	//Mode0
	public String langEditMode = "Mode1";
	public ScreenResolutionType Screen_Resolution = ScreenResolutionType.R_1136_640;
	public com.ielfgame.stupidGame.enumTypes.Orientation Orientation = com.ielfgame.stupidGame.enumTypes.Orientation.Vertical;
	public ElfPointf ScreenScale = new ElfPointf(1, 1);
	
	public Platform plantform = Platform.IOS;
	
	/***
	 * Art_Project_Directory_REF_DIR
	 * config.xml -> last_xml
	 * auto-save <= 10M -> xxx_data.zip
	 * xml 
	 * image 
	 * font -> 
	 * 
	 * Code_Directory_REF_DIR => XCode Resource
	 * script 
	 * layout 
	 * font 
	 * pngs -> to be packed
	 * plist -> 
	 * 
	 * auto-save 
	 * --script
	 * --layout
	 */
	
	public static enum ScreenResolutionType { 
		R_1136_640(1136, 640),
		R_960_640(960, 640),
		R_800_480(800, 480),;
		
		private final int width;
		private final int height;
		
		ScreenResolutionType(int w, int h) {
			width = w;
			height = h;
		}
		
		public int getWidth(boolean isHor) {
			return isHor? width : height;
		}
		
		public int getHeight(boolean isHor) {
			return isHor? height : width;
		} 
	} 
	
	public static enum Platform {
		IOS("image","raw"), Android("image-android","raw-android");
		
		public final String image;
		public final String raw;
		Platform(String image, String raw) {
			this.image = image;
			this.raw = raw;
		}
		
		
	}
	
	public int getLogicWidth() { 
		return Screen_Resolution.getWidth(Orientation.isHorizontal());
	} 
	
	public int getLogicHeight() { 
		return Screen_Resolution.getHeight(Orientation.isHorizontal());
	} 
	
	public int getPhysicWidth() { 
		return Math.round( getLogicWidth() * ScreenScale.x );
	} 
	
	public int getPhysicHeight() { 
		return Math.round( getLogicHeight() * ScreenScale.y );
	} 
}
