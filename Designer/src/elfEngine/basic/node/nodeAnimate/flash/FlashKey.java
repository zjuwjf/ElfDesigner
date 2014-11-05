package elfEngine.basic.node.nodeAnimate.flash;

import java.util.HashMap;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.flash.FlashBranch.BasicFlashBranch;
import elfEngine.basic.node.nodeAnimate.flash.FlashBranch.DelegateFlashBranch;
import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlash;
import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlashBranch;
import elfEngine.basic.node.nodeAnimate.flash.FlashModel.IFlashKey;

public class FlashKey { 
	
	public static class BasicFlashKey implements IFlashKey {
		private final ElfPointf mPosition = new ElfPointf();
		private final ElfPointf mScale = new ElfPointf();
		private final ElfColor mColor = new ElfColor();
		private float mRotate;
		private boolean mVisible = true;
		private String mResid;
		private InterType mPositionInterType = InterType.Linear;
		private InterType mScaleInterType = InterType.Linear;
		private InterType mRotateInterType = InterType.Linear;
		private InterType mColorInterType = InterType.Linear;
		private BasicFlashBranch mParent;
		private int mKeyIndex;

		public void setPosition(ElfPointf pos) {
			mPosition.set(pos);
		}

		public ElfPointf getPosition() {
			return mPosition;
		}

		public void setPositionInterType(InterType type) {
			mPositionInterType = type;
		}

		public InterType getPositionInterType() {
			return mPositionInterType;
		}

		public void setRotate(float rotate) {
			mRotate = rotate;
		}

		public float getRotate() {
			return mRotate;
		}

		public void setRotateInterType(InterType type) {
			mRotateInterType = type;
		}

		public InterType getRotateInterType() {
			return mRotateInterType;
		}

		public void setScale(ElfPointf scale) {
			mScale.set(scale);
		}

		public ElfPointf getScale() {
			return mScale;
		}

		public void setScaleInterType(InterType type) {
			mScaleInterType = type;
		}

		public InterType getScaleInterType() {
			return mScaleInterType;
		}

		public void setColor(ElfColor color) {
			mColor.set(color);
		}

		public ElfColor getColor() {
			return mColor;
		}

		public void setColorInterType(InterType type) {
			mColorInterType = type;
		}

		public InterType getColorInterType() {
			return mColorInterType;
		}

		public boolean getVisible() {
			return mVisible;
		}

		public void setVisible(boolean visible) {
			mVisible = visible;
		}

		public void setResid(String resid) {
			mResid = resid;
		}

		public String getResid() {
			return mResid;
		}

		public void setFlashBranch(IFlashBranch flashBranch) {
			if (flashBranch instanceof BasicFlashBranch) {
				mParent = (BasicFlashBranch) flashBranch;
			}
		}

		public BasicFlashBranch getFlashBranch() {
			return mParent;
		}

		public void setKeyIndex(int index) {
			mKeyIndex = index;
		}

		public int getKeyIndex() {
			return mKeyIndex;
		}

		public BasicFlashKey copy() {
			final BasicFlashKey ret = new BasicFlashKey();
			ret.setColor(this.getColor());
			ret.setColorInterType(this.getColorInterType());
			ret.setPosition(this.getPosition());
			ret.setPositionInterType(this.getPositionInterType());
			ret.setScale(this.getScale());
			ret.setScaleInterType(this.getScaleInterType());
			ret.setRotate(this.getRotate());
			ret.setRotateInterType(this.getRotateInterType());
			ret.setVisible(this.getVisible());
			ret.setResid(this.getResid());
			ret.setKeyIndex(this.getKeyIndex());
			ret.setFlashBranch(this.getFlashBranch());
			return ret;
		}

		public int getKeyScope() {
			return 1;
		}

		public String getName() {
			return null;
		}
	}
	
	public static abstract class ADelegateFlashKey implements IFlashKey {
		private DelegateFlashBranch mDelegateFlashBranch;
		private IFlash mDelegate;
		private int mKeyIndex;

		public ADelegateFlashKey(IFlash flashActionData) {
			mDelegate = flashActionData;
		}

		public void setDelegate(IFlash delegate) {
			mDelegate = delegate;
		}

		public IFlash getDelegate() {
			return mDelegate;
		}

		public int getMaxFrameSize() {
			return mDelegate.getMaxFrameSize();
		}

		public int getMaxTime() {
			return mDelegate.getMaxTime();
		}

		public void setName(String name) {
			mDelegate.setName(name);
		}

		@Override
		public String getName() {
			return mDelegate.getName();
		}

		public int getKeyScope() {
			return mDelegate.getMaxFrameSize();
		}

		public void setFlashBranch(IFlashBranch flashBranch) {
			if (flashBranch instanceof DelegateFlashBranch) {
				mDelegateFlashBranch = (DelegateFlashBranch) flashBranch;
			}
		}

		public DelegateFlashBranch getFlashBranch() {
			return mDelegateFlashBranch;
		}

		public void setKeyIndex(int index) {
			mKeyIndex = index;
		}

		public int getKeyIndex() {
			return mKeyIndex;
		}

		public abstract ADelegateFlashKey copy();

		public abstract ADelegateFlashKey reverse();

		public abstract void setCurrentTime(int time, final HashMap<IFlashBranch, ElfNode> map);
	}

	public static class CloneFlashKey extends ADelegateFlashKey {

		public CloneFlashKey(IFlash flashActionData) {
			super(flashActionData);
		}

		@Override
		public void setCurrentTime(int time, final HashMap<IFlashBranch, ElfNode> map) {
			this.getDelegate().setCurrentTime(time, map);
		}

		@Override
		public CloneFlashKey copy() {
			return new CloneFlashKey(this.getDelegate());
		}

		@Override
		public ReverseFlashKey reverse() {
			return new ReverseFlashKey(this.getDelegate());
		}

		public String getName() {
			return "C-" + this.getDelegate().getName();
		}
	}

	public static class ReverseFlashKey extends ADelegateFlashKey {
		public ReverseFlashKey(IFlash flashActionData) {
			super(flashActionData);
		}
		
		@Override
		public void setCurrentTime(int time, final HashMap<IFlashBranch, ElfNode> map) {
			this.getDelegate().setCurrentTime(this.getDelegate().getMaxTime() - time, map);
		}
		
		@Override
		public ReverseFlashKey copy() {
			return new ReverseFlashKey(this.getDelegate());
		}

		@Override
		public CloneFlashKey reverse() {
			return new CloneFlashKey(this.getDelegate());
		}

		public String getName() {
			return "R-" + this.getDelegate().getName();
		}
	}
}
