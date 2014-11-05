package com.ielfgame.stupidGame.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Stack;

import elfEngine.basic.node.ElfNode;

public class BasicXMLNode extends AbstractXMLItemConverter{
	
	public BasicXMLNode(Class<?> selectClass) { 
		super(selectClass); 
		
		reflect(mSelectClass); 
	} 
	
	public int hashCode() {
		return mSelectClass.hashCode();
	}
	
	void reflect(Class<?> _class) { 
		final Class<?> classSave = _class;
		final LinkedList<String> residList = new LinkedList<String>();
		final LinkedList<String> residArrayList = new LinkedList<String>();
		
		final Stack<Class<?>> stack = new Stack<Class<?>>(); 
		
		while(_class != null && ElfNode.class.isAssignableFrom(_class)) { 
			stack.add(_class); 
			_class = _class.getSuperclass(); 
		} 
		
		while(!stack.isEmpty()) {
			final Class<?> myClass = stack.pop(); 
			final Field [] fs = myClass.getDeclaredFields(); 
			
			for(int i=0; i<fs.length; i++) { 
				final Field f = fs[i];
				final String name = f.getName();
				
				if(name.startsWith("REF_")) {  
					final String funcName = name.substring(4);
					try { 
						f.setAccessible(true);
						final int mask = f.getInt(null);
						if((mask & ElfNode.XML_GET_SHIFT) != 0 || (mask & ElfNode.XML_SET_SHIFT) != 0 || (mask & ElfNode.XML_ALL_SHIFT) != 0) {
							final Method [] methods = myClass.getMethods(); 
							for(int j=0; j<methods.length; j++) { 
								final Method method = methods[j];
								final String methodName = method.getName();
								if(methodName.equals("get"+funcName)) { 
									final Class<?>  returnType = method.getReturnType(); 
									
									if(returnType == String.class) { 
										residList.add(funcName);
									} else if(returnType == String[].class) { 
										residArrayList.add(funcName);
									}
									
									this.addXMLAttr(returnType, funcName); 
								} 
							} 
						} 
					} catch (Exception e) { 
						e.printStackTrace(); 
					} 
				} 
			}
		}
		if(classSave != null) { 
			final String name = classSave.getSimpleName();
			XMLStringManage.checkInString(name, residList);
			XMLStringManage.checkInStringArray(name, residArrayList);
		} 
	}

	public Object createData(final Object parent) { 
		if(ElfNode.class.isAssignableFrom(mSelectClass)) { 
			Constructor<?> constructor;
			try { 
				constructor = mSelectClass.getDeclaredConstructor(ElfNode.class, int.class);
				final Object node = constructor.newInstance(parent, 1);
				
				((ElfNode)node).addToParent();
				
				return node;
			} catch (Exception e) { 
				e.printStackTrace();
				System.exit(1);
			} 
		} else { 
			System.err.println("Not ElfNode :"+mSelectClass.getSimpleName());
			System.exit(1);
		} 
		return null; 
	} 
	
	public void onAllChildsCreated(final Object data, final Object [] childs) {
		if(data != null) {
			((ElfNode) data).onRecognizeRequiredNodes(); 
		}
	} 
} 
