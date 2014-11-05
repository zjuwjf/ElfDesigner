package elfEngine.basic.touch;

public class ShieldEventDecoder extends BasicEventDecoder{

	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		if(mDelegateNode.isInSelectSize(event)) {
			return true;
		}
		return false;
	}

	protected boolean onTouchDwon(ElfEvent event) {
		if(mDelegateNode.isInSelectSize(event)) {
			return true;
		}
		return false;
	}

	protected boolean onTouchUp(ElfEvent event) {
		if(mDelegateNode.isInSelectSize(event)) {
			return false;
		}
		return false;
	}

}
