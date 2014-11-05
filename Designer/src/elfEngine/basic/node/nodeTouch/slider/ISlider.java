package elfEngine.basic.node.nodeTouch.slider;


public interface ISlider {
	
	public void sliderUp(float x, float y);
	public void sliderMove(float x, float y);
	public void sliderDown(float x, float y);
	
	public void setPercentage(float percentage, boolean innerCall);
	public float getPercentage();
	
}
