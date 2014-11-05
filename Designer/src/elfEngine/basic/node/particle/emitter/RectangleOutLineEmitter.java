package elfEngine.basic.node.particle.emitter;

import elfEngine.extend.ElfRandom;

public class RectangleOutLineEmitter extends BasicRectangleEmitter{

	public RectangleOutLineEmitter(float centreX, float centreY,
			float width, float height) {
		super(centreX, centreY, width, height);
	}

	@Override
	public void getPosition(float[] position) {
		final float rate = (mWidth + mHeight)*ElfRandom.nextFloat();
		if(rate<mWidth/(mWidth+mHeight)){
			position[0] = mCentreX+ElfRandom.nextFloat(-mWidth*0.5f, mWidth*0.5f);
			position[1] = mCentreY+ElfRandom.nextFloat()>0.5f? mHeight*0.5f : -mHeight*0.5f;
		} else {
			position[0] = mCentreX+ElfRandom.nextFloat()>0.5f? mWidth*0.5f : -mWidth*0.5f;
			position[1] = mCentreY+ElfRandom.nextFloat(-mHeight*0.5f, mHeight*0.5f);
		}
	}

	public RectangleOutLineEmitter copy(){
		return new RectangleOutLineEmitter(mCentreX,mCentreY,mWidth,mHeight);
	}
}
