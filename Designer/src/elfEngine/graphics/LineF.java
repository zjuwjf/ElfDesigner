package elfEngine.graphics;


public class LineF {
	
	public static final float THRESHHOLD = 0.000000001F;
	
	public PointF p1, p2;
	
	public LineF(PointF p1, PointF p2){
		this.p1 = p1;
		this.p2 = p2;
	}

	public LineF(LineF src){
		this(src.p1, src.p2);
	}
	
	public LineF(float p1x, float p1y, float p2x, float p2y){
		this(new PointF(p1x, p1y), new PointF(p2x, p2y));
	}
	
	public void set(PointF p1, PointF p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public float getLength(){
		return (float)Math.sqrt((this.p1.x - this.p2.x)*(this.p1.x - this.p2.x) 
				+ (this.p1.y - this.p2.y)*(this.p1.y - this.p2.y));
	}
	
	public float distance(PointF mP1,PointF mP2)
	{
		return (float)Math.sqrt((mP1.x-mP2.x)*(mP1.x-mP2.x)+(mP1.y-mP2.y)*(mP1.y-mP2.y));
	}
	
	public boolean checkIronLineDis(PointF point,float dist)
	{
		if(this.p1==this.p2)
		{
			float dis2 = distance(point,this.p1);
			if(dist>dis2)
				return false;
			return true;
		}
		else if(this.p1.x==this.p2.x)
		{
			float mDis = distance(this.p1,this.p2);
			float dis2 = distance(point,this.p1);
			float dis3 = distance(point,this.p2);
			

			if((mDis*mDis+dis2*dis2)<dis3*dis3)
			{
				return true;
			}
			else if((mDis*mDis+dis3*dis3)<dis2*dis2)
			{
				return true;
			}
			else
			{
				float dis1 = Math.abs(point.x-this.p1.x);
				if(dist>dis1)
					return false;
				return true;
			}
		}
		else if(this.p1.y==this.p2.y)
		{
			float mDis = distance(this.p1,this.p2);
			float dis2 = distance(point,this.p1);
			float dis3 = distance(point,this.p2);

			if((mDis*mDis+dis2*dis2)<dis3*dis3)
			{
				return true;
			}
			else if((mDis*mDis+dis3*dis3)<dis2*dis2)
			{
				return true;
			}
			else
			{
				float dis1 = Math.abs(point.y-this.p1.y);
				if(dist>dis1)
					return false;
				return true;
			}
		}
		else
		{
			float mDis = distance(this.p1,this.p2);
			float dis2 = distance(point,this.p1);
			float dis3 = distance(point,this.p2);
			

			if((mDis*mDis+dis2*dis2)<dis3*dis3)
			{
				return true;
			}
			else if((mDis*mDis+dis3*dis3)<dis2*dis2)
			{
				return true;
			}
			
			else
			{
				float b = (float)(this.p2.x-this.p1.x)/(float)(this.p1.y-this.p2.y);
				float c = -b*(float)this.p1.y - (float)this.p1.x;
				float dis1 = (float)(Math.abs((float)point.x+b*(float)point.y+c)/Math.sqrt(1.0+b*b));
				
				//System.err.println("this is distance dis1:"+dis1);
				
				if(dist>dis1)
					return false;
				return true;
			}
		}
	}
	
	public boolean checkPoint(PointF point,float dist)
	{
		if(this.p1==this.p2)
		{
			float dis2 = distance(point,this.p1);
			if(dist<dis2)
				return false;
			return true;
		}
		else if(this.p1.x==this.p2.x)
		{
			float mDis = distance(this.p1,this.p2);
			float dis2 = distance(point,this.p1);
			float dis3 = distance(point,this.p2);
			
//			if(Math.acos((dis2*dis2-dis3*dis3+mDis*mDis)/2*dis2*mDis)>Math.PI/2)
//			{
//				if(dist<dis2)
//					return false;
//				return true;
//			}
//			else if(Math.acos((-dis2*dis2+dis3*dis3+mDis*mDis)/2*dis3*mDis)>Math.PI/2)
//			{
//				if(dist<dis3)
//					return false;
//				return true;
//			}
			if((mDis*mDis+dis2*dis2)<dis3*dis3)
			{
				//System.err.println("this is distance dis2:"+dis2);
				if(dist<dis2)
					return false;
				return true;
			}
			else if((mDis*mDis+dis3*dis3)<dis2*dis2)
			{
				//System.err.println("this is distance dis3:"+dis3);
				if(dist<dis3)
					return false;
				return true;
			}
			else
			{
				float dis1 = Math.abs(point.x-this.p1.x);
				if(dist<dis1)
					return false;
				return true;
			}
		}
		else if(this.p1.y==this.p2.y)
		{
			float mDis = distance(this.p1,this.p2);
			float dis2 = distance(point,this.p1);
			float dis3 = distance(point,this.p2);
			
//			if(Math.acos((dis2*dis2-dis3*dis3+mDis*mDis)/2*dis2*mDis)>Math.PI/2)
//			{
//				if(dist<dis2)
//					return false;
//				return true;
//			}
//			else if(Math.acos((-dis2*dis2+dis3*dis3+mDis*mDis)/2*dis3*mDis)>Math.PI/2)
//			{
//				if(dist<dis3)
//					return false;
//				return true;
//			}
			if((mDis*mDis+dis2*dis2)<dis3*dis3)
			{
				//System.err.println("this is distance dis2:"+dis2);
				if(dist<dis2)
					return false;
				return true;
			}
			else if((mDis*mDis+dis3*dis3)<dis2*dis2)
			{
				//System.err.println("this is distance dis3:"+dis3);
				if(dist<dis3)
					return false;
				return true;
			}
			else
			{
				float dis1 = Math.abs(point.y-this.p1.y);
				if(dist<dis1)
					return false;
				return true;
			}
		}
		else
		{
			float mDis = distance(this.p1,this.p2);
			float dis2 = distance(point,this.p1);
			float dis3 = distance(point,this.p2);
			
//			if(Math.acos((dis2*dis2-dis3*dis3+mDis*mDis)/(2*dis2*mDis))>Math.PI/2)
//			{
//				System.err.println("this is distance dis2:"+dis2);
//				if(dist<dis2)
//					return false;
//				return true;
//			}
//			else if(Math.acos((dis3*dis3-dis2*dis2+mDis*mDis)/(2*dis3*mDis))>Math.PI/2)
//			{
//				System.err.println("this is distance dis3:"+dis3);
//				if(dist<dis3)
//					return false;
//				return true;
//			}
			if((mDis*mDis+dis2*dis2)<dis3*dis3)
			{
				//System.err.println("this is distance dis2:"+dis2);
				if(dist<dis2)
					return false;
				return true;
			}
			else if((mDis*mDis+dis3*dis3)<dis2*dis2)
			{
				//System.err.println("this is distance dis3:"+dis3);
				if(dist<dis3)
					return false;
				return true;
			}
			
			else
			{
				float b = (float)(this.p2.x-this.p1.x)/(float)(this.p1.y-this.p2.y);
				float c = -b*(float)this.p1.y - (float)this.p1.x;
				float dis1 = (float)(Math.abs((float)point.x+b*(float)point.y+c)/Math.sqrt(1.0+b*b));
				//System.err.print(point.x+" "+point.y+" "+p1.x+" "+p1.y+" "+p2.x+" "+p2.y);
				//System.err.println("this is distance dis1:"+dis1);
				
				if(dist<dis1)
					return false;
				return true;
			}
		}
	}
	
	/**
	 * Check if <code>line</code> is on this line
	 * @param line
	 * @return
	 */
	public boolean ownLineF(LineF line){
		return this.pointOnLine(line.p1) && this.pointOnLine(line.p2);
	}
	
	/**
	 * @param dir
	 * 		direction of extend
	 * 		1:		p1
	 * 		other:	p2
	 * @param extendLengthRate
	 * 		Length to extend
	 * @return
	 * 		The new Extended Line
	 */
	public LineF getExtendedLine(int dir, float extendLengthRate){
		LineF res = new LineF(0, 0, 0, 0);
		
		if(dir == 1){
			res.p2.x = this.p2.x;
			res.p2.y = this.p2.y;
			
			float dx = this.p1.x - this.p2.x;
			float dy = this.p1.y - this.p2.y;
			
			res.p1.x = this.p1.x + dx * extendLengthRate;
			res.p1.y = this.p1.y + dy * extendLengthRate;
			
		}else{
			res.p1.x = this.p1.x;
			res.p1.y = this.p1.y;
			
			float dx = this.p2.x - this.p1.x;
			float dy = this.p2.y - this.p1.y;
			
			res.p2.x = this.p2.x + dx * extendLengthRate;
			res.p2.y = this.p2.y + dy * extendLengthRate;
		}
		return res;
	}
	
	public final boolean pointOnLine(PointF p){
		return 
			((this.p1.x <= p.x && p.x <= this.p2.x) || (this.p2.x <= p.x && p.x <= this.p1.x)) &&
			((this.p1.y <= p.y && p.y <= this.p2.y) || (this.p2.y <= p.y && p.y <= this.p1.y)) &&
			Math.abs(((p.y - this.p1.y)*(this.p2.x - this.p1.x) - (this.p2.y - this.p1.y)*(p.x - this.p1.x))) < THRESHHOLD
			;
	}
	
	public final boolean parallelWith(LineF line){
		return (this.p1.x - this.p2.x)*(line.p1.y - line.p2.y) == (this.p1.y - this.p2.y)*(line.p1.x - line.p2.y);
	}
	
	public final static PointF getIntersectPoint(LineF line1, LineF line2){
		return line1.getIntersectPointWith(line2);
	}
	
	public final static boolean intersectStrict(LineF line1, LineF line2){
		return line1.intersectStrictWith(line2);
	}
	
	public final static boolean intersectLoose(LineF line1, LineF line2){
		return line1.intersectLooseWith(line2);
	}
	
	public final PointF getIntersectPointWith(LineF l){
		if(!this.intersectLooseWith(l)){
			return null;
		}
		
		float numerator, denominator;
		denominator = (this.p2.y - this.p1.y)*(l.p2.x - l.p1.x) - (l.p2.y - l.p1.y)*(this.p2.x - this.p1.x);
		if(denominator == 0){
			return null;
		}
		numerator = (l.p1.y*l.p2.x - l.p1.x*l.p2.y)*(this.p2.x - this.p1.x) - (this.p1.y*this.p2.x - this.p1.x*this.p2.y)*(l.p2.x - l.p1.x);
		PointF p = new PointF(numerator/denominator, 0);
		if(this.p2.x == this.p1.x){
			p.y = (p.x - l.p1.x)*(l.p2.y - l.p1.y)/(l.p2.x - l.p1.x) + l.p1.y;
		}else{
			p.y = (p.x - this.p1.x)*(this.p2.y - this.p1.y)/(this.p2.x - this.p1.x) + this.p1.y;
		}
		return p;
	}
	
	public final boolean intersectStrictWith(LineF line){
		return 
			Math.max(this.p1.x, this.p2.x) >= Math.min(line.p1.x, line.p2.x) &&
			Math.max(line.p1.x, line.p2.x) >= Math.min(this.p1.x, this.p2.x) &&
			Math.max(this.p1.y, this.p2.y) >= Math.min(line.p1.y, line.p2.y) &&
			Math.max(line.p1.y, line.p2.y) >= Math.min(this.p1.y, this.p2.y) &&
			
			cross(this.p1.x - line.p1.x, this.p1.y - line.p1.y, line.p2.x - line.p1.x, line.p2.y - line.p1.y) *
			cross(this.p2.x - line.p1.x, this.p2.y - line.p1.y, line.p2.x - line.p1.x, line.p2.y - line.p1.y) < 0 &&
			
			cross(line.p1.x - this.p1.x, line.p1.y - this.p1.y, this.p2.x - this.p1.x, this.p2.y - this.p1.y) *
			cross(line.p2.x - this.p1.x, line.p2.y - this.p1.y, this.p2.x - this.p1.x, this.p2.y - this.p1.y) < 0
			;
	}
	
	public final boolean intersectLooseWith(LineF line){		
		return 
		Math.max(this.p1.x, this.p2.x) >= Math.min(line.p1.x, line.p2.x) &&
		Math.max(line.p1.x, line.p2.x) >= Math.min(this.p1.x, this.p2.x) &&
		Math.max(this.p1.y, this.p2.y) >= Math.min(line.p1.y, line.p2.y) &&
		Math.max(line.p1.y, line.p2.y) >= Math.min(this.p1.y, this.p2.y) &&
		
		cross(p1.x - line.p1.x, p1.y - line.p1.y, line.p2.x - line.p1.x, line.p2.y - line.p1.y) *
		cross(p2.x - line.p1.x, p2.y - line.p1.y, line.p2.x - line.p1.x, line.p2.y - line.p1.y) <= 0 &&
		
		cross(line.p1.x - p1.x, line.p1.y - p1.y, p2.x - p1.x, p2.y - p1.y) *
		cross(line.p2.x - p1.x, line.p2.y - p1.y, p2.x - p1.x, p2.y - p1.y) <= 0
		;
	}
	
	public final boolean aside(LineF line){
		return cross(line.p1.x - this.p1.x, line.p1.y - this.p1.y, this.p2.x - this.p1.x, this.p2.y - this.p1.y) *
		cross(line.p2.x - this.p1.x, line.p2.y - this.p1.y, this.p2.x - this.p1.x, this.p2.y - this.p1.y) > 0;
	}
	
	private final float cross(float v1x, float v1y, float v2x, float v2y){
		return v1x * v2y - v2x * v1y;
	}
	
	@Override
	public String toString(){
		return "(" + this.p1.x + ", " + this.p1.y + ") --> (" + this.p2.x + ", " + this.p2.y + ")";
	}
	
	public void mirror(PointF p1, float radius, float rotate, float length, PointF ret){
		
	}
	
	public PointF getCentre(){
		return new PointF((p1.x+p2.x)*0.5f, (p1.y+p2.y)*0.5f);
	}
	
	public void copy(LineF copy){
		p1.x = copy.p1.x;
		p2.x = copy.p2.x;
		
		p1.y = copy.p1.y;
		p2.y = copy.p2.y;
	}
}
