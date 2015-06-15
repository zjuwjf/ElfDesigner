package elfEngine.basic.node.nodeAnimate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.PuppyNode.IOnIndexListener;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.basic.node.particle.modifier.MathHelper;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.MotionEvent;
import elfEngine.graphics.LineF;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLHelper;

public class MoveNode extends ElfNode{

	public MoveNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		setAlongPath();
	}
	
	protected boolean mIsInMoving = false; 
	public void setIsInMoving(boolean moving) { 
		mIsInMoving = moving;
	} 
	public boolean getIsInMoving() { 
		return mIsInMoving;
	} 
	protected final static int REF_IsInMoving = DEFAULT_SHIFT; 
	
	public void setPosition(final ElfPointf pos) { 
		super.setPosition(pos); 
	} 
	
	protected float [] mRateTable; 
	protected ElfPointf [] mPointsOnWay; 
	protected float mDistance; 
	protected float mRateOnWay = 0; 
	protected final ElfPointf mReturnPosition = new ElfPointf(); 
	protected int mLastIndexOnWay = -1; 
	protected float mSpeed = 30; 
	protected boolean mIsLoopPath = false; 
	
	protected float [] mModifierAlphaArr;
	
	public void setAlphaArray(float [] array) {
		mModifierAlphaArr = MathHelper.copyFloatArray(array);
	}
	
	public float [] getAlphaArray() {
		return MathHelper.copyFloatArray(mModifierAlphaArr);
	}
	
	public boolean getIsLoopPath() {
		return mIsLoopPath;
	}
	public void setIsLoopPath(boolean loop) {
		if(mIsLoopPath != loop) {
			mIsLoopPath = loop;
			setAlongPath();
		}
	}
	protected final static int REF_IsLoopPath = DEFAULT_SHIFT;
	
	public float getDistance() {
		return mDistance;
	}
	protected final static int REF_Distance = FACE_GET_SHIFT | XML_GET_SHIFT;
	
	public float getRateOnWay() {
		return mRateOnWay;
	}
	public void setRateOnWay(float rateOnWay) {
		mRateOnWay = rateOnWay;
	} 
	protected final static int REF_RateOnWay = DEFAULT_SHIFT;
	
	public void setSpeed(float speed) {
		mSpeed = speed;
	} 
	public float getSpeed() { 
		return mSpeed;
	} 
	protected final static int REF_Speed = DEFAULT_SHIFT;
	
	
	
	
	private void setAlongPath() {  
		final ElfPointf [] pointsOnWay = getLinePoints(); 
		mRateTable = null; 
		if(pointsOnWay != null && pointsOnWay.length >= 2) { 
			if(mIsLoopPath) { 
				mPointsOnWay = new ElfPointf[pointsOnWay.length + 1];
				for(int i=0; i<pointsOnWay.length; i++) {
					mPointsOnWay[i] = pointsOnWay[i];
				}
				mPointsOnWay[ pointsOnWay.length ] = pointsOnWay[0]; 
			} else { 
				mPointsOnWay = pointsOnWay;
			} 
			
			mRateTable = new float[mPointsOnWay.length]; 
			mRateTable[0] = 0;
			mDistance = 0;
			for(int i=1; i<mPointsOnWay.length; i++) { 
				final ElfPointf p0 = mPointsOnWay[i-1]; 
				final ElfPointf p1 = mPointsOnWay[i]; 
				mRateTable[i] = (float)Math.sqrt((p0.x-p1.x)*(p0.x-p1.x)+(p0.y-p1.y)*(p0.y-p1.y)) + mRateTable[i-1];
				mDistance = mRateTable[i];
			} 
			if(mDistance <= 0) {
				mRateTable = null;
				return ;
			} 
			for(int i=1; i<mPointsOnWay.length; i++) {
				mRateTable[i] /= mDistance;
			} 
		} else { 
			mPointsOnWay = null;
		}
	} 
	protected final static int REF_AlongPath = FACE_SET_SHIFT; 
	
	public void setDebug() {
		if(mPointsOnWay != null) {
			System.err.println("mPointsOnWay");
			for(ElfPointf p : mPointsOnWay) {
				System.err.println(""+p.x+","+p.y);
			}
		}
		if(mRateTable != null) {
			System.err.println("mRateTable");
			for(float p : mRateTable) {
				System.err.println(""+p);
			} 
		}
	}
	protected final static int REF_Debug = FACE_SET_SHIFT;
	
	protected IOnIndexListener mIOnIndexListener;
	public void setOnIndexListener(final IOnIndexListener onIndexListener) {
		mIOnIndexListener = onIndexListener;
	}
	
	private final static boolean isOnReversWay(final float rate) {
		final float mode = rate % 2; 
		return mode > 1;
	} 
	
	public void calc(final float pMsElapsed) {
		super.calc(pMsElapsed);
		
		if(mRateTable != null && mPointsOnWay != null) { 
			final float newRateOnWay = mRateOnWay + (mSpeed*pMsElapsed/1000f)/mDistance;
			final int index;
			if(mIsLoopPath) {
				index = getPositionByRate(mPointsOnWay, mRateTable, newRateOnWay, false, mReturnPosition); 
				
				if(mIOnIndexListener != null) {
					if(index > mLastIndexOnWay) {
						for(int i=mLastIndexOnWay; i<index; i++) {
							mIOnIndexListener.onPassIndex(i, true);
						}
					} else if(index < mLastIndexOnWay) {
						for(int i=mLastIndexOnWay; i<mPointsOnWay.length-2; i++) {
							mIOnIndexListener.onPassIndex(i, true);
						}
						for(int i=0; i<index; i++) {
							mIOnIndexListener.onPassIndex(i, true);
						} 
					} 
				} 
			} else { 
				index = getPositionByRate(mPointsOnWay, mRateTable, newRateOnWay, true, mReturnPosition); 
				final boolean isOldReverse = isOnReversWay(mRateOnWay);
				final boolean isNowReverse = isOnReversWay(newRateOnWay);
				if(mIOnIndexListener != null) {
					if(isOldReverse && !isNowReverse) {
						for(int i=mLastIndexOnWay-1; i>=0; i--) { 
							mIOnIndexListener.onPassIndex(i, false);
						} 
						for(int i=1; i<index; i++) { 
							mIOnIndexListener.onPassIndex(i, true);
						} 
					} else if(!isOldReverse && isNowReverse) { 
						for(int i=mLastIndexOnWay; i<mRateTable.length; i++) { 
							mIOnIndexListener.onPassIndex(i, true);
						}
						for(int i=mRateTable.length-2; i>=index; i--) { 
							mIOnIndexListener.onPassIndex(i, false);
						} 
					} else if(isOldReverse) {
						for(int i=mLastIndexOnWay-1; i>=index; i--) { 
							mIOnIndexListener.onPassIndex(i, false);
						} 
					} else {
						for(int i=mLastIndexOnWay; i<index; i++) { 
							mIOnIndexListener.onPassIndex(i, true); 
						} 
					} 
				} 
			} 
			
			this.setPosition(mReturnPosition);
			mRateOnWay = newRateOnWay;
			mLastIndexOnWay = index;
		} 
	}
	
	private boolean mEditShapeEnable = true;
	public void setEditTouchBoundsEnable(boolean enable) {
		mEditShapeEnable = enable;
	}
	public boolean getEditTouchBoundsEnable() {
		return mEditShapeEnable;
	}
	protected final static int REF_EditTouchBoundsEnable = DEFAULT_SHIFT;
	
	protected final ArrayList<ElfPointf> mLinePoints = new ArrayList<ElfPointf>(); 
	
	public ElfPointf [] getLinePoints() {
		final ElfPointf [] ret = new ElfPointf[mLinePoints.size()];
		mLinePoints.toArray(ret);
		return ret;
	} 
	
	public ElfPointf [] getLinePointsCopy() {
		final ElfPointf [] ret = new ElfPointf[mLinePoints.size()];
		for (int i=0; i<ret.length; i++) {
			ret[i] = new ElfPointf(mLinePoints.get(i));
		}
		return ret;
	} 
	
	public void setLinePoints(ElfPointf [] ps) {
		mLinePoints.clear();
		if(ps != null) { 
			for(ElfPointf p : ps) { 
				mLinePoints.add(p);
			} 
		} 
	} 
	protected final static int REF_LinePoints = DEFAULT_SHIFT;
	
	ElfPointf [] array(ArrayList<ElfPointf> list) { 
		ElfPointf [] ret = new ElfPointf [list.size()]; 
		list.toArray(ret); 
		return ret; 
	} 
	
	void arrayToList(ArrayList<ElfPointf> list, ElfPointf [] points) { 
		list.clear(); 
		for(ElfPointf p : points) { 
			list.add(p); 
		} 
	} 
	
	private final ElfColor mLineColor = new ElfColor(0,1,1,1); 
	private final ElfColor mPointColor = new ElfColor(1,0,0,1); 
	private final ElfColor mTextColor = new ElfColor(1,1,0,1); 
	
	private boolean mDrawLine = true; 
	private boolean mDrawPoint = true; 
	private boolean mDrawText = true; 
	
	public boolean getDrawLine() {
		return mDrawLine;
	}
	public void setDrawLine(boolean mDrawLine) {
		this.mDrawLine = mDrawLine;
	}
	protected final static int REF_DrawLine = DEFAULT_SHIFT;
	
	public boolean getDrawPoint() {
		return mDrawPoint;
	}
	public void setDrawPoint(boolean mDrawPoint) {
		this.mDrawPoint = mDrawPoint;
	}
	protected final static int REF_DrawPoint = DEFAULT_SHIFT;
	
	public boolean getDrawText() {
		return mDrawText;
	}
	public void setDrawText(boolean mDrawText) {
		this.mDrawText = mDrawText;
	}
	protected final static int REF_DrawText = DEFAULT_SHIFT;
	
	List<ElfPointf> getDrawPoints() { 
		final LinkedList<ElfPointf> list = new LinkedList<ElfPointf>();
		for(ElfPointf p : mLinePoints) {
			final ElfPointf ret = new ElfPointf();
			this.selfXYToChild(p.x, p.y, ret);
			list.add( ret );
		} 
		return list;
	} 
	
	List<ElfPointf> getOnLinePoints() {
		final LinkedList<ElfPointf> list = new LinkedList<ElfPointf>();
		list.addAll(mLinePoints); 
		return list;
	} 
	
	void remove(final ElfPointf point) {
		mLinePoints.remove(point);
	}
	
	void addPoint(final ElfPointf point, int index) {
		index = Math.max(index, 0);
		try {
			mLinePoints.add(index, point);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	public void drawSelf() { 
		super.drawSelf(); 
		if(this.getGLId() != 0 || !this.isSelected()) { 
			return ;
		} 
		GLHelper.glBindTexture(0); 
		GLHelper.glBlendFunc(BlendMode.BLEND.sourceBlendFunction, BlendMode.BLEND.destinationBlendFunction); 
		GL11.glEnable(GL11.GL_LINE_SMOOTH); 
		GL11.glLineWidth(1); 
//		GL11.glDisable(GL11.GL_LINE_STIPPLE); 
		GL11.glPointSize(8); 
		
		final List<ElfPointf> points = getDrawPoints();
		
		if(mDrawLine) { 
			GLHelper.glColor4f(mLineColor.red, mLineColor.green, mLineColor.blue, mLineColor.alpha); 
			GL11.glBegin(GL11.GL_LINE_STRIP);
			for(final ElfPointf p : points) { 
				GL11.glVertex3f(p.x, p.y, 0); 
			} 
			if(mIsLoopPath && points.size() > 2) { 
				final ElfPointf p = points.get(0);
				GL11.glVertex3f(p.x, p.y, 0); 
			}
			GL11.glEnd(); 
		} 

		if(mDrawPoint) {
			GLHelper.glColor4f(mPointColor.red, mPointColor.green, mPointColor.blue, mPointColor.alpha); 
			GL11.glBegin(GL11.GL_POINTS); 
			for(final ElfPointf p : points) { 
				GL11.glVertex3f(p.x, p.y, 0); 
			} 
			GL11.glEnd(); 
		} 
		
		if(mDrawText) { 
			int count = 0;
			for(final ElfPointf p : points) { 
				drawText(p.x, p.y, count++); 
			} 
		} 
	} 
	
	private final static ArrayList<TextNode> sTextNodeList = new ArrayList<TextNode>();
	void drawText(float x, float y, final int num) {
		int size = 0;
		while ((size = sTextNodeList.size()) < num + 1) {
			final TextNode textNode = new TextNode(null, 0);
			textNode.setGLid(this.getGLId());
			textNode.setTextSize(15);
			textNode.setColor(new ElfColor(0, 0, 1, 1));
			textNode.setText("" + size);
			textNode.calcSprite(0);
			sTextNodeList.add(textNode);
		} 

		final TextNode textNode = sTextNodeList.get(num);
		textNode.setColor(mTextColor); 
		textNode.setPosition(x + 3, y + 18);
		textNode.drawSprite(); 
	}
	
	ElfPointf getExistedPoint(ElfPointf p0, final List<ElfPointf> ps) {
		final float threshold = 6;
		if (p0 != null) {
			for (final ElfPointf p : ps) {
				if (Math.abs(p0.x - p.x) < threshold && Math.abs(p0.y - p.y) < threshold) {
					return p;
				}
			}
		}
		return null;
	} 
	private ElfPointf mDragPoint = null; 
	
	public boolean onPreSelectTouchSelf(ElfEvent event) {
		if (mEditShapeEnable && isSelected()) {
			if (event.action == MotionEvent.LEFT_DOUBLE_CLICK) { 
				mDragPoint = null;
				if(mDrawPoint == false) {
					return false;
				} 
				// add or delete 
				// 1.delete 
				final ElfPointf ret = new ElfPointf(); 
				this.screenXYToSelf(event.x, event.y, ret);
				
				final List<ElfPointf> drawPoints = getOnLinePoints();
				final ElfPointf del = getExistedPoint(ret, drawPoints); 
				if (del != null) {  
					remove(del); 
					setAlongPath();
					return true; 
				} 
				
				// add on line
				final float threshold = 6;
				
				final List<ElfPointf> linePoints = getOnLinePoints();
				final int index = addPointOnLine(ret, threshold, linePoints);
				if (index != -1) {
					addPoint(ret, index); 
					setAlongPath();
					return true;
				} 
				
				// add last
				addPoint(ret, linePoints.size()); 
				setAlongPath();
				return true; 
			} else if (event.action == MotionEvent.LEFT_CLICK) {
				// noting
			} else if (event.action == MotionEvent.LEFT_DOWN) {
				final ElfPointf ret = new ElfPointf(); 
				this.screenXYToSelf(event.x, event.y, ret);
				
				if(mDrawPoint) {
					final List<ElfPointf> drawPoints = getOnLinePoints();
					mDragPoint = getExistedPoint(ret, drawPoints); 
					if (mDragPoint != null) { 
						setAlongPath();
						return true;
					} 
				} 
			} else if (event.action == MotionEvent.LEFT_MOVE) {
				if (mDragPoint != null) { 
					final ElfPointf ret = new ElfPointf(); 
					this.screenXYToSelf(event.x, event.y, ret);
					mDragPoint.set(ret);
					setAlongPath();
					return true; 
				} 
			} else if (event.action == MotionEvent.LEFT_UP) {
				if (mDragPoint != null) { 
					mDragPoint = null; 
					setAlongPath();
					return true;
				} else { 
					return false;
				} 
			} 
		} 
		
		return false; 
	} 
	
	private final int addPointOnLine(final ElfPointf currPoint, float dis, final List<ElfPointf> ps) {
		final LineF line = new LineF(0, 0, 0, 0);
		ElfPointf firstPoint = null;
		Iterator<ElfPointf> it = ps.iterator();
		int index = -1;
		
		while (it.hasNext()) {
			final ElfPointf p = it.next();
			line.p1 = line.p2;
			line.p2 = p;
			index++;
			
			if (line.p1 != null && line.p2 != null && line.checkPoint(currPoint, dis)) { 
				return index; 
			} 
			
			if (firstPoint == null) { 
				firstPoint = p;
			} 
		} 
		return -1; 
	} 
	
	//
	public static int getPositionByRate(final ElfPointf [] pos, final float [] rateTable, float rate, final boolean revers, final ElfPointf ret) { 
		if(rateTable == null || rateTable.length < 2 ) {
			return -1;
		} 
		
		if(rate < 0) {
			rate = 0;
		} else if(rate > 1) {
			if(revers) { 
				rate = rate%2; 
				rate = rate>1? 2-rate:rate; 
			} else { 
				rate = rate%2; 
				rate = rate>1? rate - 1:rate; 
			} 
		} 
		
		for(int i=0; i<rateTable.length; i++) { 
			if(rateTable[i] >= rate) {
				if(i==0) { 
					ret.set(pos[i]); 
					return 1;
				} else { 
					final ElfPointf p0 = pos[i-1];
					final ElfPointf p1 = pos[i];
					rate -= rateTable[i-1];
					final float rateSize = rateTable[i]-rateTable[i-1]; 
					if(rateSize > 0) { 
						rate /= rateSize;
					}
					ret.setPoint(p0.x*(1-rate) + p1.x*rate, p0.y*(1-rate) + p1.y*rate);
				} 
				return i; 
			} 
		} 
		
		return rateTable.length-1; 
	}
	
}
