package elfEngine.basic.node.nodeShape;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.graphics.PointF;
import elfEngine.opengl.GLHelper;

public class BezierNode extends ElfNode {
	
	public static class BezierAssistNode extends TextNode {
		public BezierAssistNode(ElfNode father, int ordianl) {
			super(father, ordianl); 
			setName("#assist"); 
			setTextSize(15);
		} 
	} 
	
	
	public BezierNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setName("#bezier");
		this.setUseSettedSize(true);
		this.setSize(100, 100);
	} 
	
	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes();
		
		for(int i=0; i<mControlPoints.length; i++) {
			final BezierAssistNode node = new BezierAssistNode(this, 0);
			node.setPosition(mControlPoints[i]); 
			this.addChild(node, i+1); 
		} 
	} 
	
	public void onRecognizeRequiredNodes() { 
		super.onRecognizeRequiredNodes();
		final int [] count = new int[]{0};
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if(node instanceof BezierAssistNode) {
					count[0]++;
				} 
				return false;
			}
		});
		
		for(int i=count[0]; i<mControlPoints.length; i++) { 
			final BezierAssistNode node = new BezierAssistNode(this, i+1);
			node.setPosition(mControlPoints[i]); 
			this.addChild(node, i+1); 
		} 
	} 
	
	public void calc(final float pMsElapsed) { 
		super.calc(pMsElapsed); 
		
		final int [] count = new int[]{0}; 
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if(node instanceof BezierAssistNode) { 
					mControlPoints[count[0]].set(node.getPosition()); 
					
					node.setVisible(mShowControlPoints); 
					
					((BezierAssistNode) node).setText("p"+count[0]); 
					node.setName("#assist"+count[0]); 
					count[0]++; 
				} 
				return false; 
			} 
		}); 
		
		if(mAutoProgress && mProgress < 1) {
			mProgress += pMsElapsed/mAutoProgressTime;
		} else if(mAutoProgress && mProgress >= 1) {
			mProgress = 0;
		}
		
		update();
	} 

	private int mPrecision = 1000;
	public void setPrecision(int precision) {
		if (mPrecision != precision && precision > 1) {
			mPrecision = precision;
		} 
	} 
	public int getPrecision() {
		return mPrecision;
	}
	protected final static int REF_Precision = DEFAULT_SHIFT; 
	
	private float mLineWidth = 10;
	public void setLineWidth(float lineWidth) {
		if(mLineWidth != lineWidth) {
			mLineWidth = lineWidth;
		}
	}
	public float getLineWidth() {
		return mLineWidth;
	}
	protected final static int REF_LineWidth = DEFAULT_SHIFT;
	
	private boolean mIsDash = false;
	public void setIsDash(boolean dash) {
		mIsDash = dash;
	}
	public boolean getIsDash() {
		return mIsDash;
	}
	protected final static int REF_IsDash = DEFAULT_SHIFT; 
	
	private int mDashFactor = 1;
	
	public int getDashFactor() {
		return mDashFactor;
	}
	public void setDashFactor(int mDashFactor) {
		this.mDashFactor = mDashFactor;
	}
	protected final static int REF_DashFactor = DEFAULT_SHIFT; 
	
	private int mDashPattern = 0xff; 
	public int getDashPattern() { 
		return mDashPattern; 
	} 
	public void setDashPattern(int mDashPattern) {
		this.mDashPattern = mDashPattern;
	} 
	protected final static int REF_DashPattern = DEFAULT_SHIFT; 

	private final LinkedList<ElfPointf> mPoints = new LinkedList<ElfPointf>();
	
	private final ElfPointf [] mControlPoints = new ElfPointf[] {new ElfPointf(-100, 0), new ElfPointf(-50, 50), new ElfPointf(50, -50), new ElfPointf(100, 0)} ;
	private boolean mShowControlPoints = false; 
	
	public boolean getShowControlPoints() {
		return mShowControlPoints;
	}
	public void setShowControlPoints(boolean mShowControlPoints) {
		this.mShowControlPoints = mShowControlPoints;
	}
	protected final static int REF_ShowControlPoints = DEFAULT_SHIFT; 
	
	public void setControlPoints(final ElfPointf [] pos) {
		if(pos!= null) { 
			final int [] count = new int[]{0};
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) { 
					if(count[0]<pos.length && node instanceof BezierAssistNode) {
						node.setPosition(pos[count[0]]);
						count[0]++;
					} 
					return false;
				}
			});
			
			for(int i=count[0]; i<mControlPoints.length && i < pos.length; i++) {
				mControlPoints[i].set(pos[i]); 
			} 
		} 
	} 
	public ElfPointf[] getControlPoints() { 
		return mControlPoints; 
	} 
	protected final static int REF_ControlPoints = DEFAULT_SHIFT; 

	private float mProgress = 1;
	public void setProgress(float progress) { 
		mProgress = progress; 
	}
	public float getProgress() { 
		return mProgress; 
	}
	protected final static int REF_Progress = DEFAULT_SHIFT; 
	
	private boolean mAutoProgress = false;
	public boolean getAutoProgress() {
		return mAutoProgress;
	}
	public void setAutoProgress(boolean mAutoProgress) {
		this.mAutoProgress = mAutoProgress;
	}
	protected final static int REF_AutoProgress = DEFAULT_SHIFT; 
	
	private float mAutoProgressTime = 1000;
	public float getAutoProgressTime() {
		return mAutoProgressTime;
	}
	public void setAutoProgressTime(float mAutoProgress) {
		if(mAutoProgressTime > 10) {
			this.mAutoProgressTime = mAutoProgress;
		}
	}
	protected final static int REF_AutoProgressTime = DEFAULT_SHIFT; 

	void update() { 
		mPoints.clear(); 
		
		final PointF startP = mControlPoints[0];
		final PointF control1P = mControlPoints[1];
		final PointF control2p = mControlPoints[2];
		final PointF endP = mControlPoints[3];

		final float xa = startP.x;
		final float xb = control1P.x;
		final float xc = control2p.x;
		final float xd = endP.x;
		
		final float ya = startP.y; 
		final float yb = control1P.y; 
		final float yc = control2p.y; 
		final float yd = endP.y; 
		
		for (int i = 0; i < mPrecision + 1; i++) { 
			final float t = (float) i / mPrecision;
			if(t <= mProgress) {
				float x = bezierat(xa, xb, xc, xd, t);
				float y = bezierat(ya, yb, yc, yd, t);
				mPoints.add(new ElfPointf(x, y)); 
			} 
		}  
	} 

	public void drawSelf() {
		GLHelper.glBindTexture(0); 
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		
		//GL_ALIASED_LINE_WIDTH_RANGE
//		GL11.glGetIntegerv(GL11.gl_Al, );
		GL11.glLineWidth(mLineWidth); 
		
		if(mIsDash) { 
			GL11.glEnable(GL11.GL_LINE_STIPPLE); 
			GL11.glLineStipple(mDashFactor, (short)mDashPattern); 
		} else { 
			GL11.glDisable(GL11.GL_LINE_STIPPLE); 
		} 
		
		GL11.glBegin(GL11.GL_LINE_STRIP); 
		for (ElfPointf p : mPoints) { 
			GL11.glVertex3f(p.x, p.y, 0); 
		} 
		GL11.glEnd(); 
	} 

	static float bezierat(float a, float b, float c, float d, float t) {
		return (float) (Math.pow(1 - t, 3) * a + 3 * t * (Math.pow(1 - t, 2)) * b + 3 * Math.pow(t, 2) * (1 - t) * c + Math.pow(t, 3) * d);
	} 
} 
