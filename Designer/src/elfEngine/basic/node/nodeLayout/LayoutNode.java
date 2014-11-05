package elfEngine.basic.node.nodeLayout;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.LayoutMode;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.DrawHelper;
import elfEngine.opengl.GLHelper;

public class LayoutNode extends ElfNode{
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
	protected final static int REF_LayoutMode = DEFAULT_SHIFT;
	
	private int mSpace = 100;
	public int getLayoutSpace() {
		return mSpace;
	}
	public void setLayoutSpace(int space) {
		if(mSpace != space) {
			this.mSpace = space;
			updateChildPos();
		} 
	}
	protected final static int REF_LayoutSpace = DEFAULT_SHIFT;

	public void myRefresh(){
		super.myRefresh();
		updateChildPos();
	}
	
	public void drawSelected(){
		super.drawSelected();
		
		if(isSelected()){
			final int childs = this.getChildsSize();
			for(int i=0; i<childs; i++){
				GLHelper.glPushMatrix();
				
				final ElfPointf pos = getPos(i);
				GLHelper.glTranslatef(pos.x, pos.y, 0);
				
				GLHelper.glColor4f(0, 1, 1, 1);
				DrawHelper.fillRect(4, 4);
				
				GLHelper.glPopMatrix();
			} 
		} 
	}

	public LayoutNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		setName("#layout");
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
		updateChildPos();
		return ret;
	} 
	
	protected void updateChildPos(){
		final int [] index = new int[1];
		index[0] = -1;
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				node.setPosition( getPos(++index[0]) );
				return false;
			}
		});
	}
	
	
	public ElfPointf getPos(int index){
		final ElfPointf ret = new ElfPointf();
		final int childs = this.getChildsSize();
		switch (mLayoutMode) {
		case Left2Right:
			ret.x = (-(childs-1)/2f+index)*mSpace;
			break;
		case Right2Left:
			ret.x = ((childs-1)/2f-index)*mSpace;
			break;
		case Bottom2Top:
			ret.y = (-(childs-1)/2f+index)*mSpace;
			break;
		case Top2Bottom:
			ret.y = ((childs-1)/2f-index)*mSpace;
			break;
		case Left2RightFixed:
			ret.x = (+index)*mSpace;
			break;
		case Right2LeftFixed:
			ret.x = (-index)*mSpace;
			break;
		case Bottom2TopFixed:
			ret.y = (+index)*mSpace;
			break;
		case Top2BottomFixed:
			ret.y = (-index)*mSpace;
			break;
		} 
		return ret;
	} 
}
