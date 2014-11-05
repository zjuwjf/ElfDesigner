package elfEngine.basic.node.nodeList;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.interpolator.ElfInterAction;
import org.cocos2d.actions.interval.MoveBy;
import org.cocos2d.actions.interval.MoveTo;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.HorizontalAlign;
import com.ielfgame.stupidGame.enumTypes.LayoutMode;
import com.ielfgame.stupidGame.enumTypes.Orientation;
import com.ielfgame.stupidGame.enumTypes.VerticalAlign;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ClipNode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.bar.BarNode;
import elfEngine.basic.node.bar.Joint9Node;
import elfEngine.basic.node.nodeLayout.LinearLayoutNode;
import elfEngine.basic.node.nodeTouch.TouchNode;
import elfEngine.basic.touch.ScrollEventDecoder;
import elfEngine.basic.touch.ScrollEventDecoder.IOnScroll;
import elfEngine.graphics.PointF;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLHelper.F4;
import elfEngine.opengl.GLManage;

public class ListNode extends TouchNode implements IOnScroll, IClipable {

	private final ScrollEventDecoder mScrollEventDecoder = new ScrollEventDecoder();

	private ListContainerNode mContainer;
	private ListScrollNode mListScrollNode;

	public ListNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setTouchEnable(true);
		setUseSettedSize(true);
		setSize(200, 200);
		setName("#list");
		setPriorityLevel(LIST_PRIORITY);
		setTouchDecoder(mScrollEventDecoder);
	}

	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes();
		mContainer = new ListContainerNode(this, 0);
		mContainer.addToParent();

		mListScrollNode = new ListScrollNode(this, 1);
		mListScrollNode.addToParent();

		// setListDir(ListDir.horizontal);
	}

	public ListContainerNode getContainer() {
		return mContainer;
	}
	
	public void myRefresh() {
		super.myRefresh();
		this.onRecognizeRequiredNodes();
	}

	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();

		mContainer = null;
		mListScrollNode = null;

		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(final ElfNode node) {
				if (node instanceof ListContainerNode) {
					mContainer = (ListContainerNode) node;
				} else if (node instanceof ListScrollNode) {
					mListScrollNode = (ListScrollNode) node;
					mListScrollNode.setAnchorPosition(0.5f, 0.5f);
				}
				return false;
			}
		});

		if (mContainer == null) {
			mContainer = new ListContainerNode(this, 0);
			mContainer.addToParent();
		}

		if (mListScrollNode == null) {
//			mListScrollNode = new ListScrollNode(this, 1);
//			mListScrollNode.addToParent();
		}

		// setListDir(mListDir);
	}
	
