package elfEngine.basic.counter;

public interface Interpolator {
	public float getInterpolation(float input);
	public float getInterpolation(float input, float rate);
}
