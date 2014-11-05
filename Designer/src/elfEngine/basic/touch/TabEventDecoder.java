package elfEngine.basic.touch;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.ButtonEventDecoder.IButton;

public class TabEventDecoder extends BasicEventDecoder{
	
	private boolean mIsTriggerOnDown;
	private boolean mIsClaimed;
	
	public boolean getTriggerOnDown() {
		return mIsTriggerOnDown;
	}

	public void setTriggerOnDown(boolean mIsTriggerOnDown) {
		this.mIsTriggerOnDown = mIsTriggerOnDown;
	}
	
	public void setDelegate(ElfNode node) {
		super.setDelegate(node);
		assert(node instanceof IButton);
	}
	
	public IButton getButton() {
		return (IButton)mDelegateNode;
	} 
	
	private boolean mTouchGiveUpOnMoveOrOutOfRange = false;
	public void setTouchGiveUpOnMoveOrOutOfRange(boolean set) {
		mTouchGiveUpOnMoveOrOutOfRange = set;
	}
	public boolean getTouchGiveUpOnMoveOrOutOfRange() {
		return mTouchGiveUpOnMoveOrOutOfRange;
	}
	
	boolean isTouchGiveUp(ElfEvent event) {
		final ElfPointf last = getLastTouchDownPointInScreen(event);
		if(mTouchGiveUpOnMoveOrOutOfRange) {
			return  (Math.abs(last.x-event.x) > sThreshold ||
					Math.abs(last.y-event.y) > sThreshold);
		} else {
			return mDelegateNode.isInSelectSize(event);
		}
	}
	
	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		if(!mIsTriggerOnDown) {
			if(mIsClaimed && isTouchGiveUp(event)) {
				mIsClaimed = false; 
//				getButton().onReleased();
			} 
		} 
		return false;
	} 

	protected boolean onTouchDwon(ElfEvent event) { 
		
		mIsClaimed = mDelegateNode.isInSelectSize(event);

		if(mIsTriggerOnDown && mIsClaimed) { 
			getButton().onPressed(); 
			getButton().trigger(mDelegateNode, event); 
		} 
		
		return mIsClaimed;
	} 

	protected boolean onTouchUp(ElfEvent event) { 
		if( !mIsTriggerOnDown ) {
			if(mIsClaimed) { 
				if(!(mDelegateNode.isInSelectSize(event))) { 
//					getButton().onReleased();
					mIsClaimed = false;
				} else {
					getButton().onPressed(); 
					getButton().trigger(mDelegateNode, event); 
				}
			} 
		} 
		return false;
	} 
} 
