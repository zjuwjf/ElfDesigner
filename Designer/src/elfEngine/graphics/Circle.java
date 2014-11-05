package elfEngine.graphics;

import elfEngine.basic.math.MathCons;

public class Circle extends BasicShape{
	
	private final static int sDefaultVertexs = 32;
	
	private float mRadiusX, mRadiusY;
	private float mCentreX, mCentreY;
	private int mVertexs = sDefaultVertexs;
	
	public Circle(final float cx, final float cy, final float radiusX, final float radiusY){
		mCentreX = cx;
		mCentreY = cy;
		
		mRadiusX = radiusX;
		mRadiusY = radiusY;
		
		mVertexs = sDefaultVertexs;
	}
	
	public Circle(final float cx, final float cy, final float radius){
		this(cx, cy, radius, radius);
	}
	
	public float width(){
		return mRadiusX*2;
	}
	
	public float height(){
		return mRadiusY*2;
	}
	
	public void translate(final float tx, final float ty){
		mCentreX += tx;
		mCentreY += ty;
	}
	
	public float area(){
		return mRadiusX*mRadiusY*MathCons.PI;
	}
	
	public boolean contains(float x, float y){
		x -= mCentreX;
		y -= mCentreY;
		
		final float rx2 = mRadiusX*mRadiusX;
		final float ry2 = mRadiusY*mRadiusY;
		
		return x*x*ry2 + y*y*rx2<= rx2*ry2;
	}

	public Rectangle bounds() {
		return new Rectangle(mCentreX-mRadiusX,mCentreY-mRadiusY,
				mCentreX+mRadiusX,mCentreY+mRadiusY);
	}

	public boolean collideWith(IShape shape) {
		final ShapeType type = shape.getType();
		if(type==ShapeType.CIRCLE){
			final Circle circle = (Circle)shape;
			final float z = (float)Math.atan2(mCentreY-circle.mCentreY, mCentreX-circle.mCentreX);
			
			final double cos = Math.cos(z);
			final double sin = Math.sin(z);
			
			final double r1 = Math.sqrt(mRadiusX*mRadiusX*cos*cos+mRadiusY*mRadiusY*sin*sin);
			final double r2 = Math.sqrt(circle.mRadiusX*circle.mRadiusX*cos*cos+circle.mRadiusY*circle.mRadiusY*sin*sin);
			
			final double r = Math.sqrt((mCentreX-circle.mCentreX)*(mCentreX-circle.mCentreX)+(mCentreY-circle.mCentreY)*(mCentreY-circle.mCentreY));
			
			return r <= r1+r2;
		} else if(type==ShapeType.RECTANGLE){
			final Rectangle rectangle = (Rectangle)shape;
			if(contains(rectangle.left, rectangle.bottom)){
				return true;
			} else if(contains(rectangle.left, rectangle.top)){
				return true;
			} else if(contains(rectangle.right, rectangle.bottom)){
				return true;
			} else if(contains(rectangle.right, rectangle.top)){
				return true;
			}
		} else if(type==ShapeType.POLYGON){
			final Polygon polygon = (Polygon)shape;
			final int vertexs = polygon.getVertexs();
			final float [] xs = polygon.getXs();
			final float [] ys = polygon.getYs();
			for(int i=0; i<vertexs; i++){
				if(contains(xs[i], ys[i])){
					return true;
				}
			}
		}
		return false;
	}

	public Vertex getCentre() {
		return new Vertex(mCentreX,mCentreY);
	}

	public ShapeType getType() {
		return ShapeType.CIRCLE;
	}
	
	public void setVertexs(final int vertexs){
		mVertexs = vertexs;
	}

	public int getVertexs() {
		return mVertexs;
	}

	public float[] getXs() {
		final float [] xs = new float[mVertexs];
		final float basicRad = MathCons._2PI/mVertexs;
		float rad = 0f;
		
		for(int i=0; i<mVertexs; i++){
			final float cos = (float)Math.cos(rad);
			xs[i] = cos*mRadiusX + mCentreX;
			rad+=basicRad;
		}
		
		return xs;
	}

	public float[] getYs() {
		final float [] ys = new float[mVertexs];
		final float basicRad = MathCons._2PI/mVertexs;
		float rad = 0f;
		
		for(int i=0; i<mVertexs; i++){
			final float sin = (float)Math.sin(rad);
			ys[i] = sin*mRadiusY + mCentreY;
			rad+=basicRad;
		}
		
		return ys;
	}

	public short[] triangles() {
		//convex polygon
		final int triangles = mVertexs-2;
		final short [] is = new short[triangles*3];
		
		for(short i=1; i<=triangles; i++){
			final int index = i*3;
			is[index] = 0;
			is[index+1] = i;
			is[index+2] = (short)(i+1);
		}
		
		return is;
	}
	
	public float getGirth(){
		return (mRadiusX+mRadiusY)*MathCons.PI;
	}
}
