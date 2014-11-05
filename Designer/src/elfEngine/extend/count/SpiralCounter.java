package elfEngine.extend.count;

public final class SpiralCounter implements ICoordinateCounter{
	
	private float mCentreX, mCentreY;
	private float mBegRadius, mBegArc;
	
	//cw
	private float mVR, mVT;
	private float mGR, mGT;
	
	private float mSElapsed;
	private float mSLife = -1;
	
	/**
	 * @param centreX
	 * @param centreY
	 * @param begRadius
	 * @param begArc
	 * @param vR
	 * @param vT
	 * @param gR
	 * @param gT
	 * @param sLife
	 * @ccw
	 */
	public SpiralCounter(float centreX, float centreY, float begRadius, float begArc,
			float vR, float vT, float gR, float gT, float sLife){
		this.mCentreX = centreX;
		this.mCentreY = centreY;
		this.mBegRadius = begRadius;
		this.mBegArc = begArc;
		this.mVR = vR;
		this.mVT = vT;
		this.mGR = gR;
		this.mGT = gT;
		this.mSLife = sLife;
	}

	/**
	 * @param centreX
	 * @param centreY
	 * @param begRadius
	 * @param begArc
	 * @param vR
	 * @param vT
	 * @param gR
	 * @param gT
	 * @param sLife
	 */
	public void set(float centreX, float centreY, float begRadius, float begArc,
			float vR, float vT, float gR, float gT, float sLife){
		this.mCentreX = centreX;
		this.mCentreY = centreY;
		this.mBegRadius = begRadius;
		this.mBegArc = begArc;
		this.mVR = vR;
		this.mVT = vT;
		this.mGR = gR;
		this.mGT = gT;
		this.mSLife = sLife;
	}
	
	@Override
	public void count(float elapsed) {
		// TODO Auto-generated method stub
		mSElapsed += elapsed*0.001f;
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return mSLife > 0 && mSElapsed > mSLife;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		mSElapsed = 0;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		final float r = mBegRadius+mVR*mSElapsed+0.5f*mGR*mSElapsed*mSElapsed;
		final float t = mBegArc + (mVT*mSElapsed+0.5f*mGT*mSElapsed*mSElapsed)/r;
		return mCentreX+r*(float)Math.cos(t);
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		final float r = mBegRadius+mVR*mSElapsed+0.5f*mGR*mSElapsed*mSElapsed;
		final float t = mBegArc + (mVT*mSElapsed+0.5f*mGT*mSElapsed*mSElapsed)/r;
		return mCentreY+r*(float)Math.sin(t);
	}
}
