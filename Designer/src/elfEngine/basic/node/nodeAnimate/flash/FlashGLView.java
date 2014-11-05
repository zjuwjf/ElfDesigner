package elfEngine.basic.node.nodeAnimate.flash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import com.ielfgame.stupidGame.GLView.IGLRender;
import com.ielfgame.stupidGame.GLView.ViewMode;
import com.ielfgame.stupidGame.config.FrameInterval;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLHelper;

public class FlashGLView implements Runnable{
	private GLCanvas mGLCanvas;
	private boolean stencilSupport;
	private final IGLRender mRender;
	private ViewMode mViewMode;
	
	public FlashGLView() {
		this.mRender = new FlashGLRender(this) ;
	}
	
	public Composite create(Composite parent) {
		final GLData data = new GLData();
		data.doubleBuffer = true;
		data.stencilSize = 8;

		mGLCanvas = new GLCanvas(parent, SWT.NO_BACKGROUND | SWT.BORDER, data);
		
		final Menu menu = mRender.getMenu(parent.getShell());
		if(menu != null) {
			mGLCanvas.setMenu(menu);
		}
		
//		GridData gridData = new GridData(mRender.getPhysicWidth(), mRender.getPhysicHeight());
//		gridData.verticalAlignment = GridData.BEGINNING;
//		
//		mGLCanvas.setLayout(new GridLayout());
//		mGLCanvas.setLayoutData(gridData);
//		mGLCanvas.setSize(mRender.getPhysicWidth(), mRender.getPhysicHeight());
		
		mGLCanvas.setCurrent();

		try {
			GLContext.useContext(mGLCanvas);
		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		
		setupViewingArea();

		mRender.setDND(mGLCanvas);
		mGLCanvas.addMouseListener(mRender);
		mGLCanvas.addMouseMoveListener(mRender);
		mGLCanvas.addMouseWheelListener(mRender);
		mGLCanvas.addKeyListener(mRender);

		run();
		return mGLCanvas;
	}

	private boolean hasStencilSupport() {
		return stencilSupport;
	}

	private final void renderScene() {
		GLHelper.resetScissor();
		mRender.render();
	}

	private static boolean sRunning = true;

	public static void pauseGL() {
		sRunning = false;
	}

	public static void resumeGL() {
		sRunning = true;
	}

	public void run() {
		if (!mGLCanvas.isDisposed()) {

			if (sRunning) {

				mGLCanvas.setCurrent();
				if (!isStencilSupportNeeded() || hasStencilSupport()) {
					GL11.glClearColor(0, 0, 0, 1);
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

					// GL11.glLoadIdentity();
					renderScene();
				} else {
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				}
				mGLCanvas.swapBuffers();
			}

			final FrameInterval sFrameInterval = PowerMan.getSingleton(FrameInterval.class);
			mGLCanvas.getDisplay().timerExec(sFrameInterval.GL_INTERVAL, this);
		}
	}
	
	boolean isStencilSupportNeeded() {
		return false;
	}
	
	public final void setupViewingArea() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(BlendMode.BLEND.sourceBlendFunction, BlendMode.BLEND.destinationBlendFunction);

//		GridData gridData = new GridData(mRender.getPhysicWidth(), mRender.getPhysicHeight());
//		gridData.verticalAlignment = GridData.BEGINNING;

//		mGLCanvas.setLayout(new GridLayout());
//		mGLCanvas.setLayoutData(gridData);
//		mGLCanvas.setSize(mRender.getPhysicWidth(), mRender.getPhysicHeight());
//		mGLCanvas.pack();
//		mGLCanvas.layout();

		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		// GL11.glEnable( GL11.GL_CULL_FACE); // ��������
		// GL11.glCullFace( GL11.GL_FRONT); // �õ�����

		GL11.glViewport(0, 0, mRender.getPhysicWidth(), mRender.getPhysicHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, mRender.getPhysicWidth(), 0, mRender.getPhysicHeight(), -1000, 1000);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		setViewMode(ViewMode.MODE_2D, 0, 0, 0, 0);
		ElfNode.resetStatic();
	}
	
	public void setViewMode(ViewMode viewMode, float fovy, float aspect, float zNear, float zFar) {
		if (viewMode != mViewMode) {
			mViewMode = viewMode;
			switch (mViewMode) {
			case MODE_2D:
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0, mRender.getPhysicWidth(), 0, mRender.getPhysicHeight(), -1000, 1000);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);

				GL11.glDisable(GL11.GL_DEPTH_TEST);
				break;
			case MODE_3D:
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GLU.gluPerspective(fovy, aspect, zNear, zFar);
				GL11.glScalef(1, 1, (float) mRender.getPhysicHeight() / mRender.getLogicHeight()); //
				GL11.glMatrixMode(GL11.GL_MODELVIEW);

				GL11.glEnable(GL11.GL_DEPTH_TEST);
				break;
			}
		}
	}
	
	public Point getViewSize() {
		return mGLCanvas.getSize();
	}
}
