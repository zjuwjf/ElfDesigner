package elfEngine.basic.modifier;

import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class TwinkleModifier extends BasicNodeModifier{
	private float mLastValue = 0;
	public TwinkleModifier(float period, float life) {
		super(0, 1, period, life, LoopMode.RELOOP, null);
	}
	
	@Override
	public void modifier(ElfNode node) {
		final float value = getValue();
		if(mLastValue < value){
			node.setVisible(true);
		} else {
			node.setVisible(false);
		}
		
		mLastValue = value;
	}
	public void reset(){
		super.reset();
		mLastValue = 0;
	}
}
