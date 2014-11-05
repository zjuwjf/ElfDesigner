package elfEngine.opengl;

import org.lwjgl.opengl.GL11;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode.ColorAssist;


public class TextureRegion implements ITexture{ 
	
	public Texture texture;
	
	protected float u;
	protected float v;
	protected float u2;
	protected float v2;
	
	protected int regionWidth;
	protected int regionHeight;
	
	public TextureRegion(Texture texture) {
		this(texture, 0, 0);
	}
	
	public TextureRegion(Texture texture, int x, int y) {
		this(texture, x, y, texture.getWidth(), texture.getHeight());
	}
	
	public TextureRegion(Texture texture, int x, int y, int width, int height) {
		set(texture, x, y, width, height);
	}
	
	public TextureRegion(Texture texture, float u, float v, float u2, float v2) {
		set(texture, u, v, u2, v2);
	}
	
	public TextureRegion(TextureRegion region, int x, int y, int width, int height) {
		set(region.getTexture(), region.getRegionX()+x, region.getRegionY()+y, width, height);
	}
	
	public void set(Texture texture, int x, int y, int width, int height) {
		set(texture, x / (float)texture.getPower2Width(),
					 y / (float)texture.getPower2Height(),
					(x + width) / (float)texture.getPower2Width(),
					(y + height) / (float)texture.getPower2Height());
	}
	
	public void set(TextureRegion region, int x, int y, int width, int height) {
		set(region.getTexture(), region.getRegionX()+x, region.getRegionY()+y, width, height);
	}
	
	public void set(Texture texture, float u, float v, float u2, float v2) {
		assert texture != null;
		
		this.texture = texture;
		
		if(!texture.isGLBindIdValid()){
			texture.load();
		}
		
		this.u = u;
		this.v = v;
		this.u2 = u2;
		this.v2 = v2;
		
		regionWidth = Math.round(Math.abs(u2 - u) * texture.getPower2Width());
		regionHeight = Math.round(Math.abs(v2 - v) * texture.getPower2Height());
	}
	
	//左上角(0,0) 坐标系
	public int getRegionX () {
		return Math.round(u * texture.getPower2Width());
	}
	
	public int getRegionY () {
		return Math.round(v * texture.getPower2Height());
	}
	
	public void flip (boolean x, boolean y) {
		if (x) {
			float temp = u;
			u = u2;
			u2 = temp;
		}
		if (y) {
			float temp = v;
			v = v2;
			v2 = temp;
		}
	}
	
	public void draw(){
		if( !texture.isGLBindIdValid() ){
			texture.load(); 
		}		
		
		GLHelper.glBindTexture(texture.getGLBindId());
		
		GL11.glBegin(GL11.GL_QUADS);
		
		final float wh = regionWidth *0.5f;
		final float hh = regionHeight *0.5f;
		
		GL11.glTexCoord2f(getU2(), getV2());
		GL11.glVertex3f(wh, -hh, 0); 
		
		GL11.glTexCoord2f(getU(), getV2()); 
		GL11.glVertex3f(-wh, -hh, 0); 
		
		GL11.glTexCoord2f(getU(), getV()); 
		GL11.glVertex3f(-wh, hh, 0); 
		
		GL11.glTexCoord2f(getU2(), getV()); 
		GL11.glVertex3f(wh, hh, 0); 
		
		GL11.glEnd();
		
	}
	
	/***
	 * 渐变
	 * @param display
	 * @param rb
	 * @param lb
	 * @param lt
	 * @param rt
	 */
	public void draw(ColorAssist display, ElfColor rb, ElfColor lb, ElfColor lt, ElfColor rt) {
		if( !texture.isGLBindIdValid() ){
			texture.load(); 
		}		
		
		GLHelper.glBindTexture(texture.getGLBindId());
		
		GL11.glBegin(GL11.GL_QUADS);
		
		final float wh = regionWidth *0.5f;
		final float hh = regionHeight *0.5f;
		
		GL11.glColor4f(rb.red*display.red, rb.green*display.green, rb.blue*display.blue, rb.alpha*display.alpha);
		GL11.glTexCoord2f(getU2(), getV2());
		GL11.glVertex3f(wh, -hh, 0); 
		
		GL11.glColor4f(lb.red*display.red, lb.green*display.green, lb.blue*display.blue, lb.alpha*display.alpha);
		GL11.glTexCoord2f(getU(), getV2()); 
		GL11.glVertex3f(-wh, -hh, 0); 
		
		GL11.glColor4f(lt.red*display.red, lt.green*display.green, lt.blue*display.blue, lt.alpha*display.alpha);
		GL11.glTexCoord2f(getU(), getV()); 
		GL11.glVertex3f(-wh, hh, 0); 
		
		GL11.glColor4f(rt.red*display.red, rt.green*display.green, rt.blue*display.blue, rt.alpha*display.alpha);
		GL11.glTexCoord2f(getU2(), getV()); 
		GL11.glVertex3f(wh, hh, 0); 
		
		GL11.glEnd();
	}
	
	public void draw(float x, float y) {
		GLHelper.glPushMatrix();
		GLHelper.glTranslatef(x, y, 0);
		draw();
		GLHelper.glPopMatrix();
	}
	
	public void draw(float x, float y, float scaleX, float scaleY, float rotate) {
		GLHelper.glPushMatrix();
		GLHelper.glTranslatef(x, y, 0);
		GLHelper.glScalef(scaleX, scaleY);
		GLHelper.glRotatef(rotate);
		draw();
		GLHelper.glPopMatrix();
	}
	
	public int getWidth(){
		return regionWidth;
	}
	public int getHeight(){
		return regionHeight;
	}

	@Override
	public Texture getTexture() {
		return this.texture;
	}

	@Override
	public float getU() {
		return u;
	}

	@Override
	public float getV() {
		return v;
	}

	@Override
	public float getU2() {
		return u2;
	}

	@Override
	public float getV2() {
		return v2;
	}
}
