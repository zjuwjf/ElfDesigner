package com.ielfgame.stupidGame.design.hotSwap.flash;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKey;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashMain;
import com.ielfgame.stupidGame.face.basic.EmptyTrees;
import com.ielfgame.stupidGame.face.basic.ICurrentSelect;
import com.ielfgame.stupidGame.face.basic.PropertyTree;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class FlashPropertyTab  extends AbstractWorkSpaceTab implements Runnable {
	
	
	public FlashPropertyTab() {
		super("Flash Property");
		
		mFlashNodeTrees = new FrashNodeTrees(150,200);
		mKeyNodeTrees = new KeyNodeTrees(150,200);
		
	}

	private Composite [] mStackComposites = new Composite[1];
	private StackLayout [] mStackLayouts = new StackLayout[1];
	
	private Composite [] mStackComposites2 = new Composite[1];
	private StackLayout [] mStackLayouts2 = new StackLayout[1];
	
	private final EmptyTrees mKeyNodeTrees;
	private final EmptyTrees mFlashNodeTrees;
	
	public Composite createTab(final CTabFolder parent) { 
		GridLayout gridLayout = new GridLayout();
		
		final SashForm mainPanel = new SashForm(parent, SWT.NONE);
		mainPanel.setOrientation(SWT.V_SCROLL);
		
		final SashForm upPanel = new SashForm(mainPanel, SWT.NONE);
		upPanel.setOrientation(SWT.H_SCROLL);
		gridLayout.numColumns = mStackComposites.length;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 0;
		upPanel.setLayout(gridLayout);
		
		final SashForm downPanel = new SashForm(mainPanel, SWT.NONE);
		downPanel.setOrientation(SWT.H_SCROLL);
		gridLayout.numColumns = mStackComposites.length;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 0;
		downPanel.setLayout(gridLayout);
		
		gridLayout.numColumns = mStackComposites.length;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 0;
		mainPanel.setLayout(gridLayout);
		
		for (int i = 0; i < gridLayout.numColumns; i++) {
			final Group group = new Group(upPanel, SWT.NONE);
			group.setLayout(new GridLayout());
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			mStackComposites2[i] = new Composite(group, SWT.NONE);
			mStackLayouts2[i] = new StackLayout();
			mStackComposites2[i].setLayout(mStackLayouts2[i]);
			mStackComposites2[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			mFlashNodeTrees.createTrees(mStackComposites2[i]);
		} 
		
		for (int i = 0; i < gridLayout.numColumns; i++) {
			final Group group = new Group(downPanel, SWT.NONE);
			group.setLayout(new GridLayout());
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			mStackComposites[i] = new Composite(group, SWT.NONE);
			mStackLayouts[i] = new StackLayout();
			mStackComposites[i].setLayout(mStackLayouts[i]);
			mStackComposites[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			mKeyNodeTrees.createTrees(mStackComposites[i]);
		} 
		
		mainPanel.setWeights(new int[]{4,8});
		
		run(); 
		
		return mainPanel; 
	} 

	private Object mCurrentNode;
	private Object mCurrentNode2;
	
	// update
	private void update2() {
		Object node = sFlashManager.getFlashMain();
		
		try {
			final Control [] newControls = new Control[mStackLayouts2.length];
			if (mCurrentNode2 != node) { 
				mCurrentNode2 = node; 
				if(mCurrentNode2 != null) { 
					final PropertyTree baPropertyTree = mFlashNodeTrees.findRightTree(mCurrentNode2.getClass());
					if(baPropertyTree != null) {
						newControls[0] = baPropertyTree.getTree();
						baPropertyTree.run(true);
					} else {
						newControls[0] = null;
					}
				} 
				
				for(int i=0; i<mStackLayouts2.length; i++) {
					if(newControls[i] != mStackLayouts2[i].topControl) {
						mStackLayouts2[i].topControl = newControls[i];
						mStackComposites2[i].layout();
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(mCurrentNode2 != null) { 
			final PropertyTree basicPropertyTree = mFlashNodeTrees.findRightTree(mCurrentNode2.getClass());
			if(basicPropertyTree != null) { 
				basicPropertyTree.run(false);
			} 
		} 
	} 
	
	public void update() {
		update2();
		
		Object node = null;
		
		IFlashKey[] selectKeys = sFlashManager.getAllSelectedFlashKeys();
		
		if(selectKeys.length > 0) {
			node = selectKeys[0];
		} 
		
		try {
			final Control [] newControls = new Control[mStackLayouts.length];
			if (mCurrentNode != node) { 
				mCurrentNode = node; 
				if(mCurrentNode != null) { 
					final PropertyTree baPropertyTree = mKeyNodeTrees.findRightTree(mCurrentNode.getClass());
					if(baPropertyTree != null) {
						newControls[0] = baPropertyTree.getTree();
						baPropertyTree.run(true);
					} else {
						newControls[0] = null;
					}
				} 
				
				for(int i=0; i<mStackLayouts.length; i++) {
					if(newControls[i] != mStackLayouts[i].topControl) {
						mStackLayouts[i].topControl = newControls[i];
						mStackComposites[i].layout();
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(mCurrentNode != null) { 
			final PropertyTree basicPropertyTree = mKeyNodeTrees.findRightTree(mCurrentNode.getClass());
			if(basicPropertyTree != null) { 
				basicPropertyTree.run(false);
			} 
		} 
	} 
	
	private final static FlashManager sFlashManager = PowerMan.getSingleton(FlashManager.class);
	
	private static class KeyNodeTrees extends EmptyTrees {
		public KeyNodeTrees(final int widthType, final int widthValue) {
			final LinkedList<String> elfList = new LinkedList<String>(); 
			
			elfList.add("Resid");
			elfList.add("Position");
			elfList.add("Scale");
			elfList.add("Rotate"); 
			elfList.add("Color"); 
			elfList.add("Visible"); 
			
			final LinkedList<String> flashKeyList = new LinkedList<String>();
			flashKeyList.add("InterType");
			flashKeyList.add("InterStrong");
			flashKeyList.add("LoopMode"); 
			flashKeyList.add("FrameIndex");
			//PauseMotion
			
			final PropertyTree elfNodeTree = new PropertyTree(widthType, widthValue); 
			elfNodeTree.setCurrentSelect(motionICurrentSelect); 
			elfNodeTree.setSelectClass(ElfNode.class, false, elfList); 
			this.checkInTree(elfNodeTree); 
			
			final PropertyTree flashKeyNodeTree = new PropertyTree(widthType, widthValue); 
			flashKeyNodeTree.setCurrentSelect(motionICurrentSelect); 
			
			flashKeyNodeTree.setSelectClass(ElfNode.class, false, elfList); 
			flashKeyNodeTree.setSelectClass(KeyFrameNode.class, false, flashKeyList); 
			this.checkInTree(flashKeyNodeTree); 
		}
		
		final ICurrentSelect motionICurrentSelect = new ICurrentSelect() {
			public Object[] getSelectObjs(boolean gloabl) {
				//
				final IFlashKey[] all = sFlashManager.getAllSelectedFlashKeys();
				if(all.length > 0) {
//					final IFlashKey key = all[0];
//					if(key != null) {
//						return new Object[]{ key };
//					}
					return all;
				}
				
				return null ; 
			}
		};
		
		public PropertyTree findRightTree(final Class<?> select) { 
			if(motionICurrentSelect.getSelectObjs(false) != null) { 
				if (select != null) { 
					final LinkedList<PropertyTree> results = new LinkedList<PropertyTree>();
					for (PropertyTree tree : mTrees) {
						final Class<?> treeClass = tree.getSelectClass();
						if (treeClass != null && treeClass.isAssignableFrom(select)) {
							results.add(tree);
						}
					}

					Collections.sort(results, new Comparator<PropertyTree>() {
						public int compare(PropertyTree arg0, PropertyTree arg1) {
							if (arg0.getSelectClass() == arg1.getSelectClass()) {
								return 0;
							} else if (arg0.getSelectClass().isAssignableFrom(arg1.getSelectClass())) {
								return 1;
							} else {
								return -1;
							}
						}
					});

					if (results.size() > 0) {
						return results.getFirst();
					}
				}
				return null;
			} 
			return null;
		}
	}
	
	private static class FrashNodeTrees extends EmptyTrees {
		public FrashNodeTrees(final int widthType, final int widthValue) {
			final LinkedList<String> flashKeyList = new LinkedList<String>(); 
			
			flashKeyList.add("KeyStorage");
			flashKeyList.add("FPS");
			flashKeyList.add("MaxF");
			flashKeyList.add("ProgressTime");
			
			final PropertyTree flashKeyNodeTree = new PropertyTree(widthType, widthValue); 
			flashKeyNodeTree.setCurrentSelect(motionICurrentSelect); 
			flashKeyNodeTree.setSelectClass(FlashMainNode.class, false, flashKeyList); 
			this.checkInTree(flashKeyNodeTree); 
		}
		
		final ICurrentSelect motionICurrentSelect = new ICurrentSelect() {
			public Object[] getSelectObjs(boolean gloabl) {
				final IFlashMain current = sFlashManager.getFlashMain();
				if(current != null) {
					return new Object[]{ current };
				}
				return null ; 
			}
		};
		
		public PropertyTree findRightTree(final Class<?> select) { 
			if(motionICurrentSelect.getSelectObjs(false) != null) { 
				if (select != null) { 
					final LinkedList<PropertyTree> results = new LinkedList<PropertyTree>();
					for (PropertyTree tree : mTrees) {
						final Class<?> treeClass = tree.getSelectClass();
						if (treeClass != null && treeClass.isAssignableFrom(select)) {
							results.add(tree);
						}
					}

					Collections.sort(results, new Comparator<PropertyTree>() {
						public int compare(PropertyTree arg0, PropertyTree arg1) {
							if (arg0.getSelectClass() == arg1.getSelectClass()) {
								return 0;
							} else if (arg0.getSelectClass().isAssignableFrom(arg1.getSelectClass())) {
								return 1;
							} else {
								return -1;
							}
						}
					});

					if (results.size() > 0) {
						return results.getFirst();
					}
				}
				return null;
			} 
			return null;
		}
	}
}
