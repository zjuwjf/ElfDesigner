package elfEngine.basic.touch;

import com.ielfgame.stupidGame.enumTypes.ClickState;

import elfEngine.basic.node.ElfNode;

public class ClickEventDecoder extends BasicEventDecoder {
	
	public void setDelegate(ElfNode node) {
		super.setDelegate(node);
		
		assert(node instanceof IClickable);
	} 
	
	long mMaxClickTime = 500;
	public void setClickTime(final long time) {
		mMaxClickTime = time;
	} 
	public long getClickTime() {
		return mMaxClickTime;
	} 
	
	float mMaxMove = 10.0f;
	public void setMaxMove(float move) {
		mMaxMove = move;
	} 
	public float getMaxMove() {
		return mMaxMove;
	} 
	
	boolean mCatched = false;
	long mLastTouchTime = -1;
	
	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) { 
		if(mCatched) {
			if ( Math.abs( getTotalMoveX(event) ) < mMaxMove && Math.abs( getTotalMoveY(event) ) < mMaxMove ) {
				final long now = System.currentTimeMillis();
				if(now - mLastTouchTime > mMaxClickTime) {
					mCatched = false;
					final IClickable clickable = (IClickable)mDelegateNode;
					clickable.onHide(mDelegateNode);
				} 
			} else {
				mCatched = false;
				final IClickable clickable = (IClickable)mDelegateNode;
				clickable.onHide(mDelegateNode);
			} 
		} 
		return false;
	} 

	protected boolean onTouchDwon(ElfEvent event) { 
		final IClickable clickable = (IClickable)mDelegateNode;
		if(mDelegateNode.isInSelectSize(event)) {
			mLastTouchTime = System.currentTimeMillis();
			mCatched = true; 
			clickable.onShow(mDelegateNode);
		} 
		return false;
	}

	protected boolean onTouchUp(ElfEvent event) { 
		if(mCatched) {
			final IClickable clickable = (IClickable)mDelegateNode;
			clickable.onHide(mDelegateNode);
			clickable.trigger(mDelegateNode, event);
			trigger(event);
		}
		return false;
	}
	
	public void run() { 
		final IClickable clickable = (IClickable)mDelegateNode;
		final long now = System.currentTimeMillis();
		if(mCatched && now - mLastTouchTime > mMaxClickTime) {
			mCatched = false;
			clickable.onHide(mDelegateNode);
		}
	} 
	
	public interface IClickable { 
		public void onShow(ElfNode node); 
		public void onHide(ElfNode node); 
		public void onInvaid(ElfNode node); 
		
		public ClickState getState();
		
		public void trigger(ElfNode node, ElfEvent event);
	}
} 
