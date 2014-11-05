package elfEngine.basic.node.nodeTouch.slider;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.BasicEventDecoder;
import elfEngine.basic.touch.ElfEvent;

public class SliderDecoder extends BasicEventDecoder {

	private ISlider mSlider;
	private boolean mCatched;
	
	public SliderDecoder() {
	}

	public void setDelegate(ElfNode node) {
		super.setDelegate(node);
		assert node instanceof ISlider;
		mSlider = (ISlider) node;
	} 

	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		if (!this.getCatched()) {
			return false;
		}
		
		mSlider.sliderMove(event.x, event.y);
		return true;
	}

	protected boolean onTouchDwon(ElfEvent event) { 
		if (mDelegateNode != null && mDelegateNode.isInSelectSize(event)) {
			this.setCatched(true);
			mSlider.sliderDown(event.x, event.y);
			return false;
		}
		
		return false;
	}
	
	protected boolean onTouchUp(ElfEvent event) {
		if (!this.getCatched()) {
			return false;
		}
		mSlider.sliderUp(event.x, event.y);
		return false;
	}
	
	public void setCatched(boolean b) {
		mCatched = b;
	}
	
	public boolean getCatched() {
		return mCatched;
	}

	public void reset() {
		super.reset();
		setCatched(false);
	}
}
