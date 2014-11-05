package com.ielfgame.stupidGame.data;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class ElfDataDisplay implements IDataDisplay{

	public String toTitle() {
		return this.getClass().getSimpleName();
	} 

	public String[] toLabels() { 
		final LinkedList<String> fieldList = new LinkedList<String>();
		try {
			final Field[] fs = this.getClass().getFields();
			for (int i = 0; i < fs.length; i++) { 
				fs[i].setAccessible(true); 
				fieldList.add(fs[i].getName()); 
			} 
			final String[] ret = new String[fieldList.size()];
			fieldList.toArray(ret);
			return ret;
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return null; 
	} 
	
	public String[] toValues() {
		final LinkedList<String> fieldList = new LinkedList<String>();
		try {
			final Field[] fs = this.getClass().getFields();
			for (int i = 0; i < fs.length; i++) {
				fs[i].setAccessible(true);
				fieldList.add(Stringified.toText(fs[i].get(this), false)); 
			}
			final String[] ret = new String[fieldList.size()];
			fieldList.toArray(ret);
			return ret; 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return null;
	} 
	
	public Class<?> [] toTypes() {
		final LinkedList<Class<?>> fieldList = new LinkedList<Class<?>>();
		try {
			final Field[] fs = this.getClass().getFields();
			for (int i = 0; i < fs.length; i++) {
				fs[i].setAccessible(true);
				fieldList.add( fs[i].getType() ); 
			}
			final Class<?>[] ret = new Class<?>[fieldList.size()];
			fieldList.toArray(ret);
			return ret; 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public int setValues(Object [] values) {
		int i = 0;
		try {
			final Field[] fs = this.getClass().getFields();
			for (; i < fs.length; i++) {
				fs[i].setAccessible(true); 
				if (values[i] != null) { 
					fs[i].set(this, values[i]); 
				} else { 
					return i; 
				} 
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
			return i; 
		} 

		return -1;//finished
	}
	
	public int setValuesByStrings(String [] strings) {
		if(strings == null) {
			return 0;
		}
		final Object [] values = new Object[strings.length];
		Class<?> [] types = this.toTypes();
		for (int i = 0; i < values.length; i++) {
			try {
				values[i] = Stringified.fromText(types[i], strings[i])[0];
			} catch (Exception exception) {
			} 
		} 
		return setValues(values);
	}
	
	public boolean setValueByLabelAndString(final String label, final String value) {
		try {
			final Field[] fs = this.getClass().getFields();
			for (int i = 0; i < fs.length; i++) {
				final String name = fs[i].getName();
				if(name.equals(label)) {
					try {
						Object obj = Stringified.fromText(fs[i].getType(), value)[0];
						fs[i].setAccessible(true); 
						fs[i].set(this, obj); 
						return true;
					} catch (Exception exception) {
					} 
				}
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		
		return false;
	}
	

	@Override
	public int fromValues(String[] values) {
		return setValuesByStrings(values);
	}
} 
