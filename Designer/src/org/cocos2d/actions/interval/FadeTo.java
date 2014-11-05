package org.cocos2d.actions.interval;

import elfEngine.basic.node.ElfNode;


//
// FadeTo
//

public class FadeTo extends IntervalAction {
    float toOpacity, fromOpacity;

    public static FadeTo action(float t, float a) {
        return new FadeTo(t, a);
    }

    protected FadeTo(float t, float a) {
        super(t);
        toOpacity = a;
    }

    @Override
    public IntervalAction copy() {
        return new FadeTo(duration, toOpacity);
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        fromOpacity = (target).getAlpha();
    }

    @Override
    public void update(float t) {
        (target).setAlpha((fromOpacity + (toOpacity - fromOpacity) * t));
    }
}
