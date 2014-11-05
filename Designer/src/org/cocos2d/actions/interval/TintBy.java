package org.cocos2d.actions.interval;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;

//
// TintBy
//

public class TintBy extends IntervalAction {
    protected int deltaR, deltaG, deltaB;
    protected int fromR, fromG, fromB;

    public static TintBy action(float t, int r, int g, int b) {
        return new TintBy(t, r, g, b);
    }

    protected TintBy(float t, int r, int g, int b) {
        super(t);
        deltaR = r & 0xff;
        deltaG = g & 0xff;
        deltaB = b & 0xff;
    }

    public IntervalAction copy() {
        return new TintBy(duration, deltaR, deltaG, deltaB);
    }

    public void start(ElfNode aTarget) { 
        super.start(aTarget); 

        fromR = (int)(aTarget.getColor().red*255);
        fromG = (int)(aTarget.getColor().green*255);
        fromB = (int)(aTarget.getColor().blue*255);
    }

    public void update(float t) {
        target.setColor(new ElfColor((fromR + deltaR * t) / 255f, (fromG + deltaG * t) / 255f, (fromB + deltaB * t) / 255f, target.getAlpha()));
    }
    
    public IntervalAction reverse() {
        return new TintBy(duration, -deltaR, -deltaG, -deltaB);
    }
}
