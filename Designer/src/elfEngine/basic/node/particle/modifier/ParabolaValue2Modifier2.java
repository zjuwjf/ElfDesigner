package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.node.particle.IValue;

public class ParabolaValue2Modifier2 extends ParabolaModifier{
	private IValue mVelocity, mAngle, mGX, mGY, mSLife;
	
	public ParabolaValue2Modifier2(IValue velocity, IValue angle, IValue gX, IValue gY,
			IValue sLife) {
		super(0, 0, 0, 0, 0);
		mVelocity = velocity;
		mAngle = angle;
		mGX = gX;
		mGY = gY;
		mSLife = sLife;
		
		final float deg = mAngle.getValue();
		final float v = mVelocity.getValue();
		set(v*MathHelper.cos(deg), v*MathHelper.sin(deg), mGX.getValue(), mGY.getValue(), mSLife.getValue());
	}

	public void reDress(){
		reset();
		final float deg = mAngle.getValue();
		final float v = mVelocity.getValue();
		set(v*MathHelper.cos(deg), v*MathHelper.sin(deg), mGX.getValue(), mGY.getValue(), mSLife.getValue());
	}
	
	@Override
	public IParticleModifier newInstance() {
		return new ParabolaValue2Modifier2(mVelocity, mAngle, mGX, mGY, mSLife);
	}
}
