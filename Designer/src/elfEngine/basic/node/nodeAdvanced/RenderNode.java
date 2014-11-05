package elfEngine.basic.node.nodeAdvanced;

import org.eclipse.swt.graphics.ImageData;
import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.bitmapTool.ImageHelper;
import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.Texture;
import elfEngine.opengl.TextureRegion;

public class RenderNode extends ElfNode{

	private TextureRegion mTextureRegion = null;
	private final ElfColor mClearColor = new ElfColor(0,0,0,0);
	
	public RenderNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	} 
	
	public void onDead() {
		super.onDead();
		if(mTextureRegion != null) { 
			mTextureRegion.texture.invalid();
			mTextureRegion = null; 
		} 
	} 
	
	public void calc(float t) { 
		super.calc(t); 
		
		if(mRenderEnable) { 
			final int width = Math.round(this.getWidth()); 
			final int height = Math.round(this.getHeight()); 
			GL11.glClearColor(mClearColor.red, mClearColor.green, mClearColor.blue, mClearColor.alpha); 
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); 
			
			GL11.glPushMatrix(); 
			
			GL11.glTranslatef(this.getWidth()/2, this.getHeight()/2, 0); 
			GL11.glScalef(1, -1, 1); 
			drawChilds(); 
			GL11.glPopMatrix(); 
			
			final TextureRegion tr = GLManage.loadTextureRegion(this.getResid(), mGLid); 
			if(tr != null) { 
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, tr.texture.getGLBindId()); 
				GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height); 
			} else if(mTextureRegion != null) {
				if( mTextureRegion.texture.getWidth() != width ||  mTextureRegion.texture.getHeight() != height) { 
					mTextureRegion.texture.invalid();
					
					final ImageData data = ImageHelper.create(width, height); 
					final Texture texture = new Texture(data); 
					texture.load(); 
					mTextureRegion = new TextureRegion(texture); 
				} 
				
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, mTextureRegion.texture.getGLBindId()); 
				GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height); 
			} else {
				final ImageData data = ImageHelper.create(width, height); 
				final Texture texture = new Texture(data); 
				texture.load(); 
				mTextureRegion = new TextureRegion(texture); 
				
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, mTextureRegion.texture.getGLBindId()); 
				GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height); 
			} 
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); 
		} 
	} 
	
	protected boolean mRenderEnable = true; 
	public boolean getRenderEnable() {
		return mRenderEnable;
	}
	public void setRenderEnable(boolean mRenderEnable) {
		this.mRenderEnable = mRenderEnable;
	}
	protected final static int REF_RenderEnable = DEFAULT_SHIFT; 
	
	public void drawSprite() { 
		if (mVisible) { 
			drawBefore(); 
			final TextureRegion tr = GLManage.loadTextureRegion(this.getResid(), mGLid); 
			if(tr != null) {
				drawSelf(); 
			} else if(mTextureRegion != null) {
				mTextureRegion.draw();
			}
			//no children
			drawAfter();
		} 
	} 
} 
