package elfEngine.basic.node.nodeAdvanced;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.BufferHelper;


public class MotionBlurNode extends ElfNode{
	
	public MotionBlurNode(ElfNode father, int ordinal) {
		super(father, ordinal);
	} 
	
	int BLUR_TEX_SIZE = 1024 ;
	int g_iConcussion = 10; 
	
	public void drawSelf() { 
		
//		final ElfColor color = this.getColor();
////		final float width = this.getSize().x;
//		
//		GL11.glColor4f(color.red, color.green, color.blue, color.alpha * 0.4f);
//		GL11.glTranslatef(-10, 0, 0);
//		super.drawSelf();
//		
//		GL11.glColor4f(color.red, color.green, color.blue, color.alpha * 0.4f);
//		GL11.glTranslatef(20, 0, 0);
//		super.drawSelf();
//		
//		GL11.glColor4f(color.red, color.green, color.blue, color.alpha * 0.8f);
//		GL11.glTranslatef(-10, 0, 0);
//		super.drawSelf();
		g_iConcussion++;
		if(g_iConcussion > 150) {
			g_iConcussion = 0;
		}
		
		GL11.glPushMatrix(); 

        // Turn off depth testing so that we can blend over the screen 
		GL11.glDisable(GL11.GL_DEPTH_TEST); 

        // Set our blend method and enable blending 
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);     
//		GL11.glEnable(GL11.GL_BLEND); 

        int [] iVP = {0,0,0,0}; 
        final IntBuffer buffer = BufferHelper.sIntBuffer;
        buffer.position(0);
        GL11.glGetInteger(GL11.GL_VIEWPORT, buffer); 
        buffer.position(0);
        iVP[0] = buffer.get(0);
        iVP[1] = buffer.get(1);
        iVP[2] = buffer.get(2);
        iVP[3] = buffer.get(3);
        
        // Bind the rendered texture to our QUAD 
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, g_Texture[0]);             
        
        GL11.glColor4f(1.0f, 1.0f, 1.0f, (g_iConcussion/150.0f)); 

        // Switch to 2D mode (Ortho mode) 
        OrthoMode(iVP[0], iVP[1], iVP[2], iVP[3]); 

        // Display a 2D quad with our blended texture 
        GL11.glBegin(GL11.GL_QUADS); 

            // Display the top left point of the 2D image 
        GL11.glTexCoord2f(0.0f, 1.0f);    GL11.glVertex2f(0, 0); 

            // Display the bottom left point of the 2D image 
            GL11.glTexCoord2f(0.0f, 0.0f);    GL11.glVertex2f(0, BLUR_TEX_SIZE); 

            // Display the bottom right point of the 2D image 
            GL11.glTexCoord2f(1.0f, 0.0f);    GL11.glVertex2f(BLUR_TEX_SIZE, BLUR_TEX_SIZE); 

            // Display the top right point of the 2D image 
            GL11.glTexCoord2f(1.0f, 1.0f);    GL11.glVertex2f(BLUR_TEX_SIZE, 0); 
        // Stop drawing  
        GL11.glEnd(); 

        // Let's set our mode back to perspective 3D mode. 
        PerspectiveMode(); 

        // Turn depth testing back on and texturing off.  If you do NOT do these 2 lines of  
        // code it produces a cool flash effect that might come in handy somewhere down the road. 
//        GL11.glEnable(GL11.GL_DEPTH_TEST);                         
//        GL11.glDisable(GL11.GL_BLEND);                             

    // Go back to our original matrix 
        GL11.glPopMatrix(); 
		
	} 
	
	int [] g_Texture = new int[1];
	void CreateRenderTexture(int width, int height, int channels, int type) 
	{ 
	    // Create a pointer to store the blank image data 
	    byte [] pTexture = null; 
	    
	    // Allocate and init memory for the image array and point to it from pTexture 
	    pTexture = new byte [width * height * channels]; 
//	    memset(pTexture, 0, width * height * channels * sizeof(unsigned int));     
	    for(int i=0; i<pTexture.length; i++) {
	    	pTexture[i] = 0;
	    } 

	    // Register the texture with OpenGL and bind it to the texture ID 
//	    GL11.glGenTextures(textures)
	    g_Texture[0] = GL11.glGenTextures();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, g_Texture[0]); 
	    
	    BufferHelper.sByteBuffer.position(0);
	    BufferHelper.sByteBuffer.put(pTexture);
	    BufferHelper.sByteBuffer.position(0);
		
	    // Create the texture and store it on the video card 
	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, channels, width, height, 0, type, GL11.GL_BYTE, BufferHelper.sByteBuffer); 
	    
//	    GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels) ;
	    
	    // Set the texture quality 
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR); 
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR); 

	    // Since we stored the texture space with OpenGL, we can delete the image data 
//	    delete [] pTexture; 
	} 

	void OrthoMode(int left, int top, int right, int bottom) 
	{ 
	    // Switch to our projection matrix so that we can change it to ortho mode, not perspective. 
		GL11.glMatrixMode(GL11.GL_PROJECTION);                         

	    // Push on a new matrix so that we can just pop it off to go back to perspective mode 
		GL11.glPushMatrix();                                     
	     
	    // Reset the current matrix to our identify matrix 
		GL11.glLoadIdentity();                                 

	    //Pass in our 2D ortho screen coordinates like so (left, right, bottom, top).  The last 
	    // 2 parameters are the near and far planes. 
		GL11.glOrtho( left, right, bottom, top, 0, 1 );     
	     
	    // Switch to model view so that we can render the scope image 
		GL11.glMatrixMode(GL11.GL_MODELVIEW);                                 

	    // Initialize the current model view matrix with the identity matrix 
		GL11.glLoadIdentity();                                         
	} 

	void PerspectiveMode() 
	{ 
	    // Enter into our projection matrix mode 
		GL11.glMatrixMode( GL11.GL_PROJECTION );                             

	    // Pop off the last matrix pushed on when in projection mode (Get rid of ortho mode) 
		GL11.glPopMatrix();                                             

	    // Go back to our model view matrix like normal 
		GL11.glMatrixMode( GL11.GL_MODELVIEW );                             

	    // We should be in the normal 3D perspective mode now 
	} 
}
