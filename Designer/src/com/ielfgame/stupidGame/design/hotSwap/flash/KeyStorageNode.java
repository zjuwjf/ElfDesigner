package com.ielfgame.stupidGame.design.hotSwap.flash;

import java.util.ArrayList;

import elfEngine.basic.node.ElfNode;

public class KeyStorageNode extends ElfNode {
	
	private int fps = 20;
	private int maxF = 40;
	private int mTime = 0;
	private float mSpeedRate = 1.0f;
	
	private boolean mApplyAnchor = false;
	private boolean mFixedFrameEnabe = false;

	public KeyStorageNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		this.setVisible(false);
		this.setPaused(true);
	}
	
	public void setApplyAnchor(boolean b) {
		mApplyAnchor = b;
	}
	
	public boolean getApplyAnchor() {
		return mApplyAnchor;
	}
	protected final static int REF_ApplyAnchor = DEFAULT_SHIFT;
	
	public void setFixedFrameEnable(boolean b) {
		mFixedFrameEnabe = b;
	}
	
	public boolean getFixedFrameEnable() {
		return mFixedFrameEnabe;
	}
	
	protected final static int REF_FixedFrameEnable = DEFAULT_SHIFT;
	
	public void setSpeedRate(float rate) {
		if(rate >= 0) {
			mSpeedRate = rate;
		}
	}
	
	public float getSpeedRate() {
		return mSpeedRate;
	}
	
	protected final static int REF_SpeedRate = DEFAULT_SHIFT;
	
	public int getFPS() {
		return fps;
	}

	public void setFPS(int fps) {
		if (fps > 0)
			this.fps = fps;
	}
	protected final static int REF_FPS = DEFAULT_SHIFT;

	public int getMaxF() {
		return maxF;
	}

	public void setMaxF(int maxF) {
		if (maxF > 0)
			this.maxF = maxF;
	}
	protected final static int REF_MaxF = DEFAULT_SHIFT;
	
	public int getProgressTime() {
		return mTime;
	}

	public void setProgressTime(int mills) {
		if(mills >= 0) {
			mTime = mills;
			final float fp;
			
			if(mTime > getLoopTime()) {
				mTime = mTime % getLoopTime();
				fp = (mills * this.getFPS() / 1000.0f);
			} else if(mTime == getLoopTime()) {
				fp = this.getMaxF();
			} else {
				fp = (mills * this.getFPS() / 1000.0f);
			}
			
			if(this.getFixedFrameEnable()) {
				updateFlash( Math.round(fp-0.4999999f) );
			} else { 
				updateFlash( fp );
			} 
		}
	}
	protected final static int REF_ProgressTime = DEFAULT_SHIFT;
	
	public int getLoopTime() {
		return Math.round( this.getMaxF() * (1000.0f/this.getFPS()) );
	}
	
	public KeyFrameArrayNode[] getKeyFrameArrayNodes() {
		final ArrayList<KeyFrameArrayNode> arrayList = new ArrayList<KeyFrameArrayNode>();
		final ElfNode [] nodes = this.getChilds();
		for(final ElfNode node : nodes) {
			if(node instanceof KeyFrameArrayNode) {
				arrayList.add((KeyFrameArrayNode)node);
			}
		}
		
		return arrayList.toArray(new KeyFrameArrayNode[arrayList.size()]);
	}
	
	public KeyFrameArrayNode findFlashKeyArrayByTarget(ElfNode target) {
		final KeyFrameArrayNode[] array = this.getKeyFrameArrayNodes();
		for (KeyFrameArrayNode kfa : array) {
			if (kfa.getTarget() == target) {
				return kfa;
			}
		}
		return null;
	}
	
	public KeyFrameArrayNode findFlashKeyArrayByTargetName(String target) {
		final KeyFrameArrayNode[] array = this.getKeyFrameArrayNodes();
		for (KeyFrameArrayNode kfa : array) {
			if (kfa.getName().equals(target)) {
				return kfa;
			}
		}
		return null;
	}
	
	private void updateFlash(final float fp) {
		final KeyFrameArrayNode[] kfas = this.getKeyFrameArrayNodes();
		for (final KeyFrameArrayNode kfa : kfas) {
			kfa.setFrame(fp);
		}
	}
	
	public KeyFrameArrayNode findKeyFrameArrayNodeByTarget(final ElfNode node) {
		final KeyFrameArrayNode [] array = this.getKeyFrameArrayNodes();
		for(KeyFrameArrayNode a : array) {
			if(a.getTarget() == node) {
				return a;
			}
		}
		return null;
	}
	
	public void recheckTargets() {
		final KeyFrameArrayNode[] kfa = getKeyFrameArrayNodes();
		for(KeyFrameArrayNode n : kfa) {
			n.recheckTarget();
		}
	}
	
//	public void addToParentView(int newIndex) {
//		addToParent(newIndex);
//	}

}
