package elfEngine.basic.node;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.ElfPointi;

import elfEngine.opengl.GLHelper;

public class GenieTest2 extends ElfNode {
	
	public GenieTest2(ElfNode father, int ordinal) {
		super(father, ordinal);

		calc(0);
	}

	protected ElfPointi mGridSize = new ElfPointi(10, 10);
	protected float mBlockSize = 50;
	protected ElfPointf[][] mGridPoints = new ElfPointf[11][11];
	
	public void setGridSize(final ElfPointi size) {
		if(size != null && size.x > 0 && size.y > 0) {
			mGridSize = size;
			mGridPoints = new ElfPointf[size.x+1][size.y+1];
			calc(0);
		}
	}
	public ElfPointi getGridSize() {
		return mGridSize;
	}
	protected final static int REF_GridSize = DEFAULT_SHIFT;

	protected float mProgress = 0;
	protected float mLife = 1;

	public void setProgress(float progress) {
		mProgress = progress;
	}

	public float getProgress() {
		return mProgress;
	}

	protected final static int REF_Progress = DEFAULT_SHIFT;

	public void setBlockSize(float size) {
		mBlockSize = size;
	}

	public float getBlockSize() {
		return mBlockSize;
	}

	protected final static int REF_BlockSize = DEFAULT_SHIFT;

	public void calc(float dt) {
		super.calc(dt);
		final float t = mProgress/mLife;
		update(t);
	}
	//

	public void drawSelf() {
		GLHelper.glBindTexture(0);

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(1);
		GL11.glDisable(GL11.GL_LINE_STIPPLE);

		GL11.glBegin(GL11.GL_LINES);
		for (int i = 0; i <= mGridSize.x; i++) {
			for (int j = 0; j <= mGridSize.y; j++) {
				if (i < mGridSize.x) {
					final ElfPointf p1 = getPos(i, j);
					final ElfPointf p2 = getPos(i + 1, j);
					GL11.glVertex3f(p1.x, p1.y, 0);
					GL11.glVertex3f(p2.x, p2.y, 0);
				}

				if (j < mGridSize.y) {
					final ElfPointf p1 = getPos(i, j);
					final ElfPointf p2 = getPos(i, j + 1);
					GL11.glVertex3f(p1.x, p1.y, 0);
					GL11.glVertex3f(p2.x, p2.y, 0);
				}
			}
		}
		GL11.glEnd();
		
		final float depthA = mBlockSize*mGridSize.y, depthB = depthA*mRate;
		ElfPointf fa=getPosInit(0, 0), fd=new ElfPointf(mOffX+fa.x*mRate, -fa.y-mSpace); 
		ElfPointf sa=getPosInit(mGridSize.x, 0), sd=new ElfPointf(mOffX+sa.x*mRate, -sa.y-mSpace);
		
		GLHelper.glColor4f(1, 0, 0, 1);
		GL11.glBegin(GL11.GL_LINES);
		
		GL11.glVertex2f(fd.x, fd.y);
		GL11.glVertex2f(sd.x, sd.y);
		
		GL11.glVertex2f(fd.x, fd.y);
		GL11.glVertex2f(fd.x, fd.y-depthB);
		
		GL11.glVertex2f(sd.x, sd.y);
		GL11.glVertex2f(sd.x, sd.y-depthB);
		
		GL11.glVertex2f(fd.x, fd.y-depthB);
		GL11.glVertex2f(sd.x, sd.y-depthB);
		
		GL11.glEnd();
	}
	

	ElfPointf getPos(int i, int j) {
		return mGridPoints[i][j];
	}

	void setPos(int i, int j, float x, float y) {
		mGridPoints[i][j] = new ElfPointf(x, y);
	}

	ElfPointf getPosInit(int i, int j) {
		return new ElfPointf(mBlockSize * (-mGridSize.x / 2f + i), mBlockSize * (mGridSize.y / 2f - j));
	}

	float mRate = 0.25f;
	float mSpace = 100;
	float mOffX = 0;
	public float getRate() {
		return mRate;
	}
	public void setRate(float mRate) {
		this.mRate = mRate;
	}
	protected final static int REF_Rate = DEFAULT_SHIFT;
	public float getSpace() {
		return mSpace;
	}
	public void setSpace(float mSpace) {
		this.mSpace = mSpace;
	}
	protected final static int REF_Space = DEFAULT_SHIFT;
	public float getOffX() {
		return mOffX;
	}
	public void setOffX(float offx) {
		this.mOffX = offx;
	}
	protected final static int REF_OffX = DEFAULT_SHIFT;
	// Bezier
	protected float mCurveAnimationStart = 0, mCurveAnimationEnd = 0.4f; 
	protected float mSlideAnimationStart = 0.3f, mSlideAnimationEnd = 1; 
	
	boolean mBezierX;
	public void setBezierX(boolean b) {
		mBezierX = b;
	}
	public boolean getBezierX() {
		return mBezierX;
	}
	protected final static int REF_BezierX = DEFAULT_SHIFT;
	
