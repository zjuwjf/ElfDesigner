package org.cocos2d.actions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cocos2d.actions.base.CCAction;

import elfEngine.basic.debug.Debug;
import elfEngine.basic.node.ElfNode;

public class ActionManager {
//    private static final String LOG_TAG = ActionManager.class.getSimpleName();

    static class HashElement {
        CopyOnWriteArrayList<CCAction> actions;
        ElfNode target;
        boolean paused;

        HashElement(ElfNode t, boolean p) {
            target = t;
            paused = p;
        }

        public String toString() {
            String s = "target=" + target + ", paused=" + paused + ", actions=" + actions + "\n";
            for (CCAction a : actions) {
                s += a.toString() + "\n";
            }
            return s;
        }
    }


    /**
     * ActionManager is a singleton that manages all the actions.
     * Normally you won't need to use this singleton directly. 99% of the cases you will use the ElfBasicNode interface,
     * which uses this singleton.
     * But there are some cases where you might need to use this singleton.
     * Examples:
     * - When you want to run an action where the target is different from a ElfBasicNode.
     * - When you want to pause / resume the actions
     *
     * @since v0.8
     */

    private ConcurrentHashMap<ElfNode, HashElement> targets;
//    private HashElement	currentTarget;
//    private boolean currentTargetSalvaged;

    /**
     * returns a shared instance of the ActionManager
     */
//    private static ActionManager _sharedManager = null;
//
//    public static ActionManager sharedManager() {
//        synchronized (ActionManager.class) {
//            if (_sharedManager == null) {
//                _sharedManager = new ActionManager();
//            }
//            return _sharedManager;
//        }
//    }
    
    public ActionManager() {
        synchronized (ActionManager.class) { 
//          Scheduler.sharedScheduler().schedule(new Scheduler.Timer(this, "tick"));
        	
            targets = new ConcurrentHashMap<ElfNode, HashElement>(131); 
        }
    }

    private void deleteHashElement(HashElement element) {
        element.actions.clear();
        targets.remove(element.target);
    }

    private void actionAlloc(HashElement element) {
        if (element.actions == null)
            element.actions = new CopyOnWriteArrayList<CCAction>(); // 4 actions per node by default
//        else
//            element.actions.ensureCapacity(element.actions.size()*2);
    }

    private void removeAction(int index, HashElement element) {

        element.actions.remove(index);

        if (element.actions.size() == 0) {
            deleteHashElement(element);
        }

    }

    // actions

    // TODO figure out why the target not found
    /**
     * Pauses all actions for a certain target.
     * When the actions are paused, they won't be "ticked".
     */
    public void pauseAllActions(ElfNode target) {
        HashElement element = targets.get(target);
        if (element != null) {
            element.paused = true;
        } else {
            //Log.w(LOG_TAG, "pauseAllActions: target not found");
        }
    }

    /**
     * Resumes all actions for a certain target.
     * Once the actions are resumed, they will be "ticked" in every frame.
     */
    public void resumeAllActions(ElfNode target) {
        HashElement element = targets.get(target);
        if (element != null) {
            element.paused = false;
        } else {
//            Log.w(LOG_TAG, "resumeAllActions: target not found");
        }
    }


    /**
     * Adds an action with a target. The action can be added paused or unpaused.
     * The action will be run "against" the target.
     * If the action is added paused, then it will be queued, but it won't be "ticked" until it is resumed.
     * If the action is added unpaused, then it will be queued, and it will be "ticked" in every frame.
     */
    public void addAction(CCAction action, ElfNode target, boolean paused) {
        assert action != null : "Argument action must be non-null";
        assert target != null : "Argument target must be non-null";

        HashElement element = targets.get(target);
        if (element == null) {
            element = new HashElement(target, paused);
            targets.put(target, element);
        }

        actionAlloc(element);

        assert !element.actions.contains(action) : "runAction: Action already running";

        element.actions.add(action);

        action.start(target);
    }

    /**
     * Removes all actions from all the targers.
     */
    public void removeAllActions() {
        for (HashElement element : targets.values()) {
            removeAllActions(element.target);
        }
    }

    /**
     * Removes all actions from a certain target.
     * All the actions that belongs to the target will be removed.
     */
    public void removeAllActions(ElfNode target) {
        // explicit null handling
        if (target == null)
            return;

        HashElement element = targets.get(target);
        if (element != null) {
//            if( element.actions.contains(element.currentAction) && !element.currentActionSalvaged ) {
//                element.currentActionSalvaged = true;
//            }

            element.actions.clear();
//            if( currentTarget == element )
//                currentTargetSalvaged = true;
//            else
            deleteHashElement(element);

        } else {
            Debug.w( "removeAllActions: target not found");
        }
    }

    /**
     * Removes an action given an action reference.
     */
    public void removeAction(CCAction action) {
        HashElement element = targets.get(action.getOriginalTarget());
        if (element != null) {
            int i = element.actions.indexOf(action);
            if (i != -1) {
                removeAction(i, element);
            }
        } else {
        	Debug.w( "removeAction: target not found");
        }
    }

    /**
     * Removes an action given its tag and the target
     */
    public void removeAction(int tag, ElfNode target) {
        assert tag != CCAction.INVALID_TAG : "Invalid tag";

        HashElement element = targets.get(target);
        if (element != null) {
            if (element.actions != null) {
                int limit = element.actions.size();
                for (int i = 0; i < limit; i++) {
                    CCAction a = element.actions.get(i);
                    if (a.getTag() == tag && a.getOriginalTarget() == target)
                        removeAction(i, element);
                }
            } else
            	Debug.w( "removeAction: Action not found");
        } else {
        	Debug.w( "removeAction: target not found");
        }
    }

    /**
     * Gets an action given its tag and a target
     *
     * @return the Action with the given tag
     */
    public CCAction getAction(int tag, ElfNode target) {
        assert tag != CCAction.INVALID_TAG : "Invalid tag";

        HashElement element = targets.get(target);
        if (element != null) {
            if (element.actions != null) {
                int limit = element.actions.size();
                for (int i = 0; i < limit; i++) {
                    CCAction a = element.actions.get(i);
                    if (a.getTag() == tag)
                        return a;
                }
            } else
            	Debug.w( "getAction: Action not found");
        } else {
        	Debug.w( "getAction: target not found");
        }
        return null;
    }

    /**
     * Returns the numbers of actions that are running in a certain target
     * Composable actions are counted as 1 action. Example:
     * If you are running 1 Sequence of 7 actions, it will return 1.
     * If you are running 7 Sequences of 2 actions, it will return 7.
     */
    public int numberOfRunningActions(ElfNode target) {
        HashElement element = targets.get(target);
        if (element != null) {
            return element.actions != null ? element.actions.size() : 0;
        } else
        	Debug.w( "numberOfRunningActions: target not found");
        return 0;
    }

    public void tick(float dt) {
        for (HashElement currentTarget : targets.values()) {
            if (!currentTarget.paused) {

                // The 'actions' may change while inside this loop.
                for (int actionIndex = 0; actionIndex < currentTarget.actions.size(); actionIndex++) {
                    CCAction currentAction = currentTarget.actions.get(actionIndex);

                    currentAction.step(dt);
                    if (currentAction.isDone()) {
                        currentAction.stop();

                        removeAction(currentAction);
                    }
                }
            }

            if (currentTarget.actions.size() == 0)
                deleteHashElement(currentTarget);

        }
    }
}
