package org.cocos2d.actions.interval;

import elfEngine.basic.node.ElfNode;


//
// RotateTo
//

public class RotateTo extends IntervalAction {
    private float dstAngle;
    private float startAngle;
	private float diffAngle;


    public static RotateTo action(float t, float a) {
        return new RotateTo(t, a);
    }

    protected RotateTo(float t, float a) {
        super(t);
        dstAngle = a;
    }

    @Override
    public IntervalAction copy() {
        return new RotateTo(duration, dstAngle);
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);

		startAngle = target.getRotate();
		if (startAngle > 0)
			startAngle = startAngle % 360.0f;
		else
			startAngle = startAngle % -360.0f;

		diffAngle = dstAngle - startAngle;
		if (diffAngle > 180)
			diffAngle -= 360;
		if (diffAngle < -180)
			diffAngle += 360;
    }

    @Override
    public void update(float t) {
        target.setRotate(startAngle + diffAngle * t);
    }
}
