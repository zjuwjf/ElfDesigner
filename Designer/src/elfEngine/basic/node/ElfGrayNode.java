package elfEngine.basic.node;

import elfEngine.opengl.GLManage;
import elfEngine.opengl.Texture;

public class ElfGrayNode extends ElfNode {
	
	private boolean mGrayEnabled = true;
	
	public ElfGrayNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	public void drawSelf() {
		if(mGrayEnabled) {
			GLManage.draw(getResid(), getGLId(), Texture.IMAGE_GRAY);
		} else {
			GLManage.draw(getResid(), getGLId());
		}
	}
	
	public void setGrayEnabled(boolean b) {
		mGrayEnabled = b;
	}
	public boolean getGrayEnabled() {
		return mGrayEnabled;
	}
	protected final static int REF_GrayEnabled = DEFAULT_SHIFT;
	
}
