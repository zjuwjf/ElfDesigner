package elfEngine.basic.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.list.ElfList;
import elfEngine.basic.node.ElfNode;

public class ParallelModifier extends BasicNodeModifier{
	
	public ParallelModifier(float period, float life, LoopMode mode, Interpolator inter, INodeModifier...modifiers) {
		super(0, 1, period, life, mode, inter);
		
		mModifierList = new ElfList<INodeModifier>();
		for(int i=0; i<modifiers.length; i++){
			mModifierList.insertLast(modifiers[i]);
		}
		
	}

	private final ElfList<INodeModifier> mModifierList;
	public final ElfList<INodeModifier> getModifiers(){
		return mModifierList;
	}
	public final void clearModifiers(){
		mModifierList.clear();
	}
	
	public final void addModifier(final INodeModifier modifier){
		mModifierList.insertLast(modifier);
	}
	
	public final void removeModifier(final INodeModifier modifier){
		mModifierList.remove(modifier);
	}
	
	public final void resetModifiers(){
		final ElfList<INodeModifier>.Iterator it = mModifierList.iterator(true);
		while(it.hasNext()){
			final INodeModifier modifier = it.next();
			modifier.reset();
		}
	}
	
	public void modifier(ElfNode node) {
		final ElfList<INodeModifier>.Iterator it = mModifierList.iterator(true);
//		final float progress = getValue()*getPeroid();
		final float progress = mProgress;
		
		while(it.hasNext()){
			final INodeModifier modifier = it.next();
			modifier.setProgress(progress);
			modifier.modifier(node);
			
			if(modifier.isDead()){
				if(modifier.isRemovable()){
					it.remove();
				}
				
				modifier.onModifierFinished(node);
			}
		}
	}
}
