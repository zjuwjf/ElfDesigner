package elfEngine.basic.node.nodeAnimate.timeLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

import com.ielfgame.stupidGame.ColorFactory;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.language.LanguageManager;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.APowerManSingleton;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ElfNode;

public class TimeCanvasView extends APowerManSingleton implements PaintListener, MouseListener, MouseMoveListener, KeyListener, MouseWheelListener {

	private final ArrayList<TimeData> mPositionKeyArray = new ArrayList<TimeData>();
	private final ArrayList<TimeData> mRotateKeyArray = new ArrayList<TimeData>();
	private final ArrayList<TimeData> mScaleKeyArray = new ArrayList<TimeData>();
	private final ArrayList<TimeData> mColorKeyArray = new ArrayList<TimeData>();
	private final ArrayList<TimeData> mVisibleKeyArray = new ArrayList<TimeData>();

	// private final ArrayList<TimeData> mGlobalKeyArray = new
	// ArrayList<TimeData>();
	// private final void getGlobalKeys() {
	//
	// }
	
	public ArrayList<ElfMotionKeyNode> getSelectedKeyNodesByTime(final int time) {
		
		final ArrayList<ElfMotionKeyNode> ret = new ArrayList<ElfMotionKeyNode>();
		
		if(mMotionNode != null) {
			final ArrayList<ElfNode> nodes = this.getMotionSelectNodes();
			for(final ElfNode node : nodes) {
				for(final NodePropertyType type : NodePropertyType.values()) {
					final ElfMotionKeyNode key = mMotionNode.findKeysByNodeAndTime(node, type.toString(), time);
					if(key != null) {
						ret.add(key);
					}
				}
			}
		}
		
		return ret;
	}

	ArrayList<TimeData> findKeyArrayByType(NodePropertyType type) {
		final Comparator<TimeData> comparator = new Comparator<TimeData>() {
			public int compare(TimeData o1, TimeData o2) {
				return o1.getTime() - o2.getTime();
			}
		};

		Collections.sort(mPositionKeyArray, comparator);
		Collections.sort(mColorKeyArray, comparator);
		Collections.sort(mRotateKeyArray, comparator);
		Collections.sort(mScaleKeyArray, comparator);
		Collections.sort(mVisibleKeyArray, comparator);

		// return global
		if(type == null) {
			final ArrayList<ElfNode> selectList = this.getMotionSelectNodes();
			final ArrayList<TimeData> globalKeyArray = new ArrayList<TimeData>();
			final HashSet<Integer> hashSet = new HashSet<Integer>();
			
			for (ElfNode node : selectList) {
				for (NodePropertyType nodetype : NodePropertyType.values()) {
					final ElfMotionKeyNode[] keys = mMotionNode.findKeysByNode(node, nodetype.toString());
					if (keys != null) {
						for (ElfMotionKeyNode key : keys) {
							hashSet.add(key.getTime());
						}
					}
				}
			}

			for (final Integer i : hashSet) {
				final TimeData data = TimeData.getGlobalTimeDataByTime(i);
				globalKeyArray.add(data);
			}

			Collections.sort(globalKeyArray, comparator);
			
			return globalKeyArray;
		}
		

		switch (type) {
		case ColorType:
			return mColorKeyArray;
		case PositionType:
			return mPositionKeyArray;
		case RotateType:
			return mRotateKeyArray;
		case ScaleType:
			return mScaleKeyArray;
		case VisibleType:
			return mVisibleKeyArray;
		}

		return null;
	}

	private final ArrayList<ElfNode> getMotionSelectNodes() {
		final ArrayList<ElfNode> selectList = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		final ArrayList<ElfNode> ret = new ArrayList<ElfNode>();

		if (this.mMotionNode != null && mMotionNode.getKeyStorageNode() != null) {
			for (ElfNode node : selectList) {
				if (mMotionNode.isRecurFatherOf(node) && node != mMotionNode.getKeyStorageNode() && !mMotionNode.getKeyStorageNode().isRecurFatherOf(node)) {
					ret.add(node);
				}
			}
		}

		return ret;
	}

