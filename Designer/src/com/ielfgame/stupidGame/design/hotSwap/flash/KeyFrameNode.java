package com.ielfgame.stupidGame.design.hotSwap.flash;

import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKey;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class KeyFrameNode extends ElfNode implements IFlashKey {

	/***
	 * public ElfColor color = new ElfColor(); public ElfPointf position = new
	 * ElfPointf(); public ElfPointf scale = new ElfPointf(1.0f, 1.0f); public
	 * boolean visible = true; public String resid = "default"; public float
	 * rotation = 0;
	 */
	private boolean frameSelect;
	private int frame;
	private InterType inter = InterType.Linear;
	private int mInterStrong = 0;
	private LoopMode mode = LoopMode.STAY;
	
	public boolean getFrameSelect() {
		return frameSelect;
	}

	public void setFrameSelect(boolean select) {
		if (!getLocked()) {
			this.frameSelect = select;
		} else {
			this.frameSelect = false;
		}
	}

	protected final static int REF_FrameSelect = FACE_ALL_SHIFT | PASTE_SHIFT | COPY_SHIFT | UNDO_SHIFT;

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		if (!getLocked()) {
			this.frame = frame;
		}
	}

	public int getFrameIndex() {
		return this.getFrame() + 1;
	}

	public void setFrameIndex(int frame) {
		this.setFrame(frame - 1);
	}

	protected final static int REF_FrameIndex = DEFAULT_SHIFT;

	public InterType getInterType() {
		return inter;
	}

	public void setInterType(InterType inter) {
		if (!getLocked()) {
			this.inter = inter;
		}
	}

	protected final static int REF_InterType = DEFAULT_SHIFT;
	
	public int getInterStrong() {
		return mInterStrong;
	}
	
	public void setInterStrong(int strong) {
		mInterStrong = strong;
	}
	
	protected final static int REF_InterStrong = DEFAULT_SHIFT;
	
	public float getInterpolation(float input) {
		if(this.inter == InterType.FlashEase) {
			return this.inter.getInterpolation(input, this.getInterStrong()/100f);
		} else {
			return this.inter.getInterpolation(input);
		}
	}

	public LoopMode getLoopMode() {
		return mode;
	}

	public void setLoopMode(LoopMode mode) {
		if (!getLocked()) {
			this.mode = mode;
		}
	}

	protected final static int REF_LoopMode = DEFAULT_SHIFT;

	public KeyFrameNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}

	public void setPropertiesFromElfNode(ElfNode node) {
		if (node != null) {
			this.setVisible(node.getVisible());
			this.setResid(node.getResid());

			this.setPosition(node.getPosition());
			this.setScale(node.getScale());
			this.setColor(node.getColor());
			this.setRotate(node.getRotate());
		}
	}

	// public KeyFrameArrayNode getParent() {
	// return (KeyFrameArrayNode)super.getParent();
	// }

	public KeyFrameArrayNode getKeyFrameArrayParent() {
		return (KeyFrameArrayNode) super.getParent();
	}

	public KeyFrameNode copyKey() {
		final KeyFrameNode ret = new KeyFrameNode(this.getParent(), this.ordinal());
		ElfNode.copyFrom(ret, this);
		return ret;
	}

	private boolean getLocked() {
		if (this.getKeyFrameArrayParent() != null) {
			return getKeyFrameArrayParent().getLocked();
		}
		return false;
	}
	
//	public void addToParentView(int newIndex) {
//		addToParent(newIndex);
//	}
}
