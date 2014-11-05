package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.counter.BasicCounter;
import elfEngine.basic.node.ElfNode;
import elfEngine.extend.ElfConfig;
import elfEngine.graphics.PointF;

public class ParticlePathModeAModifier extends BasicCounter implements IParticleModifier, ElfConfig{

	/**
	 * CCPoint tmp, radial, tangential;

				radial = CCPointZero;
				// radial acceleration
				if(p->pos.x || p->pos.y)
					radial = ccpNormalize(p->pos);
				tangential = radial;
				radial = ccpMult(radial, p->modeA.radialAccel);

				// tangential acceleration
				float newy = tangential.x;
				tangential.x = -tangential.y;
				tangential.y = newy;
				tangential = ccpMult(tangential, p->modeA.tangentialAccel);
				
				// (gravity + radial + tangential) * dt
				tmp = ccpAdd( ccpAdd( radial, tangential), modeA.gravity);
				tmp = ccpMult( tmp, dt);
				p->modeA.dir = ccpAdd( p->modeA.dir, tmp);
				tmp = ccpMult(p->modeA.dir, dt);
				p->pos = ccpAdd( p->pos, tmp );
	 */
	protected static final PointF mTmp = new PointF();
	protected static final PointF mRadial = new PointF();
	protected static final PointF mTangential = new PointF();
	
	protected float mAngle, mVelocity;
	protected float mRadialAccel, mTangentialAccel, mGravityX, mGravityY, mVelocityX, mVelocityY;
	protected float mLastProgress = 0;
	
	protected float mOrginOriginX, mOrginOriginY;
	
	private float mTmpVelocityX=0, mTmpVelocityY=0;
	
	public ParticlePathModeAModifier(float radialAccel, float tangentialAccel, 
			float gravityX, float gravityY, 
			float angle, float velocity){
		
		mRadialAccel = radialAccel;
		mTangentialAccel = tangentialAccel;
		mGravityX = gravityX;
		mGravityY = gravityY;
		mAngle = angle;
		mVelocity = velocity;
		
		mVelocityX = MathHelper.cos(mAngle)*mVelocity;
		mVelocityY = MathHelper.sin(mAngle)*mVelocity;
		
		mTmpVelocityX = mVelocityX;
		mTmpVelocityY = mVelocityY;
	}
	
	public void set(float radialAccel, float tangentialAccel, 
			float gravityX, float gravityY, 
			float angle, float velocity){
		mRadialAccel = radialAccel;
		mTangentialAccel = tangentialAccel;
		mGravityX = gravityX;
		mGravityY = gravityY;
		
		mAngle = angle;
		mVelocity = velocity;
		
		mVelocityX = MathHelper.cos(mAngle)*mVelocity;
		mVelocityY = MathHelper.sin(mAngle)*mVelocity;
		
		mTmpVelocityX = mVelocityX;
		mTmpVelocityY = mVelocityY;
	}
	
	@Override
	public void modifier(ElfNode node) {
		final float x = node.getPosition().x - mOrginOriginX, 
		y = node.getPosition().y-mOrginOriginY;
		
		mRadial.setPoint(0, 0);
		if(x!=0 || y!= 0){
			final float l = MathHelper.sqrt(x*x+y*y);
			mRadial.setPoint(x/l, y/l);
		}
		mTangential.setPoint(mRadial);
		
		mRadial.x *= mRadialAccel;
		mRadial.y *= mRadialAccel;
		
		final float newY = mTangential.x;
		mTangential.x = -mTangential.y;
		mTangential.y = newY;
		
		mTangential.x *= mTangentialAccel;
		mTangential.y *= mTangentialAccel;
		
		final float gx = mGravityX+mRadial.x+mTangential.x;
		final float gy = mGravityY+mRadial.y+mTangential.y;
		
		final float span = mProgress-mLastProgress;
		
		final float dvx = gx*span;
		final float dvy = gy*span;
		
		mTmpVelocityX += dvx;
		mTmpVelocityY += dvy;
		
		final float dx = mTmpVelocityX*span;
		final float dy = mTmpVelocityY*span;
		
		node.translate(dx, dy);
		
		mLastProgress = mProgress;
	}
	
	@Override
	public IParticleModifier newInstance() {
		return new ParticlePathModeAModifier(mRadialAccel, mTangentialAccel, mGravityX, mGravityY, mVelocityX, mVelocityY);
	}
	
	@Override
	public void reDress() {	
		reset();
	}
	
	public void reset() {
		super.reset();
		mLastProgress = 0;
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
		mOrginOriginX = x;
		mOrginOriginY = y;
	}
}
