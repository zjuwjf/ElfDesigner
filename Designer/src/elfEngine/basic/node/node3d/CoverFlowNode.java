package elfEngine.basic.node.node3d;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.enumTypes.Orientation;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.GestureEventDecoder;
import elfEngine.basic.touch.GestureEventDecoder.IOnFling;

public class CoverFlowNode extends Elf3DNode implements IOnFling{
	
	public CoverFlowNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setTouchEnable(true);
	} 
	
	private final GestureEventDecoder mGestureEventDecoder = new GestureEventDecoder(this, this);
	
	public boolean onTouch(ElfEvent event) {
		return mGestureEventDecoder.onTouch(event);
	}
	
	private int mPriorityLevel = 10;
	public int getPriorityLevel() { 
		return mPriorityLevel; 
	} 
	public void setPriorityLevel(int mPriorityLevel) {
//		if (mPriorityLevel > 0) {
			this.mPriorityLevel = mPriorityLevel;
//		}
	}
	protected final static int REF_PriorityLevel = DEFAULT_SHIFT;
	
	private InterType mInterType = InterType.Viscous;
	public void setInterType(InterType type) {
		mInterType = type;
	}
	public InterType getInterType() {
		return mInterType;
	}
	protected final static int REF_InterType = DEFAULT_SHIFT;
	
	private InterType mBackInterType = InterType.AcceDece;
	public void setBackInterType(InterType type) {
		mBackInterType = type;
	}
	public InterType getBackInterType() {
		return mBackInterType;
	}
	protected final static int REF_BackInterType = DEFAULT_SHIFT;
	
	public void calc(float pMsElapsed) { 
		super.calc(pMsElapsed); 
		if(mIsAnimate) { 
			mAnimateProgress += pMsElapsed*0.001f; 
			
			final ArrayList<Elf3DNode> list = new ArrayList<Elf3DNode>();
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) { 
					if(node instanceof Elf3DNode) { 
						list.add((Elf3DNode)node); 
					} 
					return false; 
				} 
			});
			
			final int size = list.size();
			
			//check is on the right way
			final int nextNodeIndex = mCurrentNodeIndex+mAddIndex;
			final float rate = Math.min(mAnimateProgress / mAnimateTime, 1.0f);
			final boolean error = (nextNodeIndex < 0 || nextNodeIndex >= size);
			
			final float inter; 
			if(error) { 
				if(rate >= 1.0f) { 
					inter = 0; 
				} else { 
					inter = mBackInterType.getInterpolation( 0.5f-Math.abs(rate-0.5f) );
				} 
			} else { 
				if(rate >= 1.0f) { 
					inter = 1.0f; 
				} else { 
					inter = mInterType.getInterpolation( rate );
				}
			}
			
			for(int i=0; i<size; i++) { 
				final Elf3DNode node = list.get(i); 
				setState(node, i-mCurrentNodeIndex, i-mCurrentNodeIndex-mAddIndex, inter);
			} 
			
			if(rate  >= 1.0f) {
				if(error) { 
					mAddIndex = 0;
					mIsAnimate = false;
				} else {
					stopAnimate();
				}
			} 
		} 
	} 
	
	public void drawSprite() {
		super.drawSprite();
		
		if(mAlphaMirror > 0) { 
			GL11.glPushMatrix();
			GL11.glTranslatef(0, mMirrorTransY, 0);
			GL11.glScalef(1, -1, 1);
			final float alpha = getAlpha();
			setAlpha(alpha*mAlphaMirror);
			super.drawSprite();
			setAlpha(alpha);
			GL11.glPopMatrix();
		} 
	} 
	
	boolean mIsAnimate = false; 
	float mAnimateTime = 1; 
	float mAnimateProgress = 0; 
	int mCurrentNodeIndex; 
	int mAddIndex = 0; 
	float mMirrorTransY = 0;
	float mAlphaMirror = 0.4f; 
	
	private final FOS mFOSX = new FOS(0,-200,200,-150,150);
	private final FOS mFOSY = new FOS(0,0,0,0,0);
	private final FOS mFOSZ = new FOS(10,-100,-100,-100,-100); 
	private final FOS mFOSRotateY = new FOS(0,-60,60,-10,10); 
	private final FOS mFOSRotateX = new FOS(0,0,0,0,0); 
	private final FOS mFOSAlpha = new FOS(1,0.8f,0.8f,-0.15f,-0.15f); 
	private final FOS mFOSScale = new FOS(1.3f,1,1,0,0); 
	
	public float getMirrorTransY() {
		return mMirrorTransY;
	}
	public void setMirrorTransY(float mAnimateTime) {
		this.mMirrorTransY = mAnimateTime;
	}
	public float getAnimateTime() {
		return mAnimateTime;
	}
	public void setAnimateTime(float mAnimateTime) {
		this.mAnimateTime = mAnimateTime;
	}
	public float getAnimateProgress() {
		return mAnimateProgress;
	}
	public void setAnimateProgress(float mAnimateProgress) {
		this.mAnimateProgress = mAnimateProgress;
	}
	public int getCurrentNodeIndex() {
		return mCurrentNodeIndex;
	}
	public void setCurrentNodeIndex(int mCurrentNodeIndex) {
		this.mCurrentNodeIndex = mCurrentNodeIndex;
	}
	public float getAlphaMirror() {
		return mAlphaMirror;
	}
	public void setAlphaMirror(float mAlphaMirror) {
		this.mAlphaMirror = mAlphaMirror;
	}
	public FOS getFOSX() {
		return new FOS(mFOSX);
	}
	public FOS getFOSY() {
		return new FOS(mFOSY);
	}
	public FOS getFOSZ() {
		return new FOS(mFOSZ);
	}
	public FOS getFOSRotateX() {
		return new FOS(mFOSRotateX);
	}
	public FOS getFOSRotateY() {
		return new FOS(mFOSRotateY);
	}
	public FOS getFOSAlpha() {
		return new FOS(mFOSAlpha);
	}
	public FOS getFOSScale() {
		return new FOS(mFOSScale);
	}
	public void setFOSX(FOS fos) {
		mFOSX.set(fos);
	}
	public void setFOSY(FOS fos) {
		mFOSY.set(fos);
	}
	public void setFOSZ(FOS fos) {
		mFOSZ.set(fos);
	}
	public void setFOSRotateX(FOS fos) {
		mFOSRotateX.set(fos);
	}
	public void setFOSRotateY(FOS fos) {
		mFOSRotateY.set(fos);
	}
	public void setFOSAlpha(FOS fos) {
		mFOSAlpha.set(fos);
	}
	public void setFOSScale(FOS fos) {
		mFOSScale.set(fos);
	}
	protected final static int REF_AnimateTime = DEFAULT_SHIFT;