	private int getKeyHeight(NodePropertyType type) {
		int ret = TimeLinetop;
		final int step = 30;

		if (type == null) {
			return ret + mOriginPoint.y + step * 6;
		}

		switch (type) {
		case VisibleType:
			ret += step;
		case ColorType:
			ret += step;
		case ScaleType:
			ret += step;
		case RotateType:
			ret += step;
		case PositionType:
			ret += step;
		}

		ret += mOriginPoint.y;

		return ret;
	}

	int getMaxHeight() {
		final int step = 30;
		return step * 5 + TimeLinetop;
	}

	public TimeCanvasView() {
	}

	private final static int LeftMargin = 80;
	private final static int LineMargin = 10;

	private final static int TimeLinetop = 40;
	private final static int CurrTop = 15;

	private Display mDisplay;
	private Canvas mCanvas;

	private final Point mOriginPoint = new Point(0, 0);
	private final TimeData mCurrKeyTime = new TimeData(1390);

	private final MotionNodeOnChangeListener mMotionNodeOnChangeListener = new MotionNodeOnChangeListener();

	private ElfNode mCurrentNode;
	private ElfMotionNode mMotionNode;

	public Canvas getCanvas() {
		return mCanvas;
	}

	public void setAutoKey(boolean autoKey) {
		mMotionNodeOnChangeListener.setAutoKey(autoKey);
	}

	public boolean getAutoKey() {
		return mMotionNodeOnChangeListener.getAutoKey();
	}

	public void loadMotionNode(ElfMotionNode motionNode) {
		if (mMotionNode != motionNode) {
			mMotionNodeOnChangeListener.bindMotion(motionNode);

			mMotionNode = motionNode;
			mCurrKeyTime.bind(mMotionNode);
			loadSelectNode(null);
		}
	}

	void loadSelectNode(ElfNode select) {
		// clear
		if (mCurrentNode != select) {
			TimeData.setSelectTimeData(null);

			for (NodePropertyType type : NodePropertyType.values()) {
				final ArrayList<TimeData> list = findKeyArrayByType(type);
				if (list != null) {
					list.clear();
				}
				if (select != null) {
					final ElfMotionKeyNode[] keys = mMotionNode.findKeysByNode(select, type.toString());
					if (keys != null) {
						for (ElfMotionKeyNode key : keys) {
							final TimeData data = new TimeData(key.getTime());
							data.bind(key);
							data.setType(type);
							list.add(data);
						}
					}
				}
			}
			mCurrentNode = select;
		}

		mMotionNodeOnChangeListener.bindNode(PowerMan.getSingleton(DataModel.class).getSelectNodeList());
	}

	void onSelectChange(ElfNode newSelect) {
		if (mMotionNode != null) {
			if (newSelect != null && !mMotionNode.isRecurFatherOfAndNotKeys(newSelect)) {
				newSelect = null;
			}
			loadSelectNode(newSelect);
		}
	}

	// data

