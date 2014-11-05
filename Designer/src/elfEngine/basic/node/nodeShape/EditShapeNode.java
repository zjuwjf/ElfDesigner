package elfEngine.basic.node.nodeShape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.ShapeType;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.MotionEvent;
import elfEngine.graphics.LineF;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLHelper;

public class EditShapeNode extends ElfNode{

	public EditShapeNode(ElfNode father, int ordinal) { 
		super(father, ordinal);
		setName("#editor");
	} 
	
	protected ElfPointf[] getControlPointsCopy() {
		final ElfPointf [] ret = new ElfPointf[mControlPoints.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = new ElfPointf(mControlPoints.get(i));
		}
		return ret;
	}

	protected ElfPointf[] getCurvePointsCopy() {
		final ElfPointf [] ret = new ElfPointf[mCurvePoints.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = new ElfPointf(mCurvePoints.get(i));
		}
		return ret;
	}

	protected ElfPointf[] getPointPointsCopy() {
		final ElfPointf [] ret = new ElfPointf[mPointPoints.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = new ElfPointf(mPointPoints.get(i));
		}
		return ret;
	}

	protected ElfPointf[] getLinePointsCopy() {
		final ElfPointf [] ret = new ElfPointf[mLinePoints.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = new ElfPointf(mLinePoints.get(i));
		}
		return ret;
	}

	protected ElfPointf[] getPolygonPointsCopy() {
		final ElfPointf [] ret = new ElfPointf[mPolygonPoints.size()];
		for(int i=0; i<ret.length; i++) {
			ret[i] = new ElfPointf(mPolygonPoints.get(i));
		}
		return ret;
	}

	protected ElfPointf getRectanglePointCopy() {
		return new ElfPointf(mRectanglePoint);
	}

	protected ElfPointf getCirclePointCopy() {
		return new ElfPointf(mCirclePoint);
	}
	
	private ShapeType mShapeType = ShapeType.Polygon;
	public ShapeType getShapeType() { 
		return mShapeType;
	}
	public void setShapeType(ShapeType type) {
		if(type!= null) {
			mShapeType = type;
		}
	} 
	protected final static int REF_ShapeType = DEFAULT_SHIFT;
	
	private boolean mEditShapeEnable = true;
	public void setEditTouchBoundsEnable(boolean enable) {
		mEditShapeEnable = enable;
	}
	public boolean getEditTouchBoundsEnable() {
		return mEditShapeEnable;
	}
	protected final static int REF_EditTouchBoundsEnable = DEFAULT_SHIFT;
	//Circle 1 point 
	//Rectangle 1 point 
	//Polygon n point 
	//Line n point 
	//Curve n point 
	//Point n point 
	
	protected final ElfPointf mCirclePoint = new ElfPointf(); 
	protected final ElfPointf mRectanglePoint = new ElfPointf(); 
	
	public ElfPointf getCirclePoint() {
		return mCirclePoint;
	}
	public void setCirclePoint(ElfPointf p) {
		mCirclePoint.set(p);
	}
	protected final static int REF_CirclePoint = DEFAULT_SHIFT;
	
	public ElfPointf getRectanglePoint() {
		return mRectanglePoint;
	}
	public void setRectanglePoint(ElfPointf p) {
		mRectanglePoint.set(p);
	}
	protected final static int REF_RectanglePoint = DEFAULT_SHIFT;
	
	protected final ArrayList<ElfPointf> mPolygonPoints = new ArrayList<ElfPointf>(); 
	protected final ArrayList<ElfPointf> mPointPoints = new ArrayList<ElfPointf>(); 
	
	protected final ArrayList<ElfPointf> mLinePoints = new ArrayList<ElfPointf>(); 
	protected final ArrayList<ElfPointf> mCurvePoints = new ArrayList<ElfPointf>(); 
	protected final ArrayList<ElfPointf> mControlPoints = new ArrayList<ElfPointf>(); 
	
	public ElfPointf [] getPolygonPoints() {
		final ElfPointf [] ret = new ElfPointf[mPolygonPoints.size()];
		mPolygonPoints.toArray(ret);
		return ret;
	} 
	public void setPolygonPoints(ElfPointf [] ps) {
		mPolygonPoints.clear();
		if(ps != null) { 
			for(ElfPointf p : ps) { 
				mPolygonPoints.add(p);
			} 
		} 
	} 
	protected final static int REF_PolygonPoints = DEFAULT_SHIFT;
	
