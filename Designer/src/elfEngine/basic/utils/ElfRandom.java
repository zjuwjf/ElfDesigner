package elfEngine.basic.utils;

import java.util.Random;

public final class ElfRandom {
	private static Random mRandom= new Random(System.currentTimeMillis());

	
	public static void init(long seed){
		mRandom = new Random(seed);
	}
	
	public static float nextFloat(){
		return mRandom.nextFloat();
	}
	
	public static boolean nextBoolean(float rate){		
		return nextFloat() < rate;
	}
	
//	public static int nextColor(){
//		return Color.rgb(nextInt(256), nextInt(256), nextInt(256));
//	}
		
	public static int nextInt(int n){
		final int res = (int)(nextFloat()*n);
		if(res == n)
			return res-1;
		return res;
	}
	
	public static <T> T nextT(T...ts){
		final int index = ElfRandom.nextInt(ts.length);
		return ts[index];
	}
	
	public static int nextInt(int beg, int end){
		return beg+nextInt(end-beg);
	}
	
	public static float nextFloat(float max, float min){
		return nextFloat()*(max-min)+min;
	}
	
	public static int nextInt(int [] array){
		return array[nextInt(array.length)];
	}
	
	public static float nextFloat(float [] array){
		return array[nextInt(array.length)];
	}
	
	public static <T> void randomSort(T [] array) {
		final int len = array.length;
		for(int i=0; i<len; i++) {
			final int swap = i + ElfRandom.nextInt(len-i);
			final T tmp = array[i];
			array[i] = array[swap];
			array[swap] = tmp;
		}
	}
	
	/**
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static <T extends Enum<T>> T nextEnum(final Class<T> t) {   
		final int index = nextInt( t.getEnumConstants().length );
	    return t.getEnumConstants()[index];
	 } 
}
