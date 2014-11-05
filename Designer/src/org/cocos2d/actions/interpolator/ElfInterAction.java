package org.cocos2d.actions.interpolator;

import org.cocos2d.actions.ease.EaseAction;
import org.cocos2d.actions.interval.IntervalAction;

import elfEngine.basic.counter.Interpolator;

public class ElfInterAction  extends EaseAction {

    public static ElfInterAction action(IntervalAction action, Interpolator interpolator) {
        return new ElfInterAction(action, interpolator);
    } 

    protected ElfInterAction(IntervalAction action, Interpolator interpolator) { 
        super(action);
        
        mInterpolator = interpolator;
    } 
    
    private Interpolator mInterpolator;

    @Override
    public ElfInterAction copy() { 
        return new ElfInterAction(other.copy(), mInterpolator);
    } 
    
    public void update(float t) { 
    	if(mInterpolator == null) {
    		 other.update(t); 
    	} else {
    		 other.update(mInterpolator.getInterpolation(t)); 
    	}
       
    } 

    public IntervalAction reverse() { 
        return new ElfInterAction(other.reverse(), mInterpolator);
    } 
}