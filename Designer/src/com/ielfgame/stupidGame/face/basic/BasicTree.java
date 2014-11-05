package com.ielfgame.stupidGame.face.basic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.power.APowerManSingleton;
import com.ielfgame.stupidGame.power.PowerMan;

public abstract class BasicTree extends APowerManSingleton implements Runnable {

	public interface IName {
		public String getName();

		public void setName(String name);
	}

	public interface IChild {
		public Object[] getChildsForFace();
	}

	public interface IDataTreeItem {
		public void setTreeItem(TreeItem item);

		public TreeItem getTreeItem();
	}

	public interface ITreeObject extends IName, IChild, IDataTreeItem {
	}

	// menu,
	// add, delete, rename,

	// drag, drop,

	// click, double click,

	// image,

	private BasicMenu mBasicMenu;

	public void setBasicMenu(BasicMenu basicMenu) {
		mBasicMenu = basicMenu;
	}

	private String[] mTreeColumns;
	private int[] mWidths;

	protected void setTreeColumns(final String[] treeColumns, final int[] widths) {
		mTreeColumns = treeColumns;
		mWidths = widths;
		if (mTreeColumns != null && mWidths != null) {
			assert (mTreeColumns.length != mWidths.length);
		}
	}

	protected Tree mTree;

	protected TreeItem mRootTreeItem;
	protected TreeItem mRecycleTreeItem;

	public Tree getTree() {
		return mTree;
	}

