package org.cocos2d.actions.instant;

import elfEngine.basic.debug.Debug;
import elfEngine.basic.node.ElfNode;


/**
 * Calls a 'callback' with the node as the first argument
 * N means Node
 */
public class CallFuncN extends CallFunc {

    public static CallFuncN action(Object t, String s) {
        return new CallFuncN(t, s);
    }

	protected CallFuncN() {}
    /**
     * creates the action with the callback
     */
    protected CallFuncN(Object t, String s) {
        init(t, s);

        try {
            Class<?> cls = targetCallback.getClass();
            Class<?> partypes[] = new Class[1];
            partypes[0] = ElfNode.class;
            invocation = cls.getMethod(selector, partypes);
        } catch (NoSuchMethodException e) {
			Debug.e("CallFuncN:Exception " + e.toString());
        }
    }

	@Override
    public CallFuncN copy() {
        return new CallFuncN(targetCallback, selector);
    }

    /**
     * executes the callback
     */
    public void execute() {
        try {
            invocation.invoke(targetCallback, target);
        } catch (Exception e) {
        }
    }
}
