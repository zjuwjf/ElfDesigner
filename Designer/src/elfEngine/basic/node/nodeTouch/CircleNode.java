package elfEngine.basic.node.nodeTouch;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.RotateEventDecoder;

public class CircleNode extends TouchNode{
	private final RotateEventDecoder mRotateEventDecoder = new RotateEventDecoder();
	
	public CircleNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setName("#circle");
		
		setTouchEnable(true);
		setTouchDecoder(mRotateEventDecoder);
		
		setPriorityLevel(ROTATE_PRIORITY);
	} 
	
	public float getAnimateTime() {
		return mRotateEventDecoder.getAnimateTime();
	}
	public void setAnimateTime(float mAnimateTime) {
		mRotateEventDecoder.setAnimateTime(mAnimateTime);
	}
	protected final static int REF_AnimateTime = DEFAULT_SHIFT;
	
	public float getMinRotate() {
		return mRotateEventDecoder.getMinRotate();
	}
	public void setMinRotate(float mMinRotate) {
		mRotateEventDecoder.setMinRotate(mMinRotate);
	}
	protected final static int REF_MinRotate = DEFAULT_SHIFT;

	public float getMaxRotate() {
		return mRotateEventDecoder.getMaxRotate();
	}
	public void setMaxRotate(float mMaxRotate) {
		mRotateEventDecoder.setMaxRotate(mMaxRotate);
	}
	protected final static int REF_MaxRotate = DEFAULT_SHIFT;

	public float[] getStayRotates() {
		return mRotateEventDecoder.getStayRotates();
	}
	public void setStayRotates(float[] stayRotates) {
		mRotateEventDecoder.setStayRotates(stayRotates);
	}
	protected final static int REF_StayRotates = DEFAULT_SHIFT;
	
	public void setStayIndex(int index) {
		mRotateEventDecoder.setStayIndex(index);
	} 
	public int getStayIndex() {
		return mRotateEventDecoder.getStayIndex();
	} 
	protected final static int REF_StayIndex = DEFAULT_SHIFT;
} 
