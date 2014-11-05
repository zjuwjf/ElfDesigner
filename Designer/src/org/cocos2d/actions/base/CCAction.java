package org.cocos2d.actions.base;

import org.cocos2d.types.Copyable;

import elfEngine.basic.debug.Debug;
import elfEngine.basic.node.ElfNode;

public abstract class CCAction implements Copyable {
    public static final int INVALID_TAG = -1; 

    private ElfNode originalTarget;
    public ElfNode target;
    private int tag;
    
    private boolean mHasStarted = false;

    public ElfNode getOriginalTarget() {
        return originalTarget;
    }
    
    public void setOriginalTarget(ElfNode value) { 
        originalTarget = value;
    } 

    public ElfNode getTarget() {
        return target;
    }

    public void setTarget(ElfNode value) {
        target = value;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int value) {
        tag = value;

    }

    public static CCAction action() {
        return null;
    }

    protected CCAction() {
        target = originalTarget = null;
        tag = INVALID_TAG;
    }

    public CCAction copy() {
//        Action copy = new Action();
//        copy.tag = tag;
        return null;
    } 

    public void start(ElfNode aTarget) {
        originalTarget = target = aTarget;
        mHasStarted = true;
    } 
    
    public void stop() {
        target = null;
        if(mRun != null && mHasStarted) { 
        	mRun.run(); 
        } 
        mHasStarted = false;
    }
    
    public boolean isDone() {
        return true;
    } 
    
    public void step(float dt) { 
    	Debug.w( "Override me");
    }
    
    public void update(float time) {
    	Debug.w( "Override me");
    }
    
    public interface CocosActionTag {
        public final int kActionTagInvalid = -1;
    }

    private Runnable mRun;
    public void setListener(Runnable run) {
    	mRun = run;
    }
}