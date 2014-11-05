package com.ielfgame.stupidGame.reflect;

import java.lang.reflect.Field;

import elfEngine.basic.node.ElfNode;

public class ReflectTest {
	
	public static void main(String [] args) {
		Class<?> nodeClass = ElfNode.class;
		final Field [] fields = nodeClass.getDeclaredFields();
		for(int i=0; i<fields.length; i++) { 
			final Field field = fields[i]; 
			final String name = field.getName();
			if(name.startsWith("REF_")) { 
				field.setAccessible(true);
				try {
					final int value = field.getInt(null);
					System.err.println(name+" : " + value); 
				} catch (Exception e) {
					e.printStackTrace();
				} 
			} 
		} 
	} 
} 

