package elfEngine.graphics;

public abstract class BasicShape implements IShape{
	
	public final void getLine(final int index, final float [] ret){
		final int vertexs = getVertexs();
		if(index>=vertexs){
			throw new IllegalArgumentException("index must < mVertexs!");
		}
		if(index<0){
			throw new IllegalArgumentException("index must >= 0!");
		}
		
		final float [] xs = getXs();
		final float [] ys = getYs();
		
		final int next = nextIndex(index);
		ret[0] = xs[index];
		ret[1] = ys[index];
		ret[2] = xs[next];
		ret[3] = ys[next];
	}
	
	public final void getLine(final int index, final Segment ret){
		final int vertexs = getVertexs();
		if(index>=vertexs){
			throw new IllegalArgumentException("index must < mVertexs!");
		}
		if(index<0){
			throw new IllegalArgumentException("index must >= 0!");
		}
		
		final float [] xs = getXs();
		final float [] ys = getYs();
		
		final int next = nextIndex(index);
		ret.p1.x = xs[index];
		ret.p1.y = ys[index];
		ret.p2.x = xs[next];
		ret.p2.y = ys[next];
	}
	
	protected final int nextIndex(final int index){
		final int vertexs = getVertexs();
		return index >= vertexs-1?0:index+1;
	}
	
	/*
	protected float mRotate=0f;
	protected float mScaleX=1f, mScaleY=1f;
	
	
	@Override
	public float getRotate() {
		// TODO Auto-generated method stub
		return mRotate;
	}

	@Override
	public float getScaleX() {
		// TODO Auto-generated method stub
		return mScaleX;
	}

	@Override
	public float getScaleY() {
		// TODO Auto-generated method stub
		return mScaleY;
	}

	@Override
	public boolean isRotated() {
		// TODO Auto-generated method stub
		return mRotate!=0f;
	}

	@Override
	public boolean isScaled() {
		// TODO Auto-generated method stub
		return mScaleX!=1f || mScaleY!=1f;
	}

	@Override
	public void scale(final float sx, final float sy) {
		// TODO Auto-generated method stub
		mScaleX = sx;
		mScaleY = sy;
	}

	@Override
	public void scaleX(final float sx) {
		// TODO Auto-generated method stub
		mScaleX = sx;
	}

	@Override
	public void scaleY(final float sy) {
		// TODO Auto-generated method stub
		mScaleY = sy;
	}

	@Override
	public void setRotate(final float r) {
		// TODO Auto-generated method stub
		mRotate = r;
	}


	@Override
	public void reset() {
		// TODO Auto-generated method stub
		mRotate = 0f;
		mScaleX = 1f;
		mScaleY = 1f;
	}
	*/
}
