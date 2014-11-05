package elfEngine.basic.modifier;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class ClockShake extends BasicNodeModifier{
	
	private final float mMaxRotated;
	private int mLooped=0;
	private float mCurrRotated;
	private float mRate;
	private float mTotalrotated = 0;
	
	public ClockShake(final float rotate, float period, final float rate, boolean isRemoveable) {
		super(0, 1, period, -1, LoopMode.RELOOP, InterType.Dece);
		setRemovable(isRemoveable);
		mMaxRotated = rotate;
		mRate = rate;
		mCurrRotated = mMaxRotated;
	}
	
	public void modifier(ElfNode node) {
		final int loop = (int)(getProgress()/getPeroid());
		if(loop > mLooped && (loop&1)==0){
			mCurrRotated *= mRate;
			if(Math.abs( mCurrRotated ) <0.1f){
				setLife(getProgress());
			}
		}
		
		final int mode = loop%4;
		if(mode==0||mode==1){
			final float rotate = getValue()*mCurrRotated;
			node.transRotate(rotate-mTotalrotated);
			mTotalrotated = rotate;
		} else {
			final float rotate = -getValue()*mCurrRotated;
			node.transRotate(rotate-mTotalrotated);
			mTotalrotated = rotate;
		}
		
		mLooped = loop;
	}
	
	public void onModifierFinished(ElfNode node) {
		super.onModifierFinished(node);
		node.transRotate(-mTotalrotated);
		mTotalrotated = 0;
	}

	
	public void reset(){
		super.reset();
		setLife(-1);
		mLooped=0;
		mTotalrotated = 0;
		mCurrRotated = mMaxRotated;
	}
	
}
