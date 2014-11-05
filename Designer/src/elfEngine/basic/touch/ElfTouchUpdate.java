package elfEngine.basic.touch;

import com.ielfgame.stupidGame.platform.PlatformHelper;

import elfEngine.basic.list.ElfList;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.extend.ElfScreenNode;

public final class ElfTouchUpdate {
	
	private final ElfScreenNode mScreenNode;
	private Object mLock = new Object();
	private ElfList<ElfEvent> mEventList = new ElfList<ElfEvent>();
	
	private final ElfList<ElfNode> mInsertNode = new ElfList<ElfNode>();
	
	public ElfTouchUpdate(final ElfScreenNode node){
		mScreenNode = node;
	}
	
	public void onTouch(MotionEvent event, float height){	
		synchronized (mLock){
			final ElfEvent newEvent = ElfEvent.obtain(event, height); 
			mEventList.insertLast(newEvent);
		}
	}
	
	public void addTouchNode(final ElfNode node) {
		mInsertNode.insertFirst(node);
	}
	
	public void removeTouchNode(ElfNode node) {
		mInsertNode.remove(node);
	}
	
	public void update(){ 
		synchronized (mLock) { 
			final ElfList<ElfEvent>.Iterator itOut = mEventList.iterator(true);
			final ElfNode node = mScreenNode.getBindNode();
			
			while(itOut.hasNext()){
				final ElfEvent event = itOut.next(); 
				
				BasicEventDecoder.onTouchGlobal(event);
				
				boolean flag = false;
				if((event.stateMask & PlatformHelper.CTRL) != 0) {
					final ElfList<ElfNode>.Iterator nodeIter = mInsertNode.iterator(true);
					while(nodeIter.hasNext()){
						final ElfNode aNode = nodeIter.next(); 
						if(aNode.onNextSelectTouch(event)) {
							flag = true;
							break;
						}
					}
				}
				
//				final ElfList<ElfNode>.Iterator nodeIter2 = mScreenNode.getChildList().iterator(false);
//				while(nodeIter2.hasNext()) {
//					final ElfNode node2 = nodeIter2.next();
//					if(node2 != node) {
//						if(node2.onNextSelectTouch(event)) {
//							flag = true;
//							break;
//						}
//					} else {
//					}
//				}
				
				if(!flag && node!=null && !node.onPreSelectTouch(event) && !node.onSelectTouch(event)) { 
					node.onNextSelectTouch(event); 
				}	
				
				if(event.action == MotionEvent.LEFT_UP) {
					BasicEventDecoder.resetGlobal(event);
				}
				
				event.recycle();
			}
			
			mEventList.clear();
		}
	}
}
