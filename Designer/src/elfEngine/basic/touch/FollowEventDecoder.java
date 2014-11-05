package elfEngine.basic.touch;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.FollowType;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.modifier.EmptyModifier;
import elfEngine.basic.modifier.FollowModifier;
import elfEngine.basic.modifier.INodeModifier.ModifierListener;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.SwipTouchDecoder.IOnStayIndexChangeListener;
import elfEngine.basic.touch.SwipTouchDecoder.IOnTouchUp;

public class FollowEventDecoder extends BasicEventDecoder{
	private ElfNode [] mStayNodes;
	private ElfPointf [] mStayRanges = new ElfPointf[0];
	private ElfNode mFinger;
	private FollowType mFollowType = FollowType.FIRST;
	
	private ElfPointf mOffset = new ElfPointf();
	
	public FollowType getFollowType() {
		return mFollowType;
	}
	public void setFollowType(FollowType mFollowType) {
		this.mFollowType = mFollowType;
	} 
	
	public ElfPointf[] getStayRanges() {
		return mStayRanges;
	}
	public void setStayRanges(ElfPointf[] mStayRanges) { 
		if(mStayRanges != null) { 
			this.mStayRanges = mStayRanges; 
		} 
	} 
	
	public ElfNode[] getStayNodes() {
		return mStayNodes;
	}
	public void setStayNodes(ElfNode[] mStayNodes) {
		this.mStayNodes = mStayNodes; 
		setCurrentStayIndex(0);
	}
	
	public void setDelegate(ElfNode node) {
		super.setDelegate(node);
		mFinger = node;
	}
	
	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		if(! mCatched ) { 
			return false; 
		} 
		
		mFinger.setPositionInScreen(new ElfPointf(event.x-mOffset.x, event.y-mOffset.y));
		return true;
	} 
	
	protected boolean onTouchUp(ElfEvent event) { 
		if(! mCatched) { 
			return false; 
		} 
		
		if(mStayNodes == null || mStayNodes.length == 0) {
			return false;
		}
		
//		mFinger.clearModifiers(); 
		int findIndex = 0;
		
		if(mFollowType == FollowType.CLOSEST) { 
			//find close 
			float min = Float.MAX_VALUE;
			for(int i=0; i<mStayNodes.length; i++) { 
				final ElfNode stay = mStayNodes[i]; 
				if(stay != null) { 
					final ElfPointf pos = new ElfPointf(); 
					stay.screenXYToChild(event.x, event.y, pos); 
					final float dis = (pos.x)*(pos.x) + (pos.y)*(pos.y); 
					if(dis < min) { 
						min = dis; 
						findIndex = i; 
					} 
				} 
			} 
		} else { 
			//prefer origin
			for(int i=0; i<mStayNodes.length; i++) { 
				final ElfNode stay = mStayNodes[i]; 
				if(stay != null) { 
					final ElfPointf pos = new ElfPointf(); 
					stay.screenXYToChild(event.x, event.y, pos); 
					
					final ElfPointf range;
					if(mStayRanges == null || mStayRanges.length <= i) {
						range = new ElfPointf( stay.getWidth(), stay.getHeight() );
					} else { 
						range = mStayRanges[i]; 
					} 
					
					if(Math.abs(pos.x) < range.x/2 && Math.abs(pos.y) < range.y/2) {
						findIndex = i; 
						break; 
					} 
				} 
			} 
		} 
		
		final ElfNode targetParent = mStayNodes[findIndex];
		final FollowModifier follow = new FollowModifier(targetParent, mAnimateTime, -1, mInterType);
		final EmptyModifier empty = new EmptyModifier(0, 1, mAnimateTime, mAnimateTime, LoopMode.STAY, null);
		final int myIndex = findIndex;
		empty.setModifierListener(new ModifierListener() {
			public void onFinished(ElfNode node) { 
				if(mOnStayIndexChange != null) { 
					mOnStayIndexChange.onChangeFinished(mLastIndex, myIndex);
				} 
				mLastIndex = myIndex;
			} 
		}); 
		
		if(mOnTouchUp != null) { 
			mOnTouchUp.onChangeStart(mLastIndex, findIndex); 
		} 
		
		mFinger.setUseModifier(true); 
		
		mFinger.addModifier(follow); 
		mFinger.addModifier(empty); 
		
		return false; 
	} 
	
	public void reset() {
		super.reset();
		this.mCatched = false;
	}
	
	protected boolean onTouchDwon(ElfEvent event) { 
		if(mFinger.isInSelectSize(event)) { 
			setCatched(true);
			final ElfPointf pos = mFinger.getPositionInScreen();
			mOffset.setPoint(event.x-pos.x, event.y-pos.y);
			return true;
		} else {
			setCatched(false);
		}
		
		return false; 
	} 
	
	private IOnStayIndexChangeListener mOnStayIndexChange;
	private IOnTouchUp mOnTouchUp;
	private int mLastIndex = 0;
	private float mAnimateTime = 500;
	private InterType mInterType = InterType.Viscous;
	private boolean mCatched = false; 
	
	public int getCurrentStayIndex() {
		return mLastIndex;
	} 
	public void setCurrentStayIndex(int index) {
		if(mStayNodes != null && mStayNodes.length > 0) {
			index = Math.max(index, 0);	
			index = Math.min(index, mStayNodes.length-1);	
			
			mLastIndex = index;
			mDelegateNode.clearModifiers();
			mDelegateNode.setUseModifier(true);
			final FollowModifier follow = new FollowModifier(mStayNodes[mLastIndex], mAnimateTime, -1, mInterType);
			mDelegateNode.addModifier(follow);
			reset();
		} 
	}
	
	public void setCatched(final boolean select) {
		mCatched = select;
		if(mCatched) {
			mFinger.clearModifiers(); 
		}
	} 
	public boolean getCatched() { 
		return mCatched; 
	} 
	
	public float getAnimateTime() {
		return mAnimateTime; 
	}
	public void setAnimateTime(float mAnimateTime) {
		this.mAnimateTime = mAnimateTime;
	} 
	public InterType getInterType() { 
		return mInterType;
	} 
	public void setInterType(InterType mInterType) { 
		this.mInterType = mInterType; 
	} 
	
	public void setOnStayIndexChangeListener(final IOnStayIndexChangeListener listener) { 
		mOnStayIndexChange = listener; 
	} 
	public void setOnTouchUp(IOnTouchUp onTouchUp){ 
		mOnTouchUp = onTouchUp; 
	} 
} 
