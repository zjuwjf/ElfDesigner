package elfEngine.basic.node.nodeAnimate.flash;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.flash.FlashKey.ADelegateFlashKey;
import elfEngine.basic.node.nodeAnimate.flash.FlashKey.BasicFlashKey;
import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlash;
import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlashBranch;
import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlashKey;
import elfEngine.basic.node.particle.modifier.MathHelper;

public class FlashBranch {
	public static class BasicFlashBranch implements IFlashBranch {
		private String mName = "Empty";
		private IFlash mFlash;
		private final LinkedList<BasicFlashKey> mKeyDatas = new LinkedList<BasicFlashKey>();
		private boolean mVisible = true;
		private boolean mLocked;

		@Override
		public String getName() {
			return mName;
		}

		@Override
		public void setName(String name) {
			mName = name;
		}

		@Override
		public boolean addKeyData(IFlashKey data) {
			if (data instanceof BasicFlashKey) {
				removeKeyData(data);
				((BasicFlashKey) data).setFlashBranch(this);
				return mKeyDatas.add((BasicFlashKey) data);
			}
			return false;
		}

		@Override
		public boolean removeKeyData(IFlashKey data) {
			if (data instanceof BasicFlashKey) {
				((BasicFlashKey) data).setFlashBranch(null);
				return mKeyDatas.remove(data);
			}
			return false;
		}

		@Override
		public BasicFlashKey[] getKeyArray() {
			final BasicFlashKey[] ret = new BasicFlashKey[mKeyDatas.size()];
			mKeyDatas.toArray(ret);
			
			Arrays.sort(ret, new Comparator<BasicFlashKey>() {
				public int compare(BasicFlashKey o1, BasicFlashKey o2) {
					return o1.getKeyIndex() - o2.getKeyIndex();
				}
			});
			
			return ret;
		}

		@Override
		public void setVisible(boolean visible) {
			mVisible = visible;
		}

		@Override
		public boolean getVisible() {
			return mVisible;
		}

		@Override
		public void setLocked(boolean locked) {
			mLocked = locked;
		}

		@Override
		public boolean getLocked() {
			return mLocked;
		}

		@Override
		public void setPropertyByTime(int time, int fps, final HashMap<IFlashBranch, ElfNode> map) {
			final ElfNode node = map.get(this);
			if (node != null) {
				final BasicFlashKey[] keys = this.getKeyArray();
				BasicFlashKey before = null, after = null;
				for (int i = 0; i < keys.length; i++) {
					final BasicFlashKey key = keys[i];
					final float keyTime = key.getKeyIndex() * 1000f / fps;
					if (keyTime <= time) {
						before = key;
					} else if (keyTime > time) {
						after = key;
						break;
					}
				}

				if (before != null && after != null) {
					final float keyBeforeTime = before.getKeyIndex() * 1000f / fps;
					final float keyAfterTime = after.getKeyIndex() * 1000f / fps;
					final float r = (time - keyBeforeTime) / (keyAfterTime - keyBeforeTime);
					// visible
					node.setVisible(before.getVisible());
					node.setResid(before.getResid());

					node.setPosition(ElfPointf.between(before.getPosition(), after.getPosition(), before.getPositionInterType().getInterpolation(r)));
					node.setScale(ElfPointf.between(before.getScale(), after.getScale(), before.getScaleInterType().getInterpolation(r)));
					node.setColor(ElfColor.between(before.getColor(), after.getColor(), before.getColorInterType().getInterpolation(r)));
					node.setRotate(MathHelper.between(before.getRotate(), after.getRotate(), before.getRotateInterType().getInterpolation(r)));
				} else if (before != null && after == null) {
					// stay
					node.setVisible(before.getVisible());
					node.setResid(before.getResid());

					node.setPosition(before.getPosition());
					node.setScale(before.getScale());
					node.setColor(before.getColor());
					node.setRotate(before.getRotate());
				} else if (before == null && after != null) {
					// stay
					node.setVisible(after.getVisible());
					node.setResid(after.getResid());

					node.setPosition(after.getPosition());
					node.setScale(after.getScale());
					node.setColor(after.getColor());
					node.setRotate(after.getRotate());
				} else {
					// do nothing
				}
			}
		}

