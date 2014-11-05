package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class OutputWorkSpaceTab extends AbstractWorkSpaceTab{
	protected StyledText mOutput;
	
	
	
	public OutputWorkSpaceTab() {
		super("Output");
	}

	public Composite createTab(CTabFolder parent) {
		final Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		
		mOutput = new StyledText(composite, SWT.NONE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		mOutput.setEditable(false);
		mOutput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		mOutput.setWordWrap(true);
		mOutput.setJustify(true);
		mOutput.setBottomMargin(0);
//		mOutput.setText("output:\n");
		return composite;
	}

	public void update() {
		
	}
	
	public String getTestText() {
		return "hello world";
	}
}
