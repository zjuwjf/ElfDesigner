package elfEngine.basic.counter;



public class ResidCounter extends ElfCounter implements IResidCounter{
	
	public ResidCounter(int[] resids, float period, float life,LoopMode mode) {
		super(0, 1, period, life, mode, null);
		// TODO Auto-generated constructor stub
		mResids = resids;
	}

	private int [] mResids;
	
	public int[] getResids() {
		return mResids;
	}

	public void setResids(int[] mResids) {
		this.mResids = mResids;
	}
	
	public final int getResid() {
		// TODO Auto-generated method stub
		final float value = getValue();
		int index = (int)(mResids.length*value);
		if(index == mResids.length){
			return mResids[ mResids.length-1 ];
		} else {
			return mResids[ index ];
		}
	}
}
