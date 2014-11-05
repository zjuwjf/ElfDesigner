package elfEngine.basic.node.nodeText;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.enumTypes.TextAlign;
import com.ielfgame.stupidGame.enumTypes.TextFont;
import com.ielfgame.stupidGame.enumTypes.TextStyle;
import com.ielfgame.stupidGame.enumTypes.VerticalTextAlign;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.Texture;
import elfEngine.opengl.TextureRegion;

public class TextNode extends ElfNode {
	
	public TextNode(ElfNode father, int ordinal) {
		super(father, ordinal);
//		setBlendMode(BlendMode.ACTIVLE);
		mImage = new Image(null, new ImageData(1, 1, 32, new PaletteData(0xff0000, 0xff00, 0xff)));
		setName("#text");
		this.mNeedUpdateGL = true;
	}
	
	public String [] getSelfResids() {
		return new String[0];
	}
	
	public void setGLid(int id) {
		if(id != this.getGLId()) {
			this.mNeedUpdateGL = true;
//			System.err.println(this.getText()+":"+id);
			super.setGLid(id);
		}
	}
	
	public void onRecognizeRequiredNodes() {
		super.onRecognizeRequiredNodes();
		//if not called
		setFontSizeRate(1);
	}
	
	public void onDead() {
		super.onDead();
		mNeedUpdateGL = true;
		if(mTextureRegion != null) { 
			mTextureRegion.texture.invalid(); 
			mTextureRegion = null;
		} 
	} 
	
	static final Image sImageForSize = new Image(null, new ImageData(1, 1, 32, new PaletteData(0xff0000, 0xff00, 0xff)));
	protected String mText = "text";
	protected Image mImage;
	protected TextureRegion mTextureRegion = null;

	boolean mNeedUpdateGL = false;
	protected String mFontName = TextFont.getDefaultFont();
	
	protected int mTextSize = 21;

	TextStyle mTextStlye = TextStyle.Normal;

	protected TextAlign mAlign = TextAlign.CENTER;
	protected VerticalTextAlign mVerticlaAlign = VerticalTextAlign.CENTER;

	public VerticalTextAlign getVerticlaAlign() {
		return mVerticlaAlign;
	}
	public void setVerticlaAlign(VerticalTextAlign mVerticlaAlign) {
		if (mVerticlaAlign != null && this.mVerticlaAlign != mVerticlaAlign) {
			this.mVerticlaAlign = mVerticlaAlign;
			if (this.isUseDimension()) {
				mNeedUpdateGL = true;
			}
		}
	}
	protected final static int REF_VerticlaAlign = DEFAULT_SHIFT;

	protected int mTextWidth, mTextHeight;
	protected int mTextureWidth, mTextureHeight;

	public void setTextAlign(TextAlign align) {
		if (mAlign != align && align != null) {
			mAlign = align;
			if (!this.isUseDimension()) {
				switch (mAlign) {
				case LEFT:
					break;
				case CENTER:
					break; 
				case RIGHT: 
					break; 
				} 
			} else {
				mNeedUpdateGL = true;
			} 
		} 
	} 
	public TextAlign getTextAlign() { 
		return mAlign;
	}
	//reflect
    protected static final int REF_TextAlign =  DEFAULT_SHIFT;
    
	public void setTextSize(int size) {
		if (mTextSize != size) {
			mTextSize = size;
			updateText();
		}
	}
	public int getTextSize() {
		return mTextSize;
	} 
	//reflect
    protected static final int REF_TextSize =  DEFAULT_SHIFT;
    
	public void setTextFont(String font) { 
		if (!mFontName.endsWith(font) && TextFont.getFontIndex(font) >= 0) {
			mFontName = font;
			updateText();
		} 
	} 
	public String getTextFont() { 
		return mFontName;
	} 
	public static String [] arrayTextFont() { 
		return TextFont.toStrings();
	} 
	//reflect 
    protected static final int REF_TextFont =  DEFAULT_SHIFT;
    
//    private String mCustomFont;
//    public void setCustomFont(String font) {
//    	mCustomFont = font;
//    }
//    public String getCustomFont() { 
//    	return mCustomFont;
//    } 
//    protected final static int REF_CustomFont = DEFAULT_SHIFT;
	
