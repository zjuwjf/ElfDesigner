package elfEngine.basic.node.nodeTouch.slider;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.TouchNode;
import elfEngine.basic.node.particle.modifier.MathHelper;

public class SliderNode extends TouchNode implements ISlider {
	
	public static interface IPercentageListener {
		public void onPercentageChange(float percentage, boolean innerCall);
	}
	
	private ISlider mSliderListener;
	
	private final ElfPointf mPoint1 = new ElfPointf(-1000, 0);
	private final ElfPointf mPoint2 = new ElfPointf(1000, 0);
	
	private IPercentageListener mPercentageListener;
	private final SliderDecoder mSliderDecoder = new SliderDecoder();
	
	//bar-node?
	
	public SliderNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setTouchDecoder(mSliderDecoder);
	}
	
	public void setPercentageListener(IPercentageListener percentage) {
		mPercentageListener = percentage;
	}
	
	public void setSliderListener(ISlider slider) {
		mSliderListener = slider;
	}
	
	@Override
	public void sliderUp(float x, float y) {
		if (mSliderListener != null) {
			mSliderListener.sliderUp(x, y);
		}
	}

	@Override
	public void sliderMove(float x, float y) {
		if (Math.abs(mPoint1.x - mPoint2.x) > Math.abs(mPoint1.y - mPoint2.y)) {
			final ElfPointf posInScreen = this.getPositionInScreen();
			posInScreen.x = x;
			this.setPositionInScreen(posInScreen);
			
			final float width = this.getWidth();
			final ElfPointf anchor = this.getAnchorPosition();

			final ElfPointf pos = this.getPosition();
			
			final float rate = getRate(mPoint1.x, mPoint2.x, pos.x, width, anchor.x);
			this.setPercentage(rate*100, true);
		} else {
			final ElfPointf posInScreen = this.getPositionInScreen();
			posInScreen.y = y;
			this.setPositionInScreen(posInScreen);

			final float height = this.getHeight();
			final ElfPointf anchor = this.getAnchorPosition();

			final ElfPointf pos = this.getPosition();

			final float rate = getRate(mPoint1.y, mPoint2.y, pos.y, height, anchor.y);
			this.setPercentage(rate*100, true);
		}

		if (mSliderListener != null) {
			mSliderListener.sliderMove(x, y);
		}
	}

	@Override
	public void sliderDown(float x, float y) {
		if (mSliderListener != null) {
			mSliderListener.sliderDown(x, y);
		}
	}

	public void setCatched(boolean catched) {
		mSliderDecoder.setCatched(catched);
	}

	public boolean getCatched() {
		return mSliderDecoder.getCatched();
	}

	public void setPoint1(ElfPointf point) {
		mPoint1.set(point);
	}

	public void setPoint2(ElfPointf point) {
		mPoint2.set(point);
	}

	public ElfPointf getPoint1() {
		return new ElfPointf(mPoint1);
	}

	public ElfPointf getPoint2() {
		return new ElfPointf(mPoint2);
	}

	protected final static int REF_Point1 = DEFAULT_SHIFT;
	protected final static int REF_Point2 = DEFAULT_SHIFT;

	@Override
	public void setPercentage(float percentage, boolean innerCall) {
		percentage = Math.max(0, percentage);
		percentage = Math.min(100, percentage);
		
		final ElfPointf anchor = this.getAnchorPosition();
		final float width = this.getWidth();
		final float height = this.getHeight();
		
		final float x,y;
		final float rate = percentage/100f;
		if (Math.abs(mPoint1.x - mPoint2.x) > Math.abs(mPoint1.y - mPoint2.y)) {
			if (mPoint1.x > mPoint2.x) {
				x = MathHelper.between(mPoint1.x-width*(1-anchor.x), mPoint2.x+width*anchor.x, rate);
			} else {
				x = MathHelper.between(mPoint1.x+width*anchor.x, mPoint2.x-width*(1-anchor.x), rate);
			}
			y = MathHelper.between(mPoint1.y, mPoint2.y, rate);
		} else {
			if (mPoint1.y > mPoint2.y) {
				y = MathHelper.between(mPoint1.y-height*(1-anchor.y), mPoint2.y+height*anchor.y, rate);
			} else {
				y = MathHelper.between(mPoint1.y+height*anchor.y, mPoint2.y-height*(1-anchor.y), rate);
			}
			x = MathHelper.between(mPoint1.x, mPoint2.x, rate);
		}

		this.setPosition(x, y);
		
		if(mPercentageListener != null) {
			mPercentageListener.onPercentageChange(percentage, innerCall);
		}
	}
	
	public void setPercentage(float percentage) {
		this.setPercentage(percentage, false);
	}

	public static float getRate(float f1, float f2, float f, float l,  float al) {
		if(Math.abs(f1-f2) >= l) {
			if(f1 > f2) {
				return (f1-f-l*(1-al))/(f1-f2-l);
			} else {
				return (f-f1-l*al)/(f2-f1-l);
			}
		} else {
			if(f1 == f2) {
				return f1;
			} else {
				return (f-f1)/(f2-f1);
			}
		}
	}
	
	public float getPercentage() {
		final ElfPointf anchor = this.getAnchorPosition();
		final ElfPointf pos = this.getPosition();
		
		if (Math.abs(mPoint1.x - mPoint2.x) > Math.abs(mPoint1.y - mPoint2.y)) {
			final float width = this.getWidth();
			float r;
			if(mPoint1.x > mPoint2.x) {
				r = (mPoint1.x-width*(1-anchor.x)-pos.x)/(mPoint1.x-mPoint2.x-width);
			} else { 
				r = (pos.x-width*anchor.x-mPoint1.x)/(mPoint2.x-mPoint1.x-width);
			}
			return r*100;
		} else {
			final float height = this.getHeight();
			float r;
			if(mPoint1.y > mPoint2.y) {
				r = (mPoint1.y-pos.y-height*(1-anchor.y))/(mPoint1.y-mPoint2.y-height);
			} else { 
				r = (-mPoint1.y+pos.y-height*anchor.y)/(mPoint2.y-mPoint1.y-height);
			}
			return r*100;
		}
	}
	
	protected final static int REF_Percentage = DEFAULT_SHIFT;
}
