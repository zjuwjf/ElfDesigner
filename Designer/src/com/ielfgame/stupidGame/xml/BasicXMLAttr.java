package com.ielfgame.stupidGame.xml;

import java.lang.reflect.Method;

import org.dom4j.Element;

import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.data.Stringified.ErrorStruct;

public class BasicXMLAttr { 
	
	protected final Class<?> mSelectClass;
	protected final Class<?> mValueClass;
	
	protected Method mGetFunc;
	protected Method mSetFunc;
	
	protected final String mAttrLabel;
	
	public BasicXMLAttr(Class<?> mSelectClass, Class<?> mValueClass, 
			String getFunc, String setFunc, 
			String mAttrLabel) {
		super();
		this.mSelectClass = mSelectClass;
		this.mValueClass = mValueClass;
		try {
			this.mGetFunc = mSelectClass.getMethod(getFunc);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} 
		
		try {
			this.mSetFunc = mSelectClass.getMethod(setFunc,mValueClass);
		} catch (Exception e) {
//			e.printStackTrace();
			mSetFunc = null;
		} 
		
		this.mAttrLabel = mAttrLabel;
	} 
	
	public BasicXMLAttr(Class<?> mSelectClass, Class<?> mValueClass, String func) {
		this(mSelectClass, mValueClass, "get"+func, "set"+func, func); 
	} 
	
//	static final String ENTER = "&#x0D;";
	
	//from xml
	public void setDataProperty(final Object select, final Element element) {
		if(select != null && mSelectClass.isAssignableFrom(select.getClass())) {
			String text = element.attributeValue(mAttrLabel);
			
			if(text == null) {
//				if(mAttrLabel.equals("FontSizeRate")) { 
//					try { 
//						//
//						mSetFunc.invoke(select, 1); 
//					} catch (Exception e) { 
//					} 
//				} 
				
//				System.err.println("XML:"+mSelectClass.getSimpleName() + " Does Not Has " + mAttrLabel);
				return ;
			} 
			//Escape Sequence
			text = XMLConfig.fromXML(text);
			
			final Object[] ret = Stringified.fromText(mValueClass, text);
			
			final Object value = ret[0];
			final ErrorStruct errorRet = (ErrorStruct)ret[1];
			if(errorRet == null) {
				try {
					if(mSetFunc != null) {
						mSetFunc.invoke(select, value);
					} else { 
						if(!mAttrLabel.equals("FullName")) {
//							System.out.println("XML:"+mSelectClass.getSimpleName()+", No set"+mAttrLabel+" Func");
						} 
					} 
				} catch (Exception e) { 
					e.printStackTrace(); 
				} 
			} else { 
				System.err.println("error info:"+errorRet.errorInfo+"\n");
				System.err.println("error type:"+errorRet.errorType+"\n");
				System.err.println("error pos:["+errorRet.beginPos+","+errorRet.endPos+")\n");
			} 
		} 
	} 
	
	//to xml
	public void setElemenAttr(final Object select, final Element element) {
		if(select != null && mSelectClass.isAssignableFrom(select.getClass()) && mGetFunc != null) {
			try {
				final Object value = mGetFunc.invoke(select);
				String out = Stringified.toText(value, false);
				out = XMLConfig.toXML(out);
				element.addAttribute(mAttrLabel, out);
			} catch (Exception e) { 
				e.printStackTrace();
			} 
		} else { 
			//error
		}
	} 
} 
