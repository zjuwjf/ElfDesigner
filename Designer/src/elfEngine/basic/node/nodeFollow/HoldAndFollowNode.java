package elfEngine.basic.node.nodeFollow;

import org.cocos2d.actions.ease.EaseBackIn;
import org.cocos2d.actions.ease.EaseBackOut;
import org.cocos2d.actions.interval.ScaleTo;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.BasicEventDecoder.TriggerListener;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.HoldEventDecoder;
import elfEngine.basic.touch.SwipTouchDecoder.IOnTouchUp;

public class HoldAndFollowNode extends FollowNode { 
	
	protected boolean mIsInHoldState = true; 
	protected final HoldEventDecoder mHoldEventDecoder = new HoldEventDecoder();
	
	void initDelegate() {
		mHoldEventDecoder.setDelegate(this);
		setTouchDecoder(mHoldEventDecoder);
		
		mHoldEventDecoder.addListener(new TriggerListener() {
			public void trigger(ElfNode node, ElfEvent event) {
				HoldAndFollowNode.this.stopActions();
				HoldAndFollowNode.this.clearModifiers();
				HoldAndFollowNode.this.runAction(EaseBackIn.action(ScaleTo.action(mScaleAnimateTime/1000, mHoldScale)));
			}
		});
		
		mHoldEventDecoder.addListener(new TriggerListener() {
			public void trigger(ElfNode node, ElfEvent event) {
				mIsInHoldState = false;
				HoldAndFollowNode.this.setCatched(true);
			}
		});
		
		mFollowEventDecoder.setOnTouchUp(new IOnTouchUp() {
			public void onChangeStart(int lastIndex, int newIndex) { 
				mIsInHoldState = true;
				HoldAndFollowNode.this.runAction(EaseBackOut.action(ScaleTo.action(getAnimateTime()/1000, 1)));
			}
		});
	}
	
	private float mHoldScale = 2;
	public void setHoldScale(float scale) {
		mHoldScale = scale;
	}
	public float getHoldScale() {
		return mHoldScale;
	}
	protected final static int REF_HoldScale = DEFAULT_SHIFT;
	
	public HoldAndFollowNode(ElfNode father, int ordinal) { 
		super(father, ordinal);
		
		setName("#holdAndFollow");
		initDelegate();
	} 

	public void calc(float t) {
		super.calc(t);
		if(mIsInHoldState) {
			mHoldEventDecoder.run();
			setTouchDecoder(mHoldEventDecoder);
		} else {
			setTouchDecoder(mFollowEventDecoder);
		}
	} 
	
	public void setMaxMove(float move) {
		mHoldEventDecoder.setMaxMove(move);
	}
	public float getMaxMove() {
		return mHoldEventDecoder.getMaxMove();
	}
	protected final static int REF_MaxMove = DEFAULT_SHIFT;
	
	public void setHoldTime(int time) { 
		mHoldEventDecoder.setHoldTime(time);
	} 
	public int getHoldTime() { 
		return mHoldEventDecoder.getHoldTime(); 
	} 
	protected final static int REF_HoldTime = DEFAULT_SHIFT;
	
	private float mScaleAnimateTime = 500;
	public float getScaleAnimateTime() {
		return mScaleAnimateTime;
	}
	public void setScaleAnimateTime(float scaleAnimateTime) {
		mScaleAnimateTime = scaleAnimateTime;
	}
	protected final static int REF_ScaleAnimateTime = DEFAULT_SHIFT;
	
}
