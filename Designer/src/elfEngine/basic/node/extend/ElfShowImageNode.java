package elfEngine.basic.node.extend;

import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.modifier.LifeModifier;
import elfEngine.basic.modifier.ScaleModifier;
import elfEngine.basic.modifier.SequenceModifier;
import elfEngine.basic.modifier.TransXYModifier;
import elfEngine.basic.node.ElfNode;

public class ElfShowImageNode extends ElfNode{
//	private float mWidth, mHeight;
	public ElfShowImageNode(ElfNode father, int ordinal, final String key) {
		super(father, ordinal);
		setResid(key);
		
//		final DashRectNode dashRectNode = new DashRectNode(this, null);
//		mWidth = GLManage.getTextureRegion(key).getWidth();
//		mHeight = GLManage.getTextureRegion(key).getHeight();
//		dashRectNode.addToFather();
//		dashRectNode.setSize(mWidth+2, mHeight+2);
		
		setUseModifier(true);
		final float life = 500;
		final float begScale = 0.5f;
		setScale(begScale, begScale);
//		addModifier(new AlphaModifier(0, 1, life*2, life*2, null, null));
		final float trans = PowerMan.getSingleton(ProjectSetting.class).getPhysicWidth();
		translate(trans, 0);
		addModifier(new SequenceModifier(
				new TransXYModifier(-trans, 0, life, life, LoopMode.STAY, InterType.Viscous),
				new ScaleModifier(begScale, 1, begScale, 1, life, life, null, InterType.Overshoot)));
	}
	
//	protected void drawSelf(){
//		super.drawSelf();
//	}
	
	public void showIn(){
		addToParent();
	}
	
	public void showOut(){
		final float life = 500;
//		addModifier(new AlphaModifier(1, 0, life*2, life*2, null, null));
		final float trans = PowerMan.getSingleton(ProjectSetting.class).getPhysicWidth();
		final float begScale = 0.5f;
		addModifier(new SequenceModifier(
				new ScaleModifier(1, begScale, 1, begScale, life, life, null, InterType.Overshoot),
				new TransXYModifier(-trans, 0, life, life, LoopMode.STAY, InterType.Viscous)
				));
		addModifier(new LifeModifier(life*2));
	}
}
