package elfEngine.basic.counter;


public interface IBasicCounter {
	
	public boolean isDead();
	public void count(final float pMsElapsed);
	public void reset();
	
	public float getOverProgressed();
	
	public float getProgress();
	public void setProgress(float progress);
	
	public float getLife();
	public void setLife(float life);
	
	public float getValue();
	
}
