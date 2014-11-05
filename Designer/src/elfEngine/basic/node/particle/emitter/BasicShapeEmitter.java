package elfEngine.basic.node.particle.emitter;

import elfEngine.graphics.IShape;

public abstract class BasicShapeEmitter extends BasicParticleEmitter{
	protected IShape mShape; 
	
	public BasicShapeEmitter(IShape shape, float centreX, float centreY) {
		super(centreX, centreY);
		mShape = shape;
	}

	public void setShape(final IShape shape){
		mShape = shape;
	}
	
	public IShape getShape(){
		return mShape;
	} 
}
