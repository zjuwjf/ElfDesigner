package elfEngine.basic.touch;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.modifier.RotateModifier;


public class RotateEventDecoder extends BasicEventDecoder{
	
	protected final ElfPointf mLastTouchPoint = new ElfPointf();
	protected final ElfPointf mInSelfPoint = new ElfPointf();
	
	protected float mMinRotate = -360, mMaxRotate = 720;
	protected float [] mStayRotates = {0, 90, 180, 270};
	protected float mAnimateTime = 500;
	
	protected boolean mSelected = false;
	
	int mStayIndex = 0;
	
	public void setStayIndex(int index) {
		mStayIndex = index;
		if(mDelegateNode != null) {
			if(mStayRotates != null && mStayRotates.length > index) {
				mDelegateNode.setRotate(mStayRotates[index]);
			}
		}
	}
	
	public int getStayIndex() {
		return mStayIndex;
	}
	
	public boolean isSelelced() {
		return mSelected;
	}
	
	public float getAnimateTime() {
		return mAnimateTime;
	}

	public void setAnimateTime(float mAnimateTime) {
		this.mAnimateTime = mAnimateTime;
	}

	public float getMinRotate() {
		return mMinRotate;
	}

	public void setMinRotate(float mMinRotate) {
		this.mMinRotate = mMinRotate;
	}

	public float getMaxRotate() {
		return mMaxRotate;
	}

	public void setMaxRotate(float mMaxRotate) {
		this.mMaxRotate = mMaxRotate;
	}

	public float[] getStayRotates() {
		return mStayRotates;
	}

	public void setStayRotates(float[] stayRotates) {
		this.mStayRotates = new float[stayRotates.length];
		for(int i=0; i<stayRotates.length; i++){
			this.mStayRotates[i] = stayRotates[i];
		}
	}
	
	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		
		if(mDelegateNode != null && mSelected) {
			if(mDelegateNode.isInSelectSize(event)) {
				mDelegateNode.screenXYToChild(event.x, event.y, mInSelfPoint);
				
				final float centerx = mDelegateNode.getWidth() * (mDelegateNode.getAnchorPosition().x - 0.5f);
				final float centery = mDelegateNode.getHeight() * (mDelegateNode.getAnchorPosition().y - 0.5f);
				
				final float oldAngle = (float) Math.atan2(mLastTouchPoint.y - centery, mLastTouchPoint.x - centerx);
				final float newAngle = (float) Math.atan2(mInSelfPoint.y - centery, mInSelfPoint.x - centerx);
				float newRotae = -(newAngle - oldAngle) * 180f / (float) Math.PI;
				
				if(newRotae > 10) {
					newRotae = 0;
				} else if(newRotae < -10) {
					newRotae = 0;
				}
				
				float rotate = getRotate0_360( mDelegateNode.getRotate() );
				if(rotate + newRotae > mMaxRotate) {
					mDelegateNode.setRotate(mMaxRotate);
				} else if(rotate + newRotae < mMinRotate) {
					mDelegateNode.setRotate(mMinRotate);
				} else {
					mDelegateNode.setRotate(rotate + newRotae);
				} 
				
				// chage
				mDelegateNode.screenXYToChild(event.x, event.y, mLastTouchPoint);
				return true;
			} 
		} 
		
		return false;
	}
	
	protected boolean onTouchDwon(ElfEvent event) {
		if(mDelegateNode != null) {
			if(mDelegateNode.isInSelectSize(event)) {
				mDelegateNode.screenXYToChild(event.x, event.y, mLastTouchPoint);
				mSelected = true;
				
				return true;
			} 
		} 
		return false;
	} 
	
	protected boolean onTouchUp(ElfEvent event) {
		if(mDelegateNode != null && mSelected) {
			mDelegateNode.clearModifiers();
			
			float rotate = getRotate0_360(mDelegateNode.getRotate());
			int index = findNearIndex(rotate);
			if(mStayRotates != null && mStayRotates.length > index) {
				mDelegateNode.setUseModifier(true);
				mDelegateNode.addModifier(new RotateModifier(rotate, mStayRotates[index], mAnimateTime, mAnimateTime, null, InterType.Bounce));
			}
			mStayIndex = index;
		} 
		mSelected = false;
		return false;
	} 
	
	final float getRotate0_360(float rotate) {
//		while(rotate < 0) rotate += 360;
//		while(rotate >= 360) rotate -= 360;
		
		return rotate;
	}
	
	final int findNearIndex(float rotate) {
		rotate = getRotate0_360(rotate);
		
		float delty = Float.MAX_VALUE;
		int ret = 0;
		for(int i=0; mStayRotates != null && i<mStayRotates.length; i++) {
			if(Math.abs(rotate - mStayRotates[i]) < delty) {
				delty = Math.abs(rotate - mStayRotates[i]);
				ret = i;
			} 
		} 
		
		return ret;
	} 
}
