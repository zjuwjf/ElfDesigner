package elfEngine.basic.node;


import java.io.File;
import java.io.IOException;

import mdesl.graphics.ITexture;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.Texture;

import org.lwjgl.LWJGLException;

import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.math.MathCons;
import elfEngine.basic.node.particle.modifier.MathHelper;
import elfEngine.basic.pool.ArrayPool;
import elfEngine.opengl.BlendMode;

public class BatchNode extends ElfNode{

	public BatchNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	}
	
	private ITexture mTexture;
	private String mBathResid = "";
	public void setBathResid(String resid) {
		mBathResid = resid;
		try {
			mTexture = new Texture(new File(resid).toURI().toURL(), Texture.LINEAR, Texture.REPEAT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getBathResid() {
		return mBathResid;
	}
	protected final static int REF_BathResid = DEFAULT_SHIFT | DROP_RESID_SHIFT;
	
	private BlendMode mBathBlendMode = BlendMode.BLEND;
	public void setBathBlendMode(BlendMode mode) {
		mBathBlendMode = mode;
	}
	public BlendMode getBathBlendMode() {
		return mBathBlendMode;
	}
	protected final static int REF_BathBlendMode = DEFAULT_SHIFT;
	
	private boolean mBatchRender = true;
	public void setBatchRender(boolean b) {
		mBatchRender = b;
	}
	public boolean getBatchRender() {
		return mBatchRender;
	}
	protected final static int REF_BatchRender = DEFAULT_SHIFT;
	
	static final int MAX_BATCH_CHILDS = 5000;
	
	
	static SpriteBatch spriteBatch;
	
	boolean sHasInited = false;
	
	public void drawSprite() { 
		if(!mBatchRender) {
			super.drawSprite();
			return;
		}
		
		if (mVisible) { 
			drawBefore(); 
			drawSelf(); 
			
			if(mTexture != null) {
				final ElfNode [] childs = this.getBatchChilds();
				final int childSize = Math.min(childs.length, MAX_BATCH_CHILDS);
				if(childSize > 0) {
					if(!sHasInited) {
						sHasInited = true;
						try {
							spriteBatch = new SpriteBatch(MAX_BATCH_CHILDS);
							spriteBatch.resize(960, 640);
						} catch (LWJGLException e) {
							e.printStackTrace();
						} 
					}
					
					spriteBatch.begin();
					
					for(int i=0; i<childSize; i++) {
						final ElfNode node = childs[i];
						final float x = node.getPosition().x;
						final float y = node.getPosition().y;
						final ElfPointf scale = node.getScale();
						
						final float width = mTexture.getWidth() * scale.x;
						final float height = mTexture.getHeight() * scale.y;
						
						final float rotationRadians = node.getRotate() * MathCons.RAD_TO_DEG;
						
						final ElfColor color = node.getColor();
						
						spriteBatch.setColor(color.red, color.green, color.blue, color.alpha);
						spriteBatch.draw(mTexture, x, y, width, height, 0, 0, rotationRadians);
					}
					
					spriteBatch.end();
				}
			}
			
			drawAfter(); 
		}
	} 
	
	public ElfNode [] getBatchChilds() {
		return this.getChilds();
	}
	
	/***
	 * 0, 1
	 * 3, 2
	 */
	final static float [] sTexCoords = new float[]{0,0, 1,0, 1,1, 0,1};
	
	/***
	 * 
	 * @param node
	 * @param unitWidth
	 * @param unitHeight
	 * @param unitRed
	 * @param unitGreen
	 * @param unitBlue
	 * @param unitAlpha
	 * @return
	 */
	
	public static final float [] getVerticeColorsTexCoords(final ElfNode node, 
			float unitWidth, float unitHeight, float unitRed, float unitGreen, float unitBlue, float unitAlpha) {
		//vertices 3 * 4 = 12
		//texCoords 2*4 = 8
		//colors   4 * 4 = 16
		//sum = 36
		final float [] ret = ArrayPool.float36;
		
		final float rotate = node.getRotate() * MathCons.RAD_TO_DEG ;
		final ElfPointf scale = node.getScale();
		final float scaleX = scale.x;
		final float scaleY = scale.y;
		final ElfPointf position = node.getPosition();
		final float x = position.x;
		final float y = position.y;
		final float z = node.getZ();
		
		unitWidth *= scaleX;
		unitHeight *= scaleY;
		
		final float cos = MathHelper.cos(rotate);
		final float sin = MathHelper.sin(rotate);
		
		final float ltX = (-unitWidth)*cos + unitHeight*sin;
		final float ltY = (unitWidth)*sin + unitHeight*cos;
		
		final float lbX = (-unitWidth)*cos - unitHeight*sin;
		final float lbY = (unitWidth)*sin - unitHeight*cos;
		
		final ElfColor color = node.getColor();
		final float r = unitRed*color.red;
		final float g = unitGreen*color.green;
		final float b = unitBlue*color.blue;
		final float a = unitAlpha*color.alpha;
		
		//v
		ret[0] = x+ltX;
		ret[1] = y+ltY;
		ret[2] = z;
		
		int off = 9;
		ret[off+0] = x-lbX;
		ret[off+1] = y-lbY;
		ret[off+2] = z;
		
		off = 18;
		ret[off+0] = x-ltX;
		ret[off+1] = y-ltY;
		ret[off+2] = z;
		
		off = 27;
		ret[off+0] = x+lbX;
		ret[off+1] = y+lbY;
		ret[off+2] = z;
		
		for(int i=0; i<4; i++) {
			off = i*9;
			//t
			ret[off+3] = sTexCoords[i*2];
			ret[off+4] = sTexCoords[i*2+1];
			//c
			ret[off+5] = r;
			ret[off+6] = g;
			ret[off+7] = b;
			ret[off+8] = a;
		}
		
		return ret;
		
	}

	
}