	public Tree createTree(final Composite parent, final int style) {
		mTree = new Tree(parent, style);
		final GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		mTree.setLayoutData(data);

		if (mTreeColumns != null) {
			mTree.setHeaderVisible(true);
			for (int i = 0; i < mTreeColumns.length; i++) {
				final TreeColumn colum = new TreeColumn(mTree, SWT.NONE);
				colum.setText(mTreeColumns[i]);
				if (mWidths != null) {
					colum.setWidth(mWidths[i]);
				} else {
					colum.setWidth(1280 / mTreeColumns.length);
				}
			}
		}

		mRootTreeItem = new TreeItem(mTree, 0);
		mRootTreeItem.setText("Root");

		mRecycleTreeItem = new TreeItem(mTree, 0);
		mRecycleTreeItem.setText("Recycle");

		initRootItem(mTree, mRootTreeItem, mRecycleTreeItem);

		setDND();

		mTree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				final TreeItem item = (TreeItem) e.item;
				if (isValid(item)) {
					onClick(item, (ITreeObject) getData(item));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				final TreeItem item = (TreeItem) e.item;
				if (isValid(item)) {
					onDoubleClick(item, (ITreeObject) getData(item));
				}
			}
		});

		mTree.addTreeListener(new TreeAdapter() {
			public void treeExpanded(TreeEvent event) {
				try {
					final TreeItem item = (TreeItem) event.item;
					final Image[] images = (Image[]) item.getData(INSIDE_IMAGE_KEY);
					item.setImage(images[0]);
				} catch (Exception e) {
				}
			}

			public void treeCollapsed(TreeEvent event) {
				try {
					final TreeItem item = (TreeItem) event.item;
					final Image[] images = (Image[]) item.getData(INSIDE_IMAGE_KEY);
					item.setImage(images[1]);
				} catch (Exception e) {
				}
			}
		});

		if (mBasicMenu != null) {
			final Shell shell = PowerMan.getSingleton(MainDesigner.class).mShell;
			mTree.setMenu(mBasicMenu.create(shell));
		}

		// update
		run();

		return mTree;
	}

	public void run() {
		if (mTree != null && !mTree.isDisposed()) {

			{
				final TreeItem[] roots = mRootTreeItem.getItems();
				for (TreeItem item : roots) {
					updateItem(item);
				}
			}

			{
				final TreeItem[] roots = mRecycleTreeItem.getItems();
				for (TreeItem item : roots) {
					updateItem(item);
				}
			}

			final Display display = PowerMan.getSingleton(MainDesigner.class).mShell.getDisplay();
			display.timerExec(200, this);
		}
	}

	void updateItem(final TreeItem item) {
		if (isValid(item)) {
			final ITreeObject data = (ITreeObject) item.getData(INSIDE_DATA_KEY);
			update(item, data);

			final TreeItem[] childs = item.getItems();
			for (TreeItem child : childs) {
				updateItem(child);
			}
		}
	}

	public void addObject(ITreeObject father, ITreeObject child) {
		// if()
		addObject(father, child, father.getChildsForFace().length);
	}

	public abstract void initRootItem(final Tree tree, final TreeItem root, final TreeItem recycle);

	public abstract void addObject(ITreeObject father, ITreeObject child, int index);

	public abstract void removeObject(ITreeObject father, ITreeObject child, int index);

	public abstract void onClick(final TreeItem item, final ITreeObject data);

	public abstract void onDoubleClick(final TreeItem item, final ITreeObject data);

	public abstract void update(final TreeItem item, final ITreeObject data);

	public abstract Image[] getImage(ITreeObject object);

	final static String INSIDE_DATA_KEY = "#data";
	final static String INSIDE_IMAGE_KEY = "#image";

	protected void setData(final TreeItem item, ITreeObject data) {

		if (isValid(item)) {
			item.setData(INSIDE_DATA_KEY, data);
			item.setData(INSIDE_IMAGE_KEY, getImage(data));
		}

		if (data != null) {
			final TreeItem oldItem = data.getTreeItem();
			if (isValid(oldItem)) {
				oldItem.setData(INSIDE_DATA_KEY, null);
			}

			data.setTreeItem(item);
		}
	}

	public ITreeObject getData(final TreeItem item) {
		if (isValid(item)) {
			return (ITreeObject) item.getData(INSIDE_DATA_KEY);
		}
		return null;
	}

	protected final static int TreeItem_Style = SWT.NONE;

	protected void copyBranch(final TreeItem newParent, final TreeItem child, int newIndex) {
		if (isValid(child) && isValid(newParent)) {
			newIndex = Math.min(newIndex, newParent.getItemCount());
			newIndex = Math.max(newIndex, 0);
			final TreeItem item = new TreeItem(newParent, TreeItem_Style, newIndex);
			setData(item, (ITreeObject) getData(child));

			item.setText("" + child.getText());

			final TreeItem[] myChilds = child.getItems();
			if (myChilds != null) {
				for (int i = 0; i < myChilds.length; i++) {
					copyBranch(item, myChilds[i], i);
				}
			}
		}
	}

	// could
	public void disposeBranch(final TreeItem item, boolean clearObject) {
		if (isValid(item) && !isRootItem(item)) {
			final TreeItem[] childs = item.getItems();
			for (TreeItem child : childs) {
				disposeBranch(child, false);
			}
			if (clearObject) {
				final TreeItem parent = item.getParentItem();
				if (parent != null) {
					final int index = parent.indexOf(item);
					this.removeObject(getData(parent), getData(item), index);
				}
			}

			setData(item, null);
			item.dispose();
		}
	}

	public void rebuild() {
		final TreeItem[] roots = this.getRootItems();

		/**
		 * 
		 */

		for (int i = 0; i < roots.length; i++) {
			final TreeItem root = roots[i];
			final TreeItem[] items = root.getItems();
			for (TreeItem item : items) {
				this.disposeBranch(item, false);
			}
		}
	}

	public void addToTree(final ITreeObject fatherObject, final ITreeObject childObject, boolean updateFather) {
		if (fatherObject != null) {
			final TreeItem fatherItem = fatherObject.getTreeItem();
			if (isValid(fatherItem)) {
				final int count = fatherItem.getItemCount();
				this.addToTree(fatherObject, childObject, count, updateFather);
			} else {
				this.addToTree(fatherObject, childObject, 0, updateFather);
			}
		}
	}

	//
	public void addToTree(final ITreeObject fatherObject, final ITreeObject childObject, int index, boolean updateFather) {
		if (fatherObject != null && childObject != null) {
			if (updateFather) {
				addObject(fatherObject, childObject, index);
			}

			final TreeItem fatherItem = fatherObject.getTreeItem();

			if (isValid(fatherItem)) {
				final TreeItem childItem = new TreeItem(fatherItem, TreeItem_Style, index);
				childItem.setText("" + childObject.getName());
				setData(childItem, childObject);

				final Object[] cs = ((IChild) childObject).getChildsForFace();
				for (int i = 0; cs != null && i < cs.length; i++) {
					if (cs[i] instanceof ITreeObject) {
						addToTree(childObject, (ITreeObject) cs[i], i, false);
					}
				}
			}
		}
	}

	//
	public void disposeTree() {
		final TreeItem[] roots = mTree.getItems();
		for (TreeItem root : roots) {
			final TreeItem[] items = root.getItems();
			for (TreeItem item : items) {
				item.dispose();
			}
		}
	}

	protected boolean isRecurFatherOf(final TreeItem father, final TreeItem child) {
		if (isValid(father) && isValid(child)) {
			TreeItem parent = child.getParentItem();
			while (parent != null) {
				if (parent == father) {
					return true;
				}
				parent = parent.getParentItem();
			}
		}

		return false;
	}

	public boolean isRootItem(final TreeItem item) {
		if (isValid(item)) {
			return item.getParentItem() == null;
		}
		return false;
	}

	public boolean isValid(final TreeItem item) {
		return item != null && !item.isDisposed();
	}

	public Object getSelectObject() {
		try {
			return mTree.getSelection()[0].getData(INSIDE_DATA_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Object[] getSelectObjects() {
		try {
			final Object[] ret = new Object[mTree.getSelection().length];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = mTree.getSelection()[i].getData(INSIDE_DATA_KEY);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Object[0];
	}

	public TreeItem getSelectItem() {
		try {
			return mTree.getSelection()[0];
		} catch (Exception e) {
		}

		return null;
	}

	//
	private final void setDND() {

		final int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		final Transfer[] types = new Transfer[] { TextTransfer.getInstance() };

		final DropTarget target = new DropTarget(mTree, operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetListener() {
			public void dragOver(DropTargetEvent event) {
				TreeItem oldItem = null;
				try {
					oldItem = mTree.getSelection()[0];
				} catch (Exception e) {
				}

				final TreeItem newItem = (TreeItem) event.item;

				event.feedback = 0;

				if (isValid(newItem)) {
					if (isValid(oldItem) && oldItem != newItem && !isRecurFatherOf(oldItem, newItem)) {
						event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
					} else {
						event.feedback = DND.FEEDBACK_SCROLL;
					}
				} else {
					event.feedback = DND.FEEDBACK_SCROLL;
				}

				if (isValid(newItem)) {
					Point pt = mTree.getDisplay().map(null, mTree, event.x, event.y);
					Rectangle bounds = newItem.getBounds();
					if (pt.y < bounds.y + bounds.height / 3 && !isRootItem(newItem)) {
						event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
					} else if (pt.y > bounds.y + 2 * bounds.height / 3 && !isRootItem(newItem)) {
						event.feedback |= DND.FEEDBACK_INSERT_AFTER;
					} else {
						event.feedback |= DND.FEEDBACK_SELECT;
					}
				}
			}

			public void drop(DropTargetEvent event) {
				TreeItem dropItem = null, oldItemFather = null;
				int oldIndex = 0;
				try {
					dropItem = mTree.getSelection()[0];
					oldItemFather = dropItem.getParentItem();
					oldIndex = oldItemFather.indexOf(dropItem);
				} catch (Exception e) {
				}
				TreeItem newFatherItem = (TreeItem) event.item;

				if ((!isValid(newFatherItem)) || (!isValid(dropItem)) || (!isValid(oldItemFather))) {
					event.detail = DND.DROP_NONE;
					return;
				}

				final ITreeObject oldFatherObject = getData(oldItemFather);
				final ITreeObject dropObject = getData(dropItem);
				ITreeObject newFatherObject = getData(newFatherItem);

				Point pt = mTree.getDisplay().map(null, mTree, event.x, event.y);
				Rectangle bounds = newFatherItem.getBounds();
				TreeItem parent = newFatherItem.getParentItem();

				if (parent != null) {
					TreeItem[] items = parent.getItems();
					int index = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == newFatherItem) {
							index = i;
							break;
						}
					}

					final int newIndex;
					if (pt.y < bounds.y + bounds.height / 3) {
						newFatherItem = parent;
						newFatherObject = BasicTree.this.getData(parent);
						newIndex = index;
					} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
						newFatherItem = parent;
						newFatherObject = BasicTree.this.getData(parent);
						newIndex = index + 1;
					} else {
						newFatherObject = BasicTree.this.getData(newFatherItem);
						newIndex = newFatherItem.getItemCount() + 1;
					}

					if (!isRecurFatherOf(dropItem, newFatherItem) && dropItem != newFatherItem) {
						removeObject(oldFatherObject, dropObject, oldIndex);
						addObject(newFatherObject, dropObject, newIndex);

						// item
						copyBranch(newFatherItem, dropItem, newIndex);
						disposeBranch(dropItem, false);
					}
				} else {
					final int newIndex = newFatherItem.getItemCount() + 1;

					removeObject(oldFatherObject, dropObject, oldIndex);
					addObject(newFatherObject, dropObject, newIndex);

					// item
					copyBranch(newFatherItem, dropItem, newIndex);
					disposeBranch(dropItem, false);
				}
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

		final DragSource source = new DragSource(mTree, operations);
		source.setTransfer(types);
		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				TreeItem[] selection = mTree.getSelection();
				if (selection.length > 0 && !isRootItem(selection[0])) {
					event.doit = true;
				} else {
					event.doit = false;
				}
			};

			public void dragSetData(DragSourceEvent event) {
				event.data = "DND_TREE";
			}

			public void dragFinished(DragSourceEvent event) {
			}
		});
	}

	public TreeItem[] getRootItems() {
		return new TreeItem[]{ mRootTreeItem } ;
	}

	public ITreeObject[] getRootObjects() {
		final TreeItem[] roots = getRootItems();
		final ITreeObject[] ret = new ITreeObject[roots.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = getData(roots[i]);
		}
		return ret;
	}
}
