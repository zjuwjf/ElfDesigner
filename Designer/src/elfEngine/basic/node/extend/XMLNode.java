package elfEngine.basic.node.extend;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.ielfgame.stupidGame.xml.XMLVersionManage;

import elfEngine.basic.node.ElfNode;

public class XMLNode extends ElfNode{

	private final HashMap<String, ElfNode> mDynamicNodeMap = new HashMap<String, ElfNode>();
	
	public XMLNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}

	final ElfNode getDynamicNode(final String key) {
		return mDynamicNodeMap.get(key).copyDeep();
	}
	
	public void loadXML(final String xmlPath) {
		final InputStream stream = this.getClass().getResourceAsStream("SublinesControl.xml");
		
		final List<Object> list = XMLVersionManage.readFromXML(stream);
		
		for (final Object object : list) {
			if (object instanceof ElfNode) {
				final ElfNode node = (ElfNode)object;
				node.setParent(this);
				node.addToParent();
			}
		}
		
		this.iterateChildsDeep(new IIterateChilds() { 
			public boolean iterate(final ElfNode node) { 
				final String name = node.getName(); 
				if(name.startsWith("@")) { 
					mDynamicNodeMap.put(name, node); 
				} 
				return false;
			} 
		}); 
		
		final Set<String> set = mDynamicNodeMap.keySet();
		for(String key : set) {
			final ElfNode node = mDynamicNodeMap.get(key);
			node.removeFromParent();
		}
	}
}
