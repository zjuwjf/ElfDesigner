package com.ielfgame.stupidGame.xml;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

public class AbstractXMLItemConverter implements IXMLItemConverter {
	
	protected final Class<?> mSelectClass;
	protected final LinkedList<BasicXMLAttr> mAttrList = new LinkedList<BasicXMLAttr>();
	
	public AbstractXMLItemConverter(final Class<?> selectClass) { 
		this.mSelectClass = selectClass;
		
		assert (mSelectClass != null);
	} 

	public Element createElement(final Element parent) {
		final Element element = parent.addElement(mSelectClass.getSimpleName());
		return element;
	} 
	
	public Object createData(final Object parent) { 
		try {
			return mSelectClass.newInstance();
		} catch (Exception e) { 
			e.printStackTrace();
			System.exit(1); 
		} 
		return null;
	}
	
	/**
	 * @param element
	 * @return with createData
	 */
	public boolean isMyLabel(final Element element) {
		return element != null && element.getName().equals(mSelectClass.getSimpleName());
	}
	
	/**
	 * @param data
	 * @return with setDataProperty and setElemenAttr
	 */
	public boolean isMySuperType(final Object data) { 
		return data != null && mSelectClass.isAssignableFrom(data.getClass());
	} 
	
	/**
	 * @param data
	 * @return with createElement
	 */
	public boolean isMyType(final Object data) {
		return data != null && mSelectClass == data.getClass();
	}

	public void addXMLAttr(Class<?> valueType, String func) {
		final BasicXMLAttr basicXMLAttr = new BasicXMLAttr(mSelectClass, valueType, func);
		mAttrList.add(basicXMLAttr);
	} 
	
	public void addXMLAttr(Class<?> valueType, String func, String label) {
		final BasicXMLAttr basicXMLAttr = new BasicXMLAttr(mSelectClass, valueType, "get"+func, "set"+func, label);
		mAttrList.add(basicXMLAttr);
	} 

	/**
	 * @param data
	 * @return with createElement
	 */
	public final Object[] getChilds(final Object data) {
		if(data instanceof IXMLChilds) {
			return ((IXMLChilds) data).getChildsForXML();
		} 
		return null; 
	} 
	
	public final Element[] getElementChilds(final Element element) {
		@SuppressWarnings("rawtypes")
		final List eles = element.elements();
		final Element[] ret = new Element[eles.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (Element) eles.get(i);
		} 
		return ret;
	} 

	// from xml
	public void setDataProperty(final Object select, final Element element) {
		for (BasicXMLAttr basicXMLAttr : mAttrList) {
			basicXMLAttr.setDataProperty(select, element);
		} 
	} 
	
	// to xml
	public void setElemenAttr(final Object select, final Element element) {
		for (BasicXMLAttr basicXMLAttr : mAttrList) { 
			basicXMLAttr.setElemenAttr(select, element); 
		} 
	} 

	public static void sort(final List<AbstractXMLItemConverter> converters) {
		Collections.sort(converters, new Comparator<AbstractXMLItemConverter>() {
			public int compare(AbstractXMLItemConverter arg0, AbstractXMLItemConverter arg1) {
				if(arg0.mSelectClass == arg1.mSelectClass) {
					return 0;
				} else if(arg0.mSelectClass.isAssignableFrom(arg1.mSelectClass)) {
					return 1;
				} else if(arg1.mSelectClass.isAssignableFrom(arg0.mSelectClass)) {
					return -1;
				} else { 
					//
					throw new IllegalArgumentException("AbstractXMLItemConverter : sort with different ethnic groups!");
				} 
			}
		});
	}

	public void onChildCreated(Object data, Object child) {
	}
	
	public void onAllChildsCreated(Object data, Object[] child) {
	} 
}
