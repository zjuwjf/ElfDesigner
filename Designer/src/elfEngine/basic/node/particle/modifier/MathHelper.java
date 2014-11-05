package elfEngine.basic.node.particle.modifier;

public final class MathHelper {
	public static float cos(float e){
		return (float)Math.cos(e);
	}
	
	public static float sin(float e){
		return (float)Math.sin(e);
	}
	
	public static float sqrt(float e){
		return (float)Math.sqrt(e);
	}
	
	public static int clamp(int value, int min, int max) {
		return Math.min( Math.max(min, value), max);
	}
	
	public static float clamp(float value, float min, float max) {
		return Math.min( Math.max(min, value), max);
	}
	
	public static long clamp(long value, long min, long max) {
		return Math.min( Math.max(min, value), max);
	} 
	
	public static float between(final float a, final float b, final float r) {
		return a*(1-r) + b * r;
	}
}
