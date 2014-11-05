//package elfEngine.basic.node.extend;
//
//import org.eclipse.opengl.GL;
//
//import elfEngine.basic.node.ElfNode;
//import elfEngine.opengl.GLHelper;
//import elfEngine.opengl.RectF;
//import elfEngine.opengl.Texture;
//import elfEngine.opengl.TextureRegion;
//
//public class DashRectNode extends ElfNode{
//	private float mWidth, mHeight;
//	private final TextureRegion mTextureRegion;
//	
//	public DashRectNode(ElfNode father, int ordinal) {
//		super(father, ordinal);
//		final Texture texture = new Texture(DashRectNode.class.getResourceAsStream ("line.png"));
//		texture.load();
//		mTextureRegion = new TextureRegion(texture);
//		
//		addDeadListener(new IDeadListener() {
//			public void onDead(ElfNode node) {
//				dipose();
//			}
//		});
//		
////		setColor(1, 0, 0, 1);
//	}
//	
//	public void dipose(){
//		mTextureRegion.mTexture.invalid();
//	}
//	
//	public void setSize(float width, float height){
//		mWidth = width;
//		mHeight = height;
//	}
//	
//	public void load(){
//		mTextureRegion.mTexture.load();
//	}
//	
//	protected void drawSelf(){
//		
//		final TextureRegion tr = mTextureRegion;
//		if( !tr.mTexture.isGLBindIdValid() ){
//			tr.mTexture.load();
//		}	
//		
//		GLHelper.glPushMatrix();
//		GLHelper.glTranslatef(-mWidth/2, 0, 0);
//		drawLine(mHeight, true, tr);
//		GLHelper.glPopMatrix();
//		
//		GLHelper.glPushMatrix();
//		GLHelper.glTranslatef(0, mHeight/2, 0);
//		GLHelper.glRotatef(90);
//		drawLine(mWidth, true, tr);
//		GLHelper.glPopMatrix();
//		
//		GLHelper.glPushMatrix();
//		GLHelper.glTranslatef(mWidth/2, 0, 0);
//		drawLine(mHeight, true, tr);
//		GLHelper.glPopMatrix();
//		
//		GLHelper.glPushMatrix();
//		GLHelper.glTranslatef(0, -mHeight/2, 0);
//		GLHelper.glRotatef(90);
//		drawLine(mWidth, true, tr);
//		GLHelper.glPopMatrix();
//	}
//	
//	//vOrH for image
//	private final void drawLine(float length, boolean vOrH, TextureRegion textureRegion){
//		final Texture texture = textureRegion.mTexture;
//		final float width = textureRegion.getWidth(), height = textureRegion.getHeight();
//		final int widthPow2 = texture.getPower2Width(), heightPower2 = texture.getPower2Height();
//		final RectF rectSrc = new RectF();
//		final RectF rectDst = new RectF();
//		
//		final float lenRate = length/(vOrH ? heightPower2:widthPow2);
//		final float begRate = vOrH ? textureRegion.mRectSrc.top:textureRegion.mRectSrc.left;
//		final float endRate = vOrH ? textureRegion.mRectSrc.bottom:textureRegion.mRectSrc.right;
//		final float midRate = (begRate+endRate)/2f;
//				
//		if(vOrH){
//			rectSrc.left = textureRegion.mRectSrc.left;
//			rectSrc.right = textureRegion.mRectSrc.right;
//			rectSrc.top = midRate - lenRate/2;
//			rectSrc.bottom = midRate + lenRate/2;
//			
//			rectDst.left = -width/2;
//			rectDst.right = width/2;
//			rectDst.top = length/2;
//			rectDst.bottom = -length/2;
//		} else {
//			rectSrc.top = textureRegion.mRectSrc.top;
//			rectSrc.bottom = textureRegion.mRectSrc.bottom;
//			rectSrc.left = midRate - lenRate/2;
//			rectSrc.right = midRate + lenRate/2;
//			
//			rectDst.left = -length/2;
//			rectDst.right = length/2;
//			rectDst.top = height/2;
//			rectDst.bottom = -height/2;
//		}
//				
//		GLHelper.glBindTexture(texture.getGLBindId());
//		GL.glBegin(GL.GL_QUADS);
//		
//		//rb
//		GL.glTexCoord2f(rectSrc.right, rectSrc.bottom);
//		GL.glVertex3f(rectDst.right, rectDst.bottom, 0);
//		
//		//lb
//		GL.glTexCoord2f(rectSrc.left, rectSrc.bottom);
//		GL.glVertex3f(rectDst.left, rectDst.bottom, 0);
//		
//		//lt
//		GL.glTexCoord2f(rectSrc.left, rectSrc.top);
//		GL.glVertex3f(rectDst.left, rectDst.top, 0);
//		
//		//rt
//		GL.glTexCoord2f(rectSrc.right, rectSrc.top);
//		GL.glVertex3f(rectDst.right, rectDst.top, 0);
//		
//		GL.glEnd();
//	}
//}
