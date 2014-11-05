package elfEngine.basic.node.nodeAnimate.timeLine;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;

public class MotionCounter { 
	private int mLoopStart, mLoopEnd; 
	private int mProgress; 
	
	private int mInnerProgress;
	
	private InterType mInterType = InterType.Linear;
	private LoopMode mLoopMode = LoopMode.LOOP;
	
	public MotionCounter(int start, int end) {
		this.setLoopStart(start);
		this.setLoopEnd(end);
	}
	
	public void count(int add) { 
		mInnerProgress += add; 
		calcProgress(); 
	} 
	
	void calcProgress() { 
		final int period = mLoopEnd - mLoopStart;
		float innerRate = 0;
		//loop
		switch(mLoopMode) { 
		case RELOOP: 
			mInnerProgress = mInnerProgress%(2*period+1); 
			if(mInnerProgress > period) { 
				innerRate = 2-mInnerProgress/((float)period);
			} else { 
				innerRate = mInnerProgress/((float)period);
			} 
			break; 
		case LOOP: 
			mInnerProgress = mInnerProgress%(period+1); //[0, period]
			innerRate = mInnerProgress/((float)period);
			break;
		case STAY: 
			mInnerProgress = Math.min(mInnerProgress, period); 
			innerRate = (mInnerProgress)/((float)period);
			break;
		case ENDLESS: 
			innerRate = (mInnerProgress)/((float)period);
			break;
		} 
		innerRate = mInterType.getInterpolation(innerRate); 
		mProgress = mLoopStart + Math.round( (mLoopEnd-mLoopStart) * innerRate ); 
	} 
	
	public void setInterType(InterType type) {
		if(type != null) {
			mInterType = type;
		}
	} 
	public InterType getInterType() {
		return mInterType;
	}
	
	public void setLoopMode(LoopMode mode) {
		if(mode != null) {
			mLoopMode = mode;
		}
	}
	public LoopMode getLoopMode() {
		return mLoopMode;
	}
	
	public void setProgress(int progress) { 
		if(progress != mProgress) { 
			mProgress = progress; 
			final int oldProgress = mProgress;
			final int period = mLoopEnd - mLoopStart; 
			for(int i=0; i<=period; i++) { 
				mInnerProgress = i; 
				calcProgress(); 
				//find right inner progress 
				if(oldProgress == mProgress) { 
					break; 
				} 
			} 
			mProgress = oldProgress; 
		} 
	} 
	
	public int getProgress() {
		return mProgress;
	} 
	
	public void setLoopStart(int start) {
		if(start < mLoopEnd) {
			mLoopStart = start;
		}
	}
	public int getLoopStart() {
		return mLoopStart;
	}
	
	public void setLoopEnd(int end) {
		if(end > mLoopStart) {
			mLoopEnd = end;
		}
	}
	public int getLoopEnd() {
		return mLoopEnd;
	}
	
	public void reset() {
		mInnerProgress = mLoopStart;
		mProgress = mLoopStart;
	} 
}
