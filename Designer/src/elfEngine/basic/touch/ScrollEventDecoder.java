package elfEngine.basic.touch;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;

public class ScrollEventDecoder extends BasicEventDecoder{
	
	public interface IOnScroll {
		
		//animate
		public void onScroll( ElfNode node, float speedX, float speedY);
		
		public ElfPointf onMove(float dx, float dy);
		//for after onMove
		public ElfPointf onRestrict( ElfNode node ); 
		
		public void onStop( ElfNode node );
		
		public ElfNode getMoveNode();
		public boolean isInSelectSize(ElfEvent event);
		
		public void setIsInTouching(boolean touching);
		public boolean getIsTouching();
	} 

	//need move, up
	protected IOnScroll mScroller;
	
	protected long mLastMoveTime = -1;
	
	public void setDelegate(ElfNode node) {
		super.setDelegate(node);
		
		assert(node instanceof IOnScroll);
		mScroller = (IOnScroll)node;
	}

	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) { 
		
		if(mScroller != null && mScroller.getMoveNode() != null) {
			if(mScroller.isInSelectSize(event)) {
				final ElfPointf pos = mScroller.getMoveNode().getPositionInScreen();
				final ElfPointf off = mScroller.onMove(moveX, moveY);
				
				if(off != null) {
					pos.translate(off.x, off.y);
				} else { 
					pos.translate(moveX, moveY);
				} 
				
				mScroller.getMoveNode().setPositionInScreen(pos);
				mScroller.onRestrict( mScroller.getMoveNode() ); 
				
				if(mLastMoveTime < 0) { 
					mLastMoveTime = System.currentTimeMillis();
				} //start
				
				mScroller.setIsInTouching(true);
				return true;
			} else if(mLastMoveTime > 0) {
				mLastMoveTime = -1;
				mScroller.setIsInTouching(true);
				return false; 
			} else {
				mScroller.setIsInTouching(true);
				return false;
			}
		} 
		
		return false; 
	} 

	protected boolean onTouchDwon(ElfEvent event) { 
		if(mScroller != null && mScroller.isInSelectSize(event)) {
			mScroller.onStop(mScroller.getMoveNode()); 
			
			mLastMoveTime = System.currentTimeMillis();
			mScroller.setIsInTouching(true);
			
			return true;
		} 
		return false; 
	} 
	
	protected boolean onTouchUp(ElfEvent event) { 
		if(mScroller != null && mLastMoveTime > 0) { 
			final float period = System.currentTimeMillis() - mLastMoveTime;
			if(period > 0) {
				mScroller.onScroll(mScroller.getMoveNode(), getTotalMoveX(event)/period,  getTotalMoveY(event)/period); 
			} else {
				mScroller.onScroll(mScroller.getMoveNode(), 0,  0); 
			} 
			mLastMoveTime = -1;
		} 
		
		return false; 
	} 
	
	public void reset() { 
		super.reset(); 
		mLastMoveTime = -1; 
		mScroller.setIsInTouching(false); 
	} 
} 
