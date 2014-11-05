package elfEngine.extend.count;

import elfEngine.basic.counter.Interpolator;
import elfEngine.graphics.PointF;

public final class ElfCounter implements ICounter{
	
	public enum Mode {
		LOOP, RELOOP, STAY;
		/**
		 * @param progress
		 * @param period
		 * @return[0,1)
		 */
		public final float rate(final float progress, final float period){
			
			switch(this){
			case LOOP:
				final float p = progress/period;
				return p-(int)p;
			case RELOOP:
				final float loopf = progress/period;
				final int loop = (int)(loopf);
				if((loop&1) == 0){
					return (loopf-loop);
				}else{
					return 1-(loopf-loop);
				}
			case STAY:
				if(progress >= period){
					return 0.9999999f;
				} else {
					return progress/period;
				}
			}
			return 0;
		}
	}
	
	public static class Inter{
		public static final Interpolator 
//		AcceDece = Inter,
//		Acce = new AccelerateInterpolator(),
//		Anticipate = new AnticipateInterpolator(),
//		AnticipateOvershoot = new AnticipateOvershootInterpolator(),
//		Bounce = new BounceInterpolator(),
//		Cycle = new CycleInterpolator(1),
//		Dece = new DecelerateInterpolator(),
//		Linear = new LinearInterpolator(),
//		Overshoot = new OvershootInterpolator(),
		Viscous = new Viscous();
		;
		
		public static class Viscous implements Interpolator{
			// This controls the viscous fluid effect (how much of it)
			private final float mViscousFluidScale = 8.0f;
			// must be set to 1.0 (used in viscousFluid())
			private float mViscousFluidNormalize = 1.0f;
			public Viscous(){
				mViscousFluidNormalize = 1.0f;
				mViscousFluidNormalize = 1.0f / getInterpolation(1.0f);
			}
			@Override
			public float getInterpolation(float x) {
				x *= mViscousFluidScale;
				if (x < 1.0f) {
					x -= (1.0f - (float)Math.exp(-x));
				} else {
					float start = 0.36787944117f;   // 1/e == exp(-1)
					x = 1.0f - (float)Math.exp(1.0f - x);
					x = start + x * (1.0f - start);
				}
				x *= mViscousFluidNormalize;
				return x;
			}
			@Override
			public float getInterpolation(float input, float rate) {
				// TODO Auto-generated method stub
				return 0;
			}
		}
	}
	
	
	private Mode mMode = Mode.LOOP;
	private int [] mArry;
	private float mPeriod, mProgress, mLastProgress, mLife;
	private float mBeg, mEnd;
	private PointF mStart, mStop;
	private Interpolator mInterpolator = Inter.Viscous;

	/**
	 * @param pStart
	 * @param pStop
	 * @param period
	 * @param life
	 * @param mode
	 * @param interpolator
	 */
	public ElfCounter(PointF pStart, PointF pStop, float period, float life, Mode mode, Interpolator interpolator){
		reset(pStart, pStop, period, life, mode, interpolator);
	}
	/**
	 * @param pStart
	 * @param pStop
	 * @param period
	 * @param life
	 */
	public ElfCounter(PointF pStart, PointF pStop, float period, float life){
		reset(pStart, pStop, period, life, mMode, mInterpolator);
	}
	/**
	 * @param arry
	 * @param period
	 * @param life
	 * @param NumberMode
	 */
	public ElfCounter (int [] arry, float period, float life, Mode mode, Interpolator interpolator){
		reset(arry,period,life,mode, interpolator);
	}
	/**
	 * @param arry
	 * @param period
	 * @param life
	 */
	public ElfCounter (int [] arry, float period, float life){
		reset(arry,period,life,mMode, mInterpolator);
	}
	/**
	 * @param beg
	 * @param end
	 * @param period
	 * @param life
	 * @param mode
	 * @[beg,end)
	 */
	public ElfCounter (float beg, float end, float period, float life, Mode mode, Interpolator interpolator){
		reset(beg,end,period,life,mode, interpolator);
	}
	/**
	 * @param beg
	 * @param end
	 * @param period
	 * @param life
	 */
	public ElfCounter (float beg, float end, float period, float life){
		reset(beg,end,period,life,mMode, mInterpolator);
	}
	
