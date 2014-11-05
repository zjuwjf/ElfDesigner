package elfEngine.basic.node.particle.emitter;

import elfEngine.basic.math.MathCons;
import elfEngine.extend.ElfRandom;

public class CircleInEmitter extends BasicCircleEmitter{
	
	public CircleInEmitter(float centreX, float centreY, float radiusX, float radiusY) {
		super(centreX, centreY,radiusX,radiusY);
	}
	
	public CircleInEmitter(float centreX, float centreY, float radius) {
		super(centreX,centreY,radius,radius);
	}
	
	@Override
	public void getPosition(float[] position) {
		final float radian = ElfRandom.nextFloat()*MathCons._2PI;
		final float rate = ElfRandom.nextFloat();
		
		position[0] = mCentreX+mRadiusX*(float)Math.cos(radian)*rate;
		position[1] = mCentreY+mRadiusY*(float)Math.sin(radian)*rate;
	}
	
	public CircleInEmitter copy(){
		return new CircleInEmitter(mCentreX,mCentreY,mRadiusX,mRadiusY);
	}
}
