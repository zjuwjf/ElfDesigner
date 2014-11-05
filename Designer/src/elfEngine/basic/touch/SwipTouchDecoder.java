package elfEngine.basic.touch;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.counter.InterHelper.InterType;
import elfEngine.basic.modifier.PathModifier;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.pool.ArrayPool;
import elfEngine.graphics.GraphHelper;
import elfEngine.opengl.RectF;

/**
 * @author zju_wjf
 *
 */
public class SwipTouchDecoder extends BasicEventDecoder{
	
	private InterType mInter = InterType.Viscous;
	private float mAnimateLife = 1000;
	private ElfPointf [] mStayPoints;
	
	private float mLastMoveX, mLastMoveY;
	
	private final ElfPointf mMin = 
			new ElfPointf(-999999, -999999), 
			mMax = new ElfPointf(999999, 999999);
	private final RectF mStayPointsBound = new RectF();
	private final ElfPointf mTmpCurr = new ElfPointf();
	private int mStayIndex = 0;
	private long mLastTouchTime = -1;
	
	private float mSpeedRate = 1; 
	private float mTouchMoveRate = 2; 
	private float mMaxOffset = 100;
	
	public float getSpeedRate() {
		return mSpeedRate;
	}

	public void setSpeedRate(float mSpeedRate) {
		this.mSpeedRate = mSpeedRate;
	}

	public float getTouchMoveRate() {
		return mTouchMoveRate;
	}

	public void setTouchMoveRate(float mTouchMoveRate) {
		this.mTouchMoveRate = mTouchMoveRate;
	}
	
	public void setMaxSpeed(float speed) {
		mMaxOffset = speed;
	}
	public float getMaxOffset() {
		return mMaxOffset;
	}
	
	public SwipTouchDecoder() { 
	} 
	
	public void setMinMax(final float minx, final float miny, final float maxx, final float maxy) {
		mMin.setPoint(minx, miny);
		mMax.setPoint(maxx, maxy);
	} 
	
	public void setMinPoint(final ElfPointf min) {
		mMin.set(min);
	}
	
	public void setMaxPoint(final ElfPointf max) {
		mMax.set(max);
	}
	
	public ElfPointf [] getStayPoints() {
		return mStayPoints;
	}
	
	public void setStayPoints(final ElfPointf...stays) {
		mStayPoints = new ElfPointf[stays.length];
		for(int i=0; i<mStayPoints.length; i++) {
			mStayPoints[i] = new ElfPointf();
			mStayPoints[i].set(stays[i]);
		} 
		
		if(mStayPoints != null && mStayPoints.length > 0) {
			mStayPointsBound.set(mStayPoints[0].x, mStayPoints[0].y, mStayPoints[0].x, mStayPoints[0].y);
			for(int i=1; i<mStayPoints.length; i++){
				final ElfPointf p = mStayPoints[i];
				if(mStayPointsBound.left>p.x){
					mStayPointsBound.left=p.x;
				} else if(mStayPointsBound.right<p.x){
					mStayPointsBound.right=p.x;
				}
				
				if(mStayPointsBound.top<p.y){
					mStayPointsBound.top=p.y;
				} else if(mStayPointsBound.bottom>p.y){
					mStayPointsBound.bottom=p.y;
				}
			} 
			
			if(mDelegateNode != null) {
				mDelegateNode.setPosition(mStayPoints[0]);
			}
		} 
	}
	
	public void goToNewStayIndex(final int index){
		mDelegateNode.clearModifiers();
		mTmpCurr.setPoint(mDelegateNode.getPosition().x, mDelegateNode.getPosition().y);
		
		mDelegateNode.setUseModifier(true);
		mDelegateNode.addModifier(new PathModifier(mAnimateLife, mAnimateLife, null, mInter, 
				mTmpCurr, mStayPoints[index]){
			public void onModifierFinished(final ElfNode node){
				super.onModifierFinished(node);
				if(mOnStayIndexChange!=null){
					mOnStayIndexChange.onChangeFinished(mStayIndex, index);
				}
				mStayIndex = index;
			}
		});
	}
	
	public void setCurrentIndex(final int index){
		mDelegateNode.clearModifiers();
		mStayIndex = index;
		mDelegateNode.setPosition(mStayPoints[index]);
	}
	
	public void setInterType(final InterType inter ){ 
		mInter = inter; 
	} 
	public InterType getInterType() { 
		return mInter; 
	} 
	
	public void setAnimateTime(final float time){
		mAnimateLife = time;
	}
	
	public float getAnimateTime() {
		return mAnimateLife;
	} 
	
	public int getCurrentIndex(){
		return mStayIndex;
	}
	
	public ElfPointf getMinPoint() {
		return mMin;
	}
	
	public ElfPointf getMaxPoint() {
		return mMax;
	}
	
	protected boolean onTouchDwon(ElfEvent event) {
		if(mStayPoints == null || mStayPoints.length == 0) {
			return false;
		}
		
		mDelegateNode.clearModifiers();
		mLastMoveX = mLastMoveY = 0;
		
		return false;
	}
	
	protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
		if(mStayPoints == null || mStayPoints.length == 0) {
			return false;
		} 
		
		if(mLastTouchTime < 0) { 
			mLastTouchTime = System.currentTimeMillis();
		} 
		
		if(mRestrictXY != null){ 
			final float [] ret = ArrayPool.float2; 
			ret[0] = moveX; 
			ret[1] = moveY; 
			mRestrictXY.restrict(ret); 
			moveX = ret[0]; 
			moveY = ret[1]; 
		} 
		
