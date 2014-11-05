package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.modifier.INodeModifier;
import elfEngine.basic.modifier.ParallelModifier;

public class PParalleModifier extends ParallelModifier{

	public PParalleModifier(float period, float life, LoopMode mode,
			Interpolator inter, INodeModifier[] modifiers) {
		super(period, life, mode, inter, modifiers);
	}
	
}
