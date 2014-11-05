package elfEngine.graphics;

import java.util.LinkedList;

public class Curve { 
	public final LinkedList<PointF> points = new LinkedList<PointF>(); 
	public boolean close = false; 
	public float length;
	public int pointNum; 
	
	
	public void update() { 
		length = 0; 
		pointNum = 0;
		if(points.size() > 1) { 
			PointF beg = null; 
			for(PointF p : points) { 
				if(beg != null) { 
					length += (beg.x-p.x)*(beg.x-p.x) + (beg.y-p.y)*(beg.y-p.y); 
				} 
				beg = p; 
				pointNum ++ ;
			} 
			if(close) { 
				PointF p = points.getFirst(); 
				length += (beg.x-p.x)*(beg.x-p.x) + (beg.y-p.y)*(beg.y-p.y); 
			} 
		} 
	} 
	
	public PointF [] getPerpendicularLine(float pos, float width) { 
		final int size = pointNum;
		if(size <= 1) { 
			return null; 
		} else { 
			if(pos <= 0) { 
				if(close) { 
					return getPerpendicularLine(points.getLast(), points.getFirst(), points.get(1), width); 
				} else { 
					return getPerpendicularLine(points.getFirst(), points.get(1), 0, width); 
				} 
			} else if(pos >= length) { 
				if(close) { 
					return getPerpendicularLine(points.get(pointNum-2), points.getLast(), points.getFirst(), width); 
				} else { 
					return getPerpendicularLine(points.get(pointNum-2), points.getLast(), 1, width); 
				} 
			}
			
			float len = 0; 
			PointF beg = null; 
			int count = 0;
			for(PointF p : points) { 
				if(beg != null) { 
					final float piece = (beg.x-p.x)*(beg.x-p.x) + (beg.y-p.y)*(beg.y-p.y); 
					len += piece; 
					if(len > pos) { 
						return getPerpendicularLine(beg, p, (pos - (len - piece))/piece, width); 
					} else if(len == pos) { 
						if(count >= pointNum - 1) {
							if(close) { 
								return getPerpendicularLine(points.get(pointNum-2), points.getLast(), points.getFirst(), width); 
							} else { 
								return getPerpendicularLine(points.get(pointNum-2), points.getLast(), 1, width); 
							} 
						} else { 
							return getPerpendicularLine(beg, p, points.get(count+1), width); 
						} 
					} 
				} 
				
				beg = p; 
				count ++;
			} 
			
			return null; 
		} 
	} 
	
	public static PointF [] getPerpendicularLine(PointF p1, PointF p2, float rate, float width) {
		final PointF [] ret = new PointF[2]; 
		
		final PointF mid = new PointF(p1.x*(1-rate) + p2.x*rate, p1.y*(1-rate) + p2.y*rate);
		
		double angle = Math.atan2(p2.y-p1.y, p2.x-p1.x); 
		
		float dx = (float)(-Math.sin(angle) * width * 0.5f);
		float dy = (float)(+Math.cos(angle) * width * 0.5f);
		
		ret[0] = new PointF(mid.x + dx, mid.y + dy);
		ret[1] = new PointF(mid.x - dx, mid.y - dy);
		
		return ret;
	} 
	
	public static PointF [] getPerpendicularLine(PointF p1, PointF p2, PointF p3, float width) { 
		final PointF [] ret = new PointF[2]; 
		
//		final float len1 = (float)Math.sqrt((p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y));
//		final float len2 = (float)Math.sqrt((p3.x-p2.x)*(p3.x-p2.x)+(p3.y-p2.y)*(p3.y-p2.y));
		
		double angle1 = Math.atan2(p2.y-p1.y, p2.x-p1.x);  
		double angle2 = Math.atan2(p3.y-p2.y, p3.x-p2.x);  
//		double angle = (angle1*len1 + angle2*len2) /(len1+len2);
		double angle = (angle1 + angle2)/2; 
		
		float dx = (float)(-Math.sin(angle) * width * 0.5f);
		float dy = (float)(+Math.cos(angle) * width * 0.5f);
		
		ret[0] = new PointF(p2.x + dx, p2.y + dy);
		ret[1] = new PointF(p2.x - dx, p2.y - dy);
		
		return ret; 
	} 
} 
