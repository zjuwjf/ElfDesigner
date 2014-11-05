package elfEngine.basic.node.nodeAnimate.flash;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlash;

public class FlashWorkBench { 
	private ElfNode mCurrentNode;
	private final SelectRegion mSelectRegion = new SelectRegion(null);
	private final CopyRegion mCopyRegion = new CopyRegion();
	private int mCurrentFrame;
	private IFlash mCurrentFlash;

	public void setCurrentFlash(IFlash flash) {
		mCurrentFlash = flash;
	}

	public IFlash getCurrentFlash() {
		return mCurrentFlash;
	}
	
	public int getCurrentFrame() {
		return mCurrentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.mCurrentFrame = currentFrame;
	}

	/***
	 * select-region
	 */
	
	/***
	 * Copy 
	 * 	branch
	 * 	key
	 * 
	 * Ctrl+c/v
	 */
	
	public ElfNode getCurrentNode() {
		return mCurrentNode;
	}

	public void setCurrentNode(ElfNode currentNode) {
		if(this.mCurrentNode == currentNode) {
			if(this.mCurrentNode != null) {
				
			}
			this.mCurrentNode = currentNode;
			if(this.mCurrentNode != null) {
				
			}
		}
	}
	
	public SelectRegion getSelectRegion() {
		return mSelectRegion;
	}
	
	public CopyRegion getCopyRegion() {
		return mCopyRegion;
	}
}
