package elfEngine.basic.node.particle.emitter;

import elfEngine.basic.elfEngine.basic.object.IElfCentre;


public interface IParticleEmitter extends IElfCentre{
	public void getPosition(float [] position);
	public IParticleEmitter copy();
}
