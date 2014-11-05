package com.ielfgame.stupidGame.data;


public class ElfPointi implements IDataDisplay {
	public int x, y;
	
	public ElfPointi() {
		this(0, 0);
	}
	
	public ElfPointi(ElfPointi copy) {
		this(copy.x, copy.y);
	}
	
	public ElfPointi(int x, int y) {
		this.x = x;
		this.y = y;
	} 
	
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public String toTitle() {
		return "Point";
	} 

	public String[] toLabels() {
		return new String[]{"x","y"};
	} 

	public String[] toValues() {
		return new String[]{""+x,""+y};
	} 
	
	public int fromValues(String[] values) {
		if(values == null) {
			return 0;
		}
		try {
			x = Integer.valueOf( values[0] );
		} catch (Exception e) {
			return 0;
		} 
		
		try {
			y = Integer.valueOf( values[1] );
		} catch (Exception e) { 
			return 1;
		} 
		
		return -1;
	}

	public void set(ElfPointi elfPoint) {
		this.x = elfPoint.x;
		this.y = elfPoint.y;
	} 
}
