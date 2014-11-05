package com.ielfgame.stupidGame.GLView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.cocos2d.actions.interpolator.ElfInterAction;
import org.cocos2d.actions.interval.MoveBy;
import org.cocos2d.actions.interval.ScaleTo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.StatusViewWorkSpace;
import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.enumTypes.Orientation;
import com.ielfgame.stupidGame.enumTypes.TextStyle;
import com.ielfgame.stupidGame.fileBar.FileBar;
import com.ielfgame.stupidGame.imExport.ImExport;
import com.ielfgame.stupidGame.newNodeMenu.SearchMenu;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.undo.UndoRedoManager;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.modifier.AlphaModifier;
import elfEngine.basic.modifier.EmptyModifier;
import elfEngine.basic.modifier.LifeModifier;
import elfEngine.basic.modifier.SequenceModifier;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;
import elfEngine.basic.node.extend.ElfScreenNode;
import elfEngine.basic.node.extend.FpsNode;
import elfEngine.basic.node.extend.ScaleNode;
import elfEngine.basic.node.nodeLayout.LinearLayout2DNode;
import elfEngine.basic.node.nodeList.ListNode;
import elfEngine.basic.node.nodeText.StrokeTextNode;
import elfEngine.basic.node.particle.modifier.MathHelper;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.MotionEvent;

public class BasicGLViewRender implements IGLRender {
	
	private boolean mGLPaused = false;
	private boolean mUseControl = true;
	private boolean mUseFixedSize = true;
	private int mGLID = GLID.GL_DEFAULT_ID;
	
	private IGLView mGLView;
	
	// nodes
	private ElfScreenNode mRoot;
	private final FpsNode mFps = new FpsNode(null, Integer.MAX_VALUE);
	private final ScaleNode mScaleNode = new ScaleNode(null, 0);
	
	private int mLastMouseDown = 0;
	private final static int LEFT_DOWN = 1;
	private final static int MID_DOWN = 2;
	private final static int RIGHT_DOWN = 4;
	
	private final ElfPointf mLastMousePos = new ElfPointf();
	private final HashMap<Integer, Boolean> mKeyMap = new HashMap<Integer, Boolean>();
	private final String ViewScaleKey = "ViewScale";
	private final float mPercentages[] = { 0.25f, 0.5f, 0.75f, 1, 1.5f, 2, 4, 8 };
	private ElfNode mOldText;
	
	private final float sAnimateTime = 0.6f;
	private final InterType sAnimateInter = InterType.Dece;

	public BasicGLViewRender() { 
		this.setRootNode(new ElfScreenNode());
		
		final float off = 30;
		mFps.setPosition(+off, +off);
		mFps.setVisible(false);
		
		mRoot.setUseSettedSize(true);
		mRoot.setSize(getPhysicWidth(), getPhysicHeight());
		
		mRoot.setData(ViewScaleKey, 2);

		mScaleNode.setPosition(0, 0);
		mScaleNode.setScale(getFixedScale());
		
		resetView();
	}
	
	public void setRootNode(ElfScreenNode root) {
		mRoot = root;
		resetView();
		
		if(mRoot != null) {
			mRoot.addTouchNode(mScaleNode);
		}
	}
	
	@Override
	public ElfScreenNode getRootNode() {
		return mRoot;
	}
	
	public void setUseControl(boolean control) {
		mUseControl = control;
		if(!mUseControl) {
			resetRootNode();
		}
	}
	public boolean getUseControl() {
		return mUseControl;
	}

	public void resetRootNode() {
		mRoot.stopActions();
		mRoot.setScale(getFixedScale());
		mRoot.setPosition(getPhysicWidth() / 2, getPhysicHeight() / 2);
	}

	public void render() { 
		mRoot.setUseSettedSize(true);
		mRoot.setSize(getPhysicWidth(), getPhysicHeight());
		mRoot.setGLid( this.getGLID() );
		
		mRoot.update();
		mRoot.drawSprite();
		if(mUseControl) {
			mRoot.drawSpriteSelected();
		}
		
		mFps.update();
		mFps.drawSprite();
		
		mScaleNode.update();
		mScaleNode.drawSprite();
	}

