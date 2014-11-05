package elfEngine.basic.list;

/**
 * @author zju_wjf
 *
 * @param <T>
 */
public interface IElfIterator<T> {
	public void remove();
	public boolean hasNext();
	public boolean hasPrev();
	public void insertBefore(T t);
	public void insertAfter(T t);
	public T next();
	public T prev();
	public T curr();
}
