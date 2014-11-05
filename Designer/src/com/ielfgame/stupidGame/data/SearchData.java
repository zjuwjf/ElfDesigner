package com.ielfgame.stupidGame.data;

import java.lang.reflect.Method;
import java.util.LinkedList;

import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;

public class SearchData extends ElfDataXML{
	public enum SearchType {
		ByNodeName,
		ByNodeType,
		ByResids,
		ByProperty,
	} 
	
	public enum SearchOp {
		Contains,
		NotContains,
		Equals,
		NotEquals,
		ContainsIgnoreCase,
		NotContainsIgnoreCase,
		EqualsIgnoreCase,
		NotEqualsIgnoreCase,
		Regex
	} 
	
	public SearchType type = SearchType.ByNodeName; 
	public SearchOp op = SearchOp.Contains;
	public String value; 
	public String property; 
	
	public ElfNode [] search() {
		final DataModel model =  PowerMan.getSingleton(DataModel.class);
		if(model != null) {
			final ElfNode screenNode = model.getRootScreen();
			final LinkedList<ElfNode> list = new LinkedList<ElfNode>();
			screenNode.iterateChildsDeep(new IIterateChilds() { 
				public boolean iterate(final ElfNode node) {
					if( match(node, SearchData.this) ) {
						list.add(node);
					}
					return false;
				}
			});
			
			final ElfNode [] ret = new ElfNode[list.size()];
			list.toArray(ret);
			return ret;
		}
		return new ElfNode[0];
	} 
	
	public static boolean match(final ElfNode node, final SearchData data) {
		switch (data.type) {
		case ByNodeName:return match(node, "FullName", data.value, data.op);
		case ByNodeType:return match2(node, data.value, data.op);
		case ByResids:return match(node, "SelfResids", data.value, data.op);
		case ByProperty:return match(node, data.property, data.value, data.op);
		default:
			break;
		}
		return false;
	}
	
	public static boolean match2(final ElfNode node, final String value, final SearchOp op) {
		try {
			String text = node.getClass().getSimpleName();
			if(op == SearchOp.Contains) {
				if(text.contains(value)) {
					return true;
				}
			} else if(op == SearchOp.Equals) {
				if(text.equals(value)) {
					return true;
				}
			} else if(op == SearchOp.EqualsIgnoreCase) {
				if(text.equalsIgnoreCase(value)) {
					return true;
				}
			} else if(op == SearchOp.ContainsIgnoreCase) {
				if(text.toLowerCase().contains(value.toLowerCase())) {
					return true;
				}
			} else if(op == SearchOp.Regex) {
				if(text.regionMatches(0, value, 0, text.length())) {
					return true;
				}
			} else if(op == SearchOp.NotContains) {
				if(!text.contains(value)) {
					return true;
				}
			} else if(op == SearchOp.NotEquals) {
				if(!text.equals(value)) {
					return true;
				}
			} else if(op == SearchOp.NotEqualsIgnoreCase) {
				if(!text.equalsIgnoreCase(value)) {
					return true;
				} 
			} else if(op == SearchOp.NotContainsIgnoreCase) {
				if(!text.toLowerCase().contains(value.toLowerCase())) {
					return true;
				}
			} 
		} catch (Exception e) {
		}
		return false;
	}
	
	public static boolean match(final ElfNode node, final String property, final String value, final SearchOp op) {
		try {
			
			if(node.getClass().getSimpleName().equals(value)) {
				return true;
			}
			
			final Class<?> _class = node.getClass();
			final Method method = _class.getMethod("get"+property);
			if(method != null) {
				final Object ret = method.invoke(node);
				String text = Stringified.toText(ret, false);
				if(op == SearchOp.Contains) {
					if(text.contains(value)) {
						return true;
					}
				} else if(op == SearchOp.Equals) {
					if(text.equals(value)) {
						return true;
					}
				} else if(op == SearchOp.EqualsIgnoreCase) {
					if(text.equalsIgnoreCase(value)) {
						return true;
					}
				} else if(op == SearchOp.ContainsIgnoreCase) {
					if(text.toLowerCase().contains(value.toLowerCase())) {
						return true;
					}
				} else if(op == SearchOp.Regex) {
					if(text.regionMatches(0, value, 0, text.length())) {
						return true;
					}
				} else if(op == SearchOp.NotContains) {
					if(!text.contains(value)) {
						return true;
					}
				} else if(op == SearchOp.NotEquals) {
					if(!text.equals(value)) {
						return true;
					}
				} else if(op == SearchOp.NotEqualsIgnoreCase) {
					if(!text.equalsIgnoreCase(value)) {
						return true;
					} 
				} else if(op == SearchOp.NotContainsIgnoreCase) {
					if(!text.toLowerCase().contains(value.toLowerCase())) {
						return true;
					}
				} 
				
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public static void main(String [] args) {
	}
} 
