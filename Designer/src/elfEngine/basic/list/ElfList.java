package elfEngine.basic.list;

import java.util.ArrayList;

import elfEngine.basic.pool.ElfPool;
import elfEngine.basic.pool.IElfPoolItem;
import elfEngine.basic.pool.INewInstance;

public class ElfList<T>  implements IElfList<T>{
	
	protected final InnerNode mHead;
	protected final InnerNode mTail;
	protected final ElfPool mPool = new ElfPool();
	private final int mInitPoolSize = 32;
	
	public ElfList(){
		mHead = new InnerNode();
		mTail = new InnerNode();
		
		mHead.prev = mHead;
		mHead.next = mTail;
		mTail.prev = mHead;
		mTail.next = mTail;
		
		mPool.fillPool(new INewInstance() {
			public final InnerNode newInstance() {
				return new InnerNode();
			}
		},mInitPoolSize);
	}
	
	public final int size(){
		int ret = 0;
		InnerNode node = mHead;
		while(node.next!=mTail){
			node = node.next;
			ret++;
		}
		return ret;
	}
	
	public final Iterator iterator(final boolean isHead){
		return new Iterator(isHead);
	}
	
	/**
	 * @param t
	 * @return 
	 */
	public final Iterator iterator(T t){
		final Iterator it = new Iterator(true);
		while(it.hasNext()){
			if(it.next()==t){
				return it;
			}
		}
		return null;
	}
	
	public final boolean remove(T t){
		final Iterator it = new Iterator(false);
		while(it.hasPrev()){
			final T tmp = it.prev();
			if(tmp == t){
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	public final boolean contains(T t){
		final Iterator it = new Iterator(false);
		
		while(it.hasPrev()){
			final T tmp = it.prev();
			if(tmp == t){
				return true;
			}
		}
		return false;
	}
	
	public final boolean isEmpty(){
		return mHead.next == mTail;
	}
	
	public final void clear(){		
		while(!isEmpty()){
			removeFirst();
		}
	}
	
	public final void insertFirst(T t){
		final InnerNode next = mHead.next;
		@SuppressWarnings("unchecked")
		final InnerNode newNode = (InnerNode)mPool.getFreshItem();
		newNode.data = t;
		
		newNode.next = next;
		newNode.prev = mHead;
		
		mHead.next = newNode;
		next.prev = newNode;

	}
	
	public final void insertLast(T t){
		final InnerNode prev = mTail.prev;
		@SuppressWarnings("unchecked")
		final InnerNode newNode = (InnerNode)mPool.getFreshItem();
		newNode.data = t;
		
		newNode.next = mTail;
		newNode.prev = prev;
		
		mTail.prev = newNode;
		prev.next = newNode;
		
	}
	
	public final T getFirst(){
		return mHead.next.data;
	}
	
	public final T getLast(){
		return mTail.prev.data;
	}
	
	public final void removeFirst(){
		final InnerNode next = mHead.next;
		
		if(next!=mTail){
			next.data = null;
			next.recycle();
		}
		
		mHead.next = next.next;
		next.next.prev = mHead;
		
		
	}
	
	public final void removeLast(){
		final InnerNode prev = mTail.prev;
		
		if(prev!=mHead){
			prev.data = null;
			prev.recycle();
		}
		
		mTail.prev = prev.prev;
		prev.prev.next = mTail;

	}
	
	private final class InnerNode implements IElfPoolItem{
		private T data;
		private InnerNode next, prev;
		private InnerNode(){
			mPool.recycle(this);
		}
		@Override
		public final void recycle() {
			mPool.recycle(this);
		}
	}
	
	public final class Iterator implements IElfIterator<T>{
		private InnerNode current;
		public Iterator(final boolean isHead){
			current = isHead? mHead:mTail;
		}
		
		public boolean hasNext(){
			return current.next!=mTail;
		}
		public boolean hasPrev(){
			return current.prev!=mHead;
		}
		public T next(){
			current = current.next;
			return current.data;
		}
		public T prev(){
			current = current.prev;
			return current.data;
		}
		public T getPrev(){
			return current.prev.data;
		}
		public T getNext(){
			return current.next.data;
		}
		public T curr(){
			return current.data;
		}
		
		public void insertBefore(T t){
			@SuppressWarnings("unchecked")
			final InnerNode newNode = (InnerNode)mPool.getFreshItem();
			newNode.data = t;
			
			final InnerNode prev = current.prev;
			newNode.prev = prev;
			newNode.next = current;
			current.prev = newNode;
			prev.next = newNode;
		}
		
		public void insertAfter(T t){
			@SuppressWarnings("unchecked")
			final InnerNode newNode = (InnerNode)mPool.getFreshItem();
			newNode.data = t;
			
			final InnerNode next = current.next;
			newNode.prev = current;
			newNode.next = next;
			current.next = newNode;
			next.prev = newNode;
		}
		
		public void remove(){
			current.data = null;
			current.recycle();
			if(current.prev!=null){
				current.prev.next = current.next;
			}
			if(current.next!=null){
				current.next.prev = current.prev;
			}
		}
	}
	
	
	public ArrayList<T> toArray(){
		final int size = size();
		final ArrayList<T> arrayList = new ArrayList<T>(size);
		final Iterator it = iterator(true);
		while(it.hasNext()){
			arrayList.add(it.next());
		}
		return arrayList;
	}
}
