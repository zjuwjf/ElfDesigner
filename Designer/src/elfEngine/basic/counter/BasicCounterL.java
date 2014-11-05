package elfEngine.basic.counter;

public class BasicCounterL {
	protected long mProgress = 0;
	protected long mLife = -1;
	private boolean mIsRemovable = false;
	
	public void count(float pMsElapsed) {
		mProgress += pMsElapsed;
		
		//end
		if(mLife >= 0 && mProgress > mLife) { 
			mProgress = mLife; 
		} 
	}
	
	public final long getLife() {
		return mLife;
	}
	
	public final long getOverProgressed() {
		if(mLife < 0){
			return Long.MIN_VALUE;
		}
		return mProgress-mLife;
	}

	public final long getProgress() {
		return mProgress;
	}

	public boolean isDead() {
		return mLife >=0 && mProgress >= mLife;
	}

	public void reset() {
		mProgress = 0;
	}

	public void setLife(long life) {
		mLife = life;
	}

	public void setProgress(long progress) { 
		mProgress = progress;
		
		if(mLife >= 0 && mProgress > mLife) { 
			mProgress = mLife; 
		} 
	}
	
	public final boolean isRemovable() {
		return mIsRemovable;
	}
	
	public final void setRemovable(boolean isRemovable) {
		mIsRemovable = isRemovable;
	}
}
