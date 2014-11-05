package elfEngine.opengl;

public class RectF {
	public float left, right, top, bottom;
	
	public RectF(){
		
	}
	public RectF(float left, float right, float top, float bottom) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public void set(float left, float top, float right, float bottom){
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	
	public String toString(){
		return "RectF("+left+","+right+","+top+","+bottom+")";
	}
	
//	public static RectF fromString(String string){
//		
//	}
}
