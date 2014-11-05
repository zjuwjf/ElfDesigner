package elfEngine.basic.touch;

import org.eclipse.swt.SWT;

import com.ielfgame.stupidGame.platform.PlatformHelper;

public class MotionEvent {
	public static final int LEFT_DOWN = 1;
	public static final int LEFT_MOVE = 2;
	public static final int LEFT_UP = 3;
	
	public static final int RIGHT_DOWN = 4;
	public static final int RIGHT_MOVE = 5;
	public static final int RIGHT_UP = 6;
	
	public static final int LEFT_CLICK = 7;
	public static final int RIGHT_CLICK = 8;
	
	public static final int LEFT_DOUBLE_CLICK = 9;
	public static final int RIGHT_DOUBLE_CLICK = 10;
	
	public static final int WHEEL_MOVE = 11;
	
	public static final int CTRL = PlatformHelper.CTRL;
	public static final int SHIFT = SWT.SHIFT;
	
	private float x, y;
	private int action;
	private int stateMask;
	public MotionEvent(float x, float y, int action, int stateMask) {
		super();
		this.x = x;
		this.y = y;
		this.action = action;
		this.stateMask = stateMask;
	}
	public int getStateMask() {
		return stateMask;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public int getAction() {
		return action;
	}
	public void setLocation(float newX, float newY) {
		this.x = newX;
		this.y = newY;
	}
	
}
