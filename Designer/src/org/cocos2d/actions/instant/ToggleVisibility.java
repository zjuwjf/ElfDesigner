package org.cocos2d.actions.instant;

import elfEngine.basic.node.ElfNode;

/**
 * Toggles the visibility of a node
 */
public class ToggleVisibility extends InstantAction {

	protected ToggleVisibility() {
	}
	
    public static ToggleVisibility action() {
        return new ToggleVisibility();
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        target.setVisible(!target.getVisible());
    }

	@Override
    public ToggleVisibility copy() {
        return new ToggleVisibility();
    }
}
