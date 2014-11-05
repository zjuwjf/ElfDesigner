package elfEngine.basic.pool.template;

import elfEngine.basic.pool.IElfPool;
import elfEngine.basic.pool.IElfPoolItem;

public interface IElfPoolT <T extends IElfPoolItem> extends IElfPool{
	public T getFreshItem();
	public void recycle(IElfPoolItem t);
	public int remains();
}
