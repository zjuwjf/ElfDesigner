package org.cocos2d.actions.interval;

import elfEngine.basic.node.ElfNode;

//
// ScaleTo
//

public class ScaleTo extends IntervalAction {
    protected float scaleX;
    protected float scaleY;
    protected float startScaleX;
    protected float startScaleY;
    protected float endScaleX;
    protected float endScaleY;
    protected float deltaX;
    protected float deltaY;

    public static ScaleTo action(float t, float s) {
        return new ScaleTo(t, s, s);
    }

    public static ScaleTo action(float t, float sx, float sy) {
        return new ScaleTo(t, sx, sy);
    }

//    protected ScaleTo(float t, float s) {
//        this(t, s, s);
//    }

    protected ScaleTo(float t, float sx, float sy) {
        super(t);
        endScaleX = sx;
        endScaleY = sy;
    }

    public IntervalAction copy() {
        return new ScaleTo(duration, endScaleX, endScaleY);
    }

    public void start(ElfNode aTarget) {
        super.start(aTarget);
        startScaleX = target.getScale().x;
        startScaleY = target.getScale().y;
        deltaX = endScaleX - startScaleX;
        deltaY = endScaleY - startScaleY;
    }

    public void update(float t) {
        target.setScale(startScaleX + deltaX * t, startScaleY + deltaY * t);
    }
} 
