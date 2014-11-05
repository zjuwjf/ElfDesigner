package elfEngine.basic.node.nodeTouch;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.basic.touch.ShieldEventDecoder;

public class ShieldNode extends TouchNode{
	private final ShieldEventDecoder mShieldEventDecoder = new ShieldEventDecoder();
	
	private boolean mShieldInOrOut = true; 
	public void setShieldInOrOut(boolean ShieldInOrOut) {
		mShieldInOrOut = ShieldInOrOut;
	} 
	public boolean getShieldInOrOut() {
		return mShieldInOrOut;
	} 
	protected final static int REF_ShieldInOrOut = DEFAULT_SHIFT;
	
	public boolean isInSelectSize(ElfEvent event) {
		return mShieldInOrOut == super.isInSelectSize(event);
	}
	
	public ShieldNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setTouchEnable(true);
		
		this.setName("#shield");
		
		setTouchDecoder(mShieldEventDecoder);
		
		setPriorityLevel(SHIELD_PRIORITY);
	}
} 
