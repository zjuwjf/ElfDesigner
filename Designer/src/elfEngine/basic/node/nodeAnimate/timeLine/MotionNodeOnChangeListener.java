package elfEngine.basic.node.nodeAnimate.timeLine;

import java.util.LinkedList;
import java.util.List;

import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.INodePropertyChange;

public class MotionNodeOnChangeListener implements INodePropertyChange {

	private ElfMotionNode mMotionNode;
	protected boolean mAutoKey = false;
	private final LinkedList<ElfNode> mSelectedNode = new LinkedList<ElfNode>();

	public MotionNodeOnChangeListener() {
	}
	
	public void setAutoKey(final boolean autoKey) {
		mAutoKey = autoKey;
	}
	
	public boolean getAutoKey() {
		return mAutoKey;
	}

	public void bindMotion(ElfMotionNode motionNode) {
		mMotionNode = motionNode;
	}

	public void bindNode(final List<ElfNode> selectlist) {
		for(final ElfNode selectnode : mSelectedNode) {
			selectnode.setNodePropertyChange(null);
		}
		
		mSelectedNode.clear();
		mSelectedNode.addAll(selectlist);
		
		for(final ElfNode selectnode : mSelectedNode) {
			selectnode.setNodePropertyChange(this);
		}
	}

	public void onNodePropertyChange(ElfNode node, NodePropertyType type) {
		if (mMotionNode != null) {
			final TimeData data = TimeData.getSelectTimeData();
			
			if(!mMotionNode.isRecurFatherOf(node)) {
				return;
			}

			if (!mAutoKey) {
				if (data != null) {
					final ElfMotionKeyNode key = data.getBindKeyNode();
					if (key != null) {
						switch (data.getType()) {
						case ColorType:
							key.setColor(node.getColor());
							break;
						case PositionType:
							key.setPosition(node.getPosition());
							break;
						case RotateType:
							key.setRotate(node.getRotate());
							break;
						case ScaleType:
							key.setScale(node.getScale());
							break;
						case VisibleType:
							key.setVisible(node.getVisible());
							break;
						}
					}
				}
			} else {
				final int time = mMotionNode.getProgress();
				final ElfMotionKeyNode[] keys = mMotionNode.getKeyNodesByTime(node, time);
				final ElfMotionKeyNode findKey = keys[type.ordinal()];
				if (findKey != null) {
					switch (type) {
					case ColorType:
						findKey.setColor(node.getColor());
						break;
					case PositionType:
						findKey.setPosition(node.getPosition());
						break;
					case RotateType:
						findKey.setRotate(node.getRotate());
						break;
					case ScaleType:
						findKey.setScale(node.getScale());
						break;
					case VisibleType:
						findKey.setVisible(node.getVisible());
						break;
					}
				} else {
					final ElfMotionKeyNode newKey = mMotionNode.addKey(node, type, time);
					PowerMan.getSingleton(TimeCanvasView.class).addKey(newKey, type, true);
				}
			}
		}
	}

	public boolean onNodePropertyChangeBefore(ElfNode node, NodePropertyType type) {
		if (mMotionNode != null) {
			
			if(!mMotionNode.isRecurFatherOf(node)) {
				return true;
			}
			
			final TimeData data = TimeData.getSelectTimeData();
			if (data != null && !mAutoKey) {
				final ElfMotionKeyNode key = data.getBindKeyNode();
				if (key != null) {
					return data.getType() == type;
				}
			}
			
			if(mAutoKey) {
				final ElfMotionKeyNode[] keys = mMotionNode.findKeysByNode(node, type.toString());
				if(keys == null || keys.length == 0) {
					final ElfMotionKeyNode newKey = mMotionNode.addKey(node, type, 0);
					PowerMan.getSingleton(TimeCanvasView.class).addKey(newKey, type, false);
				}
			}
		}
		
		return true;
	}
}
