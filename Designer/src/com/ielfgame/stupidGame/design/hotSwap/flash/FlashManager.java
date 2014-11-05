package com.ielfgame.stupidGame.design.hotSwap.flash;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKey;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashKeyArray;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashStruct.IFlashMain;
import com.ielfgame.stupidGame.power.APowerManSingleton;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.extend.ElfScreenNode.IUpdateHandler;

public class FlashManager extends APowerManSingleton implements IFlashMain {
	
	private IFlashMain mFlashMain;
	
	private boolean mAutoKey;
	private boolean mShowAll;

	private FlashManager() {
		PowerMan.getSingleton(DataModel.class).getScreenNode().getUpdateHandlerList().addUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pMsElapsed) {
				FlashUpdateManager.updateSelects();
			}
		});
	}
	
	public static void init() {
		final FlashManager fm = PowerMan.getSingleton(FlashManager.class);
		if(fm == null) {
			new FlashManager();
		}
	}
	
	public void setAutoKey(boolean b) {
		mAutoKey = b;
	}
	public boolean getAutoKey() {
		return mAutoKey;
	}
	
	public void setShowAll(boolean b) {
		mShowAll = b;
//		if(mFlashMain != null) {
//			mFlashMain.autoReducedOrAll(!mShowAll);
//		}
	}
	
	public boolean getShowAll() {
		return mShowAll;
	}

	public void bindFlashMain(final IFlashMain fmn) {
		mFlashMain = (mFlashMain == fmn) ? null : fmn;
		if(mFlashMain != null) {
			mFlashMain.check();
			mFlashMain.setProgressTime(0);
		}
	}
	
	public IFlashMain getFlashMain() {
		return mFlashMain;
	}
	
	public int getFPS() {
		if(mFlashMain != null) {
			return mFlashMain.getFPS();
		}
		return 1;
	}
	
	public void setFPS(int fps) {
		if(mFlashMain != null) {
			mFlashMain.setFPS(fps);
		}
	}
	
	public IFlashKey[] getAllSelectedFlashKeys() {
		if(mFlashMain != null) {
			return mFlashMain.getAllSelectedFlashKeys();
		}
		return new IFlashKey[0];
	}

	public IFlashKeyArray findFlashKeyArrayByTarget(ElfNode target) {
		if (mFlashMain != null) {
			return mFlashMain.findFlashKeyArrayByTarget(target);
		}
		return null;
	}

	public void setMaxF(int maxF) {
		if(mFlashMain != null) {
			mFlashMain.setMaxF(maxF);
		}
	}

	public int getMaxF() {
		if(mFlashMain != null) {
			return mFlashMain.getMaxF();
		}
		return 20;
	}

	public void play() {
		if(mFlashMain != null) {
			mFlashMain.play();
		}
	}

	public void stop() {
		if(mFlashMain != null) {
			mFlashMain.stop();
		}
	}
	
	public void toggle() {
		if(mFlashMain != null) {
			if(mFlashMain.isPlaying()) {
				mFlashMain.stop();
			} else {
				mFlashMain.play();
			}
		}
	}

	public int getProgressTime() {
		if(mFlashMain != null) {
			return mFlashMain.getProgressTime();
		}
		return 0;
	}

	public void setProgressTime(int mills) {
		if(mFlashMain != null) {
			mFlashMain.setProgressTime(mills);
		}
	}
	
	public void setFrame(int frame) {
		if(mFlashMain != null) {
			mFlashMain.setProgressTime((int)(frame*1000/mFlashMain.getFPS()));
		}
	}
	
	public void update(int dt) {
		if(mFlashMain != null) {
			mFlashMain.update(dt);
		}
	}

	public IFlashKeyArray[] getFlashKeyArrays() {
		if(mFlashMain != null) {
			return mFlashMain.getFlashKeyArrays();
		}
		return new IFlashKeyArray[0];
	}

	public void autoReducedOrAll(boolean reduced) {
		if(mFlashMain != null) {
			mFlashMain.autoReducedOrAll(reduced);
		}
	}

	public IFlashKey[] getAllFlashKeys() {
		if(mFlashMain != null) {
			return mFlashMain.getAllFlashKeys();
		}
		return new IFlashKey[0];
	}

	public void setAllFlashKeySelected(boolean selected) {
		if(mFlashMain != null) {
			mFlashMain.setAllFlashKeySelected(selected);
		}
	}
	
	public void check() {
		if(mFlashMain != null) {
			mFlashMain.check();
		}
	}

	public int getCurrentFrame() {
		if(mFlashMain != null) {
			return mFlashMain.getCurrentFrame();
		}
		return 0;
	}
	
	public void addKeyFrameByTarget(final ElfNode target, final int frame) {
		final IFlashKeyArray kArray = this.findFlashKeyArrayByTarget(target);
		addKeyFrameByKeyArray(kArray, frame);
	}
	
	public IFlashKey addKeyFrameByKeyArray(final IFlashKeyArray kArray, final int frame) {
		if(kArray != null) {
			final IFlashKey kfn = kArray.createFlashKeyByFrame(frame);
			if(kfn != null) {
				kfn.setPropertiesFromElfNode(kArray.getTarget());
				kfn.setFrame(frame);
				kArray.addFlashKey(kfn);
				
				return kfn;
			}
		}
		return null;
	}
	
	public IFlashKey addKeyFramePrevByKeyArray(final IFlashKeyArray kArray, final int frame) {
		if(kArray != null) {
			IFlashKey newKey = null;
			for(int i=frame-1; i>=0; i--) {
				final IFlashKey prevK = kArray.findFlashKeyByFrame(i);
				if(prevK != null) {
					newKey = prevK.copyKey();
					break;
				}
			}
			
			if( newKey != null && kArray.findFlashKeyByFrame(frame) == null ) {
				newKey.setFrame(frame);
				
				kArray.addFlashKey(newKey);
				
				return newKey;
			}
		}
		return null;
	}
	
	public IFlashKey addKeyFrameNextByKeyArray(final IFlashKeyArray kArray, final int frame) {
		if(kArray != null) {
			IFlashKey newKey = null;
			
			final IFlashKey [] keys = kArray.getFlashKeys();
			for(IFlashKey k : keys) {
				if(k.getFrame() > frame) {
					newKey = k.copyKey();
					break;
				}
			}
			
			if( newKey != null && kArray.findFlashKeyByFrame(frame) == null ) {
				newKey.setFrame(frame);
				kArray.addFlashKey(newKey);
				return newKey;
			}
		}
		return null;
	}

	public String getAnimateName() {
		if(mFlashMain != null) {
			return mFlashMain.getAnimateName();
		}
		return null;
	}

	public void setAnimateName(String name) {
		if(mFlashMain != null) {
			mFlashMain.setAnimateName(name);
		}
	}

	public void addAnimate(String name) {
		if(mFlashMain != null) {
			mFlashMain.addAnimate(name);
		}
	}

	public void copyCurrentAnimate(String name) {
		if(mFlashMain != null) {
			mFlashMain.copyCurrentAnimate(name);
		}
	}

	public void deleteAnimate(String name) {
		if(mFlashMain != null) {
			mFlashMain.deleteAnimate(name);
		}
	}

	public String[] arrayAnimateName() {
		if(mFlashMain != null) {
			return mFlashMain.arrayAnimateName();
		}
		return new String[]{};
	}

	public void setSpeedRate(float rate) {
		if(mFlashMain != null) {
			mFlashMain.setSpeedRate(rate);
		}
	}

	public float getSpeedRate() {
		if(mFlashMain != null) {
			return mFlashMain.getSpeedRate();
		}
		return 1;
	}

	public boolean isPlaying() {
		if(mFlashMain != null) {
			return mFlashMain.isPlaying();
		}
		return false;
	}
}
