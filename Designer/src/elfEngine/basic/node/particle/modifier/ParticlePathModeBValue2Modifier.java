package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.node.particle.IValue;

public class ParticlePathModeBValue2Modifier extends ParticlePathModeBModifier{

	protected IValue mRradiusMin, mDeltaRadius, mDegPerMs;
	
	public ParticlePathModeBValue2Modifier(IValue radiusMin, IValue deltaRadius,
			IValue degPerMs) {
		super(radiusMin.getValue(), deltaRadius.getValue(), degPerMs.getValue());
		mRradiusMin = radiusMin;
		mDeltaRadius = deltaRadius;
		mDegPerMs = degPerMs;
	}

	public void reDress(){
		super.reDress();
		set(mRradiusMin.getValue(), mDeltaRadius.getValue(), 
				mDegPerMs.getValue());
	}
	
	@Override
	public IParticleModifier newInstance() {
		return new ParticlePathModeBValue2Modifier(mRradiusMin,mDeltaRadius,mDegPerMs);
	}
}
