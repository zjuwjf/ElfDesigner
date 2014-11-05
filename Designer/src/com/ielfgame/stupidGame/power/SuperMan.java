package com.ielfgame.stupidGame.power;

import java.util.HashMap;


public final class SuperMan{
	static {
		System.err.println("load SuperMan ");
	} 
	
	private final static HashMap<Class<? extends ISingleton>, ISingleton> sMap = new HashMap<Class<? extends ISingleton>, ISingleton>();
	public static void register(Class<? extends ISingleton> _class, ISingleton workSpace){
		System.err.println(_class.getCanonicalName() + " registers!"); 
		sMap.put(_class, workSpace); 
	} 
	
	@SuppressWarnings("unchecked")
	public static <T extends ISingleton> T getSingleton(Class<T> _class){
		final T ret = (T)sMap.get(_class);
		return ret;
	} 
} 


