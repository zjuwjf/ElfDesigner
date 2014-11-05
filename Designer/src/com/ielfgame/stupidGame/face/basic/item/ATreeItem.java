package com.ielfgame.stupidGame.face.basic.item;

import com.ielfgame.stupidGame.face.basic.ICurrentSelect;
import com.ielfgame.stupidGame.face.basic.ITreeItem;

public abstract class ATreeItem implements ITreeItem{
	private float mInterval = 0.05f;
	public void setValueIntervalf(float interval) {
		this.mInterval = interval;
	}
	public float getValueIntervalf() {
		return mInterval;
	}
	
	private int mIntervali = 1;
	public void setValueIntervali(int interval) {
		mIntervali = interval;
	}
	public int getValueIntervali() {
		return mIntervali;
	}
	
	private ICurrentSelect mCurrentSelect = ICurrentSelect.CurrentSelect_Default;
	public final ICurrentSelect getCurrentSelect() {
		return mCurrentSelect;
	} 
	public final void setCurrentSelect(ICurrentSelect mCurrentSelect) {
		this.mCurrentSelect = mCurrentSelect;
	}
	
	public final Object[] getSelectObjs(boolean gloabl) { 
		if(mCurrentSelect != null) {
			return mCurrentSelect.getSelectObjs(gloabl);
		} else {
			return new Object[0];
		}
	}
}