	public Canvas create(final Composite parent) {
		mDisplay = parent.getDisplay();

		mCanvas = new Canvas(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);

		final TimeViewMenu timeViewMenu = new TimeViewMenu(this);
		mCanvas.setMenu(timeViewMenu.create(mCanvas.getShell()));

		final ScrollBar hBar = mCanvas.getHorizontalBar();
		hBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				final int width = mCanvas.getSize().x;
				int hSelection = hBar.getSelection();
				mOriginPoint.x = -hSelection;
				hBar.setMaximum(width * 10);
			}
		});
		hBar.setMaximum(12000);

		final ScrollBar vBar = mCanvas.getVerticalBar();
		vBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int vSelection = vBar.getSelection();
				mOriginPoint.y = -vSelection;
			}
		});
		vBar.setMaximum(getMaxHeight());

		mCanvas.addMouseWheelListener(this);
		mCanvas.addMouseListener(this);
		mCanvas.addMouseMoveListener(this);
		mCanvas.addKeyListener(this);
		mCanvas.addPaintListener(this);

		this.run();

		return mCanvas;
	}

	void update() {
		onSelectChange(PowerMan.getSingleton(DataModel.class).getSelectNode());
		mCurrKeyTime.refreshTime();
		// check ?
		final TimeData data = TimeData.getSelectTimeData();
		if (data != null) {
			if (data.getTime() != mCurrKeyTime.getTime()) {
				TimeData.setSelectTimeData(null);
			}
		}
		
//		System.err.println("data:"+TimeData.getSelectTimeData());
	}

	public void run() {
		final int milliseconds = 100;
		mDisplay.timerExec(milliseconds, new Runnable() {
			public void run() {
				update();
				if (!mCanvas.isDisposed()) {
					mCanvas.redraw();
					mDisplay.timerExec(milliseconds, this);
				}
			}
		});
	}

	private TimeData mCopyedData = null;

	public boolean couldCopy() {
		final TimeData selectData = TimeData.getSelectTimeData();
		return (this.mCurrentNode != null && selectData != null && selectData.getType() != null);
	}

	public void copy() {
		final TimeData selectData = TimeData.getSelectTimeData();
		if (this.mCurrentNode != null && selectData != null && selectData.getType() != null) {
			mCopyedData = new TimeData(selectData);
		} else {
			final MessageDialog dialog = new MessageDialog();
			dialog.open("Warnings", "No Selected Data To Copy!");
		}
	}

	public boolean couldPaste() {
		return (this.mCurrentNode != null);
	}

	public void paste() {
		if (mCopyedData != null) {

			mCopyedData = null;
		} else {

		}
	}

	void drawHead(final GC gc, Point size) {
		gc.setClipping(0, 0, LeftMargin, size.y);
		if (mCurrentNode != null) {
			drawStringLeft(gc, mCurrentNode.getName(), 20, 30, false, ColorFactory.YELLOW);
		}

		final int headTop = TimeLinetop;
		gc.setClipping(0, headTop, LeftMargin, size.y - headTop);

		for (NodePropertyType type : NodePropertyType.values()) {
			final TimeData data = TimeData.getSelectTimeData();
			if (data != null && data.getType() == type) {
				drawStringLeft(gc, type.toString().substring(1), 10, getKeyHeight(type), true, ColorFactory.RED);
			} else {
				drawStringLeft(gc, type.toString().substring(1), 10, getKeyHeight(type), true, ColorFactory.BLUE);
			}
		}
		drawStringLeft(gc, "Global", 10, getKeyHeight(null), true, ColorFactory.ORANGE);

		gc.setClipping(LeftMargin, headTop, size.x - LeftMargin, size.y - headTop);

		for (NodePropertyType type : NodePropertyType.values()) {
			final TimeData data = TimeData.getSelectTimeData();
			if (data != null && data.getType() == type) {
				drawLine(gc, LeftMargin, getKeyHeight(type), size.x, getKeyHeight(type), 1, ColorFactory.RED);
			} else {
				drawLine(gc, LeftMargin, getKeyHeight(type), size.x, getKeyHeight(type), 1, ColorFactory.GREEN);
			}
		}

		drawLine(gc, LeftMargin, getKeyHeight(null), size.x, getKeyHeight(null), 2, ColorFactory.ORANGE);

		gc.setClipping(0, 0, size.x, size.y);
	}

	void drawTimeLine(final GC gc, Point size) {
		gc.setClipping(LeftMargin, 0, size.x - LeftMargin, size.y);

		final TimeData timeData = new TimeData(0);
		if (mMotionNode != null) {
			timeData.setTime(mMotionNode.getLoopStart());
			int x = timeData.getPointX(mOriginPoint.x + LeftMargin + LineMargin);
			drawLine(gc, x, TimeLinetop, x, TimeLinetop - 30, 1, ColorFactory.BLACK_GRAY);

			timeData.setTime(mMotionNode.getLoopEnd());
			x = timeData.getPointX(mOriginPoint.x + LeftMargin + LineMargin);
			drawLine(gc, x, TimeLinetop, x, TimeLinetop - 30, 1, ColorFactory.BLACK_GRAY);
		}

		drawLine(gc, 0, TimeLinetop, size.x, TimeLinetop, 2, ColorFactory.BLACK_GRAY);

		final int divisions = TimeData.getCurrDivisions();
		final int stepTime = TimeData.getCurrStepTime();

		int firstIndex = -Math.round(mOriginPoint.x / TimeData.getCurrStepPixels()) - 1;
		firstIndex = Math.max(firstIndex, 0);
		int stepNumbers = (Math.round(size.x / TimeData.getCurrStepPixels()) + 3);

		for (int si = firstIndex; si < firstIndex + stepNumbers; si++) {
			// double
			int time = si * stepTime;
			timeData.setTime(time);
			int x = timeData.getPointX(mOriginPoint.x + LeftMargin + LineMargin);

			drawLine(gc, x, TimeLinetop, x, TimeLinetop + getMaxHeight(), 2, ColorFactory.BLACK_GRAY);

			drawString(gc, timeData.toTimeString(), x, TimeLinetop - 10, true, ColorFactory.RED);

			for (int di = 1; di < divisions; di++) {
				time = si * stepTime + di * stepTime / divisions;
				timeData.setTime(time);
				x = timeData.getPointX(mOriginPoint.x + LeftMargin + LineMargin);
				drawLine(gc, x, TimeLinetop, x, TimeLinetop + getMaxHeight(), 1, ColorFactory.BLACK_GRAY);
			}
		}

		gc.setClipping(0, 0, size.x, size.y);
	}

	void drawKeys(final GC gc, Point size) {
		final int headTop = TimeLinetop;
		gc.setClipping(LeftMargin, headTop, size.x - LeftMargin, size.y - headTop);

		final NodePropertyType[] types = new NodePropertyType[NodePropertyType.values().length + 1];
		for (int i = 0; i < NodePropertyType.values().length; i++) {
			types[i] = NodePropertyType.values()[i];
		}

		for (final NodePropertyType type : types) {
			final ArrayList<TimeData> list = findKeyArrayByType(type);
			if (list != null) {
				final int height = getKeyHeight(type);
				final int diameter = 10;

				final int listSize = list.size();
				for (int i = 0; i < listSize; i++) {
					final TimeData data = list.get(i);
					final int x = data.getPointX(mOriginPoint.x + LeftMargin + LineMargin);
					if (data.getSelect()) {
						drawOval(gc, x, height, diameter, ColorFactory.RED);
					} else {
						drawOval(gc, x, height, diameter, ColorFactory.BLUE);
					}

					if (i == listSize - 1) {
						final ElfMotionKeyNode key = data.getBindKeyNode();
						if (key != null && key.getLoopMode() != null) {
							drawStringLeft(gc, key.getLoopMode().toString(), x + 10, height, true, ColorFactory.BLACK_GRAY);
						}
					} else {
						final ElfMotionKeyNode key = data.getBindKeyNode();
						if (key != null && key.getInterType() != null) {
							if (key.getInterType() != InterType.Linear) {
								drawStringLeft(gc, key.getInterType().toString(), x + 10, height, true, ColorFactory.BLACK_GRAY);
							}
						}
					}
				}
			}
		}

		if (mDragData != null) {
			final int x = mDragData.getPointX(mOriginPoint.x + LeftMargin + LineMargin);
			final int height = getKeyHeight(mDragData.getType());
			final int diameter = 10;
			drawOval(gc, x, height, diameter, ColorFactory.YELLOW);
		}

		gc.setClipping(0, 0, size.x, size.y);
	}

	void drawCurrent(final GC gc, Point size) {
		gc.setClipping(LeftMargin, 0, size.x - LeftMargin, size.y);

		int x = mCurrKeyTime.getPointX(mOriginPoint.x + LeftMargin + LineMargin);

		drawLine(gc, x, CurrTop, x, TimeLinetop + getMaxHeight(), 1, ColorFactory.RED);

		if (mCurrKeyTime.getSelect()) {
			drawOval(gc, x, CurrTop, 10, ColorFactory.RED);
		} else {
			drawOval(gc, x, CurrTop, 10, ColorFactory.BLUE);
		}

		gc.setClipping(0, 0, size.x, size.y);
	}

	void drawOval(final GC gc, int x, int y, int diameter, Color color) {
		final Color old = gc.getBackground();
		gc.setBackground(color);
		gc.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
		gc.setBackground(old);
	}

	void drawLine(final GC gc, int x1, int y1, int x2, int y2, int thin, Color color) {
		Color old = gc.getForeground();
		final int oldThin = gc.getLineWidth();

		gc.setForeground(color);
		gc.setLineWidth(thin);

		gc.drawLine(x1, y1, x2, y2);

		gc.setForeground(old);
		gc.setLineWidth(oldThin);
	}

	public void paintControl(PaintEvent e) {
		final GC gc = e.gc;

		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);

		Point size = mCanvas.getSize();

		gc.setBackground(ColorFactory.WHITE);
		gc.fillRectangle(0, 0, size.x, size.y);

		gc.setBackground(ColorFactory.BLACK_GRAY);

		if (mCurrentNode != null) {
			drawHead(gc, size);
			drawTimeLine(gc, size);
			drawKeys(gc, size);
			drawCurrent(gc, size);

			drawGlobal(gc, size);
		}
	}

	private void drawGlobal(GC gc, Point size) {

	}

	// center
	static void drawString(GC gc, String text, int x, int y, boolean trans, Color color) {
		final Color old = gc.getForeground();

		final Point size = gc.textExtent(text);
		gc.setForeground(color);

		gc.drawText(LanguageManager.get(text), x - size.x / 2, y - size.y / 2, trans);

		gc.setForeground(old);
	}

	static void drawStringLeft(GC gc, String text, int x, int y, boolean trans, Color color) {
		final Color old = gc.getForeground();

		final Point size = gc.textExtent(text);
		gc.setForeground(color);

		gc.drawText(LanguageManager.get(text), x, y - size.y / 2, trans);

		gc.setForeground(old);
	}

	private boolean mShowFrameIndex = true;

	public void keyPressed(KeyEvent e) {
		if (e.keyCode == SWT.F7) {
			mShowFrameIndex = !mShowFrameIndex;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void mouseDoubleClick(MouseEvent e) {
		// add or delete
		// left
		if (e.button == 1) {
			if (mMotionNode != null && mCurrentNode != null && isInKeysTouchArea(e)) {
				final int diameter = 8;
				final int x = e.x;
				final int y = e.y;

				final NodePropertyType type = findTypeByPointY(y, diameter);
				if (type != null) {
					final TimeData data = findTimeDataByPointX(x, type, diameter);
					final ArrayList<TimeData> list = findKeyArrayByType(type);
					if (list != null) {
						if (data != null) {
							// remove
							list.remove(data);
							data.recycleNode();
						} else {
							// add
							final TimeData addData = new TimeData(0);
							addData.setPointX(x, mOriginPoint.x + LeftMargin + LineMargin);

							final ElfMotionKeyNode addKey = mMotionNode.addKey(mCurrentNode, type, addData.getTime());

							if (addKey != null) {
								addData.setType(type);
								addData.bind(addKey);
								list.add(addData);

								mCurrKeyTime.setTime(addData.getTime());
								TimeData.setSelectTimeData(addData);
							}
						}
					}
				}
			}
		}
	}

	public void addKey(final ElfMotionKeyNode key, final NodePropertyType type, boolean setTime) {
		final TimeData addData = new TimeData(key.getTime());
		addData.setType(type);
		addData.bind(key);

		final ArrayList<TimeData> list = findKeyArrayByType(type);
		list.add(addData);

		if (setTime) {
			mCurrKeyTime.setTime(addData.getTime());
			TimeData.setSelectTimeData(addData);
		}
	}

	NodePropertyType findTypeByPointY(int y, int diameter) {
		for (NodePropertyType type : NodePropertyType.values()) {
			final int height = getKeyHeight(type);
			if (Math.abs(height - y) <= diameter / 2) {
				return type;
			}
		}
		return null;
	}
	
	private final boolean isInGlobalLine(int y, int diameter) {
		
		final int height = getKeyHeight(null);
		if (Math.abs(height - y) <= diameter / 2) {
			return true;
		}
		return false;
	}

	TimeData findTimeDataByPointX(int x, NodePropertyType type, int diameter) {
		final ArrayList<TimeData> list = findKeyArrayByType(type);
		if (list != null) {
			for (TimeData data : list) {
				final int pointX = data.getPointX(mOriginPoint.x + LeftMargin + LineMargin);
				if (Math.abs(pointX - x) <= diameter / 2) {
					return data;
				}
			}
		}

		return null;
	}

	private Point mLastPoint = new Point(-1, -1);

	final TimeData findSelectTimeData() {
		return TimeData.getSelectTimeData();
	}

	private ElfMotionKeyNode mDragKey;
	private TimeData mDragData;

	public void mouseDown(MouseEvent e) {
		// reset
		mDragData = null;
		mDragKey = null;
		TimeData.setSelectTimeData(null);

		final int diameter = 8;

		if (isInKeysTouchArea(e)) {
			final NodePropertyType type = findTypeByPointY(e.y, diameter);
			if (type != null) {
				final TimeData data = findTimeDataByPointX(e.x, type, diameter);
				if (data != null) {
					TimeData.setSelectTimeData(data);
					mCurrKeyTime.setTime(data.getTime());

					final ElfMotionKeyNode keyNode = data.getBindKeyNode();
					if (keyNode != null && (e.stateMask & PlatformHelper.CTRL) != 0) {
						// add
						mDragData = new TimeData(data);
						mDragKey = (ElfMotionKeyNode) keyNode.copyDeep();
						mDragData.bind(mDragKey);

						// Resid
					}
				}
			} else if(isInGlobalLine(e.y, diameter)) {
				final TimeData data = findTimeDataByPointX(e.x, null, diameter);
				if (data != null) {
					TimeData.setSelectTimeData(data);
					mCurrKeyTime.setTime(data.getTime());
				}
			}
		}

		if (isInCurrentKeyTouchArea(e)) {
			if (e.y < TimeLinetop) {
				mCurrKeyTime.setPointX(e.x, mOriginPoint.x + LeftMargin + LineMargin);
				TimeData.setSelectTimeData(mCurrKeyTime);
			}
		}

		mLastPoint.x = e.x;
		mLastPoint.y = e.y;
	}

	public void mouseMove(MouseEvent e) {
		
		if( (e.stateMask & SWT.BUTTON1) != 0) {
			if (mLastPoint.x >= 0 && mLastPoint.y >= 0) {
				final TimeData data = findSelectTimeData();
				if (data != null) {
					if (data == mCurrKeyTime) {
						data.setPointX(e.x, mOriginPoint.x + LeftMargin + LineMargin);
					} else {
						// ctrl
						if ((e.stateMask & PlatformHelper.CTRL) != 0) {
							if (mDragData != null) {
								final int oldTime = mDragData.getTime();

								mDragData.setPointX(e.x, mOriginPoint.x + LeftMargin + LineMargin);
								final int newTime = mDragData.getTime();
								int count = 0;
								final ArrayList<TimeData> list = findKeyArrayByType(mDragData.getType());
								for (TimeData d : list) {
									if (d.getTime() == newTime) {
										count++;
									}
								}
								if (count >= 1) {
									mDragData.setTime(oldTime);
								}
							}
						} else if ((e.stateMask & PlatformHelper.SHIFT) != 0) {
							mDragData = null;
							mDragKey = null;

							final int oldTime = data.getTime();
							final int newTime = TimeData.getTimeByXAndMargin(e.x, mOriginPoint.x + LeftMargin + LineMargin);
							
							// all move
							if (newTime != oldTime) {
								final NodePropertyType type = data.getType();
								final List<TimeData> list = this.findKeyArrayByType(type);

								final int index = list.indexOf(data);
								
								if((newTime - oldTime) > 0) {
									for (int i = list.size()-1; i >= index && index >= 0; i--) {
										final TimeData dataItem = list.get(i);
										dataItem.setTime(dataItem.getTime() + (newTime - oldTime));
									}
								} else {
									for (int i = index; i < list.size() && index >= 0; i++) {
										final TimeData dataItem = list.get(i);
										dataItem.setTime(dataItem.getTime() + (newTime - oldTime));
									}
								}
							}

							if(data.getIsGlobalData()) {
								this.updateKeys();
							}
							
							mCurrKeyTime.setTime(mCurrKeyTime.getTime() + (newTime - oldTime));

						} else {
							mDragData = null;
							mDragKey = null;

							final int newTime = TimeData.getTimeByXAndMargin(e.x, mOriginPoint.x + LeftMargin + LineMargin);

							int count = 0;
							final ArrayList<TimeData> list = findKeyArrayByType(data.getType());
							for (TimeData d : list) {
								if (d.getTime() == newTime) {
									count++;
								}
							}
							if (count ==0) {
								data.setTime(newTime);
							}
							
							//
							mCurrKeyTime.setTime(data.getTime());
							//update ?
							if(data.getIsGlobalData()) { 
								this.updateKeys();
							} 
						}
					}
				}
			}

			if (mLastPoint.x >= 0 && mLastPoint.y >= 0) {
				mLastPoint.x = e.x;
				mLastPoint.y = e.y;
			}
		}
		
		
	}

	private void updateKeys() {
		for(NodePropertyType type : NodePropertyType.values()) {
			final ArrayList<TimeData> list = this.findKeyArrayByType(type);
			for(TimeData data : list) {
				final ElfMotionKeyNode ketNode = data.getBindKeyNode();
				if(ketNode != null) {
					data.setTime(ketNode.getTime());
				}
			}
		}
	}

	public void mouseUp(MouseEvent e) {
		// drop
		if (mDragData != null) {
			mDragKey.setTime(mDragData.getTime());
			mDragKey.addToParentView();

			final ArrayList<TimeData> list = findKeyArrayByType(mDragData.getType());
			list.add(mDragData);

			mDragData = null;
			mDragKey = null;
		}

		// reset
		mLastPoint.x = -1;
		mLastPoint.y = -1;
	}

	boolean isInKeysTouchArea(MouseEvent e) {
		if (mMotionNode != null && mMotionNode.getPauseMotion() && mCurrentNode != null) {
			final int x = e.x;
			final int y = e.y;
			if (x >= LeftMargin && y >= TimeLinetop) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	boolean isInCurrentKeyTouchArea(MouseEvent e) {
		if (mMotionNode != null && mMotionNode.getPauseMotion() && mCurrentNode != null) {
			final int x = e.x;
			if (x >= LeftMargin) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public void mouseScrolled(MouseEvent e) {
		if ((e.stateMask & PlatformHelper.CTRL) != 0) {
			TimeData.scroll(e.count);
		}
	}
}
