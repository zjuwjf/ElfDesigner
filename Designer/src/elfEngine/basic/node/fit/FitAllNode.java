package elfEngine.basic.node.fit;

import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class FitAllNode extends ElfNode{
	
	/***
	 * Location
	 * LT, LB, RT, RB
	 * LM, RM, TM, BM
	 * C
	 * 
	 * Point weight
	 * Point pixel
	 * 
	 * Scale -> to fit ?
//	 * Fill, ByHeight, ByWidth
	 * 
	 * Size->List, Map
	 * 
	 */
	
	/***
	 * effect on children
	 */
	
	//position fit
	private boolean mPositionFitEnable;
	private EdgeFitType mPositionEdgeFitType = EdgeFitType.BothXY;
	private ElfPointf mPositionWeightPoint = new ElfPointf();
	private ElfPointf mPositionValuePoint = new ElfPointf();
	
	//scale fit
	private boolean mScaleFitEnable;
	private EdgeFitType mScaleEdgeFitType = EdgeFitType.BothXY;
	private ElfPointf mScaleWeightPoint = new ElfPointf();
	private ElfPointf mScaleValuePoint = new ElfPointf();
	
	//size fit
	private boolean mSizeFitEnable;
	private EdgeFitType mSizeEdgeFitType = EdgeFitType.BothXY;
	private ElfPointf mSizeWeightPoint = new ElfPointf();
	private ElfPointf mSizeValuePoint = new ElfPointf();
	private boolean mEffectChildrenSize;
	
	public FitAllNode(ElfNode father, int ordinal) { 
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
	
	public boolean getScaleFitEnable() {
		return mScaleFitEnable;
	}

	public void setScaleFitEnable(boolean mScaleFitEnable) {
		this.mScaleFitEnable = mScaleFitEnable;
	}
	protected final static int REF_ScaleFitEnable = DEFAULT_SHIFT;
	
	public EdgeFitType getScaleEdgeFitType() {
		return mScaleEdgeFitType;
	}

	public void setScaleEdgeFitType(EdgeFitType mScaleEdgeFitType) {
		this.mScaleEdgeFitType = mScaleEdgeFitType;
	}
	protected final static int REF_ScaleEdgeFitType = DEFAULT_SHIFT;

	public ElfPointf getScaleWeightPoint() {
		return new ElfPointf(mScaleWeightPoint);
	}

	public void setScaleWeightPoint(ElfPointf mScaleWeightPoint) {
		this.mScaleWeightPoint.set(mScaleWeightPoint);
	}
	protected final static int REF_ScaleWeightPoint = DEFAULT_SHIFT;

	public ElfPointf getScaleValuePoint() {
		return new ElfPointf(mScaleValuePoint);
	}

	public void setScaleValuePoint(ElfPointf mScaleValuePoint) {
		this.mScaleValuePoint.set(mScaleValuePoint);
	}
	protected final static int REF_ScaleValuePoint = DEFAULT_SHIFT;

	public boolean getSizeFitEnable() {
		return mSizeFitEnable;
	}

	public void setSizeFitEnable(boolean mSizeFitEnable) {
		this.mSizeFitEnable = mSizeFitEnable;
	}
	protected final static int REF_SizeFitEnable = DEFAULT_SHIFT;

	public EdgeFitType getSizeEdgeFitType() {
		return mSizeEdgeFitType;
	}

	public void setSizeEdgeFitType(EdgeFitType mSizeEdgeFitType) {
		this.mSizeEdgeFitType = mSizeEdgeFitType;
	}
	protected final static int REF_SizeEdgeFitType = DEFAULT_SHIFT;
	
	public ElfPointf getSizeWeightPoint() {
		return new ElfPointf(mSizeWeightPoint);
	}

	public void setSizeWeightPoint(ElfPointf mSizeWeightPoint) {
		this.mSizeWeightPoint.set(mSizeWeightPoint);
	}
	protected final static int REF_SizeWeightPoint = DEFAULT_SHIFT;

	public ElfPointf getSizeValuePoint() {
		return new ElfPointf(mSizeValuePoint);
	}

	public void setSizeValuePoint(ElfPointf mSizeValuePoint) {
		this.mSizeValuePoint.set(mSizeValuePoint);
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
		//do self ?
		if (this.getPositionFitEnable()) { 
			final int sx = PowerMan.getSingleton(ProjectSetting.class).getLogicWidth();
			final int sy = PowerMan.getSingleton(ProjectSetting.class).getLogicHeight();

			final float x = EdgeFitType.getValueX(sx, sy, this.mPositionWeightPoint.x, this.mPositionWeightPoint.y, this.mPositionValuePoint.x, this.mPositionValuePoint.y, mPositionEdgeFitType);
			final float y = EdgeFitType.getValueY(sx, sy, this.mPositionWeightPoint.x, this.mPositionWeightPoint.y, this.mPositionValuePoint.x, this.mPositionValuePoint.y, mPositionEdgeFitType);
			
			final ElfNode top = this.getTopNode();
			
			if(top != null) { 
				ElfPointf pos = top.getPosition();
				ElfPointf scale = top.getScale();
				
				final ElfPointf size = top.getSize();
				top.setPosition(size.x/2, size.y/2);
				top.setScale(1,1);
				
				this.setPositionInScreen(new ElfPointf(x, y));
				
				top.setPosition(pos);
				top.setScale(scale);
			} else { 
				this.setPositionInScreen(new ElfPointf(x, y));
			} 
		}
		
		if (this.getScaleFitEnable()) { 
			final int sx = PowerMan.getSingleton(ProjectSetting.class).getLogicWidth();
			final int sy = PowerMan.getSingleton(ProjectSetting.class).getLogicHeight();

			final float x = EdgeFitType.getValueX(sx, sy, this.mScaleWeightPoint.x, this.mScaleWeightPoint.y, this.mScaleValuePoint.x, this.mScaleValuePoint.y, mScaleEdgeFitType);
			final float y = EdgeFitType.getValueY(sx, sy, this.mScaleWeightPoint.x, this.mScaleWeightPoint.y, this.mScaleValuePoint.x, this.mScaleValuePoint.y, mScaleEdgeFitType);
			
			this.setScale(x, y);
		}
		
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
