package elfEngine.basic.node.nodeTouch;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.ShowType;
import com.ielfgame.stupidGame.undo.UndoRedoManager;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.basic.touch.BasicEventDecoder;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.IElfOnTouch;
import elfEngine.basic.touch.MotionEvent;
import elfEngine.graphics.LineF;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLHelper;

public abstract class TouchNode extends ElfNode {

	public TouchNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setName("#touch");
		this.setTouchEnable(true);
	} 
	
	private boolean mPenetrate = false;
	public void setPenetrate(boolean penetrate) {
		mPenetrate = penetrate;
	}
	public boolean getPenetrate() {
		return mPenetrate;
	}
	protected final static int REF_Penetrate = DEFAULT_SHIFT;
	
	private BasicEventDecoder mTouchDecoder;
	public void setTouchDecoder(BasicEventDecoder decoder) {
		mTouchDecoder = decoder;
		decoder.setDelegate(this);
	} 
	public IElfOnTouch getTouchDecoder() {
		return mTouchDecoder;
	}
	
	public final boolean onTouch(ElfEvent event) {
		if(mTouchDecoder != null) {
			return mTouchDecoder.onTouch(event) && !mPenetrate;
		}
		return false;
	}
	
	private int mPriorityLevel = 10;
	public int getPriorityLevel() { 
		return mPriorityLevel; 
	} 
	public void setPriorityLevel(int mPriorityLevel) {
//		if (mPriorityLevel > 0) {
			this.mPriorityLevel = mPriorityLevel;
//		}
	}
	protected final static int REF_PriorityLevel = DEFAULT_SHIFT;

	public static final int BUTTON_PRIORITY = 128;
	public static final int TAB_PRIORITY = 128;
	public static final int CLICK_PRIORITY = 128;
	public static final int HOLD_PRIORITY = 128;
	public static final int ROTATE_PRIORITY = 128;
	public static final int LIST_PRIORITY = 256;
	public static final int MAP_PRIORITY = 256;
	public static final int SWIP_PRIORITY = 512;
	public static final int SHIELD_PRIORITY = 64;
	public static final int FOLLOW_PRIORITY = 64;
	public static final int INPUT_PRIORITY = 128;

	public String getPriorityLevelHelp() {
		String ret = "";
		ret += "1. PriorityLevel must be greater than 0.\n";
		ret += "2. The smaller and more priority.\n";
		ret += "3. Default:\n";
		ret += "\tButton " + BUTTON_PRIORITY + "\n";
		ret += "\tTab " + TAB_PRIORITY + "\n";
		ret += "\tClick " + CLICK_PRIORITY + "\n";
		ret += "\tHold " + HOLD_PRIORITY + "\n";
		ret += "\tRotate " + ROTATE_PRIORITY + "\n";
		ret += "\tList " + LIST_PRIORITY + "\n";
		ret += "\tMap " + MAP_PRIORITY + "\n";
		ret += "\tSwip " + SWIP_PRIORITY + "\n";
		ret += "\tShield " + SHIELD_PRIORITY + "\n";
		ret += "\tFollow " + FOLLOW_PRIORITY + "\n";
		ret += "\tInput " + INPUT_PRIORITY + "\n";

		return ret;
	}
	public void setPriorityLevelHelp(String info) {
	}
	protected final static int REF_PriorityLevelHelp = FACE_GET_SHIFT;
	
	public boolean isInSelectSize(final ElfEvent event) {
		if (mUseTouchBounds) { 
			final ElfPointf p = this.convertTouchToSelf(event);
			final ElfPointf[] ptPolygon = getTouchBounds();
			return PtInPolygon(p, ptPolygon, ptPolygon.length);
		} else {
			return super.isInSelectSize(event);
		}
	} 
	
	public void onArrowKey(final float dx, final float dy) {
		if(mEditTouchBoundsEnable && mShowType != ShowType.Hide) { 
			UndoRedoManager.checkInUndo();
			
			final ElfPointf ret = new ElfPointf();
			for(final ElfPointf p : mTouchBounds) { 
				this.childXYToScreen(p.x, p.y, ret); 
				ret.translate(dx, dy); 
				this.screenXYToChild(ret.x, ret.y, ret);
				p.set(ret); 
			} 
			
		} else {
			super.onArrowKey(dx, dy);
		}
	} 

	// bounds-----------------------------------------------------------
	private final ArrayList<ElfPointf> mTouchBounds = new ArrayList<ElfPointf>();

	public ElfPointf[] getTouchBounds() {
		ElfPointf[] ret = new ElfPointf[mTouchBounds.size()];
		mTouchBounds.toArray(ret);
		return ret;
	}
	
	public ElfPointf[] getTouchBoundsCopy() {
		ElfPointf[] ret = new ElfPointf[mTouchBounds.size()]; 
		for(int i=0; i<ret.length; i++) {
			ret[i] = new ElfPointf(mTouchBounds.get(i));
		}
		return ret;
	}

	public void setTouchBounds(final ElfPointf[] bounds) {
		if (bounds != null) {
			mTouchBounds.clear();
			for (final ElfPointf p : bounds) {
				if (p != null) {
					mTouchBounds.add(p);
				}
			}
		}
	}

	protected final static int REF_TouchBounds = DEFAULT_SHIFT;

	private ShowType mShowType = ShowType.Nums;

	public ShowType getShowType() {
		return mShowType;
	}

	public void setShowType(ShowType showType) {
		this.mShowType = showType;
	}

	protected final static int REF_ShowType = DEFAULT_SHIFT;

	private boolean mUseTouchBounds = false;

	public void setUseTouchBounds(boolean useTouchBounds) {
		mUseTouchBounds = useTouchBounds;
	}

	public boolean getUseTouchBounds() {
		return mUseTouchBounds;
	}

	protected final static int REF_UseTouchBounds = DEFAULT_SHIFT;

	private boolean mEditTouchBoundsEnable = false;

	public void setEditTouchBoundsEnable(boolean enable) {
		mEditTouchBoundsEnable = enable;
	}

	public boolean getEditTouchBoundsEnable() {
		return mEditTouchBoundsEnable;
	}

	protected final static int REF_EditTouchBoundsEnable = DEFAULT_SHIFT;

	public void drawSelf() {
		super.drawSelf();
		switch (mShowType) {
		case Nums:
			if (mTouchBounds != null) {
				int count = 0;
				for (final ElfPointf p : mTouchBounds) {
					drawText(p.x, p.y, count++);
				}
			}
		case Points:
			if (mTouchBounds != null) {
				GLHelper.glBindTexture(0);
				GLHelper.glBlendFunc(BlendMode.BLEND.sourceBlendFunction, BlendMode.BLEND.destinationBlendFunction);

				GLHelper.glColor4f(0, 1, 1, 1);

				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glLineWidth(1);
				GL11.glDisable(GL11.GL_LINE_STIPPLE);

				GL11.glBegin(GL11.GL_LINE_LOOP);

				for (final ElfPointf p : mTouchBounds) {
					GL11.glVertex3f(p.x, p.y, 0);
				}
				GL11.glEnd();

				GLHelper.glColor4f(1, 0, 0, 1);

				GL11.glPointSize(8);
				GL11.glBegin(GL11.GL_POINTS);
				for (final ElfPointf p : mTouchBounds) {
					GL11.glVertex3f(p.x, p.y, 0);
				}
				GL11.glEnd();

			}
			break;
		case Hide:
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
		textNode.setPosition(x + 3, y + 18);
		textNode.drawSprite(); 
	}

	ElfPointf getExistedPoint(ElfPointf p0) {
		final float threshold = 6;
		if (p0 != null) {
			for (final ElfPointf p : mTouchBounds) {
				if (Math.abs(p0.x - p.x) < threshold && Math.abs(p0.y - p.y) < threshold) {
					return p;
				}
			}
		}
		return null;
	}

	private ElfPointf mDragPoint = null;

	public boolean onPreSelectTouchSelf(ElfEvent event) {
		if (mEditTouchBoundsEnable && isSelected() && mShowType != ShowType.Hide) {
			if (event.action == MotionEvent.LEFT_DOUBLE_CLICK) {
				mDragPoint = null;
				
				UndoRedoManager.checkInUndo();
				
				// add or delete
				// 1.delete
				final ElfPointf ret = convertTouchToSelf(event);
				final ElfPointf del = getExistedPoint(ret);
				if (del != null) {
					mTouchBounds.remove(del);
					return true;
				} 

				// add on line
				final float threshold = 6;
				if (addPointOnLine(ret, threshold)) {
					return true;
				}

				// add last
				mTouchBounds.add(ret);
				return true;

			} else if (event.action == MotionEvent.LEFT_CLICK) {
				// noting
			} else if (event.action == MotionEvent.LEFT_DOWN) {
				final ElfPointf ret = convertTouchToSelf(event);
				mDragPoint = getExistedPoint(ret);

				if (mDragPoint != null) {
					return true;
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
				} 
			}
		}
		return false;
	}

	boolean addPointOnLine(final ElfPointf currPoint, float dis) {
		final LineF line = new LineF(0, 0, 0, 0);
		ElfPointf firstPoint = null;
		Iterator<ElfPointf> it = mTouchBounds.iterator();
		int index = -1;

		while (it.hasNext()) {
			final ElfPointf p = it.next();
			line.p1 = line.p2;
			line.p2 = p;
			index++;

			if (line.p1 != null && line.p2 != null && line.checkPoint(currPoint, dis)) {
				mTouchBounds.add(index, currPoint);
				return true;
			}

			if (firstPoint == null) {
				firstPoint = p;
			}
		}

		if (index >= 2) {
			line.p1 = firstPoint;
			index++;
			if (line.p1 != null && line.p2 != null && line.checkPoint(currPoint, dis)) {
				mTouchBounds.add(index, currPoint);
				return true;
			}
		}
		return false;
	}

	boolean PtInPolygon(ElfPointf p, ElfPointf[] ptPolygon, int nCount) {
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
