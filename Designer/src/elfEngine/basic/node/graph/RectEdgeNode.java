package elfEngine.basic.node.graph;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.DrawHelper;

public class RectEdgeNode extends ElfNode{

	public RectEdgeNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setColor(new ElfColor(0, 0, 1, 0.3f));
	} 

	public void drawSelf() {
		final ElfColor color = this.getColor();
		final ElfPointf size = this.getSize();
		
		DrawHelper.setColor(color.red, color.green, color.blue, color.alpha);
		DrawHelper.fillRect(size.x, size.y, 0, 0);
		
		DrawHelper.setColor(color.red, color.green, color.blue, 1);
		DrawHelper.drawRect(size.x, size.y, 0, 0, 1);
	}
}
