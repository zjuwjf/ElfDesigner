package com.ielfgame.stupidGame;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.Actions.Inter;
import com.ielfgame.stupidGame.Actions.Mode;
import com.ielfgame.stupidGame.Actions.Type;
import com.ielfgame.stupidGame.animation.Animate;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.modifier.AlphaModifier;
import elfEngine.basic.modifier.EmptyModifier;
import elfEngine.basic.modifier.INodeModifier;
import elfEngine.basic.modifier.ParallelModifier;
import elfEngine.basic.modifier.PathModifier;
import elfEngine.basic.modifier.RotateModifier;
import elfEngine.basic.modifier.ScaleModifier;
import elfEngine.basic.modifier.SequenceModifier;
import elfEngine.basic.node.particle.modifier.EmitterMoveModifier;

public class ActionData  implements ActionDataConfig{
	
	public final String [] mStrings = new String[ sTableLabels.length ];
	public final ArrayList<ActionData> mChildsList = new ArrayList<ActionData>();
	
	public ActionData mFather;
	
	public boolean mChecked;
	public TreeItem mItem;
	public final boolean mTopFlag;
	
	public ActionData(boolean flag){
		mTopFlag = flag;
		
//		public final static int INDEX_NAME = 0;
//		public final static int INDEX_TYPE = 1;
//		public final static int INDEX_START_VALUE = 2;
//		public final static int INDEX_END_VALUE = 3;
//		public final static int INDEX_MODE = 4;
//		public final static int INDEX_START_TIME = 5;
//		public final static int INDEX_END_TIME = 6;
//		public final static int INDEX_PERIOD = 7;
//		public final static int INDEX_INTERPOLOR = 8;
		
		mStrings[INDEX_NAME] = "newName";
		mStrings[INDEX_TYPE] = Type.Alpha.toString();
		mStrings[INDEX_START_VALUE] = "";
		mStrings[INDEX_END_VALUE] = "";
		mStrings[INDEX_MODE] = Mode.Stay.toString();
//		mStrings[INDEX_START_TIME] = "0.00";
		mStrings[INDEX_END_TIME] = "1.00";
		mStrings[INDEX_PERIOD] = "1.00";
		mStrings[INDEX_INTERPOLOR] = Inter.Linear.toString();
	}
	
	public boolean iteratorChild(IIterator iterator){
		for(ActionData data : mChildsList){
			if(iterator.iterator(data)){
				return true;
			}
		}
		return false;
	}
	
	public boolean iteratorChildDeep(IIterator iterator){
		for(ActionData data : mChildsList){
			if(iterator.iterator(data)){
				return true;
			} else {
				boolean ret = data.iteratorChildDeep(iterator);
				if(ret){
					return true;
				}
			}
		}
		return false;
	}
	
	interface IIterator{
		public boolean iterator(ActionData data);
	}

	public void removeFromFather() {
		if(mFather != null){
			mFather.mChildsList.remove(this);
		}
	}

	public boolean isRecurFatherOf(ActionData child) {
		while(child != null){
			if(child.mFather == this){
				return true;
			} else {
				child = child.mFather;
			}
		}
		return false;
	}

	public void addToFather(int index) {
		if(mFather != null){
			mFather.mChildsList.remove(this);
			index = Math.min(index, mFather.mChildsList.size());
			index = Math.max(index, 0);
			mFather.mChildsList.add(index, this);
		}
	}
	
	ArrayList<String> toStrings(){
		final ArrayList<String> ret = new ArrayList<String>();
		String text = "";
		for(int i=0; i<sTableLabels.length; i++){
			if(i == sTableLabels.length-1){
				text += mStrings[i];
			} else {
				text += mStrings[i] + "#";
			}
		}
		ret.add(text);
		
		for(ActionData child : mChildsList){
			final ArrayList<String> childTexts = child.toStrings();
			for(String str : childTexts){
				ret.add("\t" + str);
			}
		}
		
		return ret;
	}
	
	public void print(){
		final ArrayList<String> ret = toStrings();
		for(String text : ret){
			System.err.println(text);
		}
	}
	
