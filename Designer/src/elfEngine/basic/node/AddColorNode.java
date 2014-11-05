package elfEngine.basic.node;

import com.ielfgame.stupidGame.data.ElfColor;

public class AddColorNode extends ElfNode {

	private final ElfColor mAddColor = new ElfColor(0, 0, 0, 0);

	public AddColorNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}

	public void setAddColor(ElfColor color) {
		mAddColor.set(color);
	}

	public ElfColor getAddColor() {
		return new ElfColor(mAddColor);
	}

	protected final static int REF_AddColor = DEFAULT_SHIFT;
}
