package com.ielfgame.stupidGame.animation;

import java.util.ArrayList;

import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLHelper;

public class Animate implements Cloneable{
	
	public float mGlobelFrameTime = 0.1f;
	public int mGlobelOffX, mGlobelOffY;
	public final ArrayList<AnimateFrame> mFrames = new ArrayList<AnimateFrame>();
	public String mName = "Animate -1";
	
	public BlendMode mBlendMode = BlendMode.ACTIVLE;
	
	public Animate(){ 
		
	} 
	
	public Animate(Animate animate){
		this.mGlobelFrameTime = animate.mGlobelFrameTime;
		this.mGlobelOffX = animate.mGlobelOffX;
		this.mGlobelOffY = animate.mGlobelOffY;
		this.mName = animate.mName;
		for(AnimateFrame frame:animate.mFrames){
			mFrames.add(frame);
		}
	} 
	/***
	 * 
	 * @param rate [0, 1]
	 * @return
	 */
	public AnimateFrame getAnimateFrame(final float rate){
		if(!mFrames.isEmpty()){
			final int size = mFrames.size();
			
			if(rate <= 0){
				return mFrames.get(0);
			} else if(rate >= 1){
				return mFrames.get(size - 1);
			} else {
				final float animateTime = getAnimateTime();
				float currenRate = 0;
				int index = 0;
				while(currenRate < rate && index < size) {
					currenRate += getFrameTime(index)/animateTime;
					index ++;
				}
				return mFrames.get(index - 1); 
			}
		}
		
		return null;
	}
	
	public float getAnimateTime(){
		float ret = 0;
		for(AnimateFrame frame:mFrames){
			ret += frame.getFrameTimeRectify() + mGlobelFrameTime;
		}
		return ret;
	}
	
	public final float getFrameTime(int frameIndex){
		return mFrames.get(frameIndex).getFrameTimeRectify() + mGlobelFrameTime;
	}
	
	private float mElapsedMsCount = 0;
	private AnimateFrame mCurrentFrame = null;
	
	public AnimateFrame getCurrentFrame(){
		return mCurrentFrame;
	}
	
	public void update(float t){
		mElapsedMsCount += t*0.001f;
		final float total = getAnimateTime();
		if(total > 0){
			float rate = mElapsedMsCount/total;
			while(rate > 1){
				rate -= 1;
			}
			mCurrentFrame = getAnimateFrame(rate);
		} else {
			mCurrentFrame = null;
		} 
	} 
	public void draw(int GLid){
		GLHelper.glBlendFunc(mBlendMode.sourceBlendFunction, mBlendMode.destinationBlendFunction);
		GLHelper.glPushMatrix();
		GLHelper.glTranslatef(mGlobelOffX, mGlobelOffY, 0);
		if(mCurrentFrame != null){
			mCurrentFrame.draw(GLid);
		}
		GLHelper.glPopMatrix();
	}
	
	public void clearElapsedTime(){
		mElapsedMsCount = 0;
	}
	
	public Animate copy(){
		try {
			return (Animate) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void copyFrom(Animate from){
		this.mFrames.clear();
		for(AnimateFrame frame:from.mFrames){ 
			final AnimateFrame newframe = new AnimateFrame();
			newframe.copyFrom(frame);
			this.mFrames.add(newframe);
		}
		this.mGlobelFrameTime = from.mGlobelFrameTime;
		this.mGlobelOffX = from.mGlobelOffX;
		this.mGlobelOffY = from.mGlobelOffY;
		this.mName = new String(from.mName);
	}
}
