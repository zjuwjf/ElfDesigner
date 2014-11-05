package elfEngine.basic.node.nodeFollow;

import elfEngine.basic.node.ElfNode;


public class ChairNode extends ElfNode{ 
	//walkMan
	public ChairNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
	} 
	
	protected boolean mBeforehandDriveAway ;
	protected WalkNode mWalkNode;
	
	public void onDragOver(WalkNode walkNode) { 
	} 
	public void onDragLeave(WalkNode walkNode) { 
	} 
	
	public boolean hasChaired() { 
		return mWalkNode != null;
	} 
	
	public WalkNode getChairedWalkMan() {
		return mWalkNode;
	}
	
	//private boolean 
	public void onAccept(WalkNode walkNode) { 
		mWalkNode = walkNode;
		if(mWalkNode != null) { 
			mWalkNode.resetFather(this);
			//
		} 
	}
	public WalkNode onDriveAway() { 
		final WalkNode ret = mWalkNode;
		mWalkNode = null;
		if(ret != null) { 
			ret.goToNextNode();
		} 
		return ret;
	} 
	
	public void onWalkManComing() { 
		if(mBeforehandDriveAway) { 
			onDriveAway();
		} 
	} 
	public void onWalkManCame(WalkNode walkNode) { 
		onDriveAway();
		onAccept(walkNode);
	} 
	
} 
