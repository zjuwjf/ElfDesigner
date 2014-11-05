package com.ielfgame.stupidGame.data;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Stack;

import com.ielfgame.stupidGame.data.Stringified.ErrorStruct.ErrorType;

import elfEngine.basic.utils.EnumHelper;

public class Stringified {
	
	final static Class<?> [] sBasicClasses = {
		int.class, Integer.class, 
		float.class, Float.class,
		boolean.class, Boolean.class, 
		double.class, Double.class, 
		char.class, Character.class,
		long.class, Long.class
		}; 
	
	//config ?
	final static String [] REPLACE_LEFT_BRACKETS = {"%;@0", "%;@1", "%;@2"};
	final static String [] REPLACE_RIGHT_BRACKETS = {"%;#0", "%;#1", "%;#2"};
	
	final static String [] LEFT_BRACKETS  = {"(", "[", "{"};
	final static String [] RIGHT_BRACKETS = {")", "]", "}"};
	
	final static String [] IGNORES = {"\t", " ", "\n", "\r", "\f"};
	final static String [] COMMA = {",", "，"};
	
	public static String toText(final Object object, boolean isField) { 
		if(object == null) { 
			return "null"; 
		} 
		
		if(object != null) {
			final Class<?> _class = object.getClass();
			
			if(_class.isArray()) { 
				String text = "";
				final int len = Array.getLength(object);
				for(int i=0; i<len; i++) {
					if(_class.getComponentType() == String.class) {
//						if(PlatformHelper.IS_WINDOWS) {
//							text += toText(Array.get(object, i), true) + ((i==len-1) ? "":",\n");
//						} else {
//							text += toText(Array.get(object, i), true) + ((i==len-1) ? "":",\n");
//						}
						text += toText(Array.get(object, i), true) + ((i==len-1) ? "":",\n");
					} else {
						text += toText(Array.get(object, i), true) + ((i==len-1) ? "":",");
					} 
				} 
				
				if(isField) {
					return "{" + text + "}";
				} else { 
					return text;
				} 
			} else if(_class == String.class) { 
				String text = (String)object;
				if(isField) {
					for(int i=0; i<LEFT_BRACKETS.length; i++) {
						text = text.replace(LEFT_BRACKETS[i], REPLACE_LEFT_BRACKETS[i]);
						text = text.replace(RIGHT_BRACKETS[i], REPLACE_RIGHT_BRACKETS[i]);
					}
					return "{" + text + "}";
				} else { 
					return String.valueOf(text);
				} 
			} else if(Enum.class.isAssignableFrom(_class)) {
				return String.valueOf(object);
			} else if(isBasicClass(_class)) { 
				return String.valueOf(object);
			} else {
				try {
					final Field [] fields = _class.getFields();
					String text = "";
					for(int i=0; i<fields.length; i++) {
						final Object fieldObj = fields[i].get(object);
						text += toText(fieldObj, true) + ((i==fields.length-1) ? "":",");
					} 
					
					if(isField) {
						return "{" + text + "}";
					} else {
						return text;
					}
				} catch (Exception e) { 
					e.printStackTrace();
				} 
			} 
		} 
		return null;
	} 
	
	//0---value, 1---error
	public static Object [] fromText(final Class<?> _class, final String text) {
		return fromText(_class, text, 0, text.length(), false);
	} 
	
