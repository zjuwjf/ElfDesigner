package elfEngine.basic.node.nodeAnimate.flash;

import elfEngine.basic.node.ClipNode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeLayout.LinearLayoutNode;
import elfEngine.basic.node.nodeList.ListNode;


public class FlashNode extends ElfNode {
	
	public FlashNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	/***
	 * flash-list
	 * 
	 * 		basic-flash
	 * 		-
	 * 		
	 * 
	 * 		combine-flash
	 * 		-
	 * 		
	 */
	
	/***
	 * branch-list
	 * 		
	 * 		
	 * 		
	 * 
	 */
	
	/***
	 * time-ruler
	 * 
	 * 
	 * 
	 * 
	 */
	
	protected final LinearLayoutNode mRootLayoutNode = new LinearLayoutNode(this, 0);
	
	protected final ListNode mFlashList = new ListNode(mRootLayoutNode, 0);
	
	protected final LinearLayoutNode mTimeLayoutNode = new LinearLayoutNode(mRootLayoutNode, 0);
	
	protected final ListNode mBranchList = new ListNode(mTimeLayoutNode, 0);
	
	protected final TimeRulerNode mTimeRulerTop = new TimeRulerNode(mTimeLayoutNode, 0);
	
	protected void init() {
		
		mRootLayoutNode.addToParent();
		
		mFlashList.addToParent();
		
		mTimeLayoutNode.addToParent();
		
		mBranchList.addToParent();
		
		mTimeRulerTop.addToParent();
		
	}
	
	public static class TimeRulerNode extends ClipNode {
		protected int mPixelPerFrame = 10;
		protected int mStartTime = 0;
		
		public void setStartTime(int time) {
			mStartTime = time;
		}
		
		public void setPixelPerFrame(int pixel) {
			mPixelPerFrame = pixel;
		}
		
		public TimeRulerNode(ElfNode father, int ordinal) {
			super(father, ordinal);
		}
		
		public void drawSelf() {
//			TextNode ;
			//draw lines
		}
	}
}
