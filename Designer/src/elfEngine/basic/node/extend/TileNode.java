package elfEngine.basic.node.extend;

import com.ielfgame.stupidGame.enumTypes.LayoutMode;

import elfEngine.basic.node.ElfNode;

public class TileNode extends ElfNode{
	
	private LayoutMode mLayoutMode = LayoutMode.Left2Right;
	
	public LayoutMode getLayoutMode() {
		return mLayoutMode;
	}

	public void setLayoutMode(LayoutMode mLayoutPrior) {
		if(mLayoutPrior != null && mLayoutPrior != this.mLayoutMode){
			this.mLayoutMode = mLayoutPrior;
			updateChildPos();
		} 
	} 
	
	public TileNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
		setUseSettedSize(true);
	} 
	
	public void myRefresh() {
		super.myRefresh();
		updateChildPos();
	}
	
	protected void updateChildPos(){		
		updateSize();
		final float w = getWidth(), h = getHeight();
		final float [] x = new float[1], y = new float[1];
		switch(mLayoutMode) {
		case Left2Right:
			x[0] = -w/2;
			y[0] = 0;
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					node.setPosition(x[0]+node.getWidth()/2, y[0]);
					x[0] += node.getWidth();
					return false;
				}
			});
			break;
		case Right2Left:
			x[0] = w/2;
			y[0] = 0;
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					node.setPosition(x[0]-node.getWidth()/2, y[0]);
					x[0] -= node.getWidth();
					return false;
				}
			});
			break;
		case Bottom2Top:
			x[0] = 0;
			y[0] = -h/2;
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					node.setPosition(x[0], y[0]+node.getHeight()/2);
					y[0] += node.getHeight();
					return false;
				}
			});
			break;
		case Top2Bottom:
			x[0] = 0;
			y[0] = h/2;
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					node.setPosition(x[0], y[0]-node.getHeight()/2);
					y[0] -= node.getHeight();
					return false;
				}
			});
			break;
		}
	} 
	
	protected void updateSize() {
		final float [] selectWidth = new float[1], selectHeight = new float[1];
		selectWidth[0] = selectHeight[0] = 0;
		
		switch(mLayoutMode) {
		case Left2Right:
		case Right2Left:
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					selectWidth[0] += node.getWidth();
					selectHeight[0] = Math.max(selectHeight[0], node.getHeight());
					return false;
				}
			});
			break;
		case Bottom2Top:
		case Top2Bottom:
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					selectWidth[0] = Math.max(selectWidth[0], node.getWidth());
					selectHeight[0] += node.getHeight();
					return false;
				}
			});
			break;
		}
		
		setSize(selectWidth[0], selectHeight[0]);
	}
	
	protected void addChild(final ElfNode child){
		super.addChild(child);
		updateChildPos();
	} 
	
	protected void addChild(final ElfNode child, int index){
		super.addChild(child, index);
		updateChildPos();
	} 
	
	protected boolean removeChild(final ElfNode child){
		final boolean ret = super.removeChild(child);
		if(ret){
			updateChildPos();
		} 
		return ret;
	} 
	
}