	/**
	 * @param beg
	 * @param end
	 * @param period
	 * @param life
	 * @param mode
	 * @[beg,end)
	 */
	public void reset(float beg, float end, float period, float life, Mode mode, Interpolator interpolator){
		this.mBeg = beg;
		this.mEnd = end;
		this.mPeriod = period;
		this.mLife = life;
		this.mMode = mode;
		this.mInterpolator = interpolator;
		//reset
		reset();
	}
	/**
	 * @param beg
	 * @param end
	 * @param period
	 * @param life
	 */
	public void reset(float beg, float end, float period, float life){
		this.mBeg = beg;
		this.mEnd = end;
		this.mPeriod = period;
		this.mLife = life;
		//
		reset();
	}
	
	/**
	 * @param arry
	 * @param period
	 * @param life
	 * @param exp
	 * @param NumberMode
	 */
	public void reset(int [] arry, float period, float life, Mode mode, Interpolator interpolator){
		this.mArry = arry;
		this.mPeriod = period;
		this.mLife = life;
		this.mMode = mode;
		
		this.mBeg = 0;
		this.mEnd = mArry.length;
		
		this.mInterpolator = interpolator;
		reset();
	}
	/**
	 * @param arry
	 * @param period
	 * @param life
	 */
	public void reset(int [] arry, float period, float life){
		this.mArry = arry;
		this.mPeriod = period;
		this.mLife = life;
		
		this.mBeg = 0;
		this.mEnd = mArry.length;
		//
		reset();
	}
	/**
	 * @param start
	 * @param stop
	 * @param period
	 * @param life
	 * @param exp
	 * @param NumberMode
	 */
	public void reset(PointF pStart, PointF pStop, float period, float life, Mode mode, Interpolator interpolator){
		this.mStart = pStart;
		this.mStop = pStop;
		this.mPeriod = period;
		this.mLife = life;
		this.mMode = mode;
		
		this.mBeg = mStart.x;
		this.mEnd = mStop.x;
		
		this.mInterpolator = interpolator;
		reset();
	}
	/**
	 * @param pStart
	 * @param pStop
	 * @param period
	 * @param life
	 */
	public void reset(PointF pStart, PointF pStop, float period, float life){
		this.mStart = pStart;
		this.mStop = pStop;
		this.mPeriod = period;
		this.mLife = life;
		
		this.mBeg = mStart.x;
		this.mEnd = mStop.x;
		
		reset();
	}
	
	public void reset(){
		this.mProgress = this.mLastProgress = 0;
	}

	@Override
	public void count(float pTime) {
		// TODO Auto-generated method stub
		this.mLastProgress = this.mProgress;
		this.mProgress += pTime;
	}
	@Override
	public int resid() {
		// TODO Auto-generated method stub
		int index = (int)(value());
		if(index >= this.mArry.length){
			index = this.mArry.length-1;
		} else if(index < 0){
			index = 0;
		}
		return this.mArry[index];
	}
	
	/* (non-Javadoc)
	 * @see elfEngine.extend.ICounter#value()
	 * @[beg, end)
	 */
	@Override
	public float value() {
		// TODO Auto-generated method stub
		return this.mBeg+rate()*(this.mEnd-this.mBeg);
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return this.mLife >= 0 && this.mProgress >= this.mLife;
	}
	
	public final float rate() {
		// TODO Auto-generated method stub
		return mInterpolator.getInterpolation(mMode.rate(mProgress, mPeriod));
	}
	
	private float lastRate(){
		return mInterpolator.getInterpolation(mMode.rate(mLastProgress, mPeriod));
	}

	public boolean isOver(int pLoop) {
		// TODO Auto-generated method stub
		return pLoop<=loop();
	}
	
	public int loop() {
		// TODO Auto-generated method stub
		return (int)(this.mProgress/this.mPeriod);
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return this.mStart.x+rate()*(this.mStop.x-this.mStart.x);
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return this.mStart.y+rate()*(this.mStop.y-this.mStart.y);
	}
	
	public float transX(){
		return (rate()-lastRate())*(this.mStop.x-this.mStart.x);
	}
	
	public float transY(){
		return (rate()-lastRate())*(this.mStop.y-this.mStart.y);
	}
	
	public float elapsed(){
		return this.mProgress;
	}
	
	public float life(){
		return this.mLife;
	}
	public void setLife(float life){
		mLife = life;
	}
	
	public float period(){
		return this.mPeriod;
	}
	
	public void setPeriod(float period){
		mPeriod = period;
	}
	public Mode mode(){
		return this.mMode;
	}
	public void setMode(Mode mode){
		this.mMode = mode;
	}
	
	public Interpolator interpolator(){
		return this.mInterpolator;
	}
	public void setInterpolator(Interpolator inter){
		mInterpolator = inter;
	}
	
}