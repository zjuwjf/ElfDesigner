package elfEngine.util.particleXml;

import java.util.ArrayList;

public class XMLNode {
	public final String name;
	public final ArrayList<XMLNode> childs;
	public String value;
	private final int mDepth;
	
	public XMLNode(final String name, final int depth){
		this.name = name;
		this.childs = new ArrayList<XMLNode>();
		this.mDepth = depth;
	}
	
	public int getDepth(){
		return mDepth;
	}
	
	public ArrayList<XMLNode> findByKey(final String key){
		final ArrayList<XMLNode> array = new ArrayList<XMLNode>();
		
		if(name!=null&&key!=null){
			if(name.equals(key)){
				array.add(this);
			} else {
				for(XMLNode node:childs){
					final ArrayList<XMLNode> addList = node.findByKey(key);
					for(XMLNode add:addList){
						array.add(add);
					}
				}
			}
		}
		
		return array;
	}
	
	public ArrayList<XMLNode> findByValue(final String pValue){
		final ArrayList<XMLNode> array = new ArrayList<XMLNode>();
		
		if(this.value!=null){
			if(this.value.equals(pValue)){
				array.add(this);
			} else {
				for(XMLNode node:childs){
					final ArrayList<XMLNode> addList = node.findByValue(pValue);
					for(XMLNode add:addList){
						array.add(add);
					}
				}
			}
		} else {
			for(XMLNode node:childs){
				final ArrayList<XMLNode> addList = node.findByValue(pValue);
				for(XMLNode add:addList){
					array.add(add);
				}
			}
		}
		
		
		
		return array;
	}
	
	public XMLNode findNextByNode(final XMLNode node){
		if(childs.isEmpty()){
			return null;
		} else {
			final int size = childs.size();
			for(int i=0; i<size; i++){
				final XMLNode xnode = childs.get(i);
				if(xnode == node){
					if(i==size-1){
						return null;
					} else {
						return childs.get(i+1);
					}
				}
			}
			
			for(XMLNode xnode:childs){
				final XMLNode next = xnode.findNextByNode(node);
				if(next!=null){
					return next;
				}
			}
		}
		
		return null;
	}
	
	public XMLNode findPrevByNode(final XMLNode node){
		if(childs.isEmpty()){
			return null;
		} else {
			final int size = childs.size();
			for(int i=0; i<size; i++){
				final XMLNode xnode = childs.get(i);
				if(xnode == node){
					if(i==0){
						return null;
					} else {
						return childs.get(i-1);
					}
				}
			}
			
			for(XMLNode xnode:childs){
				final XMLNode prev = xnode.findPrevByNode(node);
				if(prev!=null){
					return prev;
				}
			}
		}
		
		return null;
	}
	
	
//	public 
	
	public String toString(){
		String tab = "", ret = "";
		for(int i=0; i<mDepth; i++){
			tab += "\t";
		}
		
		ret += "\n"+tab+name;
		if(childs.isEmpty()){
			ret += ":"+value;
		} else {
			for(XMLNode node: childs){
				ret += node.toString();
			}
		}
		
		return ret;
	}
}
