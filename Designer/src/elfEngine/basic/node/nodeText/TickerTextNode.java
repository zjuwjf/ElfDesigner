package elfEngine.basic.node.nodeText;

import elfEngine.basic.node.ElfNode;

public class TickerTextNode extends TextNode{ 
	
	public TickerTextNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
	} 
	
	protected String mTextString;
	protected float mDuration;
	
}
