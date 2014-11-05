package elfEngine.basic.node.node3d;

import org.lwjgl.util.glu.GLU;

import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.data.ElfVertex3;

public class ElfCamera extends ElfDataDisplay{
	public ElfVertex3 eye = new ElfVertex3(0,0,1);
	public ElfVertex3 center = new ElfVertex3(0,0,0);
	public ElfVertex3 up = new ElfVertex3(0,1,0);
	
	public ElfCamera() { 
	} 
	
	public ElfCamera(ElfCamera camera) {
		setCamera(camera);
	}
	
	public void setCamera(ElfCamera camera) {
		this.eye.set(camera.eye);
		this.up.set(camera.up);
		this.center.set(camera.center);
	}
	
	public ElfVertex3 getEye() {
		return new ElfVertex3(eye);
	}
	public void setEye(ElfVertex3 mEye) {
		this.eye.set(mEye);
	}
	public ElfVertex3 getUp() {
		return new ElfVertex3(up);
	}
	public void setUp(ElfVertex3 mUp) {
		this.up.set(mUp);
	}
	public ElfVertex3 getCenter() {
		return new ElfVertex3(center);
	}
	public void setCenter(ElfVertex3 mCenter) {
		this.center.set(mCenter);
	}
	
	public void locate() {
		GLU.gluLookAt(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
	}
}
