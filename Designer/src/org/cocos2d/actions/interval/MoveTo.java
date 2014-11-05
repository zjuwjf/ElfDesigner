package org.cocos2d.actions.interval;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.particle.ParticleNode;

//
// MoveTo
//

public class MoveTo extends IntervalAction {
	private float endPositionX;
	private float endPositionY;
	private float startPositionX;
	private float startPositionY;
	protected float deltaX;
	protected float deltaY;

	public static MoveTo action(float t, float x, float y) {
		return new MoveTo(t, x, y);
	}

	protected MoveTo(float t, float x, float y) {
		super(t);
		endPositionX = x;
		endPositionY = y;
	}

	@Override
	public IntervalAction copy() {
		return new MoveTo(duration, endPositionX, endPositionY);
	}

	@Override
	public void start(ElfNode aTarget) {
		super.start(aTarget);

		if (aTarget instanceof ParticleNode) {
			startPositionX = ((ParticleNode) target).getEmitter().getCentreX();
			startPositionY = ((ParticleNode) target).getEmitter().getCentreY();
		} else {
			startPositionX = target.getPosition().x;
			startPositionY = target.getPosition().y;
		}

		deltaX = endPositionX - startPositionX;
		deltaY = endPositionY - startPositionY;
	}

	@Override
	public void update(float t) {
		if (target instanceof ParticleNode) {
			((ParticleNode) target).getEmitter().setCentre(startPositionX + deltaX * t, startPositionY + deltaY * t);
		} else {
			target.setPosition(startPositionX + deltaX * t, startPositionY + deltaY * t);
		} 
	} 
} 
