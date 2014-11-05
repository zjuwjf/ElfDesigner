package com.ielfgame.stupidGame.animation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.power.PowerMan;

public class AnimateControl extends AbstractWorkSpaceTab {

	public AnimateControl(String text) { 
		super("Animate"); 
	} 
	
	public void update() { 
		// 
	} 

	public Composite createTab(CTabFolder parent) {
		mIconCache = PowerMan.getSingleton(MainDesigner.class).mIconCache;

		final Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER | SWT.SEPARATOR);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);

		// left controls
		createControls(composite); 
		
		// middle global
		createGlobal(composite); 
		
		// right frame
		createFrame(composite); 
		
		return composite;
	}

	private IconCache mIconCache;

	void createControls(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		group.setLayout(gridLayout);
		group.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING));
		group.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
			} 
		});

		ToolBar toolBar = new ToolBar(group, SWT.FLAT);
		// play tool item
		final ToolItem playItem = new ToolItem(toolBar, SWT.PUSH);
		playItem.setText(ResJudge.getResourceString("Play"));
		playItem.setImage(mIconCache.stockImages[mIconCache.iconPlay]);
		
		// pause tool item
		final ToolItem pauseItem = new ToolItem(toolBar, SWT.PUSH);
		pauseItem.setText(ResJudge.getResourceString("Pause"));
		pauseItem.setImage(mIconCache.stockImages[mIconCache.iconPause]);
		
		// save tool item
		final ToolItem saveItem = new ToolItem(toolBar, SWT.PUSH);
		saveItem.setText(ResJudge.getResourceString("Save"));
		saveItem.setImage(mIconCache.stockImages[mIconCache.iconSave]);
		
		// clear all
		final ToolItem clearItem = new ToolItem(toolBar, SWT.PUSH);
		clearItem.setText(ResJudge.getResourceString("Clear"));
		clearItem.setImage(mIconCache.stockImages[mIconCache.iconRemoveAll]);
		
	} 
	
	void createGlobal(Composite parent) {
		
	} 
	
	void createFrame(Composite parent) { 
		
	} 
} 
