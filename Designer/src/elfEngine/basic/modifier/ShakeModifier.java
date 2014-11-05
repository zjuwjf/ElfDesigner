package elfEngine.basic.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.utils.ElfRandom;

public class ShakeModifier extends BasicNodeModifier{
	
	private float mShakedX, mShakedY, mShakedR;
	
	private float mMaxOffsetX, mMaxOffsetY, mMaxOffsetR;
	private float mMinOffsetX, mMinOffsetY, mMinOffsetR;
	
	private float mLastValue = 0;
	
	public ShakeModifier(float offX, float offY, float offR, float period, float life, LoopMode mode, Interpolator inter) {
		super(0, 1, period, life, mode, inter);
		// TODO Auto-generated constructor stub
		mMaxOffsetX = offX;
		mMaxOffsetY = offY;
		mMaxOffsetR = offR;
		
		mMinOffsetX = offX*0.5f;
		mMinOffsetY = offY*0.5f;
		mMinOffsetR = offR*0.5f;
	}

	@Override
	public void modifier(ElfNode node) {
		// TODO Auto-generated method stub
		final float value = getValue();
		if(value < mLastValue){
			node.translate(-mShakedX,-mShakedY);
			node.transRotate(-mShakedR);
			mShakedX = mShakedY = mShakedR = 0;
			
		}else if(value>=0.5f&&mLastValue<0.5f){
			final float dx = ElfRandom.nextFloat(mMaxOffsetX, mMinOffsetX);
			final float dy = ElfRandom.nextFloat(mMaxOffsetY, mMinOffsetY);
			final float dr = ElfRandom.nextFloat(mMaxOffsetR, mMinOffsetR);
			
			node.translate(dx,dy);
			node.transRotate(dr);
			
			mShakedX += dx;
			mShakedY += dy;
			mShakedR += dr;
		} 
		mLastValue = value;
	}
	
	@Override
	public void onModifierFinished(ElfNode node) {
		// TODO Auto-generated method stub
		super.onModifierFinished(node);
		node.translate(-mShakedX, -mShakedY);
		node.transRotate(-mShakedR);
		mShakedX = mShakedY = mShakedR = mLastValue = 0;
	}

	public void reset(){
		super.reset();
		
		mShakedX = 0;
		mShakedY = 0;
		mShakedR = 0;
		mLastValue = 0;
	}
}
