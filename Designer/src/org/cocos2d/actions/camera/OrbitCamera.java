package org.cocos2d.actions.camera;

import org.cocos2d.actions.interval.IntervalAction;
import org.cocos2d.opengl.Camera;
import org.cocos2d.types.CCMacros;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.node3d.Elf3DNode;
import elfEngine.basic.node.node3d.ElfCamera;

public class OrbitCamera extends CameraAction {
    private float radius;
    private float deltaRadius;
    private float angleZ;
    private float deltaAngleZ;
    private float angleX;
    private float deltaAngleX;

    private float radZ;
    private float radDeltaZ;
    private float radX;
    private float radDeltaX;

    public static OrbitCamera action(float t, float r, float dr, float z, float dz, float x, float dx) {
        return new OrbitCamera(t, r, dr, z, dz, x, dx);
    }
	  
    protected OrbitCamera(float t, float r, float dr, float z, float dz, float x, float dx) {
        super(t);

        radius = r;
        deltaRadius = dr;
        angleZ = z;
        deltaAngleZ = dz;
        angleX = x;
        deltaAngleX = dx;

        radDeltaZ = CCMacros.CC_DEGREES_TO_RADIANS(dz);
        radDeltaX = CCMacros.CC_DEGREES_TO_RADIANS(dx);
    } 
    
    public IntervalAction copy() {
        return new OrbitCamera(duration, radius, deltaRadius, angleZ, deltaAngleZ, angleX, deltaAngleX);
    }


    @Override
    public void start(ElfNode aTarget) {
        super.start(aTarget);
        float[] r = new float[1], zenith = new float[1], azimuth = new float[1];

        spherical(r, zenith, azimuth);
        if (Float.isNaN(radius))
            radius = r[0];
        if (Float.isNaN(angleZ))
            angleZ = CCMacros.CC_RADIANS_TO_DEGREES(zenith[0]);
        if (Float.isNaN(angleX))
            angleX = CCMacros.CC_RADIANS_TO_DEGREES(azimuth[0]);

        radZ = CCMacros.CC_DEGREES_TO_RADIANS(angleZ);
        radX = CCMacros.CC_DEGREES_TO_RADIANS(angleX);
    }

    @Override
    public void update(float t) {
        float r = (radius + deltaRadius * t) * Camera.getZEye();
        float za = radZ + radDeltaZ * t;
        float xa = radX + radDeltaX * t;

        float i = (float) Math.sin(za) * (float) Math.cos(xa) * r + centerXOrig;
        float j = (float) Math.sin(za) * (float) Math.sin(xa) * r + centerYOrig;
        float k = (float) Math.cos(za) * r + centerZOrig;

        final Elf3DNode node = (Elf3DNode)target;
        final ElfCamera camera = node.getCamera();
        camera.eye.set(i, j, k);
        node.setCamera(camera);
    }

    private void spherical(float newRadius[], float zenith[], float azimuth[]) {
        float[] ex = new float[1], ey = new float[1], ez = new float[1];
        float[] cx = new float[1], cy = new float[1], cz = new float[1];
        float x, y, z;
        float r; // radius
        float s;

        final Elf3DNode node = (Elf3DNode)target;
        final ElfCamera camera = node.getCamera();
        ex[0] = camera.eye.x;
        ey[0] = camera.eye.y;
        ez[0] = camera.eye.z;
        cx[0] = camera.center.x;
        cy[0] = camera.center.y;
        cz[0] = camera.center.z;
        
        x = ex[0] - cx[0];
        y = ey[0] - cy[0];
        z = ez[0] - cz[0];

        r = (float) Math.sqrt(Math.pow(x, 2) + (float) Math.pow(y, 2) + Math.pow(z, 2));
        s = (float) Math.sqrt(Math.pow(x, 2) + (float) Math.pow(y, 2));

        if (s == 0.0f)
            s = 0.00000001f;
        if (r == 0.0f)
            r = 0.00000001f;

        zenith[0] = (float) Math.acos(z / r);
        
        if (x < 0)
            azimuth[0] = (float) Math.PI - (float) Math.asin(y / s);
        else
            azimuth[0] = (float) Math.asin(y / s);

        newRadius[0] = r / Camera.getZEye();
    }

}
