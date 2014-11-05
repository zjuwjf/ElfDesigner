package elfEngine.basic.utils;

import elfEngine.opengl.BlendMode;


public class EnumHelper {
	
	public static <T extends Enum<T>> String [] toString(final Class<T> _class){ 
		final Enum<T>[] types = _class.getEnumConstants();
		String [] ret = new String[types.length];
		for(int i=0; i<types.length; i++){
			ret[i] = types[i].toString();
		} 
		return ret;
	} 
	
	public static <T extends Enum<T>> T fromString(final Class<T> _class, String string){
		final T[] types = _class.getEnumConstants();
		for(int i=0; i<types.length; i++){
			if( types[i].toString().equalsIgnoreCase(string) ){
				return types[i]; 
			} 
		} 
		return null;
	}
	
	public static String [] toStringNoT(final Class<? extends Enum<?>> _class){
		final Enum<?>[] types = _class.getEnumConstants();
		String [] ret = new String[types.length];
		for(int i=0; i<types.length; i++){
			ret[i] = types[i].toString();
		}
		return ret;
	} 
	
	public static Enum<?> fromStringNoT(final Class<? extends Enum<?>> _class, String string){
		final Enum<?> [] types = _class.getEnumConstants();
		for(int i=0; i<types.length; i++){
			if( types[i].toString().equalsIgnoreCase(string) ){
				return types[i]; 
			} 
		} 
		return null;
	}
	
	public static void main(String [] args){
		final String [] texts = toString(BlendMode.class);
		for(int i=0; i<texts.length; i++){
			System.err.println(texts[i]);
		}
	}
	
	public enum BoolEnum {
		True(true), False(false);
		final boolean value;
		BoolEnum(boolean value){
			this.value = value;
		}
		
		public boolean value(){
			return value;
		}
		
		public String toString(){
			return super.toString().toLowerCase();
		}
	}
}
