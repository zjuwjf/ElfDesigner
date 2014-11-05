package elfEngine.basic.touch;

import elfEngine.basic.pool.ElfPool;
import elfEngine.basic.pool.IElfPoolItem;
import elfEngine.basic.pool.INewInstance;
import elfEngine.graphics.PointF;

public final class ElfEvent extends PointF implements IElfPoolItem{

	public int pointerID;
	public int action;
	public int wheelMove;
	public int stateMask;
	
	
	private ElfEvent(){
		recycle();
	}
	
//	public String toString(){
//		switch(action){
//		case MotionEvent.LEFT_DOWN:
//			return "LEFT_DOWN "+super.toString();
//		case MotionEvent.LEFT_MOVE:
//			return "LEFT_MOVE "+super.toString();
//		case MotionEvent.LEFT_UP:
//			return "LEFT_UP "+super.toString();
//		case MotionEvent.RIGHT_DOWN:
//			return "RIGHT_DOWN "+super.toString();
//		case MotionEvent.RIGHT_MOVE:
//			return "RIGHT_MOVE "+super.toString();
//		case MotionEvent.RIGHT_UP:
//			return "RIGHT_UP "+super.toString();
//		case MotionEvent.LEFT_CLICK:
//			return "LEFT_CLICK "+super.toString();
//		case MotionEvent.RIGHT_CLICK:
//			return "RIGHT_CLICK "+super.toString();
//		case MotionEvent.LEFT_DOUBLE_CLICK:
//			return "LEFT_DOUBLE_CLICK "+super.toString();
//		case MotionEvent.RIGHT_DOUBLE_CLICK:
//			return "RIGHT_DOUBLE_CLICK "+super.toString();
//		}
//		return "error event!";
//	}
	
	public void set(MotionEvent event){
		this.x = event.getX();
		this.y = event.getY();
		this.action = event.getAction();
	}
	
	private final static ElfPool mPool = new ElfPool();
	static {
		mPool.fillPool(new INewInstance(){
			public final IElfPoolItem newInstance() {
				return new ElfEvent();
			}
		}, 8);
	}
	
	public final static ElfEvent obtain(){
		return (ElfEvent)mPool.getFreshItem();
	}
	
	public final static ElfEvent obtain(MotionEvent event, float height){ 
		final ElfEvent ret = (ElfEvent)mPool.getFreshItem();
		ret.setPoint(event.getX(), height - event.getY());
		ret.action = event.getAction();
		ret.stateMask = event.getStateMask();
		
		return ret;
	}
	
	public final static void initialize(){
		mPool.fillPool(new INewInstance(){
			public final IElfPoolItem newInstance() {
				return new ElfEvent();
			}
		}, 8);
	}
	
	public void recycle() {
		mPool.recycle(this);
	}
}
