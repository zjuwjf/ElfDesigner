package elfEngine.basic.node.nodeAnimate;

import java.io.File;
import java.util.LinkedList;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.node.ElfNode;

public class MultiAnimateNode extends ElfNode{

	public MultiAnimateNode(ElfNode father, int copy) {
		super(father, copy);
	}
	
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
	} 
	
	protected String mState = "";
	public boolean setAnimateState(final String state) { 
		mState = state; 
		final boolean [] flag = new boolean[]{false}; 
		
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if(node instanceof SimpleAnimateNode) { 
					((SimpleAnimateNode)node).setResetOnVisibleChange(false);
					final String name = node.getName(); 
					if(name.equals(state) || (name.startsWith("#") && name.substring(1).equals(state))) {
						node.setVisible(true); 
						node.setPaused(false); 
						flag[0] = true; 
					} else { 
						node.setVisible(false); 
						node.setPaused(true); 
					} 
				}
				return false;
			}
		});
		return flag[0];
	} 
	public String getAnimateState() { 
		return mState;
	} 
	public String [] arrayAnimateState() { 
		final LinkedList<String> list = new LinkedList<String>();
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if(node instanceof SimpleAnimateNode) {
					final String name = node.getName();
					if(name.startsWith("#")) { 
						list.add( name.substring(1) );
					} else { 
						list.add( name );
					} 
				}
				return false;
			} 
		}); 
		final String [] ret = new String [list.size()];
		list.toArray(ret); 
		return ret; 
	} 
	protected final static int REF_AnimateState = DEFAULT_SHIFT;
	
	protected String mAnimatesFolder;
	public void setAnimatesFolder(final String path) { 
		mAnimatesFolder = path;
		
		final boolean isSelected = this.isSelected(); 
		if(isSelected) {
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					if(node instanceof SimpleAnimateNode) {
						PowerMan.getSingleton(NodeViewWorkSpaceTab.class).removeNode(node);
					} 
					return false; 
				} 
			}); 
			
			final String [] folders = FileHelper.getFullPaths(path); 
			
			for(int i=0; i<folders.length; i++) { 
				final String folder = folders[i]; 
				if(new File(folder).isDirectory() && !folder.contains(".svn")) { 
					String state = folder.substring( folder.lastIndexOf(FileHelper.DECOLLATOR) +1 ); 
					final SimpleAnimateNode node = new SimpleAnimateNode(this, Integer.MAX_VALUE); 
					node.setName(state);
					node.setResidFolder(folder);
					PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addNode(this, node, Integer.MAX_VALUE, false);
				} 
			} 
			setAnimateState(mState); 
		} 
	} 
	public String getAnimatesFolder() {
		return mAnimatesFolder; 
	} 
	protected final static int REF_AnimatesFolder = DEFAULT_SHIFT | DROP_RESID_SHIFT;
} 
