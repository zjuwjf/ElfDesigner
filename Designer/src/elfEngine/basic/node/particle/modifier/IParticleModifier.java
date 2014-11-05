package elfEngine.basic.node.particle.modifier;

import elfEngine.basic.modifier.INodeModifier;

public interface IParticleModifier extends INodeModifier{
	
	/**
	 * re-dress to be a new one; (reset in it)
	 */
	public void reDress();
	public IParticleModifier newInstance();
	
	public void setOriginPosition(float x, float y);
	
}
