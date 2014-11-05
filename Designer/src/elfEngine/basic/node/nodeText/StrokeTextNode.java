package elfEngine.basic.node.nodeText;

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
import com.ielfgame.stupidGame.enumTypes.TextAlign;
import com.ielfgame.stupidGame.enumTypes.VerticalTextAlign;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.Texture;
import elfEngine.opengl.TextureRegion;

public class StrokeTextNode extends TextNode{
	
	public StrokeTextNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
	} 
	
	protected int mStrokeSize = 2;
	protected final ElfColor mStrokeInColor = new ElfColor(1,1,1,1);
	protected final ElfColor mStrokeOutColor = new ElfColor(0,0,1,1);
	
	public int getStrokeSize() {
		return mStrokeSize;
	}
	public void setStrokeSize(int mStrokeSize) {
		this.mStrokeSize = Math.max(mStrokeSize,0);
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_StrokeSize = DEFAULT_SHIFT;
	
	public ElfColor getStrokeInColor() {
		return mStrokeInColor;
	}
	public void setStrokeInColor(ElfColor color) {
		mStrokeInColor.set(color);
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_StrokeInColor = DEFAULT_SHIFT;

	public ElfColor getStrokeOutColor() {
		return mStrokeOutColor;
	}
	public void setStrokeOutColor(ElfColor color) {
		mStrokeOutColor.set(color);
		this.mNeedUpdateGL = true;
	}
	protected final static int REF_StrokeOutColor = DEFAULT_SHIFT;

	protected void refreshTexture() {
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
						Math.min(Math.max(Math.round(255*mStrokeOutColor.red),0),255), 
						Math.min(Math.max(Math.round(255*mStrokeOutColor.green),0),255), 
						Math.min(Math.max(Math.round(255*mStrokeOutColor.blue),0),255)));
				
				for (int i=0; i<360; i+=5) {
			         int x= Math.round((float)Math.sin((float)(Math.PI)*i/180f)*mStrokeSize);
			         int y= Math.round((float)Math.cos((float)(Math.PI)*i/180f)*mStrokeSize);
			         drawText(lines, vAlign, hAlign, height, drawOffY, gc, x+mStrokeSize, y+mStrokeSize);
			    } 
			} 
			
			gc.setForeground(new Color(null, 
					Math.min(Math.max(Math.round(255*mStrokeInColor.red),0),255), 
					Math.min(Math.max(Math.round(255*mStrokeInColor.green),0),255), 
					Math.min(Math.max(Math.round(255*mStrokeInColor.blue),0),255)));
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
