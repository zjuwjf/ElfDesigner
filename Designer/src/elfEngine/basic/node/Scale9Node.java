package elfEngine.basic.node;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.opengl.GLManage;
import elfEngine.opengl.TextureRegion;

public class Scale9Node extends ElfNode {

	private int capInsetsHeight;
	private int capInsetsWidth;
	private int capInsetsX;
	private int capInsetsY;

	private static final int LT_INDEX = 0;
	private static final int MT_INDEX = 1;
	private static final int RT_INDEX = 2;

	private static final int LM_INDEX = 3;
	private static final int MM_INDEX = 4;
	private static final int RM_INDEX = 5;

	private static final int LB_INDEX = 6;
	private static final int MB_INDEX = 7;
	private static final int RB_INDEX = 8;

	private TextureRegion[] mTextureRegionArray = null;
	private ElfPointf [] mPositionArray = new ElfPointf[9];
	private ElfPointf [] mScaleArray = new ElfPointf[9];

	public Scale9Node(ElfNode father, int ordinal) {
		super(father, ordinal);

		this.setUseSettedSize(true);
		
		for(int i=0; i<9; i++) {
			mPositionArray[i] = new ElfPointf();
		}
		for(int i=0; i<9; i++) {
			mScaleArray[i] = new ElfPointf(1, 1);
		}
	}

	public void setUseSettedSize(boolean use) {
		super.setUseSettedSize(true);
	}
	
	public boolean getUseSettedSize() {
		return true;
	}
	
	public int getCapInsetsX() {
		return capInsetsX;
	}

	public void setCapInsetsX(int capInsetsX) {
		this.capInsetsX = capInsetsX;
		updateTextureRegions();
	}
	protected final static int REF_CapInsetsX = DEFAULT_SHIFT;

	public int getCapInsetsY() {
		return capInsetsY;
	}

	public void setCapInsetsY(int capInsetsY) {
		this.capInsetsY = capInsetsY;
		updateTextureRegions();
	}
	protected final static int REF_CapInsetsY = DEFAULT_SHIFT;

	public int getCapInsetsWidth() {
		return capInsetsWidth;
	}

	public void setCapInsetsWidth(int capInsetsWidth) {
		this.capInsetsWidth = capInsetsWidth;
		updateTextureRegions();
	}
	protected final static int REF_CapInsetsWidth = DEFAULT_SHIFT;
	
	public int getCapInsetsHeight() {
		return capInsetsHeight;
	}

	public void setCapInsetsHeight(int capInsetsHeight) {
		this.capInsetsHeight = capInsetsHeight;
		updateTextureRegions();
	}
	protected final static int REF_CapInsetsHeight = DEFAULT_SHIFT;

	
	
	public void setResid(final String resid) {
		super.setResid(resid);
		updateTextureRegions();
	}

	private void updateTextureRegions() {
		final TextureRegion tr = GLManage.loadTextureRegion(getResid(), getGLId());
		if (tr != null) {
			final int rw = tr.getWidth();
			final int rh = tr.getHeight();

			mTextureRegionArray = new TextureRegion[9];

			mTextureRegionArray[LT_INDEX] = new TextureRegion(tr, 0, 0, capInsetsX, capInsetsY);
			mTextureRegionArray[MT_INDEX] = new TextureRegion(tr, capInsetsX, 0, capInsetsWidth, capInsetsY);
			mTextureRegionArray[RT_INDEX] = new TextureRegion(tr, capInsetsX + capInsetsWidth, 0, rw - (capInsetsX + capInsetsWidth), capInsetsY);

			mTextureRegionArray[LM_INDEX] = new TextureRegion(tr, 0, capInsetsY, capInsetsX, capInsetsHeight);
			mTextureRegionArray[MM_INDEX] = new TextureRegion(tr, capInsetsX, capInsetsY, capInsetsWidth, capInsetsHeight);
			mTextureRegionArray[RM_INDEX] = new TextureRegion(tr, capInsetsX + capInsetsWidth, capInsetsY, rw - (capInsetsX + capInsetsWidth), capInsetsHeight);

			mTextureRegionArray[LB_INDEX] = new TextureRegion(tr, 0, capInsetsY + capInsetsHeight, capInsetsX, rh - (capInsetsY + capInsetsHeight));
			mTextureRegionArray[MB_INDEX] = new TextureRegion(tr, capInsetsX, capInsetsY + capInsetsHeight, capInsetsWidth, rh - (capInsetsY + capInsetsHeight));
			mTextureRegionArray[RB_INDEX] = new TextureRegion(tr, capInsetsX + capInsetsWidth, capInsetsY + capInsetsHeight, rw - (capInsetsX + capInsetsWidth), rh - (capInsetsY + capInsetsHeight));
			
			updatPositionScaleArray();
		} else {
			mTextureRegionArray = null;
		}
	}
	
