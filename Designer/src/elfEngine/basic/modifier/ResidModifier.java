package elfEngine.basic.modifier;

import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class ResidModifier extends BasicNodeModifier{
	private String [] mResids;
	
	public final String[] getResids() {
		return mResids;
	}

	public final void setResids(String[] mResids) {
		this.mResids = mResids;
	}
	
	public ResidModifier(String[] resids, float period, float life, LoopMode mode) {
		super(0, 1, period, life, mode, null);
		this.mResids = resids;
	}

	@Override
	public void modifier(ElfNode node) {
		node.setResid(getResid());
	}
	
	public final String getResid() {
		final float value = getValue();
		final int index = (int)(mResids.length*value);
		if(index >= mResids.length){
			return mResids[ mResids.length-1 ];
		} else if(index<0){
			return mResids[0];
		} else {
			return mResids[ index ];
		}
	}

	public ResidModifier copy(){
		return new ResidModifier(this.mResids, this.getPeroid(), this.mLife, this.getMode());
	}
}
