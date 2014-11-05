package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.ielfgame.stupidGame.autoSave.AutoSave;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class StatusViewWorkSpace extends AbstractWorkSpace implements Runnable{
	private Label [] mLabels = new Label[5];
	
	private final AutoSave mAutoSave = new AutoSave();
	
	public Composite createWorkSpace(Composite parent) {
		
		final SashForm statusBar = new SashForm(parent, SWT.NONE) ;
		statusBar.setOrientation(SWT.HORIZONTAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 20;
		gridData.horizontalSpan = 3;
		statusBar.setLayoutData(gridData);
		
		for(int i=0; i<mLabels.length; i++) {
			final Label label = new Label(statusBar, SWT.NONE | SWT.BORDER);
			label.setText(" ");
			mLabels[i] = label;
		} 
		statusBar.setWeights(new int[]{3, 2, 2, 2, 2});
		
		run();
		
		return statusBar;
	} 
	
	public void run() { 
		if(mLabels[0] != null && !mLabels[0].isDisposed()) { 
			mAutoSave.run();
			
			final String path = PowerMan.getSingleton(DataModel.class).getLastImportPath();
			mLabels[0].setText("Path:"+path);
			
			final ElfNode node = PowerMan.getSingleton(DataModel.class).getSelectNode();
			if(node == null) {
				mLabels[2].setText("Select : null");
			} else { 
				final int size = PowerMan.getSingleton(DataModel.class).getSelectNodeList().size();
				final String text = String.format("Select[%d]: %s, Type : %s", size, node.getName(), node.getClass().getSimpleName());
				mLabels[2].setText(text);
			} 
			final ElfNode bindNode = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode();
			if(bindNode != null) {
				final ElfPointf p = bindNode.getNodeIndex(node);
				mLabels[4].setText("Node Count:"+(int)(p.x) + ", Current:"+(int)(p.y));
			} else {
				mLabels[4].setText("");
			}
			
			mLabels[0].getDisplay().timerExec(500, this);
		} 
	}
	
	public Label getLable(int index){
		
		return mLabels[index];
	} 
}
