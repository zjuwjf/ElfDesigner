package elfEngine.basic.node.nodeTouch;

import com.ielfgame.stupidGame.enumTypes.Orientation;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.touch.GestureEventDecoder;
import elfEngine.basic.touch.GestureEventDecoder.IOnFling;

public class GestureNode extends TouchNode implements IOnFling{

	public GestureNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		this.setTouchDecoder(new GestureEventDecoder(this, this));
	}
	
	private Orientation mOrientation = Orientation.Horizontal;
	public Orientation getOrientation() {
		return mOrientation;
	} 
	public void setOrientation(Orientation mOrientation) {
		this.mOrientation = mOrientation;
	}
	protected final static int REF_Orientation = DEFAULT_SHIFT;
	
	@Override
	public void onFling(float distanceX, float distanceY, float velocityX, float velocityY) {
		switch (mOrientation) {
		case Horizontal:
		case Horizontal_R2L:
			break;
		case Vertical:
		case Vertical_B2T:
			break;
		}
		
		System.err.println("GestureNode onFling");
	} 
	
	
	
	
}
