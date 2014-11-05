package elfEngine.basic.touch;

import elfEngine.graphics.IShape;

public class ElfEventListener {
	protected final IShape mArea;
	protected final int mEventType;
	public ElfEventListener(final IShape area, final int eventType){
		this.mArea = area;
		this.mEventType = eventType;
	}
	
	public void trigger(ElfEvent event){
		if(event.action == mEventType){
			
		}
	}
}
