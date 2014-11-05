package com.ielfgame.stupidGame.face.basic;

import org.eclipse.swt.widgets.TreeItem;

public interface ITreeItem extends ICurrentSelect{
	
	public void onDoubleClick();
	public TreeItem create(final TreeItem root) ;//create display
	public void run(boolean instance) ;//update display
	public TreeItem getMyTreeItem();
	
	public String copyContext(); 
	public boolean pasteContext(String paste); 
	public Class<?> getValueType(); 
	
	public Object getValue();
	public void setValue(Object value);
	
	public void setValueIntervalf(float interval);
	public float getValueIntervalf();
	
	public void setValueIntervali(int interval);
	public int getValueIntervali();
	
} 
