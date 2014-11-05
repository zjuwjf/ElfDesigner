package elfEngine.graphics;

public interface IShape {
	/**
	 * ^
	 * |
	 * |
	 * ---------->
	 */
	
	public boolean contains(final float x, final float y);
	
	public float area();
	
	public Rectangle bounds();
	
	public ShapeType getType();
	
	public boolean collideWith(final IShape shape);
	
	public Vertex getCentre();
	
	public void translate(final float x, final float y);
	
	public void getLine(final int index, final float [] ret);
	public void getLine(final int index, final Segment ret);
	
	public int getVertexs();
	
	public float [] getXs();
	
	public float [] getYs();
	
	public short [] triangles();
	
	public float getGirth();
	
	/*
	public void setRotate(final float r);
	
	public float getRotate();
	
	public boolean isRotated();
	
	public void scale(final float sx, final float sy);
	
	public void scaleX(final float sx);
	
	public float getScaleX();
	
	public void scaleY(final float sy);
	
	public float getScaleY();
	
	public boolean isScaled();
	
	public void reset();
	*/
	
}
