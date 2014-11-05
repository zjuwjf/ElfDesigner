package org.cocos2d.actions.camera;

import org.cocos2d.actions.interval.IntervalAction;
import org.cocos2d.actions.interval.ReverseTime;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.node3d.Elf3DNode;
import elfEngine.basic.node.node3d.ElfCamera;

public abstract class CameraAction extends IntervalAction {
	protected float centerXOrig;
	protected float centerYOrig;
	protected float centerZOrig;

	protected float eyeXOrig;
	protected float eyeYOrig;
	protected float eyeZOrig;

	protected float upXOrig;
	protected float upYOrig;
	protected float upZOrig;

	protected CameraAction(float t) {
		super(t);
	}

	@Override
	public void start(ElfNode aTarget) {
		super.start(aTarget);

		if (aTarget instanceof Elf3DNode) {
			final Elf3DNode node = (Elf3DNode) aTarget;
			
			final ElfCamera camera = node.getCamera();

			eyeXOrig = camera.eye.x;
			eyeYOrig = camera.eye.y;
			eyeZOrig = camera.eye.z;

			centerXOrig = camera.center.x;
			centerYOrig = camera.center.y;
			centerZOrig = camera.center.z;

			upXOrig = camera.up.x;
			upYOrig = camera.up.y;
			upZOrig = camera.up.z;
		}
	}

	@Override
	public IntervalAction reverse() {
		return ReverseTime.action(this);
	}

}
