package com.ielfgame.stupidGame.data;

import elfEngine.graphics.PointF;

public class ElfPointf extends PointF{
	
	public ElfPointf() {
	}
	
	public ElfPointf(ElfPointf elfPointf) {
		if(elfPointf != null) {
			this.x = elfPointf.x;
			this.y = elfPointf.y;
		}
	}
	
	public ElfPointf(float x, float y) { 
		this.x = x;
		this.y = y;
	} 
	
	public ElfPointf translate(float dx, float dy) {
		this.x += dx;
		this.y += dy;
		
		return this;
	}

	public void set(ElfPointf elfPoint) {
		if(elfPoint != null) {
			this.x = elfPoint.x;
			this.y = elfPoint.y;
		}
	} 
	
	public ElfPointf ccpNeg() {
		return new ElfPointf(-this.x, -this.y);
	}
	
	public ElfPointf mult(float rate) {
		this.x *= rate;
		this.y *= rate;
		return this;
	}
	
	public ElfPointf mult(float x, float y) {
		this.x *= x;
		this.y *= y;
		
		return this;
	}
	
	public double getLength() {
		return Math.sqrt(x*x+y*y);
	}
	
	public static ElfPointf between(final ElfPointf p0, final ElfPointf p1, final float rate) {
		final ElfPointf ret = new ElfPointf();
		ret.setPoint(p0.x*(1-rate)+p1.x*rate, p0.y*(1-rate)+p1.y*rate);
		return ret;
	}
	
} 
