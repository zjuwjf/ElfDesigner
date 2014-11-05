package elfEngine.basic.node.nodeAnimate.flash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;
import elfEngine.basic.node.nodeAnimate.flash.FlashBranch.BasicFlashBranch;
import elfEngine.basic.node.nodeAnimate.flash.FlashBranch.DelegateFlashBranch;
import elfEngine.basic.node.nodeAnimate.flash.FlashKey.ADelegateFlashKey;

public class FlashModel {

	/***
	 * IFlashKey --BasicFlashKeyData
	 * 
	 * --ADelegateFlashKey --> copy, reverse --CloneDelegateFlashKey
	 * --ReverseDelegateFlashKey
	 * 
	 * IFlashBranch --BasicFlashBranch
	 * 
	 * --DelegateFlashBranch
	 * 
	 * IFlash --BasicFlash
	 * 
	 */

	public static interface ICopy {
		public ICopy copy();
	}

	public static interface IFlashKey extends ICopy {
		public IFlashKey copy();

		public int getKeyScope();

		public String getName();

		public void setFlashBranch(IFlashBranch flashBranch);

		public IFlashBranch getFlashBranch();

		public void setKeyIndex(int index);

		public int getKeyIndex();
	}

	public static interface IFlashBranch {
		public String getName();

		public void setName(String name);

		public boolean addKeyData(IFlashKey data);

		public boolean removeKeyData(IFlashKey data);

		public IFlashKey[] getKeyArray();

		public void setVisible(boolean visible);

		public boolean getVisible();

		public void setLocked(boolean locked);

		public boolean getLocked();

		public void setPropertyByTime(int time, int fps, final HashMap<IFlashBranch, ElfNode> map);

		public IFlashBranch copy();

		public void setFlash(IFlash flash);

		public IFlash getFlash();
		
		public boolean hasLinkedWithNode(); 
	}

	public static interface IFlash extends ICopy {
		
		public void addFlashBranch(IFlashBranch branch);

		public void removeFlashBranch(IFlashBranch branch);

		public IFlashBranch[] getFlashBranches();

		public void setFPS(int fps);

		public int getFPS();

		public void setMaxFrameSize(int max);

		public int getMaxFrameSize();

		public int getMaxTime();

		public void setName(String name);

		public String getName();

		public void setCurrentTime(final int time, HashMap<IFlashBranch, ElfNode> linkMap);

		public void setCurrentTime(final int time);

		public IFlash copy();

		public boolean isDependOn(IFlash flash);
		
		public boolean hasDelegateBranch();
		
		public void linkNodes(final ElfNode node, HashMap<IFlashBranch, ElfNode> linkMap);

		public void linkNodes(final ElfNode node);

		public void updateLinkNames();

		public ElfNode getLinkNode(final IFlashBranch branch);

		public void linkBranch(final IFlashBranch branch, final ElfNode node);
	
		public boolean hasLinkedWithNodes();
	}
	
	public static class ElfFlash implements IFlash {
		private final ArrayList<IFlashBranch> mFlashBranches = new ArrayList<IFlashBranch>();
		private int mFPS = 20;
		private int mMaxFrameSize = 100;
		private String mName = "unknow";
		private final HashMap<IFlashBranch, ElfNode> mLinkMap = new HashMap<IFlashBranch, ElfNode>();

		@Override
		public void linkNodes(final ElfNode node, final HashMap<IFlashBranch, ElfNode> linkMap) {
			if (node != null) {
				final IFlashBranch[] branches = getFlashBranches();
				for (final IFlashBranch branch : branches) {
					if (branch instanceof DelegateFlashBranch) {
						final DelegateFlashBranch delegateFlashBranch = (DelegateFlashBranch) branch;
						final ADelegateFlashKey[] keys = delegateFlashBranch.getKeyArray();
						for (final ADelegateFlashKey key : keys) {
							key.getDelegate().linkNodes(node, linkMap);
						}
					} else if (branch instanceof BasicFlashBranch) {
						final BasicFlashBranch basicFlashBranch = (BasicFlashBranch) branch;
						final String linkName = basicFlashBranch.getName();
						node.iterateChildsDeepWithSelf(new IIterateChilds() {
							public boolean iterate(final ElfNode node) {
								final String nodeName = node.getFullName();
								if (linkName.equals(nodeName)) {
									linkBranch(branch, node);
									return true;
								}
								return false;
							}
						});
					}
				}
			}
		}

		@Override
		public void linkNodes(ElfNode node) {
			mLinkMap.clear();
			this.linkNodes(node, mLinkMap);
		}

		@Override
		public void setCurrentTime(int time) {
			this.setCurrentTime(time, mLinkMap);
		}

		@Override
		public ElfNode getLinkNode(final IFlashBranch branch) {
			return mLinkMap.get(branch);
		}

