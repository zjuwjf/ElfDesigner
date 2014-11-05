package elfEngine.basic.list;

import elfEngine.basic.ordinal.IElfOrdinal;


/**
 * @author zju_wjf
 * èƒ¥ÛµΩ–°
 * @param <T>
 */
public class ElfOrderedList <T extends IElfOrdinal>extends ElfList<T>{
	
	public final void insertBefore(T t){
		final ElfList<T>.Iterator it = this.iterator(true);
		while(it.hasNext()){
			final T tmp = it.next();
			if(tmp.ordinal() >= t.ordinal()){
				it.insertBefore(t);
				return ;
			}
		}

		it.insertAfter(t);
	}
	
	
	public final void insertAfter(T t){
		final ElfList<T>.Iterator it = this.iterator(false);
		while(it.hasPrev()){
			final T tmp = it.prev();
			if(tmp.ordinal() <= t.ordinal()){
				it.insertAfter(t);
				return ;
			} 
		} 

		it.insertBefore(t);
	}
}
