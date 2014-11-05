package elfEngine.basic.node.nodeAnimate.timeLine;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.DrawHelper;
import elfEngine.opengl.GLHelper;

public class BoneNode extends ElfNode {
	
	private static boolean sShowBones = false;
	public static void setShowBones(boolean show) {
		sShowBones = show;
	}
	public static boolean getShowBones() {
		return sShowBones;
	}
	
	public BoneNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	private float mLength;
	public void setLength(float length) {
		this.mLength = length;
	}
	public float getLength() {
		return this.mLength;
	}
	protected final static int REF_Length = DEFAULT_SHIFT;
	
	public void drawSelf() { 
	} 
	
	public void drawSelected() { 
		super.drawSelected();
		if(sShowBones) {
			GLHelper.glColor4f(0, 1, 0, 1); 
			DrawHelper.fillRect(mLength, 2, mLength/2, 0); 
			GLHelper.glColor4f(1, 0, 0, 1); 
			DrawHelper.fillRect(6, 6); 
		} 
	}
}
