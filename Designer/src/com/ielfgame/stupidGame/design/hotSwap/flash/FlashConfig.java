package com.ielfgame.stupidGame.design.hotSwap.flash;

public class FlashConfig {
	/***
	 * static functions
	 * 760.0,34.0
	 */
	public static float getXByFrame(float frame) {
		return 5 + -sFrameBarLength/2 + 10 *frame;
	}
	
	public static int getFrameByX(float x) {
		return Math.round((x - 5 +sFrameBarLength/2)/10 );
	}
	
	private static float sFrameBarLength;
	public static void setFrameBarLength(float l) {
		sFrameBarLength = l;
	}
	
//	public static float getFrameBarLength() {
//		return sFrameBarLength;
//	}
	
	private static float sFrameStartXInScreen;
	public static float getFrameStartXInScreen() {
		return sFrameStartXInScreen;
	}
	public static void setFrameStartXInScreen(float x) {
		sFrameStartXInScreen = x;
	}
	
	public static int getFrameDiffByXDiff(float diffX) {
		return getFrameByX(diffX) - getFrameByX(0);
	}
}
