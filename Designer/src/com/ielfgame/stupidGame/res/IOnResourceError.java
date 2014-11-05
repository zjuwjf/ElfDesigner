package com.ielfgame.stupidGame.res;

public interface IOnResourceError {
	public void onConfict(final String name, final String path1, final String path2);
	public void onIlleagl(final String name, final String path);
	public void flush();
};
