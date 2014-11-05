package com.ielfgame.stupidGame.texturePacker.pvr;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:55:02 - 08.03.2010
 */
public enum PixelFormat {
	// ===========================================================
	// Elements
	// ===========================================================

	UNDEFINED,
	RGBA_4444,
	RGBA_5551,
	RGBA_8888,
	RGB_565,
	A_8,
	I_8,
	AI_88;

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mGLInternalFormat;
	private int mGLFormat;
	private int mGLType;
	private int mBitsPerPixel;

	// ===========================================================
	// Constructors
	// ===========================================================

	private PixelFormat() {
//		this.mGLInternalFormat = pGLInternalFormat;
//		this.mGLFormat= pGLFormat;
//		this.mGLType = pGLType;
//		this.mBitsPerPixel = pBitsPerPixel;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getGLInternalFormat() {
		return this.mGLInternalFormat;
	}

	public int getGLFormat() {
		return this.mGLFormat;
	}

	public int getGLType() {
		return this.mGLType;
	}

	public int getBitsPerPixel() {
		return this.mBitsPerPixel;
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}