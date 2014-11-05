package com.ielfgame.stupidGame.data;

public class ElfEnum {
	
	protected String [] enums;
	protected String current;
	protected String name;
	
	public ElfEnum() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ElfEnum(String[] enums, String current) {
		super();
		this.enums = enums;
		this.current = current;
	}
	
	public String[] getEnums() {
		return enums;
	}
	
	public void setEnums(String[] enums) {
		this.enums = enums;
	}
	public String getCurrent() {
		return current;
	}
	
	public void setCurrent(String current) {
		this.current = current;
	} 
	
	public boolean isEnum(String current) {
		if(enums != null) {
			for(String e : enums) {
				if(e.equals(current)) {
					return true;
				} 
			} 
		} 
		return false; 
	} 
	
	public boolean isEnum() {
		return isEnum(this.current);
	}
}
