package com.ielfgame.stupidGame.design.hotSwap.flash;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.graph.RectEdgeNode;
import elfEngine.basic.node.nodeTouch.TouchNode;
import elfEngine.basic.touch.BasicEventDecoder;
import elfEngine.basic.touch.ElfEvent;
import elfEngine.graphics.PointF;
import elfEngine.graphics.Rectangle;

public class SelectRectNode extends TouchNode {

	private PointF mPoint1;
	private PointF mPoint2;
	private PointF mMinSize = new PointF(20, 20);
	
	private final RectEdgeNode mDrawer = new RectEdgeNode(this, 0);
	private Runnable mTrigger;

	public SelectRectNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setTouchDecoder(new SelectRectEventDecoder());
		mDrawer.setUseSettedSize(true);
	}
	
	public void drawSelf() {
		final Rectangle rect = this.getRectangle();
		if(rect != null) {
			final ElfPointf ret1 = new ElfPointf();
			this.screenXYToChild(rect.left, rect.bottom, ret1);
			
			final ElfPointf ret2 = new ElfPointf();
			this.screenXYToChild(rect.right, rect.top, ret2);
			
			mDrawer.setSize(Math.abs(ret1.x-ret2.x), Math.abs(ret1.y-ret2.y));
			mDrawer.setPosition((ret1.x+ret2.x)/2, (ret1.y+ret2.y)/2);
			mDrawer.drawSprite();
		}
	}

	public PointF getPoint1() {
		return mPoint1;
	}

	public void setPoint1(PointF point1) {
		this.mPoint1 = point1;
	}

	public PointF getPoint2() {
		return mPoint2;
	}

	public void setPoint2(PointF point2) {
		this.mPoint2 = point2;
	}

	public PointF getMinSize() {
		return mMinSize;
	}

	public void setMinSize(PointF minSize) {
		this.mMinSize = minSize;
	}
	
	public void setListener(final Runnable run) {
		this.mTrigger = run;
	}
	
	public void trigger() {
		if(this.mTrigger != null) {
			this.mTrigger.run();
		}
	}

	public Rectangle getRectangle() {
		if (mPoint1 != null && mPoint2 != null) {
			if (Math.abs((mPoint1.x - mPoint2.x)) > mMinSize.x && Math.abs((mPoint1.y - mPoint2.y)) > mMinSize.y) {
				final Rectangle rect = new Rectangle();
				rect.left = Math.min(mPoint1.x, mPoint2.x);
				rect.right = Math.max(mPoint1.x, mPoint2.x);
				rect.top = Math.max(mPoint1.y, mPoint2.y);
				rect.bottom = Math.min(mPoint1.y, mPoint2.y);
				return rect;
			}
		}
		return null;
	}

	/***
	 * reset setPoint1 get setPoint2 setMinWidth setMinHeight
	 * 
	 * getSelectRect
	 */

	private static class SelectRectEventDecoder extends BasicEventDecoder {
		private boolean mCatched;
		
		public void setDelegate(ElfNode node) {
			super.setDelegate(node);
			assert (node instanceof SelectRectNode);
		}
		
		public SelectRectNode getDelegate() {
			return (SelectRectNode) mDelegateNode;
		}

		protected boolean onTouchMove(ElfEvent event, float moveX, float moveY) {
			if (mCatched && mDelegateNode.isInSelectSize(event)) {
				SelectRectNode node = getDelegate();
				node.setPoint2(new PointF(event.x, event.y));
				
//				node.trigger();
			}

			return false;
		}

		protected boolean onTouchDwon(ElfEvent event) {
			if (mDelegateNode.isInSelectSize(event)) {
				mCatched = true;
				SelectRectNode node = getDelegate();
				node.setPoint1(new PointF(event.x, event.y));
				node.setPoint2(new PointF(event.x, event.y));
				
//				node.trigger();
			}
			return false;
		}

		protected boolean onTouchUp(ElfEvent event) {
			if (mCatched && mDelegateNode.isInSelectSize(event)) {
				SelectRectNode node = getDelegate();
				node.setPoint2(new PointF(event.x, event.y));
				
				node.trigger();
			}

			mCatched = false;
			return false;
		}
	}
}
