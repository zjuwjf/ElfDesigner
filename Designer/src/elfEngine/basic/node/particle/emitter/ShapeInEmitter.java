package elfEngine.basic.node.particle.emitter;

import elfEngine.extend.ElfRandom;
import elfEngine.graphics.IShape;
import elfEngine.graphics.Rectangle;

public class ShapeInEmitter extends BasicShapeEmitter{


	public ShapeInEmitter(IShape shape, float centreX,
			float centreY) {
		super(shape, centreX, centreY);
	}

	@Override
	public void getPosition(float[] position) {
		if(mShape != null){
			final Rectangle bounds = mShape.bounds(); 
			
			float x, y;
			do{
				x = ElfRandom.nextFloat(bounds.left, bounds.right);
				y = ElfRandom.nextFloat(bounds.bottom, bounds.top);
			}while( !mShape.contains(x, y) );
			position[0] = mCentreX+x;
			position[1] = mCentreY+y;
		} 
	}
	
	public ShapeInEmitter copy(){
		return new ShapeInEmitter(mShape, mCentreX, mCentreY);
	}
}
