package elfEngine.basic.modifier;

import elfEngine.basic.counter.ElfCounter;
import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public abstract class BasicNodeModifier extends ElfCounter implements INodeModifier{
	
	public BasicNodeModifier(float beg, float end, float period, float life,
			LoopMode mode, Interpolator inter) {
		this(beg, end, period, life, mode, inter, false);
	}
	
	public BasicNodeModifier(float beg, float end, float period, float life,
			LoopMode mode, Interpolator inter, boolean removable) {
		super(beg, end, period, life, mode, inter);
		setRemovable(removable);
	}
	
	protected ModifierListener mModifierListener = null;
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
