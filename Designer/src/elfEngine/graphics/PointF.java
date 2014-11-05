package elfEngine.graphics;

import com.ielfgame.stupidGame.data.ElfDataDisplay;


public class PointF extends ElfDataDisplay{
	public float x, y;
	
	public PointF(){
	}
	
	public PointF(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void setPoint(float x, float y){
		this.x = x;
		this.y = y;
	} 

	public void setPoint(PointF mradial) {
		this.x = mradial.x;
		this.y = mradial.y;
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
}
