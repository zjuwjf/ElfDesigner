package elfEngine.basic.node.nodeAdvanced;

import elfEngine.basic.modifier.BasicNodeModifier;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.utils.ElfRandom;

public class ShakeNode extends ElfNode{
	
	public ShakeNode(ElfNode father, int ordianl) { 
		super(father, ordianl); 
	} 
	
	protected float mShakeMaxOff;
	protected float mShakeDuration;
	protected float mFrequency;
	public float getFrequency() {
		return mFrequency;
	}
	public void setFrequency(float frequency) {
		this.mFrequency = frequency;
	}
	protected final static int REF_Frequency = DEFAULT_SHIFT;
	
	public float getShakeMaxOff() {
		return mShakeMaxOff;
	}
	public void setShakeMaxOff(float mShakeMaxOff) {
		this.mShakeMaxOff = mShakeMaxOff;
	}
	protected final static int REF_ShakeMaxOff = DEFAULT_SHIFT;
	
	public float getShakeDuration() {
		return mShakeDuration;
	}
	public void setShakeDuration(float mShakeDuration) {
		this.mShakeDuration = mShakeDuration;
	}
	protected final static int REF_ShakeDuration = DEFAULT_SHIFT;
	
	private ShakeModifier mShakeModifier;
	public void setShakeModifier() {
		if(mShakeModifier != null) {
			mShakeModifier.onModifierFinished(this);
		} 
		this.setUseModifier(true);
		mShakeModifier = new ShakeModifier(mShakeMaxOff, mFrequency, mShakeDuration);
		this.addModifier(mShakeModifier);
	}
	protected final static int REF_ShakeModifier = FACE_SET_SHIFT; 
} 

class ShakeModifier extends BasicNodeModifier {
	protected float mOffsetX=0, mOffsetY=0;
	protected final float mMaxOff;
	protected final float mFrequency; 
	protected float mAccum; 
	
	public ShakeModifier(float maxOff, float frequency, float life) { 
		super(0, 1, life, life, null, null);
		mMaxOff = maxOff;
		mFrequency = frequency;
	} 

	public void count(float pMsElapsed) {
		super.count(pMsElapsed);
		mAccum += pMsElapsed;
	}
	
	public void modifier(ElfNode node) {
		if(mAccum > mFrequency) { 
			node.translate(-mOffsetX, -mOffsetY); 
			mOffsetX = (ElfRandom.nextFloat()-0.5f)*mMaxOff; 
			mOffsetY = (ElfRandom.nextFloat()-0.5f)*mMaxOff; 
			node.translate(mOffsetX, mOffsetY); 
			while(mAccum >= mFrequency) mAccum -= mFrequency; 
		} 
	} 
	
	public void onModifierFinished(ElfNode node){ 
		super.onModifierFinished(node); 
		node.translate(-mOffsetX, -mOffsetY); 
		mOffsetX = mOffsetY = 0; 
		mAccum = 0; 
	} 
} 
