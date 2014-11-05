package org.cocos2d.actions.interval;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;


//
// TintTo
//

public class TintTo extends IntervalAction {
    protected int toR, toG, toB;
    protected int fromR, fromG, fromB;

    public static TintTo action(float t, int r, int g, int b) {
        return new TintTo(t, r, g, b);
    }

    protected TintTo(float t, int r, int g, int b) {
        super(t);
        toR = r & 0xff;
        toG = g & 0xff;
        toB = b & 0xff;
    }

    public IntervalAction copy() {
        return new TintBy(duration, toR, toG, toB);
    }

    public void start(ElfNode aTarget) {
        super.start(aTarget);
        
        fromR = (int)(aTarget.getColor().red*255);
        fromG = (int)(aTarget.getColor().green*255);
        fromB = (int)(aTarget.getColor().blue*255);
    }

    public void update(float t) {        
        target.setColor(new ElfColor((fromR + (toR - fromR) * t) / 255f, 
        		 (fromG + (toR - fromR) * t) / 255f, 
        		 (fromB + (toR - fromR) * t) / 255f, target.getAlpha()));
    }
}
