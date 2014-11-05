package elfEngine.basic.node.particle.emitter;

public abstract class BasicRectangleEmitter extends BasicParticleEmitter{
	protected float mWidth, mHeight;
	
	public BasicRectangleEmitter(float centreX, float centreY, float width, float height) {
		super(centreX, centreY);
		mWidth = width;
		mHeight = height;
	}
	
	public float getWidth() {
		return mWidth;
	}

	public void setWidth(float width) {
		this.mWidth = width;
	}

	public float getHeight() {
		return mHeight;
	}

	public void setHeight(float height) {
		this.mHeight = height;
	}
}
