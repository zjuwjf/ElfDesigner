package com.ielfgame.stupidGame.undo;

import java.util.LinkedList;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class UndoRedoManager {
	private static final ElfNode sElfScreenNode = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode();

	private static LinkedList<ElfNode> sUndoNodeList = new LinkedList<ElfNode>();
	private static LinkedList<ElfNode> sRedoNodeList = new LinkedList<ElfNode>();
	private static final int sMaxUndoLen = 5;
	private static final int sMaxNodeForUnodRedo = 1200;
	
	private static boolean sEnableUndoRed = true;

	private static void checkInUndoInner(final ElfNode node) {
		if (sEnableUndoRed) {
			if (!sUndoNodeList.isEmpty()) {
				final ElfNode last = sUndoNodeList.getLast();
				if (ElfNode.isSameNodeDeep(node, last)) {
					return;
				}
			}

			sUndoNodeList.add(node);

			while (sUndoNodeList.size() > sMaxUndoLen) {
				sUndoNodeList.removeFirst();
			}

			sRedoNodeList.clear();
		}
		// System.err.println("onCheck:" + sUndoNodeList.size() + ":" +
		// sRedoNodeList.size());
	}

	private static void copyToScreenNode(ElfNode nodeSrc) {
		copyDeep(nodeSrc, sElfScreenNode);
	}

	private static void copyDeep(final ElfNode nodeSrc, ElfNode nodeDst) {
		assert (nodeSrc.getIdentityID() == nodeDst.getIdentityID());

		ElfNode.copyFrom(nodeDst, nodeSrc);
		// System.err.println("::::::"+nodeSrc.getName()+","+nodeDst.getName());
		/***
		 * 
		 */
		final ElfNode[] srcChild = nodeSrc.getChilds();
		final ElfNode[] dstChild = nodeDst.getChilds();

		final LinkedList<ElfNode> dstChildList = new LinkedList<ElfNode>();
		for (final ElfNode n : dstChild) {
			dstChildList.add(n);
		}

		// System.err.println("src:");
		// for(ElfNode n : srcChild) {
		// System.err.println("\t:"+n.getName());
		// }
		//
		// System.err.println("dst:");
		// for(ElfNode n : dstChildList) {
		// System.err.println("\t:"+n.getName());
		// }

		for (int i = 0; i < srcChild.length; i++) {
			final ElfNode aSrcChild = srcChild[i];

			final LinkedList<ElfNode> recover = new LinkedList<ElfNode>(dstChildList);
			final LinkedList<ElfNode> shouldRemoveList = new LinkedList<ElfNode>();

			ElfNode aDstChild = null;
			while (!dstChildList.isEmpty()) {
				aDstChild = dstChildList.removeFirst();
				if (aDstChild.getIdentityID() == aSrcChild.getIdentityID()) {
					break;
				} else {
					// should removed ?
					// aDstChild.removeFromParentView(false);
					shouldRemoveList.add(aDstChild);
				}
				aDstChild = null;
			}

			if (aDstChild == null) {
				// recover it
				assert (dstChildList.isEmpty());
				dstChildList.addAll(recover);
			} else {
				// removes
				for (final ElfNode node : shouldRemoveList) {
					node.removeFromParentView(false);
					// System.err.println("remove "+node.getName());
				}
			}

			// do not find a Identity matched node
			if (aDstChild == null) {
				aDstChild = aSrcChild.copyDeep(nodeDst, true);
				aDstChild.addToParentView(i);

				// System.err.println("add "+aDstChild.getName());
			} else {
				// System.err.println("copy "+aSrcChild.getName()+","+aDstChild.getName());
				copyDeep(aSrcChild, aDstChild);
			}
		}

		while (!dstChildList.isEmpty()) {
			ElfNode aDstChild = dstChildList.removeFirst();
			aDstChild.removeFromParentView(false);
		}
	}

	// ctrl + z
	private static void onUndoInnner() {
		if (!sUndoNodeList.isEmpty()) {
			final ElfNode node = sUndoNodeList.removeLast();

			final ElfNode redoNode = sElfScreenNode.copyDeep(null, true);
			sRedoNodeList.add(redoNode);

			copyToScreenNode(node);
		}
	}

	private static void onRedoInnner() {
		if (!sRedoNodeList.isEmpty()) {
			final ElfNode node = sRedoNodeList.removeLast();

			final ElfNode undoNode = sElfScreenNode.copyDeep(null, true);
			sUndoNodeList.add(undoNode);

			copyToScreenNode(node);
		}
	}

	public static void checkInUndo() {
		final ElfNode bind = PowerMan.getSingleton(DataModel.class).getRootScreen();
		final ElfPointf p = bind.getNodeIndex(bind);
		if(p.x > sMaxNodeForUnodRedo) {
			sEnableUndoRed = false;
		} else {
			sEnableUndoRed = true;
		}
//		sEnableUndoRed = PowerMan.getSingleton(DataModel.class).getRootScreen().getNodeDepth();
		
		final long lasttime = System.currentTimeMillis();
		if (sEnableUndoRed) {
			checkInUndoInner(sElfScreenNode.copyDeep(null, true));
		}
		final long nowtime = System.currentTimeMillis();
		System.err.println("check last:" + (nowtime - lasttime));
	}

	public static void onUndo() {
		if (sEnableUndoRed) {
			onUndoInnner();
		}
	}

	public static void onRedo() {
		if (sEnableUndoRed) {
			onRedoInnner();
		}
	}

	public static void clear() {
		sUndoNodeList.clear();
		sRedoNodeList.clear();
	}
}
