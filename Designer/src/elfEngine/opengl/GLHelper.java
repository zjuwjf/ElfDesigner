package elfEngine.opengl;

import java.nio.DoubleBuffer;
import java.util.Stack;

import org.lwjgl.opengl.GL11;



public final class GLHelper {
	
	public static int minPow2(int n){
		int i = 0;
		while(n> (1<<i)){
			i++;
		}
		return (1<<i);
	}
	
	public static boolean isPow2(int n){
		return (n&(n-1)) == 0;
	}
		
//	private static float sRed = -1;
//	private static float sGreen = -1;
//	private static float sBlue = -1;
//	private static float sAlpha = -1;
//	private static int sBindTextureId = GL.GL_INVALID_VALUE;
//	private static int sSourceBlendFunction = GL.GL_INVALID_VALUE;
//	private static int sDestinationBlendFunction = GL.GL_INVALID_VALUE;
	
	public static void glPushMatrix(){
		GL11.glPushMatrix();
	}
	
	public static void glPopMatrix(){
		GL11.glPopMatrix();
	}
	
	public static void glTranslatef(final float x, final float y, final float z){
		GL11.glTranslatef(x, y, z);
	}
	
	public static void glRotatef(final float rotate){
		GL11.glRotatef(rotate, 0, 0, -1);
	}
	
	public static void glScalef(final float scaleX, final float scaleY){
		GL11.glScalef(scaleX, scaleY, 1);
	}
	
	public static void  glTexCoordPointer(float left, float top, float right, float bottom){
		GL11.glTexCoord2f(left,top);
		GL11.glTexCoord2f(left,bottom);
		GL11.glTexCoord2f(right,bottom);
		GL11.glTexCoord2f(right,top);
	}
	
	public static void  glVertexPointer(float left, float top, float right, float bottom){
		GL11.glVertex3f(left,top,0);
		GL11.glVertex3f(left,bottom,0);
		GL11.glVertex3f(right,bottom,0);
		GL11.glVertex3f(right,top,0);
	}
	
//	public static void glDrawElements(){
//		GL.glDrawElements(GL.GL_TRIANGLES, 6, GL.GL_INT, INDEX_ARRAY);
//	}
	
	public static void glBlendFunc(final int sourceBlendFunction, final int destinationBlendFunction){
		GL11.glBlendFunc(sourceBlendFunction, destinationBlendFunction);
	}
	
	public static void glColor4f(final float red, final float green, final float blue, final float alpha){
		GL11.glColor4f(red, green, blue, alpha);
	}
	
	public static void glBindTexture(final int id){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public static void reset(){
//		sRed = -1;
//		sGreen = -1;
//		sBlue = -1;
//		sAlpha = -1;
//		sBindTextureId = GL.GL_INVALID_VALUE;
//		sSourceBlendFunction = GL.GL_INVALID_VALUE;
//		sDestinationBlendFunction = GL.GL_INVALID_VALUE;
	}
	
	
	
	public final static class F4 { 
		public float left, bottom, width, height; 
	} 
	private final static Stack<F4> sScissorStack = new Stack<F4>(); 
	
	public static void resetScissor() { 
		sScissorStack.clear(); 
		setScissor(); 
	} 
	
	private final static void setScissor() {
		if(sScissorStack.isEmpty()) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		} else {
			final F4 f4 = sScissorStack.peek();
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(Math.round(f4.left), Math.round(f4.bottom), Math.round(f4.width), Math.round(f4.height));
		}
	} 
	
	public static boolean getGlScissorEnabled() {
		return GL11.glGetBoolean(GL11.GL_SCISSOR_TEST);
	}
	
	public static void setGlScissorEnabled(boolean enable) {
		if(enable) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
		} else {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}
	
	public static void pushScissor(float left, float bottom, float width, float height) {
		final F4 f4 = new F4();
		f4.left = Math.round(left);
		f4.bottom = Math.round(bottom);
		f4.width = Math.round(width);
		f4.height = Math.round(height);
		
		if(!sScissorStack.isEmpty()) {
			final F4 peek = sScissorStack.peek();
			
			final float right = Math.min(peek.left+peek.width, f4.left+f4.width);
			final float top = Math.min(peek.bottom+peek.height, f4.bottom+f4.height);
			
			f4.left = Math.max(f4.left, peek.left);
			f4.bottom = Math.max(f4.bottom, peek.bottom);
			
			f4.width = right-f4.left;
			f4.height = top-f4.bottom;
		}
		
		if(Math.round(f4.width) <= 0 || Math.round(f4.height) <= 0) {
			f4.left = Integer.MAX_VALUE;
			f4.bottom = Integer.MAX_VALUE;
			f4.width = 1;
			f4.height = 1;
		} 
		
		sScissorStack.push(f4);
		setScissor();
	}
	
	public static F4 popScissor() { 
		F4 ret = null;
		if(!sScissorStack.isEmpty()) { 
			ret = sScissorStack.pop(); 
		} 
		setScissor();
		return ret;
	} 
	
	public static void pushClip(double [] top, double [] bottom, double [] left, double [] right) {
		GL11.glEnable(GL11.GL_CLIP_PLANE0); 
		GL11.glEnable(GL11.GL_CLIP_PLANE1); 
		GL11.glEnable(GL11.GL_CLIP_PLANE2); 
		GL11.glEnable(GL11.GL_CLIP_PLANE3); 
		
		final DoubleBuffer buffer = BufferHelper.sDoubleBuffer;
		buffer.clear();
		buffer.put(top);
		buffer.flip();
		GL11.glClipPlane(GL11.GL_CLIP_PLANE0, buffer);
		
		buffer.position(0);
		buffer.put(bottom);
		buffer.position(0);
		GL11.glClipPlane(GL11.GL_CLIP_PLANE1, buffer);
		
		buffer.position(0);
		buffer.put(left);
		buffer.position(0);
		GL11.glClipPlane(GL11.GL_CLIP_PLANE2, buffer);
		
		buffer.position(0);
		buffer.put(right);
		buffer.position(0);
		GL11.glClipPlane(GL11.GL_CLIP_PLANE3, buffer);
		
	} 
	
	public static void popClipPanel() { 
		GL11.glDisable(GL11.GL_CLIP_PLANE0); 
		GL11.glDisable(GL11.GL_CLIP_PLANE1); 
		GL11.glDisable(GL11.GL_CLIP_PLANE2); 
		GL11.glDisable(GL11.GL_CLIP_PLANE3); 
	} 
} 
