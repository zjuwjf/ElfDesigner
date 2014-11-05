package org.cocos2d.actions.base;

import elfEngine.basic.debug.Debug;

public abstract class FiniteTimeAction extends CCAction {
//    private static final String LOG_TAG = FiniteTimeAction.class.getSimpleName();

    protected float duration;

    public static FiniteTimeAction action(float d) {
        return null;
    }

    protected FiniteTimeAction(float d) {
        duration = d;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    @Override
    public FiniteTimeAction copy() {
        return null;
    }

    public FiniteTimeAction reverse() {
        Debug.w( "Override me");
        return null;
    }

}
