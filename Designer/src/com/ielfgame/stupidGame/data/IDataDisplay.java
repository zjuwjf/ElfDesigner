package com.ielfgame.stupidGame.data;

/**
 * @author zju_wjf
 * @Special dialog
 */ 
public interface IDataDisplay{
	public abstract String toTitle();
	public abstract String [] toLabels();
	public abstract String [] toValues();
	public int fromValues(final String [] values);
} 