	static Object [] fromText(final Class<?> _class, final String text, final int begin, final int end, boolean isField) {
		assert (_class != null) ;
		if(_class.isArray()) {
			final Object [] ret;
			ret = breakText(text, begin, end, isField);
			
			final Range [] ranges = (Range [])ret[0];
			final ErrorStruct error = (ErrorStruct)ret[1];
			
			if(ranges == null) { 
				return new Object[] {null, error};
			} else {
				final Class<?> type = _class.getComponentType();
				final Object array = Array.newInstance(type, ranges.length);
				for(int i=0; i<ranges.length; i++) {
					final Object [] childRet = fromText(type, text, ranges[i].begin, ranges[i].end, true);
					final Object childObj = childRet[0];
					final ErrorStruct childError = (ErrorStruct)childRet[1];
					if(childError != null) {
						return new Object[] {null, childError};
					} else {
						Array.set(array, i, childObj);
					}
				} 
				
				return new Object[] {array, null};
			}
		} else {
			final String subString = text.substring(begin, end);
			if(_class == int.class || _class == Integer.class) {
				try {
					return new Object [] { Integer.valueOf(subString), null};
				} catch (Exception e) {
					final ErrorStruct error = new ErrorStruct();
					error.beginPos = begin;
					error.endPos = end;
					error.errorInfo = subString+":Not A Valid Int";
					error.errorType = ErrorType.Unacceptable;
					return new Object [] { null, error};
				}
			} else if(_class == float.class || _class == Float.class) {
				try {
					return new Object [] { Float.valueOf(subString), null};
				} catch (Exception e) {
					final ErrorStruct error = new ErrorStruct();
					error.beginPos = begin;
					error.endPos = end;
					error.errorInfo = subString+":Not A Valid Float";
					error.errorType = ErrorType.Unacceptable;
					return new Object [] { null, error};
				}
			} else if(_class == double.class || _class == Double.class) {
				try {
					return new Object [] { Double.valueOf(subString), null};
				} catch (Exception e) {
					final ErrorStruct error = new ErrorStruct();
					error.beginPos = begin;
					error.endPos = end;
					error.errorInfo = subString+":Not A Valid Double";
					error.errorType = ErrorType.Unacceptable;
					return new Object [] { null, error};
				}
			} else if(_class == boolean.class || _class == Boolean.class) {
				try {
					return new Object [] { Boolean.valueOf(subString), null};
				} catch (Exception e) {
					final ErrorStruct error = new ErrorStruct();
					error.beginPos = begin;
					error.endPos = end;
					error.errorInfo = subString+":Not A Valid Boolean";
					error.errorType = ErrorType.Unacceptable;
					return new Object [] { null, error};
				}
			} else if(_class == char.class || _class == Character.class) {
				if(subString!=null && subString.length() == 1) {
					return new Object [] { Character.valueOf(subString.charAt(0)), null};
				} else  {
					final ErrorStruct error = new ErrorStruct();
					error.beginPos = begin;
					error.endPos = end;
					error.errorInfo = subString+":Not A Valid Char";
					error.errorType = ErrorType.Unacceptable;
					return new Object [] { null, error};
				} 
			} else if(_class == long.class || _class == Long.class) {
				try {
					return new Object [] { Long.valueOf(subString), null};
				} catch (Exception e) {
					final ErrorStruct error = new ErrorStruct();
					error.beginPos = begin;
					error.endPos = end;
					error.errorInfo = subString+":Not A Valid Long";
					error.errorType = ErrorType.Unacceptable;
					return new Object [] { null, error};
				}
			} else if(Enum.class.isAssignableFrom(_class)) {  
				@SuppressWarnings("unchecked")
				final Enum<?> myEnum = EnumHelper.fromStringNoT((Class<? extends Enum<?>>)_class, subString);
				if(myEnum != null) {
					return new Object [] { myEnum, null};
				} else {
					final ErrorStruct error = new ErrorStruct();
					error.beginPos = begin;
					error.endPos = end;
					error.errorInfo = subString+":Not A Valid Enum("+_class.getSimpleName()+")";
					error.errorType = ErrorType.Unacceptable;
					return new Object [] { null, error};
				}
			} else if(_class == String.class) {
				if(isField) {
					final Range range = removeABracket(text, begin, end);
					if(range == null) {
						//error
						final ErrorStruct errorStruct = new ErrorStruct();
						errorStruct.beginPos = begin;
						errorStruct.endPos = end;
						errorStruct.errorType = ErrorType.UnexpectChar;
						errorStruct.errorInfo = "At pos ["+(begin)+","+end+"], expect for {...}";
						return new Object [] {null, errorStruct};
					} else {
						String retStr = text.substring(range.begin, range.end);
						for(int i=0; i<LEFT_BRACKETS.length; i++) {
							retStr = retStr.replace(REPLACE_LEFT_BRACKETS[i], LEFT_BRACKETS[i]);
							retStr = retStr.replace(REPLACE_RIGHT_BRACKETS[i], RIGHT_BRACKETS[i]);
						}
						return new Object [] {retStr, null};
					} 
				} else {
					String ret = subString;
					for(int i=0; i<LEFT_BRACKETS.length; i++) {
						ret = ret.replace(REPLACE_LEFT_BRACKETS[i], LEFT_BRACKETS[i]);
						ret = ret.replace(REPLACE_RIGHT_BRACKETS[i], RIGHT_BRACKETS[i]);
					}
					return new Object [] { ret, null};
				}
			} else {
				try {
					final Object classRet = _class.newInstance();
				
					final Field [] fields = _class.getFields();
					
					final Object [] ret;
					if(isField) {
						ret = breakText(text, begin, end, true);
					} else {
						ret = breakText(text, begin, end, false);
					} 
					
					final Range [] ranges = (Range [])ret[0];
					final ErrorStruct error = (ErrorStruct)ret[1];
					
					if(ranges == null) { 
						return new Object[] {null, error};
					} else if(fields.length != ranges.length) {
						final ErrorStruct newError = new ErrorStruct();
						newError.beginPos = begin;
						newError.endPos = end;
						newError.errorInfo = _class.getSimpleName()+" takes "+fields.length + "fields not "+ranges.length;
						newError.errorType = ErrorType.Unacceptable;
						return new Object[] {null, newError};
					} else {
						for(int i=0; i<fields.length; i++) {
							final Range range = ranges[i];
							final Object [] childRet = fromText(fields[i].getType(), text, range.begin, range.end, true);
							final Object childObj = childRet[0];
							final ErrorStruct childError = (ErrorStruct)childRet[1];
							if(childError != null) {
								return new Object[] {null, childError};
							} else {
								fields[i].set(classRet, childObj);
							} 
						}
					} 
					
					return new Object[] {classRet, null};
				} catch (Exception e) { 
					e.printStackTrace();
					
					final ErrorStruct newError = new ErrorStruct();
					newError.beginPos = begin;
					newError.endPos = end;
					newError.errorInfo = "Check the Exception!";
					newError.errorType = ErrorType.Unacceptable;
					return new Object[] {null, newError};
				}
			}
		}
	} 
	
