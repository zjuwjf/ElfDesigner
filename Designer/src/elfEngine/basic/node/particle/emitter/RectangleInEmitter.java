package elfEngine.basic.node.particle.emitter;

import elfEngine.extend.ElfRandom;

public class RectangleInEmitter extends BasicRectangleEmitter{

	/**
	 * @param centreX
	 * @param centreY
	 * @param width
	 * @param height
	 */
	public RectangleInEmitter(float centreX, float centreY,
			float width, float height) {
		super( centreX, centreY, width, height);
	}

	@Override
	public void getPosition(float[] position) {
		position[0] = mCentreX+ElfRandom.nextFloat(-mWidth*0.5f, mWidth*0.5f);
		position[1] = mCentreY+ElfRandom.nextFloat(-mHeight*0.5f, mHeight*0.5f);
	}
	
	public RectangleInEmitter copy(){
		return new RectangleInEmitter(mCentreX,mCentreY,mWidth,mHeight);
	}
}
