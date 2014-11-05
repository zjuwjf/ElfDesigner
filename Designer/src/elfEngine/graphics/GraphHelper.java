package elfEngine.graphics;

import elfEngine.basic.math.MathCons;


public final class GraphHelper {
	
	public static enum Concave{
		CONCAVE, CONVEX, FLAT
	}
	
	public static enum Site{
		OUT_VERTEX, //p1 == p2. test!=p1
		LEFT_SIDE, RIGHT_SIDE, 
		P1_EXTEND, P2_EXTEND, 
		ON_LINE
	}
	
	public static Site side(final float p1x,final float p1y,
			final float testx,final float testy,
			final float p2x,final float p2y){
		
		final float value = ((p1x-testx)*(testy-p2y)-(p1y-testy)*(testx-p2x));
		if(value==0){
			if(p1x>p2x){
				if(testx>p1x){
					return Site.P1_EXTEND;
				} else if(testx<p2x){
					return Site.P2_EXTEND;
				} else {
					return Site.ON_LINE;
				}
			} else if(p1x<p2x){
				if(testx>p2x){
					return Site.P2_EXTEND;
				} else if(testx<p1x){
					return Site.P1_EXTEND;
				} else {
					return Site.ON_LINE;
				}
			} else {
				if(p1y>p2y){
					if(testy>p1y){
						return Site.P1_EXTEND;
					} else if(testy<p2y){
						return Site.P2_EXTEND;
					} else {
						return Site.ON_LINE;
					} 
				} else if(p2y>p1y){
					if(testy>p2y){
						return Site.P2_EXTEND;
					} else if(testy<p1y){
						return Site.P1_EXTEND;
					} else {
						return Site.ON_LINE;
					} 
				} else{
					if(testy==p1y&&testx==p1x){
						return Site.ON_LINE;
					} else {
						return Site.OUT_VERTEX;
					} 
				}
			}
			
		} else if(value<0){
			return Site.LEFT_SIDE;
		} else {
			return Site.RIGHT_SIDE;
		}
	}
	
	/**
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @param p3x
	 * @param p3y
	 * @return
	 * p2 is the test vertex
	 */
	public static boolean isConvex(final float p1x,final float p1y,
			final float p2x,final float p2y,
			final float p3x,final float p3y){
		
		return side(p1x,p1y,p2x,p2y,p3x,p3y) == Site.LEFT_SIDE;
	}
	
	/**
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @param p3x
	 * @param p3y
	 * @param testX
	 * @param testY
	 * @return
	 * make sure p1,p2,p3 are not on the same line, not care of clockwise
	 * inside or on the 3 lines
	 */
	public static boolean isInsideTriangle(final float p1x,final float p1y,
			final float p2x,final float p2y,
			final float p3x,final float p3y, final float testX, final float testY){
		
		final Site a = side(p1x,p1y,testX,testY,p2x,p2y);
		if(a==Site.ON_LINE){
			return true;
		}
		
		final Site b = side(p2x,p2y,testX,testY,p3x,p3y);
		if(b==Site.ON_LINE){
			return true;
		} else if( a!=b ){
			return false;
		}
		
		final Site c = side(p3x,p3y,testX,testY,p1x,p1y);
		if(c==Site.ON_LINE){
			return true;
		} else {
			return b==c;
		}
		
	}
	
	public static boolean isCrossed(final float x1, final float y1, final float x2, final float y2,
			final float x3,final float y3, final float x4, final float y4){
		
		final Site s1 = side(x3,y3, x1,y1, x4,y4);
		if(s1==Site.ON_LINE){
			return true;
		}
		
		final Site s2 = side(x3,y3, x2,y2, x4,y4);
		if(s2==Site.ON_LINE){
			return true;
		}
		
		if((s1==Site.LEFT_SIDE&&s2==Site.RIGHT_SIDE) || (s1==Site.RIGHT_SIDE&&s2==Site.LEFT_SIDE)){
			final Site s3 = side(x1,y1, x3,y3, x2,y2);
			if(s3==Site.ON_LINE){
				return true;
			}
			
			final Site s4 = side(x1,y1, x4,y4, x2,y2);
			if(s4==Site.ON_LINE){
				return true;
			}
			
			return (s3==Site.LEFT_SIDE&&s4==Site.RIGHT_SIDE) || (s3==Site.RIGHT_SIDE&&s4==Site.LEFT_SIDE);
		} else {
			return false;
		}
	}
	