		if(mDelegateNode.getPosition().x+moveX>mMax.x){
			mLastMoveX = mMax.x - mDelegateNode.getPosition().x;
			mDelegateNode.setPositionX(mMax.x);
		} else if(mDelegateNode.getPosition().x+moveX<mMin.x){ 
			mLastMoveX = mMin.x - mDelegateNode.getPosition().x; 
			mDelegateNode.setPositionX(mMin.x);
		} else {
			mLastMoveX = moveX;
			mDelegateNode.translate(moveX, 0);
		}
		
		if(mDelegateNode.getPosition().y+moveY>mMax.y){
			mLastMoveY = mMax.y-moveY;
			mDelegateNode.setPositionY(mMax.y);
		} else if(mDelegateNode.getPosition().y+moveY<mMin.y){
			mLastMoveY = mMin.y-moveY;
			mDelegateNode.setPositionY(mMin.y);
		} else {
			mLastMoveY = moveY;
			mDelegateNode.translate(0, moveY);
		} 
		
		return false;
	}

	@Override
	protected boolean onTouchUp(ElfEvent event) {
		if(mStayPoints == null || mStayPoints.length == 0 || mLastTouchTime < 0) {
			return false;
		} 
		
		if(mRestrictXY != null){
			final float [] ret = ArrayPool.float2;
			ret[0] = mLastMoveX;
			ret[1] = mLastMoveY;
			mRestrictXY.restrict(ret);
			mLastMoveX = ret[0];
			mLastMoveY = ret[1];
		}
		
		final float x = mDelegateNode.getPosition().x, y = mDelegateNode.getPosition().y;
		if(x<=mStayPointsBound.left || x>=mStayPointsBound.right){
			mLastMoveX =0;
		}
		if(y<=mStayPointsBound.bottom || y>=mStayPointsBound.top){
			mLastMoveY =0;
		}
		
		float offX, offY;
		offX = this.getTotalMoveX(event);
		offY = this.getTotalMoveY(event);
		
		if(mRestrictXY != null) { 
			final float [] f2 = new float[]{offX, offY}; 
			mRestrictXY.restrict(f2); 
			offX = f2[0]; 
			offY = f2[1]; 
		} 
		
		mTmpCurr.setPoint(x, y); 
		final int nearIndex; 
		long timeElapse = System.currentTimeMillis() - mLastTouchTime; 
		final float speedX = 1000*offX / timeElapse; 
		final float speedY = 1000*offY / timeElapse; 
		
		final float offsetx = Math.max(Math.min(getMaxOffset(), offX*mTouchMoveRate+speedX*mSpeedRate), -getMaxOffset()); 
		final float offsety = Math.max(Math.min(getMaxOffset(), offY*mTouchMoveRate+speedY*mSpeedRate), -getMaxOffset()); 
		
		nearIndex = findNear(x , y, offsetx, offsety); 
		
		if(mOnTouchUp != null){ 
			mOnTouchUp.onChangeStart(mStayIndex, nearIndex); 
		} 
		
		mDelegateNode.setUseModifier(true);
		mDelegateNode.addModifier(new PathModifier(mAnimateLife, mAnimateLife, null, mInter, 
				mTmpCurr, mStayPoints[nearIndex]){
			public void onModifierFinished(final ElfNode node){
				super.onModifierFinished(node);
				if(mOnStayIndexChange!=null){
					mOnStayIndexChange.onChangeFinished(mStayIndex, nearIndex);
				}
				mStayIndex = nearIndex;
			}
		});
		
		return false;
	}
	
	private final int findNear(final float x, final float y){
		int nearIndex = 0;
		float dis = GraphHelper.distance2(x, y, mStayPoints[0].x, mStayPoints[0].y);
		for(int i=1; i<mStayPoints.length; i++){
			final float dis2 = GraphHelper.distance2(x, y, mStayPoints[i].x, mStayPoints[i].y);
			if(dis2<dis){
				dis = dis2;
				nearIndex = i;
			}
		}
		
		return nearIndex;
	}
	
	private final int findNear(final float x, final float y, final float moveX, final float moveY){
		final int b = findNear(x, y);
		final int e = findNear(x+moveX, y+moveY); 
		if(e > b) { 
			return b+1; 
		} else if(e < b) { 
			return b-1; 
		} 
		return b;
	}
	
	private IOnStayIndexChangeListener mOnStayIndexChange;
	private IRestrictXY mRestrictXY;
	private IOnTouchUp mOnTouchUp;
	
	public void setOnStayIndexChangeListener(final IOnStayIndexChangeListener listener){
		mOnStayIndexChange = listener;
	}
	public void setRestrictXY(IRestrictXY restrict){
		mRestrictXY = restrict;
	}
	public void setOnTouchUp(IOnTouchUp onTouchUp){
		mOnTouchUp = onTouchUp;
	}
	
	public void reset() { 
		super.reset();
		mLastTouchTime = -1;
	}
	
	public static interface IOnStayIndexChangeListener{
		public void onChangeFinished(final int lastIndex, final int newIndex);
	} 
	
	public static interface IRestrictXY{
		public void restrict(final float [] xy);
	} 
	
	public static interface IOnTouchUp{
		public void onChangeStart(final int lastIndex, final int newIndex);
	} 
	
	public static class RestrictX implements IRestrictXY{
		public void restrict(float[] xy) {
			xy[0] = 0;
		}
	}
	
	public static class RestrictY implements IRestrictXY{
		public void restrict(float[] xy) {
			xy[1] = 0;
		}
	}
	
	public static class NoRestrict implements IRestrictXY{
		public void restrict(float[] xy) {
		}
	}
	
	public final static IRestrictXY RestrictX = new RestrictX();
	public final static IRestrictXY RestrictY = new RestrictY();
	public final static IRestrictXY NoRestrict = new NoRestrict();
}
