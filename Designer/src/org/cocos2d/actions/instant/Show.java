package org.cocos2d.actions.instant;

import org.cocos2d.actions.base.FiniteTimeAction;

import elfEngine.basic.node.ElfNode;

/**
 * Show the node
 */
public class Show extends InstantAction {

	public Show() {
    }
	
    public static Show action() {
        return new Show();
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        target.setVisible(true);
    }

	@Override
    public Show copy() {
        return new Show();
    }

    @Override
    public FiniteTimeAction reverse() {
        return new Hide();
    }
}
