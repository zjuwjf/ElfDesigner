package com.ielfgame.stupidGame;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.counter.Interpolator;
import elfEngine.basic.counter.LoopMode;

public class Actions {
	
	
	public enum Type{
		Rotate,
		Scale,
		Move,
		Alpha,
		Animate,
		Sequence,
		Empty,
		Parallel,
		EmitterMove
		;
		
		public static Type fromString(String text){
			Type [] types = Type.values();
			for(Type type:types){
				if(type.toString().equalsIgnoreCase(text)){
					return type;
				}
			}
			return null;
		}
		
		public static String [] toStrings(){
			Type [] types = Type.values();
			final String [] ret = new String[types.length];
			for(int i=0; i<ret.length; i++){
				ret[i] = types[i].toString();
			}
			return ret;
		}
	}
	
	public enum Mode{
		Loop(LoopMode.LOOP),
		Reloop(LoopMode.RELOOP),
		Stay(LoopMode.STAY),
		Endless(LoopMode.ENDLESS)
		;
		final LoopMode mLoopMode;
		Mode(LoopMode loopMode){
			mLoopMode = loopMode;
		}
		
		public static Mode fromString(String text){
			Mode [] types = Mode.values();
			for(Mode type:types){
				if(type.toString().equalsIgnoreCase(text)){
					return type;
				}
			}
			return null;
		}
		
		public static String [] toStrings(){
			Mode [] types = Mode.values();
			final String [] ret = new String[types.length];
			for(int i=0; i<ret.length; i++){
				ret[i] = types[i].toString();
			}
			return ret;
		}
	}
	
	public enum Inter{
		Linear(InterType.Linear),
		AcceDece(InterType.AcceDece),
		Anticipate(InterType.Anticipate),
		AnticipateOvershoot(InterType.AnticipateOvershoot),
		Bounce(InterType.Bounce),
		Cycle(InterType.Cycle),
		Dece(InterType.Dece),
		Overshoot(InterType.Overshoot),
		Viscous(InterType.Viscous)
		;
		final Interpolator mInterpolator;
		Inter(Interpolator interpolator){
			mInterpolator = interpolator;
		}
		
		public static Inter fromString(String text){
			Inter [] types = Inter.values();
			for(Inter type:types){
				if(type.toString().equalsIgnoreCase(text)){
					return type;
				}
			}
			return null;
		}
		
