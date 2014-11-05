package elfEngine.basic.elfEngine.basic.object;

public interface IElfCentre {
	public void setCentre(float x, float y);
	public float getCentreX();
	public float getCentreY();
	public void setCentreX(float x);
	public void setCentreY(float y);
	public void translate(float x, float y);
	public void translateX(float x);
	public void translateY(float y);
}
