package elfEngine.basic.counter;

import elfEngine.basic.list.ElfCircle;


/**
 * @author zju_wjf
 *
 * @param <T>
 */
public class SequenceCounter<T extends IBasicCounter> extends BasicCounter implements IBasicCounter{

	protected ElfCircle<T> mTotalList = new ElfCircle<T>();
	protected ElfCircle<T>.Iterator mIter = mTotalList.iterator();
	
	protected float 		mSequenceTime = 0;
	protected float 		mPositiveTime = 0;
	protected int 			mNegativeCount = 0;
	protected int 			mCounterSize = 0;
	protected float 		mLastProgress = 0;
	
	public SequenceCounter(T...ts){
		this(getLife(ts), ts);
		
		setLife(mSequenceTime);
	}
	
	public SequenceCounter(float life, T...ts){
		setLife(life);
		
		for(int i=0; i<ts.length; i++){
			final T t = ts[i];
			mTotalList.insertLast(t);
			addCounterTime( t.getLife() );
		}
		
		mIter.next();//point to head
	}
	
	private static float getLife(IBasicCounter...ts){
		float ret = 0;
		for(IBasicCounter c : ts){
			final float life = c.getLife();
			if(life < 0){
				return -1;
			}
			ret += c.getLife();
		}
		return ret;
	}
	
	public void refreshSequenceTime(){
		mSequenceTime = 0;
		mPositiveTime = 0;
		mNegativeCount = 0;
		
		final ElfCircle<T>.Iterator it = mTotalList.iterator();
		while(it.hasNext()){
			addCounterTime(it.next().getLife());
		}
	}
	
	public void add(T t){
		mTotalList.insertLast(t);
		addCounterTime(t.getLife());
		mCounterSize++;
	}
	
	private final void addCounterTime(final float addTime){
		if(addTime > 0){
			mPositiveTime += addTime;
			if(mSequenceTime >= 0){
				mSequenceTime += addTime;
			}
		} else {
			mSequenceTime = -1;
			mNegativeCount ++;
		}
	}
	
	public float getSequenceTime(){
		return mSequenceTime;
	}
	
	public void remov(T t){
		final boolean removeFlag = mTotalList.remove(t);
		if(removeFlag){
			final float life = t.getLife();
			if(life < 0){
				mNegativeCount--;
				if(mNegativeCount == 0){
					mSequenceTime = mPositiveTime;
				}
			} else {
				mPositiveTime -= life;
			}
			mCounterSize--;
		}
	}
	
	public void setProgress(final float progress){
		final float elapsed = progress - mProgress;
		if(elapsed > 0){
			this.count(elapsed);
		}
	}
	
	public void count(float pMsElapsed){
		mLastProgress = mProgress;
		super.count(pMsElapsed);
	}
	
	public void reset(){
		super.reset();
		mLastProgress = 0;
		
		mIter = mTotalList.iterator();
		while(mIter.hasNext()){
			mIter.next().reset();
		}
		mIter.next();//to be first
	}
	
	@Deprecated
	public float getValue() {
		return mIter.curr().getValue();
	}
	@Deprecated
	public final T getCurrentCounter(){
		return mIter.curr();
	}
}
