package elfEngine.graphics;

public class Point {
	public int x, y;
	public Point(){
	}
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void set(Point point) {
		this.x = point.x;
		this.y = point.y;
	}
	
	public boolean equals(Object o){
		if(o == null){
			return false;
		} else if(o instanceof Point){
			return x==((Point)o).x && y==((Point)o).y;
		}
		return false;
	}
	
	public String toString(){
		return "("+x+","+y+")";
	}
}
