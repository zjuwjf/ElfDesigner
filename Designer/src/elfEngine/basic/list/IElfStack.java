package elfEngine.basic.list;

/**
 * @author zju_wjf
 *
 * @param <T>
 */
public interface IElfStack <T>{
	public T pop();
	public void push(T t);
	public T getFirst();
	public void removeFirst();
	public void clear();
	public boolean isEmpty();
	public int size();
}
