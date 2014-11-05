package elfEngine.basic.node.bar;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.Orientation;

import elfEngine.basic.node.ClipNode;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeLayout.LinearLayoutNode;

public class TrainNode extends ClipNode {

	public TrainNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}

	private final LinearLayoutNode mDrawer = new LinearLayoutNode(this, 0);

	public Orientation getOrientation() {
		return mDrawer.getOrientation();
	}

	public void setOrientation(Orientation or) {
		mDrawer.setOrientation(or);
	}

	protected final static int REF_Orientation = DEFAULT_SHIFT;

	public void setResidArray(String[] resids) {
		mDrawer.removeAllChilds();
		if (resids != null) {
			for (String resid : resids) {
				final ElfNode node = new ElfNode(mDrawer, 0);
				node.addToParent();
				node.setResid(resid);
			}
		}
		mDrawer.layout();
	}

	public String[] getResidArray() {
		final ElfNode[] nodes = mDrawer.getChilds();
		final String[] ret = new String[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			ret[i] = nodes[i].getResid();
		}
		return ret;
	}

	protected final static int REF_ResidArray = DEFAULT_SHIFT;

	private float mSpeed = 10;

	public void setSpeed(float speed) {
		mSpeed = speed;
	}

	public float getSpeed() {
		return mSpeed;
	}

	protected final static int REF_Speed = DEFAULT_SHIFT;

	private float mValue = 0;

	public void setInnerValue(float value) {
		mValue = value;
	}

	public float getInnerValue() {
		return mValue;
	}

	protected final static int REF_InnerValue = DEFAULT_SHIFT;

	public void calc(float dt) {
		mValue += mSpeed * dt * 0.001f;
	}

	public void drawSelf() {
		drawByValueAndOrientation(mValue, getOrientation());
	}

	private final void drawByValueAndOrientation(float value, Orientation ori) {
		final float maxLength;
		final float myLength;
		float remain;
		final ElfPointf stepPos = new ElfPointf();

		if (ori.isHorizontal()) {
			maxLength = mDrawer.getWidth();
			myLength = this.getWidth();

			stepPos.setPoint(-maxLength, 0);

			remain = value % maxLength;
			
			if(maxLength>0) {
				while(remain < 0) {
					remain += maxLength;
				}
			}
			
			mDrawer.setPosition(remain + myLength / 2 - maxLength / 2, 0);
		} else {
			maxLength = mDrawer.getHeight();
			myLength = this.getHeight();

			stepPos.setPoint(0, -maxLength);

			remain = value % maxLength;
			if(maxLength>0) {
				while(remain < 0) {
					remain += maxLength;
				}
			}
			
			
			mDrawer.setPosition(0, remain + myLength / 2 - maxLength / 2);
		}

		float space = myLength + remain;
		while (space > 0) {
			mDrawer.drawSprite();
			space -= maxLength;
			mDrawer.translate(stepPos.x, stepPos.y);
		}
	}
}
