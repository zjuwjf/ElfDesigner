package com.ielfgame.stupidGame.data;


public class ElfColor implements IDataDisplay{
	
	public float red=1, green=1, blue=1, alpha=1;
	
	public ElfColor() {
		this(1,1,1,1);
	}
	public ElfColor(ElfColor color) {
		set(color);
	}
	
	public ElfColor(float r, float g, float b, float a) {
		this.set(r,g,b,a);
	}
	
	public String toTitle() {
		return "RGBA";
	}
	
	public String[] toLabels() { 
		final String [] ret = {"red","green","blue","alpha"};
		return ret;
	}

	public String[] toValues() {
		final String [] ret = {""+red, ""+green, ""+blue, ""+alpha};
		return ret;
	}

	public int fromValues(String[] values) {
		if(values == null) {
			return 0;
		}
		try {
			this.red = Float.valueOf(values[0]); 
		} catch (Exception e) {
			return 0;
		} 
		
		try {
			this.green = Float.valueOf(values[1]); 
		} catch (Exception e) {
			return 1;
		} 
		
		try {
			this.blue = Float.valueOf(values[2]); 
		} catch (Exception e) {
			return 2;
		} 
		
		try {
			this.alpha = Float.valueOf(values[3]); 
		} catch (Exception e) {
			return 4;
		} 
		
//		this.set(red, green, blue, alpha);
		
		this.red = red > 1 ? red/255 : red;
		this.green = green > 1 ? green/255 : green;
		this.blue = blue > 1 ? blue/255 : blue;
		this.alpha = alpha > 1 ? alpha/255 : alpha;
		
		return -1;
	}

	public void set(ElfColor color) {
		if(color != null) {
			this.set(color.red, color.green, color.blue, color.alpha);
		}
	} 
	
	public void set(float red, float green, float blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	} 
	
	public static ElfColor between(final ElfColor c0, final ElfColor c1, final float r) {
		final ElfColor ret = new ElfColor();
		final float v = 1-r;
		ret.set(c0.red*v+c1.red*r, c0.green*v+c1.green*r, c0.blue*v+c1.blue*r, c0.alpha*v+c1.alpha*r);
		return ret;
	}
} 
