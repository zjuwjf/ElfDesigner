package org.cocos2d.actions.interval;

import java.util.ArrayList;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.FiniteTimeAction;

import elfEngine.basic.node.ElfNode;

//
// Sequence
//

public class Sequence extends IntervalAction { 
	
    private ArrayList<FiniteTimeAction> actions;
    private float split;
    private int last;
    
    public static IntervalAction actions(FiniteTimeAction action1, FiniteTimeAction... actions) {
        FiniteTimeAction prev = action1;
        for (FiniteTimeAction now : actions) { 
        	if(now != null) { 
        		prev = new Sequence(prev, now);
        	} else { 
        		break;
        	} 
        } 
        return (IntervalAction) prev;
    } 
    
    public static IntervalAction actions(FiniteTimeAction action1, FiniteTimeAction action2) {
        return new Sequence(action1, action2);
    } 
    
    public static IntervalAction actions(FiniteTimeAction action1, FiniteTimeAction action2, FiniteTimeAction action3) {
        return new Sequence(new Sequence(action1, action2), action3);
    } 
    
    public static IntervalAction actions(FiniteTimeAction action1, FiniteTimeAction action2, FiniteTimeAction action3, FiniteTimeAction action4) {
        return new Sequence(new Sequence(new Sequence(action1, action2), action3), action4);
    } 
    
    public static IntervalAction actions(FiniteTimeAction action1, FiniteTimeAction action2, FiniteTimeAction action3, FiniteTimeAction action4, FiniteTimeAction action5) {
        return new Sequence(new Sequence(new Sequence(new Sequence(action1, action2), action3), action4), action5);
    } 
    
    public static IntervalAction actions(FiniteTimeAction action1, FiniteTimeAction action2, FiniteTimeAction action3, FiniteTimeAction action4, FiniteTimeAction action5, FiniteTimeAction action6) {
        return new Sequence(new Sequence(new Sequence(new Sequence(new Sequence(action1, action2), action3), action4), action5), action6);
    } 
    
    public static IntervalAction actions(FiniteTimeAction action1, FiniteTimeAction action2, FiniteTimeAction action3, FiniteTimeAction action4, FiniteTimeAction action5, FiniteTimeAction action6, FiniteTimeAction action7) {
        return new Sequence(new Sequence(new Sequence(new Sequence(new Sequence(new Sequence(action1, action2), action3), action4), action5), action6), action7);
    } 

    protected Sequence(FiniteTimeAction one, FiniteTimeAction two) {
        super(one.getDuration() + two.getDuration());
        
        actions = new ArrayList<FiniteTimeAction>(2);
        actions.add(one);
        actions.add(two);
    }

    @Override
    public IntervalAction copy() {
        return new Sequence(actions.get(0).copy(), actions.get(1).copy());
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        split = actions.get(0).getDuration() / duration;
        last = -1;
    }

    public void stop() {
        for (CCAction action : actions)
            action.stop(); 
        super.stop(); 
    } 


    @Override
    public void update(float t) {
        int found;
        float new_t;

        if (t >= split) {
            found = 1;
            if (split == 1)
                new_t = 1;
            else
                new_t = (t - split) / (1 - split);
        } else {
            found = 0;
            if (split != 0)
                new_t = t / split;
            else
                new_t = 1;
        }

        if (last == -1 && found == 1) {
            actions.get(0).start(target);
            actions.get(0).update(1.0f);
            actions.get(0).stop();
        }

        if (last != found) {
            if (last != -1) {
                actions.get(last).update(1.0f);
                actions.get(last).stop();
            }
            actions.get(found).start(target);
        }
        actions.get(found).update(new_t);
        last = found;
    }

    @Override
    public IntervalAction reverse() {
        return new Sequence(actions.get(1).reverse(), actions.get(0).reverse());
    }
}
