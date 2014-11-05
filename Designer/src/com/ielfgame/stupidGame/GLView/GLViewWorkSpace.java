//package com.ielfgame.stupidGame.GLView;
//
//import org.eclipse.swt.widgets.Composite;
//
//import com.ielfgame.stupidGame.AbstractWorkSpace;
//import com.ielfgame.stupidGame.data.DataModel;
//import com.ielfgame.stupidGame.power.PowerMan;
//
//public class GLViewWorkSpace extends AbstractWorkSpace {
//	
//	private final BasicGLView mBasicGLView = new BasicGLView();
//	
//	public GLViewWorkSpace() {
//		final BasicGLViewRender render = new BasicGLViewRender();
//		render.setRootNode(PowerMan.getSingleton(DataModel.class).getScreenNode());
//		
//		mBasicGLView.setGLRender(render);
//	} 
//	
//	public BasicGLView getGLView() {
//		return mBasicGLView;
//	}
//
//	public Composite createWorkSpace(Composite parent) {
//		return mBasicGLView.createWorkSpace(parent);
//	}
//	
//}
