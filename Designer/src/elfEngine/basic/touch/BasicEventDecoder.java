package elfEngine.basic.touch;

import java.util.LinkedList;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.TouchNode;

public abstract class BasicEventDecoder implements IElfOnTouch{
	
	protected static final float sThreshold = 10;
	
	public interface TriggerListener {
		public void trigger(ElfNode node, ElfEvent event) ;
	} 
	
	private final LinkedList<TriggerListener> mListeners = new LinkedList<TriggerListener>();
	public void addListener(final TriggerListener listener) {
		mListeners.add(listener);
	}
	public void clearListeners() {
		mListeners.clear();
	}
	
	protected void trigger(ElfEvent event) { 
		for(final TriggerListener listener : mListeners) { 
			listener.trigger(mDelegateNode, event); 
		} 
	}
	
	protected ElfNode mDelegateNode = null;
	
	public void setDelegate(ElfNode node) {
		mDelegateNode = node;
	}
	
	public ElfNode getDelegateNode() {
		return mDelegateNode;
	}
	
	private static float sLastX = -1, sLastY, sMoveX = 0, sMoveY = 0, sTotalMoveX=0, sTotalMoveY=0;
	
	private final static ElfPointf sLastTouchDownPointInScreen = new ElfPointf();//
	
	public ElfPointf getLastTouchDownPointInScreen(ElfEvent event) {
		return new ElfPointf(sLastTouchDownPointInScreen);
	}
	
	protected float getTotalMoveX(ElfEvent event){
		return sTotalMoveX;
	}
	
	protected float getTotalMoveY(ElfEvent event){
		return sTotalMoveY;
	}
	
	protected float getLastX() {
		return sLastX;
	}

	protected float getLastY(ElfEvent event) {
		return sLastY;
	}

	protected float getMoveX(ElfEvent event) {
		return sMoveX;
	}

	protected float getMoveY(ElfEvent event) {
		return sMoveY;
	}

	protected abstract boolean onTouchMove(final ElfEvent event, final float moveX, final float moveY);
	protected abstract boolean onTouchDwon(final ElfEvent event);
	protected abstract boolean onTouchUp(final ElfEvent event);
	
	public boolean onTouch(ElfEvent event) { 
		if(mDelegateNode == null || !mDelegateNode.isRealVisible(event)) {
			return false; 
		} 
		
		boolean notPenetrate = true;
		if(mDelegateNode instanceof TouchNode) {
			notPenetrate = !((TouchNode)mDelegateNode).getPenetrate();
		}
		
		switch(event.action){ 
		case MotionEvent.LEFT_DOWN:
			boolean ret = onTouchDwon(event) & notPenetrate;
			return ret;
		case MotionEvent.LEFT_MOVE:
			return onTouchMove(event, sMoveX, sMoveY) & notPenetrate;
		case MotionEvent.LEFT_UP:
			onTouchUp(event);
			reset();
			
			return false;
		default:
		}
		
		return false;
	}
	
	protected void reset() {
	}
	
	public static void onTouchGlobal(ElfEvent event) {
		
		switch(event.action){ 
		case MotionEvent.LEFT_DOWN:
			sLastX = event.x;
			sLastY = event.y;
			sLastTouchDownPointInScreen.setPoint(sLastX, sLastY);
		case MotionEvent.LEFT_MOVE:
			if(sLastX>=0){ 
				sMoveX = event.x - sLastX;
				sMoveY = event.y - sLastY;
			} else { 
				sMoveX = 0;
				sMoveY = 0;
			} 
			
			sLastX = event.x;
			sLastY = event.y;
			
			sTotalMoveX += sMoveX;
			sTotalMoveY += sMoveY;
			
		case MotionEvent.LEFT_UP:
		default:
		}
	}
	
	public static void resetGlobal(ElfEvent event){
		sLastX = -1;
		sMoveX = sMoveY = 0;
		sTotalMoveX = sTotalMoveY = 0;
		sLastTouchDownPointInScreen.setPoint(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
	}
}
