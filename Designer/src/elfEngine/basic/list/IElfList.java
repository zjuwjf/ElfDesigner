package elfEngine.basic.list;

/**
 * @author zju_wjf
 * 
 * @param <T>
 */

public interface IElfList<T> {
	public boolean contains(T t);
	public void insertFirst(T t);
	public void insertLast(T t);
	public T getFirst();
	public T getLast();
	public void removeFirst();
	public void removeLast();
	public boolean isEmpty();
	public int size();
	public IElfIterator<T> iterator(boolean head);
}
