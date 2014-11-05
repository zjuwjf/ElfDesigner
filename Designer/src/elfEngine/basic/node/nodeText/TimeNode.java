package elfEngine.basic.node.nodeText;

import java.util.Date;
import java.util.LinkedList;

import com.ielfgame.stupidGame.enumTypes.TimeFormat;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.richLabel.LabelNode;

public class TimeNode extends LabelNode {
	
	public TimeNode(ElfNode father, int ordinal) { 
		super(father, ordinal);
		
		setName("#time");
	} 
	
	final ElfDate mElfDate = new ElfDate();
	public ElfDate getElfDate() { 
		return mElfDate; 
	} 
	
	public void setTimeUpdateRate(float rate) {
		mElfDate.setUpdateRate(rate);
	}
	public float getTimeUpdateRate() {
		return mElfDate.getUpdateRate();
	}
	//reflect
    protected static final int REF_TimeUpdateRate = FACE_ALL_SHIFT + XML_ALL_SHIFT;
	
	public void setYearMonthDay(int year, int month, int day) {
		mElfDate.setYearMonthDay(year, month, day);
	} 
	public void setYearMonthDay(final Integer [] ymd) {
		if(ymd != null && ymd.length > 0) {
			if(ymd.length == 1) {
				mElfDate.setYearMonthDay(ymd[0], mElfDate.mMonth, mElfDate.mDay);
			} else if(ymd.length == 2) {
				mElfDate.setYearMonthDay(ymd[0], ymd[1], mElfDate.mDay);
			} else {
				mElfDate.setYearMonthDay(ymd[0], ymd[1], ymd[2]);
			} 
		} 
	} 
	
	public Integer [] getYearMonthDay() {
		return new Integer [] {mElfDate.mYear, mElfDate.mMonth, mElfDate.mDay};
	} 
	//reflect
    protected static final int REF_YearMonthDay =  FACE_ALL_SHIFT + XML_ALL_SHIFT + PASTE_SHIFT + COPY_SHIFT;
	
	public void setHourMinuteSecond(Integer [] hms) {
		if(hms != null && hms.length > 0) {
			if(hms.length == 1) { 
				mElfDate.setHourMinuteSecond(hms[0], mElfDate.mMinute, mElfDate.mSecond);
			} else if(hms.length == 2) {
				mElfDate.setHourMinuteSecond(hms[0], hms[1], mElfDate.mSecond);
			} else {
				mElfDate.setHourMinuteSecond(hms[0], hms[1], hms[2]); 
			} 
		} 
	} 
	public Integer [] getHourMinuteSecond() { 
		return new Integer [] {mElfDate.mHour, mElfDate.mMinute, mElfDate.mSecond };
	} 
	//reflect
    protected static final int REF_HourMinuteSecond = FACE_ALL_SHIFT + XML_ALL_SHIFT + PASTE_SHIFT + COPY_SHIFT;
	
	public void setAsSystemTime() { 
		mElfDate.setAsSystemTime();
	} 
	//reflect
    protected static final int REF_AsSystemTime = FACE_SET_SHIFT;
	
	public void setTimeFormat(TimeFormat timeFormat) { 
		mElfDate.setTimeFormat(timeFormat);
	} 
	public TimeFormat getTimeFormat() { 
		return mElfDate.getTimeFormat();
	} 
	//reflect
    protected static final int REF_TimeFormat = FACE_ALL_SHIFT | XML_ALL_SHIFT;
	
	final LinkedList<TimerListener> mTimerListeners = new LinkedList<TimeNode.TimerListener>();
	public static abstract class TimerListener extends ElfDate{ 
		public abstract void onTime();  
	} 
	public void addTimeListener(TimerListener listener, int hour, int minute, int second) { 
		mTimerListeners.add(listener); 
		listener.setHourMinuteSecond(hour, minute, second); 
	} 
	public void addTimeListener(TimerListener listener, int year, int month, int day, int hour, int minute, int second) {
		mTimerListeners.add(listener);
		listener.setYearMonthDay(year, month, day);
		listener.setHourMinuteSecond(hour, minute, second);
	} 
	public void removeTimeListener(TimerListener listener) {
		mTimerListeners.remove(listener);
	} 
	public void clearTimeListeners() { 
		mTimerListeners.clear();
	} 
	
