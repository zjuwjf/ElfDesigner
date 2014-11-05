package elfEngine.extend.count;

public final class MoveCounter implements ICoordinateCounter{
	
	private ICoordinateCounter [] mCounters;
	
	public MoveCounter(ICoordinateCounter...counters){
		this.mCounters = counters;
	}
	
	@Override
	public void count(float elapsed) {
		// TODO Auto-generated method stub
		for(ICoordinateCounter counter:mCounters){
			counter.count(elapsed);
		}
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		for(ICoordinateCounter counter:mCounters){
			if(counter.isDead()){
				return true;
			}
		}
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		for(ICoordinateCounter counter:mCounters){
			counter.reset();
		}
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		float x = 0;
		for(ICoordinateCounter counter:mCounters){
			x += counter.getX();
		}
		return x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		float y = 0;
		for(ICoordinateCounter counter:mCounters){
			y += counter.getY();
		}
		return y;
	}
}
