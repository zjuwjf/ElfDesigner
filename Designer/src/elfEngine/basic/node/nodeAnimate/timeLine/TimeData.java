package elfEngine.basic.node.nodeAnimate.timeLine;

import java.util.ArrayList;

import com.ielfgame.stupidGame.power.PowerMan;


public class TimeData {
	
	public String toString() {
		return "time="+this.time+"; type="+this.type;
	}
	//time
	public final static int MIN_UNIT = 50;//ms 
	
	protected final static int MAX_DIVISION_PIXELS = 25; 
	protected final static int MIN_DIVISION_PIXELS = 15; 
	
	protected final static int [] STEPS = {250, 500, 1000, 5000, 10000, 20000, 50000}; 
	protected final static int [] DIVISIONS = {5, 5, 5, 5, 5, 5, 5}; 
	//scale
	protected final static float MAX_TIME_PER_PIXEL = (STEPS[STEPS.length-1]/DIVISIONS[STEPS.length-1])/(float)MIN_DIVISION_PIXELS;//100ms -> x pixels
	protected final static float MIN_TIME_PER_PIXEL = (STEPS[0]/DIVISIONS[0])/(float)MAX_DIVISION_PIXELS;
	
	//MillisecondsPerPixels
	private static float sMillisecondsPerPixels = MIN_TIME_PER_PIXEL; 
	private static int sCurrStepTime = STEPS[0]; 
	private static int sCurrDivisions = DIVISIONS[0]; 
	
	public static void scroll(final int count) {
		float r = sMillisecondsPerPixels;
		for(int i=0; i<count; i++) { 
			r *= 0.9f;
		}
		for(int i=count; i<0; i++) { 
			r *= 1.1f;
		} 
		setMillisecondsPerPixels(r); 
	}
	
	public static int getCurrStepTime() { 
		return sCurrStepTime;
	} 
	public static int getCurrDivisions() { 
		return sCurrDivisions; 
	} 
	public static float getCurrStepPixels() { 
		return sCurrStepTime/(sMillisecondsPerPixels); 
	} 
	
	private static void setMillisecondsPerPixels(float mpp) { 
		if(mpp <= MIN_TIME_PER_PIXEL) { 
			sMillisecondsPerPixels = MIN_TIME_PER_PIXEL;
			sCurrStepTime = STEPS[0];
			sCurrDivisions = DIVISIONS[0]; 
		} else if(mpp >= MAX_TIME_PER_PIXEL) {
			sMillisecondsPerPixels = MAX_TIME_PER_PIXEL;
			sCurrStepTime = STEPS[STEPS.length-1];
			sCurrDivisions = DIVISIONS[STEPS.length-1];
		} else { 
			sMillisecondsPerPixels = mpp; 
			for(int i=0; i<STEPS.length; i++) { 
				final int divisionTime = STEPS[i]/DIVISIONS[i]; 
				final float divisionPixels = divisionTime/sMillisecondsPerPixels;
				if(divisionPixels >= MIN_DIVISION_PIXELS) { 
					sCurrStepTime = STEPS[i]; 
					sCurrDivisions = DIVISIONS[i]; 
					break;
				} 
			} 
		} 
	} 
	
	public static float MillisecondsPerPixels() {
		return sMillisecondsPerPixels;
	}
	
	private static TimeData sSelectedTimeData;
	public static void setSelectTimeData(TimeData data) {
		if(sSelectedTimeData != null) {
			sSelectedTimeData.setSelect(false);
			sSelectedTimeData = null;
		}
		if(data != null) { 
			data.setSelect(true); 
			sSelectedTimeData = data; 
		}
	}
	public static TimeData getSelectTimeData() {
		return sSelectedTimeData;
	}
	
//	public static TimeData[] getSelectTimeDataArray {
//		
//	}
	
//	private static final TimeData sCurrentTimeData = new TimeData(0);
	
	//not static
	//1. ElfMotionNode
	//2. ElfMotionKeyNode
	
	private ElfMotionNode mElfMotionNode;
	private ElfMotionKeyNode mElfMotionKeyNode;
	private int time; 
	private boolean select;
	private NodePropertyType type;
	private boolean mIsGlobalData;
	
	public TimeData(int time) { 
		setTime( time ); 
	} 
	
	public void setIsGlobalData(boolean is) {
		mIsGlobalData = is;
	}
	
