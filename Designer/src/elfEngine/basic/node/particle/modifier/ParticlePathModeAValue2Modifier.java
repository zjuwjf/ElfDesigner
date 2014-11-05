package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.node.particle.IValue;

public class ParticlePathModeAValue2Modifier extends ParticlePathModeAModifier{
	
	protected IValue mRadialAccelValue2;
	protected IValue mTangentialAccelValue2;
	protected IValue mGravityXValue2;
	protected IValue mGravityYValue2;
	protected IValue mAngle;
	protected IValue mVelocity;
	
	/**
	 * @param radialAccel
	 * @param tangentialAccel
	 * @param gravityX
	 * @param gravityY
	 * @param angle
	 * @param velocity
	 */
	public ParticlePathModeAValue2Modifier(IValue radialAccel,
			IValue tangentialAccel, IValue gravityX, IValue gravityY,
			IValue angle, IValue velocity) {
		super(radialAccel.getValue(), tangentialAccel.getValue(), 
				gravityX.getValue(), gravityY.getValue(), 
				angle.getValue(), velocity.getValue());
		
		mRadialAccelValue2 = radialAccel;
		mTangentialAccelValue2 = tangentialAccel;
		mGravityXValue2 = gravityX;
		mGravityYValue2 = gravityY;
		mAngle = angle;
		mVelocity = velocity;
	}

	public void reDress(){
		super.reDress();
		set(mRadialAccelValue2.getValue(), mTangentialAccelValue2.getValue(), 
				mGravityXValue2.getValue(), mGravityYValue2.getValue(), 
				mAngle.getValue(), mVelocity.getValue());
	}
	
	@Override
	public IParticleModifier newInstance() {
		return new ParticlePathModeAValue2Modifier(mRadialAccelValue2,mTangentialAccelValue2,mGravityXValue2,mGravityYValue2,mAngle,mVelocity);
	}
}
