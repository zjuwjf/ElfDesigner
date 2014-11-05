package elfEngine.basic.node.fit;

import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class FitSizeNode extends ElfNode {

	/***
	 * setSize child and self
	 */
	// size fit
	private boolean mSizeFitEnable;
	private EdgeFitType mSizeEdgeFitType = EdgeFitType.BothXY;
	private final ElfPointf mSizeWeightPoint = new ElfPointf();
	private final ElfPointf mSizeValuePoint = new ElfPointf();
	private boolean mEffectChildrenSize;

	public FitSizeNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}

	public boolean getSizeFitEnable() {
		return mSizeFitEnable;
	}

	public void setSizeFitEnable(boolean mSizeFitEnable) {
		this.mSizeFitEnable = mSizeFitEnable;
		effect();
	}
	protected final static int REF_SizeFitEnable = DEFAULT_SHIFT;
	
	public EdgeFitType getSizeEdgeFitType() {
		return mSizeEdgeFitType;
	}

	public void setSizeEdgeFitType(EdgeFitType mSizeEdgeFitType) {
		this.mSizeEdgeFitType = mSizeEdgeFitType;
		effect();
	}
	protected final static int REF_SizeEdgeFitType = DEFAULT_SHIFT;

	public ElfPointf getSizeWeightPoint() {
		return new ElfPointf(mSizeWeightPoint);
	}

	public void setSizeWeightPoint(ElfPointf mSizeWeightPoint) {
		this.mSizeWeightPoint.set(mSizeWeightPoint);
		effect();
	}
	protected final static int REF_SizeWeightPoint = DEFAULT_SHIFT;

	public ElfPointf getSizeValuePoint() {
		return new ElfPointf(mSizeValuePoint);
	}

	public void setSizeValuePoint(ElfPointf mSizeValuePoint) {
		this.mSizeValuePoint.set(mSizeValuePoint);
		effect();
	}
	protected final static int REF_SizeValuePoint = DEFAULT_SHIFT;
	
	/***
	 * for list , map, clip...
	 */
	public boolean getEffectChildrenSize() {
		return mEffectChildrenSize;
	}

	public void setEffectChildrenSize(boolean mEffectChildrenSize) {
		this.mEffectChildrenSize = mEffectChildrenSize;
		effect();
	}
	protected final static int REF_EffectChildrenSize = DEFAULT_SHIFT;
	
	public void calc(float dt) {
		super.calc(dt);
		effect();
	}
	
	protected final void effect() { 
		if (this.getSizeFitEnable()) { 
			final int sx = PowerMan.getSingleton(ProjectSetting.class).getLogicWidth();
			final int sy = PowerMan.getSingleton(ProjectSetting.class).getLogicHeight();

			final float x = EdgeFitType.getValueX(sx, sy, this.mSizeWeightPoint.x, this.mSizeWeightPoint.y, this.mSizeValuePoint.x, this.mSizeValuePoint.y, mSizeEdgeFitType);
			final float y = EdgeFitType.getValueY(sx, sy, this.mSizeWeightPoint.x, this.mSizeWeightPoint.y, this.mSizeValuePoint.x, this.mSizeValuePoint.y, mSizeEdgeFitType);
			
			this.setSize(x, y);
			
			if(getEffectChildrenSize()) { 
				final ElfNode [] nodes = this.getChilds();
				for(final ElfNode node : nodes) { 
					node.setSize(x, y);
				} 
			}
		}
	}
}
