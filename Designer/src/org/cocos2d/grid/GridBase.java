//package org.cocos2d.grid;
//
//import org.cocos2d.opengl.Camera;
//import org.cocos2d.types.CCGridSize;
//import org.cocos2d.types.CCPoint;
//import org.cocos2d.types.CCSize;
//import org.cocos2d.utils.CCFormatter;
//import org.eclipse.opengl.GL;
//import org.eclipse.opengl.GLU;
//
//public abstract class GridBase {
//
//    protected boolean active;
//    protected int reuseGrid;
//    protected CCGridSize gridSize;
//    protected Texture2D texture;
//    protected CCPoint step;
//
//    public static final int kTextureSize = 512;
//
//    public boolean reuseGrid() {
//        return reuseGrid > 0;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public void setActive(boolean flag) {
//        active = flag;
//    }
//
//    public int getGridWidth() {
//        return gridSize.x;
//    }
//
//    public int getGridHeight() {
//        return gridSize.y;
//    }
//
//    public GridBase(CCGridSize gSize) {
//        active = false;
//        reuseGrid = 0;
//        gridSize = gSize;
//
//        CCSize win = Director.sharedDirector().winSize();
//
//        if (texture == null) {
//            Bitmap.Config config = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = Bitmap.createBitmap(kTextureSize, kTextureSize, config);
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawBitmap(bitmap, 0, 0, new Paint());
//            
//            texture = new Texture2D(bitmap, win);
//        }
//
//        step = CCPoint.make(0, 0);
//        step.x = win.width / gridSize.x;
//        step.y = win.height / gridSize.y;
//    }
//
//    public String toString() {
//        return new CCFormatter().format("<%s = %08X | Dimensions = %ix%i>", GridBase.class, this, gridSize.x, gridSize.y);
//    }
//
//    private static final boolean LANDSCAPE_LEFT = false;
//
//    // This routine can be merged with Director
//    public void applyLandscape() {
//        boolean landscape = Director.sharedDirector().getLandscape();
//
//        if (landscape) {
//            GL.glTranslatef(160, 240, 0);
//
//            if (LANDSCAPE_LEFT) {
//            	GL.glRotatef(-90, 0, 0, 1);
//            	GL.glTranslatef(-240, -160, 0);
//            } else {
//                // rotate left
//            	GL.glRotatef(90, 0, 0, 1);
//            	GL.glTranslatef(-240, -160, 0);
//            } // LANDSCAPE_LEFT
//        }
//    }
//
//    public void set2DProjection() {
//        CCSize winSize = Director.sharedDirector().winSize();
//
//        GL.glLoadIdentity();
//        GL.glViewport(0, 0, (int) winSize.width, (int) winSize.height);
//        GL.glMatrixMode(GL.GL_PROJECTION);
//        GL.glLoadIdentity();
//        GL.glOrtho(0, winSize.width, 0, winSize.height, -100, 100);
//        GL.glMatrixMode(GL.GL_MODELVIEW);
//    }
//
//    // This routine can be merged with Director
//    public void set3DProjection() {
//        CCSize winSize = Director.sharedDirector().displaySize();
//
//        GL.glViewport(0, 0, (int) winSize.width, (int) winSize.height);
//        GL.glMatrixMode(GL.GL_PROJECTION);
//        GL.glLoadIdentity();
//        GLU.gluPerspective( 60, winSize.width / winSize.height, 0.5f, 1500.0f);
//
//        GL.glMatrixMode(GL.GL_MODELVIEW);
//        GL.glLoadIdentity();
//        GLU.gluLookAt( winSize.width / 2, winSize.height / 2, Camera.getZEye(),
//                winSize.width / 2, winSize.height / 2, 0,
//                0.0f, 1.0f, 0.0f
//        );
//    }
//
//    public void beforeDraw() {
//        set2DProjection();
//    }
//
//    public void afterDraw(Camera camera) {
//        set3DProjection();
//        applyLandscape();
//
//        boolean cDirty = camera.isDirty();
//        camera.setDirty(true);
//        camera.locate();
//        camera.setDirty(cDirty);
//
//        GL.glEnable(GL.GL_TEXTURE_2D);
//        GL.glBindTexture(GL.GL_TEXTURE_2D, texture.name());
//
//        blit();
//
//        GL.glDisable(GL.GL_TEXTURE_2D);
//    }
//
//    public abstract void blit();
//
//    public abstract void reuse();
//}
