package elfEngine.basic.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class TimeRateModifier extends BasicNodeModifier{

	public TimeRateModifier(float beg, float end, float period, float life,
			LoopMode mode, Interpolator inter) {
		super(beg, end, period, life, mode, inter);
	}

	@Override
	public void modifier(ElfNode node) {
		node.setElaspedRate(getValue());
	}
}
