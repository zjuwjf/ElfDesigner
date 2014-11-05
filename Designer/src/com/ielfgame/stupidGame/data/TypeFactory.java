package com.ielfgame.stupidGame.data;

public class TypeFactory {
	
	public enum ClassType {
		INT_TYPE, LONG_TYPE, FLOAT_TYPE, DOUBLE_TYPE, CHAR_TYPE, BOOL_TYPE, ENUM_TYPE, STRING_TYPE, ELSE_TYPE, ARRAY_TYPE,
		ELFENUM_TYPE,
	} 
	
	public static final ClassType getType(Class<?> _class) {
		if(_class.isArray()) {
			return ClassType.ARRAY_TYPE;
		} else {
			if(_class == int.class || _class == Integer.class) {
				return ClassType.INT_TYPE;
			} else if(_class == long.class || _class == Long.class) {
				return ClassType.LONG_TYPE;
			} else if(_class == float.class || _class == Float.class) {
				return ClassType.FLOAT_TYPE;
			} else if(_class == double.class || _class == Double.class) {
				return ClassType.DOUBLE_TYPE;
			} else if(_class == char.class || _class == Character.class) {
				return ClassType.CHAR_TYPE;
			} else if(_class == boolean.class || _class == Boolean.class) {
				return ClassType.BOOL_TYPE;
			} else if(Enum.class.isAssignableFrom(_class)) {
				return ClassType.ENUM_TYPE;
			} else if(_class == String.class) { 
				return ClassType.STRING_TYPE;
			} else if(ElfEnum.class.isAssignableFrom(_class)) {
				return ClassType.ELFENUM_TYPE;
			}
			
			return ClassType.ELSE_TYPE;
		} 
	} 
	
	/**
	 * @param _class, int , long, float , double ,char, boolean, enum
	 * @return
	 */
	public static final boolean isBasicType(Class<?> _class) {
		if(_class == int.class || _class == Integer.class) {
			return true;
		} else if(_class == long.class || _class == Long.class) {
			return true;
		} else if(_class == float.class || _class == Float.class) {
			return true;
		} else if(_class == double.class || _class == Double.class) {
			return true;
		} else if(_class == char.class || _class == Character.class) {
			return true;
		} else if(_class == boolean.class || _class == Boolean.class) {
			return true;
		} else if(Enum.class.isAssignableFrom(_class)) { 
			return true;
		} 
		
		return false; 
	} 
}
