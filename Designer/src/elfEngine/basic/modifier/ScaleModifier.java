package elfEngine.basic.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class ScaleModifier extends BasicNodeModifier{
	private float mScaleXBeg, mScaleXOffset;
	private float mScaleYBeg, mScaleYOffset;
	
	public ScaleModifier(float sxBeg, float sxEnd, float syBeg, float syEnd, float period, float life,
			LoopMode mode, Interpolator inter) {
		super(0, 1, period, life, mode, inter);
		// TODO Auto-generated constructor stub
		mScaleXBeg = sxBeg;
		mScaleXOffset = sxEnd-sxBeg;
		mScaleYBeg = syBeg;
		mScaleYOffset = syEnd-syBeg;
	}

	@Override
	public void modifier(ElfNode node) {
		final float value = getValue();
		node.setScale(mScaleXBeg+mScaleXOffset*value, mScaleYBeg+mScaleYOffset*value);
	}
}
