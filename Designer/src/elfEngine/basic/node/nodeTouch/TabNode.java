package elfEngine.basic.node.nodeTouch;

import java.util.ArrayList;

import com.ielfgame.stupidGame.ResJudge;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.extend.ElfScreenNode;
import elfEngine.basic.touch.TabEventDecoder;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.Texture;

public class TabNode extends ButtonNode { 
	
	//add by tangen
	protected ElfNode mNormalNode;
	protected ElfNode mPressedNode;
	protected ElfNode mInvalidNode;
	
	@Override
	protected void drawPic() {
		final String resid = getResid(); 
		switch (mButtonState) { 
		case Normal: 
			if(mNormalNode != null){
				setNodeVisible(mNormalNode,true);
				setNodeVisible(mPressedNode,false);
				setNodeVisible(mInvalidNode,false);
			}else{
				GLManage.draw(resid, mGLid);
			}
			break; 
		case Pressed: 
			if(mPressedNode != null){
				setNodeVisible(mNormalNode,false);
				setNodeVisible(mPressedNode,true);
				setNodeVisible(mInvalidNode,false);
				break;
			}
		case Invalid:
			if(mInvalidNode != null){
				setNodeVisible(mNormalNode,false);
				setNodeVisible(mPressedNode,false);
				setNodeVisible(mInvalidNode,true);
				break;
			}
			if(ResJudge.isRes(resid)) {
				GLManage.draw(resid, mGLid);
			} else {
				GLManage.draw(getNormalResid(), mGLid, Texture.IMAGE_GRAY);
			}
			break; 
		} 
	}
	
	@Override
	public void onCreateRequiredNodes() {
		// TODO Auto-generated method stub
		super.onCreateRequiredNodes();
		
		mNormalNode = new ElfNode(this, 0);
		mPressedNode = new ElfNode(this, 0);
		mInvalidNode = new ElfNode(this, 0);

		mNormalNode.setName("#normal");
		mNormalNode.addToParent();

		mPressedNode.setName("#pressed");
		mPressedNode.addToParent();

		mInvalidNode.setName("#invalid");
		mInvalidNode.addToParent();
	}
	@Override
	public void onRecognizeRequiredNodes() {
		// TODO Auto-generated method stub
		super.onRecognizeRequiredNodes();
		mNormalNode = null;
		mPressedNode = null;
		mInvalidNode = null;

		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				if (node.getName().equals("#normal")) {
					mNormalNode = node;
				} else if (node.getName().equals("#pressed")) {
					mPressedNode = node;
				} else if (node.getName().equals("#invalid")) {
					mInvalidNode = node;
				}
				return false;
			}
		});
	}
	
	private void setNodeVisible(ElfNode node, boolean v){
		if(node != null){
			node.setVisible(v);
		}
	}
	//------------------------------
	
	private final TabEventDecoder mTabEventDecoder = new TabEventDecoder();
	
	protected boolean mIsSelected = false; 
	public boolean getTabSelected() {
		return mIsSelected;
	} 
	public void setTabSelected(final boolean selected) { 
		mIsSelected = selected; 
		
		final ElfNode root = this.getTopNode();
		final ElfNode node = root.findByName(this.getRelatedNodeName());
		if(node != null) {
			node.setVisible(selected);
		}
		
		if(mIsSelected) {
			super.onPressed();
		} else {
			super.onReleased();
		} 
	} 
	protected final static int REF_TabSelected = DEFAULT_SHIFT; 
	
	private ArrayList<TabNode> mTabNodeManage;
	
	private int mTabGroupId = 0;
	public void setTabGroupId(final int id) {
		mTabGroupId = id;
		setTabSelected(false);
	} 
	public int getTabGroupId() {
		return mTabGroupId;
	} 
	protected final static int REF_TabGroupId = DEFAULT_SHIFT;
	
	protected void init() {
		this.addBornListener(new IBornListener() { 
			public void onBorn(ElfNode node) {
				final ElfNode topNode = getTopNode();
				if(topNode instanceof ElfScreenNode) {
					mTabNodeManage = ((ElfScreenNode) topNode).getTabNodeManage();
					mTabNodeManage.add(TabNode.this);
				} else {
					mTabNodeManage = null;
				}
			}
		});
		this.addDeadListener(new IDeadListener() {
			public void onDead(ElfNode node) {
				mTabNodeManage.remove(TabNode.this);
				mTabNodeManage = null;
			} 
		});
		
		mTabEventDecoder.setDelegate(this);
		setTouchDecoder(mTabEventDecoder);
	}
	
	public TabNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		setName("#tab");
		setPriorityLevel(TAB_PRIORITY);
		init();
	} 
	
	public void onReleased() { 
		setTabSelected(false);
	} 
	
	public void onPressed() { 
		if(mTabNodeManage != null) {
			for(TabNode tab : mTabNodeManage) { 
				if(tab != this && tab.getTabSelected() && tab.getTabGroupId() == this.getTabGroupId()) { 
					tab.setTabSelected(false);
				} 
			} 
		}
		
		setTabSelected(true);
	}
	
	public boolean getTriggerOnDown() {
		return mTabEventDecoder.getTriggerOnDown();
	}
	public void setTriggerOnDown(boolean mIsTriggerOnDown) {
		mTabEventDecoder.setTriggerOnDown(mIsTriggerOnDown);
	}
	protected final static int REF_TriggerOnDown = DEFAULT_SHIFT;
	
	public void setTouchGiveUpOnMoveOrOutOfRange(boolean set) {
		mTabEventDecoder.setTouchGiveUpOnMoveOrOutOfRange(set);
	}
	public boolean getTouchGiveUpOnMoveOrOutOfRange() {
		return mTabEventDecoder.getTouchGiveUpOnMoveOrOutOfRange();
	}
	protected final static int REF_TouchGiveUpOnMoveOrOutOfRange = DEFAULT_SHIFT;
	
	private String mRelatedNodeName;
	public void setRelatedNodeName(String name) {
		mRelatedNodeName = name;
	}
	public String getRelatedNodeName() {
		return mRelatedNodeName;
	}
	protected final static int REF_RelatedNodeName = DEFAULT_SHIFT; 
}
