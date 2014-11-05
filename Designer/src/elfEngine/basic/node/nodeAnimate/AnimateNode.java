package elfEngine.basic.node.nodeAnimate;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.data.StringHelper;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class AnimateNode extends ElfNode{
	
	public AnimateNode(ElfNode father, int order) {
		super(father, order);
		this.setName("#animate");
	}
	
	public void onRecognizeRequiredNodes() { 
		super.onRecognizeRequiredNodes();
		mAnimateFrames.clear();
		this.iterateChilds(new IIterateChilds() { 
			public boolean iterate(ElfNode node) { 
				if(node instanceof AnimateFrameNode) { 
					mAnimateFrames.add((AnimateFrameNode)node);  
				} 
				return false; 
			} 
		}); 
	} 
	
	private ArrayList<AnimateFrameNode> mAnimateFrames = new ArrayList<AnimateNode.AnimateFrameNode>(); 
	
	private String mFrameResidsFolder = null;
	public void setResidFolder(final String frameResidsFolder) { 
		mFrameResidsFolder = frameResidsFolder; 
		final File file = new File(mFrameResidsFolder); 
		if(file.exists() && file.isDirectory()) { 
			final String [] frames = file.list(); 
			final LinkedList<String> resids = new LinkedList<String>(); 
			
			for(int i=0; frames!=null && i<frames.length; i++) { 
				frames[i] = frames[i]; 
				if(ResJudge.isRes(frames[i])) { 
					resids.add(frames[i]); 
				} 
			} 
			final String [] ret = new String[resids.size()];
			resids.toArray(ret); 
			
			StringHelper.sortByLastInt(ret);
			
			this.setFrameResids(ret);
		} 
	} 
	public String getResidFolder() { 
		return mFrameResidsFolder; 
	} 
	protected final static int REF_ResidFolder = DEFAULT_SHIFT | DROP_RESID_SHIFT ; 
	
	public String [] getSelfResids() {
		return getFrameResids();
	}
	
	public void setSelfResids(final String[] resids) {
		this.setFrameResids(resids);
	}
	
	//FrameResids 
	public String[] getFrameResids() { 
		try {
			final String [] ret = new String[mAnimateFrames.size()];
			
			for(int i=0; i<ret.length; i++) {
				ret[i] = mAnimateFrames.get(i).getResid();
			} 
			return ret; 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new String[]{};
	} 
	public void setFrameResids(final String[] frameResids) {
		if(frameResids != null) {
			//clear all child AnimateFrameNode
			for(int i=0; i<frameResids.length; i++) {
				frameResids[i] = PlatformHelper.toCurrentPath(frameResids[i]);
			}
			
			mAnimateFrames.clear();
			
			final int [] usedCount = {0};
			this.iterateChilds(new IIterateChilds() { 
				public boolean iterate(ElfNode node) { 
					if(node instanceof AnimateFrameNode) {
						if(usedCount[0] >= frameResids.length) { ;
							mAnimateFrames.remove((AnimateFrameNode)node);
							node.removeFromParentView(false);
						} else { 
							mAnimateFrames.add((AnimateFrameNode)node);
							node.setResid(frameResids[usedCount[0]]);
						} 
						usedCount[0]++;
					} 
					return false; 
				} 
			}); 
			
			while(usedCount[0] < frameResids.length) { 
				final AnimateFrameNode node = new AnimateFrameNode(this, Integer.MAX_VALUE); 
				node.setResid(frameResids[usedCount[0]]);
				try {
					PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addNode(this, node, Integer.MAX_VALUE, false); 
				} catch (Exception e) {
				} 
				
				usedCount[0]++; 
			} 
		} 
	} 
	protected final static int REF_FrameResids = DEFAULT_SHIFT;
	
	private float mUpdateRate = 1.0f;
	public float getUpdateRate() {
		return mUpdateRate;
	}
	public void setUpdateRate(float mUpdateRate) {
		this.mUpdateRate = mUpdateRate;
	}
	protected final static int REF_UpdateRate = DEFAULT_SHIFT;

	private float mProgress;
	private int mLastIndex = -1; 
	
	public void calc(float pMsElapsed) { 
		super.calc(pMsElapsed); 
		// 
		final int size = mAnimateFrames.size(); 
		if(size > 0) { 
			if(mLastIndex < 0 || mLastIndex >= size) { 
				mProgress = -mAnimateFrames.get(0).getFrameTime();
				setCurrentIndex(0); 
			} else { 
				mProgress += pMsElapsed * mUpdateRate / 1000f; 
				
				while(mProgress > 0) { 
					int index = mLastIndex + 1;
					if(index >= size) { 
						index = 0; 
					} 
					final float frameTime = mAnimateFrames.get(mLastIndex).getFrameTime(); 
					if(frameTime <= 0) { 
						//stay 
						return ; 
					} 
					mProgress -= frameTime; 
					setCurrentIndex(index); 
				} 
			} 
		} 
	} 
	
	public void setCurrentIndex(int index) { 
		final int size = mAnimateFrames.size(); 
		for(int i=0; i<size; i++) { 
			final AnimateFrameNode frame = mAnimateFrames.get(i);
			frame.setName("#f"+i); 
			if(i == index) { 
				frame.setVisible(true); 
			} else { 
				frame.setVisible(false); 
			} 
		} 
		mLastIndex = index; 
	} 
	public int getCurrentIndex() { 
		return mLastIndex; 
	} 
	protected final static int REF_CurrentIndex = DEFAULT_SHIFT;
	
	public float getLoopTime() {
		try {
			float ret = 0;
			for(AnimateFrameNode node : mAnimateFrames) {
				ret += node.getFrameTime();
			}
			ret /= mUpdateRate;
			return ret;
		} catch (Exception e) {
		}
		return 0;
	}
	protected final static int REF_LoopTime = FACE_GET_SHIFT; 
	
	public float getFrameDelay() {
		return getLoopTime() / mAnimateFrames.size();
	}
	protected final static int REF_FrameDelay = FACE_GET_SHIFT | COPY_SHIFT | XML_SET_SHIFT; 
	
	public void setPause() { 
		this.setPaused(true);
	} 
	protected final static int REF_Pause = FACE_SET_SHIFT;
	
	public void setResume() {
		this.setPaused(false);
	} 
	protected final static int REF_Resume = FACE_SET_SHIFT;
	
	public void setReset() {
		this.reset();
	} 
	protected final static int REF_Reset = FACE_SET_SHIFT;
	
	public void myRefresh() { 
		super.myRefresh(); 
		
//		mAnimateFrames.clear(); 
//		this.iterateChilds(new IIterateChilds() { 
//			public boolean iterate(ElfNode node) { 
//				if(node instanceof AnimateFrameNode) { 
//					mAnimateFrames.add((AnimateFrameNode)node); 
//				} 
//				return false; 
//			} 
//		}); 
		
		reset();
	} 
	
	public void reset() { 
		super.reset();
		mProgress = 0;
		mLastIndex = -1;
		this.setCurrentIndex(0);
	} 
	
	public static class AnimateFrameNode extends ElfNode {
		public AnimateFrameNode(ElfNode father, int order) {
			super(father, order);
			init();
			this.setName("#frame");
		}
		void init() {
			this.addBornListener(new IBornListener() {
				public void onBorn(ElfNode node) {
					final ElfNode father = AnimateFrameNode.this.getParent();
					if(father instanceof AnimateNode) {
						((AnimateNode) father).mAnimateFrames.remove(AnimateFrameNode.this);
						((AnimateNode) father).mAnimateFrames.add(AnimateFrameNode.this);
					} 
				} 
			}); 
			
			this.addDeadListener(new IDeadListener() {
				public void onDead(ElfNode node) {
					final ElfNode father = AnimateFrameNode.this.getParent();
					if(father instanceof AnimateNode) {
						((AnimateNode) father).mAnimateFrames.remove(AnimateFrameNode.this);
					} 
				} 
			}); 
		} 
		private float mFrameTime = 0.1f;
		public float getFrameTime() {
			return mFrameTime;
		}
		public void setFrameTime(float mFrameTime) { 
			this.mFrameTime = mFrameTime; 
		} 
		protected final static int REF_FrameTime = DEFAULT_SHIFT; 
	} 
	
	public void setVisible(boolean visible) {
		if(!getVisible() && visible) {
			this.reset();
		}
		super.setVisible(visible);
	}
	
	public static void main(final String [] args) { 
		Class<?> [] cs = AnimateNode.class.getDeclaredClasses(); 
		for(Class<?> c : cs) { 
			System.err.println(c.getSimpleName()); 
		} 
		System.err.println(AnimateFrameNode.class.isMemberClass()); 
	} 
} 
