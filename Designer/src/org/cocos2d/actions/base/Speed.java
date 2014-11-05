package org.cocos2d.actions.base;

import org.cocos2d.actions.interval.IntervalAction;

import elfEngine.basic.node.ElfNode;

public class Speed extends CCAction {
    protected IntervalAction other;
    protected float speed;

    public float getSpeed() {
        return speed;
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public static Speed action(IntervalAction action, float r) {
        return new Speed(action, r);
    } 
    
    protected Speed(IntervalAction action, float r) {
        other = action;
        speed = r;
    }

    public Speed copy() {
        return new Speed(other.copy(), speed);
    }
    

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        other.start(target);
    }

    @Override
    public void stop() {
        other.stop();
        super.stop();
    }

    @Override
    public void step(float dt) {
        other.step(dt * speed);
    } 

    @Override
    public boolean isDone() {
        return other.isDone();
    }

    public Speed reverse() {
        return new Speed(((IntervalAction)other).reverse(), speed);
    } 
} 
