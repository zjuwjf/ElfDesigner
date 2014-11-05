package elfEngine.graphics;


public class Segment {
	public Vertex p1; 
	public Vertex p2;
	
	public Segment(){
		this(new Vertex(), new Vertex());
	}
	
	public Segment(final Vertex p1, final Vertex p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Segment(final Segment s){
		this(s.p1,s.p2);
	}
	
	public void set(final Vertex p1, final Vertex p2){
		this.p1.set(p1);
		this.p2.set(p2);
	}
	
	public void set(final Segment s){
		this.set(s.p1, s.p2);
	}
	
	public void set(final float x1, final float y1, final float x2, final float y2){
		p1.x = x1;
		p1.y = y1;
		p2.x = x2;
		p2.y = y2;
	}
	
	public float getLength(){
		final float dx = (p1.x-p2.x);
		final float dy = (p1.y-p2.y);
		return (float) Math.sqrt(dx*dx+dy*dy);
	}
	
	public Vertex getCentre(){
		return new Vertex((p1.x+p2.x)*0.5f, (p1.y+p2.y)*0.5f);
	}
	
	public void translate(final float dx, final float dy){
		p1.translate(dx, dy);
		p2.translate(dx, dy);
	}
	
	public void breakOrExtend(final float beg, final float end, final Segment ret){
		getVertex(beg, ret.p1);
		getVertex(end, ret.p2);
	}
	
	public void getVertex(final float rate, final Vertex ret){
		final float x = p1.x + (p2.x-p1.x)*rate;
		final float y = p1.y + (p2.y-p1.y)*rate;
		ret.x = x;
		ret.y = y;
	}
	
	public String toString(){
		return "[ "+p1.toString()+", "+p2.toString()+" ]";
	}
}
