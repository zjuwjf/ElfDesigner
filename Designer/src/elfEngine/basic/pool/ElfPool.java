package elfEngine.basic.pool;

import elfEngine.basic.list.ElfStack;

/**
 * @author zju_wjf
 *
 */
public class ElfPool implements IElfPool{
	private int mRemains = 0;
	private final ElfStack<IElfPoolItem> mIdleList;
	private INewInstance mNewInstanceHandler;
	public ElfPool(){
		mIdleList = new ElfStack<IElfPoolItem>();
	}
	
	/**
	 * @param newInstanceHandler
	 * @param size
	 */
	public final void fillPool(INewInstance newInstanceHandler, int size){
		mIdleList.clear();
		mNewInstanceHandler = newInstanceHandler;
		for(int i=0; i<size; i++){
			mNewInstanceHandler.newInstance();
		}
	}
	
	@Override
	public IElfPoolItem getFreshItem() {
		mRemains--;
		IElfPoolItem ret = mIdleList.pop();
		if(ret == null){
			ret = mNewInstanceHandler.newInstance();
			mIdleList.removeFirst();
		}
		return ret;
	}
	@Override
	public final void recycle(IElfPoolItem t) {
		mRemains++;
		mIdleList.push(t);
	}
	public final int remains(){
		return mRemains;
	}
}
