package com.ielfgame.stupidGame.dialog;

public interface IDialog<T>{
	public T open(T value, Class<?> type);
} 
