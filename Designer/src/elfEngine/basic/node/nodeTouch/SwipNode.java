package elfEngine.basic.node.nodeTouch;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.SwipTouchDecoder;

public class SwipNode extends TouchNode{
	
	public enum RestrictXYType {
		RestrictX, RestrictY, NoRestrict
	}
	
	private final SwipTouchDecoder mSwipTouchDecoder = new SwipTouchDecoder();
	private RestrictXYType mRestrictXYType = RestrictXYType.NoRestrict;
	
	public void setRestrictXYType(RestrictXYType type) {
		if(type != null && type != mRestrictXYType) {
			mRestrictXYType = type;
			switch (mRestrictXYType) {
			case RestrictX:
				mSwipTouchDecoder.setRestrictXY(SwipTouchDecoder.RestrictX);
				break;
			case RestrictY:
				mSwipTouchDecoder.setRestrictXY(SwipTouchDecoder.RestrictY);
				break;
			case NoRestrict:
				mSwipTouchDecoder.setRestrictXY(SwipTouchDecoder.NoRestrict);
				break;
			} 
		}
	}
	public RestrictXYType getRestrictXYType() {
		return mRestrictXYType;
	}
	protected final static int REF_RestrictXYType = DEFAULT_SHIFT;
	
	//
	public void setMinPoint(final ElfPointf min) {
		mSwipTouchDecoder.setMinPoint(min);
	}
	public ElfPointf getMinPoint() {
		return mSwipTouchDecoder.getMinPoint();
	}
	protected final static int REF_MinPoint = DEFAULT_SHIFT;
	
	//
	public ElfPointf getMaxPoint() { 
		return mSwipTouchDecoder.getMaxPoint();
	} 
	public void setMaxPoint(final ElfPointf max) { 
		mSwipTouchDecoder.setMaxPoint(max); 
	} 
	protected final static int REF_MaxPoint = DEFAULT_SHIFT; 
	
	//
	public void setStayPoints(final ElfPointf [] stays) {
		mSwipTouchDecoder.setStayPoints(stays);
	} 
	public ElfPointf [] getStayPoints() {
		return mSwipTouchDecoder.getStayPoints();
	}
	protected final static int REF_StayPoints = DEFAULT_SHIFT;
	
	//
	public void setAnimateTime(float time) {
		mSwipTouchDecoder.setAnimateTime(time);
	}
	public float getAnimateTime() {
		return mSwipTouchDecoder.getAnimateTime();
	} 
	protected final static int REF_AnimateTime = DEFAULT_SHIFT;
	
	//
	public int getCurrentIndex(){
		return mSwipTouchDecoder.getCurrentIndex();
	} 
	public void setCurrentIndex(int index) {
		mSwipTouchDecoder.setCurrentIndex(index);
	}
	protected final static int REF_CurrentIndex = DEFAULT_SHIFT;
	
	
	public SwipNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
		
		setName("#swip"); 
		setTouchDecoder(mSwipTouchDecoder); 
		
		setTouchEnable(true); 
		
		setPriorityLevel(SWIP_PRIORITY); 
	} 
	
	public float getTouchSpeedOffsetRate() {
		return mSwipTouchDecoder.getSpeedRate();
	}
	public void setTouchSpeedOffsetRate(float mSpeedRate) {
		mSwipTouchDecoder.setSpeedRate(mSpeedRate);
	}
	protected final static int REF_TouchSpeedOffsetRate = DEFAULT_SHIFT;

	public float getTouchMoveOffsetRate() { 
		return mSwipTouchDecoder.getTouchMoveRate(); 
	} 
	public void setTouchMoveOffsetRate(float mTouchMoveRate) { 
		mSwipTouchDecoder.setTouchMoveRate(mTouchMoveRate); 
	} 
	protected final static int REF_TouchMoveOffsetRate = DEFAULT_SHIFT; 
	
	public void setMaxTouchOffset(float speed) { 
		mSwipTouchDecoder.setMaxSpeed(speed); 
	} 
	public float getMaxTouchOffset() { 
		return mSwipTouchDecoder.getMaxOffset(); 
	} 
	protected final static int REF_MaxTouchOffset = DEFAULT_SHIFT; 
	
	public void setInterType(final InterType inter ){ 
		mSwipTouchDecoder.setInterType(inter); 
	} 
	public InterType getInterType() { 
		return mSwipTouchDecoder.getInterType(); 
	} 
	protected final static int REF_InterType = DEFAULT_SHIFT; 
} 
