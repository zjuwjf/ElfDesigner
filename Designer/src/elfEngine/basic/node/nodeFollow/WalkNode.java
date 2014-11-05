package elfEngine.basic.node.nodeFollow;

import java.util.ArrayList;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;

public class WalkNode extends ElfNode {
	public enum NextPolicy { 
		JustNext, EmptyNext,
	} 
	
	public WalkNode(ElfNode father, int ordinal) {
		super(father, ordinal); 
	} 
	
	protected NextPolicy mNextPolicy = NextPolicy.JustNext; 
	protected ArrayList<ChairNode> mCharNodes = new ArrayList<ChairNode>(); 
	protected ElfNode mForeNode; 
	protected int mCharIndex; 
	protected boolean nIsInForeground; 
	protected int mMask = 0xffffffff; 
	protected boolean mGentleMan = false; 
	
	
	public void goToChar(ChairNode chair) { 
		if(mForeNode != null) {
			if(this.getParent() != mForeNode) { 
				
			} 
		} 
	} 
	
	
	
	public void goToNextNode() { 
		// search policy
		// 1. next
		// 2. next && empty
		final ChairNode chair = getCurrentChairNode();
		if(chair != null) { 
			chair.onDriveAway(); 
		} 
		
		final int next = getNextChairNodeIndex(); 
		final int len = mCharNodes.size(); 
		if(next >=0 && next < len) { 
			final ChairNode nextChair = mCharNodes.get(next);
			nextChair.onWalkManComing(); 
			
			resetFather(mForeNode); 
			
			//follow action
		} 
	} 

	ChairNode getCurrentChairNode() {
		final int len = mCharNodes.size();
		if (mCharIndex < 0 || mCharIndex >= len) {
			return null;
		} else {
			return mCharNodes.get(mCharIndex);
		} 
	}

	int getNextChairNodeIndex() { 
		final int len = mCharNodes.size(); 
		int next = mCharIndex + 1; 
		if (next < 0 || next >= len) { 
			next = 0; 
		} 
		
		for (int i = 0; i < len; i++) { 
			final int ret = (next + i) % len; 
			final ChairNode chair = mCharNodes.get(ret); 
			if (mNextPolicy == NextPolicy.JustNext) { 
				return ret; 
			} else if (!chair.hasChaired()) { 
				return ret; 
			} 
		} 

		return -1; 
	} 
	
	public void resetFather(final ElfNode father) { 
		if (father != null && father != this.getParent()) { 
			final ElfPointf pos = this.getPositionInScreen(); 
			this.removeFromParent(); 
			this.setParent(father); 
			this.addToParent(); 
			this.setPositionInScreen(pos); 
		} 
	} 
} 
