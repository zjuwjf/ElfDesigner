package elfEngine.basic.node.nodeLayout;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLHelper;

public class Layout2DNode extends ElfNode{

	public Layout2DNode(ElfNode father, int ordianl) {
		super(father, ordianl);
		setUseSettedSize(true);
	} 
	
	protected int mMarginTop; 
	protected int mMarginBottom; 
	protected int mMarginLeft; 
	protected int mMarginRight; 
	
	protected int mDimensionX = 2; 
//	protected int mDimensionY = -1; 
	
	protected int mDimensionXSpace = 100; 
	protected int mDimensionYSpace = 100; 
	protected boolean mDrawPoints = false; 
	
	public boolean getDrawPoints() {
		return mDrawPoints;
	} 
	public void setDrawPoints(boolean drawPoints) { 
		mDrawPoints = drawPoints; 
	} 
	protected final static int REF_DrawPoints = DEFAULT_SHIFT; 
	
	public int getMarginTop() {
		return mMarginTop;
	}
	public void setMarginTop(int mMarginTop) {
		this.mMarginTop = mMarginTop;
	}
	protected final static int REF_MarginTop = DEFAULT_SHIFT;

	public int getMarginBottom() {
		return mMarginBottom;
	}
	public void setMarginBottom(int mMarginBottom) {
		this.mMarginBottom = mMarginBottom;
	}
	protected final static int REF_MarginBottom = DEFAULT_SHIFT;
	
	public int getMarginLeft() {
		return mMarginLeft;
	}
	public void setMarginLeft(int mMarginLeft) {
		this.mMarginLeft = mMarginLeft;
	}
	protected final static int REF_MarginLeft = DEFAULT_SHIFT;
	
	public int getMarginRight() {
		return mMarginRight;
	}
	public void setMarginRight(int mMarginRight) {
		this.mMarginRight = mMarginRight;
	}
	protected final static int REF_MarginRight = DEFAULT_SHIFT;
	
	public int getDimensionX() {
		return mDimensionX;
	}
	public void setDimensionX(int mDimensionX) {
		if(mDimensionX > 0) {
			this.mDimensionX = mDimensionX;
		}
	}
	protected final static int REF_DimensionX = DEFAULT_SHIFT;
	
	public int getDimensionXSpace() {
		return mDimensionXSpace;
	}
	public void setDimensionXSpace(int mDimensionXSpace) {
		this.mDimensionXSpace = mDimensionXSpace;
	}
	protected final static int REF_DimensionXSpace = DEFAULT_SHIFT;
	
	public int getDimensionYSpace() {
		return mDimensionYSpace;
	}
	public void setDimensionYSpace(int mDimensionYSpace) {
		this.mDimensionYSpace = mDimensionYSpace;
	}
	protected final static int REF_DimensionYSpace = DEFAULT_SHIFT;
	
	void calcSize() {
		final int children = this.getChildsSize();
		final int x = mMarginLeft + mMarginRight + Math.min(children, mDimensionX) * mDimensionXSpace; 
		//Math.floor(arg0)
		final int y = mMarginTop + mMarginBottom + (int)Math.round(Math.ceil((float)children/mDimensionX )) * mDimensionYSpace; 
		setSize(x, y); 
	} 
	
	void calcPos() { 
		final int count[] = {0}; 
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				ElfPointf pos = getPos(count[0]); 
				node.setPosition(pos); 
				count[0]++; 
				return false;
			} 
		});
	} 
	
	ElfPointf getPos(final int i) {
		ElfPointf size = this.getSize();
		final ElfPointf ret = new ElfPointf(-size.x/2, +size.y/2);
		ret.translate(mMarginLeft + mDimensionXSpace/2, -mMarginTop-mDimensionYSpace/2);
		
		final int x = i%mDimensionX; 
		final int y = i/mDimensionX; 
		
		ret.translate(x*mDimensionXSpace, -y*mDimensionYSpace);
		return ret; 
	}
	
	public void drawSelf() { 
		calcSize(); 
		calcPos(); 
		super.drawSelf();
	} 
	
	public void drawSelected() {
		super.drawSelected();
		if(mDrawPoints && isSelected()) {
			final int children = this.getChildsSize();
			final int width = mDimensionX;
			final int height = (children-1)/mDimensionX + 1;
			
			GLHelper.glBindTexture(0);
			GLHelper.glBlendFunc(BlendMode.BLEND.sourceBlendFunction, BlendMode.BLEND.destinationBlendFunction);
			GL11.glPointSize(8);
			
			GLHelper.glColor4f(0, 0, 1, 1);
			GL11.glBegin(GL11.GL_POINTS);
			for(int i=0; i<children; i++) {
				final ElfPointf p = getPos(i);
				GL11.glVertex3f(p.x, p.y, 0);
			}
			GL11.glEnd();
			
			GLHelper.glColor4f(0, 1, 1, 1);
			GL11.glBegin(GL11.GL_POINTS);
			for(int i=children; i<width*height; i++) {
				final ElfPointf p = getPos(i);
				GL11.glVertex3f(p.x, p.y, 0);
			}
			GL11.glEnd();
		}
	}
}