		@Override
		public BasicFlashBranch copy() {
			final BasicFlashBranch ret = new BasicFlashBranch();
			ret.setLocked(this.getLocked());
			ret.setVisible(this.getVisible());
			ret.setName(this.getName());
			final BasicFlashKey[] datas = this.getKeyArray();
			for (final BasicFlashKey data : datas) {
				ret.addKeyData(data.copy());
			}
			return ret;
		}

		public BasicFlashKey getBasicKeyDataByKey(final int key) {
			final BasicFlashKey[] datas = this.getKeyArray();
			for (final BasicFlashKey data : datas) {
				if (data.getKeyIndex() == key) {
					return data;
				}
			}
			return null;
		}

		public boolean addKeyData(final int key, final ElfNode node) {
			if (getBasicKeyDataByKey(key) == null) {
				// final ElfNode node = this.getTarget();
				if (node != null) {
					final BasicFlashKey data = new BasicFlashKey();
					data.setColor(node.getColor());
					data.setPosition(node.getPosition());
					data.setResid(node.getResid());
					data.setRotate(node.getRotate());
					data.setScale(node.getScale());
					data.setVisible(node.getVisible());
					data.setKeyIndex(key);
					return addKeyData(data);
				}
			}

			return false;
		}

		@Override
		public void setFlash(IFlash flash) {
			mFlash = flash;
		}

		@Override
		public IFlash getFlash() {
			return mFlash;
		}

		@Override
		public boolean hasLinkedWithNode() {
			final IFlash flash = getFlash();
			if(flash != null) {
				return flash.getLinkNode(this) != null;
			}
			return false;
		}
	}
	
	public static class DelegateFlashBranch implements IFlashBranch {
		private final LinkedList<ADelegateFlashKey> mKeyDatas = new LinkedList<ADelegateFlashKey>();
		private IFlash mFlash;
		private String mName = "EmptyDelegate";

		public String getName() {
			return mName;
		}

		public void setName(String name) {
			mName = name;
		}

		@Override
		public boolean addKeyData(IFlashKey data) {
			if (data instanceof ADelegateFlashKey) {
				removeKeyData(data);
				return mKeyDatas.add((ADelegateFlashKey) data);
			}
			return false;
		}

		@Override
		public boolean removeKeyData(IFlashKey data) {
			if (data instanceof ADelegateFlashKey) {
				return mKeyDatas.remove(data);
			}
			return false;
		}

		@Override
		public ADelegateFlashKey[] getKeyArray() {
			final ADelegateFlashKey[] ret = new ADelegateFlashKey[mKeyDatas.size()];
			mKeyDatas.toArray(ret);
			
			Arrays.sort(ret, new Comparator<ADelegateFlashKey>() {
				public int compare(ADelegateFlashKey o1, ADelegateFlashKey o2) {
					return o1.getKeyIndex() - o2.getKeyIndex();
				}
			});
			
			return ret;
		}

		@Override
		public void setVisible(boolean visible) {
		}

		@Override
		public boolean getVisible() {
			return true;
		}

		@Override
		public void setLocked(boolean locked) {
		}

		@Override
		public boolean getLocked() {
			return false;
		}

		@Override
		public void setPropertyByTime(int time, int fps, final HashMap<IFlashBranch, ElfNode> map) {
			final ADelegateFlashKey[] fs = this.getKeyArray();
			final float rate = 1000f / fps;
			for (final ADelegateFlashKey f : fs) {
				final int startKey = f.getKeyIndex();
				final float startTime = startKey * rate;
				final float endTime = startTime + f.getMaxTime();
				if (startTime <= time && time <= endTime) {
					f.setCurrentTime((int) (time - startTime),  map);
					break;
				}
			}
		}

		@Override
		public DelegateFlashBranch copy() {
			final DelegateFlashBranch ret = new DelegateFlashBranch();
			final ADelegateFlashKey[] fs = getKeyArray();
			for (final ADelegateFlashKey f : fs) {
				ret.addKeyData(f.copy());
			}
			ret.setName(this.getName());
			return ret;
		}

		@Override
		public void setFlash(IFlash flash) {
			mFlash = flash;
		}

		@Override
		public IFlash getFlash() {
			return mFlash;
		}

		@Override
		public boolean hasLinkedWithNode() {
			return false;
		}
	}
}
