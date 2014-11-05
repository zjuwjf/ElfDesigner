//package elfEngine.basic.node.nodeAnimate;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Set;
//
//import com.ielfgame.stupidGame.NodeViewWorkSpaceTab;
//import com.ielfgame.stupidGame.ResJudge;
//import com.ielfgame.stupidGame.power.PowerMan;
//import com.ielfgame.stupidGame.utils.FileHelper;
//
//import elfEngine.basic.node.ElfNode;
//
//public class ElfMultiAnimateNode extends ElfNode{
//
//	public ElfMultiAnimateNode(ElfNode father, int ordinal) {
//		super(father, ordinal);
//		setName("#multiAnimates");
//	}
//	
//	private String mMultiAnimateFolder = null;
//	public String getMultiAnimateFolder() { 
//		return mMultiAnimateFolder;
//	} 
//	public void setMultiAnimateFolder(String multiAnimateFolder) { 
//		this.mMultiAnimateFolder = multiAnimateFolder; 
//		final File file = new File(mMultiAnimateFolder); 
//		if(file.exists() && file.isDirectory()) { 
//			final String [] ids = file.list(); 
//			final HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
//			for(String id: ids) { 
//				if(ResJudge.isRes(id)) { 
//					final int len = id.length(); 
//					
//					int labelStart = 0, numStart = 0; 
//					int labs = 0, nums = 0; 
//					
//					for(int i=0; i<len; i++) { 
//						final char c = id.charAt(i); 
//						if(nums <= 0) { 
//							//not find num 
//							if(c == '.') { 
//								if(labs > 0) { 
//									deal(map, multiAnimateFolder, id, labelStart, labs, numStart, nums);
//								} 
//								break;
//							} else if(Character.isDigit(c)) { 
//								numStart = i; 
//								nums=1; 
//							} else { 
//								labs++; 
//							} 
//						} else { 
//							//find num 
//							if(c == '.') { 
//								deal(map, multiAnimateFolder, id, labelStart, labs, numStart, nums);
//								break;
//							} else if(Character.isDigit(c)) { 
//								nums++;
//							} else { 
//								deal(map, multiAnimateFolder, id, labelStart, labs, numStart, nums);
//								
//								labelStart = i; 
//								labs = 1; 
//								numStart = 0; 
//								nums=0; 
//							} 
//						} 
//					} 
//				} 
//			} 
//			
//			rebuild(map); 
//		} else {
//			rebuild(new HashMap<String, ArrayList<String>>()); 
//		} 
//	} 
//	protected final static int REF_MultiAnimateFolder = FACE_ALL_SHIFT | DROP_RESID_SHIFT | COPY_SHIFT | PASTE_SHIFT; 
//	
//	public String getMultiAnimateFolderXML() { 
//		return mMultiAnimateFolder;
//	} 
//	public void setMultiAnimateFolderXML(String multiAnimateFolder) { 
//		mMultiAnimateFolder = multiAnimateFolder;
//	} 
//	protected final static int REF_MultiAnimateFolderXML = XML_ALL_SHIFT;
//	
//	private String [] mMultiAnimate = new String[0];
//	public String [] getMultiAnimate() { 
//		return mMultiAnimate; 
//	} 
//	public void setMultiAnimate(String [] multiAnimate) { 
//		this.mMultiAnimate = multiAnimate; 
//	} 
//	protected static final int REF_MultiAnimate = FACE_GET_SHIFT + XML_ALL_SHIFT + COPY_SHIFT; 
//	
//	private String mState = null;
//	public void setState(String state) { 
//		boolean legalState = false;
//		for(int i=0; !legalState && mMultiAnimate!=null && i<mMultiAnimate.length; i++) {
//			if(mMultiAnimate[i].equals(state)) { 
//				legalState = true; 
//			} 
//		} 
//		if(legalState) {
//			mState = state; 
//			
//			this.iterateChilds(new IIterateChilds() {
//				public boolean iterate(ElfNode node) {
//					if(node instanceof AnimateNode) {
//						if(node.getName().equals(mState)) {
//							node.setVisible(true); 
//							node.setPaused(false); 
//						} else { 
//							node.setVisible(false); 
//							node.setPaused(true); 
//						}
//					}
//					return false;
//				}
//			});
//		}
//	} 
//	public String getState() { 
//		return mState; 
//	} 
//	public String [] arrayState() { 
//		return mMultiAnimate; 
//	} 
//	protected static final int REF_State = FACE_ALL_SHIFT;
//	
//	
//	public static void main(String [] args) { 
//		System.err.println("v:"+Integer.valueOf("")); 
//	} 
//	
//	void deal(HashMap<String, ArrayList<String>> map, String prefix, String id, int labelStart, int labelNums, int numStarts, int numNums) {
//		final String label = id.substring(labelStart, labelStart+labelNums); 
//		try {
//			int num = 1;
//			if(numNums > 0) { 
//				num = Integer.valueOf( id.substring(numStarts, numStarts+numNums) ); 
//			}
//			ArrayList<String> list = map.get(label);
//			if(list == null) {
//				list = new ArrayList<String>();
//				map.put(label, list);
//			}
//			while(list.size() < num) { 
//				list.add(null); 
//			} 
//			list.set(num-1, prefix + FileHelper.DECOLLATOR +id);
//		} catch (Exception e) { 
//		} 
//	} 
//
//	void rebuild(final HashMap<String, ArrayList<String>> map) { 
//		final Set<String> keySet = map.keySet(); 
//		mMultiAnimate = new String[keySet.size()]; 
//		keySet.toArray(mMultiAnimate); 
//		
//		this.iterateChilds( new IIterateChilds() { 
//			public boolean iterate(ElfNode node) { 
//				if(node instanceof AnimateNode) { 
//					final String label = node.getName(); 
//					if( keySet.contains(label) ) { 
//						final ArrayList<String> list = map.get(label); 
//						final String [] ids = new String[list.size()]; 
//						list.toArray(ids); 
//						((AnimateNode) node).setFrameResids(ids); 
//						
//						keySet.remove(label); 
//					} 
//				} 
//				return false; 
//			} 
//		}); 
//		
//		for(String label : keySet) {
//			try {
//				final AnimateNode node = new AnimateNode(this, Integer.MAX_VALUE); 
//				node.setName(label);
//				PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addNode(this, node, Integer.MAX_VALUE); 
//				final ArrayList<String> list = map.get(label); 
//				final String [] ids = new String[list.size()];
//				list.toArray(ids);
//				node.setFrameResids(ids); 
//			} catch (Exception e) { 
//			} 
//		} 
//	} 
//	
//}
