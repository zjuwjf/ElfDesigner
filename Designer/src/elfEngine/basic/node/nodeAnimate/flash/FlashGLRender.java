package elfEngine.basic.node.nodeAnimate.flash;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.GLView.IGLRender;
import com.ielfgame.stupidGame.GLView.IGLView;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.extend.ElfScreenNode;

public class FlashGLRender implements IGLRender{ 
	
	private final ElfScreenNode mRoot = PowerMan.getSingleton(DataModel.class).getScreenNode();
	private final FlashGLView mFlashGLView;
	
	public FlashGLRender(FlashGLView view) {
		mFlashGLView = view;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		
	}

	@Override
	public void mouseUp(MouseEvent e) {
		
	}

	@Override
	public void mouseMove(MouseEvent e) {
		
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		
	}

	@Override
	public void render() {
		
		final Point size = mFlashGLView.getViewSize();
		mRoot.update();
		
		final ElfPointf pos = mRoot.getPosition();
		mRoot.setPosition(0, size.y);
		mRoot.drawSprite();
		mRoot.setPosition(pos);
	}

	@Override
	public void setDND(GLCanvas gLCanvas) {
		
	}

	@Override
	public int getPhysicWidth() {
		return 2000;
	}

	@Override
	public int getPhysicHeight() {
		return 2000;
	}

	@Override
	public int getLogicWidth() {
		return 2000;
	}

	@Override
	public int getLogicHeight() {
		return 2000;
	}

	@Override
	public boolean getFixedSize() {
		return false;
	}

	@Override
	public int getGLID() {
		return 0;
	}

	@Override
	public void setGLID(int id) {
		
	}

	@Override
	public ElfScreenNode getRootNode() {
		return null;
	}

	@Override
	public Menu getMenu(Shell shell) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPauseGL(boolean pause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getPauseGL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IGLView getGLView() {
		return null;
	}

	@Override
	public void setGLView(IGLView view) {
		
	}

	@Override
	public boolean getCharKeyPressed(int keycode) {
		return false;
	}
}
