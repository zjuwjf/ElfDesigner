package elfEngine.basic.touch;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.hf.HFST.IOnState;
import elfEngine.basic.node.hf.HFST.OnStateType;

public class HFDecoder extends HoldEventDecoder{
	//IOnState
	protected IOnState mIOnState;
	
	private boolean mIsHoldTriggered = false;
	
	public void setDelegate(ElfNode node) {
		super.setDelegate(node); 
		assert(node instanceof IOnState);
		mIOnState = (IOnState)node; 
	} 
	
	@Override
	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		if(mIsHoldTriggered) { 
			mDelegateNode.setPositionInScreen(new ElfPointf(event.x, event.y)); 
			mIOnState.onState(OnStateType.OnOver);  
			return true; 
		} else { 
			return super.onTouchMove(event, moveX, moveY);
		} 
	} 
	
	@Override
	protected boolean onTouchUp(ElfEvent event) { 
		if(mIsHoldTriggered) { 
			mIOnState.onState(OnStateType.OnGoing);
			mIsHoldTriggered = false;
			return false;
		} else { 
			return super.onTouchUp(event);
		} 
	} 
	
	public void trigger(ElfEvent event) { 
		mIsHoldTriggered = true; 
		mIOnState.onState(OnStateType.OnStart); 
		super.trigger(event);
	} 
	
	public void reset() {
		mIsHoldTriggered = false;
		super.reset();
	} 
} 
