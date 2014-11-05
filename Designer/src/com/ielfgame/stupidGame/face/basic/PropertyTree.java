package com.ielfgame.stupidGame.face.basic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.StatusViewWorkSpace;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.ElfPointi;
import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.language.LanguageManager;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.reflect.ReflectConstants;

public class PropertyTree { 
	
	//tree
	//select
	//drag, drop 
	//copy, paste
	
	private ICurrentSelect mCurrentSelect = ICurrentSelect.CurrentSelect_Default;
	public ICurrentSelect getCurrentSelect() {
		return mCurrentSelect;
	} 
	public void setCurrentSelect(ICurrentSelect mCurrentSelect) {
		this.mCurrentSelect = mCurrentSelect;
	}
	
	private final ArrayList<PropertyRootItem> mRootItems = new ArrayList<PropertyRootItem>();
	private Class<?> mSelectClass;
	private Tree mTree;
	
	private final LinkedList<ITreeItem> mCopyList = new LinkedList<ITreeItem>(); 
	private final LinkedList<ITreeItem> mPasteList = new LinkedList<ITreeItem>(); 
	private final LinkedList<ITreeItem> mDropResidList = new LinkedList<ITreeItem>(); 
	private final LinkedList<ITreeItem> mNumberList = new LinkedList<ITreeItem>();
	private final LinkedList<ITreeItem> mPointList = new LinkedList<ITreeItem>();
	
	private final int mWidthType, mWidthValue;
	public PropertyTree(int widthType, int widthValue) { 
		mWidthType = widthType;
		mWidthValue = widthValue;
	} 
	
	
	
