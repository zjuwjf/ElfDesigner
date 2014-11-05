package org.cocos2d.actions.interval;

import org.cocos2d.types.CCPoint;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;

//
// BezierBy
// 
public class BezierBy extends IntervalAction{
	
    private final ElfPointf startPos = new ElfPointf();
    private final ElfPointf endPos = new ElfPointf();
    private final ElfPointf control1 = new ElfPointf();
    private final ElfPointf control2 = new ElfPointf();
    
    private CCPoint startPosition;
    
    public static BezierBy action(float t, ElfPointf startPos,ElfPointf endPos,ElfPointf control1,ElfPointf control2) { 
        return new BezierBy(t, startPos,endPos,control1,control2);
    } 
    
    protected BezierBy(float t, ElfPointf startPos,ElfPointf endPos,ElfPointf control1,ElfPointf control2) { 
        super(t);
        this.startPos.set(startPos);
        this.endPos.set(endPos);
        this.control1.set(control1);
        this.control2.set(control2);
        startPosition = CCPoint.make(0, 0);
    } 
    
    public IntervalAction copy() { 
        return new BezierBy(duration,startPos,endPos,control1,control2);
    } 
    
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        startPosition.x = target.getPosition().x;
        startPosition.y = target.getPosition().y;
    }
    
    public void update(float t) {
        float xa = startPos.x; 
        float xb = control1.x; 
        float xc = control2.x; 
        float xd = endPos.x; 

        float ya = startPosition.y; 
        float yb = control1.y; 
        float yc = control2.y; 
        float yd = endPos.y; 
        
        float x = bezierat(xa, xb, xc, xd, t); 
        float y = bezierat(ya, yb, yc, yd, t); 
        target.setPosition(startPosition.x + x, startPosition.y + y); 
    } 
    
    // Bezier cubic formulae : 
    //	((1 - t) + t)3 = 1 expands to (1 - t)3 + 3t(1-t)2 + 3t2(1 - t) + t3 = 1 
    private static float bezierat(float a, float b, float c, float d, float t) { 
        return (float) (Math.pow(1 - t, 3) * a + 
                3 * t * (Math.pow(1 - t, 2)) * b + 
                3 * Math.pow(t, 2) * (1 - t) * c + 
                Math.pow(t, 3) * d); 
    } 
    
    public IntervalAction reverse() { 
        return new BezierBy(duration, startPos.ccpNeg(),endPos.ccpNeg(),control1.ccpNeg(),control2.ccpNeg());
    } 
}
