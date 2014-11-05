package elfEngine.basic.node.particle.emitter;

//import elfEngine.basic.ElfConfig;


public abstract class BasicParticleEmitter implements IParticleEmitter{
	
	protected float mCentreX, mCentreY;
//	protected float mEmitVelocity;// per ms
//	protected float mRemainMsElpased;
//	protected boolean mIsPauseEmit = false;
	/**
	 * @param emitV :per ms
	 * @param centreX
	 * @param centreY
	 */
	public BasicParticleEmitter(float centreX, float centreY){
		mCentreX = centreX;
		mCentreY = centreY;
	}
	
	public float getCentreX() {
		return mCentreX;
	}

	public void setCentreX(final float mCentreX) {
		this.mCentreX = mCentreX;
	}

	public float getCentreY() {
		return mCentreY;
	}


	public void setCentreY(final float mCentreY) {
		this.mCentreY = mCentreY;
	}

	public void setCentre(final float x, final float y){
		mCentreX = x;
		mCentreY = y;
	}
	
	public void translate(final float x, final float y){
		mCentreX += x;
		mCentreY += y;
	}
	
	public void translateX(final float x){
		mCentreX += x;
	}
	
	public void translateY(final float y){
		mCentreY += y;
	}

//	/**
//	 * @return per ms
//	 */
//	public float getEmitVelocity() {
//		return mEmitVelocity*SEC_TO_MS;
//	}
//	
//	
//	/**
//	 * @param mEmitVelocity per ms
//	 */
//	public void setEmitVelocity(float mEmitVelocity) {
//		this.mEmitVelocity = mEmitVelocity*MS_TO_SEC;
//	}
//
//
//	@Override
//	public int getEmitNum(float pMsElapsed) {
//		// TODO Auto-generated method stub
//		if(mIsPauseEmit){
//			return 0;
//		}
//		final float retF = mEmitVelocity*pMsElapsed;
//		final int retI = (int)retF;
//		mRemainMsElpased += retF-retI;
//		return retI;
//	}
//	
//	@Override
//	public void pauseEmit() {
//		// TODO Auto-generated method stub
//		mIsPauseEmit = true;
//	}
//
//	@Override
//	public void resumeEmit() {
//		// TODO Auto-generated method stub
//		mIsPauseEmit = false;
//	}
//	
//	public boolean isPaused(){
//		return mIsPauseEmit;
//	}
}