	public void setSize(ElfPointf size) {
		super.setSize(size);
		updatPositionScaleArray();
	}

	private void updatPositionScaleArray() {
		if(mTextureRegionArray != null) {
			final float width = this.getWidth();
			final float height = this.getHeight();
			
			final float lw = mTextureRegionArray[LT_INDEX].getWidth(); 
			final float mw = mTextureRegionArray[MT_INDEX].getWidth(); 
			final float rw = mTextureRegionArray[RT_INDEX].getWidth(); 
			
			final float th = mTextureRegionArray[LT_INDEX].getHeight();
			final float mh = mTextureRegionArray[LM_INDEX].getHeight();
			final float bh = mTextureRegionArray[LB_INDEX].getHeight();
			
			updateNodeX(width, lw, mw, rw, mPositionArray[LT_INDEX],mPositionArray[MT_INDEX],mPositionArray[RT_INDEX], mScaleArray[MT_INDEX]);
			updateNodeX(width, lw, mw, rw, mPositionArray[LM_INDEX],mPositionArray[MM_INDEX],mPositionArray[RM_INDEX], mScaleArray[MM_INDEX]);
			updateNodeX(width, lw, mw, rw, mPositionArray[LB_INDEX],mPositionArray[MB_INDEX],mPositionArray[RB_INDEX], mScaleArray[MB_INDEX]);
			
			updateNodeY(height, th, mh, bh, mPositionArray[LT_INDEX],mPositionArray[LM_INDEX],mPositionArray[LB_INDEX], mScaleArray[LM_INDEX]);
			updateNodeY(height, th, mh, bh, mPositionArray[MT_INDEX],mPositionArray[MM_INDEX],mPositionArray[MB_INDEX], mScaleArray[MM_INDEX]);
			updateNodeY(height, th, mh, bh, mPositionArray[RT_INDEX],mPositionArray[RM_INDEX],mPositionArray[RB_INDEX], mScaleArray[RM_INDEX]);
		}
	}
	
	private static final void updateNodeX(final float w, final float lw, final float mw, final float rw, final ElfPointf l, final ElfPointf m, final ElfPointf r, final ElfPointf mScale) {
		l.x = (-w/2+lw/2);
		if(mw > 0) {
			m.x = ((lw-rw)/2);
			mScale.x = ((0.5f+w-lw-rw)/mw);
		}
		r.x = (+w/2-rw/2);
	}
	
	private static final void updateNodeY(final float h, final float th, final float mh, final float bh, final ElfPointf t, final ElfPointf m, final ElfPointf b, final ElfPointf mScale) {
		t.y = (+h/2-th/2);
		if(mh > 0) {
			m.y = ((bh-th)/2);
			mScale.y = ((0.5f+h-th-bh)/mh);
		}
		b.y = (-h/2+bh/2);
	}

	/***
	 * "capInsetsHeight": 1, "capInsetsWidth": 1, "capInsetsX": 0, "capInsetsY":
	 * 0,
	 */

	// private TextureRegion

	public void drawSelf() {
		if (mTextureRegionArray != null) {
			for(int i=0; i<9; i++) {
				final TextureRegion tr = mTextureRegionArray[i];
				final ElfPointf pos = mPositionArray[i];
				final ElfPointf scale = mScaleArray[i];
				
				tr.draw(pos.x, pos.y, scale.x, scale.y, 0);
			}
		}
	}
}
