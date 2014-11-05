package elfEngine.basic.node.fit;

import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class FitScaleNode extends ElfNode {

	// scale fit
	private boolean mScaleFitEnable;
	private EdgeFitType mScaleEdgeFitType = EdgeFitType.BothXY;
	private ElfPointf mScaleWeightPoint = new ElfPointf();
	private ElfPointf mScaleValuePoint = new ElfPointf();

	public FitScaleNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	public boolean getScaleFitEnable() {
		return mScaleFitEnable;
	}

	public void setScaleFitEnable(boolean mScaleFitEnable) {
		this.mScaleFitEnable = mScaleFitEnable;
		effect();
	}
	protected final static int REF_ScaleFitEnable = DEFAULT_SHIFT;
	
	public EdgeFitType getScaleEdgeFitType() {
		return mScaleEdgeFitType;
	}

	public void setScaleEdgeFitType(EdgeFitType mScaleEdgeFitType) {
		this.mScaleEdgeFitType = mScaleEdgeFitType;
		effect();
	}
	protected final static int REF_ScaleEdgeFitType = DEFAULT_SHIFT;

	public ElfPointf getScaleWeightPoint() {
		return new ElfPointf(mScaleWeightPoint);
	}

	public void setScaleWeightPoint(ElfPointf mScaleWeightPoint) {
		this.mScaleWeightPoint.set(mScaleWeightPoint);
		effect();
	}
	protected final static int REF_ScaleWeightPoint = DEFAULT_SHIFT;

	public ElfPointf getScaleValuePoint() {
		return new ElfPointf(mScaleValuePoint);
	}

	public void setScaleValuePoint(ElfPointf mScaleValuePoint) {
		this.mScaleValuePoint.set(mScaleValuePoint);
		effect();
	}
	protected final static int REF_ScaleValuePoint = DEFAULT_SHIFT;
	
	public void calc(float dt) {
		super.calc(dt);
		effect();
	}
	
	protected final void effect() { 
		if (this.getScaleFitEnable()) { 
			final int sx = PowerMan.getSingleton(ProjectSetting.class).getLogicWidth();
			final int sy = PowerMan.getSingleton(ProjectSetting.class).getLogicHeight();

			final float x = EdgeFitType.getValueX(sx, sy, this.mScaleWeightPoint.x, this.mScaleWeightPoint.y, this.mScaleValuePoint.x, this.mScaleValuePoint.y, mScaleEdgeFitType);
			final float y = EdgeFitType.getValueY(sx, sy, this.mScaleWeightPoint.x, this.mScaleWeightPoint.y, this.mScaleValuePoint.x, this.mScaleValuePoint.y, mScaleEdgeFitType);
			
			this.setScale(x, y);
		}
	}

}