//	protected final static int REF_AnimateProgress = DEFAULT_SHIFT;
	protected final static int REF_CurrentNodeIndex = DEFAULT_SHIFT;
	protected final static int REF_AlphaMirror = DEFAULT_SHIFT;
	protected final static int REF_FOSX = DEFAULT_SHIFT;
	protected final static int REF_FOSY = DEFAULT_SHIFT;
	protected final static int REF_FOSZ = DEFAULT_SHIFT;
	protected final static int REF_FOSRotateX = DEFAULT_SHIFT;
	protected final static int REF_FOSRotateY = DEFAULT_SHIFT;
	protected final static int REF_FOSAlpha = DEFAULT_SHIFT;
	protected final static int REF_FOSScale = DEFAULT_SHIFT;
	protected final static int REF_MirrorTransY = DEFAULT_SHIFT;
	
	final void stopAnimate() {
		mCurrentNodeIndex = mCurrentNodeIndex+mAddIndex;
		mAddIndex = 0;
		mIsAnimate = false;
	}
	
	public void setPlusClick() { 
		stopAnimate(); 
		mAddIndex = 1; 
		mAnimateProgress=0; 
		mIsAnimate = true; 
	} 
	
	public void setSubClick() { 
		stopAnimate();
		mAddIndex = -1;
		mAnimateProgress=0;
		mIsAnimate = true; 
	} 
	
	public void setReset() {
		mIsAnimate = false; 
		final ArrayList<Elf3DNode> list = new ArrayList<Elf3DNode>();
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if(node instanceof Elf3DNode) { 
					list.add((Elf3DNode)node); 
				} 
				return false; 
			} 
		});
		
		final int size = list.size();
		final int mCurrentNodeIndex = size/2;
		for(int i=0; i<size; i++) { 
			final Elf3DNode node = list.get(i);
			setState(node, i-mCurrentNodeIndex, i-mCurrentNodeIndex, 0);
		} 
	} 
	
	protected final static int REF_PlusClick = FACE_SET_SHIFT;
	protected final static int REF_SubClick = FACE_SET_SHIFT;
