package com.ielfgame.stupidGame.reflect;

import com.ielfgame.stupidGame.utils.FileHelper;

public class ReflectFactory {
	
	static final String USER_DIR = System.getProperty("user.dir"); 
	static final String BIN = FileHelper.DECOLLATOR+"bin"+FileHelper.DECOLLATOR; 
	
	static final boolean OUT_USE = true;
	
	public interface IDealClass {
		public void dealClass(Class<?> _class);
	}
}
