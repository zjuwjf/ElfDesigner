package elfEngine.basic.node.hf;

import java.util.LinkedList;

import org.cocos2d.actions.ease.EaseBackIn;
import org.cocos2d.actions.ease.EaseBackOut;
import org.cocos2d.actions.interval.ScaleTo;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.modifier.FollowModifier;
import elfEngine.basic.modifier.INodeModifier.ModifierListener;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.TouchNode;
import elfEngine.basic.node.particle.modifier.MathHelper;
import elfEngine.basic.touch.HFDecoder;

public class HFST { 
	
//	static final AtomicBoolean sLocker = new AtomicBoolean(); 
	
	public enum OnStateType { 
		OnStart, OnOver, OnGoing, OnFinished
	} 
	
	public interface IOnState { 
		public void onState(OnStateType type);  
	} 
	
	public static class HFNode extends TouchNode implements IOnState{ 
		
		private final HFDecoder mHFDecoder = new HFDecoder();
		
		public HFNode(ElfNode father, int ordinal) { 
			super(father, ordinal); 
			this.setTouchDecoder(mHFDecoder); 
		} 
		
		public void calc(float t) {
			super.calc(t);
			mHFDecoder.run();
		}
		
		//holdTime
		public int getHoldTime() { 
			return mHFDecoder.getHoldTime();
		} 
		public void setHoldTime(int time) { 
			mHFDecoder.setHoldTime(time);
		} 
		protected final static int REF_HoldTime = DEFAULT_SHIFT; 
		
		private float mHoldScale = 2;
		public void setHoldScale(float scale) {
			mHoldScale = scale;
		}
		public float getHoldScale() {
			return mHoldScale;
		}
		protected final static int REF_HoldScale = DEFAULT_SHIFT;
		
		private float mAnimateTime = 500;
		public float getAnimateTime() {
			return mAnimateTime;
		}
		public void setAnimateTime(float scaleAnimateTime) {
			mAnimateTime = scaleAnimateTime;
		}
		protected final static int REF_AnimateTime = DEFAULT_SHIFT;
		
		protected IOnState mIOnState;
		
		private STNode mLastSeatNode;
		public void onState(OnStateType type) { 
			if(mIOnState != null && type == OnStateType.OnGoing) { 
				mIOnState.onState(type); 
			} 
			
			ElfPointf pos;
			STNode goingST;
			switch(type) {
			case OnStart:
				//logic detach
				mLastSeatNode = this.getCurrentSeat();
				if( mLastSeatNode != null) { 
					mLastSeatNode.setHFNode(null); 
					this.setCurrentSeat(null); 
				} 
				
				//to finger
				if(mFingerBinder != null) { 
					final NodeViewWorkSpaceTab nodeView = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
					pos = this.getPositionInScreen(); 
					nodeView.removeNode(this); 
					nodeView.addNode(mFingerBinder, this, Integer.MAX_VALUE, true); 
					this.setPositionInScreen(pos); 
				} 
				
				//scale
				this.stopActions(); 
				this.clearModifiers(); 
				this.runAction(EaseBackIn.action(ScaleTo.action(mAnimateTime/1000, mHoldScale))); 
				break;
			case OnOver:
				break;
			case OnGoing:
				autoGrabBefore();
				if(getGoingSeat() == null) {
					switch (mGoingType) { 
					case Nearest:
						setGoingSeat(getGoingSeatByNearest());
						break;
					case OrderInSize:
						setGoingSeat(getGoningSeatByOrderInSize());
						break;
					}
				}
				
				this.stopActions();
				this.clearModifiers();
				this.setUseModifier(true);
				this.runAction(EaseBackOut.action(ScaleTo.action(mAnimateTime/1000, 1)));
				
				goingST = getGoingSeat(); 
				
				if(goingST != null) { 
					final HFNode hf = goingST.getHFNode(); 
					
					this.setCurrentSeat(goingST);
					goingST.setHFNode(this);
					this.setGoingSeat(null);
					
					if(hf == this) { 
						//nothing
						System.err.println("nothing should not here");
					} else if(hf != null) { 
						//delay ?
						hf.setCurrentSeat(null); 
						hf.setGoingSeat(mLastSeatNode);
						
						final boolean auto = hf.getAutoGrab();
						hf.setAutoGrab(false);
						hf.onState(OnStateType.OnStart);
						hf.onState(OnStateType.OnGoing);
						hf.setAutoGrab(auto);
					} 
					
					final FollowModifier follow = new FollowModifier(goingST, mAnimateTime, mAnimateTime, InterType.Viscous);
					follow.setRemovable(true);
					follow.setModifierListener(new ModifierListener() { 
						public void onFinished(ElfNode node) { 
							onState(OnStateType.OnFinished); 
						} 
					}); 
					
					this.addModifier(follow);
					
				} else { 
					onState(OnStateType.OnFinished);
				} 
				
				mLastSeatNode = null;
				autoGrabAfter();
				
				break;
			case OnFinished:
				goingST = getCurrentSeat();
				if(goingST != null) {
					final NodeViewWorkSpaceTab nodeView = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
					nodeView.removeNode(this);
					nodeView.addNode(goingST, this, Integer.MAX_VALUE, true);
					this.setPosition(0, 0);
				} else {
//					this.setColor(new ElfColor(0, 0, 1, 1));
				}
				
				break;
			} 
			
			if(mIOnState != null && type != OnStateType.OnGoing) {
				mIOnState.onState(type); 
			}
		} 