	public ElfPointf [] getLinePoints() {
		final ElfPointf [] ret = new ElfPointf[mLinePoints.size()];
		mLinePoints.toArray(ret);
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
	
	public ElfPointf [] getPointPoints() {
		final ElfPointf [] ret = new ElfPointf[mPointPoints.size()];
		mPointPoints.toArray(ret);
		return ret;
	} 
	public void setPointPoints(ElfPointf [] ps) {
		mPointPoints.clear();
		if(ps != null) { 
			for(ElfPointf p : ps) { 
				mPointPoints.add(p);
			} 
		} 
	} 
	protected final static int REF_PointPoints = DEFAULT_SHIFT;
	
	public ElfPointf [] getCurvePoints() {
		final ElfPointf [] ret = new ElfPointf[mCurvePoints.size()];
		mCurvePoints.toArray(ret);
		return ret;
	} 
	public void setCurvePoints(ElfPointf [] ps) {
		mCurvePoints.clear();
		if(ps != null) { 
			for(ElfPointf p : ps) { 
				mCurvePoints.add(p);
			} 
		} 
	} 
	protected final static int REF_CurvePoints = FACE_GET_SHIFT | XML_ALL_SHIFT;
	
	public ElfPointf [] getControlPoints() { 
		final ElfPointf [] ret = new ElfPointf[mControlPoints.size()];
		mControlPoints.toArray(ret);
		return ret;
	} 
	public void setControlPoints(ElfPointf [] ps) {
		mControlPoints.clear();
		if(ps != null) { 
			for(ElfPointf p : ps) { 
				mControlPoints.add(p);
			} 
		} 
	} 
	protected final static int REF_ControlPoints = FACE_GET_SHIFT | XML_ALL_SHIFT;
	
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
	private final ElfColor mControlColor = new ElfColor(0,1,0,1); 
	private final ElfColor mTextColor = new ElfColor(1,1,0,1); 
	
	private boolean mDrawLine = true; 
	private boolean mDrawPoint = true; 
	private boolean mDrawControl = true; 
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
	
	public boolean getDrawControl() {
		return mDrawControl;
	}
	public void setDrawControl(boolean mDrawControl) {
		this.mDrawControl = mDrawControl;
	}
	protected final static int REF_DrawControl = DEFAULT_SHIFT;
	
	public boolean getDrawText() {
		return mDrawText;
	}
	public void setDrawText(boolean mDrawText) {
		this.mDrawText = mDrawText;
	}
	protected final static int REF_DrawText = DEFAULT_SHIFT;
	
	List<ElfPointf> getDrawPoints() { 
		final LinkedList<ElfPointf> list = new LinkedList<ElfPointf>();
		switch(mShapeType) {
		case Circle: list.add(mCirclePoint);break;
		case Rectangle: list.add(mRectanglePoint); break;
		case Curve: list.addAll(mCurvePoints); break;
		case Line: list.addAll(mLinePoints); break; 
		case Points: list.addAll(mPointPoints); break;
		case Polygon: list.addAll(mPolygonPoints); break;
		}
		return list;
	} 
	
	List<ElfPointf> getShowControlPoints() {
		final LinkedList<ElfPointf> list = new LinkedList<ElfPointf>();
		switch(mShapeType) {
		case Curve: list.addAll(mControlPoints); break; 
		}
		return list;
	} 
	
	final int mPrecision = 50;
	
	List<ElfPointf> getOnLinePoints() {
		final LinkedList<ElfPointf> list = new LinkedList<ElfPointf>();
		try {
			switch(mShapeType) { 
			case Circle: 
				for(int i=0; i<mPrecision+1; i++) {
					final float angle = (float)Math.PI*2*i/mPrecision;
					final float x = (float)Math.cos(angle) * mCirclePoint.x; 
					final float y = (float)Math.sin(angle) * mCirclePoint.y; 
					list.add(new ElfPointf(x, y)); 
				}
				break; 
			case Rectangle: 
				list.add(new ElfPointf(mRectanglePoint.x, mRectanglePoint.y)); 
				list.add(new ElfPointf(mRectanglePoint.x, -mRectanglePoint.y)); 
				list.add(new ElfPointf(-mRectanglePoint.x, -mRectanglePoint.y)); 
				list.add(new ElfPointf(-mRectanglePoint.x, mRectanglePoint.y)); 
				list.add(new ElfPointf(mRectanglePoint.x, mRectanglePoint.y)); 
				break; 
			case Curve: 
				final int count = mCurvePoints.size(); 
				for(int i=1; i<count; i++) { 
					final ElfPointf p0 = mCurvePoints.get(i-1); 
					final ElfPointf c =  mControlPoints.get(i); 
					final ElfPointf p1 = mCurvePoints.get(i); 
					for(int k=i==1?0:1; k<mPrecision; k++) { 
						final float r = k/(mPrecision-1f); 
						final float r20 = (1-r)*(1-r); 
						final float r21 = 2*r*(1-r); 
						final float r22 = r*r; 
						final float x = p0.x*r20 + c.x*r21 + p1.x*r22; 
						final float y = p0.y*r20 + c.y*r21 + p1.y*r22; 
						list.add(new ElfPointf(x, y)); 
					}  
				} 
				break; 
			case Line: 
				list.addAll(mLinePoints); 
				break; 
			case Polygon: 
				list.addAll(mPolygonPoints); 
				if(mPolygonPoints.size() > 0) {
					list.add(mPolygonPoints.get(0));
				} 
				break; 
			}
		} catch (Exception e) {
		} 
		
		return list;
	} 
	
	void remove(final ElfPointf point) {
		int index;
		switch(mShapeType) {
		case Curve: 
			index = mCurvePoints.indexOf(point);
			if(index != -1) {
				mCurvePoints.remove(index);
				mControlPoints.remove(index);
			} break;
		case Line:
			mLinePoints.remove(point); break;
		case Points:
			mPointPoints.remove(point); break;
		case Polygon:
			mPolygonPoints.remove(point); break;
		case Rectangle:
		case Circle:
		}
	}
	
	void addPoint(final ElfPointf point, int index) {
		index = Math.max(index, 0);
		try {
			switch(mShapeType) {
			case Curve: 
				mCurvePoints.add(index, point);
				if(index == 0) {
					mControlPoints.add(index, new ElfPointf(point));
				} else {
					final ElfPointf p = mCurvePoints.get(index-1);
					mControlPoints.add(index, new ElfPointf((point.x+p.x)/2, (point.y+p.y)/2)); 
				} 
				break;
			case Line:
				mLinePoints.add(index, point); break;
			case Points:
				mPointPoints.add(index, point); break;
			case Polygon:
				mPolygonPoints.add(index, point); break;
			case Rectangle:
			case Circle:
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	//show 
	//1. point 
	//2. control 
	//3. text 
	//4. line 
	
	public void drawSelf() { 
		super.drawSelf(); 
		
		GLHelper.glBindTexture(0); 
		GLHelper.glBlendFunc(BlendMode.BLEND.sourceBlendFunction, BlendMode.BLEND.destinationBlendFunction); 
		GL11.glEnable(GL11.GL_LINE_SMOOTH); 
		GL11.glLineWidth(1); 
//		GL11.glDisable(GL11.GL_LINE_STIPPLE); 
		GL11.glPointSize(8); 
		
		if(mDrawLine) {
			GLHelper.glColor4f(mLineColor.red, mLineColor.green, mLineColor.blue, mLineColor.alpha); 
			GL11.glBegin(GL11.GL_LINE_STRIP);
			final List<ElfPointf> lines = getOnLinePoints();
			for(final ElfPointf p : lines) { 
				GL11.glVertex3f(p.x, p.y, 0); 
			} 
			GL11.glEnd(); 
		} 

		if(mDrawPoint) {
			GLHelper.glColor4f(mPointColor.red, mPointColor.green, mPointColor.blue, mPointColor.alpha); 
			GL11.glBegin(GL11.GL_POINTS); 
			final List<ElfPointf> points = getDrawPoints();
			for(final ElfPointf p : points) { 
				GL11.glVertex3f(p.x, p.y, 0); 
			} 
			GL11.glEnd(); 
		} 
		
		if(mDrawControl) { 
			GLHelper.glColor4f(mControlColor.red, mControlColor.green, mControlColor.blue, mControlColor.alpha); 
			GL11.glBegin(GL11.GL_POINTS); 
			final List<ElfPointf> points = getShowControlPoints();
			for(final ElfPointf p : points) { 
				GL11.glVertex3f(p.x, p.y, 0); 
			} 
			GL11.glEnd(); 
		} 
		
		if(mDrawText) { 
			List<ElfPointf> points = getDrawPoints();
			int count = 0;
			for(final ElfPointf p : points) { 
				drawText(p.x, p.y, count++); 
			} 
			
			points = getShowControlPoints(); 
			count = 0; 
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
				if(mShapeType == ShapeType.Rectangle || mShapeType == ShapeType.Circle || mDrawPoint == false) {
					return false;
				} 
				// add or delete 
				// 1.delete 
				final ElfPointf ret = convertTouchToSelf(event); 
				final List<ElfPointf> drawPoints = getDrawPoints();
				final ElfPointf del = getExistedPoint(ret, drawPoints); 
				if (del != null) {  
					remove(del); 
					return true; 
				} 
				
				// add on line
				final float threshold = 6;
				if(mShapeType == ShapeType.Curve) {
					final List<ElfPointf> linePoints = getOnLinePoints();
					final int index = addPointOnLine(ret, threshold, linePoints);
					if (index != -1) {
						addPoint(ret, 1+index/mPrecision); 
						return true;
					} 
				} 
				
				final List<ElfPointf> linePoints = getOnLinePoints();
				final int index = addPointOnLine(ret, threshold, linePoints);
				if (index != -1) {
					addPoint(ret, index); 
					return true;
				} 
				
				// add last
				if(mShapeType == ShapeType.Polygon) { 
					addPoint(ret, mPolygonPoints.size()); 
				} else if(mShapeType == ShapeType.Curve) { 
					addPoint(ret, mCurvePoints.size()); 
				} else if(mShapeType == ShapeType.Points) {
					addPoint(ret, mPointPoints.size()); 
				} else {
					addPoint(ret, linePoints.size()); 
				} 
				return true; 
			} else if (event.action == MotionEvent.LEFT_CLICK) {
				// noting
			} else if (event.action == MotionEvent.LEFT_DOWN) {
				final ElfPointf ret = convertTouchToSelf(event);
				if(mDrawControl) {
					final List<ElfPointf> controlPoint = getShowControlPoints();
					mDragPoint = getExistedPoint(ret, controlPoint);
					if (mDragPoint != null) { 
						return true;
					} 
				} 
				
				if(mDrawPoint) {
					final List<ElfPointf> drawPoints = getDrawPoints();
					mDragPoint = getExistedPoint(ret, drawPoints);
					if (mDragPoint != null) { 
						return true;
					} 
				} 
			} else if (event.action == MotionEvent.LEFT_MOVE) {
				if (mDragPoint != null) { 
					final ElfPointf ret = convertTouchToSelf(event);
					mDragPoint.set(ret);
					return true; 
				} 
			} else if (event.action == MotionEvent.LEFT_UP) {
				if (mDragPoint != null) { 
					mDragPoint = null; 
					return true;
				} else { 
					return false;
				}
			}
		}

		return false;
	} 
	
//	void saveAsOld() {
//		final ShapeType type = getShapeType();
//		final ElfPointf [] points = getPoints();
//		for(int i=0; i<points.length; i++) {
//			points[i] = new ElfPointf(points[i]);
//		} 
//		
//		UndoManager.checkInUndo(new Runnable() {
//			public void run() {
//				setShapeType(type);
//				setPoints(points);
//			}
//		}, null);
//	} 
//	void saveAsNew() {
//		final ShapeType type = getShapeType();
//		final ElfPointf [] points = getPoints();
//		for(int i=0; i<points.length; i++) { 
//			points[i] = new ElfPointf(points[i]);
//		}
//		
//		UndoManager.checkInUndo(null, new Runnable() {
//			public void run() {
//				setShapeType(type);
//				setPoints(points);
//			} 
//		});
//	}
	
	int addPointOnLine(final ElfPointf currPoint, float dis, final List<ElfPointf> ps) {
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

	final boolean PtInPolygon(ElfPointf p, ElfPointf[] ptPolygon, int nCount) {
		int nCross = 0;
		for (int i = 0; i < nCount; i++) {
			ElfPointf p1 = ptPolygon[i];
			ElfPointf p2 = ptPolygon[(i + 1) % nCount];
			if (p1.y == p2.y)
				continue;
			if (p.y < Math.min(p1.y, p2.y))
				continue;
			if (p.y >= Math.max(p1.y, p2.y))
				continue;
			double x = (double) (p.y - p1.y) * (double) (p2.x - p1.x) / (double) (p2.y - p1.y) + p1.x;
			if (x > p.x)
				nCross++;
		}
		return (nCross % 2 == 1);
	} 
} 