	public void setTextStyle(TextStyle textStlye) {
		if (mTextStlye != textStlye && textStlye != null) {
			mTextStlye = textStlye;
			updateText();
		} 
	} 
	public TextStyle getTextStyle() { 
		return mTextStlye; 
	} 
	//reflect
    protected static final int REF_TextStyle =  DEFAULT_SHIFT;

	private final void updateText() { 
		mNeedUpdateGL = true; 
	} 

	public void setText(String text) {
		if (!mText.equals(text)) {
			mText = text;
			updateText();
		}
	}
	public String getText() {
		return mText;
	}
	//reflect
    protected static final int REF_Text =  DEFAULT_SHIFT;
    
    //text id 
    //id to string 
    
//	public void append(String text) {
//		mText = mText + text;
//		updateText();
//	}
	
	public void myRefresh() {
		super.myRefresh(); 
		refreshTexture(); 
	} 
	
	protected boolean mSingleLine = false;
	public void setSingleLine(boolean singleLine) {
		mSingleLine = singleLine;
	}
	public boolean getSingleLine() {
		return mSingleLine;
	}

	protected void refreshBasicTexture() {
		GC gc = new GC(sImageForSize); 
		final Font font = new Font(gc.getDevice(), mFontName, mTextSize, mTextStlye.styleValue); 
		
		gc.setFont(font); 
		gc.setAdvanced(true); 
		gc.setTextAntialias(SWT.ON); 
		
		float drawOffY = 0;
		
		if ( this.mDimensions.x > 0 &&  this.mDimensions.y > 0) { 
			mTextWidth = Math.round(mDimensions.x); 
			mTextHeight = Math.round(mDimensions.y); 
		} else if(mDimensions.x > 0) { 
			mTextWidth = Math.round(mDimensions.x); 
			if(mSingleLine) { 
				final int height = gc.textExtent(mText).y;
				mTextHeight = height; 
			} else { 
				final int height = gc.textExtent(" ").y;
				String[] lines = this.breakText(gc); 
				if(lines != null && lines.length > 0) { 
					mTextHeight = height * lines.length; 
				} else { 
					mTextHeight = height; 
				} 
			} 
		} else { 
			final Point point = gc.textExtent(mText);
			final int y = gc.textExtent(" ").y;
			mTextWidth = point.x > 0 ? point.x : 1; 
			mTextHeight = point.y > 0 ? point.y : 1; 
			
			if(mTextHeight < y) { 
				drawOffY = (y-mTextHeight)/2.0f;
				mTextHeight = y;
			} 
		} 

		mTextureWidth = GLHelper.minPow2(mTextWidth); 
		mTextureHeight = GLHelper.minPow2(mTextHeight); 

		gc.dispose();
		// gc.
		gc = null;

		if (mImage != null && !mImage.isDisposed()) {
			mImage.dispose();
			mImage = null;
		} 

		mImage = new Image(null, new ImageData(mTextureWidth, mTextureHeight, 32, new PaletteData(0xff0000, 0xff00, 0xff)));
		gc = new GC(mImage);
		gc.setFont(font);
		gc.setAdvanced(true);
		if(mTextAntialias) {
			gc.setTextAntialias(SWT.ON);
		} else {
			gc.setTextAntialias(SWT.OFF);
		}
		
		gc.setBackground(new Color(null, 0, 0, 0));
		gc.setForeground(new Color(null, 255, 255, 255));

		final TextAlign hAlign;
		final VerticalTextAlign vAlign;
		if (this.isUseDimension()) {
			hAlign = this.mAlign;
			vAlign = this.mVerticlaAlign;
		} else { 
			hAlign = TextAlign.CENTER;
			vAlign = VerticalTextAlign.CENTER;
		} 

		// draw
		{
//			final int height = gc.textExtent(" ").y; 
			final int height = gc.getFontMetrics().getHeight();
			String[] lines = this.breakText(gc); 
			drawText(lines, vAlign, hAlign, height, drawOffY, gc, 0, 0); 
		} 
		
		gc.dispose();

		final ImageData imageData = mImage.getImageData(); 
		for (int i = 0; i < mTextureWidth; i++) { 
			for (int j = 0; j < mTextureHeight; j++) { 
				final int pix = imageData.getPixel(i, j); 
				RGB rgb = imageData.palette.getRGB(pix); 
				if (rgb.blue == 0 && rgb.red == 0 && rgb.green == 0) { 
					imageData.setAlpha(i, j, 0); 
				} else { 
					imageData.setAlpha(i, j, 255); 
				} 
			} 
		} 
		
		if (mTextureRegion != null) {
			mTextureRegion.texture.invalid();
			GLManage.checkOut(mTextureRegion.texture);
			
		} 

		final Texture texture = new Texture(imageData);
		texture.load();
		mTextureRegion = new TextureRegion(texture, 0, 0, mTextWidth, mTextHeight);
		if (mImage != null && !mImage.isDisposed()) { 
			mImage.dispose(); 
		} 
		
		mImage = null;
		font.dispose();
	} 
	
