package com.ielfgame.stupidGame.enumTypes;

public enum Orientation {
	Horizontal, Vertical,
	Horizontal_R2L, Vertical_B2T;
	
	public boolean isHorizontal() {
		return this == Horizontal || this == Horizontal_R2L;
	}
}