	public void calc(final float pMsElapsed) { 
		super.calc(pMsElapsed);
		mElfDate.updateTime(pMsElapsed);
		
		final LinkedList<TimerListener> toRemoves = new LinkedList<TimeNode.TimerListener>();
		for(TimerListener listener : mTimerListeners) {
			if(mElfDate.mUpdateRate > 0) { 
				if(listener.isAfter(mElfDate)) {
					listener.onTime(); 
					toRemoves.add(listener);
				} 
			} else if(mElfDate.mUpdateRate < 0){ 
				if(listener.isBefore(mElfDate)) { 
					listener.onTime();
					toRemoves.add(listener);
				} 
			} 
		} 
		mTimerListeners.removeAll(toRemoves);
		
		setText(mElfDate.toString());
	} 
	
	public static class ElfDate {
		TimeFormat mTimeFormat = TimeFormat.HourMinuteSecond;

		int mYear, mMonth, mDay, mHour, mMinute, mSecond;
		float mMillisecond;
		
		float mUpdateRate = 1.0f;
		
		public ElfDate() {
			
			mYear = 2012;
			mMonth = mDay = 1;
			mHour = mMinute = mSecond = 0;
			mMillisecond = 0;
			
			mUpdateRate = 1.0f; 
		} 
		
		public void setUpdateRate(float updateRate) {
			mUpdateRate = updateRate;
		} 
		
		public float getUpdateRate() {
			return mUpdateRate;
		} 
		
		public void setTimeFormat(TimeFormat timeFormat) {
			if(timeFormat != null) { 
				mTimeFormat = timeFormat;
			} 
		} 
		
		public TimeFormat getTimeFormat() {
			return mTimeFormat;
		}
		
		public void copy(ElfDate elfDate) {
			this.mYear = elfDate.mYear;
			this.mMonth = elfDate.mMonth;
			this.mDay = elfDate.mDay;
			this.mHour = elfDate.mHour;
			this.mMinute = elfDate.mMinute;
			this.mSecond = elfDate.mSecond;
			this.mMillisecond = elfDate.mMillisecond;
			this.mTimeFormat = elfDate.mTimeFormat;
			this.mUpdateRate = elfDate.mUpdateRate;
		}
		
		public boolean isSameTime(ElfDate other) {
			return mMillisecond==other.mMillisecond && 
					mSecond==other.mSecond && 
					mMinute==other.mMinute && 
					mHour==other.mHour && 
					mDay==other.mDay && 
					mMonth==other.mMonth && 
					mYear==other.mYear;
		} 
		
		//big
		public boolean isAfter(ElfDate other) {
			return mYear>other.mYear||(mYear==other.mYear&&(mMonth>other.mMonth||(mMonth==other.mMonth&&
					(mDay>other.mDay||(mDay==other.mDay&&(mHour>other.mHour||(mHour==other.mHour&&(mMinute>other.mMinute||(mMinute==other.mMinute&&
					(mSecond>other.mSecond||(mSecond==other.mSecond&&mMillisecond>other.mMillisecond)))))))))));
		} 
		
		//small
		public boolean isBefore(ElfDate other) {
			return (!isSameTime(other)) && (!isAfter(other));
		} 
		
		
		public void updateTime(final float pMsElapsed) { 
			mMillisecond += pMsElapsed*mUpdateRate;
			checkTime();
		} 
		
		@SuppressWarnings("deprecation")
		public void setAsSystemTime() {
			final Date date = new Date();
			setYearMonthDay(date.getYear(), date.getMonth(), date.getDate());
			setHourMinuteSecond(date.getHours(), date.getMinutes(), date.getSeconds());
			mMillisecond = (int) (date.getTime() % 1000);
		}
		
