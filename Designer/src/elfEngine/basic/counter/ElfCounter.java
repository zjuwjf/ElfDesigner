package elfEngine.basic.counter;


public class ElfCounter extends BasicCounter implements IBasicCounter{
	private float mPeroid = 1;
	private float mBegValue = 0;
	private float mEndValue = 0;
	private float mAssist = 0;
	private float mReciprocal;
	
	private LoopMode mMode = null;
	private Interpolator mInter = null;
	
	public ElfCounter(float beg, float end, float period, float life, LoopMode mode, Interpolator inter){
		mProgress = 0;
		mBegValue = beg;
		mEndValue = end;
		mPeroid = period;
		mLife = life;
		mMode = mode;
		mInter = inter;
		
		resetAssist();
	}
	
	public final float getPeroid() {
		return mPeroid;
	}

	public final void setPeroid(float mPeroid) {
		this.mPeroid = mPeroid;
		resetAssist();
	}

	public final float getBegValue() {
		return mBegValue;
	}

	public final void setBegValue(float mBegValue) {
		this.mBegValue = mBegValue;
		resetAssist();
	}

	public final float getEndValue() {
		return mEndValue;
	}

	public final void setEndValue(float mEndValue) {
		this.mEndValue = mEndValue;
		resetAssist();
	}

	public final LoopMode getMode() {
		return mMode;
	}

	public final void setMode(LoopMode mMode) {
		this.mMode = mMode;
	}

	public final Interpolator getInter() {
		return mInter;
	}

	public final void setInter(Interpolator mInter) {
		this.mInter = mInter;
	}
	
	private final void resetAssist(){
		mReciprocal = 1f/mPeroid;
		mAssist = (mEndValue-mBegValue)*mReciprocal;
	}
	
	public final float getValue(){
		if(mInter == null){
			if(mProgress<mPeroid){
				return mBegValue + mProgress*mAssist;
			} else {
				if(mMode == LoopMode.LOOP || mMode == LoopMode.RELOOP){
					final float p = mMode.convert(mProgress*mReciprocal);
					return mBegValue + (mEndValue-mBegValue)*p;
				} else{//stay or null
					return mEndValue;
				}
			}
		} else {
			if(mMode == LoopMode.LOOP || mMode == LoopMode.RELOOP){
				final float p = mInter.getInterpolation( mMode.convert(mProgress*mReciprocal) );
				return mBegValue + (mEndValue-mBegValue)*p;
			} else {
				final float p;
				if(mProgress<mPeroid){
					p = mInter.getInterpolation( mProgress*mReciprocal );
				} else {
					p = mInter.getInterpolation( 1 );
				}
				return mBegValue + (mEndValue-mBegValue)*p;
			}
		}
	}
}
