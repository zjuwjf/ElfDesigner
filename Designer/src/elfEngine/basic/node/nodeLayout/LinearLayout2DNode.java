package elfEngine.basic.node.nodeLayout;

import java.util.ArrayList;

import com.ielfgame.stupidGame.enumTypes.HorizontalAlign;
import com.ielfgame.stupidGame.enumTypes.Orientation;
import com.ielfgame.stupidGame.enumTypes.VerticalAlign;

import elfEngine.basic.node.ElfNode;

public class LinearLayout2DNode extends ElfNode{

	public LinearLayout2DNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		this.setUseSettedSize(true);
	}
	
	protected Orientation mOrientation = Orientation.Horizontal; 
	
	protected HorizontalAlign mHorizontalAlign = HorizontalAlign.Left;
	protected HorizontalAlign mHorizontalBorn = HorizontalAlign.Left;
	protected VerticalAlign mVerticalAlign = VerticalAlign.Top;
	protected VerticalAlign mVerticalBorn = VerticalAlign.Top;
	
	protected float mSeparatorX = 0;
	protected float mSeparatorY = 0;
	protected float mDesignedLength = 400;
	
	public Orientation getOrientation() {
		return mOrientation;
	}
	public void setOrientation(Orientation mOrientation) {
		this.mOrientation = mOrientation;
	}
	protected final static int REF_Orientation = DEFAULT_SHIFT;

	public HorizontalAlign getHorizontalAlign() {
		return mHorizontalAlign;
	}
	public void setHorizontalAlign(HorizontalAlign mHorizontalAlign) {
		this.mHorizontalAlign = mHorizontalAlign;
	}
	protected final static int REF_HorizontalAlign = DEFAULT_SHIFT;
	
	public HorizontalAlign getHorizontalBorn() {
		return mHorizontalBorn;
	}
	public void setHorizontalBorn(HorizontalAlign mHorizontalBorn) {
		this.mHorizontalBorn = mHorizontalBorn;
	}
	protected final static int REF_HorizontalBorn = DEFAULT_SHIFT;
	
	public VerticalAlign getVerticalAlign() {
		return mVerticalAlign;
	}
	public void setVerticalAlign(VerticalAlign mVerticalAlign) {
		this.mVerticalAlign = mVerticalAlign;
	}
	protected final static int REF_VerticalAlign = DEFAULT_SHIFT;
	
	public VerticalAlign getVerticalBorn() {
		return mVerticalBorn;
	}
	public void setVerticalBorn(VerticalAlign mVerticalBorn) {
		this.mVerticalBorn = mVerticalBorn;
	}
	protected final static int REF_VerticalBorn = DEFAULT_SHIFT;
	
	public float getSeparatorX() {
		return mSeparatorX;
	}
	public void setSeparatorX(float mSeparatorX) {
		this.mSeparatorX = mSeparatorX;
	}
	protected final static int REF_SeparatorX = DEFAULT_SHIFT;
	
	public float getSeparatorY() {
		return mSeparatorY;
	}
	public void setSeparatorY(float mSeparatorY) {
		this.mSeparatorY = mSeparatorY;
	}
	protected final static int REF_SeparatorY = DEFAULT_SHIFT;
	
	public float getDesignedLength() {
		return mDesignedLength;
	}
	public void setDesignedLength(float mDesignedLength) {
		this.mDesignedLength = mDesignedLength;
	}
	protected final static int REF_DesignedLength = DEFAULT_SHIFT;
	
	public void drawSprite() {
		layout();
		super.drawSprite();
	}
	
	public void layout() { 
		final ArrayList<Union> us = split();
		final int count = us.size(); 
		float sum;
		final boolean little;
		final float sep;
		
		if( mOrientation.isHorizontal() ) {
			sep = mSeparatorY;
			sum = -this.getSize().y/2; 
			if(mVerticalBorn == VerticalAlign.Bottom) {
				little = true;
			} else { 
				little = false;
			} 
		} else {
			sep = mSeparatorX;
			sum = -this.getSize().x/2; 
			if(mHorizontalBorn == HorizontalAlign.Left) {
				little = true;
			} else { 
				little = false;
			} 
		} 
		
		for(int i=0; i<count; i++) { 
			final int index = little?i:count-1-i;
			final Union u = us.get(index); 
			final float len = getLen(mOrientation, u.height, u.width)/2;
			sum += (sep+len);
			u.layout(mOrientation, mHorizontalAlign, mVerticalAlign, mHorizontalBorn, mVerticalBorn, mDesignedLength, mSeparatorX, mSeparatorY, sum);
			sum += (len);
		} 
	} 
	
	final float getLen(Orientation o, float w, float h) {
		return (o.isHorizontal())?w:h;
	} 
	
	final ArrayList<Union> split() {
		final ArrayList<Union> ret = new ArrayList<Union>();
		final Union [] union = {new Union()};
		final float [] sum = {getLen(mOrientation,mSeparatorY,mSeparatorX)};
		this.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				final boolean b = union[0].setNode(node,mOrientation,mDesignedLength,mSeparatorX, mSeparatorY);
				if(!b) {
					sum[0] += getLen(mOrientation,union[0].height,union[0].width)+getLen(mOrientation, mSeparatorY, mSeparatorX);
					ret.add(union[0]);
					union[0] = new Union();
					union[0].setNode(node,mOrientation,mDesignedLength,mSeparatorX, mSeparatorY);
				} 
				return false; 
			} 
		}); 
		ret.add(union[0]);
		sum[0] += getLen(mOrientation,union[0].height,union[0].width)+getLen(mOrientation, mSeparatorY, mSeparatorX);
		if( mOrientation.isHorizontal() ) {
			this.setSize(mDesignedLength, sum[0]);
		} else { 
			this.setSize(sum[0], mDesignedLength);
		} 
		return ret; 
	} 
	
	static class Union {
		public final ArrayList<ElfNode> nodes = new ArrayList<ElfNode>();
		public float width;
		public float height;
		public boolean setNode(ElfNode node, Orientation orientation, 
				float dl, float sx, float sy) { 
			final float nw = node.getWidth()*node.getScale().x;
			final float nh = node.getHeight()*node.getScale().y;
			if( orientation.isHorizontal() ) { 
				if(nodes.isEmpty()) { 
					width += sx; 
				} 
				final float w = width + nw + sx;
				if(w > dl && !nodes.isEmpty()) {
					return false;
				}
				width = w;
				height = Math.max(height, nh);
			} else { 
				if(nodes.isEmpty()) { 
					height += sy; 
				} 
				final float h = height + nh + sy;
				if(h > dl && !nodes.isEmpty()) {
					return false;
				} 
				width = Math.max(width, nw);
				height = h; 
			} 
			nodes.add(node);
			return true;
		}
		
		public void layout(Orientation orientation, 
				HorizontalAlign hAlign, VerticalAlign vAlign,
				HorizontalAlign hBorn, VerticalAlign vBorn,
				float dl, float sx, float sy, float pos) {
			if(orientation.isHorizontal()) {
				final int count = nodes.size();
				float off=0;
				switch (hAlign) {
				case Left:	off = -dl/2;break;
				case Center:off = -width/2;break;
				case Right:	off = +dl/2-width;break;
				}
				float sum = off;
				final boolean little = hBorn==HorizontalAlign.Left;
				for(int i=0; i<count; i++) {
					final int index = little? i:count-1-i;
					final ElfNode node = nodes.get(index);
					final float hw = Math.abs(node.getWidth()*node.getScale().x/2);
					sum += sx+hw; 
					node.setPosition(sum, pos); 
					sum += hw; 
				} 
			} else { 
				final int count = nodes.size();
				float off=0;
				switch (vAlign) {
				case Bottom:off = -dl/2;break;
				case Center:off = -height/2;break;
				case Top:	off = +dl/2-height;break;
				}
				float sum = off;
				final boolean little = vBorn==VerticalAlign.Bottom;
				for(int i=0; i<count; i++) {
					final int index = little? i:count-1-i;
					final ElfNode node = nodes.get(index);
					final float hh = Math.abs(node.getHeight()*node.getScale().y/2);
					sum += sy+hh;
					node.setPosition(pos, sum);
					sum += hh;
				} 
			} 
		}
	} 
}
