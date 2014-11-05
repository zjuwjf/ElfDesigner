package org.cocos2d.actions.instant;

import org.cocos2d.actions.base.FiniteTimeAction;

public abstract class InstantAction extends FiniteTimeAction {

    protected InstantAction() {
        super(0);
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void step(float dt) {
        update(1f);
    }

    public void update(float t) {
        // ignore
    }
}

