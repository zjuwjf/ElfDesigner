package elfEngine.basic.node.nodeFollow;

import java.util.ArrayList;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.FollowType;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.TouchNode;
import elfEngine.basic.touch.FollowEventDecoder;
import elfEngine.basic.touch.SwipTouchDecoder.IOnStayIndexChangeListener;
import elfEngine.basic.touch.SwipTouchDecoder.IOnTouchUp;

public class FollowNode extends TouchNode{

	public FollowNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		setTouchDecoder(mFollowEventDecoder);
		
		setName("#follow");
		
		setTouchEnable(true);
	}
	
	protected final FollowEventDecoder mFollowEventDecoder = new FollowEventDecoder();
	
	public int getCurrentStayIndex() {
		return mFollowEventDecoder.getCurrentStayIndex();
	}
	public void setCurrentStayIndex(int index) {
		mFollowEventDecoder.setCurrentStayIndex(index);
	} 
	protected final static int REF_CurrentStayIndex = DEFAULT_SHIFT;
	
	
	public FollowType getFollowType() {
		return mFollowEventDecoder.getFollowType();
	}
	public void setFollowType(FollowType mFollowType) {
		mFollowEventDecoder.setFollowType(mFollowType);
	} 
	protected final static int REF_FollowType = DEFAULT_SHIFT;
	
	public ElfPointf[] getStayRanges() {
		return mFollowEventDecoder.getStayRanges();
	}
	public void setStayRanges(ElfPointf[] mStayRanges) {
		mFollowEventDecoder.setStayRanges(mStayRanges);
	} 
	protected final static int REF_StayRanges = DEFAULT_SHIFT;
	
	public ElfNode[] getStayNodes() { 
		return mFollowEventDecoder.getStayNodes(); 
	}
	public void setStayNodes(ElfNode[] mStayNodes) {
		mFollowEventDecoder.setStayNodes(mStayNodes);
	} 
	
	public void setStayNodesAsSelectNodes() {
		final ArrayList<ElfNode> selects = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		final ArrayList<ElfNode> selectTmp = new ArrayList<ElfNode>();
		selectTmp.addAll(selects);
		selectTmp.remove(this);
		
		final ElfNode [] nodes = new ElfNode[selectTmp.size()];
		selectTmp.toArray(nodes);
		
		FollowNode.this.setStayNodes(nodes);
	}
	protected final static int REF_StayNodesAsSelectNodes = FACE_SET_SHIFT;
	
	//use string
	public void setStayNodesByName(final String [] names) {
		if(names != null) {
			final ElfNode screenNode = PowerMan.getSingleton(DataModel.class).getScreenNode();
			screenNode.runWithDelay(new Runnable() {
				public void run() {
					final ElfNode [] nodes = new ElfNode[names.length];
					for(int i=0; i<nodes.length; i++) { 
						final ElfNode node = screenNode.findByName(names[i]);
						nodes[i] = node;
					} 
					
					FollowNode.this.setStayNodes(nodes);
				}
			}, 0);
		} 
	} 
	public String [] getStayNodesByName() { 
		final ElfNode [] nodes = mFollowEventDecoder.getStayNodes();
		if(nodes != null) {
			final String [] ret = new String[nodes.length];
			for(int i=0; i<ret.length; i++) { 
				if(nodes[i] != null) {
					ret[i] = nodes[i].getFullName();
				} else {
					ret[i] = "null";
				} 
			}
			return ret;
		} else {
			return new String[0];
		} 
	}
	protected final static int REF_StayNodesByName = DEFAULT_SHIFT;
	
	public void setCatched(final boolean select) {
		mFollowEventDecoder.setCatched(select);
	} 
	public boolean getCatched() { 
		return mFollowEventDecoder.getCatched(); 
	} 
	protected final static int REF_Catched = DEFAULT_SHIFT;
	
	public float getAnimateTime() {
		return mFollowEventDecoder.getAnimateTime();
	}
	public void setAnimateTime(float mAnimateTime) {
		mFollowEventDecoder.setAnimateTime(mAnimateTime);
	} 
	protected final static int REF_AnimateTime = DEFAULT_SHIFT;
	
	public InterType getInterType() { 
		return mFollowEventDecoder.getInterType();
	} 
	public void setInterType(InterType mInterType) { 
		mFollowEventDecoder.setInterType(mInterType);
	} 
	protected final static int REF_InterType = DEFAULT_SHIFT;
	
	public void setOnStayIndexChangeListener(final IOnStayIndexChangeListener listener) { 
		mFollowEventDecoder.setOnStayIndexChangeListener(listener);
	} 
	public void setOnTouchUp(IOnTouchUp onTouchUp){ 
		mFollowEventDecoder.setOnTouchUp(onTouchUp);
	} 
}
