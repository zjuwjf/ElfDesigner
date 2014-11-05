package elfEngine.basic.node.nodeTouch;

import org.cocos2d.actions.ease.EaseBackIn;
import org.cocos2d.actions.ease.EaseBackOut;
import org.cocos2d.actions.interval.ScaleTo;
import org.cocos2d.actions.interval.Sequence;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.BasicEventDecoder.TriggerListener;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.HoldEventDecoder;

public class HoldNode extends TouchNode {
	private final HoldEventDecoder mHoldEventDecoder = new HoldEventDecoder();

	public HoldNode(ElfNode father, int ordinal) {
		super(father, ordinal); 
		setName("#hold");
		
		this.setTouchEnable(true);
		setPriorityLevel(HOLD_PRIORITY);
		
		initDelegate();
	} 
	
	void initDelegate() {
		mHoldEventDecoder.setDelegate(this);
		this.addListener(new TriggerListener() {
			public void trigger(ElfNode node, ElfEvent event) {
				System.err.println("default hold trigger");
				HoldNode.this.stopActions();
				HoldNode.this.runAction(Sequence.actions(EaseBackIn.action(ScaleTo.action(0.5f, 2)), EaseBackOut.action(ScaleTo.action(1, 1))));
			}
		});
		
		setTouchDecoder(mHoldEventDecoder);
	}
	
	public void addListener(final TriggerListener listener) {
		mHoldEventDecoder.addListener(listener);
	}
	
	public void calc(float t) {
		super.calc(t);
		mHoldEventDecoder.run();
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
	
} 
