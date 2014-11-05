package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.counter.BasicCounter;
import elfEngine.basic.node.ElfNode;
import elfEngine.extend.ElfConfig;

public class ParabolaModifier extends BasicCounter implements IParticleModifier, ElfConfig{
	
	protected float mVX, mVY;
	protected float mGX, mGY;
	
	protected final float [] mTransTmp = new float[2];
	
	/**
	 * @param begX
	 * @param begY
	 * @param vX
	 * @param vY
	 * @param gX
	 * @param gY
	 * @param sLife
	 */
	public ParabolaModifier( float vX, float vY, float gX, float gY, float sLife){
		this.mVX = vX*MS_TO_SEC;
		this.mVY = vY*MS_TO_SEC;
		this.mGX = gX*MS2_TO_SEC2;
		this.mGY = gY*MS2_TO_SEC2;
		
		this.mLife = sLife*SEC_TO_MS;
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
	public void set(final float vX, final float vY, final float gX, final float gY, final float sLife){
		this.mVX = vX*MS_TO_SEC;
		this.mVY = vY*MS_TO_SEC;
		this.mGX = gX*MS2_TO_SEC2;
		this.mGY = gY*MS2_TO_SEC2;
		
		this.mLife = sLife*SEC_TO_MS;
	}

	private final float getX() {
		return mVX*mProgress+0.5f*mGX*mProgress*mProgress;
	}

	private final float getY() {
		return mVY*mProgress+0.5f*mGY*mProgress*mProgress;
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
		return new ParabolaModifier(mVX, mVY, mGX, mGY, mLife*MS_TO_SEC);
	}
	
	@Override
	public void reDress() {
		reset();
	}

	@Override
	public void modifier(final ElfNode node) {
		final float x = getX()-mTransTmp[0];
		final float y = getY()-mTransTmp[1];
		node.translate(x, y);
		
		mTransTmp[0] += x;
		mTransTmp[1] += y;
	}

	private ModifierListener mModifierListener = null;
	public void setModifierListener(final ModifierListener modifierListener){
		mModifierListener = modifierListener;
	}

	@Override
	@Deprecated
	public float getValue() {
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
