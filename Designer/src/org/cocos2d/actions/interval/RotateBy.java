package org.cocos2d.actions.interval;

import elfEngine.basic.node.ElfNode;


//
// RotateBy
//

public class RotateBy extends IntervalAction {
    private float angle;
    private float startAngle;

    public static RotateBy action(float t, float a) {
        return new RotateBy(t, a);
    }

    protected RotateBy(float t, float a) {
        super(t);
        angle = a;
    }

    @Override
    public IntervalAction copy() {
        return new RotateBy(duration, angle);
    }


    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        startAngle = target.getRotate();
    }

    @Override
    public void update(float t) {
        // XXX: shall I add % 360
        target.setRotate(startAngle + angle * t);
    }

    @Override
    public IntervalAction reverse() {
        return new RotateBy(duration, -angle);
    }

}
