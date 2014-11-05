package com.ielfgame.stupidGame.dialog;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ielfgame.stupidGame.Constants;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.Redirect;
import com.ielfgame.stupidGame.config.MenuConfig;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.IDataDisplay;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ColorWindow;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.list.ElfList;
import elfEngine.basic.node.ElfNode;

public class PopDialog {
	public enum PopType{
		NEW_NODE(Constants.POP_DIALOG_NEW_NODE, Constants.POP_DIALOG_LABEL_NEW_NODE), 
		RENAME(Constants.POP_DIALOG_RENAME, Constants.POP_DIALOG_LABEL_RENAME), 
		SET_INDEX(Constants.POP_DIALOG_SET_INDEX, Constants.POP_DIALOG_LABEL_SET_INDEX), 
		DELETE(Constants.POP_DIALOG_DELETE, Constants.POP_DIALOG_LABEL_DELETE),
		COPY(Constants.POP_DIALOG_COPY, Constants.POP_DIALOG_LABEL_COPY),
		COPY_DEEP(Constants.POP_DIALOG_COPY_DEEP, Constants.POP_DIALOG_LABEL_COPY),
		REFRESH_NODE(Constants.POP_DIALOG_NEW_NODE, Constants.POP_DIALOG_LABEL_NEW_NODE), 
		;
		private final String mTitle;
		private final String mLabel;
		PopType(String title, final String label){
			mTitle = title;
			mLabel = label;
		}
	};
	
	private Shell mShell;
	private String[] mValues;
	private String[] mLabels;
	private PopType [] mPopTypes;
	private ElfNode mSelectNode;
	private String mTitle;
	
	public PopDialog(Shell parent, final ElfNode selectNode, PopType...popTypes) {
		mShell = parent;
		
		mPopTypes = popTypes;
		if(popTypes.length > 0)
			mTitle = mPopTypes[0].mTitle;
		
		mSelectNode = selectNode;
		
		final String [] labels = new String[mPopTypes.length];
		final String [] values = new String[mPopTypes.length];
		
		for(int i=0; i<labels.length; i++){
			if(mPopTypes[i] == PopType.DELETE){ 
				labels[i] = mPopTypes[i].mLabel + " " + mSelectNode.getName() + "?";
			} else {
				labels[i] = mPopTypes[i].mLabel;
			} 
			
			switch(mPopTypes[i]){
			case NEW_NODE: values[i] = Constants.POP_DIALOG_TEXT_NEW_NODE;break;
			case RENAME: values[i] = mSelectNode.getName();break;
			case SET_INDEX: values[i] = ""+indexOfFather(mSelectNode);break;
			case DELETE: values[i] = mSelectNode.getName();break;
			case COPY: 
//				if(MainNodeMenu.getAutoNameSelected()) {
//					values[i] = mSelectNode.getCopyName(mSelectNode.getFatherNode());
//				} else {
//					values[i] = mSelectNode.getName();
//				}
//				break;
			case COPY_DEEP: 
				final MenuConfig menuConfig = PowerMan.getSingleton(MenuConfig.class);
				if(menuConfig.AutoNameWhenCopy) { 
					values[i] = mSelectNode.getCopyName(mSelectNode.getParent());
				} else { 
					values[i] = mSelectNode.getName();
				} 
				break;
			}
		}
		
		setLabels(labels);
		setValues(values);
	}
	
	public PopDialog() {
		mShell = PowerMan.getSingleton(MainDesigner.class).mShell;
	}
	
	public PopDialog(final IDataDisplay data) {
		mShell = PowerMan.getSingleton(MainDesigner.class).mShell;
		this.setTitle(data.toTitle());
		this.setLabels(data.toLabels());
		this.setValues(data.toValues());
	}
	
	
	private int indexOfFather(ElfNode node){
		final ElfNode father = (ElfNode)node.getParent();
		final ElfList<ElfNode> list = father.getChildList();
		int index = 0;
		final ElfList<ElfNode>.Iterator it = list.iterator(true);
		while(it.hasNext()){
			if(it.next() == node){
				return index;
			} 
			index ++;
		}
		return 0;
	}

//	private void addTextListener(final Text text) {
//		text.addModifyListener(new ModifyListener() {
//			public void modifyText(ModifyEvent e) {
//				Integer index = (Integer) (text.getData("index"));
//				mValues[index.intValue()] = text.getText();
//			}
//		});
//	}

