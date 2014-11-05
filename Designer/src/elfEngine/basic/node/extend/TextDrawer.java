package elfEngine.basic.node.extend;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.ielfgame.stupidGame.Redirect;

import elfEngine.opengl.GLHelper;
import elfEngine.opengl.Texture;
import elfEngine.opengl.TextureRegion;

public class TextDrawer {
	protected String mText = "";
	protected Image mImage = new Image(null, new ImageData(1, 1, 32, new PaletteData (0xff0000, 0xff00, 0xff)));
	
	protected TextureRegion mTextureRegion = null;
	
	boolean mNeedUpdateGL = false;
	
	//Font
	protected String mFontName;
	protected int mFontHeight = 20, mFontStyleIndex = 0, mFontNameIndex = 0;
	
	public final static int [] sStyleValues = new int [] {SWT.NORMAL, SWT.ITALIC, SWT.BOLD, SWT.BOLD | SWT.ITALIC};
	public final static ArrayList<String> sFontNameList = new ArrayList<String>();
	
	static {
		FontData [] fontDatas = Display.getCurrent().getFontList(null, true);
		for (int i=0; i < fontDatas.length; i++) {
			// remove duplicates and sort
			String nextName = fontDatas[i].getName();
			if (!sFontNameList.contains(nextName)) {
				int j = 0;
				while(j < sFontNameList.size() && nextName.compareTo((String)sFontNameList.get(j)) > 0) {
					j++;
				}
				sFontNameList.add(j, nextName);
			}
		}
		
		for(String str:sFontNameList){
			Redirect.outPrintln(str);
		}
	}
	
	public enum Align{
		LEFT, CENTER, RIGHT
	}
	private Align mAlign = Align.LEFT;
		
	public void setAlign(Align align){
		mAlign = align;
	}
	
	public void setTextSize(int size){
		if(mFontHeight != size){
			mFontHeight = size;
			updateText();
		}
	}
	
	public int getTextSize(){
		return mFontHeight;
	}
	
	public void setFont(int index){
		if(index < 0 || index >= sFontNameList.size()){
			index = 0;
		}
		mFontNameIndex = index;
	}
	
	public void setFont(String name){
		int index = sFontNameList.indexOf(name);
		if(index < 0 || index >= sFontNameList.size()){
			index = 0;
		}
		mFontNameIndex = index;
	}
	
	public void setFontStyle(int index){
		if(index < 0 || index >= sStyleValues.length){
			index = 0;
		}
		mFontStyleIndex = index;
	}
	
	private final void updateText(){
		GC gc = new GC(mImage);
		Font font = new Font(gc.getDevice(), sFontNameList.get(mFontNameIndex), mFontHeight, sStyleValues[mFontStyleIndex]);
		gc.setFont(font);
		gc.setTextAntialias(SWT.ON);
		
		final Point point = gc.stringExtent(mText);
		final int width = point.x;
		final int height = point.y;
		
		mImage = new Image(null, new ImageData(width, height, 32, new PaletteData (0xff0000, 0xff00, 0xff)));
		gc = new GC(mImage);
		gc.setFont(font);
		gc.setTextAntialias(SWT.ON);
		
		gc.setBackground(new Color(null, 0, 0, 0));
		gc.setForeground(new Color(null, 255, 255, 255));
		gc.drawText(mText, 0, 0, true);
		gc.dispose();
		
		if(mTextureRegion != null){
			mTextureRegion.texture.invalid();
		}
		
		mNeedUpdateGL = true;
	}
	
	public void setText(String text){
		if(!mText.equals(text)){
			mText = text;
			
			updateText();
		}
	}
	
	public void append(String text){
		mText = mText + text;
		updateText();
	}
	
	public void drawText(){			
		if(mNeedUpdateGL){
			mNeedUpdateGL = false;
			final Texture texture = new Texture(mImage.getImageData());
			texture.load();
			mTextureRegion = new TextureRegion(texture);
		}
		
		if(mTextureRegion != null){
			GLHelper.glPushMatrix();
			switch (mAlign){
			case LEFT:
				GLHelper.glTranslatef(mTextureRegion.getWidth()/2, 0, 0);
				break;
			case RIGHT:
				GLHelper.glTranslatef(-mTextureRegion.getWidth()/2, 0, 0);
				break;
			}
			
			mTextureRegion.draw();
			GLHelper.glPopMatrix();
		}
	}
}
