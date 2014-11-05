package elfEngine.basic.node.nodeTouch;

import java.util.LinkedList;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;

public class ColorClickNode extends ClickNode {
	
	private final ElfColor mNormalColor = new ElfColor();
	private final ElfColor mPressedColor = new ElfColor(166/255f,166/255f,166/255f, 1);
	private final ElfColor mInvalidColor = new ElfColor(166/255f,166/255f,166/255f, 1);

	public ColorClickNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes();

		mPressedNode.removeFromParent();
		mInvalidNode.removeFromParent();
	} 
	
	
	
	public ElfNode [] getChilds() { 
		final LinkedList<ElfNode> nodes = new LinkedList<ElfNode>();
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if(!node.getName().equals("#pressed") && !node.getName().equals("#invalid") &&
						!node.getName().equals("pressed") && !node.getName().equals("invalid")) { 
					nodes.add(node); 
				} 
				return false;
			}
		});
		final ElfNode [] childs = new ElfNode[nodes.size()];
		nodes.toArray(childs);
		
		return childs; 
	} 
	
	public void setNormalColor(ElfColor color) {
		mNormalColor.set(color);
	}
	public ElfColor getNormalColor() {
		return new ElfColor(mNormalColor);
	}
	protected final static int REF_NormalColor = DEFAULT_SHIFT;
	
	public void setPressedColor(ElfColor color) {
		mPressedColor.set(color);
	}
	public ElfColor getPressedColor() {
		return new ElfColor(mPressedColor);
	}
	protected final static int REF_PressedColor = DEFAULT_SHIFT;
	
	public void setInvalidColor(ElfColor color) {
		mInvalidColor.set(color);
	}
	public ElfColor getInvalidColor() {
		return new ElfColor(mInvalidColor);
	}
	protected final static int REF_InvalidColor = DEFAULT_SHIFT;
	
	public void onShow(ElfNode node) { 
		super.onShow(node);
		if(mNormalNode != null) {
			mNormalNode.setVisible(true);
			mNormalNode.setColor(mPressedColor);
		}
	} 

	public void onHide(ElfNode node) { 
		super.onHide(node);
		if(mNormalNode != null) {
			mNormalNode.setVisible(true);
			mNormalNode.setColor(mNormalColor);
		}
	} 

	public void onInvaid(ElfNode node) {
		super.onInvaid(node);
		if(mNormalNode != null) {
			mNormalNode.setVisible(true);
			mNormalNode.setColor(mInvalidColor);
		}
	} 
}
