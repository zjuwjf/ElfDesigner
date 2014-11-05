package elfEngine.basic.node.bar;

import java.util.LinkedList;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.ExtendMode;
import com.ielfgame.stupidGame.enumTypes.LayoutMode;
import com.ielfgame.stupidGame.platform.PlatformHelper;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.bar.ProgressNode.ProgressTimerType;
import elfEngine.opengl.BlendMode;

public class BarNode extends ElfNode implements IBarNode { 
	
	private final ElfNode mLeft = new ElfNode(this, 0), mRight = new ElfNode(
			this, 0);
	private final ProgressNode mMiddle = new ProgressNode(this, 0);

	private LinkedList<ElfNode> mControls = new LinkedList<ElfNode>();
	private LayoutMode mLayoutMode = LayoutMode.Left2Right;
	
	public LayoutMode getLayoutMode() {
		return mLayoutMode;
	}

	public void setLayoutMode(LayoutMode mLayoutPrior) {
		if (mLayoutPrior != null && mLayoutPrior != this.mLayoutMode) {
			this.mLayoutMode = mLayoutPrior;
			updateChildPos();
		}
	}

	public void setBlendMode(BlendMode blendMode) {
		super.setBlendMode(blendMode);
		mLeft.setBlendMode(blendMode);
		mMiddle.setBlendMode(blendMode);
		mRight.setBlendMode(blendMode);
	}
	
	// reflect
	protected static final int REF_LayoutMode = FACE_ALL_SHIFT + XML_ALL_SHIFT;

	private ExtendMode mExtendMode = ExtendMode.Both;

	public ExtendMode getExtendMode() {
		return mExtendMode;
	}

	public void setExtendMode(ExtendMode mExtendMode) {
		if (mExtendMode != null && mExtendMode != this.mExtendMode) {
			this.mExtendMode = mExtendMode;
			updateChildPos();
		}
	}

	// reflect
	protected static final int REF_ExtendMode = FACE_ALL_SHIFT + XML_ALL_SHIFT;

	protected void updateSize() {
		final float[] selectWidth = new float[1], selectHeight = new float[1];
		selectWidth[0] = selectHeight[0] = 0;

		switch (mLayoutMode) {
		case Left2Right:
		case Right2Left:
			for (ElfNode node : mControls) {
				selectWidth[0] += node.getWidth();
				selectHeight[0] = Math.max(selectHeight[0], node.getHeight());
			}
			break;
		case Bottom2Top:
		case Top2Bottom:
			for (ElfNode node : mControls) {
				selectWidth[0] = Math.max(selectWidth[0], node.getWidth());
				selectHeight[0] += node.getHeight();
			}
			break;
		}

		setSize(selectWidth[0], selectHeight[0]);
	}

	public BarNode(ElfNode father, int ordinal) {
		super(father, ordinal);

		setName("#bar");

		mLeft.setDefaultWidth(0);
		mLeft.setDefaultHeight(0);

		mMiddle.setDefaultWidth(0);
		mMiddle.setDefaultHeight(0);

		mRight.setDefaultWidth(0);
		mRight.setDefaultHeight(0);

		mControls.add(mLeft);
		mControls.add(mMiddle);
		mControls.add(mRight);

		setUseSettedSize(true);
	}

	public String[] getSelfResids() {
		return new String[] { getPic0(), getPic1(), getPic2() };
	}
	
	public void setSelfResids(final String[] resids) {
		if(resids != null) {
			if(resids.length > 0) {
				setPic0(resids[0]);
			}
			if(resids.length > 1) {
				setPic1(resids[1]);
			}
			if(resids.length > 2) {
				setPic2(resids[2]);
			}
		}
	}

	public void setGLid(int id) {
		super.setGLid(id);
		mLeft.setGLid(id);
		mMiddle.setGLid(id);
		mRight.setGLid(id);
	}

	protected void drawSelf() {
		super.drawSelf();
		mMiddle.drawSprite();
		mRight.drawSprite();
		mLeft.drawSprite();
	}

	public void myRefresh() {
		super.myRefresh();
		updateChildPos();

		mLeft.myRefresh();
		mMiddle.myRefresh();
		mRight.myRefresh();
	}

	protected void myRefreshResource() {
		super.myRefreshResource();
		// mMiddleResid.myRefreshResource();
		// mLeft.myRefreshResource();
		// mMiddle.myRefreshResource();
		// mRight.myRefreshResource();
	}

