//package com.ielfgame.stupidGame.dialog;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.custom.CCombo;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.SelectionListener;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.DirectoryDialog;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.FileDialog;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Spinner;
//import org.eclipse.swt.widgets.Text;
//
//import com.ielfgame.stupidGame.ColorFactory;
//import com.ielfgame.stupidGame.Constants;
//import com.ielfgame.stupidGame.MainDesigner;
//import com.ielfgame.stupidGame.config.RunState;
//import com.ielfgame.stupidGame.data.ElfDataDisplay;
//import com.ielfgame.stupidGame.data.ElfEnum;
//import com.ielfgame.stupidGame.data.Stringified;
//import com.ielfgame.stupidGame.data.TypeFactory;
//import com.ielfgame.stupidGame.data.TypeFactory.ClassType;
//import com.ielfgame.stupidGame.language.LanguageManager;
//import com.ielfgame.stupidGame.power.PowerMan;
//
//import elfEngine.basic.utils.EnumHelper;
//import elfEngine.basic.utils.EnumHelper.BoolEnum;
//
//public class ProjectConfigPanel {
//
//	private Shell mShell;
//
//	public ProjectConfigPanel() {
//		this(PowerMan.getSingleton(MainDesigner.class).getShell());
//	}
//
//	public ProjectConfigPanel(Shell shell) {
//		mShell = shell;
//	}
//
//	private String[] mLabels;
//	private Class<?>[] mTypes;
//	private Object[] mValues;
//	private String mTitle;
//	private Control[] mControls;
//
//	public void setTitle(final String title) {
//		this.mTitle = title;
//	}
//
//	public void setLabels(final String[] labels) {
//		this.mLabels = labels;
//	}
//
//	public void setValueTypes(final Class<?>[] types) {
//		this.mTypes = types;
//	}
//
//	public void setValues(final Object[] values) {
//		this.mValues = values;
//	}
//
//	public Object[] open(ElfDataDisplay data) {
//		if (data == null) {
//			return null;
//		}
//		this.setTitle(data.toTitle());
//		this.setLabels(data.toLabels());
//		this.setValues(data.toValues());
//		this.setValueTypes(data.toTypes());
//		return open();
//	}
//
//	public Object[] open(Object obj) {
//		if (obj == null) {
//			return null;
//		}
//		if (mTitle == null) {
//			this.setTitle("" + obj.getClass().getSimpleName());
//		}
//		this.setLabels(new String[] { obj.getClass().getSimpleName() });
//		this.setValues(new Object[] { obj });
//		this.setValueTypes(new Class<?>[] { obj.getClass() });
//		return open();
//	}
//
//	public Object[] open() {
//		final Shell shell = new Shell(mShell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
//		RunState.initChildShell(shell);
//		shell.setLayout(new GridLayout());
//
//		shell.setText("" + mTitle);
//
//		createTextWidgets(shell);
//		createControlButtons(shell);
//
//		shell.pack();
//		shell.setLocation(shell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - shell.getSize().x / 2, shell.getDisplay().getPrimaryMonitor().getClientArea().height / 2 - shell.getSize().y / 2);
//
//		shell.open();
//		Display display = shell.getDisplay();
//
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		return mValues;
//	}
//
//	void createControlButtons(final Shell shell) {
//		Composite composite = new Composite(shell, SWT.NONE);
//		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 2;
//		composite.setLayout(layout);
//
//		Button okButton = new Button(composite, SWT.PUSH);
//		okButton.setText(Constants.POP_DIALOG_OK);
//		okButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				final int size = mControls.length;
//				final Object[] olds = mValues;
//				mValues = new Object[size];
//				boolean failed = false;
//				for (int i = 0; i < size; i++) {
//					try {
//						if (ElfEnum.class.isAssignableFrom(mTypes[i])) {
//							mValues[i] = olds[i];
//							((ElfEnum) mValues[i]).setCurrent(getContext(mControls[i]));
//						} else {
//							mValues[i] = Stringified.fromText(mTypes[i], getContext(mControls[i]))[0];
//						}
//					} catch (Exception exception) {
//					}
//
//					if (ElfEnum.class.isAssignableFrom(mTypes[i])) {
//						if (!((ElfEnum) mValues[i]).isEnum()) {
//							failed = true;
//							mControls[i].setBackground(ColorFactory.RED);
//						}
//					} else if (mValues[i] == null) {
//						failed = true;
//						mControls[i].setBackground(ColorFactory.RED);
//					}
//				}
//				if (!failed) {
//					shell.close();
//				}
//			}
//		});
//
//		Button cancelButton = new Button(composite, SWT.PUSH);
//		cancelButton.setText(Constants.POP_DIALOG_CANCEL);
//		cancelButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				mValues = null;
//				shell.close();
//			}
//		});
//
//		shell.setDefaultButton(okButton);
//	}
//
//	@SuppressWarnings("unchecked")
//	void createTextWidgets(final Shell shell) {
//		if (mTypes == null)
//			return;
//
//		Composite composite = new Composite(shell, SWT.NONE);
//		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 2;
//		composite.setLayout(layout);
//
//		mControls = new Control[mTypes.length];
//		for (int i = 0; i < mTypes.length; i++) {
//			Label label = new Label(composite, SWT.RIGHT);
//			if (mLabels == null || mLabels.length <= i) {
//				label.setText("Unknow");
//			} else {
//				label.setText("" + mLabels[i]);
//			}
//
//			final Composite composite2 = new Composite(composite, SWT.NONE);
//			composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//			GridLayout layout2 = new GridLayout();
//			layout2.numColumns = 2;
//			composite2.setLayout(layout2);
//			GridData gridData = new GridData();
//			gridData.widthHint = 400;
//			composite2.setLayoutData(gridData);
//
//			final ClassType type = TypeFactory.getType(mTypes[i]);
//
//			switch (type) {
//			case ARRAY_TYPE:// text + multiLine?
//			{
//				Text text = new Text(composite2, SWT.BORDER);
//				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//				try {
//					text.setText("" + Stringified.toText(mValues[i], false));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = text;
//				text.setSelection(0, text.getText().length());
//			}
//				;
//				break;
//			case BOOL_TYPE:// combo
//			{
//				CCombo combo = new CCombo(composite2, SWT.BORDER);
//				combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//				combo.setItems(EnumHelper.toString(BoolEnum.class));
//				try {
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				combo.setText("" + Stringified.toText(mValues[i], false));
//
//				mControls[i] = combo;
//			}
//				;
//				break;
//			case CHAR_TYPE:// text + multiLine?
//			{
//				Text text = new Text(composite2, SWT.BORDER);
//				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//				try {
//					text.setText("" + Stringified.toText(mValues[i], false));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = text;
//				text.setSelection(0, text.getText().length());
//			}
//				;
//				break;
//			case DOUBLE_TYPE:// text + multiLine?
//			{
//				Text text = new Text(composite2, SWT.BORDER);
//				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//				try {
//					text.setText("" + Stringified.toText(mValues[i], false));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = text;
//				text.setSelection(0, text.getText().length());
//			}
//				;
//				break;
//			case ELSE_TYPE:// text + multiLine?
//			{
//				Text text = new Text(composite2, SWT.BORDER);
//				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//				try {
//					text.setText("" + Stringified.toText(mValues[i], false));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = text;
//				text.setSelection(0, text.getText().length());
//			}
//				;
//				break;
//			case ENUM_TYPE:// combo
//			{
//				CCombo combo = new CCombo(composite2, SWT.BORDER);
//				combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//				combo.setItems(EnumHelper.toStringNoT((Class<? extends Enum<?>>) mTypes[i]));
//				try {
//					combo.setText("" + Stringified.toText(mValues[i], false));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = combo;
//			}
//				;
//				break;
//			case FLOAT_TYPE:// text + multiLine?
//			{
//				Text text = new Text(composite2, SWT.BORDER);
//				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//				try {
//					text.setText("" + Stringified.toText(mValues[i], false));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = text;
//				text.setSelection(0, text.getText().length());
//			}
//				;
//				break;
//			case INT_TYPE:// spin
//			{
//				Spinner spinner = new Spinner(composite2, SWT.BORDER);
//				spinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//				spinner.setMaximum(Integer.MAX_VALUE);
//				spinner.setMinimum(Integer.MIN_VALUE);
//				try {
//					System.err.println("Int " + mValues[i]);
//					spinner.setSelection(Integer.valueOf(Stringified.toText(mValues[i], false)));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = spinner;
//			}
//				;
//				break;
//			case LONG_TYPE:// text + multiLine?
//			{
//				Text text = new Text(composite2, SWT.BORDER);
//				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//				try {
//					text.setText("" + Stringified.toText(mValues[i], false));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = text;
//				text.setSelection(0, text.getText().length());
//			}
//				;
//				break;
//			case STRING_TYPE:// text + multiLine?
//			{
//				Text text = new Text(composite2, SWT.BORDER);
//				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//				try {
//					text.setText("" + Stringified.toText(mValues[i], false));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = text;
//				text.setSelection(0, text.getText().length());
//			}
//				;
//				break;
//			case ELFENUM_TYPE: {
//				CCombo combo = new CCombo(composite2, SWT.BORDER);
//				combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//				if (!(mValues[i] instanceof ElfEnum)) {
//					throw new IllegalArgumentException(String.format("mValues[%d] Must Be ElfEnum", i));
//				}
//
//				combo.setItems(((ElfEnum) mValues[i]).getEnums());
//				try {
//					combo.setText("" + ((ElfEnum) mValues[i]).getCurrent());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//				mControls[i] = combo;
//			}
//				break;
//			}
//
//			final Button button = new Button(composite2, SWT.PUSH);
//			button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
//
//			switch (type) {
//			case BOOL_TYPE:
//			case CHAR_TYPE:
//			case ENUM_TYPE:
//			case FLOAT_TYPE:
//			case INT_TYPE:
//				button.setText(LanguageManager.get(type.name().substring(0, type.name().indexOf("_"))));
//				button.setEnabled(false);
//				break;
//			case STRING_TYPE:
//				button.setText(LanguageManager.get(type.name().substring(0, type.name().indexOf("_"))));
//				button.setEnabled(true);
//				try {
//					if (mLabels[i].contains("_REF_")) {
//						final String head = mLabels[i].substring(0, mLabels[i].indexOf("_REF_"));
//						final String tail = mLabels[i].substring(mLabels[i].indexOf("_REF_") + 5);
//						label.setText("" + head);
//						final int ii = i;
//						if (tail.equals("DIR")) {
//							button.addSelectionListener(new SelectionAdapter() {
//								public void widgetSelected(SelectionEvent e) {
//									final DirectoryDialog directoryDialog = new DirectoryDialog(shell);
//									final String path = directoryDialog.open();
//									if (path != null) {
//										setContext(mControls[ii], path);
//									}
//								}
//							});
//						} else if (tail.startsWith("FILE")) {
//							button.addSelectionListener(new SelectionAdapter() {
//								public void widgetSelected(SelectionEvent e) {
//									final FileDialog fileDialog = new FileDialog(shell);
//									if (tail.charAt(4) == '_') {
//										final String[] subs = tail.substring(5).split("_");
//										if (subs != null) {
//											for (int i = 0; i < subs.length; i++) {
//												subs[i] = "*." + subs[i];
//											}
//											fileDialog.setFilterExtensions(subs);
//										}
//									}
//
//									final String path = fileDialog.open();
//									if (path != null) {
//										setContext(mControls[ii], path);
//									}
//								}
//							});
//						}
//
//						button.setText(LanguageManager.get("Browse"));
//					} else {
//						try {
//							button.addSelectionListener(new MySelectionListener(mControls[i], mTypes[i], label.getText()));
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				break;
//			default:
//				button.setText(LanguageManager.get("Advance"));
//				try {
//					button.addSelectionListener(new MySelectionListener(mControls[i], mTypes[i], label.getText()));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			addModifierListener(mControls[i]);
//		}
//	}
//
//	private class MySelectionListener implements SelectionListener {
//		final Control mControl;
//		final Class<?> mType;
//		final String mTitle;
//
//		MySelectionListener(final Control control, final Class<?> type, final String title) {
//			this.mControl = control;
//			this.mType = type;
//			this.mTitle = title;
//		}
//
//		public void widgetSelected(SelectionEvent e) {
//			final AnalysisDialog<Object> dialog = new AnalysisDialog<Object>("" + mTitle, true);
//			try {
//				final Object ret = dialog.open(Stringified.fromText(mType, getContext(mControl))[0], mType);
//				if (ret != null) {
//					setContext(mControl, Stringified.toText(ret, false));
//				}
//			} catch (Exception exception) {
//			}
//		}
//
//		public void widgetDefaultSelected(SelectionEvent e) {
//			widgetSelected(e);
//		}
//	}
//
//	private final ModifyListener mModifyListener = new ModifyListener() {
//		public void modifyText(ModifyEvent e) {
//			final Control control = (Control) e.widget;
//			if (control != null) {
//				control.setBackground(ColorFactory.WHITE);
//			}
//		}
//	};
//
//	void addModifierListener(final Control control) {
//		if (control instanceof Text) {
//			((Text) control).addModifyListener(mModifyListener);
//		} else if (control instanceof CCombo) {
//			((CCombo) control).addModifyListener(mModifyListener);
//		} else if (control instanceof Spinner) {
//			((Spinner) control).addModifyListener(mModifyListener);
//		}
//	}
//
//	String getContext(final Control control) {
//		if (control instanceof Text) {
//			return ((Text) control).getText();
//		} else if (control instanceof CCombo) {
//			return ((CCombo) control).getText();
//		} else if (control instanceof Spinner) {
//			return "" + ((Spinner) control).getSelection();
//		} else if (control instanceof Label) {
//			return ((Label) control).getText();
//		}
//		return null;
//	}
//
//	void setContext(final Control control, String text) {
//		if (control instanceof Text) {
//			((Text) control).setText(text);
//		} else if (control instanceof CCombo) {
//			((CCombo) control).setText(text);
//		} else if (control instanceof Spinner) {
//			try {
//				((Spinner) control).setSelection(Integer.valueOf(text));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else if (control instanceof Label) {
//			((Label) control).setText("" + text);
//		}
//	}
//
//	public static void main(String[] args) {
//
//	}
//
//}
