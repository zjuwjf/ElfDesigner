package elfEngine.basic.node.nodeAdvanced;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.GLHelper;
import elfEngine.opengl.GLManage;
import elfEngine.opengl.TextureRegion;

public class FreeNode extends ElfNode{

	public FreeNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	} 
	
	float rotate;
	float roate2;
	float rotate3;
	
	float trans;
	public void drawSelf() { 
		GL11.glTranslatef(0, 0, -192*0.5f*(float)Math.sqrt(3));
        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        rotate += 0.5f;
//        GL11.glRotatef(rotate, 0, 1.0f, 0);
//        roate2 += 0.2f;
//        GL11.glRotatef(roate2, 1, 0, 0);
//        rotate3 += 0.1f;
//        GL11.glRotatef(rotate3, 0, 0, 1);
        //���õݹ麯���������׶���� 
        
        rotate += 0.5f; 
        GL11.glRotatef(rotate, 0, 1.0f, 0); 
        
        drawCube(6);  
        
        GL11.glDisable(GL11.GL_DEPTH_TEST); 
        
//      GLU.gluLookAt(widthInPoint/2, heightInPoint/2, zeye, widthInPoint/2, heightInPoint/2, 0, 0, 1, 0);
	} 
	
	public float getTrans() { 
		return trans;
	} 
	public void setTrans(float f) {
		trans = f;
	}
	protected final static int REF_Trans = DEFAULT_SHIFT;
	
	static class V3{
		float x, y, z;
		V3(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		} 
	}
	
	static final V3 [] pos = {
		new V3(1,1,1), 
		new V3(1,1,1), 
		new V3(1,1,1), 
		new V3(1,1,1), 
		new V3(1,1,1), 
		new V3(1,1,1), 
		new V3(1,1,1), 
		new V3(1,1,1), 
	}; 
	
	final void drawCube(final int faces) { 
		final TextureRegion tr = GLManage.loadTextureRegion(getResid(), getGLId());
		if(tr != null) { 
			final float w = tr.getWidth();
			final float h = tr.getHeight();
			
			if( !tr.texture.isGLBindIdValid() ){
				tr.texture.load(); 
			}		
			GLHelper.glBindTexture(tr.texture.getGLBindId());
			
			final ElfPointf [] poses = getPoints(faces, w); 
			
			for(int i=0; i<faces; i++) {
				final ElfPointf p0 = poses[i];
				final ElfPointf p1 = poses[(i+1)%faces];
				
				GL11.glBegin(GL11.GL_QUADS);
				
				GL11.glTexCoord2f(tr.getU2(), tr.getV2());
				GL11.glVertex3f(p0.x, -h/2, p0.y);
				
				GL11.glTexCoord2f(tr.getU(), tr.getV2());
				GL11.glVertex3f(p1.x, -h/2, p1.y);
				
				GL11.glTexCoord2f(tr.getU(), tr.getV());
				GL11.glVertex3f(p1.x, h/2, p1.y);
				
				GL11.glTexCoord2f(tr.getU2(), tr.getV());
				GL11.glVertex3f(p0.x, h/2, p0.y);
				
				GL11.glEnd();
			} 
		} 
	} 
	
	final void drawPyramid(float x, float y, float z, int n){
        if(n == 0)return;
        //��һ������׶
        GL11.glBegin(GL11.GL_TRIANGLES);
            //������
            GL11.glColor3f(1.0f,0.0f,0.0f);
            GL11.glVertex3f( x, y, z);
            GL11.glColor3f(0.0f,1.0f,0.0f);
            GL11.glVertex3f(x+1.0f,y-1.63f,z-0.57f);
            GL11.glColor3f(0.0f,0.0f,1.0f);
            GL11.glVertex3f( x-1.0f,y-1.63f,z-0.57f);
            //������
            GL11.glColor3f(1.0f,0.0f,0.0f);
            GL11.glVertex3f( x,y-1.63f,z+1.15f);
            GL11.glColor3f(0.0f,1.0f,0.0f);
            GL11.glVertex3f(x-1.0f,y-1.63f,z-0.57f);
            GL11.glColor3f(0.0f,0.0f,1.0f);
            GL11.glVertex3f( x+1.0f,y-1.63f,z-0.57f);
            //�������
            GL11.glColor3f(1.0f,0.0f,0.0f);
            GL11.glVertex3f( x,y,z);
            GL11.glColor3f(0.0f,1.0f,0.0f);
            GL11.glVertex3f(x-1.0f,y-1.63f,z-0.57f);
            GL11.glColor3f(0.0f,0.0f,1.0f);
            GL11.glVertex3f( x,y-1.63f,z+1.15f);
            //���Ҳ���
            GL11.glColor3f(1.0f,0.0f,0.0f);
            GL11.glVertex3f( x,y,z);
            GL11.glColor3f(0.0f,1.0f,0.0f);
            GL11.glVertex3f(x,y-1.63f,z+1.15f);
            GL11.glColor3f(0.0f,0.0f,1.0f);
            GL11.glVertex3f( x+1.0f,y-1.63f,z-0.57f);
        GL11.glEnd();
        //�ݹ���ã����������׶
        drawPyramid(x,y-1.63f,z+1.15f,n-1);
        drawPyramid(x-1.0f,y-1.63f,z-0.57f,n-1);
        drawPyramid(x+1.0f,y-1.63f,z-0.57f,n-1);
    } 
	
	
	ElfPointf [] getPoints(final int faces, final float len) { 
		final ElfPointf [] ret = new ElfPointf[faces]; 
		
		final float startAngle = 0; 
		final float stepAngle = (float)Math.PI*2/faces; 
		final float radius = (float)(len*0.5f/Math.sin(stepAngle/2));
		
		for(int i=0; i<ret.length; i++) { 
			final float angle = startAngle + i * stepAngle; 
			final float x = radius * (float)Math.cos(angle); 
			final float z = radius * (float)Math.sin(angle); 
			ret[i] = new ElfPointf(x, z); 
		} 
		
		return ret; 
	} 
} 
