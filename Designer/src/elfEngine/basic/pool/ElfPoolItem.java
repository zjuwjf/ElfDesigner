package elfEngine.basic.pool;

public class ElfPoolItem implements IElfPoolItem{
	private final IElfPool mPool;
	public ElfPoolItem(IElfPool pool){
		pool.recycle(this);
		mPool = pool;
	}
	
	@Override
	public final void recycle() {
		mPool.recycle(this);
	}
}
