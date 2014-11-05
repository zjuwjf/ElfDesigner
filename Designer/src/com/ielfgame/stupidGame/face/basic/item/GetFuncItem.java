package com.ielfgame.stupidGame.face.basic.item;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.data.TypeFactory;
import com.ielfgame.stupidGame.data.TypeFactory.ClassType;
import com.ielfgame.stupidGame.dialog.AnalysisDialog;
import com.ielfgame.stupidGame.language.LanguageManager;

public class GetFuncItem extends ATreeItem { 
	//
	protected final Class<?> mClassSelect;
	protected final Class<?> mClassType;
	protected ClassType mValueClassType;
	
	protected Method mGetFunc;
	
	protected final String mDialogTitle;
	protected final String[] mDialogValues;
	
	protected TreeItem mMyItem;
	protected Tree mTree;
	protected Control mControl;
	
	protected final boolean mIsGlobal;
	
	protected long mLastModifierTime = System.currentTimeMillis();
	
	protected final int MAX_MODIFIER_TIME = 2000;
	protected final int NORMAL_MODIFIER_TIME = 500;
	
	public GetFuncItem(boolean global, Class<?> classSelect, Class<?> classType, String getFunc, String setFunc, String title, String[] values, boolean direct) {
		this.mIsGlobal = global;
		this.mClassSelect = classSelect;
		this.mClassType = classType;
		
		try {
			this.mGetFunc = mClassSelect.getMethod(getFunc);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} 
		

		this.mDialogTitle = LanguageManager.get(title); 
		this.mDialogValues = values;
	} 

	public GetFuncItem(boolean global, Class<?> classSelect, Class<?> classType, String funcName, String[] values, boolean direct) {
		this(global, classSelect, classType, "get" + funcName, "set" + funcName, funcName, values, direct);
	}
	
	public GetFuncItem(boolean global, Class<?> classSelect, Class<?> classType, String funcName, boolean direct) {
		this(global, classSelect, classType, funcName, new String[] { funcName }, direct);
	} 
	

	public final TreeItem create(final TreeItem root) {
		mTree = root.getParent();
		mMyItem = new TreeItem(root, SWT.NONE);
		mMyItem.setText(0, mDialogTitle);
		
		mControl = null;
		mValueClassType = TypeFactory.getType(mClassType);

		final Tree tree = mMyItem.getParent();
		tree.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				final TreeItem[] items = tree.getSelection();
				if (items != null && items.length > 0 && items[0] == mMyItem) {
					onDoubleClick();
				} 
			} 
		}); 
		
		return mMyItem;
	} 

	public void onDoubleClick() { 
		try {
			final AnalysisDialog<Object> dialog = new AnalysisDialog<Object>(mDialogTitle, false);
			final Object in = getValue();
			dialog.open(in, mClassType); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	public Object getValue() {
		final String text = getContext();
		final Object [] ret = Stringified.fromText(mClassType, text);
		final Object objRet = ret[0];
		return objRet;
	} 

	public final void run(boolean instance) {
		final long now = System.currentTimeMillis();
		final long time;
		
		if((this.mValueClassType == ClassType.INT_TYPE || this.mValueClassType == ClassType.FLOAT_TYPE)) {
			time = MAX_MODIFIER_TIME;
		} else {
			time = NORMAL_MODIFIER_TIME;
		} 
		if(instance || time + mLastModifierTime < now) {
			//need update 
			try { 
				final Object [] objs = this.getSelectObjs(mIsGlobal);
				if(objs != null && objs.length > 0 && mClassSelect.isAssignableFrom(objs[0].getClass())) {
					final Object value = this.mGetFunc.invoke(objs[0]);
					setContext(""+Stringified.toText(value, false)); 
				} 
			} catch (Exception e) { 
				e.printStackTrace();
			} 
			mLastModifierTime = now;
		} 
	}
	
	String getContext() {
		final TreeItem item = mMyItem;
		final Control control = mControl;
		if (control == null) {
			final String ret = item.getText(1);
			return ret;
		} else {
			if (control instanceof Text) {
				return ((Text) control).getText();
			} else if (control instanceof CCombo) {
				return ((CCombo) control).getText();
			} else if (control instanceof Spinner) {
				return "" + ((Spinner) control).getSelection();
			} else if(control instanceof Label) { 
				return ((Label) control).getText();
			}
		} 
		return null;
	}

	void setContext(String text) {
		final String oldText = getContext();
		if (!oldText.equals(text)) {
			final TreeItem item = mMyItem;
			final Control control = mControl;
			if (control == null) {
				item.setText(1, text );
			} else {
				if(control instanceof Label) {
					((Label) control).setText(""+text);
				}
			}
		}
	}

	public TreeItem getMyTreeItem() {
		return mMyItem;
	}

	public String copyContext() {
		return getContext();
	}

	public boolean pasteContext(String paste) { 
		return false; 
	} 
	
	public Class<?> getValueType() {
		return this.mClassType;
	}

	public void setValue(Object value) {
	} 
}
