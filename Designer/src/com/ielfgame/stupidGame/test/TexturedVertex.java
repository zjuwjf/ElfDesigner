package com.ielfgame.stupidGame.test;

import elfEngine.basic.pool.ArrayPool;

public class TexturedVertex {
	// Vertex data
	private final float[] xyzw = new float[] {0f, 0f, 0f, 1f};
	private final float[] rgba = new float[] {1f, 1f, 1f, 1f};
	private final float[] st = new float[] {0f, 0f};
	
	// The amount of bytes an element has
	public static final int elementBytes = 4;
	
	// Elements per parameter
	public static final int positionElementCount = 4;
	public static final int colorElementCount = 4;
	public static final int textureElementCount = 2;
	
	// Bytes per parameter
	public static final int positionBytesCount = positionElementCount * elementBytes;
	public static final int colorByteCount = colorElementCount * elementBytes;
	public static final int textureByteCount = textureElementCount * elementBytes;
	
	// Byte offsets per parameter
	public static final int positionByteOffset = 0;
	public static final int colorByteOffset = positionByteOffset + positionBytesCount;
	public static final int textureByteOffset = colorByteOffset + colorByteCount;
	
	// The amount of elements that a vertex has
	public static final int elementCount = positionElementCount + 
			colorElementCount + textureElementCount;	
	// The size of a vertex in bytes, like in C/C++: sizeof(Vertex)
	public static final int stride = positionBytesCount + colorByteCount + 
			textureByteCount;
	
	// Setters
	public void setXYZ(float x, float y, float z) {
		this.setXYZW(x, y, z, 1f);
	}
	
	public void setRGB(float r, float g, float b) {
		this.setRGBA(r, g, b, 1f);
	}
	
	public void setST(float s, float t) {
		this.st[0] = s;
		this.st[1] = t;
	}
	
	public void setXYZW(float x, float y, float z, float w) {
		this.xyzw[0] = x;
		this.xyzw[1] = y;
		this.xyzw[2] = z;
		this.xyzw[3] = w;
	}
	
	public void setRGBA(float r, float g, float b, float a) {
		this.rgba[0] = r;
		this.rgba[1] = g;
		this.rgba[2] = b;
		this.rgba[3] = a;
	}
	
	// Getters	
	public float[] getElements() {
		float[] out = ArrayPool.float10;
		int i = 0;
		// Insert XYZW elements
		out[i++] = this.xyzw[0];
		out[i++] = this.xyzw[1];
		out[i++] = this.xyzw[2];
		out[i++] = this.xyzw[3];
		// Insert RGBA elements
		out[i++] = this.rgba[0];
		out[i++] = this.rgba[1];
		out[i++] = this.rgba[2];
		out[i++] = this.rgba[3];
		// Insert ST elements
		out[i++] = this.st[0];
		out[i++] = this.st[1];
		
		return out;
	}
	
	public float[] getXYZW() {
		return this.xyzw;
	}

	public float[] getXYZ() {
		return this.xyzw;
	}
	
	public float[] getRGBA() {
		return this.rgba;
	}
	
	public float[] getST() {
		return this.st;
	}
}