	public INodeModifier createModifier(Point error){
		final String strType = mStrings[INDEX_TYPE];
		String strStartValue = mStrings[INDEX_START_VALUE];
		String strEndValue =   mStrings[INDEX_END_VALUE];
		final String strMode = mStrings[INDEX_MODE];
//		final String strStartTime = mStrings[INDEX_START_TIME];
		final String strEndTime = mStrings[INDEX_END_TIME];
		final String strPeriod = mStrings[INDEX_PERIOD];
		final String strInter = mStrings[INDEX_INTERPOLOR];
		
		strStartValue = pretreatment(strStartValue);
		strEndValue = pretreatment(strEndValue);
		
		final Type type = Type.fromString(strType);
		final Mode mode = Mode.fromString(strMode);
		final Inter inter = Inter.fromString(strInter);
		
//		float startTime = 0;
//		try{
//			startTime = Float.valueOf(strStartTime) * 1000;
//		} catch (Exception e) {
//			Redirect.errPrintln("error int read startTime");
//			e.printStackTrace();
//			error.x = ModifiersEditWorkSpaceTab.INDEX_START_TIME;
//			return null;
//		}
		
		float endTime = 0;
		try{
			endTime = Float.valueOf(strEndTime) * 1000;
		} catch (Exception e) {
			Redirect.errPrintln("error int read endTime");
			e.printStackTrace();
			error.x = ModifiersEditWorkSpaceTab.INDEX_END_TIME;
			return null;
		}
		
		float periodTime = 0;
		try{
			periodTime = Float.valueOf(strPeriod) * 1000;
		} catch (Exception e) {
			Redirect.errPrintln("error int read periodTime");
			e.printStackTrace();
			error.x = ModifiersEditWorkSpaceTab.INDEX_PERIOD;
			return null;
		}
		
		if(strStartValue == null){
			Redirect.errPrintln("error int read a null startValue");
			error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
			return null;
		}
		
		if(strEndValue == null){
			Redirect.errPrintln("error int read a null strEndValue");
			error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
			return null;
		}
		
		if(type == null){
			Redirect.errPrintln("error int read a null type");
			error.x = ModifiersEditWorkSpaceTab.INDEX_TYPE;
			return null;
		}
		if(mode == null){
			Redirect.errPrintln("error int read a null mode");
			error.x = ModifiersEditWorkSpaceTab.INDEX_MODE;
			return null;
		}
		if(inter == null){
			Redirect.errPrintln("error int read a null inter");
			error.x = ModifiersEditWorkSpaceTab.INDEX_INTERPOLOR;
			return null;
		}
		
		INodeModifier ret;
		String [] values;
		
//		System.err.println("Type:"+type.toString() + " startValue:"+strStartValue);
		
		switch(type){
		case Empty:
			return new EmptyModifier(0, 1, 1, endTime, mode.mLoopMode, inter.mInterpolator);
		case Parallel:
			final ArrayList<INodeModifier> modifiers2 = new ArrayList<INodeModifier>();
			for(ActionData data : mChildsList){
				if(data.mChecked){
					error.y += 1;
					final INodeModifier modifier = data.createModifier(error);
					if(modifier == null){
						return null;
					} else {
						modifiers2.add(modifier);
					}
				}
			}
			final INodeModifier [] ms2 = new INodeModifier[modifiers2.size()];
			modifiers2.toArray(ms2);
			return new ParallelModifier(periodTime, endTime, mode.mLoopMode, inter.mInterpolator, ms2);
			
		case Sequence:
			final ArrayList<INodeModifier> modifiers = new ArrayList<INodeModifier>();
			for(ActionData data : mChildsList){
				if(data.mChecked){
					error.y += 1;
					final INodeModifier modifier = data.createModifier(error);
					if(modifier == null){
						return null;
					} else {
						modifiers.add(modifier);
					}
				}
			}
			final INodeModifier [] ms = new INodeModifier[modifiers.size()];
			modifiers.toArray(ms);
			
			if(endTime == 0){
				return new SequenceModifier(ms);
			} else {
				return new SequenceModifier(endTime, ms);
			}
			
		case Alpha:
			float startAlpha = 0;
			try{
				startAlpha = Float.valueOf(strStartValue); 
			} catch (Exception e) { 
				Redirect.errPrintln("error int read startValue :" + strStartValue);
				e.printStackTrace(); 
				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
				return null;
			}
			float endAlpha = 0;
			try{
				endAlpha = Float.valueOf(strEndValue);
			} catch (Exception e) {
				Redirect.errPrintln("error int read endValue");
				e.printStackTrace();
				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
				return null;
			}
			
			ret = new AlphaModifier(startAlpha, endAlpha, periodTime, (endTime), mode.mLoopMode, inter.mInterpolator);
			ret.setRemovable(true);
			return ret;
		case Animate:
			final Animate animate = PowerMan.getSingleton(DataModel.class).getAnimate(strStartValue);
			if(animate == null){
				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
				return null;
			}
			
			ret = new AnimateModifier(animate, (endTime), mode.mLoopMode, inter.mInterpolator);
			ret.setRemovable(true);
			return ret;
		case Move:
		case EmitterMove:
			float startPosX = 0, startPosY = 0;
			values = strStartValue.split(sSpilt);
			if( values==null || values.length != 2){
				if(values!=null){
					Redirect.errPrintln("--");
					for(int i=0; i<values.length; i++){
						System.out.print(values[i]+"#");
					}
					Redirect.errPrintln("--"+strStartValue);
				}
				Redirect.errPrintln("error int read start values");
				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
				return null;
			}
			try{
				startPosX = Float.valueOf(values[0]);
				startPosY = Float.valueOf(values[1]);
			} catch (Exception e) {
				Redirect.errPrintln("error int read start values");
				e.printStackTrace();
				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
				return null;
			}
			
			values = strEndValue.split(sSpilt);
			if( values==null || (values.length % 2 == 1) || values.length < 2){
				Redirect.errPrintln("error int read end values");
				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
				return null;
			}
			final ElfPointf [] ps = new ElfPointf[values.length/2 + 1];
			ps[0] = new ElfPointf(startPosX, startPosY);
			for(int i=1; i<ps.length; i++){
				final ElfPointf p = new ElfPointf();
				try{
					int index = (i - 1)*2;
					p.x = Float.valueOf(values[index]);
					p.y = Float.valueOf(values[index+1]);
					ps[i] = p;
				} catch (Exception e) {
					Redirect.errPrintln("error int read end values");
					e.printStackTrace();
					error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
					return null;
				}
			}
			
			if(type == Type.Move){
				ret = new PathModifier(periodTime, (endTime), mode.mLoopMode, inter.mInterpolator, ps);
				ret.setRemovable(true);
				return ret;
			} else if(type == Type.EmitterMove){
				ret = new EmitterMoveModifier(periodTime, (endTime), mode.mLoopMode, inter.mInterpolator, ps);
				ret.setRemovable(true);
				return ret;
			} 
		case Rotate:
			float startRotate = 0;
			try{
				startRotate = Float.valueOf(strStartValue);
			} catch (Exception e) {
				Redirect.errPrintln("error int read startValue");
				e.printStackTrace();
				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
				return null;
			}
			float endRotate = 0;
			try{
				endRotate = Float.valueOf(strEndValue);
			} catch (Exception e) {
				Redirect.errPrintln("error int read endValue");
				e.printStackTrace();
				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
				return null;
			}
			
			ret = new RotateModifier(startRotate, endRotate, periodTime, (endTime), mode.mLoopMode, inter.mInterpolator);
			ret.setRemovable(true);
			return ret;
		case Scale:
			float startScaleX = 1, startScaleY = 1;
			values = strStartValue.split(sSpilt);
			if( values==null || values.length != 2){
				Redirect.errPrintln("error int read start values");
				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
				return null;
			}
			try{
				startScaleX = Float.valueOf(values[0]);
				startScaleY = Float.valueOf(values[1]);
			} catch (Exception e) {
				Redirect.errPrintln("error int read start values");
				e.printStackTrace();
				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
				return null;
			}
			
			float endScaleX = 1, endScaleY = 1;
			values = strEndValue.split(sSpilt);
			if( values==null || values.length != 2){
				Redirect.errPrintln("error int read end values");
				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
				return null;
			}
			try{
				endScaleX = Float.valueOf(values[0]);
				endScaleY = Float.valueOf(values[1]);
			} catch (Exception e) {
				Redirect.errPrintln("error int read end values");
				e.printStackTrace();
				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
				return null;
			}
			
			ret = new ScaleModifier(startScaleX, endScaleX, startScaleY, endScaleY, periodTime, (endTime), mode.mLoopMode, inter.mInterpolator);
			ret.setRemovable(true);
			return ret;
		}
		
		return null;
	}
	
	private static final String sSpilt = ",";
	private static final String pretreatment(String text){
		if(text!=null){
			StringTokenizer strT1 = new StringTokenizer(text," ,()");
			text = "";
			while(strT1.hasMoreElements()){
				text += strT1.nextToken()+sSpilt;
			}
		}
		if(text.length() > 0){
			text = text.substring(0, text.length()-1);
		}
		Redirect.outPrintln("new text:"+text);
		return text;
	}
//	public String toString(){
//		
//	}
}