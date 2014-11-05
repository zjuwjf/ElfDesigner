package com.ielfgame.stupidGame.animation;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;

public class AnimateFrame implements Cloneable{
	private float mFrameTimeRectify;
	
	private String mResid;
	
	private ElfPointf mOffset = new ElfPointf();
	private ElfPointf mScale = new ElfPointf();
	private float mRotate = 0f;
	private ElfColor mColor = new ElfColor();
	
	public BlendMode mBlendMode = BlendMode.BLEND;
	
	public ElfPointf getOffset() {
		return mOffset;
	}
	public void setOffset(ElfPointf mOffset) {
		this.mOffset.set(mOffset);
	}
	public ElfPointf getScale() {
		return mScale;
	}
	public void setScale(ElfPointf mScale) {
		this.mScale.set(mScale);
	}
	public float getRotate() {
		return mRotate;
	}
	public void setRotate(float mRotate) {
		this.mRotate = mRotate;
	}
	public ElfColor getColor() {
		return mColor;
	}
	public void setColor(ElfColor mColor) {
		this.mColor.set(mColor);
	}
	public String getResid() {
		return mResid;
	}
	public void setResid(String mResid) {
		this.mResid = mResid;
	} 

	public float getFrameTimeRectify() {
		return mFrameTimeRectify;
	}
	public void setFrameTimeRectify(float mFrameTimeRectify) {
		this.mFrameTimeRectify = mFrameTimeRectify;
	}

	public BlendMode getBlendMode() {
		return mBlendMode;
	}
	public void setBlendMode(BlendMode mBlendMode) {
		this.mBlendMode = mBlendMode;
	}

	public AnimateFrame(){ 
	} 
	
	public AnimateFrame(AnimateFrame animateFrame){
		this.mResid = animateFrame.mResid;
		this.mFrameTimeRectify = animateFrame.mFrameTimeRectify;
		this.mBlendMode = animateFrame.mBlendMode;
	}
	
	public void draw(int GLid){ 
		GLHelper.glBlendFunc(mBlendMode.sourceBlendFunction, mBlendMode.destinationBlendFunction);
		GLHelper.glPushMatrix();
		GLHelper.glTranslatef(mOffset.x, mOffset.y, 0);
		GLHelper.glTranslatef(mOffset.x, mOffset.y, 0);
		
		GLManage.draw(mResid, GLid);
		GLHelper.glPopMatrix();
	} 
	
	public void copyFrom(AnimateFrame copyFrom){
		this.mResid = copyFrom.mResid;
		this.mFrameTimeRectify = copyFrom.mFrameTimeRectify;
		this.mBlendMode = copyFrom.mBlendMode;
		
		this.setOffset(copyFrom.getOffset());
		this.setScale(copyFrom.getScale());
		this.setRotate(copyFrom.getRotate());
		this.setColor(copyFrom.getColor());
	} 
} 