		public void setYearMonthDay(int year, int month, int day) {
			this.mYear = year;
			this.mMonth = month;
			this.mDay = day;
			
			checkTime();
		} 
		
		public void setHourMinuteSecond(int hour, int minute, int second) {
			this.mHour = hour;
			this.mMinute = minute;
			this.mSecond = second;
			
			checkTime();
		} 
		
		public String toString() {
			switch(mTimeFormat) {
			case YearMonthDay:
				return ""+mYear+"/"+mMonth+"/"+mDay;
			case DayMonthYear:
				return ""+mDay+"/"+mMonth+"/"+mYear; 
			case HourMinute:
				return ""+getString(mHour, 2)+":"+getString(mMinute, 2);
			case HourMinuteSecond:
				return ""+getString(mHour, 2)+":"+getString(mMinute, 2)+":"+getString(mSecond, 2);
			case MinuteSecond:
				return getString(mMinute, 2)+":"+getString(mSecond, 2);
			case MinuteSecondTenth:
				return getString(mMinute, 2)+":"+getString(mSecond, 2)+":"+getString((int)(mMillisecond/100), 1);
			case SecondTenth:
				return getString(mSecond, 2)+":"+getString((int)(mMillisecond/100), 1);
			case Hour99MinuteSecond:
				return ""+getString(mHour+mDay*24, 2)+":"+getString(mMinute, 2)+":"+getString(mSecond, 2);
			case Hour99Minute:
				return ""+getString(mHour+mDay*24, 2)+":"+getString(mMinute, 2);
			case SecondTenth2:
				return  getString(mSecond, 2)+":"+getString((int)(mMillisecond/10), 2);
			} 
			
			return "Error TimeFormat";
		}
		
		public void checkTime() {
			while (mMillisecond >= 1000) {
				mMillisecond -= 1000;
				mSecond++;
			} 
			
			while (mMillisecond < 0) {
				mMillisecond += 1000;
				mSecond--;
			} 
			
			while (mSecond >= 60) {
				mSecond -= 60;
				mMinute++;
			} 
			
			while (mSecond < 0) {
				mSecond += 60;
				mMinute--;
			}

			while (mMinute >= 60) {
				mMinute -= 60;
				mHour++;
			}
			
			while (mMinute < 0) {
				mMinute += 60;
				mHour--;
			}

			while (mHour >= 24) {
				mHour -= 24;
				mDay++;
			}
			
			while (mHour < 0) {
				mHour += 24;
				mDay--;
			}

			// year month day

			while (true) {
				final int max = maxDays(mYear, mMonth);
				if(max < mDay) {
					mDay -= max;
					mMonth++;
					while(mMonth > 12) { 
						mMonth -= 12;
						mYear++;
					} 
				} else {
					break;
				} 
			} 
			
			while (true) {
				if(mDay <= 0) {
					mMonth--;
					while(mMonth <= 0) { 
						mMonth += 12; 
						mYear--; 
					} 
					mDay += maxDays(mYear, mMonth); 
				} else { 
					break;
				} 
			} 
		}

		static int maxDays(final int year, final int month) {
			// 1,3,5,7,8,10,12 ��31��
			// 4,6,9,11 ��30
			// 2 ƽ��ֻ��28��,������29��
			if (month == 2) {
				if (year % 4 == 0) {
					return 29;
				} else {
					return 28;
				}
			} else if (month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
				return 31; 
			} else { 
				return 30;
			} 
		}

		static String getString(int value, int nums) { 
			String ret = "" + value; 
			while(ret.length() < nums) { 
				ret = "0"+ret;
			} 
			while(ret.length() > nums) { 
				ret = ret.substring(ret.length() - nums, ret.length());
			} 
			return ret;
		} 
	}
} 
