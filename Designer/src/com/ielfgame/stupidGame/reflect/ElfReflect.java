package com.ielfgame.stupidGame.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class ElfReflect {
	
	public static Object get(final ReflectUion rf, Object select) { 
		try {
			return rf.getFunc.invoke(select);
		} catch (Exception e) {
		} 
		return null;
	} 
	
	public static void set(final ReflectUion rf, final Object value, final Object select) { 
		try {
			if(rf.type == null) {
				rf.setFunc.invoke(select);
			} else { 
				rf.setFunc.invoke(select, value);
			} 
		} catch (Exception e) { 
		} 
	} 
	
	//Func, Type, Get & Set Mask
	
	public static class ReflectUion {
		public final String funcName;
		public final Class<?> type;
		public final int mask;
		public final Class<?> select;
		public final Method getFunc;
		public final Method setFunc;
		
		public ReflectUion(String func, Class<?> type, int mask, Class<?> select, 
				Method get, Method set) {
			super();
			this.funcName = func;
			this.type = type;
			this.mask = mask;
			this.select = select;
			this.getFunc = get;
			this.setFunc = set;
		} 
	} 
	
	public static ReflectUion [] getReflects(final Class<?> _class) {
		if(_class != null) {
			final LinkedList<ReflectUion> list = new LinkedList<ElfReflect.ReflectUion>();
			
			final LinkedList<Field> fieldList = new LinkedList<Field>();
			Class<?> tmp = _class;
			while(tmp != Object.class) {
				final Field [] tmpfs = tmp.getDeclaredFields();
				for(Field f :  tmpfs) {
					fieldList.add(f);
				}
				tmp = tmp.getSuperclass();
			} 
			
			final Field [] fs = new Field[fieldList.size()];
			fieldList.toArray(fs);
			
			
			for(Field f : fs) {
				f.setAccessible(true);
				try {
					final String name = f.getName();
					if(name.startsWith("REF_")) {
						final String func = name.substring(4);
						final int mask = f.getInt(null);
						
//						if(mask == 1) {
//							System.out.println("mask : " + mask + "," + func);
//						}
						
						Class<?> type = null;
						Method get = null, set = null;
						final Method [] methods = _class.getMethods(); 
						
						for(int j=0; j<methods.length; j++) { 
							final Method method = methods[j];
							final String methodName = method.getName();
							if(methodName.equals("get"+func)) { 
								type = method.getReturnType(); 
								get = method;
								break;
							} 
						} 
						
						if(type == null) {
							set = _class.getMethod("set" + func);
						} else {
							set = _class.getMethod("set" + func, type);
						}
						
						if(type != null) {
							//no get
							list.add(new ReflectUion(func, type, mask, _class, get, set));
						} else {
							//Not Expected
							//no get, means set(void)
							list.add(new ReflectUion(func, type, mask, _class, get, set));
						} 
					} 
				} catch (Exception e) { 
//					e.printStackTrace();
				}
			}
			
			final ReflectUion [] ret = new ReflectUion[list.size()];
			list.toArray(ret);
			
			return ret;
		}
		
		return new ReflectUion[0];
	} 
} 