	public static class ErrorStruct {
		public static enum ErrorType {
			UnexpectChar, Unacceptable, Unfinished
		}
		public String errorInfo;
		public ErrorType errorType;
		public int beginPos, endPos;
	} 
	
	static Range removeABracket(final String text , final int searchBeginPos, final int searchEndPos) {
		int mySearchBeginPos = -1, mySearchEndPos = -1;
		for(int i=searchBeginPos; i<searchEndPos; i++) {
			final String key = text.substring(i, i+1);
			final int leftBracketIndex = indexOf(LEFT_BRACKETS, key);
			final int ignoreIndex = indexOf(IGNORES, key);
			if(leftBracketIndex != 0) {
				mySearchBeginPos = i+1;
				break;
			} else if(ignoreIndex != 0) {
				//error
				return null;
			}
		} 
		
		if(mySearchBeginPos < 0) {
			return null;
		} 
		
		for(int i=searchEndPos-1; i>=searchBeginPos; i--) {
			final String key = text.substring(i, i+1);
			final int rightBracketIndex = indexOf(RIGHT_BRACKETS, key);
			final int ignoreIndex = indexOf(IGNORES, key);
			if(rightBracketIndex != 0) {
				mySearchEndPos = i;
				break;
			} else if(ignoreIndex != 0) {
				return null;
			} 
		} 
		
		if(mySearchEndPos < 0) {
			return null;
		} 
		
		return new Range(mySearchBeginPos, mySearchEndPos);
	} 
	
