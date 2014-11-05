package elfEngine.basic.modifier;

import elfEngine.basic.node.ElfNode;

/**
 * @author zju_wjf
 *
 */
public class LifeModifier extends BasicNodeModifier{

	public LifeModifier(float life) {
		super(0, 1, life, life, null, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void modifier(ElfNode node) {
	}
	
	public void onModifierFinished(ElfNode node){
		node.removeFromParent();
		super.onModifierFinished(node);
	}
}
