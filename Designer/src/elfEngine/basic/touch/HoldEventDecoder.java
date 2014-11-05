package elfEngine.basic.touch;

import com.ielfgame.stupidGame.data.ElfPointf;

public class HoldEventDecoder extends BasicEventDecoder implements Runnable{
	
	long mTouchDownTime;
	final ElfPointf mOrigin = new ElfPointf();
	final ElfPointf mSize = new ElfPointf();
	boolean mCatched = false;
	
	float mMaxMove = 5;
	int mHoldIime = 100;
	
	public void setMaxMove(float move) {
		mMaxMove = move;
	}
	public float getMaxMove() {
		return mMaxMove;
	}
	
	public void setHoldTime(int time) {
		mHoldIime = time;
	}
	public int getHoldTime() {
		return mHoldIime;
	} 
	
	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		if(mCatched) {  
			if(!mDelegateNode.isInSelectSize(event)) { 
				mCatched = false;
				return false; 
			} 
			
			if(Math.abs(getTotalMoveX(event)) < mMaxMove && Math.abs(getTotalMoveY(event)) < mMaxMove) {
				final long now = System.currentTimeMillis();
				if(now - mTouchDownTime > mHoldIime) { 
					trigger(event); 
					mCatched = false; 
				} 
				return true;
			} else {
				mCatched = false;
				return false; 
			}
		} 
		
		return false; 
	} 
	
	protected boolean onTouchDwon(ElfEvent event) { 
		if(mDelegateNode.isInSelectSize(event)) { 
			mCatched = true;
			mTouchDownTime = System.currentTimeMillis();
			return true;
		} else {
			mCatched = false;
		} 
		
		return false;
	} 
	
	protected boolean onTouchUp(ElfEvent event) { 
		if(mCatched) {
			mCatched = false; 
		} 
		return false;
	}

	public void run() {
		if(mCatched) {
			final long now = System.currentTimeMillis();
			if(now - mTouchDownTime > mHoldIime) { 
				trigger(null); 
				mCatched = false; 
			} 
		}
	} 
} 
