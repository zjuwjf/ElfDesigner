package elfEngine.extend.count;

import java.util.LinkedList;
import java.util.ListIterator;

public class ElfOrderedCounters  implements ICounter{
	
	private LinkedList<ICounter> mCounters = new LinkedList<ICounter>();
	private int mRemainedCounters = 0;
	
	public ElfOrderedCounters(ICounter...counters){
		mRemainedCounters = counters.length;
		for(int i=0; i<mRemainedCounters; i++){
			mCounters.add(counters[i]);
		}
	}
	
	public ElfOrderedCounters(int cout,ICounter...counters){
		mRemainedCounters = counters.length;
		for(int i=0; i<mRemainedCounters; i++){
			mCounters.add(counters[i]);
		}
		mRemainedCounters = cout;
	}
	
	public void add(ICounter pCounter){
		ListIterator<ICounter> it = mCounters.listIterator();
		for(int i=0; i<mRemainedCounters; i++){
			it.next();
		}
		it.add(pCounter);
		mRemainedCounters++;
	}
	
	public ICounter currnet(){
		return this.mCounters.getFirst();
	}

	@Override
	public void count(float time) {
		// TODO Auto-generated method stub
		final ICounter counter = currnet();
		counter.count(time);
		if(counter.isDead()){
			this.mCounters.removeFirst();
			counter.reset();
			this.mCounters.addLast(counter);
			mRemainedCounters--;
		}
	}
	
	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return mRemainedCounters == 0;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		for(int i=0; i<mRemainedCounters; i++){
			final ICounter counter = this.mCounters.getFirst();
			counter.reset();
			this.mCounters.removeFirst();
			this.mCounters.addLast(counter);
		}
		this.mRemainedCounters = this.mCounters.size();
	}

	@Override
	public int resid() {
		// TODO Auto-generated method stub
		return currnet().resid();
	}

	@Override
	public float value() {
		// TODO Auto-generated method stub
		return currnet().value();
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return currnet().getX();
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return currnet().getY();
	}
}
