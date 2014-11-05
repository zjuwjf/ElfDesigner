package elfEngine.basic.node.particle.emitter;

import elfEngine.basic.math.MathCons;
import elfEngine.extend.ElfRandom;

public class CircleOutLineEmitter  extends BasicCircleEmitter{
	
	public CircleOutLineEmitter( float centreX, float centreY, float radiusX, float radiusY) {
		super(centreX, centreY,radiusX,radiusY);
		// TODO Auto-generated constructor stub
	}
	
	public CircleOutLineEmitter( float centreX, float centreY, float radius) {
		super(centreX,centreY,radius,radius);
	}
	
	@Override
	public void getPosition(float[] position) {
		// TODO Auto-generated method stub
		final float radian = ElfRandom.nextFloat()*MathCons._2PI;
		final float rate = ElfRandom.nextFloat();
		
		position[0] = mCentreX+mRadiusX*(float)Math.cos(radian)*rate;
		position[1] = mCentreY+mRadiusY*(float)Math.sin(radian)*rate;
	}
	
	public CircleOutLineEmitter copy(){
		return new CircleOutLineEmitter(mCentreX,mCentreY,mRadiusX,mRadiusY);
	}
}