	private void createControlButtons(final Shell shell) {
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		mValues = null;
		
		Button okButton = new Button(composite, SWT.PUSH);
		okButton.setText(Constants.POP_DIALOG_OK);
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final int size = mTextList.size();
				mValues = new String[size]; 
				for(int i=0; i<size; i++){ 
					try { 
						mValues[i] = mTextList.get(i).getText(); 
					} catch (Exception exception) { 
					} 
				} 
				shell.close(); 
			} 
		});

		Button cancelButton = new Button(composite, SWT.PUSH);
		cancelButton.setText(Constants.POP_DIALOG_CANCEL);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				mValues = null;
				shell.close();
			}
		});

		shell.setDefaultButton(okButton);
	}
	
	ArrayList<Text> mTextList = new ArrayList<Text>();

	private void createTextWidgets(final Shell shell) {
		if (mLabels == null)
			return;

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		for (int i = 0; i < mLabels.length; i++) { 
			Label label = new Label(composite, SWT.RIGHT);
			label.setText(mLabels[i]);
			
			if(mLabels[i].equalsIgnoreCase(Constants.ALPHA)){
				Composite composite2 = new Composite(composite, SWT.NONE);
				composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				GridLayout layout2 = new GridLayout();
				layout2.numColumns = 3;
				composite2.setLayout(layout2);
				GridData gridData = new GridData();
				gridData.widthHint = 400;
				composite2.setLayoutData(gridData);
				
				Text text = new Text(composite2, SWT.BORDER);
				if(mVerifyListeners != null && mVerifyListeners.length > i && mVerifyListeners[i] != null)
					text.addVerifyListener(mVerifyListeners[i]);
				mTextList.add(text);
				
				text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				if (mValues.length > i && mValues[i] != null) {
					text.setText(mValues[i]);
				} else { 
					text.setText(""); 
				}
				
				Button button = new Button(composite2, SWT.PUSH);
				button.setText("Custom Color");
				button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
				button.addSelectionListener(new SelectionAdapter() { 
					public void widgetSelected(SelectionEvent e) { 
						ColorDialog dialog = new ColorDialog(shell);
						try {
							final float red = Float.valueOf(mTextList.get(0).getText());
							final float green = Float.valueOf(mTextList.get(1).getText());
							final float blue = Float.valueOf(mTextList.get(2).getText());
							dialog.setRGB(new RGB(red, green, blue));
						} catch (Exception e2) {
						}
						
						RGB rgb = dialog.open();
						if(rgb != null){
							mTextList.get(0).setText(""+rgb.red/255f);
							mTextList.get(1).setText(""+rgb.green/255f);
							mTextList.get(2).setText(""+rgb.blue/255f);
						} 
					} 
				});
				
				Button pickbutton = new Button(composite2, SWT.PUSH);
				pickbutton.setEnabled(FileHelper.IS_WINDOWS);
				
				pickbutton.setText("Pick Color");
				pickbutton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
				pickbutton.addSelectionListener(new SelectionAdapter() { 
					public void widgetSelected(SelectionEvent e) { 
						final ElfColor color = new ColorWindow().open();
						if(color != null) {
							mTextList.get(0).setText(""+color.red);
							mTextList.get(1).setText(""+color.green);
							mTextList.get(2).setText(""+color.blue);
						}
					} 
				});
			} else {
				Text text = new Text(composite, SWT.BORDER);
				if(mVerifyListeners != null && mVerifyListeners.length > i && mVerifyListeners[i] != null)
					text.addVerifyListener(mVerifyListeners[i]);
				
				GridData gridData = new GridData();
				gridData.widthHint = 400;
				text.setLayoutData(gridData);
				if (mValues.length > i && mValues[i] != null) {
					text.setText(mValues[i]);
				} else {
					text.setText("");
				}
				text.setSelection(0, text.getText().length());
				mTextList.add(text);
			} 
		}
	}

	public String[] getLabels() {
		return mLabels;
	}
	
	public String getTitle() {
		return mTitle;
	}

	public String[] getValues() {
		return mValues;
	}

	public String[] open() {
		final Shell shell = new Shell(mShell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
//		RunState.initChildShell(shell);
		shell.setLayout(new GridLayout());
		
		shell.setText(mTitle);
		
		createTextWidgets(shell);
		createControlButtons(shell);
		
		shell.pack();
		shell.setLocation(shell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - shell.getSize().x/2, 
				shell.getDisplay().getPrimaryMonitor().getClientArea().height/2 - shell.getSize().y/2);
		
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return getValues();
	} 
	
	public void setLabels(String...labels) {
		this.mLabels = labels;
	}

	public void setTitle(String title) {
		mTitle = title;
	}
	
	public void setValues(String...itemInfo) {
		if (mLabels == null)
			return;
		
		if (mValues == null || mValues.length < mLabels.length)
			mValues = new String[mLabels.length];
		
		int numItems = Math.min(mValues.length, itemInfo.length);
		for (int i = 0; i < numItems; i++) {
			mValues[i] = itemInfo[i];
		}
	}
	
	private VerifyListener [] mVerifyListeners;
	public void setVerifyListener(VerifyListener...verifyListeners){
		mVerifyListeners = verifyListeners;
	}
	
	//^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$ ����
	//^[1-9]d*|0$�� //ƥ��Ǹ����������� + 0�� 
	//^-[1-9]d*|0$���� //ƥ������������� + 0��
	//^[1-9]d*$�� �� //ƥ�������� 
	//^-[1-9]d*$ �� //ƥ�为���� 
	//^-?[1-9]d*$���� //ƥ������ 
	//^[A-Za-z]+$����//ƥ����26��Ӣ����ĸ��ɵ��ַ� 
	//^[A-Z]+$����//ƥ����26��Ӣ����ĸ�Ĵ�д��ɵ��ַ� 
	//^[a-z]+$����//ƥ����26��Ӣ����ĸ��Сд��ɵ��ַ� 
	//^[A-Za-z0-9]+$����//ƥ�������ֺ�26��Ӣ����ĸ��ɵ��ַ� 
	//^w+$����//ƥ�������֡�26��Ӣ����ĸ�����»�����ɵ��ַ� 
	public static final VerifyListener mPlusIntegerVerify = new VerifyListener() {
		public void verifyText(VerifyEvent e) {
		    Pattern pattern = Pattern.compile("^[1-9]d*$");   
		    Matcher matcher = pattern.matcher(e.text);  
		    if (matcher.matches()){
		    	e.doit = true;   
		    } else if (e.text.length() > 0){
		    	e.doit = false;   
		    }  else {
		    	e.doit = true;   
		    } 
		}
	};
	
	public static final VerifyListener mFloatVerify = new VerifyListener() {
		public void verifyText(VerifyEvent e) {
//			Pattern pattern1 = Pattern.compile("^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$");
		    Pattern pattern =  Pattern.compile("^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$");   
		    
		    
		    Matcher matcher = pattern.matcher(e.text);
		    Redirect.outPrintln("input:"+e.text);
		    if (matcher.matches()){
		    	e.doit = true;   
		    } else if (e.text.length() > 0){
		    	e.doit = false;
		    }  else {
		    	e.doit = true;   
		    } 
		}
	};
	
	public static final VerifyListener mNameVerify = new VerifyListener() {
		public void verifyText(VerifyEvent e) {
		    Pattern pattern = Pattern.compile("^w+$");
		    Matcher matcher = pattern.matcher(e.text);  
		    if (matcher.matches()){
		    	e.doit = true;
		    } else if (e.text.length() > 0){
		    	e.doit = false;   
		    }  else {
		    	e.doit = true;   
		    } 
		}
	};
}
