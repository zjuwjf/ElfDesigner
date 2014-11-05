package elfEngine.basic.node.nodeList;

import elfEngine.basic.node.ClipNode;
import elfEngine.basic.node.ElfNode;

public class NoticeLabelNode extends ElfNode{

	public NoticeLabelNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		this.setUseSettedSize(true);
		this.setSize(200, 50);
	}
	
	protected float mVelocity = 60; 
	public void setNoticeLabelVelocity(float velocity) {
		mVelocity = velocity;
	} 
	public float getNoticeLabelVelocity() {
		return mVelocity;
	}
	protected final static int REF_NoticeLabelVelocity = DEFAULT_SHIFT;
	
//	protected boolean mLoopOrRemoveOnDisappear;
//	public void setLoopOrRemoveOnDisappear(boolean loop) {
//		mLoopOrRemoveOnDisappear = loop;
//	} 
//	public boolean getLoopOrRemoveOnDisappear() {
//		return mLoopOrRemoveOnDisappear;
//	} 
//	protected final static int REF_LoopOrRemoveOnDisappear = DEFAULT_SHIFT;
	
	protected int mMaxChildCapacity = 100;
	public void setMaxChildCapacity(int max) {
		mMaxChildCapacity = max;
	} 
	public int getMaxChildCapacity() {
		return mMaxChildCapacity;
	}
	protected final static int REF_MaxChildCapacity = DEFAULT_SHIFT; 
	
	public void calc(float dt) { 
		super.calc(dt); 
		final float dx = -mVelocity * dt /1000;
		final float width = this.getSize().x;
		final ElfNode[] prev = new ElfNode[1]; 
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if(prev[0] == null) { 
					node.setPositionY(0); 
					node.translate(dx, 0); 
				} else { 
					node.setPosition(prev[0].getPosition().x+prev[0].getSize().x/2+node.getSize().x/2+width, 0); 
				} 
				prev[0] = node;
				return false;
			} 
		});
		
		if(prev[0] != null) {
			final float x = prev[0].getPosition().x;
			final float w = prev[0].getSize().x;
			if(x < -width/2-w/2) {
				prev[0] = null;
				this.iterateChilds(new IIterateChilds() { 
					public boolean iterate(ElfNode node) { 
						if(prev[0] == null) { 
							node.setPosition(width/2 + node.getSize().x/2,0); 
						} else { 
							node.setPosition(prev[0].getPosition().x+prev[0].getSize().x/2+node.getSize().x/2+width, 0); 
						} 
						prev[0] = node;
						return false;
					} 
				}); 
			} 
		} 
	} 
	
	protected void drawBefore() { 
		ClipNode.pushScissor(this);
		super.drawBefore(); 
//		final float h = getHeight(); 
//		final float w = getWidth(); 
//		
//		double planeTop[] = { 0.0f, -1.0f, 0.0f, 0 + h / 2 };
//		double planeBottom[] = { 0.0f, 1.0f, 0.0f, -0 + h / 2 };
//		double planeLeft[] = { 1.0f, 0.0f, 0.0f, -0 + w / 2 };
//		double planeRight[] = { -1.0f, 0.0f, 0.0f, 0 + w / 2 };
//		
//		GLHelper.pushClip(planeTop, planeBottom, planeLeft, planeRight); 
		
	} 
	
	protected void drawAfter() { 
		super.drawAfter(); 
		ClipNode.popScissor();
//		GLHelper.popClipPanel(); 
	} 
} 
