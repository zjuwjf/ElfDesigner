package elfEngine.graphics;


public class Vertex extends PointF{
	
	public Vertex(){
		this(100,100);
	}
	
	public Vertex(Vertex copy){
		this(copy.x, copy.y);
	}
	
	public Vertex(final float x, final float y){
		this.x = x;
		this.y = y;
	}
	
	public void set(Vertex copy){
		this.setPoint(copy.x,copy.y);
	}
	
	public void translate(final float dx, final float dy){
		this.x+=dx;
		this.y+=dy;
	}
	
	public boolean equals(final Object o){
		if(o instanceof Vertex){
			final Vertex v = (Vertex)o;
			return v.x == x && v.y == y;
		}
		return false;
	}
}