		// listen
		// onStart, onOver, onGoing, onFinised
		// sample, onStart, onFinished
		
		// data
		// grabLevel,
		protected boolean mAutoGrab = false;
		public void setAutoGrab(boolean auto) {
			mAutoGrab = auto;
		}
		public boolean getAutoGrab() {
			return mAutoGrab;
		}
		protected final static int REF_AutoGrab = DEFAULT_SHIFT;
		
		protected int mGrabLevel = 0;
		public int getGrabLevel() {
			return mGrabLevel;
		}
		public void setGrabLevel(int grabLevel) {
			mGrabLevel = grabLevel;
		}
		protected final static int REF_GrabLevel = DEFAULT_SHIFT;
		
		public void autoGrabBefore() {
			if(mAutoGrab) {
				mGrabLevel++;
			}
		}
		public void autoGrabAfter() {
			if(mAutoGrab) {
				mGrabLevel--;
			}
		}
		
		public enum GoingType { 
			Nearest, OrderInSize
		} 
		protected GoingType mGoingType = GoingType.OrderInSize;
		public void setGoingType(GoingType type) {
			mGoingType = type;
		}
		public GoingType getGoingType() {
			return mGoingType;
		}
		protected final static int REF_GoingType = DEFAULT_SHIFT;
		
		// functions
		// policy : nearest? origin?
		// getGoingSeatByNearest, getGoningSeatByOriginFirst
		public STNode getGoingSeatByNearest() {
			STNode ret = null;
			float distance = Float.MAX_VALUE;
			final ElfPointf pos = this.getPositionInScreen();
			
			for(STNode node : mSeatsList) {
				if(node.getIsLocked()) {
					continue;
				}
				if(node.getStateType() == SeatType.Empty || 
						(node.getStateType() == SeatType.Full && node.getHFNode() != null && this.getGrabLevel() > node.getHFNode().getGrabLevel()) ) {
					final ElfPointf pos2 = node.getPositionInScreen(); 
					final float dis = MathHelper.sqrt((pos.x-pos2.x)*(pos.x-pos2.x)+(pos.y-pos2.y)*(pos.y-pos2.y));
					if(dis < distance) {
						distance = dis;
						ret = node;
					}
				}
			}
			return ret;
		}
		
		public STNode getGoningSeatByOrderInSize() {
			final int length = mSeatsList.size();
			if(length == 0) {
				return null;
			}
			final ElfPointf pos = this.getPositionInScreen();
			
			for(int i=length-1; i>=0;i--) {
				final STNode node = mSeatsList.get(i);
				if(node.getIsLocked()) {
					continue;
				}
				
				if(node.getStateType() == SeatType.Empty || 
						(node.getStateType() == SeatType.Full && node.getHFNode() != null && this.getGrabLevel() > node.getHFNode().getGrabLevel()) ) {
					if(i == 0) {
						return node;
					} else if(node.isInSize(pos)) {
						return node;
					} 
				} 
			} 
			return null;
		}
		
		final LinkedList<STNode> mSeatsList = new LinkedList<STNode>();
		public void clearSeats() {
			mSeatsList.clear();
		}
		public void addSeat(STNode st) {
			mSeatsList.add(st);
		}
		public void removeSeat(STNode st) {
			mSeatsList.remove(st);
		}
		public void removeSeat(int index) {
			mSeatsList.remove(index);
		}
		public STNode getSeatByIndex(int index) {
			return mSeatsList.get(index);
		}
		public int getSeatsNum() {
			return mSeatsList.size();
		}
		
		// clearSeats, addSeat, removeSeat, ---> onStart
		// getGoingSeat, setGoingSeat --->onFinised, data
		// getCurrentSeat, setCurrentSeat --->auto
		// setBindNode, getBindNode, ---> onStart
		
		protected STNode mGoingSeat;
		protected STNode mCurrentSeat;
		protected ElfNode mFingerBinder;
		
		public void setGoingSeat(STNode stNode) {
			if(mSeatsList.contains(stNode)) {
				mGoingSeat = stNode; 
			} else {
				mGoingSeat = null; 
			}
		}
		public STNode getGoingSeat() {
			return mGoingSeat;
		}
		
