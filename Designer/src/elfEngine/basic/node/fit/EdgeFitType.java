package elfEngine.basic.node.fit;

public enum EdgeFitType {
	MinEdge, MaxEdge, BothXY;
	
	public static float getValueX(float sx, float sy, float wx, float wy, float vx, float vy, EdgeFitType type) {
		switch(type) { 
		case MaxEdge:
			final float max = Math.max(sx, sy);
			return max*wx+vx;
		case MinEdge:
			final float min = Math.min(sx, sy);
			return min*wx+vx;
		} 
		
		return sx*wx + vx;
	}
	
	public static float getValueY(float sx, float sy, float wx, float wy, float vx, float vy, EdgeFitType type) {
		switch(type) {
		case MaxEdge:
			final float max = Math.max(sx, sy);
			return max*wy+vy;
		case MinEdge:
			final float min = Math.min(sx, sy);
			return min*wy+vy;
		} 
		
		return sy*wy + vy;
	}
} 
