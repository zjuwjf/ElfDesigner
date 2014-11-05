package elfEngine.basic.modifier;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class CoordinatModifier extends BasicNodeModifier{
//	private float mCentreX, mCentreY;
//	private float mVelocityX, mVelocityY;
//	private float mGravityX, mGravityY;
	
	public CoordinatModifier(float period, float life,
			LoopMode mode, Interpolator inter) {
		super(0, 1, period, life, mode, inter);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void modifier(ElfNode node) {
		// TODO Auto-generated method stub
		
	}
}