	protected boolean mTextAntialias = true;
	public void setTextAntialias(boolean ant) {
		if(mTextAntialias != ant) {
			mTextAntialias = ant; 
			mNeedUpdateGL = true;
		} 
	} 
	public boolean getTextAntialias() {
		return mTextAntialias;
	}
	protected final static int REF_TextAntialias = DEFAULT_SHIFT;
	
	protected void drawSelf() { 
		if (mNeedUpdateGL) { 
			mNeedUpdateGL = false; 
			refreshTexture(); 
		} 
		if (mTextureRegion != null) { 
			mTextureRegion.draw(); 
		} 
	} 
	
	public ElfPointf getSize() {
		if(this.getUseSettedSize()) {
			return super.getSize();
		} else if (mTextureRegion != null) { 
			return new ElfPointf( mTextureRegion.getWidth(), mTextureRegion.getHeight() );
		} else { 
			return new ElfPointf(100, 100);
		} 
	}
	
	public void setSize(final float width, final float height) {
		super.setSize(width, height);
		this.mNeedUpdateGL = true;
	} 

	public void setUseSettedSize(boolean use) { 
		super.setUseSettedSize(use); 
		this.mNeedUpdateGL = true; 
	} 

	protected final ElfPointf mDimensions = new ElfPointf();
	public void setDimensions(ElfPointf dimensions) {
		if(dimensions != null && (dimensions.x != mDimensions.x || dimensions.y != mDimensions.y)) {
			mDimensions.set(dimensions); 
			if(mDimensions.x > 0 && mDimensions.y > 0) {
				this.setSize(mDimensions); 
			} else { 
			} 
			updateText();
		} 
	} 
	public ElfPointf getDimensions() { 
		return new ElfPointf(mDimensions);
	} 
	protected final static int REF_Dimensions = DEFAULT_SHIFT;
	
	public float getFontSizeRate() { 
		return TextFont.getDefaultFontSizeRate();
	}
	private boolean mSetFontRateCalled = false; 
	public void setFontSizeRate(float fontRate) { 
		if(!mSetFontRateCalled) { 
			mSetFontRateCalled = true; 
			final int newFontSize = Math.round(getTextSize()*fontRate/getFontSizeRate());
			this.setTextSize(newFontSize);
		}
	} 
	protected final static int REF_FontSizeRate = XML_ALL_SHIFT; 
	
