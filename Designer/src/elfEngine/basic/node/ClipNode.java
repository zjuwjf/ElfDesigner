package elfEngine.basic.node;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.nodeList.IClipable;
import elfEngine.graphics.PointF;
import elfEngine.opengl.GLHelper;

public class ClipNode extends ElfNode implements IClipable{
	
	public ClipNode(ElfNode father, int ordinal) {
		super(father, ordinal);

		setUseSettedSize(true);
	}
	
	boolean mIsClipable = true;
	public boolean getIsClipable() {
		return mIsClipable;
	}
	public void setIsClipable(boolean mIsClipable) { 
		this.mIsClipable = mIsClipable;
	}
	protected final static int REF_IsClipable = DEFAULT_SHIFT;
	
	protected void drawBefore() {
		super.drawBefore();
		if (mIsClipable) { 
			pushScissor(this);
		} 
	} 
	
	protected void drawAfter() { 
		super.drawAfter();
		if (mIsClipable) { 
			popScissor(); 
		} 
	}
	
	public static void pushScissor(final ElfNode node) {
		final ElfPointf screenPos = node.getPositionInScreen();
		
		final ElfPointf scale = node.getScale();
		
		ElfNode parent = node.getParent();
		while(parent != null) {
			final ElfPointf pScale = parent.getScale();
			scale.x *= pScale.x;
			scale.y *= pScale.y;

			parent = parent.getParent();
		}

		final ElfPointf size = node.getSize();
		final float width = Math.abs(size.x * scale.x);
		final float height = Math.abs(size.y * scale.y);
		
		final ElfPointf ar = node.getAnchorPosition();
		final float preX = (-ar.x * (width));
		final float preY = (-ar.y * (height));
		
		GLHelper.pushScissor(screenPos.x+preX, screenPos.y+preY, width, height);
	} 
	
	public static void popScissor() { 
		GLHelper.popScissor();
	}
	
	public boolean isInClipSize(float x, float y) { 
		return mIsClipable && isInSize(new PointF(x, y));
	} 
}
