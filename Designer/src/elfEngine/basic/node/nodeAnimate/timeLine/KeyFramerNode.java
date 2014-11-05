package elfEngine.basic.node.nodeAnimate.timeLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ielfgame.stupidGame.xml.NewXMLFactory;

import elfEngine.basic.node.ElfNode;

public class KeyFramerNode extends ElfNode {
	static {
		NewXMLFactory.checkIn(KeyFramerNode.class);
		NewXMLFactory.checkIn(KeyDataNode.class);
		NewXMLFactory.checkIn(KeyStoreNode.class);
		NewXMLFactory.checkIn(KeyStoreBranchNode.class);
	}
	
	public final static class KeyDataNode extends ElfNode {
		private int mKeyIndex;
		public void setKeyIndex(int index) {
			mKeyIndex = index;
			
			setName("#key-"+index);
			this.reorder(index);
		}
		public int getKeyIndex() {
			return mKeyIndex;
		}
		protected final static int REF_KeyIndex = DEFAULT_SHIFT;
		
		protected final static int REF_AnchorPosition = 0;
		protected final static int REF_Size = 0;
		protected final static int REF_UseSettedSize = 0;
		protected final static int REF_BlendMode = 0;
		protected final static int REF_TouchEnable = 0;
		protected final static int REF_TouchShielded = 0;
		protected final static int REF_Paused = 0;
		protected final static int REF_CouldMove = 0;
		protected final static int REF_Z = 0;
		protected final static int REF_UseControl = 0;
		
		public KeyDataNode(ElfNode father, int ordinal) {
			super(father, ordinal);
			setName("#key-unknown");
		}
	}
	
	public final static class KeyStoreNode extends ElfNode { 
		public KeyStoreNode(ElfNode father, int ordinal) { 
			super(father, ordinal); 
		} 
		
		public ArrayList<KeyStoreBranchNode> getBranchNodes() {
			final ArrayList<KeyStoreBranchNode> ret = new ArrayList<KeyFramerNode.KeyStoreBranchNode>();
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					if(node instanceof KeyStoreBranchNode) {
						ret.add((KeyStoreBranchNode)node);
					}
					return false;
				}
			});
			return ret;
		}
	} 
	
	public final static class KeyStoreBranchNode extends ElfNode { 
		private ElfNode mDelegateNode;
		public void setDelegate(ElfNode node) {
			mDelegateNode = node;
		}
		public ElfNode getDelegate() {
			return mDelegateNode;
		}
		
		public KeyStoreBranchNode(ElfNode father, int ordinal) { 
			super(father, ordinal); 
		} 
		
		public ArrayList<KeyDataNode> getKeyNodes() {
			final ArrayList<KeyDataNode> ret = new ArrayList<KeyFramerNode.KeyDataNode>();
			this.iterateChilds(new IIterateChilds() {
				public boolean iterate(ElfNode node) {
					if(node instanceof KeyDataNode) {
						ret.add((KeyDataNode)node);
					}
					return false;
				}
			});
			
			Collections.sort(ret, new Comparator<KeyDataNode>() {
				public int compare(KeyDataNode arg0, KeyDataNode arg1) {
					return arg0.getKeyIndex() - arg1.getKeyIndex();
				}
			});
			return ret;
		}
	} 
	
	private int mFPS = 20;
	public void setFPS(int fps) { 
		if(fps > 0) { 
			mFPS = fps; 
		} 
	}
	public int getFPS() { 
		return mFPS; 
	}
	protected final static int REF_FPS = DEFAULT_SHIFT;
	
	private int mStartFrame;
	public void setStartFrame(int frame) {
		mStartFrame = frame;
	}
	public int getStartFrame() {
		return mStartFrame;
	}
	protected final static int REF_StartFrame = DEFAULT_SHIFT;
	
	private int mEndFrame;
	public void setEndFrame(int frame) {
		mEndFrame = frame;
	}
	public int getEndFrame() {
		return mEndFrame;
	}
	protected final static int REF_EndFrame = DEFAULT_SHIFT;
	
	
	public KeyFramerNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	private final String sKeyStorageName = "KeyStorage";
	private KeyStoreNode mKeyStorage;
	
	public KeyStoreNode getKeyStoreNode() {
		return mKeyStorage;
	}
	
	private final void checkKeyStorage() { 
		mKeyStorage = null;
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				final String name = node.getName();
				if (name != null && name.contains(sKeyStorageName) && node instanceof KeyStoreNode) {
					mKeyStorage = (KeyStoreNode)node;
				}
				return mKeyStorage != null;
			}
		}); 
		if (mKeyStorage == null) {
			mKeyStorage = new KeyStoreNode(this, 0);
			mKeyStorage.setName(sKeyStorageName);
			mKeyStorage.addToParent();
			mKeyStorage.setVisible(false);
		}
	}
	
	public void onCreateRequiredNodes() {
		super.onCreateRequiredNodes();
		checkKeyStorage();
	}

	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		checkKeyStorage();
	}
}
