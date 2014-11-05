package com.ielfgame.stupidGame;

import com.ielfgame.stupidGame.animation.Animate;
import com.ielfgame.stupidGame.animation.AnimateFrame;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.modifier.BasicNodeModifier;
import elfEngine.basic.node.ElfNode;

public class AnimateModifier extends BasicNodeModifier{
	
	private final Animate mAnimate;
	private AnimateFrame mCurrentFrame;
	
	public AnimateModifier(Animate animate, float life, LoopMode mode, Interpolator inter) {
		super(0, 1, 0, life, mode, inter);
		mAnimate = animate;
		setPeroid(mAnimate.getAnimateTime() * 1000);
	} 
	
	public void modifier(ElfNode node) {
		final AnimateFrame frame = mAnimate.getAnimateFrame(getValue());
		
		if(mCurrentFrame != frame){ 
			if(mCurrentFrame != null){ 
//				node.translate(-mCurrentFrame.mOffX, -mCurrentFrame.mOffY); 
			} else { 
				node.setBlendMode(mAnimate.mBlendMode);
				node.translate(mAnimate.mGlobelOffX, mAnimate.mGlobelOffY);
			}
			
			mCurrentFrame = frame;
//			node.translate(mCurrentFrame.mOffX, mCurrentFrame.mOffY);
//			node.setResid(mCurrentFrame.mResid);
		}
	}
}
