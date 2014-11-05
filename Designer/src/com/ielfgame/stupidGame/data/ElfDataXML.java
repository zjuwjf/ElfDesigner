package com.ielfgame.stupidGame.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.dom4j.Element;

import com.ielfgame.stupidGame.power.ISingleton;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.xml.IXMLItemConverter;
import com.ielfgame.stupidGame.xml.NewXMLFactory;

public abstract class ElfDataXML extends ElfDataDisplay implements IXMLItemConverter, ISingleton{
	
	private final boolean mSingleton;
	
	public ElfDataXML(){ 
		this(true);
	} 
	
	public ElfDataXML(boolean singleton){ 
		//singleton
		mSingleton = singleton;
		if(singleton) {
			assert(PowerMan.getSingleton(this.getClass()) == null);
			PowerMan.register(this.getClass(), this);
		}
		
		NewXMLFactory.checkIn(this); 
	} 
	
	public void onChildCreated(Object data, Object child) {
	}
	
	public void onAllChildsCreated(Object data, Object[] child) {
	}
	
	public Element createElement(Element parent) {
		final Element element = parent.addElement(this.getClass().getSimpleName());
		return element;
	}
	
	public Object createData(Object parent) {
		if(mSingleton) {
			return this;
		} else {
			Constructor<?> c = null;
			try {
				c = this.getClass().getConstructor(boolean.class);
			} catch (Exception e) {
			}
			
			if(c != null) {
				try {
					return c.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			
			try {
				c = this.getClass().getConstructor();
			} catch (Exception e) {
			}
			
			if(c != null) {
				try {
					return c.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} else { 
				System.err.println("ElfDataXML createData ERROR!");
				assert(false);
			}
		}
		return this;
	}

	public void setDataProperty(Object select, Element element) {
		final ElfDataDisplay data = (ElfDataDisplay)select;
		final String [] labels = data.toLabels();
		final int size = labels.length;
		final String [] values = new String[size];
		
		for(int i=0; i<size; i++) { 
			values[i] = element.attributeValue(labels[i]);
			data.setValueByLabelAndString(labels[i], values[i]);
//			values[i] = element.attributeValue(labels[i]);
		} 
//		data.setValuesByStrings(values); 
	} 
	
	public void setElemenAttr(Object select, Element element) {
		final ElfDataDisplay data = (ElfDataDisplay)select;
		final String [] labels = data.toLabels();
		final String [] values = data.toValues();
		final int size = labels.length;
		for(int i=0; i<size; i++) {
			element.addAttribute(labels[i], values[i]);
		}
	} 

	public Element[] getElementChilds(Element element) {
		return new Element[0];
	}
	
	public Object[] getChilds(Object data) {
		return new Object[0];
	}
	
	public boolean isMyLabel(Element element) {
		final String name = element.getName();
		return this.getClass().getSimpleName().equals(name);
	}

	public boolean isMySuperType(Object data) {
		return false;
	}

	public boolean isMyType(Object data) {
		return data != null && this.getClass() == data.getClass();
	}
}
