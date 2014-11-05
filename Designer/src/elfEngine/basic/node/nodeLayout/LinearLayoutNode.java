package elfEngine.basic.node.nodeLayout;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.Orientation;

import elfEngine.basic.node.ElfNode;


public class LinearLayoutNode extends ElfNode{
	
	private Orientation mOrientation = Orientation.Horizontal;
	public Orientation getOrientation() {
		return mOrientation;
	} 
	public void setOrientation(Orientation orientation) { 
		if(orientation != null) { 
			this.mOrientation = orientation;
			layout();
		} 
	} 
	protected final static int REF_Orientation = DEFAULT_SHIFT;
	
	public LinearLayoutNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
		this.setUseSettedSize(true);
		
		this.setAutoLayout(true);
		this.setCalcSizeWithScale(true);
	} 
	
	public void setUseSettedSize(boolean b) {
		super.setUseSettedSize(true);
	}
	
	public void myRefresh() {
		super.myRefresh();
		layout();
	}
	
	public void onCreateRequiredNodes() { 
		super.onCreateRequiredNodes();
		layout();
	} 
	
	//for xml...after
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		layout();
	} 
	
	public void calc(final float pMsElapsed) {
		super.calc(pMsElapsed); 
		if(mAutoLayout) { 
			layout(); 
		} 
	} 
	
	private boolean mCalcSizeWithScale;
	public boolean getCalcSizeWithScale() { 
		return mCalcSizeWithScale; 
	}
	public void setCalcSizeWithScale(boolean mCalcSizeWithScale) {
		this.mCalcSizeWithScale = mCalcSizeWithScale;
	}
	protected final static int REF_CalcSizeWithScale = DEFAULT_SHIFT; 
	
	private boolean mAutoLayout = false;
	public boolean getAutoLayout() {
		return mAutoLayout;
	}
	public void setAutoLayout(boolean mAutoLayout) {
		this.mAutoLayout = mAutoLayout;
	} 
	protected final static int REF_AutoLayout = DEFAULT_SHIFT; 
	
	public void layout() {
		final float [] width = new float[1], height = new float[1];
		
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if( mOrientation.isHorizontal() ) { 
					float dx = node.getSize().x;
					float dy = node.getSize().y;
					
					if(mCalcSizeWithScale) {
						dx *= node.getScale().x;
						dy *= node.getScale().y;
					} 
					
					width[0] += dx;
					height[0] = Math.max(height[0], dy);
				} else {
					float dx = node.getSize().x;
					float dy = node.getSize().y;
					
					if(mCalcSizeWithScale) { 
						dx *= node.getScale().x;
						dy *= node.getScale().y;
					}
					
					width[0] = Math.max(width[0], dx);
					height[0] += dy;
				}
				return false;
			} 
		});
		
		this.setSize(width[0], height[0]);
		
		final float [] count = new float[1];
		
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if(mOrientation == Orientation.Horizontal) { 
					float dx = node.getSize().x; 
					
					if(mCalcSizeWithScale) { 
						dx *= node.getScale().x;
					} 
					
					final ElfPointf anchor = node.getAnchorPosition();
					
					node.setPosition(count[0]+dx*anchor.x-width[0]/2, 0);
					count[0]+=dx;
					
				} else if(mOrientation == Orientation.Horizontal_R2L) {
					float dx = node.getSize().x;
					
					if(mCalcSizeWithScale) {
						dx *= node.getScale().x;
					} 
					
					final ElfPointf anchor = node.getAnchorPosition();
					
					node.setPosition(count[0]-dx*(1-anchor.x)+width[0]/2, 0);
					count[0]-=dx;
					
				} else if(mOrientation == Orientation.Vertical_B2T) {
					float dy = node.getSize().y;
					
					if(mCalcSizeWithScale) { 
						dy *= node.getScale().y;
					} 
					
					final ElfPointf anchor = node.getAnchorPosition();
					
					node.setPosition(0, count[0]+dy*(anchor.y)-height[0]/2);
					count[0]+=dy;
				} else {
					float dy = node.getSize().y;
					
					if(mCalcSizeWithScale) { 
						dy *= node.getScale().y;
					} 
					
					final ElfPointf anchor = node.getAnchorPosition();
					
					node.setPosition(0, count[0]-dy*(1-anchor.y)+height[0]/2);
					count[0]-=dy;
				}
				return false;
			}
		});
	} 
} 