	protected int mMaxDynamicWidth = -1;
	public void setMaxDynamicWidth(int width) { 
		mMaxDynamicWidth = width;
	} 
	public int getMaxDynamicWidth() {
		return mMaxDynamicWidth;
	}
	protected final static int REF_MaxDynamicWidth = XML_ALL_SHIFT;
	
	protected boolean mAutoApostrophe = true;
	public void setAutoApostrophe(boolean apostrophe) {
		mAutoApostrophe = apostrophe;
	}
	public boolean getAutoApostrophe() {
		return mAutoApostrophe;
	}
	protected final static int REF_AutoApostrophe = XML_ALL_SHIFT; 
	
	boolean isUseDimension() { 
		return mDimensions.x > 0; 
	} 

	public String[] breakText(GC gc) { 
		String text = this.mText; 
		text = text.replace("\r", ""); 
		String[] lines = text.split("\n"); 
		
		if (!isUseDimension()) { 
			return lines; 
		} else { 
			final ArrayList<String> strList = new ArrayList<String>();
			for (int i = 0; i < lines.length; i++) {
				final String aline = lines[i];
				strList.addAll(breakText(gc, aline, (int)(getDimensions().x)));
			} 

			lines = new String[strList.size()];
			strList.toArray(lines);
		} 
		return lines;
	};

	public static List<String> breakText(final GC gc, final String text, final int width) {
		final ArrayList<String> strList = new ArrayList<String>();
		final int strlen = text.length();

		int lastNotLetterIndex = -1;
		for (int i = 0; i < strlen; i++) {
			Point size = gc.stringExtent(text.substring(0, i + 1));
			lastNotLetterIndex = i; 
			
			if (size.x > width) {
				if (lastNotLetterIndex > 0) { 
					strList.add(text.substring(0, lastNotLetterIndex)); 
					
					final List<String> subs = breakText(gc, text.substring(lastNotLetterIndex), width); 
					strList.addAll(subs); 
					
					return strList; 
				} else { 
				} 
			} 
		} 
		strList.add(text); 
		return strList; 
	} 
	
	public static void main(String [] args) {
		GC gc = new GC(sImageForSize); 
		final Font font = new Font(gc.getDevice(), "@Arial Unicode MS", 20, TextStyle.Normal.styleValue); 
		gc.setFont(font); 
		gc.setAdvanced(true); 
		gc.setTextAntialias(SWT.ON); 
		
		final String text = "abcdefg,中文测试,\n";
		final int size = text.length();
		int sum = 0;
		for(int i=0; i<size; i++) {
			final char c = text.charAt(i);
			final Point p = gc.stringExtent(String.valueOf(c)); 
			System.err.println("char "+c+" size:"+p.x+","+p.y);
			sum += p.x;
		} 
		
		final Point p = gc.textExtent(text); 
		System.err.println("text "+text+" size:"+p.x+","+p.y); 
		System.err.println("sum "+sum);
		gc.dispose();
		font.dispose();
	}
	
	protected void drawText(String[] lines, VerticalTextAlign vAlign, TextAlign hAlign, float height, float drawOffY, GC gc,
			int xOff, int yOff) {
		for (int i = 0; i < lines.length; i++) {
			final float y;
			if (vAlign == VerticalTextAlign.TOP) {
				y = height * i;
			} else if (vAlign == VerticalTextAlign.BOTTOM) {
				y = this.mTextHeight - (lines.length) * height + height * i;
			} else {
				y = this.mTextHeight / 2f + (i - lines.length / 2f) * height;
			} 

			final float w = gc.textExtent(lines[i]).x;
			final float x;
			if (hAlign == TextAlign.LEFT) {
				x = 0;
			} else if (hAlign == TextAlign.CENTER) { 
				x = this.mTextWidth / 2f - w / 2f;
			} else {
				x = this.mTextWidth - w;
			}
			gc.drawText(lines[i], (int) x+xOff, (int) (y+drawOffY)+yOff, true);
		} 
	}
	
