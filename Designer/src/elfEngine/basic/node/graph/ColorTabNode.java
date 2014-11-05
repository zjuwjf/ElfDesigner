package elfEngine.basic.node.graph;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.TabNode;
import elfEngine.opengl.DrawHelper;

public class ColorTabNode extends TabNode{
	
	private final ElfColor mNormalColor = new ElfColor(1, 1, 1, 1);
	private final ElfColor mPressedColor = new ElfColor(1,0,0,1);
	private final ElfColor mInvalidColor = new ElfColor(0.7f,0.7f,0.7f,0.7f);
	
	public void setColors(ElfColor [] colors) {
		final ElfColor [] oldColors = {mNormalColor, mPressedColor, mInvalidColor};
		if(colors != null) {
			for(int i=0; i<colors.length&&i<oldColors.length; i++) {
				final ElfColor c = colors[i];
				if(c != null) {
					oldColors[i].set(c);
				}
			}
		}
	}
	
	public ElfColor [] getColors() {
		return new ElfColor[]{mNormalColor, mPressedColor, mInvalidColor};
	}
	
	protected final static int REF_Colors = DEFAULT_SHIFT;

	public ColorTabNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	protected void drawPic() { 
		final ElfPointf size = this.getSize();
		
		switch (mButtonState) { 
		case Normal:
			DrawHelper.setColor(mNormalColor.red, mNormalColor.green, mNormalColor.blue, mNormalColor.alpha);
			break; 
		case Pressed:
			DrawHelper.setColor(mPressedColor.red, mPressedColor.green, mPressedColor.blue, mPressedColor.alpha);
			break;
		case Invalid:
			DrawHelper.setColor(mInvalidColor.red, mInvalidColor.green, mInvalidColor.blue, mInvalidColor.alpha);
			break; 
		} 
		
		DrawHelper.fillRect(size.x, size.y, 0, 0);
		
		switch (mButtonState) { 
		case Normal:
			DrawHelper.setColor(mNormalColor.red, mNormalColor.green, mNormalColor.blue, 1);
			break; 
		case Pressed:
			DrawHelper.setColor(mPressedColor.red, mPressedColor.green, mPressedColor.blue, 1);
			break;
		case Invalid:
			DrawHelper.setColor(mInvalidColor.red, mInvalidColor.green, mInvalidColor.blue, 1);
			break; 
		} 
		
		DrawHelper.drawRect(size.x, size.y, 0, 0, 1);
	}
}
