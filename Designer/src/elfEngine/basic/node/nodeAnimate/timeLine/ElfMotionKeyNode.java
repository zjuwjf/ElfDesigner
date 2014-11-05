package elfEngine.basic.node.nodeAnimate.timeLine;

import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ElfNode;

public class ElfMotionKeyNode extends ElfNode{ 
	
	public final static int MIN_UNIT = TimeData.MIN_UNIT;
	
	private int mTime;
	private InterType mInterType = InterType.Linear;
	private LoopMode mLoopMode = LoopMode.STAY;
	
	public ElfMotionKeyNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
	} 
	
	public void setTime(int time) { 
		mTime = Math.round( time/(float)MIN_UNIT ) * MIN_UNIT;
	} 
	public int getTime() { 
		return mTime; 
	} 
	protected final static int REF_Time = DEFAULT_SHIFT;
	
	public void setInterType(InterType type) {
		if(type != null) {
			mInterType = type;
		}
	}
	public InterType getInterType() {
		return mInterType;
	}
	protected final static int REF_InterType = DEFAULT_SHIFT; 
	
	public void setLoopMode(LoopMode loopMode) {
		if(loopMode != null) {
			mLoopMode = loopMode;
		}
	}
	public LoopMode getLoopMode() {
		return mLoopMode;
	}
	protected final static int REF_LoopMode = DEFAULT_SHIFT;
}
