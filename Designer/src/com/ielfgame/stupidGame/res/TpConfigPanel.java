package com.ielfgame.stupidGame.res;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import com.ielfgame.stupidGame.ColorFactory;
import com.ielfgame.stupidGame.Constants;
import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.newNodeMenu.SetTexturePackerPanel.ImageFormat;

import elfEngine.basic.utils.EnumHelper;

public class TpConfigPanel {
	
	public static class TpConfig {

		/***
		 * png, jpg, pvr
		 * 
		 * pvr4, pvr5, pvr8, PNG, GLT
		 */

		ImageFormat format = ImageFormat.RGBA4444;
		int num = 0;

		public String toString() {
			if (num == 0 && format == ImageFormat.RGBA4444) {
				return "default";
			}
			return String.format("%d#%s", num, format.toString());
		}
	}

	private TpConfig mResult;

	public TpConfig open(final TpConfig input, final Shell parent) {

		final TpConfig ret = new TpConfig();
		ret.format = input.format;
		ret.num = input.num;

		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		// RunState.initChildShell(shell);
		shell.setLayout(new GridLayout());
		shell.setText("Set Tp Config");

		final Composite composite0 = new Composite(shell, SWT.NONE);
		composite0.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite0.setLayout(layout);

		final Composite composite01 = new Composite(composite0, SWT.NONE);
		GridData gridData01 = new GridData();
		gridData01.widthHint = 200;
		composite01.setLayoutData(gridData01);
		layout = new GridLayout();
		layout.numColumns = 2;
		composite01.setLayout(layout);

		final Label labelFormat = new Label(composite01, SWT.RIGHT);
		labelFormat.setText("Format");

		final CCombo comboFormat = new CCombo(composite01, SWT.BORDER);
		comboFormat.setItems(EnumHelper.toString(ImageFormat.class));
		comboFormat.setText("" + ret.format.toString());
		comboFormat.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		comboFormat.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				comboFormat.setBackground(ColorFactory.WHITE);
			}
		});

		final Composite composite02 = new Composite(composite0, SWT.NONE);
		GridData gridData02 = new GridData();
		gridData02.widthHint = 200;
		composite02.setLayoutData(gridData02);
		layout = new GridLayout();
		layout.numColumns = 2;
		composite02.setLayout(layout);

		final Label labelNum = new Label(composite02, SWT.RIGHT);
		labelNum.setText("Num");

		final Spinner spinnerNums = new Spinner(composite02, SWT.NONE);
		spinnerNums.setSelection(ret.num);
		spinnerNums.setMaximum(Integer.MAX_VALUE);
		spinnerNums.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				spinnerNums.setBackground(ColorFactory.WHITE);
			}
		});

		createControlButtons(shell, comboFormat, spinnerNums, ret);

		shell.pack();
		shell.setLocation(shell.getDisplay().getClientArea().width / 2 - shell.getSize().x / 2, shell.getDisplay().getClientArea().height / 2 - shell.getSize().y / 2);

		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return mResult;
	}

	private void createControlButtons(final Shell shell, final CCombo format, final Spinner nums, final TpConfig ret) {
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		Button okButton = new Button(composite, SWT.PUSH);
		okButton.setText(Constants.POP_DIALOG_OK);
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ret.num = nums.getSelection();

				final String text = format.getText();
				final Object[] rets = Stringified.fromText(ImageFormat.class, text);
				try {
					final ImageFormat objRet = (ImageFormat) rets[0];
					ret.format = objRet;
				} catch (Exception e2) {
					e2.printStackTrace();
					format.setBackground(ColorFactory.RED);
					return;
				}

				if (ret.format == null) {
					format.setBackground(ColorFactory.RED);
					return;
				}

				mResult = ret;

				shell.close();
			}
		});

		Button cancelButton = new Button(composite, SWT.PUSH);
		cancelButton.setText(Constants.POP_DIALOG_CANCEL);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				mResult = null;

				shell.close();
			}
		});

		shell.setDefaultButton(okButton);
	}
}