	//bezier, Axis
	void update(final float t) { 
		//dowm
		final float depthA = mBlockSize*mGridSize.y, depthB = depthA*mRate;
		
		ElfPointf fa=getPosInit(0, 0), fd=new ElfPointf(mOffX+fa.x*mRate, -fa.y-mSpace); 
		ElfPointf fb,fc;
		if(mBezierX) {
			fb=new ElfPointf(fa.x,(fa.y+fd.y)/2);
			fc=new ElfPointf(fd.x,(fa.y+fd.y)/2);
		} else {
			fb=new ElfPointf((fa.x+fd.x)/2,fa.y);
			fc=new ElfPointf((fa.x+fd.x)/2,fd.y);
		}
		
		ElfPointf sa=getPosInit(mGridSize.x, 0), sd=new ElfPointf(mOffX+sa.x*mRate, -sa.y-mSpace);
		
		ElfPointf sb,sc;
		if(mBezierX) {
			sb=new ElfPointf(sa.x,(sa.y+sd.y)/2);
			sc=new ElfPointf(sd.x,(sa.y+sd.y)/2);
		} else {
			sb=new ElfPointf((sa.x+sd.x)/2,sa.y);
			sc=new ElfPointf((sa.x+sd.x)/2,sd.y);
		}
		
		//f, s
		for(int j=0; j<=mGridSize.y; j++) { 
			final ElfPointf firstOrigin = getPosInit(0, j);
			final ElfPointf secondOrigin = getPosInit(mGridSize.x, j);
			
			final float originY = firstOrigin.y;
			final float y = getAxisPos(originY,fa.y,fd.y,depthA,depthB,t);
			final float bezierT;
			if(y < fd.y) {
				bezierT = 1;
			} else {
				bezierT = (float)bezierAxisIntersection(fa.y,fb.y,fc.y,fd.y, y);
			} 
			
			final float firstX = getBezier(firstOrigin.x, fa.x, fb.x, fc.x, fd.x, bezierT, t);
			final float secondX = getBezier(secondOrigin.x, sa.x, sb.x, sc.x, sd.x, bezierT, t);
			
			for(int i=0; i<=mGridSize.x; i++) { 
				final float r = (float)i/mGridSize.x;
				final float mx = firstX*(1-r) + secondX*r;
				this.setPos(i, j, mx, y); 
			} 
		} 
	} 
	
	final float getBezier(float origin, float a, float b, float c, float d, float bezierT, float t) {
		final float v = bezierat(a,b,c,d, bezierT);//? not t
		float curveT = progressOfSegmentWithinTotalProgress(mCurveAnimationStart, mCurveAnimationEnd, t); 
		return easeInOutInterpolate(curveT, origin, v);
	}
	
	final float getAxisPos(float origin, float a, float d, float depthA, float depthB, float t) { 
		final float slideT = progressOfSegmentWithinTotalProgress(mSlideAnimationStart, mSlideAnimationEnd, t); 
		float dv = (d-a)*slideT;
		float ret = origin + dv; 
		// a -> origin -> d -> d+depth
		if(d > a) { 
			if(ret > d) {
				return d+(ret-d)*depthB/depthA;
			}
		} else {//a > d
			if(ret < d) {
				return d+(ret-d)*depthB/depthA;
			} 
		} 
		return ret;
	}
	
	static float easeInOutInterpolate(float t, float a, float b) {
	    float val = a + t*t*(3.0f - 2.0f*t)*(b - a);
	    return b > a ? Math.max(a,  Math.min(val, b)) : Math.max(b,  Math.min(val, a)); // clamping, since numeric precision might bite here
	}

	static float progressOfSegmentWithinTotalProgress(float a, float b, float t) {
		return (float) Math.min(Math.max(0.0, (t - a) / (b - a)), 1.0);
	}
	
	// Bezier
	static float bezierat(float a, float b, float c, float d, float t) {
		return (float) (Math.pow(1 - t, 3) * a + 3 * t * (Math.pow(1 - t, 2)) * b + 3 * Math.pow(t, 2) * (1 - t) * c + Math.pow(t, 3) * d);
	}
	
	static double bezierAxisIntersection(float a, float b, float c, float d, float axisPos) {
		// axisPos > a && axisPos < b
		double t = (axisPos - a) / (d - a); // first approximation - treating
											// curve as linear segment
		if(t < 0) {
			t = 0;
		} else if(t > 1) {
			t = 1;
		}
		
		//0 -> 1
		final int kIterations = 3; // Newton-Raphson iterations

		for (int i = 0; i < kIterations; i++) {
			double nt = 1.0 - t;
			
			double f = nt * nt * nt * a + 3.0 * nt * nt * t * b + 3.0 * nt * t * t * c + t * t * t * d - axisPos;
			double df = -3.0 * (a * nt * nt + b * (-3.0 * t * t + 4.0 * t - 1.0) + t * (3.0 * c * t - 2.0 * c - d * t));
			
			t -= f / df;
		}
		return t;
	}
}