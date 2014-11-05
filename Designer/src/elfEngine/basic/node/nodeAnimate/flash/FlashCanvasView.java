//package elfEngine.basic.node.nodeAnimate.flash;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.PaintEvent;
//import org.eclipse.swt.events.PaintListener;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.GC;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Canvas;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//
//import com.ielfgame.stupidGame.ColorFactory;
//import com.ielfgame.stupidGame.language.LanguageManager;
//import com.ielfgame.stupidGame.power.APowerManSingleton;
//
//import elfEngine.basic.node.nodeAnimate.flash.FlashKey.ADelegateFlashKey;
//import elfEngine.basic.node.nodeAnimate.flash.FlashKey.BasicFlashKey;
//import elfEngine.basic.node.nodeAnimate.flash.FlashModel.FlashManager;
//import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlash;
//import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlashBranch;
//import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlashKey;
//
//public class FlashCanvasView extends APowerManSingleton implements PaintListener, Runnable{
//	private Display mDisplay;
//	private Canvas mCanvas;
//	
//	/***
//	 * Models
//	 */
//	protected final FlashControl mFlashControl = FlashControl.getInstance();
//	/***
//	 * View Configs
//	 */
//	protected final int mTimeBarHeight = 45;
//	protected final int mLabelViewWidth = 150;
//	protected final int mBranchHeight = 20;
//	protected final int mKeyPixel = 12;
//	protected final int mTimeRulerHeight = 12;
//	protected final int mLaeblLeftX = 50;
//	protected final int mBranchVisibleX = mLaeblLeftX/3;
//	protected final int mBranchLockedX = mBranchVisibleX*2;
//	/***
//	 * Colors
//	 */
//	// background
//	protected final Color mNormalBranchBackgroundColor = ColorFactory.GRAY;
//	protected final Color mSelectBranchBackgroundColor = ColorFactory.GRAY;
//	protected final Color mTimeBarAreaBackgroundColor = ColorFactory.DRAK_GRAY;
//	protected final Color mBranchAreaBackgroundColor = ColorFactory.DRAK_GRAY;
//	protected final Color mBranchGridColor = ColorFactory.GRAY;
//	// time
//	protected final Color mCurrentTimeColor = ColorFactory.CYAN;
//	protected final Color mTimeRulerColor = ColorFactory.GRAY;
//	// draw
//	protected final Color mSelectKeyColor = ColorFactory.GREEN;
//	protected final Color mNormalKeyColor = ColorFactory.BLACK_GRAY;
//	// fill
//	protected final Color mFillKeyColor = ColorFactory.ORANGE;
//	// text
//	protected final Color mLabelColor = ColorFactory.WHITE;
//	protected final Color mFlashNameColor = ColorFactory.WHITE;
//	protected final Color mNumberColor = ColorFactory.BLACK_GRAY;
//	
//	public Canvas create(final Composite parent) {
//		mDisplay = parent.getDisplay();
//		
//		mCanvas = new Canvas(parent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
//		mCanvas.addPaintListener(this);
//		
//		mCanvas.addMouseWheelListener(mFlashControl);
//		mCanvas.addMouseListener(mFlashControl);
//		mCanvas.addMouseMoveListener(mFlashControl);
//		mCanvas.addKeyListener(mFlashControl);
//		mCanvas.setMenu(mFlashControl.createMenu());
//		
//		mFlashControl.setDND(mCanvas);
//		
//		this.run();
//		return mCanvas;
//	}
//
//	public void run() {
//		final int milliseconds = 20;
//		mDisplay.timerExec(milliseconds, new Runnable() {
//			public void run() {
//				update();
//				if (!mCanvas.isDisposed()) {
//					mCanvas.redraw();
//					mDisplay.timerExec(milliseconds, this);
//				}
//			}
//
//			private void update() {
//				
//			}
//		});
//	}
//
//	/***
//	 * start-frame-index start-branch-index
//	 * 
//	 * clip background select-branch grid keys
//	 * 
//	 * timebar Flash-Name times + numbers
//	 * 
//	 * current-time
//	 * 
//	 * 
//	 */
//
//	private final void drawTimeBarArea(final GC gc) {
//		final Point size = getCanvasSize();
//		final Rectangle timebarRectangle = new Rectangle(0, 0, size.x, mTimeBarHeight);
//		final Rectangle labelRectangle = new Rectangle(0, 0, mLabelViewWidth, mTimeBarHeight);
//		final Rectangle timeRectangle = new Rectangle(mLabelViewWidth-10, 0, size.x - mLabelViewWidth+10, mTimeBarHeight);
//
//		if (!mFlashControl.getTimeBarAlignTop()) {
//			timebarRectangle.y = size.y - mTimeBarHeight;
//			labelRectangle.y = size.y - mTimeBarHeight;
//			timeRectangle.y = size.y - mTimeBarHeight;
//		}
//		// draw time bar
//		// //background
//		gc.setClipping(timebarRectangle);
//		fillRectangle(gc, timebarRectangle.x, timebarRectangle.y, timebarRectangle.width, timebarRectangle.height, mTimeBarAreaBackgroundColor);
//		if (mFlashControl.getTimeBarAlignTop()) {
//			drawLine(gc, 0, timebarRectangle.y + timebarRectangle.height, timebarRectangle.width, timebarRectangle.y + timebarRectangle.height, 2, mTimeRulerColor);
//		} else {
//			drawLine(gc, 0, timebarRectangle.y, timebarRectangle.width, timebarRectangle.y, 2, mTimeRulerColor);
//		}
//		// //flash name
//		gc.setClipping(labelRectangle);
//		drawStringLeft(gc, "Flash", timebarRectangle.x + 50, timebarRectangle.y + timebarRectangle.height / 2, true, mFlashNameColor);
//
//		// //draw times and numbers
//		gc.setClipping(timeRectangle);
//		final Point logicBeginPoint = convertViewToLogicPoint(mLabelViewWidth, timebarRectangle.y);
//		final Point logicEndPoint = convertViewToLogicPoint(timebarRectangle.width, timebarRectangle.y + timebarRectangle.height);
//
//		final int timeBegin = convertLogicToTimeKeyX(logicBeginPoint.x);
//		final int timeEnd = convertLogicToTimeKeyX(logicEndPoint.x);
//
//		for (int i = timeBegin; i <= timeEnd; i++) {
//			final int x = convertLogicToViewPoint(convertTimeKeyToLogicX(i), 0).x;
//			final int y1, y2;
//			final boolean mode5 = (i % 5 == 0);
//			if (mFlashControl.getTimeBarAlignTop()) {
//				y1 = timebarRectangle.y + timebarRectangle.height;
//				y2 = y1 - (mode5 ? mTimeRulerHeight : mTimeRulerHeight / 2);
//			} else {
//				y1 = timebarRectangle.y;
//				y2 = y1 + (mode5 ? mTimeRulerHeight : mTimeRulerHeight / 2);
//			}
//			drawLine(gc, x, y1, x, y2, 1, mTimeRulerColor);
//			if (mode5) {
//				drawString(gc, Integer.toString(i), x, timebarRectangle.y + timebarRectangle.height / 2, true, mTimeRulerColor);
//			}
//		}
//
//		// //draw current time
//		final Rectangle clipRectangle = new Rectangle(mLabelViewWidth, 0, size.x-mLabelViewWidth, size.y);
//		gc.setClipping(clipRectangle);
//		final int currentKey = 31;
//		final int viewX = convertLogicToViewPoint(convertTimeKeyToLogicX(currentKey), 0).x;
//		drawLine(gc, viewX, 0, viewX, size.y, 1, mCurrentTimeColor);
//		
//		final Rectangle oldClip = null;
//		gc.setClipping(oldClip); 
//	} 
//
//	private final void drawBranchArea(final GC gc) {
//		final Point size = getCanvasSize();
//		final Rectangle clipRectangle = new Rectangle(0, mTimeBarHeight, size.x, size.y - mTimeBarHeight);
//		if (!mFlashControl.getTimeBarAlignTop()) {
//			clipRectangle.y = 0;
//		}
//		gc.setClipping(clipRectangle);
//
//		// draw background
//		fillRectangle(gc, clipRectangle.x, clipRectangle.y, clipRectangle.width, clipRectangle.height, mBranchAreaBackgroundColor);
//		
//		// draw select
//		// draw grid
//		final Point logicBeginPoint = convertViewToLogicPoint(mLabelViewWidth, clipRectangle.y);
//		final Point logicEndPoint = convertViewToLogicPoint(clipRectangle.width, clipRectangle.y + clipRectangle.height);
//
//		drawLine(gc, mLabelViewWidth, clipRectangle.y, mLabelViewWidth, clipRectangle.y + clipRectangle.height, 1, mBranchGridColor);
//		
//		//draw select
//		final Color blue = new Color(null,51,153,255);
//		final Rectangle selectRectangle = mFlashControl.getSelectRectangle();
//		fillRectangleWithAlpha(gc, selectRectangle.x, selectRectangle.y, selectRectangle.width, selectRectangle.height, 50, blue);
//		drawRectangle(gc, selectRectangle.x, selectRectangle.y, selectRectangle.width, selectRectangle.height, 1, blue);
//		
//		// current flash
//		final IFlash flash = FlashManager.getCurrentFlash();
//		
//		if (flash != null) {
//			final int beginBranch = convertLogicToBranch(logicBeginPoint.y);
//			final int endBranch = convertLogicToBranch(logicEndPoint.y);
//
//			final int beginKeyIndex = convertLogicToTimeKeyX(logicBeginPoint.x);
//			final int endKeyIndex = convertLogicToTimeKeyX(logicEndPoint.x);
//
//			// draw branches
//			final IFlashBranch[] branches = flash.getFlashBranches();
//			for (int i = beginBranch; i <= endBranch && i < branches.length; i++) {
//				final int logicUpY = convertBranchToLogicY(i);
//				final int viewUpY = convertLogicToViewPoint(0, logicUpY).y;
//
//				final int logicDownY = convertBranchToLogicY(i + 1);
//				final int viewDownY = convertLogicToViewPoint(0, logicDownY).y;
//
//				if (i == beginBranch) {
//					drawLine(gc, clipRectangle.x, viewUpY, clipRectangle.x + clipRectangle.width, viewUpY, 1, mBranchGridColor);
//				}
//				drawLine(gc, clipRectangle.x, viewDownY, clipRectangle.x + clipRectangle.width, viewDownY, 1, mBranchGridColor);
//
//				final IFlashBranch branch = branches[i];
//				if (branch != null) {
//					drawBranchVisible(gc, (viewUpY+viewDownY)/2, branch.getVisible());
//					drawBranchLocked(gc, (viewUpY+viewDownY)/2, branch.getLocked());
//					
//					drawStringLeft(gc, branch.getName(), mLaeblLeftX, (viewUpY + viewDownY) / 2, true, mLabelColor);
//
//					final IFlashKey[] keys = branch.getKeyArray();
//					for (int j = 0; j < keys.length; j++) {
//						final IFlashKey key = keys[j];
//						if (key != null) {
//							final int keyIndex = key.getKeyIndex();
//							final int keyScope = key.getKeyScope();
//							if (keyIndex + keyScope >= beginKeyIndex && keyIndex <= endKeyIndex) {
//								// draw
//								drawKey(gc, viewUpY, viewDownY - viewUpY, key);
//							}
//						}
//					}
//				}
//			}
//		}
//
//		final Rectangle oldClip = null;
//		gc.setClipping(oldClip);
//	}
//	
//	private final void drawBranchVisible(final GC gc, final int y, final boolean fill) {
//		if(fill) {
//			fillRoundRectangle(gc, mBranchVisibleX, y, 12, 12, 4, 4, ColorFactory.RED);
//		} else {
//			drawRoundRectangle(gc, mBranchVisibleX, y, 12, 12, 4, 4, 1, ColorFactory.RED);
//		}
//	}
//	
//	private final void drawBranchLocked(final GC gc, final int y, final boolean fill) { 
//		if(fill) {
//			fillRoundRectangle(gc, mBranchLockedX, y, 12, 12, 4, 4, ColorFactory.YELLOW);
//		} else {
//			drawRoundRectangle(gc, mBranchLockedX, y, 12, 12, 4, 4, 1, ColorFactory.YELLOW);
//		}
//	} 
//
//	// visible solid <>
//	private final void drawKey(final GC gc, final int top, final int height, final IFlashKey key) {
//		if (key instanceof BasicFlashKey) {
//			final BasicFlashKey data = (BasicFlashKey) key;
//			if (data.getVisible()) {
//
//			} else {
//
//			}
//		} else if (key instanceof ADelegateFlashKey) {
//			final ADelegateFlashKey delegateKey = (ADelegateFlashKey) key;
//			delegateKey.getName();
//		}
//	}
//
//	public void paintControl(final PaintEvent e) {
//		final GC gc = e.gc;
//		drawBranchArea(gc);
//		drawTimeBarArea(gc);
//	}
//
//	public boolean isInBranchArea(int viewX, int viewY) {
//		if (mFlashControl.getTimeBarAlignTop()) {
//			return viewY < mTimeBarHeight;
//		} else {
//			return viewY > getCanvasSize().y - mTimeBarHeight;
//		}
//	}
//
//	public boolean isInTimeBarArea(int viewX, int viewY) {
//		return !isInBranchArea(viewX, viewY);
//	}
//
//	public int convertLogicToBranch(final int logicY) {
//		return Math.round(logicY / (float) mBranchHeight);
//	}
//
//	public int convertBranchToLogicY(final int branch) {
//		return branch * mBranchHeight;
//	}
//
//	public int convertLogicToTimeKeyX(final int logicX) {
//		return Math.round(logicX / (float) mKeyPixel);
//	}
//
//	public int convertTimeKeyToLogicX(int timeX) {
//		return timeX * mKeyPixel;
//	}
//
//	public Point convertViewToLogicPoint(int viewX, int viewY) {
//		final Point logicPoint = new Point(0, 0);
//		final Point offset = mFlashControl.getViewOffsetPoint();
//
//		logicPoint.x = viewX - offset.x - mLabelViewWidth;
//		
//		if (mFlashControl.getTimeBarAlignTop()) {
//			logicPoint.y = viewY - offset.y - mTimeBarHeight;
//		} else {
//			logicPoint.y = viewY - offset.y;
//		}
//		return logicPoint;
//	}
//
//	public Point convertLogicToViewPoint(int logicX, int logicY) {
//		final Point viewPoint = new Point(0, 0);
//
//		final Point offset = mFlashControl.getViewOffsetPoint();
//
//		viewPoint.x = logicX + offset.x + mLabelViewWidth;
//
//		if (mFlashControl.getTimeBarAlignTop()) {
//			viewPoint.y = logicY + offset.y + mTimeBarHeight;
//		} else {
//			viewPoint.y = logicY + offset.y;
//		}
//		return viewPoint;
//	}
//
//	// private final void insert(int insertFrame, int insertBranch, int mode) {
//	//
//	// }
//	//
//	// private final void copy() {
//	//
//	// }
//	//
//	// private final void cut() {
//	//
//	// }
//	//
//	// private final void paste() {
//	//
//	// }
//	//
//	// private final void delete() {
//	//
//	// }
//	//
//	// private final void setVisible(boolean visible) {
//	//
//	// }
//	//
//	// private final void translate(final int keys) {
//	//
//	// }
//
//	Point getCanvasSize() {
//		return mCanvas.getSize();
//	}
//
//	static void fillOval(final GC gc, int x, int y, int diameter, Color color) {
//		final Color old = gc.getBackground();
//		gc.setBackground(color);
//		gc.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
//		gc.setBackground(old);
//	}
//
//	static void drawLine(final GC gc, int x1, int y1, int x2, int y2, int thin, Color color) {
//		final Color old = gc.getForeground();
//		final int oldThin = gc.getLineWidth();
//
//		gc.setForeground(color);
//		gc.setLineWidth(thin);
//
//		gc.drawLine(x1, y1, x2, y2);
//
//		gc.setForeground(old);
//		gc.setLineWidth(oldThin);
//	}
//
//	static void drawRectangle(final GC gc, int x, int y, int width, int height, int thin, Color color) {
//		final Color old = gc.getForeground();
//		final int oldThin = gc.getLineWidth();
//		gc.setForeground(color);
//		gc.setLineWidth(thin);
//
//		gc.drawRectangle(x, y, width, height);
//
//		gc.setLineWidth(oldThin);
//		gc.setForeground(old);
//	}
//
//	static void drawRoundRectangle(final GC gc, int x, int y, int width, int height, int arcWidth, int arcHeight, int thin, Color color) {
//		final Color old = gc.getForeground();
//		final int oldThin = gc.getLineWidth();
//		gc.setForeground(color);
//		gc.setLineWidth(thin);
//
//		gc.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight);
//
//		gc.setLineWidth(oldThin);
//		gc.setForeground(old);
//	}
//
//	static void fillRectangle(final GC gc, int x, int y, int width, int height, Color color) {
//		final Color old = gc.getBackground();
//		gc.setBackground(color);
//		gc.fillRectangle(x, y, width, height);
//		gc.setBackground(old);
//	}
//	
//	static void fillRectangleWithAlpha(final GC gc, int x, int y, int width, int height, int alpha, Color color) {
//		final Color old = gc.getBackground();
//		final int oldAlpha = gc.getAlpha();
//		gc.setBackground(color);
//		gc.setAlpha(alpha);
//		gc.fillRectangle(x, y, width, height);
//		gc.setBackground(old);
//		gc.setAlpha(oldAlpha);
//	}
//
//	static void fillRoundRectangle(final GC gc, int x, int y, int width, int height, int arcWidth, int arcHeight, Color color) {
//		final Color old = gc.getBackground();
//		gc.setBackground(color);
//		gc.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight);
//		gc.setBackground(old);
//	}
//
//	static void drawString(GC gc, String text, int x, int y, boolean trans, Color color) {
//		final Color old = gc.getForeground();
//		final Point size = gc.textExtent(text);
//		gc.setForeground(color);
//		gc.drawText(LanguageManager.get(text), x - size.x / 2, y - size.y / 2, trans);
//		gc.setForeground(old);
//	}
//
//	static void drawStringLeft(GC gc, String text, int x, int y, boolean trans, Color color) {
//		final Color old = gc.getForeground();
//		final Point size = gc.textExtent(text);
//		gc.setForeground(color);
//		gc.drawText(LanguageManager.get(text), x, y - size.y / 2, trans);
//		gc.setForeground(old);
//	}
//}
