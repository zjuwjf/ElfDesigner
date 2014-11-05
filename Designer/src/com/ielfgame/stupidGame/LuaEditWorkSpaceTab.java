package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

public class LuaEditWorkSpaceTab extends AbstractWorkSpaceTab{
	static String [] sLabels = {"Actions", "Touches", "More"};
	
	public LuaEditWorkSpaceTab() { 
		super("Lua Edit"); 
	} 

	public void update() {
	} 

	public Composite createTab(CTabFolder parent) {
		final Composite mainPanel = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = sLabels.length;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 0;
		mainPanel.setLayout(gridLayout);
		
		for (int i=0; i<gridLayout.numColumns; i++) {
			final Group group = new Group(mainPanel, SWT.NONE);
			final GridLayout gridLayout2 = new GridLayout();
			gridLayout2.numColumns = 1;
			group.setLayout(gridLayout2);
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			group.setText(sLabels[i]);
			
			if(i == 0) {
				final Composite buttonBar = new Composite(group, SWT.NONE);
				buttonBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				GridLayout layout = new GridLayout();
				layout.numColumns = 3;
				buttonBar.setLayout(layout);
				
				final Button bindLua = new Button(buttonBar, SWT.PUSH);
				bindLua.setText("bind lua");
				bindLua.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						PowerMan.getSingleton(DataModel.class).getScreenNode().bindToLua();
					} 
				});
				
				final Button runLua = new Button(buttonBar, SWT.PUSH);
				runLua.setText("run lua");
				
				final Button stopLua = new Button(buttonBar, SWT.PUSH);
				stopLua.setText("stop actions");
			}
			
//			final LuaText luaText = new LuaText(group);
//			luaText.setLayout(new GridLayout());
//			luaText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		} 
		
		return mainPanel;
	} 
}
