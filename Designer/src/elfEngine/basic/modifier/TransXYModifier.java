package elfEngine.basic.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class TransXYModifier extends BasicNodeModifier{
	private float mTransX, mTransY;
	private final float [] mTransTemp = {0,0};
	
	public TransXYModifier(float transX, float transY, float period, float life,
			LoopMode mode, Interpolator inter) {
		super(0, 1, period, life, mode, inter);
		mTransX = transX;
		mTransY = transY;
	}
	
	@Override
	public void modifier(ElfNode node) {
		final float value = getValue();
		final float transX = mTransX*value - mTransTemp[0];
		final float transY = mTransY*value - mTransTemp[1];
		
		node.translate(transX, transY);
		mTransTemp[0] += transX;
		mTransTemp[1] += transY;
	}

	public float getTransX() {
		return mTransX;
	}

	public void setTransX(float mTransX) {
		this.mTransX = mTransX;
	}

	public float getTransY() {
		return mTransY;
	}

	public void setTransY(float mTransY) {
		this.mTransY = mTransY;
	}
	
	public void reset(){
		super.reset();
		mTransTemp[0] = mTransTemp[1] = 0;
	}
}
