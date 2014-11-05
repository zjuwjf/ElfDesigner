package com.ielfgame.stupidGame.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.data.ElfDataDisplay;

public class MultiButtonDialog {
	
	public void open(final String title, final Shell mShell, final ElfDataDisplay [] datas) {
		final Shell shell = new Shell(mShell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
//		RunState.initChildShell(shell);
		shell.setLayout(new GridLayout());

		shell.setText("" + title);

		createButtonWidgets(shell, datas);
		
		shell.pack();
		shell.setLocation(shell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - shell.getSize().x / 2, shell.getDisplay().getPrimaryMonitor().getClientArea().height / 2 - shell.getSize().y / 2);

		shell.open();
		Display display = shell.getDisplay(); 
		while (!shell.isDisposed()) { 
			if (!display.readAndDispatch())
				display.sleep();
		} 
	} 

	private void createButtonWidgets(Shell shell, final ElfDataDisplay [] datas) {
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		
		for(final ElfDataDisplay data : datas) {
			final Button button = new Button(composite, SWT.PUSH);
			final GridData gridData = new GridData();
			gridData.widthHint = 400;
			button.setLayoutData(gridData);
			button.setText(data.toTitle());
			
			button.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					final MultiLineDialog dialog = new MultiLineDialog();
					final Object[] ret = dialog.open(data);
					if(ret != null) {
						data.setValues(ret);
					}
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});
		}
	}
	
}
