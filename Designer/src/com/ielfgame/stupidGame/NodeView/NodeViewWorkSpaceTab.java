package com.ielfgame.stupidGame.NodeView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.config.MenuConfig;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfEnum;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashManager;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashMain;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.dialog.PopDialog;
import com.ielfgame.stupidGame.dialog.PopDialog.PopType;
import com.ielfgame.stupidGame.fileBar.FileBar;
import com.ielfgame.stupidGame.newNodeMenu.MainNodeMenu;
import com.ielfgame.stupidGame.newNodeMenu.SearchMenu;
import com.ielfgame.stupidGame.nodeMap.NodeMap;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.undo.UndoRedoManager;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;
import elfEngine.basic.node.nodeAnimate.timeLine.ElfMotionNode;
import elfEngine.basic.node.nodeAnimate.timeLine.MotionWorkSpaceTab;
import elfEngine.basic.node.nodeAnimate.timeLine.TimeData;
import elfEngine.basic.node.particle.Particle;

public class NodeViewWorkSpaceTab extends AbstractWorkSpaceTab {
	private static final String TREE_ITEM_KEY = "TREE_ITEM_KEY";
	private static final String TREE_NODE_KEY = "TREE_NODE_KEY";

	private ElfNode mScreenNode = null;
	private TreeItem mScreenItem = null;

	private ElfNode mRecycleNode = null;
	private TreeItem mRecycleItem = null;

	private Shell mShell = null;
	private IconCache mIconCache = null;
	private Tree mNodeTree = null;

	public ElfNode getScreenNode() {
		return mScreenNode;
	}

	public ElfNode getRecycleNode() {
		return mRecycleNode;
	}

	public NodeViewWorkSpaceTab() {
		super("Node Tree");

		// this.UI_UPDATE_DElAY = 1000;
	}