	public void setPic0(final String resid) {
		mLeft.setResid(PlatformHelper.toCurrentPath(resid));
		updateChildPos();
	}

	public String getPic0() {
		return mLeft.getResid();
	}

	// reflect
	protected static final int REF_Pic0 = FACE_ALL_SHIFT + XML_ALL_SHIFT
			+ DROP_RESID_SHIFT;

	public void setPic1(final String resid) {
		mMiddle.setResid(PlatformHelper.toCurrentPath(resid));
		updateChildPos();
	}

	public String getPic1() {
		return mMiddle.getResid();
	}

	// reflect
	protected static final int REF_Pic1 = FACE_ALL_SHIFT + XML_ALL_SHIFT
			+ DROP_RESID_SHIFT;

	public void setPic2(final String resid) {
		mRight.setResid(PlatformHelper.toCurrentPath(resid));
		updateChildPos();
	}

	public String getPic2() {
		return mRight.getResid();
	}

	// reflect
	protected static final int REF_Pic2 = FACE_ALL_SHIFT + XML_ALL_SHIFT
			+ DROP_RESID_SHIFT;

	public void setResids(final String[] pics) {
		if (pics != null && pics.length > 0) {
			if (pics.length == 1) {
				setPic0(pics[0]);
			} else if (pics.length == 2) {
				setPic0(pics[0]);
				setPic1(pics[1]);
			} else {
				setPic0(pics[0]);
				setPic1(pics[1]);
				setPic2(pics[2]);
			}
		}
	}

	// reflect
	protected static final int REF_Pics = FACE_ALL_SHIFT + XML_ALL_SHIFT
			+ COPY_SHIFT + PASTE_SHIFT;

	protected float mLength = 0;

	public void setLength(float length) {
		length = Math.max(length, getMinLength());
		length = Math.min(length, getMaxLength());

		mLength = length;

		final LayoutMode mode = getLayoutMode();
		float l0, l1, l2;
		switch (mode) {
		case Left2Right:
		case Right2Left:
			l0 = mLeft.getWidth();
			l2 = mRight.getWidth();
			l1 = length - (l0 + l2);
			final float h = mMiddle.getHeight();
			mMiddle.setSize(l1, h);
			break;
		case Bottom2Top:
		case Top2Bottom:
			l0 = mLeft.getHeight();
			l2 = mRight.getHeight();
			l1 = length - (l0 + l2);
			final float w = mMiddle.getWidth();
			mMiddle.setSize(w, l1);
			break;
		}
		updateChildPos();
	}

	public float getLength() {
		final LayoutMode mode = getLayoutMode();
		switch (mode) {
		case Left2Right:
		case Right2Left:
			return mLeft.getWidth() + mMiddle.getWidth()*mMiddle.getPercentage()/100 + mRight.getWidth();
		case Bottom2Top:
		case Top2Bottom:
			return mLeft.getHeight() + mMiddle.getHeight()*mMiddle.getPercentage()/100 + mRight.getHeight();
		}

		return 0;
	}

	// reflect
	protected static final int REF_Length = FACE_ALL_SHIFT + XML_ALL_SHIFT;

	public float getMinLength() {
		final LayoutMode mode = getLayoutMode();
		switch (mode) {
		case Left2Right:
		case Right2Left:
			return mLeft.getWidth() + mRight.getWidth();
		case Bottom2Top:
		case Top2Bottom:
			return mLeft.getHeight() + mRight.getHeight();
		}
		return 0;
	}

	public float getMaxLength() {
		final LayoutMode mode = getLayoutMode();
		switch (mode) {
		case Left2Right:
		case Right2Left:
			final float w = mMiddle.getWidth();
			return w + getMinLength();
		case Bottom2Top:
		case Top2Bottom:
			final float h = mMiddle.getHeight();
			return h + getMinLength();
		}
		return 0;
	}

	public String getRange() {
		return "" + (int) getMinLength() + ", " + (int) getMaxLength() + "";
	}

	// reflect
	protected static final int REF_Range = FACE_GET_SHIFT;

