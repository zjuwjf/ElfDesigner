package elfEngine.basic.node.nodeMap;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ClipNode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeList.IClipable;
import elfEngine.basic.node.nodeTouch.TouchNode;
import elfEngine.basic.touch.ScrollEventDecoder;
import elfEngine.basic.touch.ScrollEventDecoder.IOnScroll;
import elfEngine.graphics.PointF;

public class MapNode extends TouchNode implements IOnScroll, IClipable{
	
	private final ScrollEventDecoder mScrollEventDecoder = new ScrollEventDecoder();
	
	private MapContainerNode mMapContainerNode;
	public MapContainerNode getMapContainerNode(){
		return mMapContainerNode;
	}

	public MapNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setTouchEnable(true);
		
		setName("#map");
		
		setPriorityLevel(MAP_PRIORITY);
		setTouchDecoder(mScrollEventDecoder);
	}
	
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		
		mMapContainerNode = null;
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if(node instanceof MapContainerNode) { 
					mMapContainerNode = (MapContainerNode)node;
				} 
				return false; 
			} 
		}); 
		
		if(mMapContainerNode == null) {
			mMapContainerNode = new MapContainerNode(this, -1);
			mMapContainerNode.addToParent();
		} 
	}
	
	public void onCreateRequiredNodes() { 
		super.onCreateRequiredNodes(); 
		
		mMapContainerNode = new MapContainerNode(this, -1); 
		mMapContainerNode.addToParent();
	}
	
	protected void drawBefore() {
		ClipNode.pushScissor(this);
		super.drawBefore();
	} 
	
	protected void drawAfter() { 
		super.drawAfter(); 
		ClipNode.popScissor(); 
	} 
	
	public static class MapContainerNode extends ElfNode {
		public MapContainerNode(ElfNode father, int ordinal) {
			super(father, ordinal);
			
			setName("#container");
		} 
	} 
	
	private final ElfPointf mSpeed = new ElfPointf();
	public void onScroll(ElfNode node, float speedX, float speedY) { 
		mSpeed.setPoint(speedX, speedY);
	} 

	public ElfPointf onRestrict(ElfNode node) {
		final ElfPointf sizeOut = this.getSize();
		final ElfPointf sizeIn = this.mMapContainerNode.getSize();
		final ElfPointf pos = this.mMapContainerNode.getPosition();
		
		final float x = adjust(pos.x, sizeIn.x, sizeOut.x);
		final float y = adjust(pos.y, sizeIn.y, sizeOut.y);
		
		final ElfPointf ret = new ElfPointf(x==pos.x? 0:1, y==pos.y? 0:1);
		this.mMapContainerNode.setPosition(x, y);
		
		return ret;
	} 

	public void onStop(ElfNode node) {
		mSpeed.setPoint(0, 0);
	} 

	public ElfNode getMoveNode() { 
		return this.mMapContainerNode;
	}
	
	public ElfPointf onMove(float dx, float dy) {
		return new ElfPointf(dx, dy);
	} 
	
	private float mSlipRate = 0.95f;
	private float mSpeedRate = 1f;
	private float mReboundRate = 0f;
	
	public float getSlipRate() {
		return mSlipRate;
	}
	public void setSlipRate(float mSlipRate) {
		this.mSlipRate = mSlipRate;
	}
	protected final static int REF_SlipRate = DEFAULT_SHIFT;
	
	public float getSpeedRate() {
		return mSpeedRate;
	}
	public void setSpeedRate(float mSpeedRate) {
		this.mSpeedRate = mSpeedRate;
	}
	protected final static int REF_SpeedRate = DEFAULT_SHIFT;
	
	public float getReboundRate() {
		return mReboundRate;
	}
	public void setReboundRate(float mReboundRate) {
		this.mReboundRate = mReboundRate;
	}
	protected final static int REF_ReboundRate = DEFAULT_SHIFT;
	
	public void calc(float pMsElapsed) {
		super.calc(pMsElapsed);
		
		if(mSpeed.x !=0 || mSpeed.y != 0) {
			final ElfPointf p1 = this.mMapContainerNode.getPositionInScreen();
			
			final ElfPointf tmp = new ElfPointf();
			tmp.set(p1);
			final ElfPointf off = onMove(mSpeed.x * pMsElapsed * mSpeedRate, mSpeed.y * pMsElapsed * mSpeedRate);
			if(off != null) {
				tmp.translate(off.x, off.y);
			} else { 
				tmp.translate(mSpeed.x * pMsElapsed * mSpeedRate, mSpeed.y * pMsElapsed * mSpeedRate);
			}
			
			this.mMapContainerNode.setPositionInScreen(tmp); 
			
			final ElfPointf rebound = onRestrict(this.mMapContainerNode);
			
			if(rebound.x != 0) {
				mSpeed.x = -mSpeed.x * mReboundRate * mSlipRate;
			} else {
				mSpeed.x = mSpeed.x * mSlipRate;
			}
			
			if(rebound.y != 0) {
				mSpeed.y = -mSpeed.y * mReboundRate * mSlipRate;
			} else {
				mSpeed.y = mSpeed.y * mSlipRate;
			}
			
			if(Math.abs(mSpeed.x * 1000) < 1f) { 
				mSpeed.x = 0;
			} 
			
			if(Math.abs(mSpeed.y * 1000) < 1f) {  
				mSpeed.y = 0; 
			} 
		} 
	} 
	
	public static final float adjust(float valueIn, float lengthIn, float lengthOut) {
		if(lengthIn < lengthOut) { 
			if(valueIn + lengthIn/2 > lengthOut/2) { 
				return lengthOut/2 - lengthIn/2; 
			} else if(valueIn - lengthIn/2 < -lengthOut/2) { 
				return -lengthOut/2 + lengthIn/2; 
			} else { 
				return valueIn;
			} 
		} else { 
			if(valueIn - lengthIn/2 > -lengthOut/2) {
				return -lengthOut/2 + lengthIn/2;
			} else if(valueIn + lengthIn/2 < lengthOut/2) {
				return lengthOut/2 - lengthIn/2;
			} else {
				return valueIn;
			} 
		} 
	}

	private boolean mIsTouching = false;
	public void setIsInTouching(boolean touching) {
		mIsTouching = touching;
	}
	public boolean getIsTouching() {
		return mIsTouching;
	}
	
	public boolean isInClipSize(float x, float y) {
		return isInSize(new PointF(x, y));
	} 
} 