	public static CrossRet cross(final float x1, final float y1, final float x2, final float y2,
			final float x3,final float y3, final float x4, final float y4){
		
		final CrossRet ret = new CrossRet();
		ret.isCrossed = isCrossed(x1,y1,x2,y2,x3,y3,x4,y4);
		
		if(ret.isCrossed){
			final float denor = ((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4));
			if(denor == 0f){// parallel
				if(side(x1,y1,x3,y3,x2,y2)==Site.ON_LINE){
					ret.cross = new Vertex(x3, y3);
				} else if(side(x1,y1,x4,y4,x2,y2)==Site.ON_LINE){
					ret.cross = new Vertex(x4, y4);
				} else if(side(x3,y3,x1,y1,x4,y4)==Site.ON_LINE){
					ret.cross = new Vertex(x1, y1);
				} else {
					ret.cross = new Vertex(x2, y2);
				} 
			} else {
				final float t=((x1-x3)*(y3-y4)-(y1-y3)*(x3-x4))/denor;
				final float x = x1+(x2-x1)*t;
				final float y = y1+(y2-y1)*t;
				
				ret.cross = new Vertex(x,y);
			}
		}

		return ret;
	}
	
	public static float distance(final float x1,final float y1,final float x2,final float y2){
		final float dx = x1-x2;
		final float dy = y1-y2;
		return (float) Math.sqrt(dx*dx+dy*dy);
	}
	
	public static float distance2(final float x1,final float y1,final float x2,final float y2){
		final float dx = x1-x2;
		final float dy = y1-y2;
		return dx*dx+dy*dy;
	}
	
	public static float distance(final float x1,final float y1,final float x2,final float y2,
			final float testX, final float testY){
		if(x1==x2&&y1==y2){
			return distance(x1, y1, testX, testY);
		}
		
		final float dis1 = distance2(x1,y1,testX,testY);
		final float dis2 = distance2(x1,y1,x2,y2);
		final float dis3 = distance2(x2,y2,testX,testY);
		
		if(dis1+dis2<dis3){
			return (float)Math.sqrt(dis1);
		} else if(dis3+dis2<dis1){
			return (float)Math.sqrt(dis3);
		} else {
			final float area = Math.abs((testX*y1-x1*testY)+(x1*y2-x2*y1)+(x2*testY-testX*y2));
			final float dis = 2*area/(float)Math.sqrt(dis2);
			
			return dis;
		}
	}
	
	public static final float areaWithSigned(final int length, final float[]vs){
		if((length & 1) != 0){
			throw new IllegalArgumentException("length must be 2*n!");
		}
		if(length > vs.length){
			throw new IllegalArgumentException("length > vs.length!");
		}
		
		float lastX = vs[length-2];
		float lastY = vs[length-1];
		float area = 0;
		for(int i=0; i<length; i+=2){
			final float x = vs[i];
			final float y = vs[i+1];
			area += (x*lastY-lastX*y);
			lastX = x;
			lastY = y;
		}
		return area;
	}
	
	public static final float areaWithSigned(final float [] vs){
		return areaWithSigned(vs.length, vs);
	}
	
	public static final float area(final float [] vs){
		return Math.abs( areaWithSigned(vs) );
	}
	
	public static final float area(final int length, final float [] vs){
		return Math.abs( areaWithSigned(length, vs) );
	}
	
	@Deprecated
	public static final void symmetry(final float x1, final float y1, final float x2, final float y2,
			final float testX, final float testY, final float [] ret){
		
//		final float dx = x2-x1, dy = y2-y1;
		
		
	}
	
