package elfEngine.basic.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class RotateModifier  extends BasicNodeModifier{

	public RotateModifier(float beg, float end, float period, float life,
			LoopMode mode, Interpolator inter) {
		super(beg, end, period, life, mode, inter);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void modifier(ElfNode node) {
		// TODO Auto-generated method stub
		node.setRotate(getValue());
	}
}
