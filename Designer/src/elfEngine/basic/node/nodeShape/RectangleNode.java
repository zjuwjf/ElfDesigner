package elfEngine.basic.node.nodeShape;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.DrawHelper;

public class RectangleNode extends ElfNode{

	public RectangleNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setName("#rect"); 
		setUseSettedSize(true); 
		setSize(100, 100); 
	} 
	
	public void drawSelf() { 
		DrawHelper.fillRect(this.getWidth(), this.getHeight());
	} 
} 
