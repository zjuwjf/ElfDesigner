package org.cocos2d.actions.interval;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.ElfVertex3;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.node3d.Elf3DNode;

public class Action3DBy extends IntervalAction{
	
	protected ElfVertex3 position = new ElfVertex3();
	protected ElfVertex3 rotate = new ElfVertex3();
	protected ElfVertex3 scale = new ElfVertex3();
	
	protected ElfVertex3 position_delta = new ElfVertex3();
	protected ElfVertex3 rotate_delta = new ElfVertex3();
	protected ElfVertex3 scale_delta = new ElfVertex3();
	
    public static Action3DBy action(float time, ElfVertex3 pos, ElfVertex3 rotate, ElfVertex3 scale) {
        return new Action3DBy(time, pos, rotate, scale);
    }
    
    protected Action3DBy(float time, ElfVertex3 pos, ElfVertex3 rotate, ElfVertex3 scale) {
        super(time);
        position_delta.set(pos);
        rotate_delta.set(rotate);
        scale_delta.set(scale);
    } 

    @Override
    public IntervalAction copy() {
        return new Action3DBy(duration, position_delta, rotate_delta, scale_delta);
    }

    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        if(aTarget instanceof Elf3DNode) {
        	final Elf3DNode node = (Elf3DNode)aTarget;
        	final ElfPointf p = node.getPosition();
        	position.x = p.x;
        	position.y = p.y;
        	position.z = aTarget.getZ();
        	
        	rotate.x = node.getRotateX();
        	rotate.y = node.getRotateY();
        	rotate.z = node.getRotate();
        	
        	final ElfPointf s = node.getScale();
        	scale.x = s.x;
        	scale.y = s.y;
        	scale.z = node.getScaleZ();
       } 
    } 
    
    public void update(float t) { 
    	if(target instanceof Elf3DNode) { 
    		final Elf3DNode node = (Elf3DNode)target;
    		final float x = position.x + t*position_delta.x;
    		final float y = position.y + t*position_delta.y;
    		final float z = position.z + t*position_delta.z;
    		
    		final float rx = rotate.x + t*rotate_delta.x;
    		final float ry = rotate.y + t*rotate_delta.y;
    		final float rz = rotate.z + t*rotate_delta.z;
    		
    		final float sx = scale.x + t*scale_delta.x;
    		final float sy = scale.y + t*scale_delta.y;
    		final float sz = scale.z + t*scale_delta.z;
    		
    		node.setPosition(x, y);
    		node.setZ(z);
    		
    		node.setRotateX(rx);
    		node.setRotateY(ry);
    		node.setRotate(rz);
    		
    		node.setScale(sx, sy);
    		node.setScaleZ(sz);
    	} 
    } 
    
    public IntervalAction reverse() {
        return new Action3DBy(duration, position_delta.reverse(), rotate_delta.reverse(), scale_delta.reverse());
    }
} 
