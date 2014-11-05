package org.cocos2d.actions.interval;

import elfEngine.basic.node.ElfNode;

//
// MoveBy
//

public class MoveBy extends MoveTo {

    public static MoveBy action(float t, float x, float y) {
        return new MoveBy(t, x, y);
    }

    protected MoveBy(float t, float x, float y) {
        super(t, x, y);
        deltaX = x;
        deltaY = y;
    }

    @Override
    public IntervalAction copy() {
        return new MoveBy(duration, deltaX, deltaY);
    }

    @Override
    public void start(ElfNode aTarget) {
        float savedX = deltaX;
        float savedY = deltaY;
        super.start(aTarget);
        deltaX = savedX;
        deltaY = savedY;
    }

    @Override
    public IntervalAction reverse() {
        return new MoveBy(duration, -deltaX, -deltaY);
    }
}
