package elfEngine.extend.count;

public class ParabolaCounter implements ICoordinateCounter{
	
	private float mBegX, mBegY;
	private float mVX, mVY;
	private float mGX, mGY;
	
	private float mMSElapsed;
	private float mMSLife = -1;
	
	/**
	 * @param begX
	 * @param begY
	 * @param vX
	 * @param vY
	 * @param gX
	 * @param gY
	 * @param sLife
	 */
	public ParabolaCounter(float begX, float begY, float vX, float vY, float gX, float gY, float sLife){
		this.mBegX = begX;
		this.mBegY = begY;
		this.mVX = vX*0.001f;
		this.mVY = vY*0.001f;
		this.mGX = gX*0.000001f;
		this.mGY = gY*0.000001f;
		this.mMSLife = sLife*1000;
	}
	
	/**
	 * @param begX
	 * @param begY
	 * @param vX
	 * @param vY
	 * @param gX
	 * @param gY
	 * @param sLife
	 */
	public void set(float begX, float begY, float vX, float vY, float gX, float gY, float sLife){
		this.mBegX = begX;
		this.mBegY = begY;
		this.mVX = vX*0.001f;
		this.mVY = vY*0.001f;
		this.mGX = gX*0.000001f;
		this.mGY = gY*0.000001f;
		this.mMSLife = sLife*1000;
	}
	
	@Override
	public void count(float msElapsed) {
		// TODO Auto-generated method stub
		mMSElapsed += msElapsed;
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return mMSLife >= 0 && mMSElapsed > mMSLife;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return mBegX+mVX*mMSElapsed+0.5f*mGX*mMSElapsed*mMSElapsed;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return mBegY+mVY*mMSElapsed+0.5f*mGY*mMSElapsed*mMSElapsed;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		mMSElapsed = 0;
	}
}
