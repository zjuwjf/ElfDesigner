package elfEngine.extend.count;

public final class ResidCounter implements IResidCounter{

	private int [] mArry;
	private float mProgress;
	private float mPeroid;
	private float mLife;
	
	public ResidCounter(int [] arry, float period, float life){
		this.mArry = arry;
		this.mPeroid = period;
		this.mLife = life;
	}
	
	@Override
	public int resid() {
		// TODO Auto-generated method stub
		final float rate = mProgress/mPeroid;
		int index = (int)(rate-(int)rate)*mArry.length;
		if(index >= mArry.length){
			return mArry.length-1;
		}
		return index;
	}

	@Override
	public void count(float elapsed) {
		// TODO Auto-generated method stub
		mProgress += elapsed;
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return mProgress>mLife && mLife>=0;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		mProgress = 0;
	}
}