	protected void updateChildPos() { 
		final float minLen = getMinLength();
		final float maxLen = getMaxLength();

		final float dLen = mLength - minLen;
		final float midMaxLen = maxLen - minLen;

		mMiddle.setProgressTimerType(ProgressTimerType.Bar);
		// mProgressNode->ignoreAnchorPointForPosition(false);

		switch (mExtendMode) {
		case Little_Big:
			switch (mLayoutMode) {
			case Left2Right:
			case Right2Left:
				mMiddle.setMidPoint(new ElfPointf(0, 0.5f));
				mMiddle.setAnchorPosition(new ElfPointf(
						0.5f * dLen / midMaxLen, 0.5f));
				break;
			case Bottom2Top:
			case Top2Bottom:
				mMiddle.setMidPoint(new ElfPointf(0.5f, 0));
				mMiddle.setAnchorPosition(new ElfPointf(0.5f, 0.5f * dLen
						/ midMaxLen));
				break;
			}
			break;
		case Big_Little:
			switch (mLayoutMode) {
			case Left2Right:
			case Right2Left:
				mMiddle.setMidPoint(new ElfPointf(1, 0.5f));
				mMiddle.setAnchorPosition(new ElfPointf(1 - 0.5f * dLen
						/ midMaxLen, 0.5f));
				break;
			case Bottom2Top:
			case Top2Bottom:
				mMiddle.setMidPoint(new ElfPointf(0.5f, 1));
				mMiddle.setAnchorPosition(new ElfPointf(0.5f, 1 - 0.5f * dLen
						/ midMaxLen));
				break;
			}
			break;
		case Both:
			mMiddle.setMidPoint(new ElfPointf(0.5f, 0.5f));
			mMiddle.setAnchorPosition(new ElfPointf(0.5f, 0.5f));
			break;
		}

		switch (mLayoutMode) {
		case Left2Right:
		case Right2Left:
			mMiddle.setBarChangeRate(new ElfPointf(1, 0));
			break;
		case Bottom2Top:
		case Top2Bottom:
			mMiddle.setBarChangeRate(new ElfPointf(0, 1));
			break;
		}
		//
		mMiddle.setPercentage((100 * dLen / (midMaxLen)));

		float maxHeight = 0;
		float maxWidth = 0;
		switch (mLayoutMode) {
		case Left2Right:
		case Right2Left:
			maxHeight = Math.max(maxHeight, mLeft.getSize().y);
			maxHeight = Math.max(maxHeight, mMiddle.getSize().y);
			maxHeight = Math.max(maxHeight, mRight.getSize().y);
			this.setSize(mLength, maxHeight);
			break;
		case Bottom2Top:
		case Top2Bottom:
			maxWidth = Math.max(maxWidth, mLeft.getSize().x);
			maxWidth = Math.max(maxWidth, mMiddle.getSize().x);
			maxWidth = Math.max(maxWidth, mRight.getSize().x);
			this.setSize(maxWidth, mLength);
			break;
		}

		final ElfPointf size = this.getSize();

		switch (mLayoutMode) {
		case Left2Right:
			mLeft.setPosition(new ElfPointf(-size.x/2 + mLeft.getSize().x / 2, 0));
			mRight.setPosition(new ElfPointf(size.x/2 - mRight.getSize().x / 2, 0));
			mMiddle.setPosition(new ElfPointf((mLeft.getSize().x - mRight.getSize().x) / 2, 0));
			break;
		case Right2Left:
			mLeft.setPosition(new ElfPointf(size.x/2 - mLeft.getSize().x / 2, 0));
			mRight.setPosition(new ElfPointf(-size.x / 2 + mRight.getSize().x / 2,0));
			mMiddle.setPosition(new ElfPointf(- (mLeft.getSize().x - mRight.getSize().x) / 2, 0));
			break;
		case Bottom2Top:
			mLeft.setPosition(new ElfPointf(0, -size.y/2 + mLeft.getSize().y / 2));
			mRight.setPosition(new ElfPointf(0, size.y/2 - mRight.getSize().y / 2));
			mMiddle.setPosition(new ElfPointf(0, (mLeft.getSize().y - mRight.getSize().y) / 2));
			break;
		case Top2Bottom:
			mLeft.setPosition(new ElfPointf(0, size.y/2 - mLeft.getSize().y / 2));
			mRight.setPosition(new ElfPointf(0, -size.y/2 + mRight.getSize().y / 2));
			mMiddle.setPosition(new ElfPointf(0, - (mLeft.getSize().y - mRight.getSize().y) / 2));
			break;
		}
	}
}
