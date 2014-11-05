package elfEngine.basic.node.particle.modifier;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.counter.BasicCounter;
import elfEngine.basic.modifier.INodeModifier;
import elfEngine.basic.node.ElfNode;

/**
 * @author zju_wjf
 *
 */
public class BasicParticleModifier extends BasicCounter implements INodeModifier{
	
	private float  mLifeReciprocal;
	private ModifierListener mModifierListener;
	/**
	 * color
	 */
	private float mAlphaBeg, mAlphaSpan;
	private float mRedBeg, mRedSpan;
	private float mGreenBeg, mGreenSpan;
	private float mBlueBeg, mBlueSpan;
	
	/**
	 * scale
	 */
	private float mScaleBeg, mScaleSpan;
	
	/**
	 * rotate
	 */
	private float mRotateBeg, mRotateSpan;
	
	/**
	 * @param redBeg
	 * @param redEnd
	 * @param greenBeg
	 * @param greenEnd
	 * @param blueBeg
	 * @param blueEnd
	 * @param alphaBeg
	 * @param alphaEnd
	 * @param scaleBeg
	 * @param scaleEnd
	 * @param life
	 */
	public BasicParticleModifier(float redBeg,float redEnd, float greenBeg, float greenEnd, float blueBeg, float blueEnd,
			float alphaBeg, float alphaEnd, float scaleBeg, float scaleEnd, float rotateBeg, float rotateSpan, float life){
		mRedBeg = redBeg;
		mRedSpan = redEnd-redBeg;
		mGreenBeg = greenBeg;
		mGreenSpan = greenEnd-greenBeg;
		mBlueBeg = blueBeg;
		mBlueSpan = blueEnd-blueBeg;
		mAlphaBeg = alphaBeg;
		mAlphaSpan = alphaEnd-alphaBeg;
		mScaleBeg = scaleBeg;
		mScaleSpan = scaleEnd-scaleBeg;
		mRotateBeg = rotateBeg;
		mRotateSpan = rotateSpan-rotateBeg;
		
		mProgress = 0;
		mLife = life;
		mLifeReciprocal = 1f/life;
	}

	@Override
	public void modifier(ElfNode node) {
		final float rate = mProgress * mLifeReciprocal;
		
		final float alpha = mAlphaBeg+mAlphaSpan*rate;
		final float red = mRedBeg+mRedSpan*rate;
		final float green = mGreenBeg+mGreenSpan*rate;
		final float blue = mBlueBeg+mBlueSpan*rate;
		node.setColor(new ElfColor(red, green, blue, alpha));
		
		final float scale = mScaleBeg+mScaleSpan*rate;
		node.setScale(scale, scale);
		
		final float rotate = mRotateBeg+mRotateSpan*rate;
		node.setRotate(rotate);
	}
	
	public void setLife(float life){
		super.setLife(life);
		mLifeReciprocal = 1f/life;
	}
	
	public void setModifierListener(ModifierListener modifierListener){
		mModifierListener = modifierListener;
	}

	@Deprecated
	public float getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void set(float redBeg,float redEnd, float greenBeg, float greenEnd, float blueBeg, float blueEnd,
			float alphaBeg, float alphaEnd, float scaleBeg, float scaleEnd, float rotateBeg, float rotateSpan, float life){
		mRedBeg = redBeg;
		mRedSpan = redEnd-redBeg;
		mGreenBeg = greenBeg;
		mGreenSpan = greenEnd-greenBeg;
		mBlueBeg = blueBeg;
		mBlueSpan = blueEnd-blueBeg;
		mAlphaBeg = alphaBeg;
		mAlphaSpan = alphaEnd-alphaBeg;
		mScaleBeg = scaleBeg;
		mScaleSpan = scaleEnd-scaleBeg;
		mRotateBeg = rotateBeg;
		mRotateSpan = rotateSpan-rotateBeg;
		
		mProgress = 0;
		mLife = life;
		mLifeReciprocal = 1f/life;
	}
	
	protected boolean mIsFinished = false;
	
	public void onModifierFinished(ElfNode node){
		if(mModifierListener != null){
			mModifierListener.onFinished(node);
		}
	}
	
	public boolean isFinished(){
		return mIsFinished;
	}
	
	public void setFinished(){
		mIsFinished = true;
	}
	
	public void reset(){
		super.reset();
		mIsFinished = false;
	}
}
