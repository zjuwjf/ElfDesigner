package elfEngine.basic.node.nodeAnimate.flash;

import org.eclipse.swt.graphics.Rectangle;

import com.ielfgame.stupidGame.data.ElfPointi;


public class SelectRegion { 
	public interface ISelectRegionChangeListener {
		public void onSelectRegionChanged(final Rectangle region);
	}
	
	private ElfPointi mFirstPoint = new ElfPointi();
	private ElfPointi mLastPoint = new ElfPointi();
	private int mThreshold = 3;
	private ISelectRegionChangeListener mSelectRegionChangeListener;
	
	public SelectRegion(ISelectRegionChangeListener listener) {
		setSelectRegionChangeListener(listener);
	}
	
	public void setFirstPoint(int x, int y) {
		mFirstPoint.set(x, y);
		mLastPoint.set(x, y);
		onSelectRegionChange();
	}
	public void setLastPoint(int x, int y) {
		mLastPoint.set(x, y);
		onSelectRegionChange();
	}
	
	public void setInvalid() {
		this.setFirstPoint(Integer.MAX_VALUE,Integer.MAX_VALUE);
	} 
	
	public void setThreshold(int threshold) {
		mThreshold = threshold;
	} 
	
	public void setSelectRegionChangeListener(ISelectRegionChangeListener selectRegionChangeListener) {
		mSelectRegionChangeListener = selectRegionChangeListener;
	}
	
	private void onSelectRegionChange() {
		if(mSelectRegionChangeListener != null) {
			mSelectRegionChangeListener.onSelectRegionChanged(getRegion());
		}
	}
	
	public Rectangle getRegion() {
		if(mFirstPoint.x == Integer.MAX_VALUE || mLastPoint.x == Integer.MAX_VALUE || mFirstPoint.y == Integer.MAX_VALUE || mLastPoint.y == Integer.MAX_VALUE) {
			return null;
		}
		
		final int minX = Math.min(mFirstPoint.x, mLastPoint.x);
		final int maxX = Math.max(mFirstPoint.x, mLastPoint.x);
		final int minY = Math.min(mFirstPoint.y, mLastPoint.y);
		final int maxY = Math.max(mFirstPoint.y, mLastPoint.y);
		
		final int width = maxX-minX;
		final int height = maxY-minY;
		
		if(width>mThreshold && height>mThreshold) {
			final Rectangle ret = new Rectangle(minX, minY, width, height);
			return ret;
		}
		
		return null;
	}
}
