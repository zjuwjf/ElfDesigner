package elfEngine.basic.node.nodeAnimate.flash;


public class FlashConfig {
	
	public static class TimeRulerConfig {
		private int timeStart=0;
		private float timeScale=0;
		
		public int[] getStepAndPixel() {
			final int [] frameArray = new int[]{05,05,10,10,20,20,30,30,40,40,50,50,100,100,100};
			final int [] pixelArray = new int[]{10,07,10,07,10,07,10,07,10,07,10,07,010,007,007};
			final int min = Math.min(frameArray.length, pixelArray.length);
			int index = Math.max(0, Math.round(timeScale * min));
			index = Math.min(index, min-1);
			return new int[]{frameArray[index], pixelArray[index]};
		}

		public int getTimeStart() {
			return timeStart;
		}

		public void setTimeStart(int timeStart) {
			this.timeStart = timeStart;
		}

		public float getTimeScale() {
			return timeScale;
		}

		public void setTimeScale(float timeScale) {
			this.timeScale = timeScale;
		}

		public String toString() {
			return "timeStart:"+timeStart+"\ntimeScale:"+timeScale;
		}
	}
	
	public static class OperationConfig {
		public boolean auto_key;
	}
	
	public static class ViewConfig {
		public boolean show_intertype;
	}
	
	public final static TimeRulerConfig timeRulerConfig = new TimeRulerConfig();
	public final static OperationConfig operationConfig = new OperationConfig();
	public final static ViewConfig viewConfig = new ViewConfig();
}
