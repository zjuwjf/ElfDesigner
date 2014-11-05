package elfEngine.basic.node.nodeAnimate;

import java.io.File;
import java.util.LinkedList;

import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.data.StringHelper;
import com.ielfgame.stupidGame.platform.PlatformHelper;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.utils.ElfRandom;

public class SimpleAnimateNode extends ElfNode{

	public SimpleAnimateNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
		setName("#simpleAnimate"); 
	} 
	
	public String [] getSelfResids() {
		if(mResidArray != null) {
			String[] ret = new String[mResidArray.length + 1];
			for(int i=0; i<mResidArray.length; i++) {
				ret[i] = mResidArray[i];
			} 
			ret[mResidArray.length] = getResid();
			return ret;
		} 
		return new String[0];
	}
	
	public void setSelfResids(final String[] resids) {
		this.setResidArray(resids);
	}
	
	public void myRefresh() {
		super.myRefresh();
		
		this.mProgressTime = 0;
		this.mCurrentIndex = 0;
	} 
	
	protected String [] mResidArray = new String[0]; 
	protected String mResidFolder = null; 
	protected int mCurrentIndex = -1; 
	protected float mProgressTime = 0; 
	protected float mFrameDelay = 0.1f; 
	protected boolean mAutoFree = false; 
	protected int mLoops = -1;
	protected float mDuration = Float.MAX_VALUE;
	
	public void setAutoFree(boolean autoFree) {
		mAutoFree = autoFree;
	}
	public boolean getAutoFree() {
		return mAutoFree;
	}
	protected final static int REF_AutoFree = DEFAULT_SHIFT;
	
	public void setLoops(final int loops) { 
		mLoops = loops;
		if(mLoops < 0) {
			this.mDuration = Float.MAX_VALUE;
		} else {
			this.mDuration = mLoops * this.getLoopTime();
		}
	}
	public int getLoops() { 
		return mLoops;
	} 
	protected final static int REF_Loops = DEFAULT_SHIFT;
	
	public void setResidFolder(final String folder) { 
		mResidFolder = folder; 
		final File file = new File(mResidFolder); 
		if(file.exists() && file.isDirectory()) { 
			final String [] list = file.list(); 
			
			final LinkedList<String> idslist = new LinkedList<String>();
			for(int i=0; i<list.length; i++) { 
				final String id = list[i]; 
				if(ResJudge.isRes(id)) { 
					idslist.add(id); 
				} 
			} 
			mResidArray = new String[idslist.size()]; 
			idslist.toArray(mResidArray); 
			
			StringHelper.sortByLastInt(mResidArray);
			
			mProgressTime = 0; 
			mCurrentIndex = -1; 
		} else { 
			mResidArray = new String[0]; 
		} 
	} 
	public String getResidFolder() { 
		return mResidFolder; 
	} 
	protected final static int REF_ResidFolder = DEFAULT_SHIFT | DROP_RESID_SHIFT; 
	
	public String[] getResidArray() {
		return mResidArray;
	}
	public void setResidArray(String[] mResidArray) { 
		if(mResidArray != null) { 
			for(int i=0; i<mResidArray.length; i++) {
				mResidArray[i] = PlatformHelper.toCurrentPath(mResidArray[i]);
			}
			
			this.mResidArray = mResidArray; 
		} 
	} 
	protected final static int REF_ResidArray = DEFAULT_SHIFT | DROP_RESID_SHIFT; 
	
	public int getCurrentIndex() { 
		return mCurrentIndex;
	}
	public void setCurrentIndex(int mCurrentIndex) {
		this.mCurrentIndex = mCurrentIndex;
	}
	protected final static int REF_CurrentIndex = DEFAULT_SHIFT; 
	
	public float getProgressTime() {
		return mProgressTime;
	}
	public void setProgressTime(float mProgressTime) { 
		this.mProgressTime = mProgressTime;
	} 
	protected final static int REF_ProgressTime = DEFAULT_FACE_SHIFT; 
	
	public float getFrameDelay() {
		return mFrameDelay;
	}
	public void setFrameDelay(float mFrameDelay) {
		this.mFrameDelay = mFrameDelay;
	} 
	protected final static int REF_FrameDelay = DEFAULT_SHIFT; 
	
	public float getLoopTime() {
		return mFrameDelay * mResidArray.length;
	}
	protected final static int REF_LoopTime = FACE_GET_SHIFT; 
	
	public void setRandomIndex() {
		if(this.mResidArray != null) {
			final int r = ElfRandom.nextInt( this.mResidArray.length );
			this.setCurrentIndex(r);
		}
	}
	protected final static int REF_RandomIndex = FACE_SET_SHIFT; 
	
	public void calc(float pMsElapsed) { 
		super.calc(pMsElapsed); 
		mProgressTime += (pMsElapsed/1000f); 
		
		if(mProgressTime >= this.mDuration) {
			mProgressTime = this.mDuration;
			setResid(null);
		} else if(mProgressTime >= 0) {
			final int addIndex = (int)(mProgressTime/mFrameDelay); 
			mProgressTime -= addIndex*mFrameDelay; 
			
			final int len = mResidArray.length; 
			if(len > 0) { 
				final int tempIndex = (mCurrentIndex+addIndex); 
				if(tempIndex >= len && mAutoRemoveFromFather) { 
					this.removeFromParent(); 
					return ; 
				}
				final int newIndex = tempIndex%len; 
				if(newIndex != mCurrentIndex) { 
					if(newIndex>=0 && newIndex<mResidArray.length) { 
						setResid(mResidArray[newIndex]); 
					} else { 
						setResid(null); 
					} 
					mCurrentIndex = newIndex; 
				} 
			} 
		} 
	} 
	
	boolean mAutoRemoveFromFather = false;
	
	public void setAutoRemoveFromFather(boolean auto) {
		mAutoRemoveFromFather = auto;
	} 
	
	private boolean mResetOnVisibleChange = true;
	public void setResetOnVisibleChange(boolean reset) {
		mResetOnVisibleChange = reset;
	}
	public boolean getResetOnVisibleChange() {
		return mResetOnVisibleChange;
	}
	
	public void setVisible(boolean visible) {
		if(!getVisible() && visible && mResetOnVisibleChange) {
			this.mProgressTime = 0;
			this.mCurrentIndex = 0;
			
			if(mResidArray != null && mResidArray.length > 0) {
				setResid(mResidArray[mCurrentIndex]); 
			}
		}
		super.setVisible(visible);
	}
} 
