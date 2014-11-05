package org.cocos2d.actions.instant;

import org.cocos2d.actions.base.FiniteTimeAction;

import elfEngine.basic.node.ElfNode;

/**
 * Hide the node
 */
public class Hide extends InstantAction {

    public static Hide action() {
        return new Hide();
    }
    
    protected Hide() { 
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        target.setVisible(false);
    }

	@Override
    public Hide copy() {
        return new Hide();
    }

    @Override
    public FiniteTimeAction reverse() {
        return new Show();
    }
}
