package elfEngine.basic.list;

import elfEngine.basic.pool.ElfPool;
import elfEngine.basic.pool.IElfPoolItem;
import elfEngine.basic.pool.INewInstance;

public class ElfCircle<T>  implements IElfList<T>{
	
	protected InnerNode mHead;
	protected final ElfPool mPool = new ElfPool();
	private final int mInitPoolSize = 32;
	
	public ElfCircle(){
		mHead = new InnerNode();
		
		mHead.prev = mHead;
		mHead.next = mHead;
		
		mPool.fillPool(new InnerNode(),mInitPoolSize);
	}
	
	public final int size(){
		if(mHead.data == null){
			return 0;
		} else {
			int ret = 1;
			InnerNode node = mHead.next;
			while(node != mHead){
				node = node.next;
				ret++;
			}
			
			return ret;
		}
	}
	
	public final Iterator iterator(){
		return new Iterator();
	}
	
	/**
	 * @param t
	 * @return 
	 */
	public final Iterator iterator(T t){
		final Iterator it = new Iterator();
		while(it.hasNext()){
			if(it.next()==t){
				return it;
			}
		}
		return null;
	}
	
	public final boolean remove(T t){
		final Iterator it = new Iterator();
		while(it.hasNext()){
			final T tmp = it.next();
			if(tmp == t){
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	public final boolean contains(T t){
		final Iterator it = new Iterator();
		
		while(it.hasNext()){
			final T tmp = it.next();
			if(tmp == t){
				return true;
			}
		}
		return false;
	}
	
	public final void reverse(){
		InnerNode current = mHead;
		do{
			final InnerNode next = current.next;
			current.reverse();
			current = next;
		}while(current!=mHead);
	}
	
	public final boolean isEmpty(){
		return mHead.data == null;
	}
	
	public final void clear(){
		while(!isEmpty()){
			removeLast();
		}
	}
	
	public final void insertFirst(T t){
		if(mHead.data==null){
			mHead.data = t;
		} else {
			final InnerNode prev = mHead.prev;
			@SuppressWarnings("unchecked")
			final InnerNode newNode = (InnerNode)mPool.getFreshItem();
			newNode.data = t;
			
			newNode.next = mHead;
			newNode.prev = prev;
			
			mHead.prev = newNode;
			prev.next = newNode;
			
			mHead = newNode;
		}
	}
	
	public final void insertLast(T t){
		if(mHead.data==null){
			mHead.data = t;
		} else {
			final InnerNode prev = mHead.prev;
			@SuppressWarnings("unchecked")
			final InnerNode newNode = (InnerNode)mPool.getFreshItem();
			newNode.data = t;
			
			newNode.next = mHead;
			newNode.prev = prev;
			
			mHead.prev = newNode;
			prev.next = newNode;
		}
	}
	
	public final T getFirst(){
		return mHead.data;
	}
	
	public final T getLast(){
		return mHead.prev.data;
	}
	
	public final void removeFirst(){
		if(mHead.next != mHead){
			final InnerNode next = mHead.next;
			mHead.next = next.next;
			mHead.data = next.data;
			next.prev = mHead;
			
			next.data = null;
			next.recycle();
		} else {
			mHead.data = null;
		}
	}
	
	public final void removeLast(){
		if(mHead.prev != mHead){
			final InnerNode prev = mHead.prev;
			mHead.prev = prev.prev;
			prev.next = mHead;
			
			prev.data = null;
			prev.recycle();
		} else {
			mHead.data = null;
		}
	}
	
	private final class InnerNode implements IElfPoolItem, INewInstance{
		private T data;
		private InnerNode next, prev;
		private InnerNode(){
		}
		public final void reverse(){
			final InnerNode tmp = next;
			next = prev;
			prev = tmp;
		}
		@Override
		public final void recycle() {
			mPool.recycle(this);
		}
		@Override
		public IElfPoolItem newInstance() {
			// TODO Auto-generated method stub
			final InnerNode ret = new InnerNode();
			mPool.recycle(ret);
			return ret;
		}
	}
	
	public final class Iterator implements IElfIterator<T>{
		private InnerNode current;
		public Iterator(){
			current = new InnerNode();
			current.next = mHead;
			current.prev = mHead.prev;
		}
		
		public boolean hasNext(){
			return current!=mHead.prev && mHead.data!=null;
		}
		public boolean hasPrev(){
			return current!=mHead && mHead.data!=null;
		}
		public T next(){
			current = current.next;
			return current.data;
		}
		public T curr(){
			return current.data;
		}
		public T prev(){
			current = current.prev;
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
			if(mHead == mHead.next){
				mHead.data = null;
				return ;
			}
			final InnerNode prev = current.prev;
			final InnerNode next = current.next;
			
			prev.next = next;
			next.prev = prev;
			
			if(current==mHead){
				mHead = mHead.next;
			} 
			current.data = null;
			current.recycle();
		}
		
		public T getCurrent(){
			return current.data;
		}
		
		public T getPrev(){
			return current.prev.data;
		}
		
		public T getNext(){
			return current.next.data;
		}
		
		public void move(int off){
			if(off<0){
				off = -off;
			}
			for(int i=0; i<off; i++){
				current = current.next;
			}
		}
	}

	@Override
	public IElfIterator<T> iterator(boolean head) {
		// TODO Auto-generated method stub
		return new Iterator();
	}
}
