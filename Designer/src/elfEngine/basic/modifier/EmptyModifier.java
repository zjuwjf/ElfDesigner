package elfEngine.basic.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class EmptyModifier extends BasicNodeModifier{
	
	public EmptyModifier(float beg, float end, float period, float life,
			LoopMode mode, Interpolator inter) {
		super(beg, end, period, life, mode, inter);
	}

	@Override
	public void modifier(ElfNode node) {
		
	}
}
