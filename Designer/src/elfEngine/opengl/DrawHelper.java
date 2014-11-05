package elfEngine.opengl;

import org.lwjgl.opengl.GL11;

public class DrawHelper {
	
	public final static void fillRect(float width, float height, float x, float y) {
		GLHelper.glPushMatrix();
		GLHelper.glTranslatef(x, y, 0);
		fillRect(width, height);
		GLHelper.glPopMatrix();
	}
	
	public final static void setColor(float r, float g, float b, float a) {
		GLHelper.glColor4f(r, g, b, a);
	}
	
	public final static void drawRect(float width, float height, float x, float y, float thin) {
		fillRect(width, thin, x, y+height/2);
		fillRect(width, thin, x, y-height/2);
		fillRect(thin, height, x+width/2, y);
		fillRect(thin, height, x-width/2, y);
	}
	
	public final static void fillRect(float width, float height) {
		final RectF rectSrc = new RectF();
		final RectF rectDst = new RectF();

		rectSrc.top = 0.0f;
		rectSrc.bottom = 1.0f;
		rectSrc.left = 0.0f;
		rectSrc.right = 1.0f;

		rectDst.left = -width / 2.0f;
		rectDst.right = width / 2.0f;
		rectDst.top = height / 2.0f;
		rectDst.bottom = -height / 2.0f;

		GLHelper.glBindTexture(0);
		GL11.glBegin(GL11.GL_QUADS);

		// rb
		GL11.glTexCoord2f(rectSrc.right, rectSrc.bottom);
		GL11.glVertex3f(rectDst.right, rectDst.bottom, 0);
		
		// lb
		GL11.glTexCoord2f(rectSrc.left, rectSrc.bottom);
		GL11.glVertex3f(rectDst.left, rectDst.bottom, 0);

		// lt
		GL11.glTexCoord2f(rectSrc.left, rectSrc.top);
		GL11.glVertex3f(rectDst.left, rectDst.top, 0);
		
		// rt
		GL11.glTexCoord2f(rectSrc.right, rectSrc.top);
		GL11.glVertex3f(rectDst.right, rectDst.top, 0);

		GL11.glEnd();
	}
	
	public final static void fillOval(float width, float height) {
		final RectF rectSrc = new RectF();
		final RectF rectDst = new RectF();

		rectSrc.top = 0.0f;
		rectSrc.bottom = 1.0f;
		rectSrc.left = 0.0f;
		rectSrc.right = 1.0f;

		rectDst.left = -width / 2.0f;
		rectDst.right = width / 2.0f;
		rectDst.top = height / 2.0f;
		rectDst.bottom = -height / 2.0f;

		GLHelper.glBindTexture(0);
		GL11.glBegin(GL11.GL_QUADS);
		
		// rb
		GL11.glTexCoord2f(rectSrc.right, rectSrc.bottom);
		GL11.glVertex3f(rectDst.right, rectDst.bottom, 0);
		
		// lb
		GL11.glTexCoord2f(rectSrc.left, rectSrc.bottom);
		GL11.glVertex3f(rectDst.left, rectDst.bottom, 0);
		
		// lt
		GL11.glTexCoord2f(rectSrc.left, rectSrc.top);
		GL11.glVertex3f(rectDst.left, rectDst.top, 0);
		
		// rt
		GL11.glTexCoord2f(rectSrc.right, rectSrc.top);
		GL11.glVertex3f(rectDst.right, rectDst.top, 0);

		GL11.glEnd();
	}
}
