package elfEngine.basic.node.particle.emitter;

public abstract class BasicCircleEmitter extends BasicParticleEmitter{
	protected float mRadiusX, mRadiusY;
	
	public BasicCircleEmitter(float centreX, float centreY, float radiusX, float radiusY) {
		super(centreX, centreY);
		mRadiusX = radiusX;
		mRadiusY = radiusY;
	}
	
	public BasicCircleEmitter( float centreX, float centreY, float radius) {
		this(centreX,centreY,radius,radius);
	}
	
	public float getRadiusX() {
		return mRadiusX;
	}

	public void setRadiusX(float mRadiusX) {
		this.mRadiusX = mRadiusX;
	}

	public float getRadiusY() {
		return mRadiusY;
	}

	public void setRadiusY(float mRadiusY) {
		this.mRadiusY = mRadiusY;
	}

}
