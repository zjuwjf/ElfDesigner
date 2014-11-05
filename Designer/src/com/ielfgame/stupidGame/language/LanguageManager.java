package com.ielfgame.stupidGame.language;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;

import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.utils.FileHelper;

public class LanguageManager {
	
	private final static HashMap<String, String> sMap = new HashMap<String, String>(); 
	
	final static String sPath = System.getProperty("user.dir") + FileHelper.DECOLLATOR+"language.ini"; 
	
	static {
		sMap.clear();
		try { 
			final File file = new File(sPath);
			if(file.exists() && !file.isDirectory()) { 
				final BufferedReader br = new BufferedReader(new FileReader(file)); 
				String temp=null;
				while(true){ 
					temp = br.readLine(); 
					if(temp == null) {
						break;
					} else {
						final String [] sp = temp.split(",") ;
//						System.err.println("read "+temp);
						if(sp != null && sp.length>= 2) { 
							System.err.println("find "+sp[0]+","+sp[1]); 
							sMap.put(sp[0], sp[1]); 
						} 
					} 
				} 
			} 
		} catch (Exception e) { 
		}
		
		LanguageManager.put("File", "文件");
		LanguageManager.put("Set", "设置");
		LanguageManager.put("Set Config", "配置设置");
		
		LanguageManager.put("Tools", "工具");
		LanguageManager.put("Run", "运行");
		LanguageManager.put("Help", "帮助");
		
		LanguageManager.put("Node Tree", "节点树视图");
		LanguageManager.put("Action", "Action视图");
		LanguageManager.put("Node Property", "单选节点属性");
		LanguageManager.put("Global Property", "多选节点属性");
		
		LanguageManager.put("Flash Edit", "编辑 Flash");
		LanguageManager.put("Motion Edit", "编辑 Motion");
		
		LanguageManager.put("Language", "语言");
		LanguageManager.put("Res", "图片集");
		
		LanguageManager.put("Preview", "图片预览");
		LanguageManager.put("Flash Property", "Flash属性");
		LanguageManager.put("KeyStorage", "选择动画");
		
		
		LanguageManager.put("New Node", "新建节点");
		LanguageManager.put("Convert Node", "转换节点类型");
		LanguageManager.put("FlashMainNode", "Flash节点");
		
		LanguageManager.put("PositionInScreen", "屏幕中的位置");
		LanguageManager.put("AnchorPosition", "锚点位置");
		
		LanguageManager.put("Size", "大小");
		LanguageManager.put("UseSettedSize", "使用设置大小");
		LanguageManager.put("Paused", "暂停");
		LanguageManager.put("BlendMode", "混合模式");
		LanguageManager.put("BlendModeHelper", "查阅混合模式");
		LanguageManager.put("TouchEnable", "触摸有效");
		LanguageManager.put("TouchShielded", "触摸屏蔽");
		LanguageManager.put("CouldMove", "是否可编辑拖动");
		LanguageManager.put("FullName", "全名");
		
		LanguageManager.put("Rename", "重命名");
		LanguageManager.put("Delete", "删除");
		
		LanguageManager.put("Auto Name", "新命名自动加一");
		
		LanguageManager.put("Refresh Node", "刷新节点");
		LanguageManager.put("Refresh Node Deep", "深度刷新节点");
		
		LanguageManager.put("View infos", "查看信息");
		
		LanguageManager.put("Comment & Uncomment", "注释-反注释(节点)");
		LanguageManager.put("Print Node", "打印节点");
		
		LanguageManager.put("Map", "地图");
		LanguageManager.put("List", "列表");
		LanguageManager.put("Clip", "裁切");
		LanguageManager.put("Touch", "触摸");
		LanguageManager.put("Text", "文本");
		LanguageManager.put("Shape", "形状");
		LanguageManager.put("Layout", "布局");
		LanguageManager.put("ProgressNode", "进度条节点");
//		LanguageManager.put("BarNode", "布局");
		LanguageManager.put("Animate", "动画");
		
		LanguageManager.put("Copy Deep", "深度复制");
		LanguageManager.put("Selects", "选中");
		LanguageManager.put("Search", "查找");
		
		if(!ResManager.getSingleton().getXCodeRootAssetSupported()) {
			
			LanguageManager.put("ElfNode", "图片节点");
			LanguageManager.put("KeyFrameNode", "关键帧节点");
			
			LanguageManager.put("Resid", "图片");
			LanguageManager.put("Position", "位置");
			LanguageManager.put("Scale", "缩放");
			LanguageManager.put("Visible", "可见性");
			LanguageManager.put("Rotate", "旋转");
			LanguageManager.put("Color", "颜色");
			
			LanguageManager.put("FrameIndex", "第几帧");
			LanguageManager.put("InterType", "缓动类型");
			LanguageManager.put("InterStrong", "缓动强度");
			//InterStrong
			LanguageManager.put("LoopMode", "循环模式");
			
			LanguageManager.put("FPS", "动画帧率");
//			LanguageManager.put("MaxF", "最大帧数");
			LanguageManager.put("MaxFIndex", "最大帧数");
			LanguageManager.put("LoopMode", "循环模式");
			LanguageManager.put("ProgressTime", "运行时间(毫秒)");
			
			LanguageManager.put("LOOP", "循环");
			LanguageManager.put("STAY", "停止");
		}
		;
	}
	
	public static String get(final String key) {
		final String value = sMap.get(key);
		if(value == null) {  
			sMap.put(key, "");
			return key; 
		} else if(value.length()==0) {
			return key; 
		} else { 
			return value; 
		} 
	} 
	
	public static void put(final String key, final String value) {
		sMap.put(key, value);
	}
	
	public static void createLanguageIni() { 
		final File file = new File(sPath);
		if(!file.exists()) { 
			try { 
				file.createNewFile(); 
				final BufferedWriter output = new BufferedWriter(new FileWriter(file));
				final Set<String> set = sMap.keySet();
				for(final String key : set) { 
					output.write(key+"\r\n");  
				} 
				output.close(); 
			} catch (Exception e) { 
			} 
		} 
	} 
} 
