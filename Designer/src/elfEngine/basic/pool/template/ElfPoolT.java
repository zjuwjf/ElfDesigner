package elfEngine.basic.pool.template;

import elfEngine.basic.list.ElfStack;
import elfEngine.basic.pool.IElfPoolItem;

public class ElfPoolT <T extends IElfPoolItem> implements IElfPoolT<T>{
	private int mRemains = 0;
	private final ElfStack<T> mIdleList;
	private INewInstanceT<T> mNewInstanceHandler;
	
	public ElfPoolT(){
		mIdleList = new ElfStack<T>();
	}
	
	/**
	 * @param newInstanceHandler
	 * @param size
	 * @make sure recycle it when it news
	 */
	public final void fillPool(INewInstanceT<T> newInstanceHandler, int size){
		mIdleList.clear();
		mNewInstanceHandler = newInstanceHandler;
		for(int i=0; i<size; i++){
			mNewInstanceHandler.newInstance();
		}
	}
	
	@Override
	public T getFreshItem() {
		mRemains--;
		T ret = mIdleList.pop();
		if(ret == null){
			ret = mNewInstanceHandler.newInstance();
			mIdleList.removeFirst();
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void recycle(IElfPoolItem t) {
		mRemains++;
		mIdleList.push((T)t);
	}
	
	public final int remains(){
		return mRemains;
	}
}