//	protected final static int REF_Reset = FACE_SET_SHIFT;
	
	void setState(Elf3DNode node, int index, int nextIndex, float rate) { 
		node.setPosition(getValueByRate(mFOSX,index,nextIndex,rate), getValueByRate(mFOSY,index,nextIndex,rate));
		node.setZ(getValueByRate(mFOSZ,index,nextIndex,rate));
		
		node.setAlpha(getValueByRate(mFOSAlpha,index,nextIndex,rate));
		node.setRotateX(getValueByRate(mFOSRotateX,index,nextIndex,rate));
		node.setRotateY(getValueByRate(mFOSRotateY,index,nextIndex,rate));
		node.setScale(getValueByRate(mFOSScale,index,nextIndex,rate), getValueByRate(mFOSScale,index,nextIndex,rate));
	} 
	
	static float getValueByIndex(FOS fos, int index) {
		if(index < 0) { 
			final int off = -1-index; 
			return fos.origin1 + off*fos.offset1; 
		} else if(index > 0) { 
			final int off = index - 1; 
			return fos.origin2 + off*fos.offset2;
		} else { 
			return fos.front; 
		} 
	} 
	
	static float getValueByRate(FOS fos, int index0, int index1, float rate) {
		float value0 = getValueByIndex(fos, index0);
		float value1 = getValueByIndex(fos, index1);
		return value0*(1-rate) + value1*rate;
	}

	private Orientation mOrientation = Orientation.Horizontal;
	public Orientation getOrientation() {
		return mOrientation;
	} 
	public void setOrientation(Orientation mOrientation) {
		this.mOrientation = mOrientation;
	}
	protected final static int REF_Orientation = DEFAULT_SHIFT;
	
	private float mThresholdDistance = 120;
	private float mThresholdVelocity = 200;
	
	public enum ThresholdOp {
		Or, And;
		public boolean getValue(boolean v0, boolean v1) {
			if(this == Or) {
				return v0 || v1;
			} else {
				return v0 && v1;
			}
		}
	}
	private ThresholdOp mThresholdOp = ThresholdOp.Or;
	
	public float getThresholdDistance() {
		return mThresholdDistance;
	}
	public void setThresholdDistance(float mThresholdDistance) {
		this.mThresholdDistance = mThresholdDistance;
	}
	public float getThresholdVelocity() {
		return mThresholdVelocity;
	}
	public void setThresholdVelocity(float mThresholdVelocity) {
		this.mThresholdVelocity = mThresholdVelocity;
	}
	public ThresholdOp getThresholdOp() {
		return mThresholdOp;
	}
	public void setThresholdOp(ThresholdOp mThresholdOp) {
		this.mThresholdOp = mThresholdOp;
	}
	protected final static int REF_ThresholdDistance = DEFAULT_SHIFT;
	protected final static int REF_ThresholdVelocity = DEFAULT_SHIFT;
	protected final static int REF_ThresholdOp = DEFAULT_SHIFT;

	public void onFling(float distanceX, float distanceY, float velocityX, float velocityY) {
		switch (mOrientation) {
		case Horizontal_R2L:
		case Horizontal:
			if(mThresholdOp.getValue(distanceX > mThresholdDistance, velocityX > mThresholdVelocity)) {
				setSubClick();
			} else if(mThresholdOp.getValue(distanceX < -mThresholdDistance, velocityX < -mThresholdVelocity)) {
				setPlusClick();
			} 
			break;
		case Vertical_B2T:
		case Vertical:
			if(mThresholdOp.getValue(distanceY > mThresholdDistance, velocityY > mThresholdVelocity)) {
				setPlusClick();
			} else if(mThresholdOp.getValue(distanceY < -mThresholdDistance, velocityY < -mThresholdVelocity)) {
				setSubClick();
			}
			break;
		} 
		System.err.printf("fling %f, %f, %f, %f \n", distanceX, distanceY, velocityX, velocityY);
	} 
	
	public static class FOS extends ElfDataDisplay{ 
		public float front; 
		public float origin1; 
		public float origin2; 
		public float offset1; 
		public float offset2;
		
		public FOS(float f, float o, float o2, float s, float s2) {
			front = f;
			origin1 = o;
			origin2 = o2;
			offset1 = s;
			offset2 = s2;
		}
		
		public FOS() {
			this(0,0,0,0,0);
		} 
		
		public FOS(FOS fos) {
			front = fos.front;
			origin1 = fos.origin1;
			origin2 = fos.origin2;
			offset1 = fos.offset1;
			offset2 = fos.offset2;
		}
		
		public void set(FOS fos) {
			front = fos.front;
			origin1 = fos.origin1;
			origin2 = fos.origin2;
			offset1 = fos.offset1;
			offset2 = fos.offset2;
		}
	}
	
	public static class FOSUnion extends ElfDataDisplay { 
		public FOS x = new FOS(0,-200,200,-150,150); 
		public FOS y = new FOS(0,0,0,0,0); 
		public FOS z = new FOS(10,-100,-100,-100,-100); 
		public FOS alpha = new FOS(1,0.8f,0.8f,-0.15f,-0.15f); 
		public FOS scale = new FOS(1.3f,1,1,0,0); 
		public FOS rotateX = new FOS(0,0,0,0,0); 
		public FOS rotateY = new FOS(0,-60,60,-10,10); 
	} 
} 