	public void setDND(final GLCanvas gLCanvas) {
		DropTarget dropTarget = new DropTarget(gLCanvas, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				if(BasicGLViewRender.this.getPauseGL()) {
					event.operations = DND.DROP_NONE;
					return;
				}
				
				if(!mUseControl) {
					event.operations = DND.DROP_NONE;
					return;
				}
				
				if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
					final String[] drops = (String[]) event.data;
					if (drops != null && drops.length > 0) {
						final LinkedList<String> list = new LinkedList<String>();
						for (String path : drops) {
							addFile(path, list);
						}

						final ElfNode bindNode = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode();
						final ElfNode selectNode = mRoot.getSelectNode();
						if (bindNode != null) {
							for (final String image : list) {
								final ElfNode node;
								if (ResJudge.isPlist(image)) {
									final ProjectSetting screen = PowerMan.getSingleton(ProjectSetting.class);

									final ListNode viewer = new ListNode(bindNode, -1);
									viewer.onCreateRequiredNodes();
									viewer.getContainer().setCalcSizeWithScale(true);
									viewer.getContainer().setAutoLayout(true);
									viewer.getContainer().setOrientation(Orientation.Horizontal);
									viewer.setAutoOutOfBounds(true);
									viewer.setSize(screen.getLogicWidth(), screen.getLogicHeight());

									final LinearLayout2DNode layout = new LinearLayout2DNode(viewer.getContainer(), -1);

									layout.setDesignedLength(screen.getLogicHeight());
									layout.setOrientation(Orientation.Vertical);
									layout.addToParent();

									node = viewer;
									final int index = FileHelper.getDirPath(image).length();
									final String nodeName = ResJudge.getLittleName(image.substring(index + 1));
									node.setName(nodeName.substring(0, nodeName.length() - 6));

									final LinkedList<String> resids = TpPlistScaner.readResidsFromPlist(image, FileHelper.getDirPath(image), false);
									for (String id : resids) {
										final ElfNode child = new ElfNode(layout, Integer.MAX_VALUE);
										child.setResid(id);
										child.setName(ResJudge.getLittleName(child.getResid()));
										child.addToParent();
									}
									PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addNode(bindNode, node, Integer.MAX_VALUE, false);
									PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(node);
									
								} else if (ResJudge.isXML(image) || ResJudge.isCocos(image)) {

									MessageDialog message = new MessageDialog();
									final boolean add;
									if (ResJudge.isXML(image)) {
										add = message.open("Open XML File", "Add " + image + "?", "", "");
									} else {
										add = message.open("Open Cocos File", "Add " + image + "?", "", "");
									}

									if (add) {
										ImExport.add(image);
									} else {
										ImExport.imports(image, true);
									}
								} else if (ResJudge.isRes(image)) {
									node = new ElfNode(bindNode, -1);
									node.setResid(image);
									node.setName(ResJudge.getLittleName(image));
									node.setVisible(true);
									if (selectNode != null) {
										PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addNode(selectNode, node, Integer.MAX_VALUE, false);
										PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(node);
									} else {
										PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addNode(bindNode, node, Integer.MAX_VALUE, false);
										PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(node);
									}
								}
							}
						}
					}
				}
			}
		});
	}
	
	
	public void keyPressed(KeyEvent e) {
		Boolean justPressed = mKeyMap.get(e.keyCode);
		mKeyMap.put(e.keyCode, true);
		
		if(this.getPauseGL()) {
			return;
		}
		
		if(!mUseControl) {
			return;
		}
		
		if (e.keyCode == PlatformHelper.SPACE) {
			Cursor cs = Display.getCurrent().getSystemCursor(SWT.CURSOR_HAND);  
			this.getGLView().getShell().setCursor(cs);
		}
		
		switch (e.keyCode) {
		case 'r':
		case 'R':
			if ((e.stateMask & PlatformHelper.CTRL) != 0) {
				resetRootNode();
			}
			break;
		case SWT.F7:
			mScaleNode.toggle();
			break;
		case SWT.F8:
			mFps.toggleVisible();
			break;
		case 'F':
		case 'f':
			if ((e.stateMask & PlatformHelper.CTRL) != 0) {
				SearchMenu.searchNodes();
			}
			break;
		case 'E':
		case 'e':
			if ((e.stateMask & PlatformHelper.CTRL) != 0) {
				NodeViewWorkSpaceTab.expandedNodes();
			}
			break;
		case 'P':
		case 'p':
			if ((e.stateMask & PlatformHelper.CTRL) != 0) {
				SearchMenu.searchPlists();
			}
			break;
		case 'N':
		case 'n':
			if ((e.stateMask & PlatformHelper.CTRL) != 0) {
				final ElfNode node = PowerMan.getSingleton(DataModel.class).getSelectNode();
				if (node != null) {
					final ElfPointf pos = node.getPositionInScreen();
					final ElfPointf target = getFixedCenter();
					node.setVisible(true);
					mRoot.runElfAction(ElfInterAction.action(MoveBy.action(sAnimateTime, target.x - pos.x, target.y - pos.y), sAnimateInter));
				}
			}
			break;
		case 'H':
		case 'h':
			FileBar.onShortcutKeys();
			break;
		}
		
		if(PlatformHelper.DEL == e.keyCode) {
			UndoRedoManager.checkInUndo();
			
			final NodeViewWorkSpaceTab nodeView = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
			if ((e.stateMask & PlatformHelper.CTRL) != 0) {
				nodeView.deleteNodes();
			} else {
				nodeView.recycleNodes();
			}
		}

		else if (e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT) {
			final ArrayList<ElfNode> list = mRoot.getSelectNodeList();
			float rate = (e.stateMask & PlatformHelper.CTRL) != 0 ? 4 : 1;
			final float offX;
			final float offY;
			switch (e.keyCode) {
			case SWT.ARROW_DOWN:
				offX = 0;
				offY = -rate;
				break;
			case SWT.ARROW_UP:
				offX = 0;
				offY = rate;
				break;
			case SWT.ARROW_LEFT:
				offX = -rate;
				offY = 0;
				break;
			case SWT.ARROW_RIGHT:
				offX = rate;
				offY = 0;
				break;
			default:
				offX = 0;
				offY = 0;
			}
			
			if(justPressed == null || justPressed == false) {
				UndoRedoManager.checkInUndo();
			}
			
			for (final ElfNode node : list) {
				node.onArrowKey(offX, offY);
			}
		} 
//		else if (e.stateMask == PlatformHelper.CTRL && (e.keyCode == 'z' || e.keyCode == 'Z')) {
//			UndoRedoManager.onUndo();
//		} else if (e.stateMask == PlatformHelper.CTRL && (e.keyCode == 'y' || e.keyCode == 'Y')) {
//			UndoRedoManager.onRedo();
//		} 
//		else if (e.stateMask == PlatformHelper.CTRL && (e.keyCode == 's' || e.keyCode == 'S')) {
//			
//			final String path = PowerMan.getSingleton(DataModel.class).getLastImportPath();
//			final MenuConfig menuConfig = PowerMan.getSingleton(MenuConfig.class);
//			if (path != null) {
//				ImExport.exports(path, menuConfig.AutoBatch);
//			}
//		} 
		else if (e.stateMask == PlatformHelper.SHIFT && (e.keyCode == 'x' || e.keyCode == 'X' || e.keyCode == 'y' || e.keyCode == 'Y')) {

			final ElfNode node = PowerMan.getSingleton(DataModel.class).getSelectNode();
			final ArrayList<ElfNode> list = new ArrayList<ElfNode>(PowerMan.getSingleton(DataModel.class).getSelectNodeList());
			if (node != null && list.size() >= 2) {
				final ElfPointf newPos = node.getPositionInScreen();

				final ElfPointf[] oldsPosArray = new ElfPointf[list.size()];
				for (int i = 0; i < list.size(); i++) {
					oldsPosArray[i] = list.get(i).getPositionInScreen();
				}

				if (e.keyCode == 'x' || e.keyCode == 'X') {
					UndoRedoManager.checkInUndo();
					
					for (int i = 0; i < list.size(); i++) {
						final ElfNode aNode = list.get(i);
						aNode.setPositionInScreen(new ElfPointf(oldsPosArray[i].x, newPos.y));
					}
				} else if (e.keyCode == 'y' || e.keyCode == 'Y') {
					UndoRedoManager.checkInUndo();
					
					for (int i = 0; i < list.size(); i++) {
						final ElfNode aNode = list.get(i);
						aNode.setPositionInScreen(new ElfPointf(newPos.x, oldsPosArray[i].y));
					}
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		mKeyMap.put(e.keyCode, false);
		
		if(this.getPauseGL()) {
			return;
		}
		
		if (e.keyCode == PlatformHelper.SPACE) {
			this.getGLView().getShell().setCursor(null);
		}
		
		if(!mUseControl) {
			return;
		}
	}

	public void mouseScrolled(MouseEvent e) {
		if(this.getPauseGL()) {
			return;
		}
		
		if(!mUseControl) {
			return;
		}
		
		if ((e.stateMask & PlatformHelper.CTRL) != 0 || true) {
			Integer index = (Integer) mRoot.getData(ViewScaleKey);
			if(index == null) {
				mRoot.setData(ViewScaleKey, 2);
				index = 2;
			}
			if (e.count > 0) {
				index = MathHelper.clamp(index + 1, 0, mPercentages.length - 1);
			} else if (e.count < 0) {
				index = MathHelper.clamp(index - 1, 0, mPercentages.length - 1);
			}

			mRoot.setData(ViewScaleKey, index);

			final ElfPointf oldScale = mRoot.getScale();
			final ElfPointf oldPosition = mRoot.getPosition();
			final ElfPointf center = getFixedCenter();

			final ElfPointf scale = getFixedScale();
			final ElfPointf newScale = new ElfPointf(scale.x * mPercentages[index], scale.y * mPercentages[index]);
			final float offx = (center.x - oldPosition.x) * ((newScale.x - oldScale.x) / oldScale.x);
			final float offy = (center.y - oldPosition.y) * ((newScale.y - oldScale.y) / oldScale.y);

			mRoot.runElfAction(ElfInterAction.action(ScaleTo.action(sAnimateTime, newScale.x, newScale.y), sAnimateInter));
			// mRoot.setScale(newScale);
			mRoot.runAction(ElfInterAction.action(MoveBy.action(sAnimateTime, -offx, -offy), sAnimateInter));
			// mRoot.translate(-offx, -offy);

			if (mOldText != null) {
				mOldText.removeFromParent();
			}

			final StrokeTextNode text = new StrokeTextNode(mRoot, Integer.MAX_VALUE);
			text.setText(String.format("View Scale %.2f", mPercentages[index]));
			text.setTextStyle(TextStyle.Bold);
			text.setScale(1 / mPercentages[index], 1 / mPercentages[index]);

			text.setUseModifier(true);
			text.addModifier(new SequenceModifier(new AlphaModifier(0, 1, 600, 600, null, null), new EmptyModifier(0, 1, 500, 500, null, null), new AlphaModifier(1, 0, 600, 600, null, null), new LifeModifier(100)));
			text.onRecognizeRequiredNodes();
			text.addToParent();

			mOldText = text;
		}
	}
	
	public boolean getCharKeyPressed(final int keycode) {
		final Boolean ret = mKeyMap.get(keycode);
		return ret != null && ret;
	}

	public void mouseMove(MouseEvent event) {
		if(this.getPauseGL()) {
			return;
		}
		
		mousePosition(event); 
		if ((mLastMouseDown & LEFT_DOWN) != 0) {
			final Boolean space = mKeyMap.get(PlatformHelper.SPACE);
			if (space != null && space) {
				mRoot.translate(event.x - mLastMousePos.x, mLastMousePos.y - event.y);
			} else {
				mRoot.onTouch(new MotionEvent(event.x, event.y, MotionEvent.LEFT_MOVE, event.stateMask));
			}
		} else if ((mLastMouseDown & MID_DOWN) != 0) {
		} else if ((mLastMouseDown & RIGHT_DOWN) != 0) { 
			if(getUseControl()) {
				mRoot.onTouch(new MotionEvent(event.x, event.y, MotionEvent.RIGHT_MOVE, event.stateMask));
			} 
		} else if (event.button == 2) {
		}

		mLastMousePos.setPoint(event.x, event.y);
	}

	public void mouseDoubleClick(MouseEvent event) {
		if(this.getPauseGL()) {
			return;
		}
		
		mousePosition(event);

		if (event.button == 1 && getUseControl()) {
			mRoot.onTouch(new MotionEvent(event.x, event.y, MotionEvent.LEFT_DOUBLE_CLICK, event.stateMask));
		} else if (event.button == 2) {
		} else if (event.button == 3) {
			// masked
			// mRoot.onTouch(new MotionEvent(event.x, event.y,
			// MotionEvent.RIGHT_DOUBLE_CLICK, event.stateMask));
			// PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes();
		}
	}

	public void mouseDown(MouseEvent event) {
		if(this.getPauseGL()) {
			return;
		}
		
		mousePosition(event);
		if (event.button == 1) {
			mLastMouseDown |= LEFT_DOWN;
			mRoot.onTouch(new MotionEvent(event.x, event.y, MotionEvent.LEFT_DOWN, event.stateMask));
		} else if (event.button == 2) {
			mLastMouseDown |= MID_DOWN;
		} else if (event.button == 3) {
			mLastMouseDown |= RIGHT_DOWN;
			
			if( getUseControl() ) {
				final MotionEvent me = new MotionEvent(event.x, event.y, MotionEvent.RIGHT_DOWN, event.stateMask);
				final ElfEvent newEvent = ElfEvent.obtain(me, mRoot.getHeight());
				mSelectNodeList.clear();
				mRoot.touchIterate(new IIterateChilds() {
					public boolean iterate(ElfNode node) {
						if (node.isRealVisible(newEvent) && node.isInSelectSize(newEvent)) {
							mSelectNodeList.add(node);
						}
						return false;
					}
				});
				newEvent.recycle();
			} 
		} 
		mLastMousePos.setPoint(event.x, event.y);
	}

	public void mouseUp(MouseEvent event) {
		if(this.getPauseGL()) {
			return;
		}
		
		mousePosition(event);

		if (event.button == 1) {
			mLastMouseDown &= ~LEFT_DOWN;
			mRoot.onTouch(new MotionEvent(event.x, event.y, MotionEvent.LEFT_UP, event.stateMask));
		} else if (event.button == 2) {
			mLastMouseDown &= ~MID_DOWN;
		} else if (event.button == 3) {
			mLastMouseDown &= ~RIGHT_DOWN;
			if(getUseControl()) {
				mRoot.onTouch(new MotionEvent(event.x, event.y, MotionEvent.RIGHT_UP, event.stateMask));
			}
		}
		mLastMousePos.setPoint(event.x, event.y);
	}

	private Label getStatusLabel() {
		return PowerMan.getSingleton(StatusViewWorkSpace.class).getLable(1);
	}

	void mousePosition(MouseEvent event) {
		final Label label = getStatusLabel();
		final ElfNode node = mRoot.getSelectNode();
		if (node != null) {
			final ElfPointf ret = new ElfPointf();
			final ElfEvent myEvent = ElfEvent.obtain(new MotionEvent(event.x, event.y, MotionEvent.RIGHT_MOVE, event.stateMask), getPhysicHeight());
			node.screenXYToChild(myEvent.x, myEvent.y, ret);
			myEvent.recycle();
			label.setText("in " + node.getName() + " : " + ret.x + "," + ret.y);
		} else {
		}
	}

	void addFile(String path, List<String> list) {
		final File file = new File(path);

		if (file.isDirectory()) {
			String[] strs = file.list();
			for (String s : strs) {
				addFile(path + FileHelper.DECOLLATOR + s, list);
			}
		} else {
			if (ResJudge.isRes(path)) {
				list.add(path);
			} else if (ResJudge.isPlist(path)) {
				list.add(path);
			} else if (ResJudge.isXML(path)) {
				list.add(path);
			} else if (ResJudge.isCocos(path)) {
				list.add(path);
			}
		}
	}

	protected ElfPointf getFixedScale() {
//		final ScaleType scaleType = PowerMan.getSingleton(ProjectConfig.class).Scale;
//		switch (scaleType) {
//		case NO_SCALE:
//			return new ElfPointf(1, 1);
//		case BY_HEIGHT:
//			return new ElfPointf((float) getPhysicHeight() / getLogicHeight(), (float) getPhysicHeight() / getLogicHeight());
//		case BY_WIDTH:
//			return new ElfPointf((float) getPhysicWidth() / getLogicWidth(), (float) getPhysicWidth() / getLogicWidth());
//		case FILL:
//			return new ElfPointf((float) getPhysicWidth() / getLogicWidth(), (float) getPhysicHeight() / getLogicHeight());
//		}
		final ElfPointf ret = PowerMan.getSingleton(ProjectSetting.class).ScreenScale;
		if(ret != null) {
			return ret;
		}
		return new ElfPointf(1,1);
	}

	ElfPointf getFixedCenter() {
		return new ElfPointf(getPhysicWidth() / 2, getPhysicHeight() / 2);
	}

	public int getPhysicWidth() {
		return PowerMan.getSingleton(ProjectSetting.class).getPhysicWidth();
	}

	public int getPhysicHeight() {
		return PowerMan.getSingleton(ProjectSetting.class).getPhysicHeight();
	}

	public int getLogicWidth() {
		return PowerMan.getSingleton(ProjectSetting.class).getLogicWidth();
	}

	public int getLogicHeight() {
		return PowerMan.getSingleton(ProjectSetting.class).getLogicHeight();
	}

	private ArrayList<ElfNode> mSelectNodeList = new ArrayList<ElfNode>();

	public Menu getMenu(final Shell shell) {
		final Menu menu = new Menu(shell, SWT.POP_UP);

		menu.addMenuListener(new MenuListener() {
			public void menuShown(final MenuEvent e) {
				final MenuItem[] items = menu.getItems();
				for (MenuItem item : items) {
					item.dispose();
				}
				
				if(BasicGLViewRender.this.getPauseGL()) {
					return;
				}
				
				if(!getUseControl()) {
					return ;
				}

				final int size = mSelectNodeList.size();
				if (size == 1) {
					final ElfNode node = mSelectNodeList.get(0);
					if (mKeyMap.get(PlatformHelper.CTRL) != null && mKeyMap.get(PlatformHelper.CTRL)) {
						PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addSelectNodes(node);
					} else {
						final boolean select = node.isSelected();
						if (select) {
							PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes();
						} else {
							PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(node);
						}
					}
				} else if (size > 1) {
					for (final ElfNode node : mSelectNodeList) {
						final MenuItem menuItem = new MenuItem(menu, SWT.CHECK);
						menuItem.setText("" + node.getName());
						menuItem.setSelection(node.isSelected());
						menuItem.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e) {
								if ((e.stateMask & PlatformHelper.CTRL) != 0) {
									PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addSelectNodes(node);
								} else {
									final boolean select = menuItem.getSelection();
									if (!select) {
										PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes();
									} else {
										PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(node);
									}
								}
							}

							public void widgetDefaultSelected(SelectionEvent e) {
								widgetSelected(e);
							}
						});
					}
				}
			}

			public void menuHidden(MenuEvent e) {
			}
		});

		return menu;
	}
	
	

	public boolean getFixedSize() {
		return mUseFixedSize;
	}
	
	public void setFixedSize(boolean fixed) {
		mUseFixedSize = fixed;
	}

	@Override
	public int getGLID() {
		return mGLID;
	}

	@Override
	public void setGLID(int id) {
		mGLID = id;
	}

	@Override
	public void resetView() {
		mRoot.setSize(getPhysicWidth(), getPhysicHeight());
		mScaleNode.setSize(getPhysicWidth(), getPhysicHeight());
		this.resetRootNode();
	}

	@Override
	public void setPauseGL(boolean pause) {
		mGLPaused = pause;
		if(!pause) {
			mRoot.setSize(getPhysicWidth(), getPhysicHeight());
			mScaleNode.setSize(getPhysicWidth(), getPhysicHeight());
		}
	}

	@Override
	public boolean getPauseGL() {
		return mGLPaused;
	}

	public IGLView getGLView() {
		return mGLView;
	}

	@Override
	public void setGLView(IGLView view) {
		mGLView = view;
	}
	
	public ElfPointf getLastMousePos() { 
		return new ElfPointf(mLastMousePos);
	}
}
