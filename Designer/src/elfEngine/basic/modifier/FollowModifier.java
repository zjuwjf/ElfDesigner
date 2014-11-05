package elfEngine.basic.modifier;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class FollowModifier extends BasicNodeModifier{
	
	private final ElfNode mTargetNode;
	private ElfPointf mStartPoint = null;
	
	public FollowModifier(ElfNode target, float period, float life, Interpolator inter) {
		super(0, 1, period, life, LoopMode.STAY, inter);
		
		mTargetNode = target;
	} 

	public void modifier(ElfNode node) { 
		try {
			if(mStartPoint == null) {
				mStartPoint = node.getPositionInScreen();
			} 
			final float value = getValue();
			final ElfPointf target = mTargetNode.getPositionInScreen();
			node.setPositionInScreen(new ElfPointf(mStartPoint.x + (target.x-mStartPoint.x)*value, mStartPoint.y + (target.y-mStartPoint.y)*value));
		} catch (Exception e) {
		}
	} 
	
	public void reset() { 
		super.reset();
		mStartPoint = null;
	} 
} 
