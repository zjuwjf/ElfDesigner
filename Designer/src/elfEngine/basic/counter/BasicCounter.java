package elfEngine.basic.counter;

import elfEngine.basic.modifier.IModifierRemovable;


public abstract class BasicCounter implements IBasicCounter, IModifierRemovable{
	protected float mProgress = 0;
	protected float mLife = -1;
	private boolean mIsRemovable = false;
	
	@Override
	public void count(float pMsElapsed) {
		mProgress += pMsElapsed;
	}
	
	@Override
	public final float getLife() {
		return mLife;
	}
	
	@Override
	public final float getOverProgressed() {
		if(mLife < 0){
			return Float.NEGATIVE_INFINITY;
		}
		return mProgress-mLife;
	}

	@Override
	public final float getProgress() {
		return mProgress;
	}

	@Override
	public boolean isDead() {
		return mLife >=0 && mProgress >= mLife;
	}

	@Override
	public void reset() {
		mProgress = 0;
	}

	@Override
	public void setLife(float life) {
		mLife = life;
	}

	@Override
	public void setProgress(float progress) {
		mProgress = progress;
	}
	
	@Override
	public final boolean isRemovable() {
		return mIsRemovable;
	}
	
	@Override
	public final void setRemovable(boolean isRemovable) {
		mIsRemovable = isRemovable;
	}
}
