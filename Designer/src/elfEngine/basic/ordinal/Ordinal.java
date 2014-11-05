package elfEngine.basic.ordinal;

public class Ordinal implements IElfOrdinal{
	private final int mOrdinal;
	public Ordinal(int ordinal){
		mOrdinal = ordinal;
	}
	public int ordinal(){
		return mOrdinal;
	}
}