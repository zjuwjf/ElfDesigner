package org.cocos2d.actions.instant;

import java.lang.reflect.Method;

import elfEngine.basic.debug.Debug;
import elfEngine.basic.node.ElfNode;

//
// CallFunc
//

/**
 * Calls a 'callback'
 */
public class CallFunc extends InstantAction {
    protected Object targetCallback;
    protected String selector;

    protected Method invocation;
    
    public static CallFunc action(Object target, String selector) {
        return new CallFunc(target, selector);
    }

	protected CallFunc() {}

    /**
     * creates an action with a callback
     */
    protected CallFunc(Object t, String s) {
        init(t, s);
        
        try {
            Class<?> cls = targetCallback.getClass();
            invocation = cls.getMethod(selector, new Class[]{});
        } catch (Exception e)
		{
			Debug.e("CallFunc:Exception " + e.toString());
        }
    }

	protected void init(Object t, String s)
	{
		targetCallback = t;
		selector = s;
	}

	@Override
    public CallFunc copy() { 
        return new CallFunc(targetCallback, selector); 
    } 
	
    @Override
    public void start(ElfNode aTarget) { 
        super.start(aTarget);
        execute();
    } 
    
    /**
     * executes the callback
     */
    public void execute() {
        try {
            invocation.invoke(targetCallback);
        } catch (Exception e) {
        }
    }
}
