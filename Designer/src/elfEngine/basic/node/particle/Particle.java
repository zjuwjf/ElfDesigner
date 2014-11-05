package elfEngine.basic.node.particle;

import elfEngine.basic.modifier.INodeModifier.ModifierListener;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.particle.modifier.BasicParticleModifier;
import elfEngine.basic.node.particle.modifier.ParticlePathModeAModifier;
import elfEngine.basic.node.particle.modifier.ParticlePathModeBModifier;

public class Particle extends ElfNode{
	//life
	public final BasicParticleModifier mBasicParticleModifier = new BasicParticleModifier(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	//position
	public final ParticlePathModeAModifier mParticlePathModeAModifier = new ParticlePathModeAModifier(0,0,0,0,0,0);
	public final ParticlePathModeBModifier mParticlePathModeBModifier = new ParticlePathModeBModifier(0,0,0);
	
	public Particle(final ElfNode father, final int ordinal) {
		super(father, -1);
		mBasicParticleModifier.setModifierListener(new ModifierListener(){
			public void onFinished(ElfNode node) { 
				node.removeFromParent(); 
			} 
		}); 
	} 
	
	public void setParticleSys(final ParticleNode sys) {
		this.addDeadListener(new IDeadListener(){
			public void onDead(ElfNode node) { 
				node.recycle(); 
				sys.mExistedParticles--; 
				if(sys.mExistedParticles<=0&&sys.mOnNoExistedParticle!=null){
					sys.mOnNoExistedParticle.onNoExistedParticle(sys);
				} 
			} 
		}); 
		
		this.setUseModifier(true);
		this.setPool(sys.mPool);
		sys.mPool.recycle(this);
	}
	
} 