	public boolean getIsGlobalData() {
		return mIsGlobalData;
	}
	
	public TimeData(TimeData data) {
		this(data.getTime());
		
		this.mElfMotionKeyNode = data.mElfMotionKeyNode;
		this.mElfMotionNode = data.mElfMotionNode;
		this.type = data.type;
	}
	
	public void bind(ElfMotionNode node) {
		mElfMotionKeyNode = null;
		mElfMotionNode = node;
	}
	public void bind(ElfMotionKeyNode node) { 
		mElfMotionKeyNode = node;
		mElfMotionNode = null;
	} 
	
	public void refreshTime() {
		if(mElfMotionNode != null) {
			this.time = mElfMotionNode.getProgress(); 
		} else if(mElfMotionKeyNode != null) { 
			this.time = mElfMotionKeyNode.getTime(); 
		} 
	} 
	
//	public void updateTimeToNode() { 
//		if(mElfMotionNode != null) { 
//			mElfMotionNode.setProgress(this.time); 
//		} else if(mElfMotionKeyNode != null) { 
//			mElfMotionKeyNode.setTime(this.time); 
//		} 
//	}
	
	private final static ArrayList<TimeData> mTimeDataArray = new ArrayList<TimeData>();
	public final static TimeData getGlobalTimeDataByTime(int time) {
		final TimeData newdata = new TimeData(time);
		
		for(TimeData data : mTimeDataArray) {
			if(data.getTime() == newdata.getTime()) {
				return data;
			}
		}
		
		newdata.setIsGlobalData(true);
		mTimeDataArray.add(newdata);
		return newdata;
	}
	
	public static final void cleanGlobal() {
		mTimeDataArray.clear();
	}
	
	public void setTime(final int time2) { 
		final int settime = Math.max(0, Math.round((float)time2/(float)MIN_UNIT) *MIN_UNIT ); 
		if(settime == this.time) {
			return;
		}
		
		final int oldTime = this.time;
		this.time = settime; 
		
		if(getIsGlobalData()) {
			for(final TimeData data2 : mTimeDataArray) {
				if(data2.getTime() == this.time && data2 != this) {
					mTimeDataArray.remove(data2);
					break;
				}
			}
			
			//
			ArrayList<ElfMotionKeyNode> keys = PowerMan.getSingleton(TimeCanvasView.class).getSelectedKeyNodesByTime(oldTime);
			for(ElfMotionKeyNode key : keys) {
				key.setTime(this.time);
			} 
			
		} else if(mElfMotionNode != null) { 
			mElfMotionNode.setProgress(this.time); 
		} else if(mElfMotionKeyNode != null) { 
			mElfMotionKeyNode.setTime(this.time); 
		} 
	} 
	public int getTime() { 
		return time; 
	} 
	
	public static int getTimeByXAndMargin(int x, int margin) {
		final int time = Math.round( (x-margin)*sMillisecondsPerPixels );
		return Math.max(0, Math.round((float)time/MIN_UNIT) * MIN_UNIT ); 
	}
	
	public int getPointX(final int margin) { 
		return Math.round( time/sMillisecondsPerPixels ) + margin; 
	} 
	
	public void setPointX(int x, int margin) { 
		setTime( Math.round( (x-margin)*sMillisecondsPerPixels ) ); 
	} 
	
//	public void setTimeDirectly(final int time) {
//		this.time = Math.max(0, Math.round((float)time/MIN_UNIT) * MIN_UNIT ); 
//	}
	
	private void setSelect(boolean select) {
		this.select = select;
	}
	public boolean getSelect() {
		return this.select;
	}
	
	public void recycleNode() {
		if(mElfMotionKeyNode != null) {
			mElfMotionKeyNode.removeFromParentView(true);
		}
		
		if(this == sSelectedTimeData) { 
			setSelectTimeData(null);
		} 
	} 
	
	public String toTimeString() {
		final String text; 
		if(time % 1000 == 0) {
			text = String.format("%d", time/1000);
		} else { 
			text = String.format("%.2f", time/1000f);
		} 
		return text;
	}
	public void setType(NodePropertyType type) {
		this.type = type;
	}
	public NodePropertyType getType() {
		return this.type;
	}
	
	public ElfMotionKeyNode getBindKeyNode() {
		return mElfMotionKeyNode;
	}
	
	public ElfMotionNode getBindMotionNode() { 
		return mElfMotionNode;
	} 
}
