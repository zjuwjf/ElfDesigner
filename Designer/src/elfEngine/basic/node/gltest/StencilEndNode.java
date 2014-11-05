package elfEngine.basic.node.gltest;

import org.lwjgl.opengl.GL11;

import elfEngine.basic.node.ElfNode;

public class StencilEndNode extends ElfNode{

	public StencilEndNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	public void drawSprite() {
		super.drawSprite();
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
}
