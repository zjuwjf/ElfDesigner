package elfEngine.basic.node.nodeList;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeLayout.LinearLayoutNode;
import elfEngine.basic.node.nodeTouch.ButtonNode;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.extend.ElfOnClickListener;

public class StretchNode extends LinearLayoutNode{

	public StretchNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	} 
	
	protected ButtonNode mButtonNode; 
	protected PullDownNode mPullDownNode; 
	
	public ButtonNode getButtonNode() {
		return mButtonNode;
	} 
	public PullDownNode getPullDownNode() {
		return mPullDownNode;
	}
	public void setButtonNode(ButtonNode buttonNode) {
		mButtonNode = buttonNode;
	}
	public void setPullDownNode(PullDownNode pullDownNode) {
		mPullDownNode = pullDownNode;
	}
	
	private final void initListener() {
		if(getButtonNode() != null && getPullDownNode() != null) {
			getButtonNode().setOnClickListener(new ElfOnClickListener() {
				public void onClick(int id, ElfEvent event) {
					getPullDownNode().setToggle();
				}
			});
		}
	}
	
	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes(); 
		
		if(mButtonNode == null) {
			final ButtonNode button = new ButtonNode(this, 0);
			button.setName(getDefaultInnerName(button));
			button.addToParent();
			this.setButtonNode(button);
		}
		
		if(mPullDownNode == null) {
			final PullDownNode pullDownNode = new PullDownNode(this, 0);
			pullDownNode.setName(getDefaultInnerName(pullDownNode));
			pullDownNode.addToParent();
			this.setPullDownNode(pullDownNode);
		} 
		
		initListener();
	} 
	
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if(isInnerNode(node)) { 
					if(getButtonNode() == null) {
						if(node instanceof ButtonNode) {
							StretchNode.this.setButtonNode((ButtonNode)node);
						} 
					}
					
					if(getPullDownNode() == null) { 
						if(node instanceof PullDownNode) { 
							StretchNode.this.setPullDownNode((PullDownNode)node);
						} 
					} 
				} 
				return false;
			} 
		}); 
		
		initListener();
	} 
}
