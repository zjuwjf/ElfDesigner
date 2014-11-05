package elfEngine.basic.modifier;

import elfEngine.basic.counter.IBasicCounter;
import elfEngine.basic.node.ElfNode;

public interface INodeModifier extends IBasicCounter, IModifierRemovable{
	public void modifier(ElfNode node);
	
	/**
	 * listener
	 */
	public void onModifierFinished(ElfNode node);
	
	public boolean isFinished();
	public void setFinished();
	public void reset();
	
	public static interface ModifierListener{
		public void onFinished(ElfNode node);
	}
	
}
