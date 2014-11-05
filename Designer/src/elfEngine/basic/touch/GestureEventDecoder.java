package elfEngine.basic.touch;

import elfEngine.basic.node.ElfNode;


public class GestureEventDecoder extends BasicEventDecoder{

	public interface IOnFling { 
		public void onFling(float distanceX, float distanceY, float velocityX,  float velocityY); 
		public boolean isInSelectSize(ElfEvent event); 
	}
	
	private IOnFling mIOnFling;
	
	public void setDelegate(IOnFling onFling) {
		mIOnFling = onFling;
	}
	
	private long mStartTime = -1; 
	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		return false; 
	} 
	
	public GestureEventDecoder(IOnFling onFling, ElfNode node) {
		setDelegate(onFling); 
		setDelegate(node); 
	} 
	
	@Override
	protected boolean onTouchDwon(ElfEvent event) {
		if(mIOnFling.isInSelectSize(event)) {
			mStartTime = System.currentTimeMillis();
			return true;
		} else { 
			mStartTime = -1L;
			return false;
		} 
	} 
	
	@Override
	protected boolean onTouchUp(ElfEvent event) {
		if(mStartTime > 0L) {
			final float elapsed = (System.currentTimeMillis()- mStartTime)/1000f;
			final float distanceX = this.getTotalMoveX(event);
			final float distanceY = this.getTotalMoveY(event);
			final float velocityX = distanceX/elapsed;
			final float velocityY = distanceY/elapsed;
			mIOnFling.onFling(distanceX, distanceY, velocityX, velocityY);
			return false;
		} 
		
		mStartTime = -1L;
		return false;
	} 
} 
