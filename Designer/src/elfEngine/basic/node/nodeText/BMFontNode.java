package elfEngine.basic.node.nodeText;

import com.ielfgame.stupidGame.BMFont.BMFont;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointi;
import com.ielfgame.stupidGame.enumTypes.HorizontalTextAlign;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.LanguageWorkSpaceTab;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.node.ElfNode;

public class BMFontNode extends ElfNode {

	private BMFont mBitmapFont;

	private String mFontFileName;

	private int mDesignedWidth = -1;
	private boolean mNeedUpdate = false;
	private String mText = "";
	private HorizontalTextAlign mHorizontalTextAlign = HorizontalTextAlign.LEFT;

	public BMFontNode(ElfNode father, int ordinal) {
		super(father, ordinal);

		this.setUseSettedSize(true);
	}

	public void setUseSettedSize(boolean use) {
		super.setUseSettedSize(true);
	}

	public void setFontPath(String path) {
		if (path != null) {
			final String simpleName = FileHelper.getSimpleNameWithSuffix(path);
			if (!simpleName.equals(mFontFileName)) {
				mFontFileName = simpleName;
				mNeedUpdate = true;
			}
		}
	}

	public String getFontPath() {
		return mFontFileName;
	}

	protected final static int REF_FontPath = DEFAULT_SHIFT | DROP_RESID_SHIFT;

	public void setDesignedWidth(int w) {
		mDesignedWidth = w;
	}

	public int getDesignedWidth() {
		return mDesignedWidth;
	}

	protected final static int REF_DesignedWidth = DEFAULT_SHIFT;
	
	public void setText(String text) {
		if (text == null) {
			text = "";
		}

		if (!mText.equals(text)) {
			mText = text;
			mKey = null;
		}
	}

	public String getText() {
		return mText;
	}

	protected final static int REF_Text = DEFAULT_SHIFT;

	private String mKey = "";

	public void setKey(String key) {
		if (key == null || key.equals("null")) {
			key = "";
		}

		if (!key.equals(mKey)) {
			final LanguageWorkSpaceTab language = PowerMan.getSingleton(LanguageWorkSpaceTab.class);
			final String value = language.key2value(key);

			if (value != null) {
				this.setText(value);
			}

			mKey = key;
		}
	}

	public String getKey() {
		return mKey;
	}

	protected final static int REF_Key = DEFAULT_SHIFT | DROP_RESID_SHIFT;

	public void setHorizontalTextAlign(HorizontalTextAlign hor) {
		this.mHorizontalTextAlign = hor;
	}

	public HorizontalTextAlign getHorizontalTextAlign() {
		return this.mHorizontalTextAlign;
	}

	protected final static int REF_HorizontalTextAlign = DEFAULT_SHIFT;

	public void calcSprite(float pMsElapsed) {
		super.calcSprite(pMsElapsed);

		if (mNeedUpdate) {
			mNeedUpdate = false;
			mBitmapFont = BMFont.getBMFont(mFontFileName);
		}

		if (mBitmapFont != null && mText != null) {
			final float[] size = mBitmapFont.getSize(mDesignedWidth, mText);
			this.setSize(size[0], size[1]);
		} else {
			this.setSize(50, 50);
		}
	}

	public void drawSelf() {
		if (mBitmapFont != null && mText != null) {
			mBitmapFont.drawTextInWidth(mText, mDesignedWidth, mHorizontalTextAlign);
		}
	}

	// for show
	public String getResid() {
		return null;
	}

	public void setResid(String id) {
	}

	public String[] getSelfResids() {
		return new String[] {};
	}
	
	public void setFontFillColor(ElfColor color) {
		this.setColor(color);
	}
	public ElfColor getFontFillColor() {
		return this.getColor();
	}
	protected final static int REF_FontFillColor = BACKGROUND_SHIFT;
	
	public void setDimension(ElfPointi p) {
		this.setDesignedWidth(p.x);
	}

	public ElfPointi getDimension() {
		return new ElfPointi(this.getDesignedWidth(), 0);
	}
	protected final static int REF_Dimension = BACKGROUND_SHIFT;
	
	public void setFontSize(float size) {
		final int n = Math.round(size);
		final String path = String.format("fzcy-%d.fnt", n);
		this.setFontPath(path);
	}

	public float getFontSize() {
		return 24;
	}

	protected final static int REF_FontSize = BACKGROUND_SHIFT;
	
}
