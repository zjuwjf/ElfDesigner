package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.node.particle.IValue;

public class SpiralValue2Modifier extends SpiralModifier{
	private IValue mBegRadius, mBegArc, mVR, mVT, mGR, mGT, mSLife;
	
	public SpiralValue2Modifier(IValue begRadius, IValue begArc, IValue vR,
			IValue vT, IValue gR, IValue gT, IValue sLife) {
		super(begRadius.getValue(), begArc.getValue(), vR.getValue(), 
				vT.getValue(), gR.getValue(), gT.getValue(), sLife.getValue());
		// TODO Auto-generated constructor stub
		mBegRadius = begRadius;
		mBegArc = begArc;
		mVR = vR;
		mVT = vT;
		mGR = gR;
		mGT = gT;
		mSLife = sLife;
	}
	
	public void reDress(){
		super.reDress();
		set(mBegRadius.getValue(), mBegArc.getValue(), mVR.getValue(), 
				mVT.getValue(), mGR.getValue(), mGT.getValue(), mSLife.getValue());
	}
	
	@Override
	public IParticleModifier newInstance() {
		return new SpiralValue2Modifier(mBegRadius,mBegArc,mVR,mVT,mGR,mGT,mSLife);
	}
}
