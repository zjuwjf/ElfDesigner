package com.ielfgame.stupidGame.xml;

import java.lang.reflect.Field;

import org.dom4j.Element;

import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.face.action.CCActionData;
import com.ielfgame.stupidGame.face.action.CCActionHelper;
import com.ielfgame.stupidGame.face.action.CCActionHelper.AbstractNewAction;

public class ActionXML extends AbstractXMLItemConverter {
	
	// Class, fileds
	public ActionXML() { 
		super(CCActionData.class);
	} 
	
	// from xml
	public void setDataProperty(final Object select, final Element element) {
		try {
			final CCActionData data = (CCActionData)select;
			
			final String className = element.attributeValue("mActoinClass");
			final String structClassName = "_" + className;
			
			AbstractNewAction abstractNewAction = null;
			final Class<?>[] _classes = CCActionHelper.class.getDeclaredClasses();
			for(int i=0; i<_classes.length; i++) { 
				final Class<?> _class = _classes[i]; 
				if(_class.getSimpleName().equals(structClassName)) { 
					abstractNewAction = (AbstractNewAction)_class.newInstance(); 
				} 
			}  
			
			if(abstractNewAction != null) {
				data.setAbstractNewAction(abstractNewAction); 
				final Field [] fs = abstractNewAction.getClass().getFields(); 
				for(int i=0; i<fs.length; i++) { 
					fs[i].setAccessible(true); 
					if(!fs[i].getName().contains("Class")) { 
						fs[i].set(abstractNewAction, Stringified.fromText(fs[i].getType(), element.attributeValue(fs[i].getName()))[0]);
					}  
				} 
			} 
			
			try { 
				final String [] targetFullNames = (String[])Stringified.fromText(String[].class, element.attributeValue("Targets"))[0];
				data.setTargetFullNames(targetFullNames); 
			} catch (Exception e) { 
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	} 
	
	public void onChildCreated(Object data, Object child) { 
		final CCActionData parentData = (CCActionData)data;
		final CCActionData childData = (CCActionData)child;
		if(parentData != null && childData != null) { 
			parentData.mChildsList.add(childData);
		} 
	} 
	
	// to xml
	public void setElemenAttr(final Object select, final Element element) {
		try {
			final CCActionData data = (CCActionData)select;
			
			final AbstractNewAction abstractNewAction = data.getAbstractNewAction();
			if(abstractNewAction != null) {
				final Field [] fs = abstractNewAction.getClass().getFields();
				for(int i=0; i<fs.length; i++) { 
					fs[i].setAccessible(true);
					if(!fs[i].getName().contains("Class")) {
						element.addAttribute(fs[i].getName(), Stringified.toText(fs[i].get(abstractNewAction), false));
					} else { 
						element.addAttribute(fs[i].getName(), ((Class<?>)fs[i].get(abstractNewAction)).getSimpleName());
					} 
				} 
			} 
			
			boolean isRoot = data.getIsRoot();
			try { 
				final String parentName = element.getParent().getName();
				if(!parentName.equals("CCActionData")) {
					isRoot = true;
				}
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
			element.addAttribute("IsRoot", ""+isRoot);
			
			if(isRoot) { 
				try {
					final String [] targetFullNames = data.getTargetFullNames(); 
					final String text = Stringified.toText(targetFullNames, false);
					element.addAttribute("Targets", XMLConfig.toXML(text)); 
				} catch (Exception e) { 
					e.printStackTrace();
				} 
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 
} 
