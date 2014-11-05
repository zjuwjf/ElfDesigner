package elfEngine.graphics;



public class Rectangle extends BasicShape{
	
	private static final short [] sTriangles = {0,1,2,1,2,3};
	public float left, right, top, bottom;
	
	public Rectangle(){
		super();
	}
	
	/**
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 */
	public Rectangle(float left, float right, float bottom, float top) {
		super();
		this.left = left;
		this.right = right;
		
		this.bottom = bottom;
		this.top = top;
	}
	
	public float width(){
		return Math.abs( right - left );
	}
	
	public float height(){
		return Math.abs( top - bottom );
	}
	
	public float area(){
		return Math.abs( (right - left)*(top - bottom) );
	}
	
	public Vertex getCentre(){
		final Vertex ret = new Vertex((left+right)*0.5f, (top+bottom)*0.5f);
		return ret;
	}
	
	@Deprecated
	public  Rectangle bounds(){
		return this;
	}
	
	public ShapeType getType(){
		return ShapeType.RECTANGLE;
	}
	
	public boolean collideWith(final IShape shape){
		final ShapeType type = shape.getType();
		switch(type){
		case RECTANGLE:
		case CIRCLE:
		case POLYGON:
		}
		return false;
	}


	public boolean contains(final float x, final float y) {
		return x>=left&&x<=right&&y>=bottom&&y<=top;
	}
	
	public void translate(float x, float y) {
		left += x;
		right += y;
		
		bottom+=y;
		top+=y;
	}

	public float[] getXs() {
		final float [] xs = new float[]{left,left,right,right};
		return xs;
	}

	public float[] getYs() {
		final float [] ys = new float[]{top,bottom,bottom,top};
		return ys;
	}

	public short[] triangles() {
		return sTriangles;
	}

	public final int getVertexs() {
		return 4;
	}
	
	public float getGirth(){
		return 2*(width() + height());
	}
}
