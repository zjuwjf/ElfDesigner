package elfEngine.basic.counter;

import elfEngine.basic.counter.InterHelper.InterType;

public class ElfCounterL extends BasicCounterL{
	private long mPeroid = 1;
	private long mBegValue = 0;
	private long mEndValue = 0;
	private float mReciprocal;
	
	private LoopMode mMode = LoopMode.STAY;
	private Interpolator mInter = InterType.Linear;
	
	public ElfCounterL(long beg, long end, long period, long life, LoopMode mode, Interpolator inter){
		mProgress = 0l;
		mBegValue = beg;
		mEndValue = end;
		mPeroid = period;
		mLife = life;
		
		setMode(mode);
		setInter(inter);
		
		resetAssist();
	} 
	
	public final long getPeroid() {
		return mPeroid;
	}

	public final void setPeroid(long mPeroid) {
		this.mPeroid = mPeroid;
		resetAssist();
	}

	public final long getBegValue() {
		return mBegValue;
	}

	public final void setBegValue(long mBegValue) {
		this.mBegValue = mBegValue;
		resetAssist();
	}

	public final long getEndValue() {
		return mEndValue;
	}

	public final void setEndValue(long mEndValue) {
		this.mEndValue = mEndValue;
		resetAssist();
	}

	public final LoopMode getMode() {
		return mMode;
	}

	public final void setMode(LoopMode mode) {
		if(mode != null) {
			this.mMode = mode;
		} else {
			this.mMode = LoopMode.STAY;
		}
	}

	public final Interpolator getInter() {
		return mInter;
	}

	public final void setInter(Interpolator mInter) {
		if(mInter != null) {
			this.mInter = mInter;
		} else {
			this.mInter = InterType.Linear;
		}
	}
	
	private final void resetAssist(){ 
		mReciprocal = 1f/mPeroid;
	}
	
	public long getValue(){ 
		final float p = mInter.getInterpolation( convert(mProgress*mReciprocal) );
		return (long) (mBegValue + (mEndValue-mBegValue)*p);
	} 
	
	public final float convert(float source){
		switch(mMode){
		case LOOP:
			if(mProgress % (2*mPeroid) != 0 && mProgress % mPeroid == 0) {
				return 1;
			}
			return source - (int)source;
		case RELOOP:
			final int integer = (int)source;
			if((integer & 1) == 0){
				return source - integer;
			} else {
				return 1 + integer - source;
			}
		case STAY:
			return source>1? 1: source;
		case ENDLESS:
			return source;
		}
		return 0;
	}
}
