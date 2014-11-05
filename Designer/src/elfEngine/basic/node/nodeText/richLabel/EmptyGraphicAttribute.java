package elfEngine.basic.node.nodeText.richLabel;

import java.awt.Graphics2D;
import java.awt.font.GraphicAttribute;

public class EmptyGraphicAttribute extends GraphicAttribute{

	protected EmptyGraphicAttribute() {
		super(0);
	}

	@Override
	public void draw(Graphics2D graphics, float x, float y) {
		
	}

	@Override
	public float getAdvance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getAscent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDescent() {
		// TODO Auto-generated method stub
		return 0;
	}

}
