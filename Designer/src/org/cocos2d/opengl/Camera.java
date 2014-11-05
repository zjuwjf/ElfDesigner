package org.cocos2d.opengl;

import org.cocos2d.utils.CCFormatter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.power.PowerMan;

public class Camera {
	private float eyeX;
	private float eyeY;
	private float eyeZ;

	private float centerX;
	private float centerY;
	private float centerZ;

	private float upX;
	private float upY;
	private float upZ;

	private boolean dirty;

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean value) {
		dirty = value;
	}

	public Camera() {
		restore();
	}

	public String toString() {
		return new CCFormatter().format("<%s = %08X | center = (%.2f,%.2f,%.2f)>", this.getClass(), this, centerX, centerY, centerZ);
	}

	public void restore() {
		// CCSize s = Director.sharedDirector().displaySize();

		eyeX = 0;// s.width / 2;
		eyeY = 0;// s.height / 2;
		eyeZ = Camera.getZEye();

		centerX = 0;// s.width / 2;
		centerY = 0;// s.height / 2;
		centerZ = 0.0f;

		upX = 0.0f;
		upY = 1.0f;
		upZ = 0.0f;

		dirty = false;
	}

	public void locate() {
		if (dirty) {

			final ProjectSetting screen = PowerMan.getSingleton(ProjectSetting.class);

			int orientation = screen.getLogicWidth() > screen.getPhysicHeight() ? 1 : 0;

			GL11.glLoadIdentity();

			switch (orientation) {
			// case Director.CCDeviceOrientationPortrait:
			// break;
			// // case Director.CCDeviceOrientationPortraitUpsideDown:
			// // GL11.glRotatef(-180,0,0,1);
			// // break;
			// case Director.CCDeviceOrientationLandscapeLeft:
			// GL11.glRotatef(-90,0,0,1);
			// break;
			// case Director.CCDeviceOrientationLandscapeRight:
			// GL11.glRotatef(90,0,0,1);
			// break;
			case 1:
				GL11.glRotatef(90, 0, 0, 1);
				// break;
			}

			GLU.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);

			switch (orientation) {
//			 case Director.CCDeviceOrientationPortrait:
//			 case Director.CCDeviceOrientationPortraitUpsideDown:
//			 // none
//			 break;
//			 case Director.CCDeviceOrientationLandscapeLeft:
//			 GL11.glTranslatef(-80,80,0);
//			 break;
//			 case Director.CCDeviceOrientationLandscapeRight:
//			 GL11.glTranslatef(-80,80,0);
//			 break;
			case 1:
				GL11.glTranslatef(-80, 80, 0);
				break;
			}
		}
	}

	public static float getZEye() {
		// CCSize s = Director.sharedDirector().displaySize();
		final ProjectSetting screen = PowerMan.getSingleton(ProjectSetting.class);
		return (screen.getPhysicHeight() / 1.1566f);
	}

	public void setEye(float x, float y, float z) {
		eyeX = x;
		eyeY = y;
		eyeZ = z;
		dirty = true;
	}

	public void setCenter(float x, float y, float z) {
		centerX = x;
		centerY = y;
		centerZ = z;
		dirty = true;
	}

	public void setUp(float x, float y, float z) {
		upX = x;
		upY = y;
		upZ = z;
		dirty = true;
	}

	public void getEye(float x[], float y[], float z[]) {
		x[0] = eyeX;
		y[0] = eyeY;
		z[0] = eyeZ;
	}

	public void getCenter(float x[], float y[], float z[]) {
		x[0] = centerX;
		y[0] = centerY;
		z[0] = centerZ;
	}

	public void getUp(float x[], float y[], float z[]) {
		x[0] = upX;
		y[0] = upY;
		z[0] = upZ;
	}

}
