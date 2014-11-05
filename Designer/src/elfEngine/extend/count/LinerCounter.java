package elfEngine.extend.count;


public final class LinerCounter implements IValueCounter{
	private float mPeriod;
	private float mProgress;
	private float mBeg, mEnd, mVar;
	
	public LinerCounter(float beg, float end, float period){
		this.mBeg = beg;
		this.mEnd = end;
		this.mPeriod = period;
		this.mVar = (mEnd-mBeg)/period;
	}
	
	public void set(float beg, float end, float period){
		this.mBeg = beg;
		this.mEnd = end;
		this.mPeriod = period;
		this.mVar = (mEnd-mBeg)/period;
		
		mProgress = 0;
	}
	
	@Override
	public float value() {
		// TODO Auto-generated method stub
		return mBeg+mProgress*mVar;
	}

	@Override
	public void count(float elapsed) {
		// TODO Auto-generated method stub
		mProgress+=elapsed;
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return mProgress>mPeriod;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		mProgress = 0;
	}
}
