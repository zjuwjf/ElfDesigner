package com.ielfgame.stupidGame.xml;

import org.dom4j.Element;

public interface IXMLItemConverter {
	
	public void onChildCreated(final Object data, final Object child);
	public void onAllChildsCreated(final Object data, final Object [] child);
	
	public Element createElement(final Element parent);
	public Object createData(final Object parent);
	
	public void setDataProperty(final Object select, final Element element);
	public void setElemenAttr(final Object select, final Element element);
	
	public Element[] getElementChilds(final Element element) ;
	
	public Object[] getChilds(final Object data) ;
	
	public boolean isMyLabel(final Element element);
	public boolean isMySuperType(final Object data);
	public boolean isMyType(final Object data) ;
}
