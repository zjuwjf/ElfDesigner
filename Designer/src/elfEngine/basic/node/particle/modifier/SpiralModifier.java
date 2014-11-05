package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.counter.BasicCounter;
import elfEngine.basic.node.ElfNode;
import elfEngine.extend.ElfConfig;

public class SpiralModifier extends BasicCounter implements IParticleModifier, ElfConfig{
	
	private float mBegRadius, mBegArc;
	
	//cw
	private float mVR, mVT;
	private float mGR, mGT;
	
	protected final float [] mTransTmp = new float[2];
	
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
	public SpiralModifier(float begRadius, float begArc,
			float vR, float vT, float gR, float gT, float sLife){
		this.mBegRadius = begRadius;
		this.mBegArc = begArc;
		this.mVR = vR*MS_TO_SEC;
		this.mVT = vT*MS_TO_SEC;
		this.mGR = gR*MS2_TO_SEC2;
		this.mGT = gT*MS2_TO_SEC2;
		
		this.mLife = sLife*SEC_TO_MS;
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
	public void set(float begRadius, float begArc,
			float vR, float vT, float gR, float gT, float sLife){
		this.mBegRadius = begRadius;
		this.mBegArc = begArc;
		this.mVR = vR*MS_TO_SEC;
		this.mVT = vT*MS_TO_SEC;
		
		this.mGR = gR*MS2_TO_SEC2;
		this.mGT = gT*MS2_TO_SEC2;
		
		this.mLife = sLife*SEC_TO_MS;
	}


	@Override
	public void reset() {
		super.reset();
		mTransTmp[0] = mTransTmp[1] = 0;
		mIsFinished = false;
	}

	@Override
	public IParticleModifier newInstance() {
		// TODO Auto-generated method stub
		return new SpiralModifier(mBegRadius,mBegArc,mVR,mVT,mGR,mGT,mLife);
	}

	@Override
	public void reDress() {
		reset();
	}

	@Override
	public void modifier(final ElfNode node) {
		// TODO Auto-generated method stub
		final float r = mBegRadius+mVR*mProgress+0.5f*mGR*mProgress*mProgress;
		final float t = mBegArc + (mVT*mProgress+0.5f*mGT*mProgress*mProgress)/r;
		final float x = r*(float)Math.cos(t);
		final float y = r*(float)Math.sin(t);
		
		final float tx = x-mTransTmp[0];
		final float ty = y-mTransTmp[1];

		node.translate(tx, ty);
		
		mTransTmp[0] += tx;
		mTransTmp[1] += ty;
	}
	
	private ModifierListener mModifierListener = null;
	
	public void setModifierListener(final ModifierListener modifierListener){
		mModifierListener = modifierListener;
	}

	@Override
	@Deprecated
	public float getValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	protected boolean mIsFinished = false;
	
	public void onModifierFinished(ElfNode node){
		if(mModifierListener != null){
			mModifierListener.onFinished(node);
		}
	}
	
	public boolean isFinished(){
		return mIsFinished;
	}
	
	public void setFinished(){
		mIsFinished = true;
	}

	@Override
	public void setOriginPosition(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
}
