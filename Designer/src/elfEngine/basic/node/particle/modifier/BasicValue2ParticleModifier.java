package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.node.particle.IValue;

public class BasicValue2ParticleModifier extends BasicParticleModifier implements IParticleModifier{
	private final IValue mAlpha1, mRed1, mGreen1, mBlue1;
	private final IValue mAlpha2, mRed2, mGreen2, mBlue2;
	private final IValue mScale1, mRotate1;
	private final IValue mScale2, mRotate2;
	private final IValue mLife;
	
	public BasicValue2ParticleModifier(
			IValue red1, IValue red2, IValue green1, IValue green2, IValue blue1,IValue blue2, IValue alpha1, IValue alpha2,
			IValue scale1,IValue scale2, IValue rotate1, IValue rotate2, IValue life
			) {
		super(red1.getValue(), red2.getValue(), green1.getValue(), green2.getValue(), blue1.getValue(), 
				blue2.getValue(), alpha1.getValue(),
				alpha2.getValue(), scale1.getValue(), scale2.getValue(), rotate1.getValue(), rotate2.getValue(), life.getValue());
		
		this.mAlpha1 = alpha1;
		this.mRed1 = red1;
		this.mGreen1 = green1;
		this.mBlue1 = blue1;
		this.mAlpha2 = alpha2;
		this.mRed2 = red2;
		this.mGreen2 = green2;
		this.mBlue2 = blue2;
		this.mScale1 = scale1;
		this.mRotate1 = rotate1;
		this.mScale2 = scale2;
		this.mRotate2 = rotate2;
		this.mLife = life;
	}

	@Override
	public BasicValue2ParticleModifier newInstance() {
		// TODO Auto-generated method stub
		return new BasicValue2ParticleModifier(mRed1,mRed2,mGreen1,mGreen2,mBlue1,mBlue2,mAlpha1,mAlpha2,mScale1,mScale2,mRotate1,mRotate2,mLife);
	}

	@Override
	public void reDress() {
		reset();
		set(mRed1.getValue(),mRed2.getValue(),
				mGreen1.getValue(),mGreen2.getValue(),
				mBlue1.getValue(),mBlue2.getValue(),
				mAlpha1.getValue(),mAlpha2.getValue(),
				mScale1.getValue(),mScale2.getValue(),
				mRotate1.getValue(),mRotate2.getValue(),
				mLife.getValue());
	}

	@Override
	public void setOriginPosition(float x, float y) {
	}
}
