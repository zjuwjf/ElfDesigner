package elfEngine.basic.node.nodeTouch;

import com.ielfgame.stupidGame.enumTypes.ClickState;

import elfEngine.basic.node.ElfNode;

public class CheckBoxNode extends ClickNode{
	
	private ElfNode mSelectedNode;
	private ElfNode mUnselectedNode;
	private boolean mStateSelected;

	public CheckBoxNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setName("#checkbox");
	}
	
	public void setStateSelected(boolean selected) {
		mStateSelected = selected;
		if(mSelectedNode != null) {
			mSelectedNode.setVisible(mStateSelected);
		} 
		if(mUnselectedNode != null) {
			mUnselectedNode.setVisible(!mStateSelected);
		}
	}
	public boolean getStateSelected() {
		return mStateSelected;
	}
	protected final static int REF_Selected = DEFAULT_SHIFT;
	
	public ElfNode getSelectNode() {
		return mSelectedNode;
	}
	
	public ElfNode getUnselectNode() {
		return mUnselectedNode;
	}
	
	public void onCreateRequiredNodes() { 
		super.onCreateRequiredNodes(); 
		
		mSelectedNode = new ElfNode(this, 0);
		mUnselectedNode = new ElfNode(this, 0);
		
		mSelectedNode.setName("#selected");
		mSelectedNode.addToParent();
		
		mUnselectedNode.setName("#unselected"); 
		mUnselectedNode.addToParent(); 
	} 
	
	//for xml...after
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		mSelectedNode = null;
		mUnselectedNode = null;
		
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if(node.getName().equals("#selected")) { 
					mSelectedNode = node;
				} else if(node.getName().equals("#unselected")) { 
					mUnselectedNode = node;
				} 
				return false;
			} 
		});
	} 
	
	public void onShow(ElfNode node) { 
		super.onShow(node);
	} 

	public void onHide(ElfNode node) { 
		if( getState() != ClickState.HIDE) {
			setStateSelected(!getStateSelected());
		} 
		super.onHide(node);
	} 

	public void onInvaid(ElfNode node) {
		super.onInvaid(node);
		mSelectedNode.setVisible(false);
		mUnselectedNode.setVisible(false);
	} 
}
