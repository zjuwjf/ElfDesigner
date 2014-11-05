package elfEngine.basic.node.bar;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;

public class Joint9Node extends ElfNode {

	/***
	 * LT,MT,RT, 0,1,2 LM,MM,RM, 3,4,5 LB,MB,BB, 6,7,8
	 */
	private static final int Index_LT = 0;
	private static final int Index_MT = 1;
	private static final int Index_RT = 2;

	private static final int Index_LM = 3;
	private static final int Index_MM = 4;
	private static final int Index_RM = 5;

	private static final int Index_LB = 6;
	private static final int Index_MB = 7;
	private static final int Index_RB = 8;

	protected final ElfNode[] mNodeArray = { new ElfNode(this, 0), new ElfNode(this, 0), new ElfNode(this, 0), new ElfNode(this, 0), new ElfNode(this, 0), new ElfNode(this, 0), new ElfNode(this, 0), new ElfNode(this, 0), new ElfNode(this, 0), };

	public Joint9Node(ElfNode father, int ordinal) {
		super(father, ordinal);
		init();
		
		this.getResidsSet();
	}
	
	public String[] getSelfResids() {
		final String [] array = new String [9];
		for(int i=0; i<array.length; i++) {
			array[i] = mNodeArray[i].getResid();
		}
		return array;
	}
	
	public void setSize(ElfPointf size) {
		super.setSize(size);
		updateNodeArray();
	}
	
	public void setResid(String resid) {
	}

	public void drawSelf() {
		for(ElfNode node : mNodeArray) {
			node.drawSprite();
		}
	}
	
	private final void init() {
		this.setUseSettedSize(true);
		
		for(ElfNode node : mNodeArray) {
			node.setDefaultHeight(0);
			node.setDefaultWidth(0);
		}
	}
	
	private final void updateNodeArray() {
		final float width = this.getWidth();
		final float height = this.getHeight();
		
		updateNodeX(width, mNodeArray[Index_LT],mNodeArray[Index_MT],mNodeArray[Index_RT]);
		updateNodeX(width, mNodeArray[Index_LM],mNodeArray[Index_MM],mNodeArray[Index_RM]);
		updateNodeX(width, mNodeArray[Index_LB],mNodeArray[Index_MB],mNodeArray[Index_RB]);
		
		updateNodeY(height, mNodeArray[Index_LT],mNodeArray[Index_LM],mNodeArray[Index_LB]);
		updateNodeY(height, mNodeArray[Index_MT],mNodeArray[Index_MM],mNodeArray[Index_MB]);
		updateNodeY(height, mNodeArray[Index_RT],mNodeArray[Index_RM],mNodeArray[Index_RB]);
	}
	
	private static final void updateNodeX(final float w, final ElfNode l, final ElfNode m, final ElfNode r) {
		final float lw = l.getWidth();
		final float mw = m.getWidth();
		final float rw = r.getWidth();
		
		l.setPositionX(-w/2+lw/2);
		if(mw > 0) {
			m.setPositionX((lw-rw)/2);
			m.setScale((0.5f+w-lw-rw)/mw, m.getScale().y);
		}
		r.setPositionX(+w/2-rw/2);
	}
	
	private static final void updateNodeY(final float h, final ElfNode t, final ElfNode m, final ElfNode b) {
		final float th = t.getHeight();
		final float mh = m.getHeight();
		final float bh = b.getHeight();
		
		t.setPositionY(+h/2-th/2);
		if(mh > 0) {
			m.setPositionY((bh-th)/2);
			m.setScale(m.getScale().x, (0.5f+h-th-bh)/mh);
		}
		b.setPositionY(-h/2+bh/2);
	}

	public String getLTResid() {
		return mNodeArray[Index_LT].getResid();
	}

	public void setLTResid(String resid) {
		mNodeArray[Index_LT].setResid(resid);
		updateNodeArray();
	}

	public String getMTResid() {
		return mNodeArray[Index_MT].getResid();
	}

	public void setMTResid(String resid) {
		mNodeArray[Index_MT].setResid(resid);
		updateNodeArray();
	}

	public String getRTResid() {
		return mNodeArray[Index_RT].getResid();
	}

	public void setRTResid(String resid) {
		mNodeArray[Index_RT].setResid(resid);
		updateNodeArray();
	}

	public String getLMResid() {
		return mNodeArray[Index_LM].getResid();
	}

	public void setLMResid(String resid) {
		mNodeArray[Index_LM].setResid(resid);
		updateNodeArray();
	}

	public String getMMResid() {
		return mNodeArray[Index_MM].getResid();
	}

	public void setMMResid(String resid) {
		mNodeArray[Index_MM].setResid(resid);
		updateNodeArray();
	}

	public String getRMResid() {
		return mNodeArray[Index_RM].getResid();
	}

	public void setRMResid(String resid) {
		mNodeArray[Index_RM].setResid(resid);
		updateNodeArray();
	}

	public String getLBResid() {
		return mNodeArray[Index_LB].getResid();
	}

	public void setLBResid(String resid) {
		mNodeArray[Index_LB].setResid(resid);
		updateNodeArray();
	}

	public String getMBResid() {
		return mNodeArray[Index_MB].getResid();
	}

	public void setMBResid(String resid) {
		mNodeArray[Index_MB].setResid(resid);
		updateNodeArray();
	}

	public String getRBResid() {
		return mNodeArray[Index_RB].getResid();
	}

	public void setRBResid(String resid) {
		mNodeArray[Index_RB].setResid(resid);
		updateNodeArray();
	}

	protected final static int REF_LTResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	protected final static int REF_LMResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	protected final static int REF_LBResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	
	protected final static int REF_MTResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	protected final static int REF_MMResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	protected final static int REF_MBResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	
	protected final static int REF_RTResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	protected final static int REF_RMResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	protected final static int REF_RBResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;

}
