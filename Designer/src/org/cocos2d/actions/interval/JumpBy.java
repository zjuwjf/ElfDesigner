package org.cocos2d.actions.interval;

import elfEngine.basic.node.ElfNode;
import elfEngine.graphics.PointF;

//
// JumpBy
//

public class JumpBy extends IntervalAction {
    protected PointF startPosition;
    protected PointF delta;
    protected float height;
    protected int jumps;

    public static JumpBy action(float time, float x, float y, float height, int jumps) {
        return new JumpBy(time, x, y, height, jumps);
    }

    protected JumpBy(float time, float x, float y, float h, int j) {
        super(time);
        startPosition = new PointF();
        delta = new PointF(x, y);
        height = h;
        jumps = j;
    }

    @Override
    public IntervalAction copy() {
        return new JumpBy(duration, delta.x, delta.y, height, jumps);
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        startPosition.x = target.getPosition().x;
        startPosition.y = target.getPosition().y;
    } 

    @Override
    public void update(float t) {
        float y = height * (float) Math.abs(Math.sin(t * (float) Math.PI * jumps));
        y += delta.y * t;
        float x = delta.x * t;
        target.setPosition(startPosition.x + x, startPosition.y + y);
    }

    @Override
    public IntervalAction reverse() {
        return new JumpBy(duration, -delta.x, -delta.y, height, jumps);
    }
}
