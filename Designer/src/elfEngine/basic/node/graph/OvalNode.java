package elfEngine.basic.node.graph;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.DrawHelper;

public class OvalNode extends ElfNode{

	public OvalNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	public void drawSelf() {
		final ElfPointf size = this.getSize();
		DrawHelper.fillOval(size.x, size.y);
	}
}
