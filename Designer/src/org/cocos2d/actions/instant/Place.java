package org.cocos2d.actions.instant;

import elfEngine.basic.node.ElfNode;
import elfEngine.graphics.PointF;

/**
 * Places the node in a certain position
 */
public class Place extends InstantAction {
    private PointF position;

    public static Place action(float x, float y) {
        return new Place(x, y);
    }

    /**
     * creates a Place action with a position
     */
    protected Place(float x, float y) {
        position = new PointF(x, y);
    }

    @Override
    public Place copy() {
        return new Place(position.x, position.y);
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        target.setPosition(position.x, position.y);
    }
}
