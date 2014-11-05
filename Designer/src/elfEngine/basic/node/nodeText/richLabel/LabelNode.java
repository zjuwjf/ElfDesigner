package elfEngine.basic.node.nodeText.richLabel;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointi;
import com.ielfgame.stupidGame.enumTypes.HorizontalTextAlign;
import com.ielfgame.stupidGame.enumTypes.VerticalTextAlign;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.LanguageWorkSpaceTab;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.richLabel.RichLabelManager.LabelDefine;

public class LabelNode extends ElfNode {

	protected final LabelDefine mLabelDefine = new LabelDefine();
	private String mText = "text";
	protected boolean mNeedUpdateTexture;
	
	public LabelNode(ElfNode father, int ordinal) {
		super(father, ordinal);
		
		this.setUseSettedSize(false);
		
		this.mLabelDefine.enableStroke = true;
		this.mLabelDefine.strokeSize = 2;
		this.mLabelDefine.strokeColor.set(0, 0, 0, 0.5f);
	}
	
	public void setUseSettedSize(boolean b) {
		super.setUseSettedSize(false);
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

	public void calcSprite(float pMsElapsed) {
		if (mNeedUpdateTexture) {
			mNeedUpdateTexture = false;
			// mSizeResid = LabelNameGenerator.getLabelName();
			mResid = LabelNameGenerator.getLabelName();
			RichLabelManager.renderPoolLabel(this.getText(), this.mLabelDefine.copy(), mResid);
			// super.setResid(mSizeResid);
		}
		super.calcSprite(pMsElapsed);
	}

	protected String getSizeResid() {
		return mResid;
	}

	private final void setNeedUpdateTexture() {
		mNeedUpdateTexture = true;
	}

	public void setText(String text) {
		if (text == null) {
			text = "";
		}

		if (!mText.equals(text)) {
			mText = text;
			mKey = null;
			this.setNeedUpdateTexture();
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

	public void setDimension(ElfPointi p) {
		this.mLabelDefine.dimension.set(p);
		this.setNeedUpdateTexture();
	}

	public ElfPointi getDimension() {
		return new ElfPointi(this.mLabelDefine.dimension);
	}

	protected final static int REF_Dimension = DEFAULT_SHIFT;

	public void setTextFont(String fontName) {
		this.mLabelDefine.fontName = fontName;
		this.setNeedUpdateTexture();
	}

	public String getTextFont() {
		return this.mLabelDefine.fontName;
	}

	public static String[] arrayTextFont() {
		return AWTFontImageHelper.getFontNames();
	}

	// reflect
	protected static final int REF_TextFont = DEFAULT_SHIFT;

	public void setFontSize(float size) {
		if (size <= 100 && size > 0) {
			this.mLabelDefine.fontSize = size;
			this.setNeedUpdateTexture();
		}
	}

	public float getFontSize() {
		return this.mLabelDefine.fontSize;
	}

	protected final static int REF_FontSize = DEFAULT_SHIFT;

	public void setFontStyle(int style) {
		this.mLabelDefine.fontStyle = style;
		this.setNeedUpdateTexture();
	}

	public int getFontStyle() {
		return this.mLabelDefine.fontStyle;
	}

	protected final static int REF_FontStyle = DEFAULT_SHIFT;

	public void setFillColor(ElfColor color) {
		this.mLabelDefine.fillColor.set(color);
		this.setNeedUpdateTexture();
	}

	public ElfColor getFillColor() {
		return new ElfColor(this.mLabelDefine.fillColor);
	}

	protected final static int REF_FillColor = DEFAULT_SHIFT;

	public void setHorizontalTextAlign(HorizontalTextAlign hor) {
		this.mLabelDefine.horizontalAlign = hor;
		this.setNeedUpdateTexture();
	}

	public HorizontalTextAlign getHorizontalTextAlign() {
		return this.mLabelDefine.horizontalAlign;
	}

	protected final static int REF_HorizontalTextAlign = DEFAULT_SHIFT;

	public void setVerticalTextAlign(VerticalTextAlign ver) {
		this.mLabelDefine.verticalAlign = ver;
		this.setNeedUpdateTexture();
	}

	public VerticalTextAlign getVerticalTextAlign() {
		return this.mLabelDefine.verticalAlign;
	}

	protected final static int REF_VerticalTextAlign = DEFAULT_SHIFT;

	public void setEnableStroke(boolean enable) {
		this.mLabelDefine.enableStroke = enable;
		this.setNeedUpdateTexture();
	}

	public boolean getEnableStroke() {
		return this.mLabelDefine.enableStroke;
	}

	protected final static int REF_EnableStroke = DEFAULT_SHIFT;

	public void setStrokeSize(int size) {
		this.mLabelDefine.strokeSize = size * 2;
		this.setNeedUpdateTexture();
	}

	public int getStrokeSize() {
		return this.mLabelDefine.strokeSize / 2;
	}

	protected final static int REF_StrokeSize = DEFAULT_SHIFT;

	public void setStrokeColor(ElfColor color) {
		this.mLabelDefine.strokeColor.set(color);
		this.setNeedUpdateTexture();
	}

	public ElfColor getStrokeColor() {
		return new ElfColor(this.mLabelDefine.strokeColor);
	}

	protected final static int REF_StrokeColor = DEFAULT_SHIFT;

	public void setEnableShadow(boolean enable) {
		this.mLabelDefine.enableShadow = enable;
		this.setNeedUpdateTexture();
	}

	public boolean getEnableShadow() {
		return this.mLabelDefine.enableShadow;
	}

	protected final static int REF_EnableShadow = DEFAULT_SHIFT;

	public void setShadowOffset(ElfPointi point) {
		this.mLabelDefine.shadowOffset.set(point);
		this.setNeedUpdateTexture();
	}

	public ElfPointi getShadowOffset() {
		return new ElfPointi(this.mLabelDefine.shadowOffset);
	}

	protected final static int REF_ShadowOffset = DEFAULT_SHIFT;

	public void setShadowColor(ElfColor color) {
		this.mLabelDefine.shadowColor.set(color);
		this.setNeedUpdateTexture();
	}

	public ElfColor getShadowColor() {
		return new ElfColor(this.mLabelDefine.shadowColor);
	}

	protected final static int REF_ShadowColor = DEFAULT_SHIFT;
	
}
