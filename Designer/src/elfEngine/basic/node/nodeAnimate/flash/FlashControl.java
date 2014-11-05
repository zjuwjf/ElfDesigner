//package elfEngine.basic.node.nodeAnimate.flash;
//
//import java.util.HashSet;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.dnd.DND;
//import org.eclipse.swt.dnd.DragSource;
//import org.eclipse.swt.dnd.DragSourceEvent;
//import org.eclipse.swt.dnd.DragSourceListener;
//import org.eclipse.swt.dnd.DropTarget;
//import org.eclipse.swt.dnd.DropTargetEvent;
//import org.eclipse.swt.dnd.DropTargetListener;
//import org.eclipse.swt.dnd.TextTransfer;
//import org.eclipse.swt.dnd.Transfer;
//import org.eclipse.swt.events.KeyEvent;
//import org.eclipse.swt.events.KeyListener;
//import org.eclipse.swt.events.MenuEvent;
//import org.eclipse.swt.events.MenuListener;
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.events.MouseListener;
//import org.eclipse.swt.events.MouseMoveListener;
//import org.eclipse.swt.events.MouseWheelListener;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.widgets.Canvas;
//import org.eclipse.swt.widgets.Menu;
//import org.eclipse.swt.widgets.MenuItem;
//import org.eclipse.swt.widgets.Shell;
//
//import com.ielfgame.stupidGame.MainDesigner;
//import com.ielfgame.stupidGame.language.LanguageManager;
//import com.ielfgame.stupidGame.power.PowerMan;
//
//import elfEngine.basic.node.nodeAnimate.flash.FlashModel.FlashManager;
//import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlash;
//import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlashBranch;
//import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlashKey;
//
//public class FlashControl implements MouseListener, MouseMoveListener, KeyListener, MouseWheelListener {
//	/***
//	 * 
//	 * key
//	 * 1. select a region
//	 * 2. select a key
//	 * 3. select a branch
//	 * 4. select time
//	 * 5. mouse region
//	 * 
//	 * branch
//	 * add, delete, copy, paste, 
//	 * visible, lock, link?
//	 * 
//	 * add 	basic-branch
//	 * 	   	delegate-branch
//	 * 
//	 * 	   	add basic-key
//	 * 	   	add clone-flash-key -- flash-name ??
//	 * 		add reverse-flash-key
//	 * 
//	 * save????
//	 * 
//	 * flash
//	 * 
//	 * basic-flash 	- link? default:@
//	 * 						relative-names: {name0}, {name1}, {name2}
//	 * 
//	 * 			   	- copy? 
//	 * 			   	- name? 
//	 * 				- 
//	 * 
//	 * 
//	 * combine-flash	-link ?
//	 * 			-link?
//	 * 
//	 * 
//	 * depend-flash,
//	 * link, copy, delete
//	 * rename, 
//	 * 
//	 * 
//	 */
//	
//	/***
//	 * 
//	 */
//	private final static FlashControl sFlashControl = new FlashControl();
//
//	public static FlashControl getInstance() {
//		return sFlashControl;
//	}
//
//	/***
//	 * 
//	 */
//	private final Point mMousePosition = new Point(0,0);
//	private final Point mSelectLT = new Point(0, 0);
//	private final Point mSelectRB = new Point(0, 0);
//	private boolean mSelectRegion = false;
//	
//	//scale ?
//	private final Point mOffsetPoint = new Point(0,0);
//	
//	//config
//	private boolean mIsAutoKey = true;
//	private boolean mIsTimeBarAlignTop = true;
//	
//	public boolean getAutoKey() {
//		return mIsAutoKey;
//	}
//	
//	public void setAutoKey(boolean mAutoKey) {
//		this.mIsAutoKey = mAutoKey;
//	}
//
//	public boolean getTimeBarAlignTop() {
//		return mIsTimeBarAlignTop;
//	}
//	
//	public void setTimeBarAlignTop(boolean mTimeBarAlignTop) {
//		this.mIsTimeBarAlignTop = mTimeBarAlignTop;
//	}
//
//	public void setMousePosition(int x, int y) {
//		mMousePosition.x = x;
//		mMousePosition.y = y;
//	}
//	
//	public Point getMousePosition() {
//		return mMousePosition;
//	}
//	
//	public void setSelectLT(int x, int y) {
//		mSelectLT.x = x;
//		mSelectLT.y = y;
//	}
//	
//	public Rectangle getSelectRectangle() {
//		final FlashCanvasView view = PowerMan.getSingleton(FlashCanvasView.class);
//		
//		final int minX = Math.max(Math.min(mSelectLT.x, mSelectRB.x), view.mLabelViewWidth);
//		final int maxX = Math.max(Math.max(mSelectLT.x, mSelectRB.x), view.mLabelViewWidth);
//		
//		final int minY, maxY;
//		if(getTimeBarAlignTop()) {
//			minY = Math.max(Math.min(mSelectLT.y, mSelectRB.y), view.mTimeBarHeight);
//			maxY = Math.max(Math.max(mSelectLT.y, mSelectRB.y), view.mTimeBarHeight);
//		} else {
//			minY = Math.min(Math.min(mSelectLT.y, mSelectRB.y), view.getCanvasSize().y-view.mTimeBarHeight);
//			maxY = Math.min(Math.max(mSelectLT.y, mSelectRB.y), view.getCanvasSize().y-view.mTimeBarHeight);
//		}
//		
//		final Rectangle ret = new Rectangle(minX, minY, maxX-minX, maxY-minY);
//		return ret;
//	}
//	
////	public Point getSelectLT() {
////		return mSelectLT;
////	}
//	
//	public void setSelectRB(int x, int y) {
//		mSelectRB.x = x;
//		mSelectRB.y = y;
//	}
//	
//	public Point getSelectRB() {
//		return mSelectRB;
//	}
//	
//	public void setSelectRegion(boolean select) {
//		mSelectRegion = select;
//	}
//	
//	public boolean getSelectRegion() {
//		return mSelectRegion;
//	}
//	
//	public void setOffsetPoint(int x, int y) {
//		mOffsetPoint.x = x;
//		mOffsetPoint.y = y;
//	}
//	
//	public Point getViewOffsetPoint() {
//		return mOffsetPoint;
//	}
//	
//	public void translateOffsetPoint(int dx, int dy) {
//		mOffsetPoint.x += dx;
//		mOffsetPoint.y += dy;
//	}
//	
//	private FlashControl() {
//		
//	}
//
//	@Override
//	public void keyPressed(KeyEvent e) {
//		switch(e.keyCode) {
//		case SWT.ARROW_LEFT:
//			break;
//		case SWT.ARROW_RIGHT:
//			break;
//		}
//	}
//
//	@Override
//	public void keyReleased(KeyEvent e) {
//		
//	}
//	
//	@Override
//	public void mouseScrolled(MouseEvent e) {
//		
//	}
//
//	@Override
//	public void mouseMove(MouseEvent e) {
//		this.setMousePosition(e.x, e.y);
//		if(getSelectRegion()) {
//			this.touchMove(e.x, e.y);
//		}
//	}
//
//	@Override
//	public void mouseDown(MouseEvent e) {
//		this.setMousePosition(e.x, e.y);
//		if(e.button == 1) {
//			this.setSelectRegion(true);
//			this.touchDown(e.x, e.y);
//		}
//	}
//
//	@Override
//	public void mouseUp(MouseEvent e) {
//		this.setMousePosition(e.x, e.y);
//		if(e.button == 1) {
//			if(getSelectRegion()) {
//				setSelectRegion(false);
//				this.touchUp(e.x, e.y);
////				if() {
////				}
//			}
//		}
//	}
//	
//	public HashSet<IFlashBranch> getSelectBranchSet() {
//		final HashSet<IFlashBranch> set = new HashSet<IFlashBranch>();
//		if(getSelectRegion()) {
//			final IFlash flash = FlashManager.getCurrentFlash();
//			if(flash != null) {
//				final FlashCanvasView view = PowerMan.getSingleton(FlashCanvasView.class);
//				
//				final Rectangle rectangle = getSelectRectangle();
//				final Point viewLt = new Point(rectangle.x, rectangle.y);
//				final Point viewRB = new Point(rectangle.x+rectangle.width, rectangle.y+rectangle.height);
//				
//				final Point lt = view.convertViewToLogicPoint(viewLt.x, viewLt.y);
//				final Point rb = view.convertViewToLogicPoint(viewRB.x, viewRB.y);
//				
//				int branchY1 = view.convertLogicToBranch(lt.y);
//				int branchY2 = view.convertLogicToBranch(rb.y);
//				
//				final IFlashBranch [] branches = flash.getFlashBranches();
//				branchY1 = Math.min(branchY1, branches.length-1);
//				branchY2 = Math.min(branchY2, branches.length-1);
//				branchY1 = Math.max(0, branchY1);
//				branchY2 = Math.max(0, branchY2);
//				
//				final int branchYMax= Math.max(branchY1, branchY2);
//				final int branchYMin= Math.min(branchY1, branchY2);
//				
//				for(int i=branchYMin; i<=branchYMax; i++) {
//					final IFlashBranch branch = branches[i];
//					set.add(branch);
//				}
//			}
//		}
//		return set;
//	}
//	
//	public HashSet<IFlashKey> getSelectKeySet() {
//		final HashSet<IFlashKey> set = new HashSet<IFlashKey>();
//		if(getSelectRegion()) {
//			final IFlash flash = FlashManager.getCurrentFlash();
//			if(flash != null) {
//				final FlashCanvasView view = PowerMan.getSingleton(FlashCanvasView.class);
//				
//				final Rectangle rectangle = getSelectRectangle();
//				final Point viewLt = new Point(rectangle.x, rectangle.y);
//				final Point viewRB = new Point(rectangle.x+rectangle.width, rectangle.y+rectangle.height);
//				
//				final Point lt = view.convertViewToLogicPoint(viewLt.x, viewLt.y);
//				final Point rb = view.convertViewToLogicPoint(viewRB.x, viewRB.y);
//				
//				int branchY1 = view.convertLogicToBranch(lt.y);
//				int branchY2 = view.convertLogicToBranch(rb.y);
//				
//				final IFlashBranch [] branches = flash.getFlashBranches();
//				branchY1 = Math.min(branchY1, branches.length-1);
//				branchY2 = Math.min(branchY2, branches.length-1);
//				branchY1 = Math.max(0, branchY1);
//				branchY2 = Math.max(0, branchY2);
//				
//				final int branchYMax= Math.max(branchY1, branchY2);
//				final int branchYMin= Math.min(branchY1, branchY2);
//				
//				final int timeX1 = view.convertLogicToTimeKeyX(lt.x);
//				final int timeX2 = view.convertLogicToTimeKeyX(rb.x);
//				
//				final int timeMax = Math.max(timeX1, timeX2);
//				final int timeMin = Math.min(timeX1, timeX2);
//				
//				for(int i=branchYMin; i<=branchYMax; i++) {
//					final IFlashBranch branch = branches[i];
//					final IFlashKey [] keys = branch.getKeyArray();
//					for(IFlashKey key : keys) {
//						if(key.getKeyIndex()+key.getKeyScope() >= timeMin && key.getKeyIndex() <= timeMax) {
//							set.add(key);
//						}
//					}
//				}
//			}
//		}
//		return set;
//	}
//	
//	@Override
//	public void mouseDoubleClick(MouseEvent e) {
//		//
//	}
//	
//	private void touchDown(int x, int y) {
//		this.setSelectLT(x, y);
//		this.setSelectRB(x, y);
//	}
//	
//	private void touchMove(int x, int y) {
//		this.setSelectRB(x, y);
//	}
//	
//	private void touchUp(int x, int y) {
//		this.setSelectRB(x, y);
//	}
//	
//	public Menu createMenu() {
//		final Shell shell = PowerMan.getSingleton(MainDesigner.class).getShell();
//		final Menu menu = new Menu(shell, SWT.POP_UP);
//		
//		final MenuItem addItem = new MenuItem(menu, SWT.PUSH);
//		addItem.setText(LanguageManager.get("Add"));
//		addItem.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		
//		final MenuItem deleteItem = new MenuItem(menu, SWT.PUSH);
//		deleteItem.setText(LanguageManager.get("Delete"));
//		deleteItem.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		
//		new MenuItem(menu, SWT.SEPARATOR);
//		
//		final MenuItem cutItem = new MenuItem(menu, SWT.PUSH);
//		cutItem.setText(LanguageManager.get("Cut"));
//		cutItem.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		
//		final MenuItem copyItem = new MenuItem(menu, SWT.PUSH);
//		copyItem.setText(LanguageManager.get("Copy"));
//		copyItem.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		
//		final MenuItem pasteItem = new MenuItem(menu, SWT.PUSH);
//		pasteItem.setText(LanguageManager.get("Paste"));
//		pasteItem.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		
//		new MenuItem(menu, SWT.SEPARATOR);
//		
//		final MenuItem reverseItem = new MenuItem(menu, SWT.PUSH);
//		reverseItem.setText(LanguageManager.get("Reverse"));
//		reverseItem.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});
//		
//		menu.addMenuListener(new MenuListener() {
//			public void menuShown(MenuEvent e) {
//			}
//			public void menuHidden(MenuEvent e) {
//			}
//		});
//		
//		return menu;
//	}
//	
//	public void setDND(final Canvas canvas) {
//		DropTarget dropTarget = new DropTarget(canvas, DND.DROP_MOVE | DND.DROP_COPY);
//		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
//		dropTarget.addDropListener(new DropTargetListener() {
//
//			public void dragEnter(DropTargetEvent event) {
//				
//			}
//
//			public void dragLeave(DropTargetEvent event) {
//				
//			}
//
//			public void dragOperationChanged(DropTargetEvent event) {
//				
//			}
//			
//			public void dragOver(DropTargetEvent event) {
//				if(event.x > 100) {
//					event.operations |= DND.DROP_MOVE;
//				} else {
//					event.operations &= (~DND.DROP_MOVE);
//				}
//			}
//
//			public void drop(DropTargetEvent event) {
//				
//			}
//
//			public void dropAccept(DropTargetEvent event) {
//				
//			}
//		});
//		
//		final int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
//		final Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
//		
//		final DragSource source = new DragSource(canvas, operations);
//		source.setTransfer(types);
//		source.addDragListener(new DragSourceListener() {
//			public void dragStart(DragSourceEvent event) {
//				event.doit = false;		
//			};
//
//			public void dragSetData(DragSourceEvent event) {
//				event.data = "text";
//			}
//
//			public void dragFinished(DragSourceEvent event) {
//			}
//		});
//	}
//}
