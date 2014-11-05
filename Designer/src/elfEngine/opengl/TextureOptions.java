package elfEngine.opengl;

import org.lwjgl.opengl.GL11;


public class TextureOptions {
	
//	static final int GL_CLAMP_TO_EDGE = 0x812F;
	
	public static final TextureOptions NEAREST = new TextureOptions(GL11.GL_NEAREST, GL11.GL_NEAREST, GL11.GL_CLAMP, GL11.GL_CLAMP, GL11.GL_MODULATE, false);
	public static final TextureOptions BILINEAR = new TextureOptions(GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_CLAMP, GL11.GL_CLAMP, GL11.GL_MODULATE, false);
	public static final TextureOptions REPEATING = new TextureOptions(GL11.GL_NEAREST, GL11.GL_NEAREST, GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_MODULATE, false);
	public static final TextureOptions REPEATING_BILINEAR = new TextureOptions(GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_MODULATE, false);

	public static final TextureOptions NEAREST_PREMULTIPLYALPHA = new TextureOptions(GL11.GL_NEAREST, GL11.GL_NEAREST, GL11.GL_CLAMP, GL11.GL_CLAMP, GL11.GL_MODULATE, true);
	public static final TextureOptions BILINEAR_PREMULTIPLYALPHA = new TextureOptions(GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_CLAMP, GL11.GL_CLAMP, GL11.GL_MODULATE, true);
	public static final TextureOptions REPEATING_PREMULTIPLYALPHA = new TextureOptions(GL11.GL_NEAREST, GL11.GL_NEAREST, GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_MODULATE, true);
	public static final TextureOptions REPEATING_BILINEAR_PREMULTIPLYALPHA = new TextureOptions(GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_MODULATE, true);

	public static final TextureOptions DEFAULT = new TextureOptions(GL11.GL_LINEAR, GL11.GL_NEAREST, GL11.GL_CLAMP, GL11.GL_CLAMP, GL11.GL_MODULATE, false); 
	
	public final int mMagFilter;
	public final int mMinFilter;
	public final int mWrapT;
	public final int mWrapS;
	public final int mTextureEnvironment;
	public final boolean mPreMultipyAlpha;

	public TextureOptions(final int pMinFilter, final int pMagFilter, final int pWrapT, final int pWrapS, final int pTextureEnvironment, final boolean pPreMultiplyAlpha) {
		this.mMinFilter = pMinFilter;
		this.mMagFilter = pMagFilter;
		this.mWrapT = pWrapT;
		this.mWrapS = pWrapS;
		this.mTextureEnvironment = pTextureEnvironment;
		this.mPreMultipyAlpha = pPreMultiplyAlpha;
	}
}
