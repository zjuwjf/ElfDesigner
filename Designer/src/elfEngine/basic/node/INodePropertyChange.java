package elfEngine.basic.node;

import elfEngine.basic.node.nodeAnimate.timeLine.NodePropertyType;

public interface INodePropertyChange {
	
//	public enum 
	public boolean onNodePropertyChangeBefore(ElfNode node, NodePropertyType type);
	public void onNodePropertyChange(ElfNode node, NodePropertyType type);
}
