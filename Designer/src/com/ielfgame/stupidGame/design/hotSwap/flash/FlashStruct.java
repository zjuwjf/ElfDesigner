package com.ielfgame.stupidGame.design.hotSwap.flash;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.LoopMode;
import elfEngine.basic.node.ElfNode;

public class FlashStruct {

	/***
	 * six properties
	 */
	public static interface IFlashProperty {
		public void setPosition(ElfPointf pos);

		public ElfPointf getPosition();

		public void setScale(ElfPointf scale);

		public ElfPointf getScale();

		public void setColor(ElfColor color);

		public ElfColor getColor();

		public void setResid(final String resid);

		public String getResid();

		public void setVisible(boolean visible);

		public boolean getVisible();

		public void setRotate(float r);

		public float getRotate();
		
		public void setPropertiesFromElfNode(ElfNode node);
	}

	public static interface IFlashKey extends IFlashProperty {
		public void setFrame(int frame);

		public int getFrame();

		public void setFrameSelect(boolean select);

		public boolean getFrameSelect();
		
		public void setInterType(InterType type);
		
		public InterType getInterType();
		
		public void setInterStrong(int strong);
		
		public int getInterStrong();
		
		public void setLoopMode(LoopMode loopMode);
		
		public LoopMode getLoopMode();
		
		public IFlashKeyArray getKeyFrameArrayParent();
		
		public IFlashKey copyKey();
	}

	public static interface IFlashKeyArray {
		public void setVisibleMask(boolean visible);

		public boolean getVisibleMask();

		public void setLocked(boolean locked);

		public boolean getLocked();

		public IFlashKey[] getFlashKeys();

		public boolean addFlashKey(IFlashKey key);

		public boolean removeFlashKey(IFlashKey key);

		public void setTarget(ElfNode node);

		public ElfNode getTarget();

		public boolean translateKey(IFlashKey key, final int diff, boolean apply);

		public void setFrame(float f);
		
		public String getTargetName();
		
		public IFlashKey findFlashKeyByFrame(int frame);
		
		public void shiftAdd(IFlashKey key);
		
		public IFlashKey createFlashKeyByFrame(int frame);
	}

	public static interface IFlashMain {
		public void setMaxF(int maxf);

		public int getMaxF();

		public void setFPS(int fps);

		public int getFPS();

		public void play();

		public void stop();
		
		public boolean isPlaying();

		public int getProgressTime();
		
		public int getCurrentFrame();

		public void setProgressTime(int mills);

		public void update(int dt);
		
		//data + selects
		public IFlashKeyArray [] getFlashKeyArrays();
		
		public IFlashKeyArray findFlashKeyArrayByTarget(ElfNode node);
		
		public void autoReducedOrAll(boolean reduced);
		
		public IFlashKey [] getAllFlashKeys();
		
		/***
		 * sorted
		 */
		public IFlashKey [] getAllSelectedFlashKeys();
		
		public void setAllFlashKeySelected(boolean selected);
		
		public void check();
		
		public String getAnimateName() ;
		
		public void setAnimateName(final String name) ;
		
		public void addAnimate(String name);
		
		public void copyCurrentAnimate(String name);
		
		public void deleteAnimate(String name);
		
		public String [] arrayAnimateName();
		
		public void setSpeedRate(float rate);
		
		public float getSpeedRate();
		
//		public void setAsElement(boolean b);
//		
//		public boolean getAsElement();
//		
//		public void setControl(final String name);
//		
//		public String getControl();
		
		//add element
		
		//is element 
		//controller 
	}
	
	public static void sortArray(final IFlashKey[] kfns, final boolean little) {
		Arrays.sort(kfns, new Comparator<IFlashKey>() {
			public int compare(IFlashKey arg0, IFlashKey arg1) {
				if (little) {
					return arg0.getFrame() - arg1.getFrame();
				} else {
					return arg1.getFrame() - arg0.getFrame();
				}
			}
		});
	}

	public static void sortList(final List<IFlashKey> kfns, final boolean little) {
		Collections.sort(kfns, new Comparator<IFlashKey>() {
			public int compare(IFlashKey arg0, IFlashKey arg1) {
				if (little) {
					return arg0.getFrame() - arg1.getFrame();
				} else {
					return arg1.getFrame() - arg0.getFrame();
				}
			}
		});
	}

}
