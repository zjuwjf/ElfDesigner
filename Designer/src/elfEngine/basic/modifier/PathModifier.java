package elfEngine.basic.modifier;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.ElfPath;
import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.pool.ArrayPool;

public class PathModifier extends BasicNodeModifier{
	protected ElfPath mElfPath;
	
	public PathModifier(float period, float life, LoopMode mode, Interpolator inter, ElfPointf...ps) {
		super(0, 1, period, life, mode, inter);
		mElfPath = new ElfPath();
		for(int i=0; i<ps.length; i++){
			mElfPath.add(ps[i]);
		}
	}
	
	@Override
	public void modifier(ElfNode node) {
		final float value = getValue();
		final float [] position = ArrayPool.float2;
		mElfPath.getPosition(value, position);
		node.setPosition(position[0], position[1]);
	}
	
	public void clearElfPath(){
		mElfPath.clear();
	}
	
	public void setElfPath(ElfPath path){
		mElfPath = path;
	}
	
	public final ElfPath getElfPath(){
		return mElfPath;
	}
}
