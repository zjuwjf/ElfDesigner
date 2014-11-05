package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.counter.BasicCounter;
import elfEngine.basic.node.ElfNode;
import elfEngine.extend.ElfConfig;

public class ParticlePathModeBModifier  extends BasicCounter implements IParticleModifier, ElfConfig{
	/**
	 * // Update the angle and radius of the particle.
				p->modeB.angle += p->modeB.degreesPerSecond * dt;
				p->modeB.radius += p->modeB.deltaRadius * dt;

				p->pos.x = - cosf(p->modeB.angle) * p->modeB.radius;
				p->pos.y = - sinf(p->modeB.angle) * p->modeB.radius;
	 */
	protected float mRadiusMin, mDeltaRadius;//
	protected float mDegPerMs;
	protected float [] mTmp = new float[2];
	
	public ParticlePathModeBModifier(float radiusMin, float deltaRadius, float degPerSecond){
		mRadiusMin = radiusMin;
		mDeltaRadius = deltaRadius;
		mDegPerMs = degPerSecond;
	}
	
	public void set(float radiusMin, float deltaRadius, float degPerSecond){
		mRadiusMin = radiusMin;
		mDeltaRadius = deltaRadius;
		mDegPerMs = degPerSecond;
	}
	
	@Override
	public IParticleModifier newInstance() {
		return new ParticlePathModeBModifier(mRadiusMin, mDeltaRadius,mDegPerMs);
	}
	
	@Override
	public void modifier(ElfNode node) {
		final float deg = mDegPerMs*mProgress;
		final float radius = mRadiusMin+mDeltaRadius*mProgress;
		final float x = -radius*MathHelper.cos(deg);
		final float y = -radius*MathHelper.sin(deg);
		
		node.translate(x-mTmp[0], y-mTmp[1]);
		mTmp[0] = x;
		mTmp[1] = y;
	}
	
	@Override
	public void reDress() {	
		reset();
		mTmp[0] = mTmp[1] = 0;
		mIsFinished = false;
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
