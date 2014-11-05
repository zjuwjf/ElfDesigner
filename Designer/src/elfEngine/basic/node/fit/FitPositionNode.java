package elfEngine.basic.node.fit;

import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class FitPositionNode extends ElfNode {
	
	//position fit
	private boolean mPositionFitEnable;
	private EdgeFitType mPositionEdgeFitType = EdgeFitType.BothXY;
	private ElfPointf mPositionWeightPoint = new ElfPointf();
	private ElfPointf mPositionValuePoint = new ElfPointf();

	public FitPositionNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}

	public boolean getPositionFitEnable() { 
		return mPositionFitEnable;
	} 
	
	public void setPositionFitEnable(boolean mPositionFitEnable) {
		this.mPositionFitEnable = mPositionFitEnable;
	}
	protected final static int REF_PositionFitEnable = DEFAULT_SHIFT;

	public EdgeFitType getPositionEdgeFitType() {
		return mPositionEdgeFitType;
	}

	public void setPositionEdgeFitType(EdgeFitType mPositionEdgeFitType) {
		this.mPositionEdgeFitType = mPositionEdgeFitType;
	}
	protected final static int REF_PositionEdgeFitType = DEFAULT_SHIFT;

	public ElfPointf getPositionWeightPoint() {
		return new ElfPointf(mPositionWeightPoint);
	}

	public void setPositionWeightPoint(final ElfPointf mPositionWeightPoint) {
		this.mPositionWeightPoint.set(mPositionWeightPoint);
	}
	protected final static int REF_PositionWeightPoint = DEFAULT_SHIFT;

	public ElfPointf getPositionValuePoint() {
		return new ElfPointf(mPositionValuePoint);
	}

	public void setPositionValuePoint(final ElfPointf mPositionValuePoint) {
		this.mPositionValuePoint.set(mPositionValuePoint);
	}
	protected final static int REF_PositionValuePoint = DEFAULT_SHIFT;
	
	public void calc(float dt) {
		super.calc(dt);
		effect();
	}
	
	protected final void effect() { 
		if (this.getPositionFitEnable()) { 
			final int sx = PowerMan.getSingleton(ProjectSetting.class).getPhysicWidth();
			final int sy = PowerMan.getSingleton(ProjectSetting.class).getPhysicHeight();
			
			final float x = EdgeFitType.getValueX(sx, sy, this.mPositionWeightPoint.x, this.mPositionWeightPoint.y, this.mPositionValuePoint.x, this.mPositionValuePoint.y, mPositionEdgeFitType);
			final float y = EdgeFitType.getValueY(sx, sy, this.mPositionWeightPoint.x, this.mPositionWeightPoint.y, this.mPositionValuePoint.x, this.mPositionValuePoint.y, mPositionEdgeFitType);
			
			final ElfNode top = this.getTopNode();
			
			if(top != null) { 
				ElfPointf pos = top.getPosition();
				ElfPointf scale = top.getScale();
				
				final ElfPointf ret = PowerMan.getSingleton(ProjectSetting.class).ScreenScale;
				top.setScale(ret);
				top.setPosition(sx / 2, sy / 2);
				
				this.setPositionInScreen(new ElfPointf(x, y));
				
				top.setPosition(pos);
				top.setScale(scale);
			} else { 
				this.setPositionInScreen(new ElfPointf(x, y));
			} 
		}
	}
}
