package elfEngine.basic.node.particle.emitter;

import elfEngine.basic.pool.ArrayPool;
import elfEngine.extend.ElfRandom;
import elfEngine.graphics.GraphHelper;
import elfEngine.graphics.IShape;

public class ShapeOutLineEmitter extends BasicShapeEmitter{

	public ShapeOutLineEmitter(IShape shape, float centreX,
			float centreY) {
		super(shape, centreX, centreY);
	}

	@Override
	public void getPosition(float[] position) {
		if(mShape != null){
			final int vertexs = mShape.getVertexs();
			final float girth = mShape.getGirth();
			final float length = ElfRandom.nextFloat()*girth;
			
			float count = 0;
			for(int i=0; i<vertexs; i++){
				final float [] ret = ArrayPool.float4;
				mShape.getLine(i, ret);
				final float dis = GraphHelper.distance(ret[0], ret[1], ret[2], ret[3]);
				count += dis;
				if(count >= length){
					final float rate = (count-length)/dis;
					position[0] = ret[2]+(ret[0]-ret[2])*rate;
					position[1] = ret[3]+(ret[1]-ret[3])*rate;
					break;
				}
			}
		}
	}
	
	public ShapeOutLineEmitter copy(){
		return new ShapeOutLineEmitter(mShape, mCentreX, mCentreY);
	}
}
