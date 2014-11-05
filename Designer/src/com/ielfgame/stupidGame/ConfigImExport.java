package com.ielfgame.stupidGame;

import java.io.File;

import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLVersionManage;

public class ConfigImExport {
	private static final String USER_DIR = System.getProperty("user.dir");
	private static final String CONFIG_PATH = USER_DIR+FileHelper.DECOLLATOR+"config.xml";
	
	static {
		System.err.println("load ConfigImExport");
	}
	
	public static final void importConfigs() {
		final File f = new File(CONFIG_PATH);
		if(f.exists()) {
			XMLVersionManage.readFromXML(CONFIG_PATH);
			f.delete();
		} 
	}
	
	public static final void exportConfigs() { 
		
//		final ArrayList<Object> list = new ArrayList<Object>();
//		
//		list.add(PowerMan.getSingleton(DataModel.class));
//		
//		XMLVersionManage.writeToXML(list, USER_DIR + CONFIG_PATH); 
//		
//		final Set<String> set = NodeMap.getKeySet();
//		for(final String key : set) {
//			Class<?> _class = NodeMap.getNodeClass(key);
//			if(_class.getSimpleName().endsWith("Node") ) { 
//				final int modifier = _class.getModifiers();
//				if( !_class.isMemberClass() && (modifier & 1024) == 0 ) {
//					try { 
//						@SuppressWarnings("unchecked")
//						final Constructor<? extends ElfNode> constructor = (Constructor<? extends ElfNode>) _class.getConstructor(ElfNode.class, int.class);
//						NodeConfig.saveConfigs(_class, constructor.newInstance(null, 0));
//					} catch (Exception e) { 
//						System.err.println(_class.getCanonicalName());
//						e.printStackTrace(); 
//					} 
//				} 
//			} 
//		} 
	} 
} 

