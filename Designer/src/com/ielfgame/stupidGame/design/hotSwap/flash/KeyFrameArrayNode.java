package com.ielfgame.stupidGame.design.hotSwap.flash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKey;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKeyArray;

import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.INodePropertyChange;
import elfEngine.basic.node.particle.modifier.MathHelper;

public class KeyFrameArrayNode extends ElfNode implements IFlashKeyArray {

	// default key frame ?
	private boolean mVisibleMask = false;
	private boolean mLocked = false;
	private ElfNode mTarget;

	public KeyFrameArrayNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setVisible(false);
	}
	
	public boolean getVisibleMask() {
		return mVisibleMask;
	}

	public void setVisibleMask(boolean visible) {
		this.mVisibleMask = visible;
		this.setFrame(mLastFp);
	}

	protected final static int REF_VisibleMask = DEFAULT_SHIFT;

	public boolean getLocked() {
		return mLocked;
	}

	public void setLocked(boolean locked) {
		this.mLocked = locked;
	}

	protected final static int REF_Locked = DEFAULT_SHIFT;
	
	public void recheckTarget() {
		this.setTarget(null);
		this.checkTarget();
	}

	public void checkTarget() {
		if (mTarget == null) {
			final ElfNode p0 = this.getParent();
			if (p0 != null) {
				final ElfNode p1 = p0.getParent();
				if (p1 != null) {
					if (p1 instanceof FlashMainNode) {
						mTarget = ((FlashMainNode) p1).findTargetBySimpleName(this.getName());
					} else {
						System.err.println("unexpected parent!!!");
					}
				}
			}
		}

		if (mTarget != null) {
			this.setName(mTarget.getName());
		}
	}

	public void setTarget(ElfNode target) {
		mTarget = target;
	}

	public ElfNode getTarget() {
		return mTarget;
	}

	private float mLastFp = 0;
	public void setFrame(float fp) {
		this.checkTarget();
		
		final KeyStorageNode ksNode = (KeyStorageNode)this.getParent();
		
		mLastFp = fp;

		if (mTarget != null && ksNode != null) {
			final INodePropertyChange npc = mTarget.getNodePropertyChange();
			mTarget.setNodePropertyChange(null);
			
			final KeyFrameNode[] ks = this.getFlashKeys();
			fp = this.getRealFP(ks, fp);

			final KeyFrameNode[] range = this.findRange(ks, fp);

			if (range != null) {
				final float a = range[0].getFrame();
				final float b = range[1].getFrame();

				float rate = (a == b) ? 0 : (fp - a) / (b - a);
				if (a != b) {
					rate = range[0].getInterpolation(rate);
				}
				
				if(ksNode.getFixedFrameEnable() && rate < 1) {
					rate = 0;
				}

				if (rate <= 0.0f) {
					mTarget.setVisible(range[0].getVisible());
					mTarget.setResid(range[0].getResid());

					mTarget.setPosition(range[0].getPosition());
					mTarget.setScale(range[0].getScale());
					mTarget.setColor(range[0].getColor());
					mTarget.setRotate(range[0].getRotate());
					
					if(ksNode.getApplyAnchor()) {
						mTarget.setAnchorPosition(range[0].getAnchorPosition());
					}
				} else if (rate >= 1.0f) {
					mTarget.setVisible(range[1].getVisible());
					mTarget.setResid(range[1].getResid());

					mTarget.setPosition(range[1].getPosition());
					mTarget.setScale(range[1].getScale());
					mTarget.setColor(range[1].getColor());
					mTarget.setRotate(range[1].getRotate());
					
					if(ksNode.getApplyAnchor()) {
						mTarget.setAnchorPosition(range[1].getAnchorPosition());
					}
				} 
				else if(range[1].getFrame() == range[0].getFrame()+1 && ksNode.getFixedFrameEnable()) {
					mTarget.setVisible(range[0].getVisible());
					mTarget.setResid(range[0].getResid());
					
					mTarget.setPosition(range[0].getPosition());
					mTarget.setScale(range[0].getScale());
					mTarget.setColor(range[0].getColor());
					mTarget.setRotate(range[0].getRotate());
					
					if(ksNode.getApplyAnchor()) {
						mTarget.setAnchorPosition(range[0].getAnchorPosition());
					}
				}
				else {
					mTarget.setVisible(range[0].getVisible());
					mTarget.setResid(range[0].getResid());

					mTarget.setPosition(ElfPointf.between(range[0].getPosition(), range[1].getPosition(), rate));
					mTarget.setScale(ElfPointf.between(range[0].getScale(), range[1].getScale(), rate));
					mTarget.setColor(ElfColor.between(range[0].getColor(), range[1].getColor(), rate));
					mTarget.setRotate(MathHelper.between(range[0].getRotate(), range[1].getRotate(), rate));
					
					if(ksNode.getApplyAnchor()) {
						mTarget.setAnchorPosition(ElfPointf.between(range[0].getAnchorPosition(), range[1].getAnchorPosition(), rate));
					}
				}
			}
			
			if (this.mVisibleMask) {
				mTarget.setVisible(false);
			}
			
			mTarget.setNodePropertyChange(npc);
		}
	}

	private final float getRealFP(final KeyFrameNode[] ks, float fp) {
		if (ks.length <= 1) {
			return fp;
		} else {
			// final int startIndex = 0;
			final int endIndex = ks.length - 1;

			// final KeyFrameNode startKey = ks[startIndex];
			final KeyFrameNode endKey = ks[endIndex];

			// final int startFrame = startKey.getFrame();
			final int endFrame = endKey.getFrame();

			if (fp > endKey.getFrame()) {
				final LoopMode lp = endKey.getLoopMode();
				switch (lp) {
				case LOOP:
					// fp = startFrame + (fp - endFrame) / (endFrame -
					// startFrame);
					fp = fp % endFrame;
					break;
				case RELOOP:
					// not supported
					System.err.println("RELOOP Not Supported!");
					fp = endFrame;
					break;
				case ENDLESS:
					System.err.println("RELOOP Not Supported!");
					fp = endFrame;
					break;
				case STAY:
					fp = endFrame;
					break;
				default:
					break;
				}
			}
		}

		return fp;
	}

	private final KeyFrameNode[] findRange(final KeyFrameNode[] ks, float fp) {
		if (ks.length == 0) {
			return null;
		} else if (ks.length == 1) {
			return new KeyFrameNode[] { ks[0], ks[0] };
		}

		int index = -1;

		for (int i = 0; i < ks.length; i++) {
			final KeyFrameNode kfd = ks[i];
			if (kfd.getFrame() >= fp) {
				index = i;
				break;
			}
		}

		if (index == 0) {
			return new KeyFrameNode[] { ks[0], ks[0] };
		} else if (index > 0) {
			return new KeyFrameNode[] { ks[index - 1], ks[index] };
		}

		return null;
	}

	public KeyFrameNode[] getFlashKeys() {
		final ArrayList<KeyFrameNode> arrayList = new ArrayList<KeyFrameNode>();
		final ElfNode[] nodes = this.getChilds();
		for (ElfNode node : nodes) {
			if (node instanceof KeyFrameNode) {
				arrayList.add((KeyFrameNode) node);
			}
		}

		// sort
		Collections.sort(arrayList, new Comparator<KeyFrameNode>() {
			public int compare(KeyFrameNode o1, KeyFrameNode o2) {
				return o1.getFrame() - o2.getFrame();
			}
		});

		for (KeyFrameNode kfn : arrayList) {
			kfn.setName("#" + kfn.getFrame());
			kfn.reorder(kfn.getFrame());
		}

		return arrayList.toArray(new KeyFrameNode[arrayList.size()]);
	}

	public int[] getRange() {
		final KeyFrameNode[] kfns = this.getFlashKeys();
		if (kfns.length == 0) {
			return null;
		} else if (kfns.length == 1) {
			return new int[] { kfns[0].getFrame(), kfns[0].getFrame() };
		} else {
			return new int[] { kfns[0].getFrame(), kfns[kfns.length - 1].getFrame() };
		}
	}

	public boolean translateKey(IFlashKey key, int diff, boolean apply) {
		if(this.getLocked()) {
			return false;
		}
		
		assert (key instanceof KeyFrameNode);
		final KeyFrameNode data = (KeyFrameNode) key;
		assert (data.getParent() == this);

		final int newframe = data.getFrame() + diff;
		final KeyFrameNode[] kfns = this.getFlashKeys();

		for (final KeyFrameNode pData : kfns) {
			if (pData != data && pData.getFrame() == newframe) {
				return false;
			}
		}
		if (apply) {
			data.setFrame(newframe);
		}

		return true;
	}

	public boolean removeFlashKey(IFlashKey key) {
		if(this.getLocked()) {
			return false;
		}
		
		assert (key instanceof KeyFrameNode);
		final KeyFrameNode data = (KeyFrameNode) key;
		assert (data.getParent() == this);
		data.removeFromParentView(false);
		return true;
	}

	public boolean addFlashKey(IFlashKey key) {
		if(this.getLocked()) {
			return false;
		}
		
		assert (key instanceof KeyFrameNode);
		final KeyFrameNode data = (KeyFrameNode) key;
		assert (data.getParent() == null || data.getParent() == this);

		final KeyFrameNode old = this.findFlashKeyByFrame(data.getFrame());
		if (old != null) {
			return false;
		}

		data.setParent(this);
		data.addToParentView();

		return true;
	}

	public String getTargetName() {
		final ElfNode target = getTarget();
		if (target != null) {
			return target.getName();
		}
		return null;
	}

	public KeyFrameNode findFlashKeyByFrame(int frame) {
		final KeyFrameNode[] kfns = this.getFlashKeys();
		for (KeyFrameNode kfn : kfns) {
			if (kfn.getFrame() == frame) {
				return kfn;
			} else if (kfn.getFrame() > frame) {
				return null;
			}
		}
		return null;
	}

	public void shiftAdd(IFlashKey ikey) {
		if(this.getLocked()) {
			return ;
		}
		
		assert (ikey instanceof KeyFrameNode);
		final KeyFrameNode key = (KeyFrameNode) ikey;
		assert (key.getParent() == this);

		key.setFrameSelect(!key.getFrameSelect());
		if (key.getFrameSelect()) {
			KeyFrameNode[] kfns = this.getFlashKeys();

			int start = -1;
			int end = -1;

			for (int i = 0; i < kfns.length; i++) {
				final KeyFrameNode k = kfns[i];
				if (k == key) {
					end = i;
					break;
				} else if (k.getFrameSelect()) {
					start = i;
				}
			}

			if (start >= 0 && end >= 0) {
				for (int i = start + 1; i < end; i++) {
					final KeyFrameNode k = kfns[i];
					k.setFrameSelect(true);
				}
			}
		}
	}

	public KeyFrameNode createFlashKeyByFrame(int frame) {
		if(this.getLocked()) {
			return null;
		}
		
		if (this.findFlashKeyByFrame(frame) == null) {
			final KeyFrameNode kfn = new KeyFrameNode(this, frame);
			kfn.setFrame(frame);

			return kfn;
		}
		return null;
	}
	
//	public void addToParentView(int newIndex) {
//		addToParent(newIndex);
//	}

	// public void addChild(ElfNode child) {
	// if(child instanceof KeyFrameNode) {
	// final KeyFrameNode newF = (KeyFrameNode)child;
	// final KeyFrameNode oldF = this.findFlashKeyByFrame( newF.getFrame() );
	// if(oldF != null) {
	// return ;
	// }
	// super.addChild(child);
	// }
	// }
	//
	// public void addChild(ElfNode child, int index) {
	// if(child instanceof KeyFrameNode) {
	// final KeyFrameNode newF = (KeyFrameNode)child;
	// final KeyFrameNode oldF = this.findFlashKeyByFrame( newF.getFrame() );
	// if(oldF != null) {
	// return ;
	// }
	// super.addChild(child);
	// }
	// }
}
