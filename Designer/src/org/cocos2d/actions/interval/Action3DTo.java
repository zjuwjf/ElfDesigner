package org.cocos2d.actions.interval;

import com.ielfgame.stupidGame.data.ElfVertex3;

import elfEngine.basic.node.ElfNode;

public class Action3DTo extends Action3DBy{
	
	private ElfVertex3 position_dst = new ElfVertex3();
	private ElfVertex3 rotate_dst = new ElfVertex3();
	private ElfVertex3 scale_dst = new ElfVertex3();
	
	public static Action3DTo action(float time, ElfVertex3 pos, ElfVertex3 rotate, ElfVertex3 scale) {
        return new Action3DTo(time, pos, rotate, scale);
    }

    protected Action3DTo(float time, ElfVertex3 pos, ElfVertex3 rotate, ElfVertex3 scale) {
        super(time, pos, rotate, scale);
        
        position_dst.set(pos);
        rotate_dst.set(rotate);
        scale_dst.set(scale);
    }

    @Override
    public IntervalAction copy() {
        return new Action3DTo(duration, position_dst, rotate_dst, scale_dst);
    } 
    
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        
        position_delta.x = position_dst.x-position.x;
        position_delta.y = position_dst.y-position.y;
        position_delta.z = position_dst.z- position.z;
        
        rotate_delta.x = rotate_dst.x-rotate.x;
        rotate_delta.y = rotate_dst.y-rotate.y;
        rotate_delta.z = rotate_dst.z-rotate.z;
        
        scale_delta.x = scale_dst.x-scale.x;
        scale_delta.y = scale_dst.y-scale.y;
        scale_delta.z = scale_dst.z-scale.z;
    } 
}
