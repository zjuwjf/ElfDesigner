package elfEngine.basic.node.nodeTouch;

import com.ielfgame.stupidGame.enumTypes.ClickState;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.ClickEventDecoder;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.ClickEventDecoder.IClickable;
import elfEngine.extend.ElfOnClickListener;

public class ClickNode extends TouchNode implements IClickable {

	public ClickNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setTouchDecoder(mEventDecoder);
		setTouchEnable(true);

		this.setName("#click");
		setPriorityLevel(CLICK_PRIORITY);
	}

	private final ClickEventDecoder mEventDecoder = new ClickEventDecoder();
	private ClickState mClickState = ClickState.HIDE;

	protected ElfNode mNormalNode;
	protected ElfNode mPressedNode;
	protected ElfNode mInvalidNode;
	
	public ElfNode getNormalNode() {
		return mNormalNode;
	}
	
	public ElfNode getInvalidNode() {
		return mInvalidNode;
	}
	
	public ElfNode getPressedNode() {
		return mPressedNode;
	}

	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes();

		mNormalNode = new ElfNode(this, 0);
		mPressedNode = new ElfNode(this, 0);
		mInvalidNode = new ElfNode(this, 0);

		mNormalNode.setName("#normal");
		mNormalNode.addToParent();

		mPressedNode.setName("#pressed");
		mPressedNode.addToParent();

		mInvalidNode.setName("#invalid");
		mInvalidNode.addToParent();
	} 

	// for xml...after
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		mNormalNode = null;
		mPressedNode = null;
		mInvalidNode = null;

		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if (node.getName().equals("#normal")) {
					mNormalNode = node;
				} else if (node.getName().equals("#pressed")) {
					mPressedNode = node;
				} else if (node.getName().equals("#invalid")) {
					mInvalidNode = node;
				}
				return false;
			}
		});
	}

	public void setMaxMove(float move) {
		mEventDecoder.setMaxMove(move);
	}

	public float getMaxMove() {
		return mEventDecoder.getMaxMove();
	}

	protected final static int REF_MaxMove = DEFAULT_SHIFT;

	public void setClickTime(long time) {
		mEventDecoder.setClickTime(time);
	}

	public long getClickTime() {
		return mEventDecoder.getClickTime();
	}

	protected final static int REF_ClickTime = DEFAULT_SHIFT;

	public void onShow(ElfNode node) {
		mClickState = ClickState.SHOW;
		if (mNormalNode != null) {
			mNormalNode.setVisible(false);
		}
		if (mPressedNode != null) {
			mPressedNode.setVisible(true);
		}
		if (mInvalidNode != null) {
			mInvalidNode.setVisible(false);
		}

	}

	public void onHide(ElfNode node) {
		mClickState = ClickState.HIDE;
		if (mNormalNode != null) {
			mNormalNode.setVisible(true);
		}
		if (mPressedNode != null) {
			mPressedNode.setVisible(false);
		}
		if (mInvalidNode != null) {
			mInvalidNode.setVisible(false);
		}

	}

	public void onInvaid(ElfNode node) {
		mClickState = ClickState.INVALID;
		if (mNormalNode != null) {
			mNormalNode.setVisible(false);
		}
		if (mPressedNode != null) {
			mPressedNode.setVisible(false);
		}
		if (mInvalidNode != null) {
			mInvalidNode.setVisible(true);
		}

	}

	public ClickState getState() {
		return mClickState;
	}

	public void calc(float t) {
		super.calc(t);
		mEventDecoder.run();
	}

	private ElfOnClickListener mOnClickListener;

	public void setOnClickListener(ElfOnClickListener elfOnClickListener) {
		mOnClickListener = elfOnClickListener;
	}

	public void trigger(ElfNode node,  ElfEvent event) {
		if (mOnClickListener != null) {
			mOnClickListener.onClick(node.getId(), event);
		}
	}
}