	public Composite createTab(final CTabFolder parent) {
		mShell = PowerMan.getSingleton(MainDesigner.class).mShell;
		mIconCache = PowerMan.getSingleton(MainDesigner.class).mIconCache;

		mNodeTree = new Tree(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		mNodeTree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				final TreeItem[] selection = mNodeTree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
					setItemImage((ElfNode) item.getData(TREE_NODE_KEY));
				}
				refreshSelects();
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				final TreeItem[] selection = mNodeTree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
					item.setExpanded(!item.getExpanded());

					setItemImage((ElfNode) item.getData(TREE_NODE_KEY));
				}
				refreshSelects();
			}
		});
		mNodeTree.addTreeListener(new TreeAdapter() {
			public void treeExpanded(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				setItemImage((ElfNode) item.getData(TREE_NODE_KEY));
			}

			public void treeCollapsed(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				setItemImage((ElfNode) item.getData(TREE_NODE_KEY));
			}
		});

		mNodeTree.setMenu(createPopUpMenu(mNodeTree));
		initScreenAndRecycle();
		setDND();
		run();

		this.bindScreen(mScreenNode);

		// final String path =
		// PowerMan.getSingleton(DataModel.class).getLastImportPath();
		// if (ResJudge.isXML(path)) {
		// mShell.getDisplay().timerExec(500, new Runnable() {
		// public void run() {
		// ImExport.imports(path, true);
		// }
		// });
		// }

		mNodeTree.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.keyCode) {
				case 'F':
				case 'f':
					if ((e.stateMask & PlatformHelper.CTRL) != 0) {
						SearchMenu.searchNodes();
					}
					break;
				case 'P':
				case 'p':
					if ((e.stateMask & PlatformHelper.CTRL) != 0) {
						SearchMenu.searchPlists();
					}
					break;
				case 'H':
				case 'h':
					FileBar.onShortcutKeys();
					break;
				}

				TreeItem[] selects = mNodeTree.getSelection();
				if (selects != null && selects.length > 0) {
					final ElfNode node = (ElfNode) selects[0].getData(TREE_NODE_KEY);
					switch (e.keyCode) {
					case SWT.F2:
						entry(PopType.RENAME);
						break;
					case SWT.F3:
						entry(PopType.COPY);
						break;
					case SWT.F4:
						entry(PopType.COPY_DEEP);
						break;
					case SWT.F5:
						PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
							public void run() {
								node.myRefresh();
							}
						}, 0);
						break;
					case SWT.F6:
						PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
							public void run() {
								node.refresh();
							}
						}, 0);
						break;
					case SWT.DEL:

						break;
					case 'E':
					case 'e':
						if ((e.stateMask & PlatformHelper.CTRL) != 0) {
							expandedNodes();
						}
						break;
					case 'N':
					case 'n':
						UndoRedoManager.checkInUndo();
						newNode();
						break;
					}

					if (PlatformHelper.DEL == e.keyCode) {
						UndoRedoManager.checkInUndo();
						if ((e.stateMask & PlatformHelper.CTRL) != 0) {
							deleteNodes();
						} else {
							recycleNodes();
						}
					}
				}
			}
		});

		return mNodeTree;
	}

	public final static void expandedNodes() {
		final List<ElfNode> list = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		for (ElfNode node : list) {
			setExpand(node, true);
		}
	}

	final static ElfEnum sElfEnum = new ElfEnum();

	public static void newNode() {

		final List<ElfNode> parents = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		if (!parents.isEmpty()) {

			final MultiLineDialog dialg = new MultiLineDialog();

			dialg.setTitle("New Node");
			dialg.setLabels(new String[] { "Type", "Name" });
			dialg.setValues(new Object[] { sElfEnum, sElfEnum.getName() });
			dialg.setValueTypes(new Class<?>[] { ElfEnum.class, String.class });

			final Object[] ret = dialg.open();
			if (ret != null) {
				sElfEnum.setName((String) ret[1]);
				final String type = sElfEnum.getCurrent();
				final Class<? extends ElfNode> _class = NodeMap.getNodeClass(type);
				if (_class != null) {
					for (ElfNode parent : parents) {
						newNode(parent, _class, sElfEnum.getName());
					}
				} else {
					MessageDialog message = new MessageDialog();
					message.open("Error", String.format("%s Is Not A Node Type!", type));
				}
			}
		}
	}

	public static void newNode(final ElfNode parent, final Class<? extends ElfNode> _class, final String name) {
		try {
			final Constructor<? extends ElfNode> constructor = _class.getConstructor(ElfNode.class, int.class);
			final ElfNode newNode = constructor.newInstance(parent, -1);

			newNode.onCreateRequiredNodes();
			newNode.setName(name);

			final NodeViewWorkSpaceTab nodeViewWorkSpaceTab = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
			nodeViewWorkSpaceTab.sysName(parent);
			nodeViewWorkSpaceTab.addNode(parent, newNode, Integer.MAX_VALUE, true);
			// setExpand(parent, true);
			PowerMan.getSingleton(NodeViewWorkSpaceTab.class).setSelectNodes(newNode);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteNodes() {
		final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		for (final ElfNode _node : nodes) {
			if (!isScreenOrRecycleNode(_node)) {
				removeNode(_node);
			}
		}
	}

	public void recycleNodes() {
		final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		for (final ElfNode _node : nodes) {
			if (!isScreenOrRecycleNode(_node)) {
				removeNode(_node);

				final ElfNode recycleNode = getRecycleNode();
				if (!recycleNode.isRecurFatherOf(_node)) {
					addNode(recycleNode, _node, Integer.MAX_VALUE, true);
				}
			}
		}
	}

	public void removeNodeItem(final ElfNode node) {
		this.removeNode(node);
	}

	private Menu createPopUpMenu(final Tree tree) {
		final MainNodeMenu mainNodeMenu = new MainNodeMenu();
		Menu popUpMenu = mainNodeMenu.create(mShell);

		// init after NodeMap init
		sElfEnum.setName("#");
		final Set<String> set = NodeMap.getKeySet();
		final String[] enums = new String[set.size()];
		set.toArray(enums);
		Arrays.sort(enums);
		sElfEnum.setEnums(enums);
		sElfEnum.setCurrent("ElfNode");

		return popUpMenu;
	}

	private final static int getDeep(ElfNode node) {
		int deep = 0;
		while (node != null) {
			deep++;
			node = node.getParent();
		}
		return deep;
	}

	private final static int getChildrenOrder(final ElfNode parent, final ElfNode child1, final ElfNode child2) {
		if (parent == null) {
			return 0;
		}
		final ElfNode[] childs = parent.getChilds();
		for (final ElfNode child : childs) {
			if (child == child1) {
				return -1;
			} else if (child == child2) {
				return 1;
			}
		}
		return 0;
	}

	private final static int compare(ElfNode arg0, ElfNode arg1, int deep0, int deep1) {
		if (deep0 > deep1) {
			if (arg0.getParent() == arg1) {
				return 1;
			} else {
				return compare(arg0.getParent(), arg1, deep0 - 1, deep1);
			}
		} else if (deep0 < deep1) {
			if (arg1.getParent() == arg0) {
				return -1;
			} else {
				return compare(arg0, arg1.getParent(), deep0, deep1 - 1);
			}
		} else {
			if (arg0 == arg1) {
				return 0;
			} else if (arg0.getParent() == arg1.getParent()) {
				return getChildrenOrder(arg0.getParent(), arg0, arg1);
			} else {
				return compare(arg0.getParent(), arg1.getParent(), deep0 - 1, deep1 - 1);
			}
		}
	}

	public final static Comparator<ElfNode> NodeComparator = new Comparator<ElfNode>() {
		public int compare(ElfNode arg0, ElfNode arg1) {
			return NodeViewWorkSpaceTab.compare(arg0, arg1, getDeep(arg0), getDeep(arg1));
		}
	};

	public void entry(final PopType type) {
		final ArrayList<ElfNode> selects = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();

		if (selectNode == null) {
			return;
		}

		else if (selects.size() == 1) {
			PopDialog dialog = new PopDialog(mShell, selectNode, type);
			final String[] values = dialog.open();
			if (values == null || values.length == 0) {
				return;
			}

			switch (type) {
			case RENAME: {
				UndoRedoManager.checkInUndo();

				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				for (ElfNode node : nodes) {
					node.setName(values[0]);
					sysName(node);
				}
			}

				break;
			case COPY: {
				if (!this.isScreenOrRecycleNode(selectNode)) {
					UndoRedoManager.checkInUndo();

					final ElfNode newNode2 = selectNode.copySelf(selectNode.getParent());
					newNode2.onRecognizeRequiredNodes();

					newNode2.iterateChildsDeep(new IIterateChilds() {
						public boolean iterate(ElfNode node) {
							node.onRecognizeRequiredNodes();
							return false;
						}
					});

					newNode2.setName(values[0]);
					addNode((ElfNode) selectNode.getParent(), newNode2, Integer.MAX_VALUE, true);
					sysName(selectNode);
				}
			}

				break;
			case COPY_DEEP: {
				if (!this.isScreenOrRecycleNode(selectNode)) {
					UndoRedoManager.checkInUndo();

					final ElfNode newNode2 = selectNode.copyDeep();
					newNode2.onRecognizeRequiredNodes();

					newNode2.iterateChildsDeep(new IIterateChilds() {
						public boolean iterate(ElfNode node) {
							node.onRecognizeRequiredNodes();
							return false;
						}
					});

					newNode2.setName(values[0]);
					addNode((ElfNode) selectNode.getParent(), newNode2, Integer.MAX_VALUE, true);
					sysName(selectNode);
				}
			}
				break;
			}
			return;
		}

		switch (type) {
		case RENAME: {
			PopDialog dialog = new PopDialog(mShell, selectNode, type);
			final String[] values = dialog.open();
			if (values == null || values.length == 0) {
				return;
			}

			UndoRedoManager.checkInUndo();
			final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
			for (ElfNode node : nodes) {
				node.setName(values[0]);
				sysName(node);
			}
		}

			break;
		case COPY: {
			final ArrayList<ElfNode> selectList = new ArrayList<ElfNode>(selects);
			Collections.sort(selectList, NodeComparator);
			
			UndoRedoManager.checkInUndo();
			
			for (ElfNode select : selectList) {
				if (!this.isScreenOrRecycleNode(select)) {
					final ElfNode newNode2 = select.copySelf(select.getParent());
					newNode2.onRecognizeRequiredNodes();

					newNode2.iterateChildsDeep(new IIterateChilds() {
						public boolean iterate(ElfNode node) {
							node.onRecognizeRequiredNodes();
							return false;
						}
					});

					final MenuConfig menuConfig = PowerMan.getSingleton(MenuConfig.class);
					if (menuConfig.AutoNameWhenCopy) {
						newNode2.setName(newNode2.getCopyName(newNode2.getParent()));
					} else {
						newNode2.setName(newNode2.getName());
					}

					// newNode2.setName(values[i]);
					addNode((ElfNode) select.getParent(), newNode2, Integer.MAX_VALUE, true);
					sysName(select);
				}
			}
		}

			break;
		case COPY_DEEP:

		{
			final ArrayList<ElfNode> selectList = new ArrayList<ElfNode>(selects);
			Collections.sort(selectList, NodeComparator);
			
			UndoRedoManager.checkInUndo();

			for (ElfNode select : selectList) {
				if (!this.isScreenOrRecycleNode(select)) {
					final ElfNode newNode2 = select.copyDeep();
					newNode2.onRecognizeRequiredNodes();

					newNode2.iterateChildsDeep(new IIterateChilds() {
						public boolean iterate(ElfNode node) {
							node.onRecognizeRequiredNodes();
							return false;
						}
					});

					final MenuConfig menuConfig = PowerMan.getSingleton(MenuConfig.class);
					if (menuConfig.AutoNameWhenCopy) {
						newNode2.setName(newNode2.getCopyName(newNode2.getParent()));
					} else {
						newNode2.setName(newNode2.getName());
					}

					addNode((ElfNode) select.getParent(), newNode2, Integer.MAX_VALUE, true);
					sysName(select);
				}
			}
		}
		}
	}

	private final void refreshSelects() {
		final ArrayList<ElfNode> selectList = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		for (ElfNode node : selectList) {
			node.setSelected(false);
		}

		selectList.clear();

		final ElfNode bindNode = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode();
		if (bindNode != null) {
			final TreeItem[] items = mNodeTree.getSelection();
			for (TreeItem item : items) {
				final ElfNode node = (ElfNode) item.getData(TREE_NODE_KEY);
				node.setSelected(true);
				selectList.add(node);
			}
		}

		showSelection();
	}

	public final void setSelects(List<ElfNode> list) {
		TreeItem[] items = mNodeTree.getSelection();
		if (items != null) {
			for (TreeItem item : items) {
				mNodeTree.deselect(item);
			}
		}

		for (ElfNode node : list) {
			TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
			mNodeTree.setSelection(item);
		}
		refreshSelects();
	}

	private final void initScreenAndRecycle() {
		if (mScreenItem == null) {
			mScreenNode = PowerMan.getSingleton(DataModel.class).getRootScreen();
			mScreenItem = new TreeItem(mNodeTree, SWT.NONE);
			mScreenItem.setData(TREE_NODE_KEY, mScreenNode);
			mScreenItem.setText(mScreenNode.toString());
			mScreenNode.setData(TREE_ITEM_KEY, mScreenItem);
			setItemImage(mScreenNode);
		}

		if (mRecycleItem == null) {
			mRecycleNode = PowerMan.getSingleton(DataModel.class).getRootRecycle();
			mRecycleItem = new TreeItem(mNodeTree, SWT.NONE);
			mRecycleItem.setData(TREE_NODE_KEY, mRecycleNode);
			mRecycleItem.setText(mRecycleNode.toString());
			mRecycleNode.setData(TREE_ITEM_KEY, mRecycleItem);

			setItemImage(mRecycleNode);
		}

	}

	public final void bindScreen(ElfNode node) {
		// unbind
		final ElfNode oldNode = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode();

		// bind
		PowerMan.getSingleton(DataModel.class).getScreenNode().bindNode(node);

		setItemImage(node);
		setItemImage(oldNode);

		if (node != null) {
			final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
			if (item != null && !item.isDisposed()) {
				item.setExpanded(true);
			}
		}
	}

	public final void bindMotion(ElfMotionNode node) {
		TimeData.cleanGlobal();
		final ElfMotionNode oldNode = PowerMan.getSingleton(MotionWorkSpaceTab.class).getMotionNode();
		if (oldNode == node) {
			PowerMan.getSingleton(MotionWorkSpaceTab.class).setMotionNode(null);
		} else {
			PowerMan.getSingleton(MotionWorkSpaceTab.class).setMotionNode(node);
		}

		setItemImage(node);
		setItemImage(oldNode);

		if (node != null) {
			final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
			if (item != null && !item.isDisposed()) {
				item.setExpanded(true);
			}
		}
	}

	public final void bindFlash(IFlashMain flash) {
		FlashManager fm = PowerMan.getSingleton(FlashManager.class);

		final IFlashMain oldFlash = fm.getFlashMain();

		fm.bindFlashMain(flash);

		if (flash instanceof ElfNode) {
			final ElfNode node = (ElfNode) flash;

			setItemImage(node);

			if (oldFlash instanceof ElfNode) {
				setItemImage((ElfNode) oldFlash);
			}

			if (node != null) {
				final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
				if (item != null && !item.isDisposed()) {
					item.setExpanded(true);
				}
			}
		}
	}

	public static final void setExpand(ElfNode node, boolean expand) {
		if (expand) {
			while (node != null) {
				final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
				if (item != null) {
					item.setExpanded(true);
				}

				node = (ElfNode) node.getParent();
			}
		} else {
			final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
			item.setExpanded(false);
			node.iterateChildsDeep(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
					item.setExpanded(false);
					return false;
				}
			});
		}
	}

	public final void removeNode(final ElfNode node) {
		node.removeFromParent();
		final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
		if (item != null && !item.isDisposed()) {
			item.dispose();
		}
		node.setData(TREE_ITEM_KEY, null);
	}

	public final void recycleNode(final ElfNode _node) {
		removeNode(_node);
		final ElfNode recycleNode = getRecycleNode();
		if (!recycleNode.isRecurFatherOf(_node)) {
			addNode(recycleNode, _node, Integer.MAX_VALUE, true);
		}
	}

	public void addNode(final ElfNode father, final ElfNode node, int index, boolean anywayAdd) {
		if (node.isRecurFatherOf(father) || node == father) {
			return;
		}
		
		final TreeItem fatherItem = (TreeItem) father.getData(TREE_ITEM_KEY);
		if (fatherItem == null || fatherItem.isDisposed()) {
			// just add
			if (anywayAdd) {
				node.setParent(father);
				node.addToParent(index);
				
				System.err.println("Just Add!");
			}

			return;
		}

		int size = fatherItem.getItemCount();
		index = Math.min(index, size);
		index = Math.max(index, 0);
		final TreeItem newItem = new TreeItem(fatherItem, SWT.NONE, index);

		newItem.setText(node.toString());
		newItem.setData(TREE_NODE_KEY, node);

		node.setParent(father);
		node.addToParent(index);
		node.setData(TREE_ITEM_KEY, newItem);

		// fatherItem.setExpanded(true);
		setItemImage(node);
		setItemImage(father);

		node.iterateChildsDeepForFace(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if (!(node instanceof Particle)) {
					final ElfNode fatherNode = (ElfNode) node.getParent();
					final TreeItem fatherItem = (TreeItem) fatherNode.getData(TREE_ITEM_KEY);

					final TreeItem oldItem = (TreeItem) node.getData(TREE_ITEM_KEY);
					if (oldItem != null && !oldItem.isDisposed()) {
						oldItem.dispose();
					}

					final TreeItem newItem = new TreeItem(fatherItem, SWT.NONE);
					node.setData(TREE_ITEM_KEY, newItem);
					newItem.setData(TREE_NODE_KEY, node);

					newItem.setText(node.toString());
					setItemImage((ElfNode) node);
				}
				return false;
			}
		});
	}

	private final void setItemImage(final ElfNode node) {
		if (node != null) {
			final FlashManager fm = PowerMan.getSingleton(FlashManager.class);

			final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
			if (node != null && item != null && !item.isDisposed()) {
				if (node == fm.getFlashMain()) {
					item.setImage(mIconCache.stockImages[mIconCache.iconCartton]);
				} else if (node == PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode()) {
					item.setImage(mIconCache.stockImages[mIconCache.iconSetAsScence]);
				} else if (item == mRecycleItem || item == mScreenItem) {
					if (item.getExpanded()) {
						item.setImage(mIconCache.stockImages[mIconCache.iconOpenDrive]);
					} else {
						item.setImage(mIconCache.stockImages[mIconCache.iconClosedDrive]);
					}
				} else if (node == fm.getFlashMain()) {
					item.setImage(mIconCache.stockImages[mIconCache.iconCartton]);
				} else if (PowerMan.getSingleton(MotionWorkSpaceTab.class) != null && node == PowerMan.getSingleton(MotionWorkSpaceTab.class).getMotionNode()) {
					item.setImage(mIconCache.stockImages[mIconCache.iconCartton]);
				} else if (PowerMan.getSingleton(MotionWorkSpaceTab.class) != null && node == PowerMan.getSingleton(DataModel.class).getSelectNode()) {
					item.setImage(mIconCache.stockImages[mIconCache.iconSelect]);
				} else {
					if (item != null && !item.isDisposed()) {
						if (item.getExpanded()) {
							item.setImage(mIconCache.stockImages[mIconCache.iconOpenFolder]);
						} else {
							item.setImage(mIconCache.stockImages[mIconCache.iconClosedFolder]);
						}
					}
				}
			}
		}
	}

	// DND
	private final void setDND() {
		final ElfNode[] sourceNode = new ElfNode[1];
		final int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		final Transfer[] types = new Transfer[] { TextTransfer.getInstance() };

		final DropTarget target = new DropTarget(mNodeTree, operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetListener() {
			public void dragOver(DropTargetEvent event) {
				final ElfNode oldNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				final ElfNode newNode = event.item == null ? null : (ElfNode) (event.item).getData(TREE_NODE_KEY);

				event.feedback = 0;

				if (event.item != null) {
					if (oldNode != null && newNode != null && oldNode != newNode && !oldNode.isRecurFatherOf(newNode)) {
						event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
					} else {
						event.feedback = DND.FEEDBACK_SCROLL;
					}
				} else {
					event.feedback = DND.FEEDBACK_SCROLL;
				}

				if (event.item != null) {
					TreeItem item = (TreeItem) event.item;
					Point pt = mNodeTree.getDisplay().map(null, mNodeTree, event.x, event.y);
					Rectangle bounds = item.getBounds();
					if (pt.y < bounds.y + bounds.height / 3 && !isScreenOrRecycleItem(item)) {
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					} else if (pt.y > bounds.y + 2 * bounds.height / 3 && !isScreenOrRecycleItem(item)) {
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					} else {
						event.feedback |= DND.FEEDBACK_SELECT;
					}
				}
			}

			public void drop(DropTargetEvent event) {
				if (event.data == null || event.item == null || sourceNode[0] == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				TreeItem item = (TreeItem) event.item;
				Point pt = mNodeTree.getDisplay().map(null, mNodeTree, event.x, event.y);
				Rectangle bounds = item.getBounds();
				TreeItem parent = item.getParentItem();
				if (parent != null) {
					TreeItem[] items = parent.getItems();
					int index = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
							break;
						}
					}
					// final ElfNode oldNode = sourceNode[0];
					final ElfNode newFatherNode;
					int newIndex;
					if (pt.y < bounds.y + bounds.height / 3) {
						newFatherNode = (ElfNode) parent.getData(TREE_NODE_KEY);
						newIndex = index;
					} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
						newFatherNode = (ElfNode) parent.getData(TREE_NODE_KEY);
						newIndex = index + 1;
					} else {
						newFatherNode = (ElfNode) item.getData(TREE_NODE_KEY);
						newIndex = item.getItemCount() + 1;
					}

					ArrayList<ElfNode> list = PowerMan.getSingleton(DataModel.class).getSelectNodeList();

					final ArrayList<ElfNode> selectList = new ArrayList<ElfNode>(list);
					Collections.sort(selectList, NodeComparator);

					for (ElfNode node0 : selectList) {
						if (!node0.isRecurFatherOf(newFatherNode) && node0 != newFatherNode) {
							final int childIndex = getChildIndex(newFatherNode, node0);
							removeNode(node0);
							if (childIndex >= 0 && childIndex < newIndex) {
								newIndex--;
							}
							addNode(newFatherNode, node0, newIndex++, true);
						}
					}

					setSelects(list);
				} else {
					// final ElfNode oldNode = sourceNode[0];
					final ElfNode newFatherNode = (ElfNode) item.getData(TREE_NODE_KEY);
					int newIndex = item.getItemCount() + 1;

					ArrayList<ElfNode> list = PowerMan.getSingleton(DataModel.class).getSelectNodeList();

					final ArrayList<ElfNode> selectList = new ArrayList<ElfNode>(list);
					Collections.sort(selectList, NodeComparator);

					for (ElfNode node0 : selectList) {
						if (!node0.isRecurFatherOf(newFatherNode) && node0 != newFatherNode) {
							final int childIndex = getChildIndex(newFatherNode, node0);
							removeNode(node0);
							if (childIndex >= 0 && childIndex < newIndex) {
								newIndex--;
							}
							addNode(newFatherNode, node0, newIndex++, true);
						}
					}

					setSelects(list);
				}
				sourceNode[0] = null;
			}

			private final int getChildIndex(final ElfNode parent, final ElfNode child) {
				if (parent == null) {
					return -1;
				} else {
					final ElfNode[] childs = parent.getChilds();
					for (int i = 0; i < childs.length; i++) {
						if (childs[i] == child) {
							return i;
						}
					}
				}
				return -1;
			}

			public void dragEnter(DropTargetEvent event) {
				event.feedback = 0;
			}

			public void dragLeave(DropTargetEvent event) {
				event.feedback = 0;
			}

			public void dragOperationChanged(DropTargetEvent event) {
				event.feedback = 0;
			}

			public void dropAccept(DropTargetEvent event) {
				event.feedback = 0;
			}
		});

		final DragSource source = new DragSource(mNodeTree, operations);
		source.setTransfer(types);
		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				TreeItem[] selection = mNodeTree.getSelection();
				if (selection.length > 0 && !isScreenOrRecycleItem(selection[0])) {
					event.doit = true;
					sourceNode[0] = (ElfNode) selection[0].getData(TREE_NODE_KEY);
				} else {
					event.doit = false;
				}
			};

			public void dragSetData(DragSourceEvent event) {
				event.data = "text";
			}

			public void dragFinished(DragSourceEvent event) {
				sourceNode[0] = null;
			}
		});
	}

	private final boolean isScreenOrRecycleItem(TreeItem item) {
		return item == mScreenItem || item == mRecycleItem;
	}

	public final boolean isScreenOrRecycleNode(ElfNode node) {
		return node == mScreenNode || node == mRecycleNode;
	}

	public final void sysName(final ElfNode node) {
		((TreeItem) node.getData(TREE_ITEM_KEY)).setText(node.toString());
	}

	public void update() {
		updateNames(mScreenNode);
		updateNames(mRecycleNode);
	}

	void updateNames(ElfNode node) {
		node.iterateChildsDeep(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
				if (item != null && !item.isDisposed()) {
					final String oldName = item.getText();
					final String newName = node.toString();

					if (!oldName.equals(newName)) {
						item.setText(newName);
					}
				}
				return false;
			}
		});
	}

	boolean isRecursionChild(final ElfNode father, final ElfNode child) {
		father.iterateChildsDeep(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				return child == node;
			}
		});

		return false;
	}

	public void rebuild() {

		mScreenNode.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				removeNode(node);
				return false;
			}
		});

		// mScreenNode

		mScreenNode.removeAllChilds();
		initScreenAndRecycle();
	}

	public void addToScreenNode(final ElfNode node, int index) {
		addNode(mScreenNode, node, index, true);
	}

	public void setSelectNodes(ElfNode... nodes) {
		final ArrayList<ElfNode> olds = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		for (ElfNode node : olds) {
			node.setSelected(false);
			final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
			if (item != null && !item.isDisposed()) {
				mNodeTree.deselect(item);
			}
		}

		olds.clear();

		for (ElfNode node : nodes) {
			node.setSelected(true);
			final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
			if (item != null && !item.isDisposed()) {
				mNodeTree.select(item);
			}
			olds.add(node);
		}

		// try {
		// final NodeViewWorkSpaceTab tab=
		// PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
		// setExpand(tab.mScreenNode, false);
		// setExpand(tab.mRecycleNode, false);
		// } catch (Exception e) {
		// }

		showSelection();
	}

	private ElfNode mOldSelect = null;

	public void showSelection() {
		final ElfNode select = PowerMan.getSingleton(DataModel.class).getSelectNode();
		if (select != null) {
			final TreeItem item = (TreeItem) select.getData(TREE_ITEM_KEY);
			if (item != null && !item.isDisposed()) {
				mNodeTree.showItem(item);
			}

		}
		setItemImage(mOldSelect);
		mOldSelect = select;
		setItemImage(select);
	}

	public void addSelectNodes(ElfNode... nodes) {
		final ArrayList<ElfNode> olds = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		for (ElfNode node : nodes) {
			node.setSelected(!node.isSelected());
			if (node.isSelected()) {
				final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
				if (item != null && !item.isDisposed()) {
					mNodeTree.select(item);
				}
				olds.add(node);
			} else {
				final TreeItem item = (TreeItem) node.getData(TREE_ITEM_KEY);
				if (item != null && !item.isDisposed()) {
					mNodeTree.deselect(item);
				}
				olds.remove(node);
			}
		}
	}
}