	public void setSelectClass(Class<?> _class, boolean isGlobal, List<String> func) {
		
		mSelectClass = _class;
		assert(mSelectClass != null);
		
		final PropertyRootItem nodeItem = new PropertyRootItem(mSelectClass, isGlobal);
		nodeItem.setCurrentSelect(mCurrentSelect); 
		
		this.checkInRootItem(nodeItem); 
		
		final Class<?> myClass = mSelectClass; 
		final Field [] fs = myClass.getDeclaredFields(); 
		for(int i=0; i<fs.length; i++) { 
			final Field f = fs[i];
			final String name = f.getName();
			
			if(name.startsWith("REF_")) {  
				final String funcName = name.substring(4); 
				if(func != null && !func.contains(funcName)) { 
					continue;
				} 
				try { 
					f.setAccessible(true);
					final int mask = f.getInt(null);
					ITreeItem myItem = null;
					if((mask & ReflectConstants.FACE_ALL_SHIFT) != 0) { 
						final Method [] methods = myClass.getMethods(); 
						for(int j=0; j<methods.length; j++) { 
							final Method method = methods[j]; 
							final String methodName = method.getName(); 
							if(methodName.equals("get"+funcName)) { 
								final Class<?>  returnType = method.getReturnType(); 
								if((mask & ReflectConstants.UNDO_SHIFT) != 0) { 
									myItem = nodeItem.addAllFuncItem(returnType, funcName, true); 
								} else {
									myItem = nodeItem.addAllFuncItem(returnType, funcName, false); 
								} 
							} 
						} 
					} else if((mask & ReflectConstants.FACE_GET_SHIFT) != 0) { 
						final Method [] methods = myClass.getMethods();
						for(int j=0; j<methods.length; j++) {
							final Method method = methods[j];
							final String methodName = method.getName();
							if(methodName.equals("get"+funcName)) { 
								final Class<?>  returnType = method.getReturnType(); 
								myItem = nodeItem.addGetFuncItem(returnType, funcName); 
							} 
						} 
					}  else if((mask & ReflectConstants.FACE_SET_SHIFT) != 0) { 
						final Method [] methods = myClass.getMethods();
						for(int j=0; j<methods.length; j++) {
							final Method method = methods[j];
							final String methodName = method.getName();
							if(methodName.equals("set"+funcName)) { 
								final Class<?>[] parameterType = method.getParameterTypes(); 
								if(parameterType.length == 0) { 
									myItem = nodeItem.addSetFuncItem(null, funcName); 
								} else if(parameterType.length == 1) {
									myItem = nodeItem.addSetFuncItem(parameterType[0], funcName); 
								} else { 
								}
							} 
						} 
					} 
					
					if(myItem != null) { 
						if((mask & ReflectConstants.COPY_SHIFT) != 0) {
							mCopyList.add(myItem);
						} 
						if((mask & ReflectConstants.PASTE_SHIFT) != 0) {
							mPasteList.add(myItem);
						} 
						if((mask & ReflectConstants.DROP_RESID_SHIFT) != 0) {
							mDropResidList.add(myItem);
						} 
						if(myItem.getValueType() == int.class || myItem.getValueType() == float.class) {
							mNumberList.add(myItem);
						}
						if(myItem.getValueType() == ElfPointi.class || myItem.getValueType() == ElfPointf.class) {
							mPointList.add(myItem);
						} 
					}
					
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			} 
		}
	}
	
	public void setSelectClass(Class<?> _class, boolean isGlobal) {
		setSelectClass(_class, isGlobal, null);
	}
	
	public Class<?> getSelectClass() {
		return mSelectClass;
	}
	
	public void checkInRootItem(PropertyRootItem item) { 
		mRootItems.add(item); 
		//check ? 
	} 
	
	public Tree createTree(final Composite parent) { 
		mTree = new Tree(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL );
		mTree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

//		mTree.setHeaderVisible(false);
		TreeColumn column1 = new TreeColumn(mTree, SWT.NONE);
		column1.setText("Type");
		column1.setWidth(mWidthType);
		
		TreeColumn column2 = new TreeColumn(mTree, SWT.NONE);
		column2.setText("Value");
		column2.setWidth(mWidthValue);
		
		for(PropertyRootItem item : mRootItems) {
			item.create(mTree);
		} 
		
		this.run(true);
		
		//copy, paste
		mTree.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
			} 
			public void keyPressed(KeyEvent e) {
				final Label show = PowerMan.getSingleton(StatusViewWorkSpace.class).getLable(3);
				if(e.stateMask == PlatformHelper.CTRL && (e.keyCode == 'c' || e.keyCode == 'C')) { 
					final TreeItem[] items = mTree.getSelection();
					if(items != null && items.length > 0) { 
						final TreeItem select = items[0];
						show.setText("Does not support " + "Ctrl + C : " + select.getText(0));
						for(ITreeItem myItem : mCopyList) {
							if(myItem.getMyTreeItem() == select) { 
								try {
									final Clipboard clipBoard = new Clipboard(PowerMan.getSingleton(MainDesigner.class).mShell.getDisplay());
									clipBoard.setContents(new String[]{select.getText(1)}, new Transfer[]{TextTransfer.getInstance()});
									show.setText("Ctrl + C :" + myItem.copyContext());
								} catch (Exception e2) {
									e2.printStackTrace();
								}
								break; 
							} 
						} 
					} 
				} else if(e.stateMask == PlatformHelper.CTRL && (e.keyCode == 'v' || e.keyCode == 'V')) { 
					final TreeItem[] items = mTree.getSelection();
					if(items != null && items.length > 0 ) { 
						final TreeItem select = items[0];
						show.setText("Does not support " + "Ctrl + V : " + select.getText(0));
						for(ITreeItem myItem : mPasteList) {
							if(myItem.getMyTreeItem() == select) { 
								final Clipboard clipBoard = new Clipboard(PowerMan.getSingleton(MainDesigner.class).mShell.getDisplay());
								final String text = (String)clipBoard.getContents(TextTransfer.getInstance());
								if(text != null) { 
									boolean suc = myItem.pasteContext(text);
									if(suc) { 
										show.setText("Ctrl + V : " + select.getText(0) + " Accepted");
									} else {
										show.setText("Ctrl + V : " + select.getText(0) + " Unacceptable");
									} 
								} else { 
									show.setText("Ctrl + V : " + select.getText(0) + " null");
								} 
								
								break;
							} 
						} 
					} 
				} 
//				else if(e.stateMask == PlatformHelper.CTRL && (e.keyCode == 'z' || e.keyCode == 'Z')) { 
//					UndoRedoManager.onUndo(); 
//				} else if(e.stateMask == PlatformHelper.CTRL && (e.keyCode == 'y' || e.keyCode == 'Y')) {
//					UndoRedoManager.onRedo(); 
//				} 
				else if(e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT){
					final TreeItem[] items = mTree.getSelection();
					if(items != null && items.length > 0) { 
						final TreeItem select = items[0];
						ITreeItem myItem = getMyTreeItem(mNumberList, select);
						if(myItem == null) {
							myItem = getMyTreeItem(mPointList, select);
						}
						
						if(myItem != null) {
							if(e.stateMask == PlatformHelper.CTRL) {
								final MultiLineDialog dialg = new MultiLineDialog();
								dialg.setTitle(LanguageManager.get("Set Value Interval"));
								if(myItem.getValueType() == int.class || myItem.getValueType() == ElfPointi.class) {
									final Object [] ret = dialg.open(Math.round(myItem.getValueIntervali()));
									if(ret != null && ret[0] != null) {
										try {
											myItem.setValueIntervali((Integer)ret[0]);
										} catch (Exception e2) { 
											e2.printStackTrace();
										} 
									} 
								} 
								if(myItem.getValueType() == float.class || myItem.getValueType() == ElfPointf.class) { 
									final Object [] ret = dialg.open(myItem.getValueIntervalf());
									if(ret != null && ret[0] != null) { 
										try {
											myItem.setValueIntervalf((Float)ret[0]);
										} catch (Exception e2) { 
											e2.printStackTrace(); 
										} 
									} 
								} 
							} else { 
								if( myItem.getValueType() == float.class ) {
									final float addOrSub = (e.keyCode == SWT.ARROW_UP||e.keyCode == SWT.ARROW_RIGHT) ? myItem.getValueIntervalf():-myItem.getValueIntervalf();
									myItem.setValue(((Float)myItem.getValue()+addOrSub));
								} else if(myItem.getValueType() == int.class ) {
									final float addOrSub = (e.keyCode == SWT.ARROW_UP||e.keyCode == SWT.ARROW_RIGHT) ? myItem.getValueIntervali():-myItem.getValueIntervali();
									myItem.setValue(((Integer)myItem.getValue()+Math.round(addOrSub)));
								} else {
									if(e.stateMask == PlatformHelper.SHIFT) {
										if(myItem.getValueType() == ElfPointf.class ) {
											final float addOrSub = (e.keyCode == SWT.ARROW_UP||e.keyCode == SWT.ARROW_RIGHT) ? myItem.getValueIntervalf():-myItem.getValueIntervalf();
											final ElfPointf value = (ElfPointf)myItem.getValue();
											value.y += addOrSub;value.x += addOrSub;
											myItem.setValue(value);
										} else if(myItem.getValueType() == ElfPointi.class ) {
											final int addOrSub = (e.keyCode == SWT.ARROW_UP||e.keyCode == SWT.ARROW_RIGHT) ? myItem.getValueIntervali():-myItem.getValueIntervali();
											final ElfPointi value = (ElfPointi)myItem.getValue();
											value.y += addOrSub;value.x += addOrSub;
											myItem.setValue(value);
										}
									} else {
										if(myItem.getValueType() == ElfPointf.class ) {
											final float addOrSub = (e.keyCode == SWT.ARROW_UP||e.keyCode == SWT.ARROW_RIGHT) ? myItem.getValueIntervalf():-myItem.getValueIntervalf();
											final ElfPointf value = (ElfPointf)myItem.getValue();
											switch(e.keyCode) {
											case SWT.ARROW_UP:
											case SWT.ARROW_DOWN:value.y += addOrSub;break;
											case SWT.ARROW_LEFT:
											case SWT.ARROW_RIGHT:value.x += addOrSub;break;
											}
											myItem.setValue(value);
										} else if(myItem.getValueType() == ElfPointi.class ) {
											final int addOrSub = (e.keyCode == SWT.ARROW_UP||e.keyCode == SWT.ARROW_RIGHT) ? myItem.getValueIntervali():-myItem.getValueIntervali();
											final ElfPointi value = (ElfPointi)myItem.getValue();
											switch(e.keyCode) {
											case SWT.ARROW_UP:
											case SWT.ARROW_DOWN:value.y += addOrSub;break;
											case SWT.ARROW_LEFT:
											case SWT.ARROW_RIGHT:value.x += addOrSub;break;
											} 
											myItem.setValue(value);
										}
									}
								}
							}
							
							e.doit = false;
						} 
					}
				}
				
//				if(e.stateMask == PlatformHelper.CTRL && (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN)) {
//					final TreeItem[] items = mTree.getSelection();
//					if(items != null && items.length > 0) { 
//						final TreeItem select = items[0];
//						for(ITreeItem myItem : mNumberList) {
//							if(myItem.getMyTreeItem() == select) { 
//								if(myItem.getValueType() == int.class) {
//									final MultiLineDialog dialg = new MultiLineDialog();
//									dialg.setTitle(LanguageManager.get("Set Value Interval"));
//									final Object [] ret = dialg.open(Math.round(myItem.getValueInterval()));
//									if(ret != null && ret[0] != null) {
//										try {
//											myItem.setValueInterval((Integer)ret[0]);
//										} catch (Exception e2) { 
//											e2.printStackTrace();
//										} 
//									} 
//								} 
//								if(myItem.getValueType() == float.class) { 
//									final MultiLineDialog dialg = new MultiLineDialog();
//									dialg.setTitle(LanguageManager.get("Set Value Interval"));
//									final Object [] ret = dialg.open(myItem.getValueInterval());
//									if(ret != null && ret[0] != null) { 
//										try {
//											myItem.setValueInterval((Float)ret[0]);
//										} catch (Exception e2) { 
//											e2.printStackTrace(); 
//										} 
//									} 
//								} 
//								e.doit = false;
//								break; 
//							} 
//						} 
//					} 
//				} else if(e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN){ 
//					final TreeItem[] items = mTree.getSelection();
//					if(items != null && items.length > 0) { 
//						final TreeItem select = items[0];
//						for(ITreeItem myItem : mNumberList) { 
//							if(myItem.getMyTreeItem() == select) { 
//								final float addOrSub = e.keyCode == SWT.ARROW_UP ? myItem.getValueInterval():-myItem.getValueInterval();
//								if(myItem.getValueType() == int.class) {
//									myItem.setValue(((Integer)myItem.getValue()+Math.round(addOrSub)));
//								} 
//								if(myItem.getValueType() == float.class) { 
//									myItem.setValue(((Float)myItem.getValue()+addOrSub));
//								} 
//								e.doit = false;
//								break; 
//							} 
//						} 
//					} 
//				} 
			} 
		}); 
		
		setDropSource(mTree);
		
		return mTree;
	} 
	
	//just for resid
	private DropTarget setDropSource(final Tree tree) {
		DropTarget dropTarget = new DropTarget(tree, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			
			public void drop(DropTargetEvent event) { 
				if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) { 
					final String[] drops = (String[]) event.data; 
					if (drops != null && drops.length > 0) { 
						final TreeItem item = getTargetItem(event); 
						if (item != null) { 
							for(ITreeItem myItem : mDropResidList) { 
								if(myItem.getMyTreeItem() == item) { 
									boolean suc ;
									if(myItem.getValueType().isArray()) {
										suc = myItem.pasteContext(Stringified.toText(drops, false));
									} else {
										suc = myItem.pasteContext(drops[0]);
									} 
									final Label show = PowerMan.getSingleton(StatusViewWorkSpace.class).getLable(3);
									if(suc) { 
										show.setText("Drop Accepted");
									} else {
										show.setText("Drop Unaccepted");
									}
								}
							}
						} 
					} 
				} 
			} 
			
			public void dragOver(DropTargetEvent event) {
				if (FileTransfer.getInstance().isSupportedType(event.currentDataType)) {
					final String[] drops = (String[]) event.data;
					if (drops != null && drops.length > 0) {
						final TreeItem item = getTargetItem(event);
						if (item != null) { 
							boolean flag = true;
							for(ITreeItem myItem : mDropResidList) { 
								if(myItem.getMyTreeItem() == item) { 
									flag = false; 
									break;
								}  
							} 
							
							if(flag) { 
								event.feedback |= DND.FEEDBACK_SCROLL; 
							}  else {
								event.feedback |= DND.FEEDBACK_NONE; 
							}
						} else {
							event.feedback |= DND.FEEDBACK_SCROLL; 
						} 
					} else {
						event.feedback |= DND.FEEDBACK_SCROLL; 
					} 
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
			
			private TreeItem getTargetItem(DropTargetEvent event) {
				final TreeItem item = tree.getItem(tree.toControl(new Point(event.x, event.y)));
				return item;
			} 
		}); 
		return dropTarget;
	} 
	
	//update
	public void run(boolean instance) {
		for(PropertyRootItem item : mRootItems) { 
			item.run(instance); 
		} 
	} 
	
	public Tree getTree() {
		return mTree;
	} 
	
	public static ITreeItem getMyTreeItem(LinkedList<ITreeItem> list, TreeItem select) {
		for(ITreeItem item : list) {
			if(item.getMyTreeItem() == select) {
				return item;
			}
		}
		return null;
	}
} 