//	public void addChild(ElfNode child) {
//		super.addChild(child);
//	}
	
	public void onAddChild(final ElfNode child) {
		super.onAddChild(child);
		
		if(child instanceof ListScrollNode) {
			
			final ListScrollNode lsNode = (ListScrollNode)child;
			final ListContainerNode lcNode = this.getContainer();
			if(lcNode != null) {
				if(lcNode.getOrientation().isHorizontal()) {
					
					final Joint9Node j9 = new Joint9Node(this, 0);
					j9.setSize(this.getSize().x, GLManage.getHeight("HHD_1_1.png"));
					j9.setPositionY(-this.getSize().y/2 + GLManage.getHeight("HHD_1_1.png")/2);
					j9.addToParentView(this.getChildsSize()-1);
					j9.setLMResid("HHD_1_1.png");
					j9.setMMResid("HHD_1_2.png");
					j9.setRMResid("HHD_1_3.png");
					
					lsNode.setLayoutMode(LayoutMode.Left2Right);
					lsNode.setFirstPoint(new ElfPointf(-this.getSize().x/2, -this.getSize().y/2+ GLManage.getHeight("HHD_1_1.png")/2));
					lsNode.setLastPoint(new ElfPointf(this.getSize().x/2, -this.getSize().y/2+ GLManage.getHeight("HHD_1_1.png")/2));
					lsNode.setPic0("HHD_2_1.png");
					lsNode.setPic1("HHD_2_2.png");
					lsNode.setPic2("HHD_2_3.png");
					
				} else {
					final Joint9Node j9 = new Joint9Node(this, 0);
					j9.setSize(GLManage.getWidth("HD_1_1.png"), this.getSize().y);
					j9.setPositionX(this.getSize().x/2-GLManage.getWidth("HD_1_1.png")/2);
					j9.addToParentView(this.getChildsSize()-1);
					j9.setMTResid("HD_1_1.png");
					j9.setMMResid("HD_1_2.png");
					j9.setMBResid("HD_1_3.png");
					
					lsNode.setLayoutMode(LayoutMode.Top2Bottom);
					lsNode.setFirstPoint(new ElfPointf(this.getSize().x/2-GLManage.getWidth("HD_1_1.png")/2, -this.getSize().y/2));
					lsNode.setLastPoint(new ElfPointf(this.getSize().x/2-GLManage.getWidth("HD_1_1.png")/2, this.getSize().y/2));
					lsNode.setPic0("HD_2_1.png");
					lsNode.setPic1("HD_2_2.png");
					lsNode.setPic2("HD_2_3.png");
				}
				
//				this.onRecognizeRequiredNodes();
				mListScrollNode = lsNode;
			}
		}
	}

	protected void drawBefore() {
		ClipNode.pushScissor(this);
		super.drawBefore();
	}

	protected void drawAfter() {
		super.drawAfter();
		ClipNode.popScissor();
	}

	public static class ListScrollNode extends BarNode {
		private final ElfPointf mFirstPoint = new ElfPointf();
		private final ElfPointf mLastPoint = new ElfPointf();
		private boolean mIsAutoFadeOut = false;

		public void setAutoFadeOut(boolean fadeOut) {
			mIsAutoFadeOut = fadeOut;
		}

		public boolean getAutoFadeOut() {
			return mIsAutoFadeOut;
		}

		public ElfPointf getFirstPoint() {
			return mFirstPoint;
		}

		public void setFirstPoint(ElfPointf mFirstPoint) {
			if (mFirstPoint != null) {
				this.mFirstPoint.set(mFirstPoint);
			}
		}

		protected final static int REF_FirstPoint = DEFAULT_SHIFT;

		public ElfPointf getLastPoint() {
			return mLastPoint;
		}

		public void setLastPoint(ElfPointf mLastPoint) {
			if (mLastPoint != null) {
				this.mLastPoint.set(mLastPoint);
			}
		}

		protected final static int REF_LastPoint = DEFAULT_SHIFT;

		// private boolean mPicOrColor = true;
		// public void setPicOrColor(boolean picOrColor) {
		// mPicOrColor = picOrColor;
		// }
		// public boolean getPicOrColor() {
		// return mPicOrColor;
		// }
		// protected final static int REF_PicOrColor = DEFAULT_SHIFT;

		public ListScrollNode(ElfNode father, int ordinal) {
			super(father, ordinal);

			setName("#ListScrollNode");
		}

		private F4 mF4;

		public void drawBefore() {
			mF4 = GLHelper.popScissor();
			super.drawBefore();
		}

		public void drawAfter() {
			super.drawAfter();
			if (mF4 != null) {
				GLHelper.pushScissor(mF4.left, mF4.bottom, mF4.width, mF4.height);
				mF4 = null;
			}
		}

		// public void drawSelf() {
		// if(getPicOrColor()) {
		// super.drawSelf();
		// } else {
		//
		// }
		// }
	}

	public static class ListContainerNode extends LinearLayoutNode {
		public ListContainerNode(ElfNode father, int ordinal) {
			super(father, ordinal);
			setUseSettedSize(true);

			setName("#container");
		}
	}

	private ElfPointf mSpeed = new ElfPointf();
	private float mAnimateTime = 0.35f;
	private InterType mInterType = InterType.Viscous;

	public void setInterType(InterType interType) {
		mInterType = interType;
	}

	public InterType getInterType() {
		return mInterType;
	}

	protected final static int REF_InterType = DEFAULT_SHIFT;

	public void onScroll(ElfNode node, float speedX, float speedY) {
		// stop

		if (mContainer == null) {
			return;
		}

		this.onStop(mContainer);

		if (mContainer.getOrientation().isHorizontal()) {
			final float inWidth = mContainer.getSize().x;
			final float outWidth = this.getSize().x;
			final float posX = mContainer.getPosition().x;

			// new added
			final ElfPointf anchor = mContainer.getAnchorPosition();
			getBoundValue(inWidth, outWidth, mBounds, mHorizontalAlign.ordinal(), (anchor.x - 0.5f));

			if (posX < mBounds[0]) {
				CCAction action = ElfInterAction.action(MoveBy.action(mAnimateTime, mBounds[0] - posX, 0), mInterType);
				this.mContainer.runAction(action);
			} else if (posX > mBounds[1]) {
				CCAction action = ElfInterAction.action(MoveBy.action(mAnimateTime, mBounds[1] - posX, 0), mInterType);
				this.mContainer.runAction(action);
			} else {
				mSpeed.setPoint(speedX, 0);
			}
		} else {
			final float inHeight = mContainer.getSize().y;
			final float outHeight = this.getSize().y;
			final float posY = mContainer.getPosition().y;

			// new added
			final ElfPointf anchor = mContainer.getAnchorPosition();
			getBoundValue(inHeight, outHeight, mBounds, mVerticalAlign.ordinal(), (anchor.y - 0.5f));

			if (posY < mBounds[0]) {
				CCAction action = ElfInterAction.action(MoveBy.action(mAnimateTime, 0, mBounds[0] - posY), mInterType);
				this.mContainer.runAction(action);
			} else if (posY > mBounds[1]) {
				CCAction action = ElfInterAction.action(MoveBy.action(mAnimateTime, 0, mBounds[1] - posY), mInterType);
				this.mContainer.runAction(action);
			} else {
				mSpeed.setPoint(0, speedY);
			}
		}
	}

	public ElfPointf onRestrict(ElfNode node) {
		final ElfPointf sizeOut = this.getSize();

		if (mContainer == null) {
			return null;
		}

		final ElfPointf sizeIn = this.mContainer.getSize();

		if (mContainer.getOrientation().isHorizontal()) {
			// new added
			final ElfPointf anchor = mContainer.getAnchorPosition();
			getBoundValue(sizeIn.x, sizeOut.x, mBounds, mHorizontalAlign.ordinal(), (anchor.x - 0.5f));

			final float overValue = this.getIsTouching() ? getMaxOutOfBoundsValue() : 0;
			getOverBoundValue(mBounds, overValue, mOverBounds);

			float x = this.mContainer.getPosition().x;

			if (x < mOverBounds[0]) {
				x = mOverBounds[0];
			} else if (x > mOverBounds[1]) {
				x = mOverBounds[1];
			}
			this.mContainer.setPositionX(x);
		} else {
			// new added
			final ElfPointf anchor = mContainer.getAnchorPosition();
			getBoundValue(sizeIn.y, sizeOut.y, mBounds, mVerticalAlign.ordinal(), (anchor.y - 0.5f));

			final float overValue = this.getIsTouching() ? getMaxOutOfBoundsValue() : 0;
			getOverBoundValue(mBounds, overValue, mOverBounds);

			float y = this.mContainer.getPosition().y;

			if (y < mOverBounds[0]) {
				y = mOverBounds[0];
			} else if (y > mOverBounds[1]) {
				y = mOverBounds[1];
			}
			this.mContainer.setPositionY(y);
		}
		return null;
	}

	public void onStop(ElfNode node) {
		mSpeed.setPoint(0, 0);
		if (mContainer != null) {
			mContainer.stopActions();
		}
	}

	public ElfNode getMoveNode() {
		return this.mContainer;
	}

	private float mSlipRate = 0.82f;
	private float mSpeedRate = 1f;
	private float mReboundRate = 0f;

	public float getSlipRate() {
		return mSlipRate;
	}

	public void setSlipRate(float mSlipRate) {
		this.mSlipRate = mSlipRate;
	}

	protected final static int REF_SlipRate = DEFAULT_SHIFT;

	public float getSpeedRate() {
		return mSpeedRate;
	}

	public void setSpeedRate(float mSpeedRate) {
		this.mSpeedRate = mSpeedRate;
	}

	protected final static int REF_SpeedRate = DEFAULT_SHIFT;

	public float getReboundRate() {
		return mReboundRate;
	}

	public void setReboundRate(float mReboundRate) {
		this.mReboundRate = mReboundRate;
	}

	protected final static int REF_ReboundRate = DEFAULT_SHIFT;

	private float mResistanceAreaSlipRate = 0.45f;

	public float getResistanceAreaSlipRate() {
		return mResistanceAreaSlipRate;
	}

	public void setResistanceAreaSlipRate(float resistanceAreaSlipRate) {
		mResistanceAreaSlipRate = resistanceAreaSlipRate;
	}

	protected final static int REF_ResistanceAreaSlipRate = DEFAULT_SHIFT;

	public void calc(float pMsElapsed) {

		// container
		if (mSpeed.x != 0 || mSpeed.y != 0) {
			final ElfPointf p1 = this.mContainer.getPositionInScreen();

			final ElfPointf tmp = new ElfPointf();
			tmp.set(p1);
			final ElfPointf off = onMove(mSpeed.x * pMsElapsed * mSpeedRate, mSpeed.y * pMsElapsed * mSpeedRate);
			if (off != null) {
				tmp.translate(off.x, off.y);
			} else {
				tmp.translate(mSpeed.x * pMsElapsed * mSpeedRate, mSpeed.y * pMsElapsed * mSpeedRate);
			}

			this.mContainer.setPositionInScreen(tmp);

			if (mContainer.getOrientation().isHorizontal()) {
				// new added
				final ElfPointf anchor = mContainer.getAnchorPosition();
				getBoundValue(this.mContainer.getSize().x, this.getSize().x, mBounds, mHorizontalAlign.ordinal(), (anchor.x - 0.5f));
				getOverBoundValue(mBounds, getMaxOutOfBoundsValue(), mOverBounds);

				final float x = this.mContainer.getPosition().x;
				if (x < mOverBounds[0]) {
					mSpeed.x = 0;
					this.mContainer.setPositionX(mOverBounds[0]);
				} else if (x > mOverBounds[1]) {
					mSpeed.x = 0;
					this.mContainer.setPositionX(mOverBounds[1]);
				} else if (x < mBounds[0] || x > mBounds[1]) {
					mSpeed.x = mSpeed.x * mResistanceAreaSlipRate;
				} else {
					mSpeed.x = mSpeed.x * mSlipRate;
				}
			} else {
				// new added
				final ElfPointf anchor = mContainer.getAnchorPosition();
				getBoundValue(this.mContainer.getSize().y, this.getSize().y, mBounds, mVerticalAlign.ordinal(), (anchor.y - 0.5f));

				getOverBoundValue(mBounds, getMaxOutOfBoundsValue(), mOverBounds);
				final float y = this.mContainer.getPosition().y;
				if (y < mOverBounds[0]) {
					mSpeed.y = 0;
					this.mContainer.setPositionY(mOverBounds[0]);
				} else if (y > mOverBounds[1]) {
					mSpeed.y = 0;
					this.mContainer.setPositionY(mOverBounds[1]);
				} else if (y < mBounds[0] || y > mBounds[1]) {
					mSpeed.y = mSpeed.y * mResistanceAreaSlipRate;
				} else {
					mSpeed.y = mSpeed.y * mSlipRate;
				}
			}

			if (Math.abs(mSpeed.x * 1000) < 2f) {
				mSpeed.x = 0;
			}

			if (Math.abs(mSpeed.y * 1000) < 2f) {
				mSpeed.y = 0;
			}

			if (mSpeed.x == 0 && mSpeed.y == 0) {
				if (mContainer.getOrientation().isHorizontal()) {
					// new added
					final ElfPointf anchor = mContainer.getAnchorPosition();
					getBoundValue(this.mContainer.getSize().x, this.getSize().x, mBounds, mHorizontalAlign.ordinal(), anchor.x - 0.5f);
					final float posX = this.mContainer.getPosition().x;
					if (posX < mBounds[0]) {
						CCAction action = ElfInterAction.action(MoveBy.action(mAnimateTime, mBounds[0] - posX, 0), mInterType);
						this.mContainer.runAction(action);
					} else if (posX > mBounds[1]) {
						CCAction action = ElfInterAction.action(MoveBy.action(mAnimateTime, mBounds[1] - posX, 0), mInterType);
						this.mContainer.runAction(action);
					}
				} else {
					final ElfPointf anchor = mContainer.getAnchorPosition();
					getBoundValue(this.mContainer.getSize().y, this.getSize().y, mBounds, mVerticalAlign.ordinal(), anchor.y - 0.5f);
					final float posY = this.mContainer.getPosition().y;
					if (posY < mBounds[0]) {
						CCAction action = ElfInterAction.action(MoveBy.action(mAnimateTime, 0, mBounds[0] - posY), mInterType);
						this.mContainer.runAction(action);
					} else if (posY > mBounds[1]) {
						CCAction action = ElfInterAction.action(MoveBy.action(mAnimateTime, 0, mBounds[1] - posY), mInterType);
						this.mContainer.runAction(action);
					}
				}
			}
		}

		this.onRestrict(this.mContainer);

		if (mListScrollNode == null) {
			return;
		}

		// bar
		final ElfPointf p1 = new ElfPointf();
		p1.set(mListScrollNode.mFirstPoint);
		final ElfPointf p2 = new ElfPointf();
		p2.set(mListScrollNode.mLastPoint);

		if (mContainer.getOrientation().isHorizontal()) {
			float listWidth = mContainer.getWidth();
			float windowWidth = this.getWidth();
			float scrollWidth = Math.abs(this.mListScrollNode.mLastPoint.x - this.mListScrollNode.mFirstPoint.x);

			if (listWidth < windowWidth) {
				mListScrollNode.setPosition((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
				mListScrollNode.setLength(scrollWidth);
			} else {
				final ElfPointf anchor = mContainer.getAnchorPosition();
				getBoundValue(this.mContainer.getSize().x, this.getSize().x, mBounds, mHorizontalAlign.ordinal(), (anchor.x - 0.5f));

				final float min = mBounds[0];
				final float max = mBounds[1];

				float x = mContainer.getPosition().x;

				float rate = (x - min) / (max - min);

				float l = windowWidth / listWidth * scrollWidth;
				if (rate > 1) {
					l /= rate;
					rate = 1;
				} else if (rate < 0) {
					l /= (1 - rate);
					rate = 0;
				}

				final ElfPointf p = new ElfPointf(0, (p1.y + p2.y) / 2);
				if (p1.x > p2.x) {
					p.x = (p1.x - l / 2) * (1 - rate) + (p2.x + l / 2) * (rate);
				} else {
					p.x = (p2.x - l / 2) * (1 - rate) + (p1.x + l / 2) * (rate);
				}

				mListScrollNode.setPosition(p);
				mListScrollNode.setLength(l);
			}
		} else {
			float listHeight = mContainer.getHeight();
			float windowHeight = this.getHeight();
			float scrollHeight = Math.abs(this.mListScrollNode.mLastPoint.y - this.mListScrollNode.mFirstPoint.y);

			if (listHeight < windowHeight) {
				mListScrollNode.setPosition((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
				mListScrollNode.setLength(scrollHeight);
			} else {

				final ElfPointf anchor = mContainer.getAnchorPosition();
				getBoundValue(this.mContainer.getSize().y, this.getSize().y, mBounds, mVerticalAlign.ordinal(), (anchor.y - 0.5f));

				final float min = mBounds[0];
				final float max = mBounds[1];

				float y = mContainer.getPosition().y;

				float rate = (y - min) / (max - min);

				float l = windowHeight / listHeight * scrollHeight;

				if (rate > 1) {
					l /= rate;
					rate = 1;
				} else if (rate < 0) {
					l /= (1 - rate);
					rate = 0;
				}

				final ElfPointf position = new ElfPointf((p1.x + p2.x) / 2, 0);
				if (p1.y > p2.y) {
					position.y = (p1.y - l / 2) * (1 - rate) + (p2.y + l / 2) * (rate);
				} else {
					position.y = (p2.y - l / 2) * (1 - rate) + (p1.y + l / 2) * (rate);
				}

				mListScrollNode.setPosition(position);
				mListScrollNode.setLength(l);
			}
		}

		if (mAutoInvisible) {
			calcVisible();
		}
	}

	private void calcVisible() {
		if (this.mContainer != null) {
			ElfNode[] children = mContainer.getChilds();
			// no rotate,
			boolean useScale = this.mContainer.getCalcSizeWithScale();
			final ElfPointf anchor = mContainer.getAnchorPosition();
			float count = 0;

			final ElfPointf inSize = mContainer.getSize();
			final ElfPointf outSize = this.getSize();

			final ElfPointf position = mContainer.getPosition();

			if (mContainer.getOrientation().isHorizontal()) {
				if (inSize.x <= outSize.x) {
					// all visible
					for (ElfNode child : children) {
						child.setVisible(true);
					}
					return;
				}

				getBoundValue(inSize.x, outSize.x, mBounds, mHorizontalAlign.ordinal(), (anchor.x - 0.5f));
				// left
				final float a = (mBounds[0] + mBounds[1]) / 2 - position.x - (outSize.x) / 2;
				// right
				final float b = outSize.x + a;

				// b-a=outSize.width
				final int flag = mContainer.getOrientation() == Orientation.Horizontal ? -1 : 1;

				// L2R
				count = flag * inSize.x / 2;
				for (ElfNode child : children) {
					float width = child.getSize().x;
					if (useScale) {
						width *= Math.abs(child.getScale().x);
					}
					if (isIntersect(a, b, count, count - flag * width)) {
						child.setVisible(true);
					} else {
						child.setVisible(false);
					}
					count -= flag * width;
				}
			} else {
				if (inSize.y <= outSize.y) {
					// all visible
					for (ElfNode child : children) {
						child.setVisible(true);
					}
					return;
				}

				getBoundValue(inSize.y, outSize.y, mBounds, mVerticalAlign.ordinal(), (anchor.y - 0.5f));

				// bottom
				final float a = (mBounds[0] + mBounds[1]) / 2 - position.y - (outSize.y) / 2;
				// top
				final float b = outSize.y + a;

				// b-a=outSize.height
				final int flag = this.mContainer.getOrientation() == Orientation.Vertical_B2T ? -1 : 1;

				// B2T
				count = flag * inSize.y / 2;
				for (ElfNode node : children) {
					float height = node.getSize().y;
					if (useScale) {
						height *= Math.abs(node.getScale().y);
					}
					if (isIntersect(a, b, count, count - flag * height)) {
						node.setVisible(true);
					} else {
						node.setVisible(false);
					}
					count -= flag * height;
				}
			}
		}
	}

	private final boolean isIntersect(float l, float r, float a, float b) {
		if (a < b) {
			return !(r < a || b < l);
		} else {
			return !(r < b || a < l);
		}
	}

	public ElfPointf onMove(float dx, float dy) {
		if (mContainer.getOrientation().isHorizontal()) {
			final float in = mContainer.getSize().x;
			final float out = this.getSize().x;
			final ElfPointf anchor = mContainer.getAnchorPosition();
			getBoundValue(in, out, mBounds, mHorizontalAlign.ordinal(), anchor.x - 0.5f);
			final float overValue = getMaxOutOfBoundsValue();
			getOverBoundValue(mBounds, overValue, mOverBounds);
			final float x = this.mContainer.getPosition().x;
			if (x < mOverBounds[0] || x > mOverBounds[1]) {
				dx = 0;
			} else if (x < mBounds[0] || x > mBounds[1]) {
				dx *= mOverDragRate;
			}
			return new ElfPointf(dx, 0);
		} else {
			final float in = mContainer.getSize().y;
			final float out = this.getSize().y;
			final ElfPointf anchor = mContainer.getAnchorPosition();
			getBoundValue(in, out, mBounds, mVerticalAlign.ordinal(), anchor.y - 0.5f);
			final float overValue = getMaxOutOfBoundsValue();
			getOverBoundValue(mBounds, overValue, mOverBounds);
			final float y = this.mContainer.getPosition().y;
			if (y < mOverBounds[0] || y > mOverBounds[1]) {
				dy = 0;
			} else if (y < mBounds[0] || y > mBounds[1]) {
				dy *= mOverDragRate;
			}
			return new ElfPointf(0, dy);
		}
	}

	private HorizontalAlign mHorizontalAlign = HorizontalAlign.Left;
	private VerticalAlign mVerticalAlign = VerticalAlign.Top;
	// +, -

	// OutOfBounds
	private float mOutOfBounds = 0;
	private boolean mAutoOutOfBounds = true;
	private float mOverDragRate = 0.3f;

	public float getAnimateTime() {
		return mAnimateTime;
	}

	public void setAnimateTime(float mAnimateTime) {
		this.mAnimateTime = mAnimateTime;
	}

	protected final static int REF_AnimateTime = DEFAULT_SHIFT;

	public HorizontalAlign getHorizontalAlign() {
		return mHorizontalAlign;
	}

	public void setHorizontalAlign(HorizontalAlign mHorizontalAlign) {
		this.mHorizontalAlign = mHorizontalAlign;
	}

	protected final static int REF_HorizontalAlign = DEFAULT_SHIFT;

	public VerticalAlign getVerticalAlign() {
		return mVerticalAlign;
	}

	public void setVerticalAlign(VerticalAlign mVerticalAlign) {
		this.mVerticalAlign = mVerticalAlign;
	}

	protected final static int REF_VerticalAlign = DEFAULT_SHIFT;

	public float getOutOfBounds() {
		return mOutOfBounds;
	}

	public void setOutOfBounds(float mOutOfBounds) {
		this.mOutOfBounds = mOutOfBounds;
	}

	protected final static int REF_OutOfBounds = DEFAULT_SHIFT;

	public boolean getAutoOutOfBounds() {
		return mAutoOutOfBounds;
	}

	public void setAutoOutOfBounds(boolean mAutoOutOfBounds) {
		this.mAutoOutOfBounds = mAutoOutOfBounds;
	}

	protected final static int REF_AutoOutOfBounds = DEFAULT_SHIFT;

	public float getOverDragRate() {
		return mOverDragRate;
	}

	public void setOverDragRate(float mOverDragRate) {
		this.mOverDragRate = mOverDragRate;
	}

	protected final static int REF_OverDragRate = DEFAULT_SHIFT;

	public void setAlignTo(int itemIndex) {
		if (mContainer != null) {
			final ElfNode nodes[] = mContainer.getChilds();
			final int count = nodes.length;
			if (count > 0) {
				itemIndex = Math.min(itemIndex, count - 1);
				itemIndex = Math.max(itemIndex, 0);

				mAlignTo = itemIndex;

				final ElfPointf oldPos = mContainer.getPosition();
				ElfPointf newPos = new ElfPointf(oldPos.x, oldPos.y);
				final ElfPointf anchor = mContainer.getAnchorPosition();
				final ElfPointf inSize = mContainer.getSize();
				final ElfPointf outSize = this.getSize();
				boolean calcScale = mContainer.getCalcSizeWithScale();

				if (mContainer.getOrientation().isHorizontal()) {
					getBoundValue(inSize.x, outSize.x, mBounds, mHorizontalAlign.ordinal(), (anchor.x - 0.5f));

					float widthCount = 0;
					float widthCountPrev = 0;
					for (int i = 0; i <= itemIndex; i++) {
						widthCountPrev = widthCount;
						ElfNode node = nodes[i];
						float w = node.getSize().x;
						if (calcScale) {
							w *= Math.abs(node.getScale().x);
						}
						widthCount += w;
					}

					if (mContainer.getOrientation() == Orientation.Horizontal_R2L) {
						float tmp = widthCount;
						widthCount = inSize.x - widthCountPrev;
						widthCountPrev = inSize.x - tmp;
					}

					// L2R
					if (mHorizontalAlign == HorizontalAlign.Right) {
						newPos.x = mBounds[0] + inSize.x - widthCount;
					} else if (mHorizontalAlign == HorizontalAlign.Left) {
						// left
						newPos.x = mBounds[1] - widthCountPrev;
					} else {
						// center
						newPos.x = (mBounds[0] + mBounds[1]) / 2 + inSize.x / 2 - (widthCountPrev + widthCount) / 2;
					}
				} else {
					getBoundValue(inSize.y, outSize.y, mBounds, mVerticalAlign.ordinal(), (anchor.y - 0.5f));

					float heightCount = 0;
					float heightCountPrev = 0;
					for (int i = 0; i <= itemIndex; i++) {
						heightCountPrev = heightCount;
						ElfNode node = nodes[i];
						float h = node.getSize().y;
						if (calcScale) {
							h *= Math.abs(node.getScale().y);
						}
						heightCount += h;
					}

					if (mContainer.getOrientation() == Orientation.Vertical) {
						float tmp = heightCount;
						heightCount = inSize.y - heightCountPrev;
						heightCountPrev = inSize.y - tmp;
					}

					// B2T
					if (mVerticalAlign == VerticalAlign.Top) {
						newPos.y = mBounds[0] + inSize.y - heightCount;
					} else if (mVerticalAlign == VerticalAlign.Bottom) {
						// Bottom
						newPos.y = mBounds[1] - heightCountPrev;
					} else {
						// center
						newPos.y = (mBounds[0] + mBounds[1]) / 2 + inSize.y / 2 - (heightCountPrev + heightCount) / 2;
					}
				}

				mContainer.setPosition(newPos);

				// Restrict
				final boolean oldAuto = this.getAutoOutOfBounds();
				this.setAutoOutOfBounds(false);
				this.onRestrict(mContainer);
				this.setAutoOutOfBounds(oldAuto);

				newPos = mContainer.getPosition();
				mContainer.setPosition(oldPos);

				MoveTo move = MoveTo.action(0.5f, newPos.x, newPos.y);
				CCAction action = ElfInterAction.action(move, InterType.Viscous);
				mContainer.runElfAction(action);
			}
		}
	}

	public int getAlignTo() {
		return mAlignTo;
	}

	private int mAlignTo = 0;
	protected final static int REF_AlignTo = FACE_ALL_SHIFT;

	private boolean mAutoInvisible = true;

	public void setAutoInvisible(boolean auto) {
		mAutoInvisible = auto;
	}

	public boolean getAutoInvisible() {
		return mAutoInvisible;
	}

	protected final static int REF_AutoInvisible = DEFAULT_SHIFT;

	public float getMaxOutOfBoundsValue() {
		if (mAutoOutOfBounds) {
			if (mContainer.getOrientation().isHorizontal()) {
				return this.getSize().x / 2;
			} else {
				return this.getSize().y / 2;
			}
		} else {
			return mOutOfBounds;
		}
	}

	private final float[] mBounds = new float[2];
	private final float[] mOverBounds = new float[2];

	static void getOverBoundValue(float[] bound, float overValue, float[] overBoundRet) {
		overBoundRet[0] = bound[0] - overValue;
		overBoundRet[1] = bound[1] + overValue;
	}

	// align to out Min
	static void getBoundValue(final float in, float out, float[] ret, int alignMin, float anchor) {
		if (in < out) {
			if (alignMin == 0) {
				ret[0] = ret[1] = -out / 2 + in / 2;
			} else if (alignMin == 2) {
				ret[0] = ret[1] = 0;
			} else {
				ret[0] = ret[1] = out / 2 - in / 2;
			}
		} else {
			// in > out
			ret[1] = -out / 2 + in / 2;
			ret[0] = out / 2 - in / 2;
		}
		final float offset = anchor * in;
		ret[0] += offset;
		ret[1] += offset;
	}

	private boolean mIsTouching = false;

	public void setIsInTouching(boolean touching) {
		mIsTouching = touching;
	}

	public boolean getIsTouching() {
		return mIsTouching;
	}

	@Override
	public boolean isInClipSize(float x, float y) {
		return isInSize(new PointF(x, y));
	}
}
