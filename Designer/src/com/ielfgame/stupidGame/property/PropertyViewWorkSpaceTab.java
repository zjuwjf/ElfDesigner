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
import com.ielfgame.stupidGame.face.basic.PropertyTree;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class PropertyViewWorkSpaceTab extends AbstractWorkSpaceTab implements Runnable {

	private Composite [] mStackComposites = new Composite[2];;
	private StackLayout [] mStackLayouts = new StackLayout[2];
	
	private final BasicNodeTrees mBasicNodeTrees;
	private final AdvancedNodeTrees mAdvancedNodeTrees;
	
	public PropertyViewWorkSpaceTab(final String label, boolean isGlobal) {
		super(label);
		
		mBasicNodeTrees = new BasicNodeTrees(isGlobal, 200, 300);
		mAdvancedNodeTrees = new AdvancedNodeTrees(isGlobal, 200, 300);
	} 

	public Composite createTab(final CTabFolder parent) { 
		final SashForm mainPanel = new SashForm(parent, SWT.NONE);
		mainPanel.setOrientation(SWT.HORIZONTAL);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
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
			
			if (i == 1) {
				group.setText("Advanced");
				mAdvancedNodeTrees.createTrees(mStackComposites[i]);
			} else {
				group.setText("Basic");
				mBasicNodeTrees.createTrees(mStackComposites[i]);
			} 
		}
		run();
		return mainPanel;
	}

	private ElfNode mCurrentNode;
	public ElfNode getCurrentNode() {
		return mCurrentNode;
	} 
	
	// update
	public void update() {
		final ElfNode node = PowerMan.getSingleton(DataModel.class).getSelectNode();
		
		try {
			final Control [] newControls = new Control[2];
			if (mCurrentNode != node) { 
				mCurrentNode = node; 
				if(mCurrentNode != null) { 
					final PropertyTree adPropertyTree = mAdvancedNodeTrees.findRightTree(mCurrentNode.getClass());
					if(adPropertyTree != null) {
						newControls[1] = adPropertyTree.getTree();
						adPropertyTree.run(true); 
					} else { 
						newControls[1] = null;
					} 
					
					final PropertyTree baPropertyTree = mBasicNodeTrees.findRightTree(mCurrentNode.getClass());
					if(baPropertyTree != null) {
						newControls[0] = baPropertyTree.getTree();
						baPropertyTree.run(true);
					} else {
						newControls[0] = null;
					}
				} 
				
				for(int i=0; i<2; i++) {
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
			final PropertyTree advancedPropertyTree = mAdvancedNodeTrees.findRightTree(mCurrentNode.getClass());
			if(advancedPropertyTree != null) {
				advancedPropertyTree.run(false);
			} 
			
			final PropertyTree basicPropertyTree = mBasicNodeTrees.findRightTree(mCurrentNode.getClass());
			if(basicPropertyTree != null) {
				basicPropertyTree.run(false);
			} 
		} 
	} 
} 