package elfEngine.basic.pool;


public interface IElfPool{
	public Object getFreshItem();
	public void recycle(IElfPoolItem t);
	public int remains();
}