	public static void toFatherXY(
			final float fatherX, final float fatherY,
			final float fatherScaleX, final float fatherScaleY,
			final float fatherScaleCentreX, final float fatherScaleCentreY,
			final float fatherRotate, 
			final float fatherRotateCentreX, final float fatherRotateCentreY,
			final float testX, final float testY,
			final float [] ret){
		
		/**
		 * first scale , than rotate
		 */
		
		//scale
		ret[0] = (testX - fatherScaleCentreX)*fatherScaleX+fatherScaleCentreX;
		ret[1] = (testY - fatherScaleCentreY)*fatherScaleY+fatherScaleCentreY;
		
		/**
		 * rotate
		 * ^
		 * |
		 * |
		 * ------>
		 */
		final float dx = ret[0] - fatherRotateCentreX;
		final float dy = ret[1] - fatherRotateCentreY;
		final float rad = fatherRotate*MathCons.RAD_TO_DEG;
		final float cos = (float)Math.cos(rad);
		final float sin = (float)Math.sin(rad);
		
		ret[0] = dx*cos + dy*sin + fatherRotateCentreX;
		ret[1] = -dx*sin + dy*cos + fatherRotateCentreY;
		
		ret[0] += fatherX;
		ret[1] += fatherY;
	}
	
	public static void fromFatherXY(
			final float fatherX, final float fatherY,
			final float fatherScaleX, final float fatherScaleY,
			final float fatherScaleCentreX, final float fatherScaleCentreY,
			final float fatherRotate, 
			final float fatherRotateCentreX, final float fatherRotateCentreY,
			final float testX, final float testY,
			final float [] ret){
		
		ret[0] = testX - fatherX - fatherRotateCentreX;
		ret[1] = testY - fatherY - fatherRotateCentreY;
		
		final float rad = fatherRotate*MathCons.RAD_TO_DEG;
		final float cos = (float)Math.cos(rad);
		final float sin = (float)Math.sin(rad);
		
		final float dx = ret[0]*cos - ret[1]*sin;
		final float dy = ret[0]*sin + ret[1]*cos;
		
		ret[0] = dx + fatherRotateCentreX - fatherScaleCentreX;
		ret[1] = dy + fatherRotateCentreY - fatherScaleCentreY;
		
		if(fatherScaleX!=0f){//else could be any x
			ret[0] = ret[0]/fatherScaleX + fatherScaleCentreX;
		} 
		
		if(fatherScaleY!=0f){//else could be any y
			ret[1] = ret[1]/fatherScaleY + fatherScaleCentreY;
		} 
	}
	
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param length
	 * @param ret 
	 */
	public static void getSymmetry2Points(final float x1,final float y1,
			final float x2, final float y2, final float x3, final float y3, final float length,
			final float [] ret){
		
		final float rad1 = GraphHelper.getRadian(x1, y1, x2, y2);
		final float rad2 = GraphHelper.getRadian(x2, y2, x3, y3);
		final float radMid = MathCons.HALF_PI+(rad1+rad2)*0.5f;
		
		final float dr = (rad1-rad2)*0.5f;
		final float drCos = (float)Math.cos(dr);
		final float thickness = drCos==0?length:length/drCos;
		
		final float cos = (float)Math.cos(radMid);
		final float sin = (float)Math.sin(radMid);
		final float dx = thickness*cos;
		final float dy = thickness*sin;
		
		ret[0] = x2+dx;
		ret[1] = y2+dy;
		ret[2] = x2-dx;
		ret[3] = y2-dy;
		
	}
	
	public static void getSymmetry2PointsHead(final float x1,final float y1,
			final float x2, final float y2, final float length,
			final float [] ret){
		getSymmetry2Points(2*x1-x2, 2*y1-y2, x1,y1, x2,y2, length, ret);
	}
	
	public static void getSymmetry2PointsTail(final float x1,final float y1,
			final float x2, final float y2, final float length,
			final float [] ret){
		getSymmetry2Points(x1,y1, x2,y2, 2*x2-x1, 2*y2-y1, length, ret);
	}
	
	public static float getRadian(final float x1, final float y1, final float x2, final float y2){
		return (float)Math.atan2(y2-y1, x2-x1);
	}
	
	public static class CrossRet{
		public Vertex cross;
		public boolean isCrossed;
	}
	
	public static void main(String [] args){
		System.out.println(side(0,0,100,100,200,100));
		System.out.println(side(0,0,100,100,100,200));
		System.out.println(side(0,0,100,100,200,200));
		System.out.println(side(0,0,100,100,-200,-200));
	}
}
