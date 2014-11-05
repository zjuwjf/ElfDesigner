package elfEngine.basic.node.graph;

import com.ielfgame.stupidGame.enumTypes.Orientation;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.bar.IBarNode;

public class ColorBarNode extends RectEdgeNode implements IBarNode{ 
	
	private Orientation mOrientation = Orientation.Horizontal;
	
	public ColorBarNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setUseSettedSize(true);
		this.setSize(100, 20);
	}
	
	public void setOrientation(Orientation orientation) {
		mOrientation = orientation;
	}
	public Orientation getOrientation() {
		return mOrientation;
	}
	protected final static int REF_Orientation = DEFAULT_SHIFT;

	public float getLength() {
		if(mOrientation.isHorizontal()) {
			return getSize().x;
		} else {
			return getSize().y;
		}
	}

	public void setLength(float length) {
		if(mOrientation.isHorizontal()) {
			setSize(length, getSize().y);
		} else {
			setSize(getSize().x, length);
		}
	} 
	protected final static int REF_Length = DEFAULT_SHIFT;
	
}
