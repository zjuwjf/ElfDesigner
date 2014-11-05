package com.ielfgame.stupidGame.face.basic;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.data.TypeFactory;
import com.ielfgame.stupidGame.data.TypeFactory.ClassType;
import com.ielfgame.stupidGame.face.basic.item.AllFuncItem;
import com.ielfgame.stupidGame.face.basic.item.GetFuncItem;
import com.ielfgame.stupidGame.face.basic.item.SetFuncItem;
import com.ielfgame.stupidGame.language.LanguageManager;

public class PropertyRootItem {
	
	protected final Class<?> mSelectClass;
	protected final boolean mIsGlobal;
	
	protected final LinkedList<ITreeItem> mItemList = new LinkedList<ITreeItem>();
	
	private ICurrentSelect mICurrentSelect = ICurrentSelect.CurrentSelect_Default;
	public ICurrentSelect getCurrentSelect() {
		return mICurrentSelect;
	} 
	public void setCurrentSelect(ICurrentSelect mCurrentSelect) {
		this.mICurrentSelect = mCurrentSelect;
	}
	/**
	 * @param selectClass
	 * @param global
	 */
	public PropertyRootItem(Class<?> selectClass, boolean global) {
		this.mSelectClass = selectClass;
		this.mIsGlobal = global;
	} 
	
	public ITreeItem addAllFuncItem(Class<?> valueType, String func, boolean undo) { 
		return this.addAllFuncItem(valueType, func, mICurrentSelect, undo);
	} 
	public ITreeItem addAllFuncItem(Class<?> valueType, String func, ICurrentSelect current, boolean undo) { 
		final AllFuncItem item = new AllFuncItem(mIsGlobal, mSelectClass, valueType, func, undo);
		
		mItemList.add(item); 
		item.setCurrentSelect(current);
		return item; 
	} 
	
	public ITreeItem addGetFuncItem(Class<?> valueType, String func) { 
		return this.addGetFuncItem(valueType, func, mICurrentSelect);
	} 
	public ITreeItem addGetFuncItem(Class<?> valueType, String func, ICurrentSelect current) { 
		final ClassType type = TypeFactory.getType(valueType);
		final boolean directModifier = (type != ClassType.ARRAY_TYPE && type != ClassType.ELSE_TYPE && type != ClassType.STRING_TYPE);
		final GetFuncItem item = new GetFuncItem(mIsGlobal, mSelectClass, valueType, func, directModifier);
		
		mItemList.add(item); 
		item.setCurrentSelect(current);
		return item;
	} 
	
	public ITreeItem addSetFuncItem(Class<?> valueType, String func) { 
		return this.addSetFuncItem(valueType, func, mICurrentSelect); 
	} 
	public ITreeItem addSetFuncItem(Class<?> valueType, String func, ICurrentSelect current) { 
		final SetFuncItem item = new SetFuncItem(mIsGlobal, mSelectClass, valueType, func);
		mItemList.add(item); 
		item.setCurrentSelect(current);
		return item;
	} 
	
	public TreeItem create(final Tree tree) { 
		final TreeItem rootItem = new TreeItem(tree, SWT.NONE); 
		rootItem.setText(LanguageManager.get(mSelectClass.getSimpleName())); 
		
		for(ITreeItem item : mItemList) { 
			item.create(rootItem); 
		} 
		rootItem.setExpanded(true);
		return rootItem;
	} 
	public final void run(boolean instance){ 
		for(ITreeItem item : mItemList) { 
			item.run(instance); 
		} 
	} 
} 