		@Override
		public void addFlashBranch(IFlashBranch branch) {
			removeFlashBranch(branch);
			mFlashBranches.add(branch);

			branch.setFlash(this);
		}

		@Override
		public void removeFlashBranch(IFlashBranch branch) {
			mFlashBranches.remove(branch);
			linkBranch(branch, null);
			branch.setFlash(null);
		}

		@Override
		public IFlashBranch[] getFlashBranches() {
			final IFlashBranch[] ret = new IFlashBranch[mFlashBranches.size()];
			mFlashBranches.toArray(ret);
			return ret;
		}

		@Override
		public void setFPS(int fps) {
			mFPS = fps;
		}

		@Override
		public int getFPS() {
			return mFPS;
		}
		
		@Override
		public void setMaxFrameSize(int max) {
			mMaxFrameSize = max;
		}

		@Override
		public int getMaxFrameSize() {
			return mMaxFrameSize;
		}

		@Override
		public void setName(String name) {
			mName = name;
		}

		@Override
		public String getName() {
			return mName;
		}

		@Override
		public ElfFlash copy() {
			final ElfFlash ret = new ElfFlash();
			ret.setFPS(this.getFPS());
			ret.setMaxFrameSize(this.getMaxFrameSize());
			ret.setName(this.getName());

			final IFlashBranch[] bs = this.getFlashBranches();
			for (final IFlashBranch b : bs) {
				ret.addFlashBranch(b.copy());
			}
			return ret;
		}

		@Override
		public int getMaxTime() {
			return this.getMaxFrameSize() * 1000 / this.getFPS();
		}

		@Override
		public boolean isDependOn(IFlash flash) {
			final IFlashBranch [] branches = this.getFlashBranches();
			for(final IFlashBranch branch : branches) {
				if(branch instanceof DelegateFlashBranch) {
					final ADelegateFlashKey[] keys = ((DelegateFlashBranch)branch).getKeyArray();
					for(final ADelegateFlashKey key : keys) {
						if(key.getDelegate() == flash) {
							return true;
						}
					}
				}
			}
			return false;
		}
		
		@Override
		public void setCurrentTime(int time, HashMap<IFlashBranch, ElfNode> linkMap) {
			final int fps = getFPS();
			final IFlashBranch[] branches = getFlashBranches();
			for (final IFlashBranch branch : branches) {
				branch.setPropertyByTime(time, fps, linkMap);
			}
		}

		@Override
		public void updateLinkNames() {
			final Set<IFlashBranch> set = this.mLinkMap.keySet();
			for (IFlashBranch fb : set) {
				final ElfNode node = this.mLinkMap.get(fb);
				if (node != null) {
					fb.setName(node.getFullName());
				}
			}
		}

		@Override
		public void linkBranch(IFlashBranch branch, ElfNode node) {
			mLinkMap.put(branch, node);
			if (node != null) {
				branch.setName(node.getFullName());
			}
		}

		@Override
		public boolean hasDelegateBranch() {
			final IFlashBranch [] branches = this.getFlashBranches();
			for(final IFlashBranch branch : branches) {
				if(branch instanceof DelegateFlashBranch) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean hasLinkedWithNodes() {
			final IFlashBranch [] branches = this.getFlashBranches();
			for(final IFlashBranch branch : branches) {
				if(!branch.hasLinkedWithNode()) {
					return false;
				}
			}
			return true;
		}
	}

	public static class FlashManager {
		
		private final static HashSet<IFlash> sFlashMap = new HashSet<IFlash>();
		
		public static void remove(final IFlash flash) {
			sFlashMap.remove(flash);
		}

		public static void put(final IFlash flash) {
			sFlashMap.add(flash);
		}

		public static IFlash get(String name) {
			for (IFlash flash : sFlashMap) {
				final String flashName = "" + flash.getName();
				if (flashName.equals(name)) {
					return flash;
				}
			}
			return null;
		}

		public static IFlash[] getFlashArray() {
			final IFlash[] ret = new IFlash[sFlashMap.size()];
			sFlashMap.toArray(ret);
			Arrays.sort(ret, new Comparator<IFlash>() {
				public int compare(IFlash o1, IFlash o2) {
					if (o1.isDependOn(o2)) {
						return 1;
					} else if (o2.isDependOn(o1)) {
						return -1;
					} else {
						return o1.getName().compareTo(o2.getName());
					}
				}
			});
			return ret;
		}

		public static void clear() {
			sFlashMap.clear();
		}

		public static void importModels() {
			sFlashMap.clear();
		}

		public static void exportModels() {
			final IFlash[] flashes = getFlashArray();
			for (final IFlash flash : flashes) {
				if (flash != null) {
					
				}
			}
		}
	}

	// public static class FlashAction extends FiniteTimeAction {
	// protected FlashAction(float d) {
	// super(d);
	// }
	// }
}
