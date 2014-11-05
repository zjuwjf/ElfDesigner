package com.ielfgame.stupidGame.GLView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import com.ielfgame.stupidGame.config.FrameInterval;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;
import elfEngine.opengl.BlendMode;
import elfEngine.opengl.GLHelper;

public class BasicGLView implements IGLView {

	private GLCanvas mGLCanvas;
	private boolean stencilSupport;
	private IGLRender mRender;
	private ViewMode mViewMode;

	public BasicGLView() {
	}

	public GLCanvas createWorkSpace(Composite parent) {
		final GLData data = new GLData();
		data.doubleBuffer = true;
		data.stencilSize = 8;

		mGLCanvas = new GLCanvas(parent, SWT.NO_BACKGROUND | SWT.BORDER, data);

		final Menu menu = mRender.getMenu(parent.getShell());
		if (menu != null) {
			mGLCanvas.setMenu(menu);
		}

		if (mRender.getFixedSize()) {
			GridData gridData = new GridData(mRender.getPhysicWidth(), mRender.getPhysicHeight());
			gridData.verticalAlignment = GridData.BEGINNING;

			mGLCanvas.setLayout(new GridLayout());
			mGLCanvas.setLayoutData(gridData);
			mGLCanvas.setSize(mRender.getPhysicWidth(), mRender.getPhysicHeight());
		} else {
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			gridData.verticalAlignment = gridData.horizontalAlignment = SWT.FILL;
			gridData.grabExcessVerticalSpace = gridData.grabExcessHorizontalSpace = true;
			mGLCanvas.setLayoutData(gridData);
		}

		mGLCanvas.setCurrent();
		try {
			GLContext.useContext(mGLCanvas);

			// update gdx
			// final GdxLwjglCanvas glc = new GdxLwjglCanvas(new
			// ApplicationListener() {
			// public void resume() {
			// }
			// public void resize(int width, int height) {
			// }
			// public void render() {
			// }
			// public void pause() {
			// }
			// public void dispose() {
			// }
			// public void create() {
			// }
			// }, false);
			//
			// glc.graphics.initiateGLInstances();

		} catch (LWJGLException e1) {
			e1.printStackTrace();
		}
		setupViewingArea(mRender.getFixedSize());

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

	private static final Object sLock = new Object();

	public void run() {
		if (!mGLCanvas.isDisposed()) {
			if (!mRender.getPauseGL()) {
				synchronized (sLock) {
					mGLCanvas.setCurrent();
					
					if (!isStencilSupportNeeded() || hasStencilSupport()) {
						final ElfColor color = new ElfColor(0, 0, 0, 0);
						if (color != null) {
							GL11.glClearColor(color.red, color.green, color.blue, color.alpha);
							GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
						}

						// GL11.glLoadIdentity();
						renderScene();
					}

					else {
						GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
					}
					mGLCanvas.swapBuffers();
				}
			}

			final FrameInterval sFrameInterval = PowerMan.getSingleton(FrameInterval.class);
			mGLCanvas.getDisplay().timerExec(sFrameInterval.GL_INTERVAL, this);
		}
	}

	boolean isStencilSupportNeeded() {
		return false;
	}

	private final void setupViewingArea(boolean fixedSize) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(BlendMode.BLEND.sourceBlendFunction, BlendMode.BLEND.destinationBlendFunction);

		if (fixedSize) {
			GridData gridData = new GridData(mRender.getPhysicWidth(), mRender.getPhysicHeight());
			gridData.verticalAlignment = GridData.BEGINNING;

			mGLCanvas.setLayout(new GridLayout());
			mGLCanvas.setLayoutData(gridData);
			mGLCanvas.setSize(mRender.getPhysicWidth(), mRender.getPhysicHeight());
			mGLCanvas.pack();
			mGLCanvas.layout();
		}

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

	/***
	 * GLdouble fovy, //角度 　　GLdouble aspect,//视景体的宽高比 　　GLdouble
	 * zNear,//沿z轴方向的两裁面之间的距离的近处 　　GLdouble zFar //沿z轴方向的两裁面之间的距离的远处
	 * 
	 * @param viewMode
	 */
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

	public void setGLRender(IGLRender render) {
		mRender = render;
		mRender.setGLView(this);
	}

	@Override
	public IGLRender getGLRender() {
		return mRender;
	}

	@Override
	public Shell getShell() {
		return mGLCanvas.getShell();
	}
}
