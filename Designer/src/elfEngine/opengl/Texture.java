package elfEngine.opengl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.lwjgl.opengl.GL11;

import elfEngine.basic.debug.Debug;

public class Texture implements ITexture{
	//GL11.GL_INVALID_VALUE
	
	private int mGLBindId = 0;
	private int mWidth, mHeight;
	private int mPower2Width, mPower2Height;
	
	public final static int IMAGE_GRAY = 1;
	public final static int IMAGE_UNDEFINED = 0;
	
	public static final int [] IMAGE_MODES = { IMAGE_UNDEFINED, IMAGE_GRAY };
	
	private int mImageMode = IMAGE_UNDEFINED;
	
	public void setImageMode(int mode) {
		mImageMode = mode;
	}
	public int getImageMode() { 
		return mImageMode; 
	} 
	
	static {
	}
	
	private static TextureOptions textureOptions = TextureOptions.DEFAULT;
	
	public static void setTextureOptions(TextureOptions textureOptions) {
		Texture.textureOptions = textureOptions;
	}
	
	private final String mKey;
	private final InputStream mStream ;
	private final ImageData mImageData;
	
	public Texture(final String key){
		mKey = key;
		mStream = null;
		mImageData = null;
		
//		GLManage.checkIn(this);
	}
	
	public Texture(InputStream stream){
		mKey = null;
		mStream = stream;
		mImageData = null;
	}
	
	//Text etc...
	public Texture(ImageData imageData){
		mKey = null;
		mStream = null;
		mImageData = imageData;
		
//		GLManage.checkIn(this);
	} 
	
	
	public int getGLBindId(){
		return mGLBindId;
	}
	
	public boolean isGLBindIdValid(){
		return mGLBindId != 0;
	}
	
	public int getWidth(){
		return mWidth;
	}
	
	public int getHeight(){
		return mHeight;
	}
	
	public int getPower2Width(){
		return mPower2Width;
	}
	
	public int getPower2Height(){
		return mPower2Height;
	}
	
	public boolean load(){ 
		GL11.glDeleteTextures(mGLBindId);
		mGLBindId = GL11.glGenTextures();
		
//		if(mGLBindId == GL11.GL_INVALID_VALUE) {
////			System.err.println("glGenTextures error");
////			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mGLBindId);
//			return false;
//		} 
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mGLBindId);
		
		ImageData source = null;
		if(mKey != null){ 
			final File file = new File(mKey);
			if(!file.exists()) { 
				mGLBindId = 0;
				return false;
			} 
			
			try {
				final FileInputStream stream = new FileInputStream(file);
				source = new ImageData(stream);
				
				try { 
					stream.close(); 
				} catch (IOException e) { 
					System.err.println("error:"+file.getAbsolutePath());
					e.printStackTrace(); 
				} 
			} catch (FileNotFoundException e) {
				mGLBindId = 0;
				return false;
			}
		} else if(mStream != null){
			try{
				source = new ImageData(mStream);
				mStream.close(); 
			} catch (Exception e) {
				e.printStackTrace();
				mGLBindId = 0;
				return false;
			} 
		} else if(mImageData != null){
			source = mImageData;
		} else {
			Debug.e("Texture Error No Key No Stream!");
			mGLBindId = 0;
			return false;
		}
		
		if(source.width <=0 || source.height <= 0 ){
			mGLBindId = 0;
			return false;
		} 
		
		mWidth = source.width;
		mHeight = source.height;
		
		mPower2Width = GLHelper.minPow2(source.width);
		mPower2Height = GLHelper.minPow2(source.height);
		
		if(mImageMode == IMAGE_GRAY) {
			Image image = new Image(null, source);
			image = new Image(null, image, SWT.IMAGE_GRAY);
			source = image.getImageData(); 
//			SWT.IMAGE_
			
		} 
//		else if(mImageMode == IMAGE_DISABLE) {
//			Image image = new Image(null, source);
//			image = new Image(null, image, SWT.IMAGE_DISABLE);
//			source = image.getImageData(); 
//		} 
		
		final ByteBuffer sByteBuffer = BufferHelper.sByteBuffer;
		sByteBuffer.clear();
		
		for(int y=0;y<mPower2Height; y++){ 
			for(int x=0; x<mPower2Width; x++){ 
				if(x < mWidth && y < mHeight){
					int index = (y*mPower2Width + x) * 4;
					
					int pixel = source.getPixel(x, y);
					RGB rgb = source.palette.getRGB(pixel);
					boolean hasAlpha = false;
					int alphaValue = 0;
					if (source.alphaData != null && source.alphaData.length > 0) {
						hasAlpha = true;
						alphaValue = source.getAlpha(x, y);
					} 
					
					sByteBuffer.position(index);
					sByteBuffer.put((byte)(rgb.red));
					sByteBuffer.put((byte)(rgb.green));
					sByteBuffer.put((byte)(rgb.blue));
					
					if(hasAlpha){
						sByteBuffer.put((byte)(alphaValue));
					} else {
						sByteBuffer.put((byte)(255));
					} 
					
				} else {
					final int index = (y * mPower2Width + x) * 4;
					sByteBuffer.position(index);
					sByteBuffer.put((byte) 0);
					sByteBuffer.put((byte) 0);
					sByteBuffer.put((byte) 0);
					sByteBuffer.put((byte) 0);
				}
			}
		}
		
		sByteBuffer.flip();
		
		GL11.glTexImage2D(
			GL11.GL_TEXTURE_2D, 0, 4, 
			mPower2Width, mPower2Height, 0,
			GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, sByteBuffer);
		
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
 textureOptions.mMinFilter);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				textureOptions.mMagFilter);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S,
				textureOptions.mWrapS);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
				textureOptions.mWrapT);
		
		return true;
	}
	
	public void invalid() {
		if(mGLBindId != 0){
			GL11.glDeleteTextures(mGLBindId); 
			mGLBindId = 0; 
		} 
	}
	
	public void dispose() {
		this.invalid();
	}
	
	@Override
	public Texture getTexture() {
		return this;
	}
	@Override
	public float getU() {
		return 0;
	}
	@Override
	public float getV() {
		return 0;
	}
	@Override
	public float getU2() {
		return 1f;
	}
	@Override
	public float getV2() {
		return 1f;
	}
}
