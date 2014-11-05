package elfEngine.basic.node.nodeAdvanced;

import org.lwjgl.opengl.GL11;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.TextureRegion;

public class AccumNode extends ElfNode{ 
	
	public AccumNode(ElfNode father, int ordinal) { 
		super(father, ordinal); 
	} 
	
	void drawMyChilds() {
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); 
		drawChilds(); 
	}
	
	public void calc(float t) { 
		super.calc(t); 
		
		if(mResetAccum) { 
			final TextureRegion tr = GLManage.loadTextureRegion(this.getResid(), mGLid); 
			if(tr != null) {  
				GL11.glClearColor(0, 0, 0, 0); 
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); 
				
				GL11.glPushMatrix(); 
				final int width = Math.round(this.getWidth()); 
				final int height = Math.round(this.getHeight()); 
				GL11.glTranslatef(this.getWidth()/2, this.getHeight()/2, 0); 
				GL11.glScalef(1, -1, 1); 
				
				mResetAccum = false; 
				float rate = 1f/mAccumCount; 
				
				if(mShakeType == ShakeType.HV_Shake) { 
					rate *= 0.5f;
					for(int i=0; i<mAccumCount; i++) { 
						if(i==0) { 
							GLHelper.glTranslatef(-mAccumCount/2f, 0, 0); 
							drawMyChilds(); 
							GL11.glAccum(GL11.GL_LOAD, rate); 
						} else { 
							GLHelper.glTranslatef(1, 0, 0); 
							drawMyChilds(); 
							GL11.glAccum(GL11.GL_ACCUM, rate); 
						} 
					} 
					GLHelper.glTranslatef(-mAccumCount/2f, 0, 0); 
					for(int i=0; i<mAccumCount; i++) { 
						if(i==0) { 
							GLHelper.glTranslatef(-mAccumCount/2f, -mAccumCount/2f, 0); 
						} else {
							GLHelper.glTranslatef(0, 1, 0); 
						} 
						drawMyChilds();
						GL11.glAccum(GL11.GL_ACCUM, rate); 
					} 
				} else { 
					for(int i=0; i<mAccumCount; i++) { 
						if(i==0) { 
							switch(mShakeType) {
							case A_Shake:
								GLHelper.glRotatef(-mAccumCount/2f); break;
							case H_Shake:
								GLHelper.glTranslatef(-mAccumCount/2f, 0, 0); break;
							case V_Shake:
								GLHelper.glTranslatef(0, -mAccumCount/2f, 0); break;
							case HV_Shake:
							}
							drawMyChilds();
							GL11.glAccum(GL11.GL_LOAD, rate); 
						} else { 
							switch(mShakeType) {
							case A_Shake:
								GLHelper.glRotatef(1);  break;
							case H_Shake:
								GLHelper.glTranslatef(1, 0, 0); break;
							case V_Shake:
								GLHelper.glTranslatef(0, 1, 0); break;
							case HV_Shake:
								
							}
							drawMyChilds();
							GL11.glAccum(GL11.GL_ACCUM, rate); 
						} 
					} 
				}
				
				GL11.glAccum(GL11.GL_RETURN, 1); 
				GL11.glPopMatrix(); 
				
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, tr.texture.getGLBindId()); 
				GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height);  
				
				if(mClearAfterRender) { 
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); 
				} 
			} 
		} 
	} 
	
	private int mAccumCount = 10;
	public void setAccumCount(int accumCount) {
		if(accumCount > 0) { 
			mAccumCount = accumCount; 
		} 
	} 
	public int getAccumCount() {
		return mAccumCount;
	}
	protected final static int REF_AccumCount = DEFAULT_SHIFT;
	
	private boolean mResetAccum = true; 
	public void setLoadAccum() {
		this.mResetAccum = true;
	}
	protected final static int REF_LoadAccum = FACE_SET_SHIFT;

	private boolean mClearAfterRender = false;
	public boolean getClearAfterRender() {
		return mClearAfterRender;
	}
	public void setClearAfterRender(boolean mClearAfterRender) {
		this.mClearAfterRender = mClearAfterRender;
	}
	protected final static int REF_ClearAfterRender = DEFAULT_SHIFT;
	
	public void drawSprite() { 
		if (mVisible) { 
			drawBefore(); 
			drawSelf(); 
			drawAfter();
		}
	} 
	
	public static enum ShakeType {
		H_Shake, V_Shake, HV_Shake, A_Shake
	} 
	
	private ShakeType mShakeType = ShakeType.HV_Shake; 
	public void setShakeType(ShakeType type) {
		if(type != null) {
			mShakeType = type;
		}
	}
	public ShakeType getShakeType() { 
		return mShakeType; 
	} 
	protected final static int REF_ShakeType = DEFAULT_SHIFT;
}
