package elfEngine.basic.counter;


import elfEngine.basic.debug.Debug;
import elfEngine.basic.list.ElfList;
import elfEngine.graphics.GraphHelper;
import elfEngine.graphics.PointF;

public class ElfPathT<T extends PointF> implements IElfPath{
	
	private ElfList<T> mList = new ElfList<T>();
	private ElfList<Float> mDistance = new ElfList<Float>();
	private float mTotalDistance = 0;
	
	public ElfPathT(T...ts){
		for(int i=0; i<ts.length; i++){
			add(ts[i]);
		}
	}
	
	public void add(final T t){
		final T last = mList.getLast();
		mList.insertLast(t);
		if(last!=null){
			final float dis = distance(last.x, last.y, t.x, t.y);
			mDistance.insertLast(dis);
			mTotalDistance += dis;
		}
	}
	
	public boolean remove(final T t){
		int index=0;
		final ElfList<T>.Iterator it = mList.iterator(true);
		while(it.hasNext()){
			if(it.next()==t){
				index = -index;
				break;
			} else {
				index--;
			}
		}
		
		return remove(index);
	}
	
	public boolean remove(int index){
		final int size = mList.size();
		if(index<0 || index>=size){
			return false;
		} else if(size == 1){//index=0
			mList.removeFirst();
			mTotalDistance = 0;
		} else {
			if(index == 0){
				final float dis = mDistance.getFirst();
				mTotalDistance -= dis;
				
				mDistance.removeFirst();
				mList.removeFirst();
			} else if(index == (size-1)){
				final float dis = mDistance.getLast();
				mTotalDistance -= dis;
				
				mDistance.removeLast();
				mList.removeLast();
			} else{
				final ElfList<T>.Iterator itT = mList.iterator(true);
				final ElfList<Float>.Iterator itDis = mDistance.iterator(true);
				
				while(index>=0){
					index--;
					itT.next();
					itDis.next();
				}
				itT.remove();
				final T next = itT.next();
//				itT.prev();
				final T prev = itT.prev();
				final float addDis = distance(next.x, next.y,  prev.x, prev.y);
				mTotalDistance +=addDis;
				itDis.insertBefore(addDis);
				
				final float removeDisLeft = itDis.curr();
				itDis.remove();
				mTotalDistance -= removeDisLeft;
				
				final float removeDisRight = itDis.next();
				itDis.remove();
				mTotalDistance -= removeDisRight;
			}
		}
		
		return false;
	}
	
	
	
	public void clear(){
		mList.clear();
		mDistance.clear();
		mTotalDistance = 0;
	}
	
	/**
	 * do not modifier the points
	 * @return
	 */
	public ElfList<T> getList(){
		return mList;
	}
	
	public float getTotalLength(){
		return mTotalDistance;
	}
	

	@Override
	public void getPosition(final float rate, final float[] ret) {
		if(rate<=0){
			final int size = mList.size();
			if(size>=2){
				final ElfList<T>.Iterator it = mList.iterator(true);
				PointF [] ts = new PointF[2];
				for(int i=0; i<2; i++){
					ts[i] = it.next();
				}
				
				final float dis = GraphHelper.distance(ts[0].x, ts[0].y, 
						ts[1].x, ts[1].y);
				if(dis!=0){
					ret[0] = ts[0].x+(ts[1].x-ts[0].x)*rate*mTotalDistance/dis;
					ret[1] = ts[0].y+(ts[1].y-ts[0].y)*rate*mTotalDistance/dis;
				}
				
			} else if(size==1){
				final T t = mList.getFirst();
				if(t!=null){
					ret[0] = t.x;
					ret[1] = t.y;
				}
			}
		} else if(rate>1){
			final int size = mList.size();
			if(size>=2){
				final ElfList<T>.Iterator it = mList.iterator(false);
				PointF [] ts = new PointF[2];
				for(int i=0; i<2; i++){
					ts[i] = it.prev();
				}
				
				final float dis = GraphHelper.distance(ts[0].x, ts[0].y, 
						ts[1].x, ts[1].y);
				if(dis!=0){
					ret[0] = ts[0].x+(ts[0].x-ts[1].x)*(rate-1)*mTotalDistance/dis;
					ret[1] = ts[0].y+(ts[0].y-ts[1].y)*(rate-1)*mTotalDistance/dis;
				}
				
			} else if(size==1){
				final T t = mList.getLast();
				if(t!=null){
					ret[0] = t.x;
					ret[1] = t.y;
				}
			}
		} else {
			float len = rate*mTotalDistance;
			final ElfList<Float>.Iterator itDis = mDistance.iterator(true);
			final ElfList<T>.Iterator itT = mList.iterator(true);
			
			while(itDis.hasNext()){
				itT.next();
				final float dis = itDis.next();
				if(len>dis){
					len -= dis;
				} else{
					if(dis==0){
						final T curr = itT.curr();
						ret[0] = curr.x;
						ret[1] = curr.y;
					} else {
						final float newRate = len/dis;
						final T curr = itT.curr();
						final T next = itT.next();
						
						ret[0] = curr.x + (next.x-curr.x)*newRate;
						ret[1] = curr.y + (next.y-curr.y)*newRate;
					}
					
					return;
				}
			}
			
			final T t = mList.getLast();
			if(t != null){
				ret[0] = t.x;
				ret[1] = t.y;
			} else {
				Debug.e("ElfPathT+ther are no points!");
			}
		}
	}
	
	private final float distance(final float x1, final float y1, final float x2, final float y2){
		final float dx = x1-x2;
		final float dy = y1-y2;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}
}
