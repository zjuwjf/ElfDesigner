package elfEngine.basic.node.gltest;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.BlendMode;

public class StencilBeginNode extends ElfNode {

	private boolean mInvisibleSelf;
	private boolean mStencilInside;
	private boolean mAlphaEnable;

	public StencilBeginNode(ElfNode father, int ordinal) {
		super(father, ordinal);

		this.setInvisibleSelf(true);
		this.setStencilInside(true);
	}

	public void setInvisibleSelf(boolean invisible) {
		mInvisibleSelf = invisible;
	}

	public boolean getInvisibleSelf() {
		return mInvisibleSelf;
	}
	protected final static int REF_InvisibleSelf = DEFAULT_SHIFT;
	
	public void setStencilInside(boolean stencilInner) {
		mStencilInside = stencilInner;
	}
	public boolean getStencilInside() {
		return mStencilInside;
	}
	protected final static int REF_StencilInside = DEFAULT_SHIFT;
	
	public void setAlphaEnable(boolean enable) {
		mAlphaEnable = enable;
	}
	
	public boolean getAlphaEnable() {
		return mAlphaEnable;
	}
	protected final static int REF_AlphaEnable = DEFAULT_SHIFT;

	public void drawSprite() {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		
		if(mAlphaEnable) {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.5f);
		}
		
		if(mStencilInside) {
			GL11.glClearStencil(0);
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 1);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		} else {
			GL11.glClearStencil(1);
			GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 0, 1);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		}

		if (mInvisibleSelf) {
			this.iterateChildsDeep(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					node.setColor(new ElfColor(0, 0, 0, 1));
					node.setBlendMode(BlendMode.ACTIVLE);
					return false;
				}
			});
		}

		super.drawSprite();
		
		if(mAlphaEnable) { 
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		} 
		
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
	}
}