	static Object [] breakText( final String text , final int searchBeginPos, final int searchEndPos, boolean isBreakBracket) { 
		int mySearchBeginPos = -1, mySearchEndPos = -1;
		if(isBreakBracket) {
			final Range range = removeABracket(text, searchBeginPos, searchEndPos);
			if(range == null) {
				//error
				final ErrorStruct errorStruct = new ErrorStruct();
				errorStruct.beginPos = searchBeginPos;
				errorStruct.endPos = searchEndPos;
				errorStruct.errorType = ErrorType.UnexpectChar;
				errorStruct.errorInfo = "At pos ["+(searchBeginPos)+","+searchEndPos+"], expect for {...}";
				return new Object [] {null, errorStruct};
			} else {
				mySearchBeginPos = range.begin;
				mySearchEndPos = range.end;
			}
		} else {
			mySearchBeginPos = searchBeginPos;
			mySearchEndPos = searchEndPos;
		}
		
		final LinkedList<Range> list = new LinkedList<Range>();
		
		final int STATE_SEARCH_BEGIN = 0;
		final int STATE_SEARCH_END = 1; 
		final int STATE_SEARCH_COMMA = 2;

		int state = STATE_SEARCH_BEGIN;
		
		final Stack<Integer> brackets = new Stack<Integer>();
		
		int beginPos = searchBeginPos;
		
		for(int i=mySearchBeginPos; i<mySearchEndPos; i++) { 
			final String key = text.substring(i, i+1);
			
			final int leftBracketIndex = indexOf(LEFT_BRACKETS, key);
			final int rightBracketIndex = indexOf(RIGHT_BRACKETS, key);
			final int ignoreIndex = indexOf(IGNORES, key);
			final int commaIndex = indexOf(COMMA, key);
			switch (state) {
			case STATE_SEARCH_BEGIN:
				if(leftBracketIndex != -1) {
					brackets.push(leftBracketIndex);
					beginPos = i;
					state = STATE_SEARCH_END;
				} else if(rightBracketIndex != -1 || commaIndex != -1) {
					//error
					final ErrorStruct errorStruct = new ErrorStruct();
					errorStruct.beginPos = searchBeginPos;
					errorStruct.endPos = searchEndPos;
					errorStruct.errorType = ErrorType.UnexpectChar;
					errorStruct.errorInfo = "At pos "+i+", unexpectChar : " + key ;
					return new Object [] {null, errorStruct};
				} else if(ignoreIndex != -1) {
					//continue
				} else {
					//find
					beginPos = i;
					state = STATE_SEARCH_END;
				} 
				break;
			case STATE_SEARCH_END:
				if(leftBracketIndex != -1) {
					brackets.push(leftBracketIndex);
				} else if(rightBracketIndex != -1) {
					if(brackets.isEmpty() || rightBracketIndex != brackets.peek()) {
						//error
						final ErrorStruct errorStruct = new ErrorStruct();
						errorStruct.beginPos = searchBeginPos;
						errorStruct.endPos = searchEndPos;
						errorStruct.errorType = ErrorType.UnexpectChar;
						errorStruct.errorInfo = "At pos "+(searchBeginPos+i)+", unexpectChar : " + key ;
						return new Object [] {null, errorStruct};
					} else {
						brackets.pop();
						if(brackets.isEmpty()) {
							list.add(new Range(beginPos, i + 1));
							state = STATE_SEARCH_COMMA;
						} 
					}
					
				} else if(commaIndex != -1) {
					if(brackets.isEmpty()) {
						list.add(new Range(beginPos, i));
						state = STATE_SEARCH_BEGIN;
					} 
				} else if(ignoreIndex != -1) {
					//continue
					if(brackets.isEmpty()) {
						list.add(new Range(beginPos, i));
						state = STATE_SEARCH_COMMA;
					}
				} else {
					//continue
				}
				break;
			case STATE_SEARCH_COMMA:
				if(leftBracketIndex != -1 || rightBracketIndex != -1) {
					//error
					final ErrorStruct errorStruct = new ErrorStruct();
					errorStruct.beginPos = searchBeginPos;
					errorStruct.endPos = searchEndPos;
					errorStruct.errorType = ErrorType.UnexpectChar;
					errorStruct.errorInfo = "At pos "+(searchBeginPos+i)+", unexpectChar : " + key  + " , expect for ,";
					return new Object [] {null, errorStruct};
				} else if(commaIndex != -1) {
					//find
//					list.
					state = STATE_SEARCH_BEGIN;
				} else if(ignoreIndex != -1) {
					//continue
				} else {
					//error
					final ErrorStruct errorStruct = new ErrorStruct();
					errorStruct.beginPos = searchBeginPos;
					errorStruct.endPos = searchEndPos;
					errorStruct.errorType = ErrorType.UnexpectChar;
					errorStruct.errorInfo = "At pos "+(searchBeginPos+i)+", unexpectChar..: " + key  + " , expect for ,";
					return new Object [] {null, errorStruct};
				} 
				break;
			}
		} 
		
		if(state == STATE_SEARCH_END) {
			if(!brackets.isEmpty()) {
				final ErrorStruct errorStruct = new ErrorStruct();
				errorStruct.beginPos = searchBeginPos;
				errorStruct.endPos = searchEndPos;
				errorStruct.errorType = ErrorType.Unfinished;
				errorStruct.errorInfo = "At pos ["+(searchBeginPos)+","+searchEndPos+"], Unfinished for break!";
				return new Object [] {null, errorStruct};
			} else {
				list.add(new Range(beginPos, mySearchEndPos));
			} 
		} 
		
		final Range [] StrRet = new Range[list.size()];
		list.toArray(StrRet);
		//no error
		final Object [] ret = {StrRet, null};
		return ret;
	} 
	
	static <T> int indexOf(T [] array, T t) {
		for(int i=0; i<array.length; i++) {
			if(array[i].equals(t)) { 
				return i;
			} 
		} 
		return -1;
	}
	
	static boolean isBasicClass(final Class<?> key) {
		for(Class<?> _class : sBasicClasses) {
			if(_class == key) {
				return true;
			}
		} 
		return false;
	} 
	
	static class Range {
		public int begin, end;
		public Range(int begin, int end) {
			this.begin = begin;
			this.end = end;
		}
	}
	
	public static void main(String [] args) { 
		final ElfPointf [] ps = new ElfPointf[] {new ElfPointf(100, 100),new ElfPointf(0, 0),new ElfPointf(200, -50), };
		final String text = Stringified.toText(ps, false);
		System.err.println( text );
		
		final Object [] ret = Stringified.fromText(ElfPointf[].class, text);
		
		System.err.println( Stringified.toText(ret, false) );
	} 
} 
