package org.cocos2d.actions.interval;

import elfEngine.basic.node.ElfNode;

//
// JumpTo

public class JumpTo extends JumpBy {

    public static JumpTo action(float time, float x, float y, float height, int jumps) {
        return new JumpTo(time, x, y, height, jumps);
    }
    
    protected JumpTo(float time, float x, float y, float height, int jumps) {
        super(time, x, y, height, jumps);
        mX = x;
        mY = y;
    }
    private final float mX,mY;
    
    @Override
    public IntervalAction copy() {
        return new JumpTo(duration, mX, mY, height, jumps);
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        delta.x = mX - startPosition.x;
        delta.y = mY - startPosition.y;
    }
}
