package elfEngine.basic.touch;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;

public class ButtonEventDecoder extends BasicEventDecoder {

	public interface IButton extends TriggerListener {
		public void onPressed();

		public void onReleased();
	}

	public void setDelegate(ElfNode node) {
		super.setDelegate(node);

		assert (node instanceof IButton);
	}

	public IButton getButton() {
		return (IButton) mDelegateNode;
	}

	private boolean mIsClaimed = false;

	private boolean mTouchGiveUpOnMoveOrOutOfRange = false;

	public void setTouchGiveUpOnMoveOrOutOfRange(boolean set) {
		mTouchGiveUpOnMoveOrOutOfRange = set;
	}

	public boolean getTouchGiveUpOnMoveOrOutOfRange() {
		return mTouchGiveUpOnMoveOrOutOfRange;
	}

	final boolean isTouchGiveUp(final ElfEvent event) {
		
		if (mTouchGiveUpOnMoveOrOutOfRange) {
			final ElfPointf last = getLastTouchDownPointInScreen(event);
			return (Math.abs(last.x - event.x) > sThreshold || Math.abs(last.y - event.y) > sThreshold);
		} else { 
			final boolean ret = (mDelegateNode.isInSelectSize(event));
//			System.err.println("event "+event.x +"," +event.y +","+ret);
			return !ret;
			// return false;
		}
	}

	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		if (mIsClaimed && isTouchGiveUp(event)) {
			mIsClaimed = false;
			getButton().onReleased();
		}

		return false;
	}

	protected boolean onTouchDwon(ElfEvent event) {
		mIsClaimed = mDelegateNode.isInSelectSize(event);
		if (mIsClaimed) {
			getButton().onPressed();
		}
		return mIsClaimed;
	}

	protected boolean onTouchUp(ElfEvent event) {
		if (mIsClaimed) {
			getButton().trigger(mDelegateNode, event);
			getButton().onReleased();
		}

		return false;
	}

	public void reset() {
		mIsClaimed = false;
	}
}
