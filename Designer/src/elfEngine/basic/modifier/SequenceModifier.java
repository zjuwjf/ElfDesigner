package elfEngine.basic.modifier;

import elfEngine.basic.counter.SequenceCounter;
import elfEngine.basic.node.ElfNode;

public class SequenceModifier extends SequenceCounter<INodeModifier> implements INodeModifier{
	public SequenceModifier(INodeModifier...modifiers){
		super(modifiers);
	}
	
	public SequenceModifier(float life, INodeModifier...modifiers){
		super(life, modifiers);
	}
	
	@Override
	public void modifier(ElfNode node) {		
		float pMsElapsed = mProgress - mLastProgress;
		
		do {
			final INodeModifier t = mIter.curr();
			t.count(pMsElapsed);
			
			if(t.isDead()){
				pMsElapsed = t.getOverProgressed();
				t.setProgress(t.getLife());
				t.modifier(node);
				t.onModifierFinished(node);
				
				mIter.next().reset();
			} else {
				break;
			}
		} while(true);
		
		if(!isDead()){
			final INodeModifier modifier = mIter.curr();
			modifier.modifier(node);
		}
	}
	
	private ModifierListener mModifierListener = null;
	public final void setModifierListener(ModifierListener modifierListener){
		mModifierListener = modifierListener;
	}
	public final ModifierListener getModifierListener(){
		return mModifierListener;
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
