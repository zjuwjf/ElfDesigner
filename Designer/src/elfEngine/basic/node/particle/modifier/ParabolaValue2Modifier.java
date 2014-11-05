package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.node.particle.IValue;

public class ParabolaValue2Modifier extends ParabolaModifier{
	private IValue mVX, mVY, mGX, mGY, mSLife;
	
	public ParabolaValue2Modifier(IValue vX, IValue vY, IValue gX, IValue gY,
			IValue sLife) {
		super(vX.getValue(), vY.getValue(), gX.getValue(), gY.getValue(), sLife.getValue());
		mVX = vX;
		mVY = vY;
		mGX = gX;
		mGY = gY;
		mSLife = sLife;
	}

	public void reDress(){
		reset();
		set(mVX.getValue(), mVY.getValue(), mGX.getValue(), mGY.getValue(), mSLife.getValue());
	}
	
	@Override
	public IParticleModifier newInstance() {
		return new ParabolaValue2Modifier(mVX, mVY, mGX, mGY, mSLife);
	}
}
