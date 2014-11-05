package com.ielfgame.stupidGame.data;

public class ElfVertex3 extends ElfDataDisplay{
	public float x, y, z;
	
	public ElfVertex3() {
		this(0,0,0);
	}
	
	public ElfVertex3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public ElfVertex3(ElfVertex3 vertex3) {
		this(vertex3.x, vertex3.y, vertex3.z);
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(ElfVertex3 vertex3) {
		set(vertex3.x, vertex3.y, vertex3.z);
	}
	
	public ElfVertex3 reverse() {
		return new ElfVertex3(-x,-y,-z);
	}
} 
