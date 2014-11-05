package elfEngine.basic.list;

/**
 * @author zju_wjf
 *
 * @param <T>
 */
public class ElfStack<T> implements IElfStack<T>{
	
	private static final int sInitArryCap = 16;
	private static final int sMaxLists = 30;
	private static final int [] sSizeTable = new int[sMaxLists];
	static{
		for(int i=0; i<sSizeTable.length; i++){
			sSizeTable[i] = (sInitArryCap<<(i+1));
			if(i>0)
				sSizeTable[i] += sSizeTable[i-1];
		}
	}
	
	private final Object [] mCacheList;
	private int mArryCap;
	private int mCacheListIndex;
	private int mArryIndex;
	
	public ElfStack(){
		mArryIndex = sInitArryCap;
		mArryCap = sInitArryCap;
		mCacheListIndex = -1;
		
		mCacheList = new Object[sMaxLists][];
	}
	
	@Override
	public final void clear() {
		for(int i=0; i<mCacheList.length; i++){
			if(mCacheList[i] != null){
				final Object[] os = (Object[])mCacheList[i];
				for(int j=0; j<os.length; j++){
					os[j] = null;
				}
			}
			mCacheList[i] = null;
		}
		mArryIndex = 16;
		mCacheListIndex = -1;
		mArryCap = sInitArryCap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final T getFirst() {
		// TODO Auto-generated method stub
		final T t = (T)(((Object [])(mCacheList[mCacheListIndex]))[mArryIndex]);
		return t;
	}
	@Override
	public final boolean isEmpty() {
		// TODO Auto-generated method stub
		return mCacheListIndex < 0;
	}
	@SuppressWarnings("unchecked")
	@Override
	public final T pop() {
		if(mCacheListIndex<0){
			return null;
		}
		final T t = (T)(((Object [])(mCacheList[mCacheListIndex]))[mArryIndex]);
		(((Object [])(mCacheList[mCacheListIndex]))[mArryIndex]) = null;
		mArryIndex--;
		if(mArryIndex<0){
			mCacheListIndex--;
			mArryCap >>= 1;
			mArryIndex = mArryCap-1;
		}
		return t;
	}
	@Override
	public final void push(T t) {
		mArryIndex++;
		if(mArryIndex>=mArryCap){
			mCacheListIndex++;
			mArryCap <<= 1;
			if(mCacheList[mCacheListIndex] == null){
				mCacheList[mCacheListIndex] = new Object[mArryCap];
			}
			mArryIndex = 0;
		}
		((Object [])(mCacheList[mCacheListIndex]))[mArryIndex] = t;
	}
	@Override
	public final void removeFirst() {
		if(mCacheListIndex<0){
			return ;
		}
		(((Object [])(mCacheList[mCacheListIndex]))[mArryIndex]) = null;
		mArryIndex--;
		if(mArryIndex<0){
			mCacheListIndex--;
			mArryCap >>= 1;
			mArryIndex = mArryCap-1;
		}
	}

	@Override
	public final int size() {
		if(mCacheListIndex<0){
			return 0;
		} else if(mCacheListIndex==0){
			return mArryIndex+1;
		} else {
			return sSizeTable[mCacheListIndex-1]+mArryIndex+1;
		}
	}
}
