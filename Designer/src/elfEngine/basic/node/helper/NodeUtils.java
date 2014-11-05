package elfEngine.basic.node.helper;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.ielfgame.stupidGame.design.hotSwap.flash.FlashMainNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyFrameArrayNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyFrameNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyStorageNode;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.timeLine.KeyFramerNode;

public class NodeUtils {
	
	private final static HashMap<Class<?>, NewNodeInstance> sMap = new HashMap<Class<?>, NodeUtils.NewNodeInstance>();

	static {
		sMap.put(ElfNode.class, new NewNodeInstance() {
			public ElfNode newInstance(ElfNode father, int order) {
				return new ElfNode(father, order);
			}
		});
		
		sMap.put(KeyFrameNode.class, new NewNodeInstance() {
			public ElfNode newInstance(ElfNode father, int order) {
				return new KeyFrameNode(father, order);
			}
		});
		
		sMap.put(KeyFrameArrayNode.class, new NewNodeInstance() {
			public ElfNode newInstance(ElfNode father, int order) {
				return new KeyFrameArrayNode(father, order);
			}
		});
		
		sMap.put(KeyFramerNode.class, new NewNodeInstance() {
			public ElfNode newInstance(ElfNode father, int order) {
				return new KeyFramerNode(father, order);
			}
		});
		
		sMap.put(KeyStorageNode.class, new NewNodeInstance() {
			public ElfNode newInstance(ElfNode father, int order) {
				return new KeyStorageNode(father, order);
			}
		});
		
		sMap.put(FlashMainNode.class, new NewNodeInstance() {
			public ElfNode newInstance(ElfNode father, int order) {
				return new FlashMainNode(father, order);
			}
		});
	}
	
	public static ElfNode newInstacne(final Class<?> _class, final ElfNode father, final int order) {
		final NewNodeInstance iNewNodeInstance = sMap.get(_class);
		if (iNewNodeInstance != null) {
			return iNewNodeInstance.newInstance(father, order);
		} else {
			try {
				Constructor<?> constructor = _class.getDeclaredConstructor(ElfNode.class, int.class);
				final ElfNode node = (ElfNode) constructor.newInstance(father, order);
				return node;
			} catch (Exception e) {
				System.err.println(_class.getSimpleName());
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private interface NewNodeInstance {
		ElfNode newInstance(final ElfNode father, final int order);
	}
}