		public void setCurrentSeat(STNode stNode) {
			mCurrentSeat = stNode; 
		}
		public STNode getCurrentSeat() {
			return mCurrentSeat;
		}
		public String getCurrentSeatByName(){
			if(mCurrentSeat == null) {
				return null;
			} else {
				return mCurrentSeat.getFullName();
			}
		}
		public void setCurrentSeatByName(final String name){
			final ElfNode screenNode = PowerMan.getSingleton(DataModel.class).getScreenNode();
			screenNode.runWithDelay(new Runnable() {
				public void run() {
					final ElfNode node = screenNode.findByName(name);
					if(node != null && node instanceof STNode) {
						setCurrentSeat((STNode)node);
					}
				} 
			}, 0);
		}
		protected final static int REF_CurrentSeatByName = FACE_GET_SHIFT | XML_ALL_SHIFT;
		
		public void setFingerBinder(ElfNode binder) { 
			mFingerBinder = binder; 
		}
		public ElfNode getFingerBinder() { 
			return mFingerBinder; 
		}
		
		public void setSeatNodesByName(final String [] names) {
			if(names != null) {
				final ElfNode screenNode = PowerMan.getSingleton(DataModel.class).getScreenNode();
				screenNode.runWithDelay(new Runnable() {
					public void run() { 
						clearSeats(); 
						for(int i=0; i<names.length; i++) { 
							final ElfNode node = screenNode.findByName(names[i]); 
							if(node != null && node instanceof STNode) { 
								addSeat((STNode)node); 
							} 
						} 
					} 
				}, 0); 
			} 
		} 
		public String [] getSeatNodesByName() { 
			final ElfNode [] nodes = new ElfNode[mSeatsList.size()];
			mSeatsList.toArray(nodes);
			
			final String [] ret = new String[nodes.length];
			for(int i=0; i<ret.length; i++) { 
				if(nodes[i] != null) { 
					ret[i] = nodes[i].getFullName(); 
				} else { 
					ret[i] = "null";
				} 
			}
			return ret;
		}
		protected final static int REF_SeatNodesByName = DEFAULT_SHIFT; 
		
		public void setFingerBinderByName(final String name) {
			final ElfNode screenNode = PowerMan.getSingleton(DataModel.class).getScreenNode();
			screenNode.runWithDelay(new Runnable() {
				public void run() {
					final ElfNode node = screenNode.findByName(name);
					setFingerBinder(node);
				}
			}, 0);
		} 
		public String getFingerBinderByName() { 
			if(getFingerBinder() == null) {
				return null;
			} else {
				return getFingerBinder().getFullName();
			}
		}
		protected final static int REF_FingerBinderByName = DEFAULT_SHIFT;
	} 
	
	public enum SeatType {
		Full, Empty
	}
	
	public static class STNode extends ElfNode { 
		public STNode(ElfNode father, int ordinal) { 
			super(father, ordinal); 
		} 
		// listen
		// onBeOver
		// data
		// changeOrginSeat
		
		protected boolean mIsLocked = false;
		public void setIsLocked(boolean is) {
			mIsLocked = is;
		} 
		public boolean getIsLocked() {
			return mIsLocked;
		}
		protected final static int REF_IsLocked = DEFAULT_SHIFT;
		
		protected SeatType mStateType = SeatType.Empty;
		public SeatType getStateType() {
			return mStateType;
		}
		private void setStateType(SeatType type) {
			mStateType = type;
		}
		protected final static int REF_StateType = FACE_GET_SHIFT;
		
		protected HFNode mCurrentHFNode;
		public void setHFNode(HFNode hfNode) {
			mCurrentHFNode = hfNode;
			if(mCurrentHFNode != null) { 
				setStateType(SeatType.Full);
			} else { 
				setStateType(SeatType.Empty);
			} 
		}
		public HFNode getHFNode() { 
			return mCurrentHFNode;
		}
		
		public void setCurrentHFNode(final String name) {
			final ElfNode screenNode = PowerMan.getSingleton(DataModel.class).getScreenNode();
			screenNode.runWithDelay(new Runnable() {
				public void run() {
					final ElfNode node = screenNode.findByName(name);
					if(node != null && node instanceof HFNode) {
						setHFNode((HFNode)node);
					}
				} 
			}, 0);
		}
		public String getCurrentHFNode() {
			if(getHFNode() == null) {
				return null;
			} else {
				return getHFNode().getFullName();
			}
		}
		protected final static int REF_CurrentHFNode = FACE_GET_SHIFT | XML_ALL_SHIFT;
		
		public void setPopHFNode() { 
			final HFNode hf = getHFNode(); 
			if(hf != null) { 
				final boolean auto = hf.getAutoGrab(); 
				hf.setAutoGrab(false); 
				hf.onState(OnStateType.OnStart); 
				this.setHFNode(null);
				hf.onState(OnStateType.OnGoing); 
				hf.setAutoGrab(auto); 
			} 
		} 
		protected final static int REF_PopHFNode = FACE_SET_SHIFT; 
	} 
	
	//auto create c++
	
	//map cover
	//list cover
	
	//swip 
	//gesture 
	//coverFlow 
	//
	
	//button, tab, click, hold, input
	//
	
	//数据类型
	//
	//
	
	//
	
	//
	
	//
	
	
}
