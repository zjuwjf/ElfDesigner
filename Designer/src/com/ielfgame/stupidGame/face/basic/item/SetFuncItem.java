package com.ielfgame.stupidGame.face.basic.item;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.dialog.AnalysisDialog;
import com.ielfgame.stupidGame.language.LanguageManager;

public class SetFuncItem extends ATreeItem{
	
	protected Tree mTree;
	protected TreeItem mMyTreeItem;
	protected final Class<?> mClassSelect;
	protected final Class<?> mClassType;
	protected final String mDisplayTile;
	protected final boolean mIsGlobal;
	
	protected Method mFunc;
	
	public SetFuncItem(boolean global, Class<?> classSelect, Class<?> classValue, String func, String title) {
		this.mIsGlobal = global;
		this.mClassSelect = classSelect;
		this.mClassType = classValue;
		
		try {
			if(mClassType == null) {
				this.mFunc = mClassSelect.getMethod("set"+func); 
			} else {
				this.mFunc = mClassSelect.getMethod("set"+func, mClassType); 
			} 
		} catch (Exception e) { 
			e.printStackTrace();
			System.exit(1);
		} 
		
		this.mDisplayTile = LanguageManager.get(title); 
	} 
	
	public SetFuncItem(boolean global, Class<?> classSelect, Class<?> classValue, String func) {
		this(global, classSelect, classValue, func, func);
	} 
	
	public TreeItem create(TreeItem root) {
		mTree = root.getParent();
		mMyTreeItem = new TreeItem(root, SWT.NONE);
		mMyTreeItem.setText(0, mDisplayTile);
		
		final TreeEditor editor = new TreeEditor(mTree);
		editor.grabHorizontal = true;
		
		final int style = SWT.NONE | SWT.BORDER;
		final Button button = new Button(mTree, style);
		editor.setEditor(button, mMyTreeItem, 1);
		button.setText("trigger");
		button.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				try {
					if(mClassType != null) { 
						final AnalysisDialog<Object> dialog = new AnalysisDialog<Object>(mDisplayTile, true);
						final Object input = mClassType.isArray() ? Array.newInstance(mClassType.getComponentType(), 0):mClassType.newInstance();
						final Object value = dialog.open(input, input.getClass()); 
						setValue(value);
					} else { 
						setValue(null);
					} 
				} catch (Exception exception) {
					exception.printStackTrace();
				} 
			} 
			public void widgetDefaultSelected(SelectionEvent e) {
			} 
		}); 
		
		return mMyTreeItem;
	} 
	
	public void setValue(Object value) {
		if(mClassType != null) {
			try {
				if(value != null) {
					final Object [] objs = this.getSelectObjs(mIsGlobal);
					for(Object o : objs) {
						click(o, value);
					} 
				} 
			} catch (Exception exception) {
				exception.printStackTrace();
			} 
		} else { 
			if(value == null) {
				final Object [] objs = this.getSelectObjs(mIsGlobal);
				for(Object o : objs) {
					click(o); 
				} 
			} 
		}
	} 
	
	void click(Object select, Object...args) {
		try { 
			if(mClassType == null) { 
				mFunc.invoke(select);
			} else { 
				mFunc.invoke(select, args);
			} 
		} catch (Exception exception) { 
			exception.printStackTrace(); 
		} 
	} 

	public TreeItem getMyTreeItem() {
		return mMyTreeItem;
	} 
	
	public void onDoubleClick() {
	} 
	
	public void run(boolean instance) {
	}

	public String copyContext() {
		return null;
	} 

	public boolean pasteContext(String paste) {
		final Object [] ret = Stringified.fromText(mClassType, paste);
		final Object value = ret[0];
		setValue(value);
		return value != null;
	} 
	
	public Class<?> getValueType() {
		return this.mClassType;
	} 

	@Override
	public Object getValue() {
		return null;
	} 
}
