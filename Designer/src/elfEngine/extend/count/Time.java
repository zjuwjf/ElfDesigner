package elfEngine.extend.count;

public final class Time {
	private final String ShortGap = ":"; 
	private final String LongGap = ":0"; 
	
	//millisecond
	private int mProgress;
	private int mInitProgress;
	
	private final int HOUR_MOD = 60*60*1000;
	private final int MINUTE_MOD = 60*1000;
	private final int SECOND_MOD = 1000;
	private final int MILLISECOND_MOD = 1;
	
	private enum Mode{
		PAUSE,RUNNING
	}
	
	private Mode mMode = Mode.RUNNING;
	public void pause(){
		mMode = Mode.PAUSE;
	}
	public void resume(){
		mMode = Mode.RUNNING;
	}

	public Time(int second) {
		super();
		mInitProgress = second*1000;
	}
	public void reset(){
		mProgress = 0;
	}
	
	public void add(int add){
		if(mMode == Mode.RUNNING)
			mProgress += add;
	}
	public void sub(int sub){
		if(mMode == Mode.RUNNING)
			mProgress -= sub;
	}
	
	public int hour(){
		int total = (mProgress+mInitProgress);
		if(total < 0){
			total = 0;
		}
		return total/HOUR_MOD;
	}
	
	public int minute(){
		int total = (mProgress+mInitProgress);
		if(total < 0){
			total = 0;
		}
		return total/MINUTE_MOD-hour()*60;
	}
	
	public int second(){
		int total = (mProgress+mInitProgress);
		if(total < 0){
			total = 0;
		}
		return total/SECOND_MOD-minute()*60;
	}
	
	public int millisecond(){
		int total = (mProgress+mInitProgress);
		if(total < 0){
			total = 0;
		}
		return total/MILLISECOND_MOD-second()*1000;
	}
	
	public boolean isDead(){
		return (mProgress+mInitProgress)<=0;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * @ return minute:second
	 */
	public String toString(){
		if(second()>=10)
			return new StringBuffer().append(minute()).append(ShortGap).append(second()).toString();
		else
			return new StringBuffer().append(minute()).append(LongGap).append(second()).toString();
	}
}
