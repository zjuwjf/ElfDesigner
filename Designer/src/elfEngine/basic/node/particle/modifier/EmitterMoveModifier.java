package elfEngine.basic.node.particle.modifier;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.modifier.PathModifier;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.particle.ParticleNode;
import elfEngine.basic.node.particle.emitter.IParticleEmitter;
import elfEngine.basic.pool.ArrayPool;

public class EmitterMoveModifier extends PathModifier{
	
	public EmitterMoveModifier(float period, float life, LoopMode mode, Interpolator inter, ElfPointf[] ps) {
		super(period, life, mode, inter, ps);
	}

	public void modifier(ElfNode node) {
		final float value = getValue();
		final float [] position = ArrayPool.float2;
		mElfPath.getPosition(value, position);
		//
		final IParticleEmitter emittrt = ((ParticleNode)node).getEmitter();
		emittrt.setCentre(position[0], position[1]);
	}
}