		public static String [] toStrings(){
			Inter [] types = Inter.values();
			final String [] ret = new String[types.length];
			for(int i=0; i<ret.length; i++){
				ret[i] = types[i].toString();
			}
			return ret;
		}
	}
	
//	public static INodeModifier createModifier(
//			final String strType,
//			String strStartValue,
//			String strEndValue,
//			final String strMode,
//			final String strStartTime,
//			final String strEndTime,
//			final String strPeriod,
//			final String strInter,
//			Point error
//			){
//		strStartValue = pretreatment(strStartValue);
//		strEndValue = pretreatment(strEndValue);
//		
//		final Type type = Type.fromString(strType);
//		final Mode mode = Mode.fromString(strMode);
//		final Inter inter = Inter.fromString(strInter);
//		
//		float startTime = 0;
//		try{
//			startTime = Float.valueOf(strStartTime) * 1000;
//		} catch (Exception e) {
//			Redirect.errPrintln("error int read startTime");
//			e.printStackTrace();
//			error.x = ModifiersEditWorkSpaceTab.INDEX_START_TIME;
//			return null;
//		}
//		
//		float endTime = 0;
//		try{
//			endTime = Float.valueOf(strEndTime) * 1000;
//		} catch (Exception e) {
//			Redirect.errPrintln("error int read endTime");
//			e.printStackTrace();
//			error.x = ModifiersEditWorkSpaceTab.INDEX_END_TIME;
//			return null;
//		}
//		
//		float periodTime = 0;
//		try{
//			periodTime = Float.valueOf(strPeriod) * 1000;
//		} catch (Exception e) {
//			Redirect.errPrintln("error int read periodTime");
//			e.printStackTrace();
//			error.x = ModifiersEditWorkSpaceTab.INDEX_PERIOD;
//			return null;
//		}
//		
//		if(strStartValue == null){
//			Redirect.errPrintln("error int read a null startValue");
//			error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
//			return null;
//		}
//		
//	
//		if(strEndValue == null){
//			Redirect.errPrintln("error int read a null strEndValue");
//			error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
//			return null;
//		}
//		
//		if(type == null){
//			Redirect.errPrintln("error int read a null type");
//			error.x = ModifiersEditWorkSpaceTab.INDEX_TYPE;
//			return null;
//		}
//		if(mode == null){
//			Redirect.errPrintln("error int read a null mode");
//			error.x = ModifiersEditWorkSpaceTab.INDEX_MODE;
//			return null;
//		}
//		if(inter == null){
//			Redirect.errPrintln("error int read a null inter");
//			error.x = ModifiersEditWorkSpaceTab.INDEX_INTERPOLOR;
//			return null;
//		}
//		
//		INodeModifier ret;
//		String [] values;
//		
//		switch(type){
//		case Alpha:
//			float startAlpha = 0;
//			try{
//				startAlpha = Float.valueOf(strStartValue);
//			} catch (Exception e) {
//				Redirect.errPrintln("error int read startValue");
//				e.printStackTrace();
//				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
//				return null;
//			}
//			float endAlpha = 0;
//			try{
//				endAlpha = Float.valueOf(strEndValue);
//			} catch (Exception e) {
//				Redirect.errPrintln("error int read endValue");
//				e.printStackTrace();
//				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
//				return null;
//			}
//			
//			ret = new SequenceModifier(
//					new EmptyModifier(0, 1, 1, startTime+1, null, null),
//					new AlphaModifier(startAlpha, endAlpha, periodTime, (endTime-startTime), mode.mLoopMode, inter.mInterpolator));
//			ret.setRemovable(true);
//			return ret;
//		case Animate:
//			final Animate animate = PowerMan.getSingleton(DataModel.class).getAnimate(strStartValue);
//			if(animate == null){
//				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
//				return null;
//			}
//			
//			ret = new SequenceModifier(
//					new EmptyModifier(0, 1, 1, startTime+1, null, null),
//					new AnimateModifier(animate, (endTime-startTime), mode.mLoopMode, inter.mInterpolator));
//			ret.setRemovable(true);
//			return ret;
//		case Move:
//			float startPosX = 0, startPosY = 0;
//			values = strStartValue.split(sSpilt);
//			if( values==null || values.length != 2){
//				if(values!=null){
//					Redirect.errPrintln("--");
//					for(int i=0; i<values.length; i++){
//						System.out.print(values[i]+"#");
//					}
//					Redirect.errPrintln("--"+strStartValue);
//				}
//				Redirect.errPrintln("error int read start values");
//				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
//				return null;
//			}
//			try{
//				startPosX = Float.valueOf(values[0]);
//				startPosY = Float.valueOf(values[1]);
//			} catch (Exception e) {
//				Redirect.errPrintln("error int read start values");
//				e.printStackTrace();
//				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
//				return null;
//			}
//			
//			values = strEndValue.split(sSpilt);
//			if( values==null || (values.length % 2 == 1) || values.length < 2){
//				Redirect.errPrintln("error int read end values");
//				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
//				return null;
//			}
//			final PointF [] ps = new PointF[values.length/2 + 1];
//			ps[0] = new PointF(startPosX, startPosY);
//			for(int i=1; i<ps.length; i++){
//				final PointF p = new PointF();
//				try{
//					int index = (i - 1)*2;
//					p.x = Float.valueOf(values[index]);
//					p.y = Float.valueOf(values[index+1]);
//					ps[i] = p;
//				} catch (Exception e) {
//					Redirect.errPrintln("error int read end values");
//					e.printStackTrace();
//					error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
//					return null;
//				}
//			}
//			
//			ret = new SequenceModifier(
//					new EmptyModifier(0, 1, 1, startTime+1, null, null),
//					new PathModifier(periodTime, (endTime-startTime), mode.mLoopMode, inter.mInterpolator, ps));
//			ret.setRemovable(true);
//			return ret;
//		case Rotate:
//			float startRotate = 0;
//			try{
//				startRotate = Float.valueOf(strStartValue);
//			} catch (Exception e) {
//				Redirect.errPrintln("error int read startValue");
//				e.printStackTrace();
//				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
//				return null;
//			}
//			float endRotate = 0;
//			try{
//				endRotate = Float.valueOf(strEndValue);
//			} catch (Exception e) {
//				Redirect.errPrintln("error int read endValue");
//				e.printStackTrace();
//				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
//				return null;
//			}
//			
//			ret = new SequenceModifier(
//					new EmptyModifier(0, 1, 1, startTime+1, null, null),
//					new RotateModifier(startRotate, endRotate, periodTime, (endTime-startTime), mode.mLoopMode, inter.mInterpolator));
//			ret.setRemovable(true);
//			return ret;
//		case Scale:
//			float startScaleX = 1, startScaleY = 1;
//			values = strStartValue.split(sSpilt);
//			if( values==null || values.length != 2){
//				Redirect.errPrintln("error int read start values");
//				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
//				return null;
//			}
//			try{
//				startScaleX = Float.valueOf(values[0]);
//				startScaleY = Float.valueOf(values[1]);
//			} catch (Exception e) {
//				Redirect.errPrintln("error int read start values");
//				e.printStackTrace();
//				error.x = ModifiersEditWorkSpaceTab.INDEX_START_VALUE;
//				return null;
//			}
//			
//			float endScaleX = 1, endScaleY = 1;
//			values = strEndValue.split(sSpilt);
//			if( values==null || values.length != 2){
//				Redirect.errPrintln("error int read end values");
//				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
//				return null;
//			}
//			try{
//				endScaleX = Float.valueOf(values[0]);
//				endScaleY = Float.valueOf(values[1]);
//			} catch (Exception e) {
//				Redirect.errPrintln("error int read end values");
//				e.printStackTrace();
//				error.x = ModifiersEditWorkSpaceTab.INDEX_END_VALUE;
//				return null;
//			}
//			
//			ret = new SequenceModifier(
//					new EmptyModifier(0, 1, 1, startTime+1, null, null),
//					new ScaleModifier(startScaleX, endScaleX, startScaleY, endScaleY, periodTime, (endTime-startTime), mode.mLoopMode, inter.mInterpolator));
//			ret.setRemovable(true);
//			return ret;
//		}
//		
//		return null;
//	}
//	
//	private static final String sSpilt = ",";
//	private static final String pretreatment(String text){
//		if(text!=null){
//			StringTokenizer strT1 = new StringTokenizer(text," ,()");
//			text = "";
//			while(strT1.hasMoreElements()){
//				text += strT1.nextToken()+sSpilt;
//			}
//		}
//		if(text.length() > 0){
//			text = text.substring(0, text.length()-1);
//		}
//		Redirect.outPrintln("new text:"+text);
//		return text;
//	}

}
