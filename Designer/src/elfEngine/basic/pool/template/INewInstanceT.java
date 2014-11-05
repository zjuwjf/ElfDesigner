package elfEngine.basic.pool.template;

import elfEngine.basic.pool.IElfPoolItem;

public interface INewInstanceT <T extends IElfPoolItem>{
	public T newInstance();
}
