package elfEngine.basic.modifier;

import elfEngine.basic.node.ElfNode;

/**
 * @author zju_wjf
 *
 */
public class NodalModifier implements INodeModifier{
	private final ModifierListener mModifierListener;
	private boolean mIsRun = false;
	private float mProgress = 0;
	public NodalModifier(ModifierListener modifierListener){
		mModifierListener = modifierListener;
	}
	
	@Override
	public final void modifier(ElfNode node) {
		mIsRun = true;
	}
	
	private boolean mIsRemovable = false;
	@Override
	public boolean isRemovable() {
		// TODO Auto-generated method stub
		return mIsRemovable;
	}
	
	@Override
	public void setRemovable(boolean isRemovable) {
		// TODO Auto-generated method stub
		mIsRemovable = isRemovable;
	}
	
	@Override
	public void reset() {
		mProgress = 0;
		mIsFinished = false;
		mIsRun = false;
	}
	
	@Override
	public boolean isDead() {
		return mIsRun;
	}
	
	@Override
	public float getLife() {
		return 0;
	}
	
	@Override
	public void count(float pMsElapsed) {
		mProgress += pMsElapsed;
	}

	@Override
	public float getOverProgressed() {
		return mProgress;
	}

	@Override
	public float getProgress() {
		return mProgress;
	}
	
	@Override
	@Deprecated
	public float getValue() {
		return 0;
	}

	@Override
	@Deprecated
	public void setLife(float life) {
	}

	@Override
	@Deprecated
	public void setProgress(float progress) {
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
	

	
}
