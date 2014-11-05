package elfEngine.basic.node.extend;

import java.util.ArrayList;

import org.cocos2d.actions.ActionManager;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.list.ElfOrderedList;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.TabNode;
import elfEngine.basic.ordinal.IElfOrdinal;
import elfEngine.basic.touch.ElfTouchUpdate;
import elfEngine.basic.touch.MotionEvent;

public class ElfScreenNode extends ElfNode{
	
	public static interface IUpdateHandler {
		public void onUpdate(float pMsElapsed);
	}
	
	public static class DefaultUpdateHandler implements IElfOrdinal {
		private final IUpdateHandler mHandler;
		private final int mPriority;
		public DefaultUpdateHandler(IUpdateHandler handler, int priority) {
			mHandler = handler;
			mPriority = priority;
		}
		public IUpdateHandler getHandler() {
			return mHandler;
		}
		public int ordinal() {
			return mPriority;
		}
	}
	
	public static class UpdateHandlerList implements IUpdateHandler{
		private final ElfOrderedList<DefaultUpdateHandler> mUpdateList = new ElfOrderedList<DefaultUpdateHandler>();
		
		public void addUpdateHandler(final IUpdateHandler handler) {
			addUpdateHandler(handler, 0);
		}
		public void addUpdateHandler(final IUpdateHandler handler, final int priority) {
			mUpdateList.insertAfter(new DefaultUpdateHandler(handler, priority));
		}
		public void removeUpdateHandler(final IUpdateHandler handler) {
			final ElfOrderedList<DefaultUpdateHandler>.Iterator it = mUpdateList.iterator(true);
			while (it.hasNext()) {
				if( it.next().getHandler() == handler) {
					it.remove();
					break;
				}
			}
		}
		
		public void onUpdate(float pMsElapsed) {
			final ElfOrderedList<DefaultUpdateHandler>.Iterator it = mUpdateList.iterator(true);
			while (it.hasNext()) {
				it.next().getHandler().onUpdate(pMsElapsed);
			}
		}
	}
	
	private ElfNode mBindNode;
	private final ElfTouchUpdate mTouchUpdate = new ElfTouchUpdate(this);
	private final UpdateHandlerList mUpdateHandlerList = new UpdateHandlerList();
	private final ArrayList<TabNode> mTabManage = new ArrayList<TabNode>(); 
	private final ActionManager mActionManager = new ActionManager();
	
	public ArrayList<TabNode> getTabNodeManage() { 
		return mTabManage;  
	} 
	
	public ElfScreenNode() { 
		super(null, 0); 
		this.setUseSettedSize(true); 
		setIsInRunningNode(true); 
		setUseDelayHandler(true); 
	} 
	
	public UpdateHandlerList getUpdateHandlerList() {
		return mUpdateHandlerList;
	}
	
	public final ActionManager getActionManager() {
		return mActionManager;
	}
	
	private long mLastTime = System.currentTimeMillis();
	
	public void update(){
		
		mTouchUpdate.update();
		
		final long now = System.currentTimeMillis();
		final float escaped = now - mLastTime;
		mLastTime = now;
		
		mUpdateHandlerList.onUpdate(escaped);
		
		this.calcSprite(escaped);
		mActionManager.tick(escaped/1000f);
		
	} 
	
//	public void drawSprite(){ 
//		resetStaticInSideZOffset();
//		super.drawSprite();
//		this.drawSpriteSelected();
//	}
	
	ElfNode mStoreFather = null;
	
	public void bindNode(ElfNode node){
		if(mBindNode != null){
			mBindNode.removeFromParent();
			mBindNode.setParent(mStoreFather);
		} 
		
		mBindNode = node;
		
		if(mBindNode != null){ 
			mStoreFather = mBindNode.getParent();
			mBindNode.setParent(this);
			mBindNode.addToParent();
			mBindNode.setGLid(getGLId());
		} 
	} 
	
	public ElfNode getBindNode(){ 
		return mBindNode;
	} 
	
	public void onTouch(MotionEvent event){
		mTouchUpdate.onTouch(event, this.getHeight()); 
	} 
	
	public void addTouchNode(ElfNode node) {
		this.removeTouchNode(node);
		mTouchUpdate.addTouchNode(node);
	} 
	
	public void removeTouchNode(ElfNode node) {
		mTouchUpdate.removeTouchNode(node);
	}
	
	public String getName() {
		return "";
	}
	
	private final ArrayList<ElfNode> mSelectNodeList = new ArrayList<ElfNode>();
	public ArrayList<ElfNode> getSelectNodeList() {
		return mSelectNodeList;
	}
	public ElfNode getSelectNode() {
		if (mSelectNodeList.isEmpty()) {
			return null;
		}
		return mSelectNodeList.get(0);
	}
	public void setSelectNode(ElfNode elfBasicNode) {
		mSelectNodeList.remove(elfBasicNode);
		mSelectNodeList.add(0, elfBasicNode);
		
		PowerMan.getSingleton(NodeViewWorkSpaceTab.class).showSelection();
	}
} 
