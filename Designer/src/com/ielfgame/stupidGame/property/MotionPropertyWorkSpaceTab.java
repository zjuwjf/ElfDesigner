package com.ielfgame.stupidGame.property;

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
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.face.basic.EmptyTrees;
import com.ielfgame.stupidGame.face.basic.PropertyTree;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.nodeAnimate.timeLine.TimeData;

public class MotionPropertyWorkSpaceTab extends AbstractWorkSpaceTab implements Runnable {

	public MotionPropertyWorkSpaceTab() {
		super("Motion-Property");
		
		mBasicNodeTrees = new CustomizedNodeTrees(false,150,200);
	}

	private Composite [] mStackComposites = new Composite[1];;
	private StackLayout [] mStackLayouts = new StackLayout[1];
	
	private final EmptyTrees mBasicNodeTrees;
	
	public Composite createTab(final CTabFolder parent) { 
		final SashForm mainPanel = new SashForm(parent, SWT.NONE);
		mainPanel.setOrientation(SWT.HORIZONTAL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = mStackComposites.length;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 0;
		mainPanel.setLayout(gridLayout);
		
		for (int i = 0; i < gridLayout.numColumns; i++) {
			final Group group = new Group(mainPanel, SWT.NONE);
			group.setLayout(new GridLayout());
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			mStackComposites[i] = new Composite(group, SWT.NONE);
			mStackLayouts[i] = new StackLayout();
			mStackComposites[i].setLayout(mStackLayouts[i]);
			mStackComposites[i].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
//			group.setText("Motion");
			mBasicNodeTrees.createTrees(mStackComposites[i]);
		} 
		run(); 
		return mainPanel; 
	} 

	private Object mCurrentNode;
	
	// update
	public void update() {
		Object node = null;
		
		final TimeData data = TimeData.getSelectTimeData();
		if(data != null) {
			node = data.getBindKeyNode();
		} 
		if(node == null) {
			node = PowerMan.getSingleton(DataModel.class).getSelectNode();
		}
		
		try {
			final Control [] newControls = new Control[mStackLayouts.length];
			if (mCurrentNode != node) { 
				mCurrentNode = node; 
				if(mCurrentNode != null) { 
					final PropertyTree baPropertyTree = mBasicNodeTrees.findRightTree(mCurrentNode.getClass());
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
			final PropertyTree basicPropertyTree = mBasicNodeTrees.findRightTree(mCurrentNode.getClass());
			if(basicPropertyTree != null) { 
				basicPropertyTree.run(false);
			} 
		} 
	} 

}
