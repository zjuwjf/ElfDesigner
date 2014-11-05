package elfEngine.basic.node.node3d;

import com.ielfgame.stupidGame.GLView.GLMainWindow;
import com.ielfgame.stupidGame.GLView.ViewMode;
import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class Elf3DLayer extends Elf3DNode{
	public Elf3DLayer(ElfNode father, int ordinal) {
		super(father, ordinal);
		setUseCamera(true);
		
		init3D();
	} 
	
	void init3D() { 
		final ElfCamera camera = getCamera();
		final ProjectSetting s = PowerMan.getSingleton(ProjectSetting.class);
		camera.eye.set(s.getLogicWidth()/2f, s.getLogicHeight()/2f, s.getPhysicHeight()/2f);
		camera.center.set(s.getLogicWidth()/2f, s.getLogicHeight()/2f, 0);
		camera.up.set(0, 1, 0);
		
		mPerspective.fovy = 90;
		mPerspective.aspect = (float)s.getLogicWidth()/s.getLogicHeight();
		mPerspective.zNear = 0.1f;
		mPerspective.zFar = 1000f;
		
		this.setCamera(camera);
	} 
	
	public static class Perspective extends ElfDataDisplay{
		public float fovy;
		public float aspect;
		public float zNear;
		public float zFar;
		public Perspective() {
		} 
		public Perspective(Perspective perspective) {
			set(perspective);
		}
		public void set(Perspective perspective) {
			this.fovy = perspective.fovy;
			this.aspect = perspective.aspect;
			this.zNear = perspective.zNear;
			this.zFar = perspective.zFar;
		}
	}
	
	private final Perspective mPerspective = new Perspective();
	public void setPerspective(Perspective perspective) {
		mPerspective.set(perspective);
	} 
	public Perspective getPerspective() { 
		return new Perspective(mPerspective); 
	}
	protected final static int REF_Perspective = DEFAULT_SHIFT;
	
	public void drawBefore() { 
		PowerMan.getSingleton(GLMainWindow.class).getGLView().setViewMode(ViewMode.MODE_3D, mPerspective.fovy, mPerspective.aspect, mPerspective.zNear, mPerspective.zFar);
		super.drawBefore(); 
	} 
	
	public void setUseCamera(boolean use) {
		super.setUseCamera(true);
	}
	
	public void drawAfter() { 
		super.drawAfter(); 
		PowerMan.getSingleton(GLMainWindow.class).getGLView().setViewMode(ViewMode.MODE_2D, 0, 0, 0, 0);
	} 
	
	protected void drawSelectBefore() { 
		PowerMan.getSingleton(GLMainWindow.class).getGLView().setViewMode(ViewMode.MODE_3D, mPerspective.fovy, mPerspective.aspect, mPerspective.zNear, mPerspective.zFar);
		super.drawSelectBefore();
	} 
	
	protected void drawSelectAfter() { 
		super.drawSelectAfter(); 
		PowerMan.getSingleton(GLMainWindow.class).getGLView().setViewMode(ViewMode.MODE_2D, 0, 0, 0, 0);
	} 
}
