package elfEngine.basic.touch;


public class MoveTouchDecoder extends BasicEventDecoder{
	
//	private float mMinValue = -Float.MAX_VALUE, mMaxValue = Float.MAX_VALUE;
	
	protected boolean onTouchDwon(ElfEvent event) {
		return false;
	}

	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		return false;
	}
	
	protected boolean onTouchUp(ElfEvent event) {
		return false;
	}
	
}