	private boolean mEnableStroke;
	private int mStrokeSize;
	private final ElfColor mStrokeColor = new ElfColor();
	private final ElfColor mFillColor = new ElfColor();
	
	private boolean mEnableShadow;
	private final ElfPointf mShadowOffset = new ElfPointf();
	private float mShadowOpacity;
	private float mShadowBlur;
	
	public boolean getEnableStroke() {
		return mEnableStroke;
	}
	public void setEnableStroke(boolean mEnableStroke) {
		this.mEnableStroke = mEnableStroke;
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_EnableStroke = DEFAULT_SHIFT;
	
	public int getStrokeSize() {
		return mStrokeSize;
	}
	public void setStrokeSize(int mStrokeSize) {
		if(mStrokeSize >= 0) {
			this.mStrokeSize = mStrokeSize;
			this.mNeedUpdateGL = true;
		}
	}
	protected final static int REF_StrokeSize = DEFAULT_SHIFT;
	
	public ElfColor getStrokeColor() {
		return new ElfColor(mStrokeColor);
	}
	public void setStrokeColor(ElfColor mStrokeColor) {
		this.mStrokeColor.set(mStrokeColor);
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_StrokeColor = DEFAULT_SHIFT;
	
	public ElfColor getFillColor() {
		return new ElfColor(mFillColor);
	}
	public void setFillColor(ElfColor mFillColor) {
		this.mFillColor.set(mFillColor);
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_FillColor = DEFAULT_SHIFT;
	
	public boolean getEnableShadow() {
		return mEnableShadow;
	}
	public void setEnableShadow(boolean mEnableShadow) {
		this.mEnableShadow = mEnableShadow;
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_EnableShadow = XML_ALL_SHIFT;
	
	public ElfPointf getShadowOffset() {
		return new ElfPointf(mShadowOffset);
	}
	public void setShadowOffset(ElfPointf mShadowOffset) {
		this.mShadowOffset.set(mShadowOffset);
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_ShadowOffset = XML_ALL_SHIFT;
	
	public float getShadowOpacity() {
		return mShadowOpacity;
	}
	public void setShadowOpacity(float mShadowOpacity) {
		this.mShadowOpacity = mShadowOpacity;
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_ShadowOpacity = XML_ALL_SHIFT;
	
	public float getShadowBlur() {
		return mShadowBlur;
	}
	public void setShadowBlur(float mShadowBlur) {
		this.mShadowBlur = mShadowBlur;
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_ShadowBlur = XML_ALL_SHIFT;
	
	protected void refreshTexture() { 
		if(!mEnableStroke) { 
			this.refreshBasicTexture(); 
			return;
		} 
		
		GC gc = new GC(sImageForSize); 
		final Font font = new Font(gc.getDevice(), mFontName, mTextSize, mTextStlye.styleValue); 
		gc.setFont(font); 
		gc.setAdvanced(true); 
		gc.setTextAntialias(SWT.ON); 
		
		float drawOffY = 0;
		
		if ( this.mDimensions.x > 0 &&  this.mDimensions.y > 0) { 
			mTextWidth = Math.round(mDimensions.x); 
			mTextHeight = Math.round(mDimensions.y); 
		} else if(mDimensions.x > 0) { 
			mTextWidth = Math.round(mDimensions.x); 
			if(mSingleLine) { 
				final int height = gc.textExtent(mText).y;
				mTextHeight = height; 
			} else { 
				final int height = gc.textExtent(" ").y;
				String[] lines = this.breakText(gc); 
				if(lines != null && lines.length > 0) { 
					mTextHeight = height * lines.length; 
				} else { 
					mTextHeight = height; 
				} 
			} 
		} else { 
			final Point point = gc.textExtent(mText);
			final int y = gc.textExtent(" ").y;
			mTextWidth = point.x > 0 ? point.x : 1; 
			mTextHeight = point.y > 0 ? point.y : 1; 
			
			if(mTextHeight < y) { 
				drawOffY = (y-mTextHeight)/2.0f;
				mTextHeight = y;
			} 
		} 
		
		final int newTextWidth = mTextWidth + 2*mStrokeSize;
		final int newTextHeight = mTextHeight + 2*mStrokeSize;
		
		mTextureWidth = GLHelper.minPow2(newTextWidth); 
		mTextureHeight = GLHelper.minPow2(newTextHeight); 
		
		gc.dispose();
		// gc.
		gc = null;

		if (mImage != null && !mImage.isDisposed()) {
			mImage.dispose();
			mImage = null;
		} 
		
		mImage = new Image(null, new ImageData(mTextureWidth, mTextureHeight, 32, new PaletteData(0xff0000, 0xff00, 0xff)));
		gc = new GC(mImage);
		gc.setFont(font);
		gc.setAdvanced(true);
		if(mTextAntialias) {
			gc.setTextAntialias(SWT.ON);
		} else {
			gc.setTextAntialias(SWT.OFF);
		}
		
		final TextAlign hAlign;
		final VerticalTextAlign vAlign;
		if (this.isUseDimension()) {
			hAlign = this.mAlign;
			vAlign = this.mVerticlaAlign;
		} else { 
			hAlign = TextAlign.CENTER;
			vAlign = VerticalTextAlign.CENTER;
		} 

		// draw
		{
			final int height = gc.textExtent(" ").y;
			String[] lines = this.breakText(gc);
			
			gc.setBackground(new Color(null, 0, 0, 0));
			
			if(mStrokeSize > 0) {
				gc.setForeground(new Color(null, 
						Math.min(Math.max(Math.round(255*mStrokeColor.red),0),255), 
						Math.min(Math.max(Math.round(255*mStrokeColor.green),0),255), 
						Math.min(Math.max(Math.round(255*mStrokeColor.blue),0),255)));
				
				for (int i=0; i<360; i+=5) {
			         int x= Math.round((float)Math.sin((float)(Math.PI)*i/180f)*mStrokeSize);
			         int y= Math.round((float)Math.cos((float)(Math.PI)*i/180f)*mStrokeSize);
			         drawText(lines, vAlign, hAlign, height, drawOffY, gc, x+mStrokeSize, y+mStrokeSize);
			    } 
			} 
			
			gc.setForeground(new Color(null, 
					Math.min(Math.max(Math.round(255*mFillColor.red),0),255), 
					Math.min(Math.max(Math.round(255*mFillColor.green),0),255), 
					Math.min(Math.max(Math.round(255*mFillColor.blue),0),255)));
			drawText(lines, vAlign, hAlign, height, drawOffY, gc, mStrokeSize, mStrokeSize);
		} 
		
		gc.dispose();

		final ImageData imageData = mImage.getImageData(); 
		for (int i = 0; i < mTextureWidth; i++) { 
			for (int j = 0; j < mTextureHeight; j++) { 
				final int pix = imageData.getPixel(i, j); 
				RGB rgb = imageData.palette.getRGB(pix); 
				if (rgb.blue == 0 && rgb.red == 0 && rgb.green == 0) { 
					imageData.setAlpha(i, j, 0); 
				} else { 
					imageData.setAlpha(i, j, 255); 
				} 
			} 
		} 

		if (mTextureRegion != null) {
			mTextureRegion.texture.invalid();
			GLManage.checkOut(mTextureRegion.texture);
		} 

		final Texture texture = new Texture(imageData);
		texture.load();
		mTextureRegion = new TextureRegion(texture, 0, 0, newTextWidth, newTextHeight);
		if (mImage != null && !mImage.isDisposed()) { 
			mImage.dispose(); 
		} 
		
		mImage = null;
		font.dispose();
	} 
}
