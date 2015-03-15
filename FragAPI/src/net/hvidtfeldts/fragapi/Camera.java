package net.hvidtfeldts.fragapi;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLException;
import javax.media.opengl.GLUniformData;

import net.hvidtfeldts.utils.Logger;

import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.glsl.ShaderState;

public class Camera {
    private final float[] right = new float[] { 0.7f, 0, -0.7f };
    
    private float[] up = new float[] { 0.4f, -0.8f, 0.4f };
    
    private float[] forward = new float[] { 0.57f, 0.57f, 0.57f };
    
    private final float[] pos = new float[] { 0, 0, -6 };
    
    private PMVMatrix raytracerMatrixStack;
    private GLUniformData raytracerPmvMatrixUniform;
    
    private final FloatBuffer farNear = FloatBuffer.allocate(2);
    
    private GLUniformData fovYScaleUniform;
    private float fovY = 45.0f;
    private GLUniformData aspectUniform;
    private GLUniformData farNearUniform;
    
    private float aspect;
    private float zNear;
    private float zFar;
    
    void setupRaytracerMatrixStack(final GL2ES2 gl) {
        raytracerMatrixStack = new PMVMatrix();
        raytracerMatrixStack.glMatrixMode(PMVMatrix.GL_PROJECTION);
        raytracerMatrixStack.glLoadIdentity();
        raytracerMatrixStack.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        raytracerMatrixStack.glLoadIdentity();
        raytracerPmvMatrixUniform = new GLUniformData("pmvMatrix", 4, 4, raytracerMatrixStack.glGetPMvMvitMatrixf()); // P, Mv
        
        fovYScaleUniform = new GLUniformData("fov_y_scale", 1);
        aspectUniform = new GLUniformData("aspect", 1);
        
        float fovYScale = (float) Math.tan(Math.PI * fovY / 360.0);
        fovYScaleUniform.setData(fovYScale);
        
        aspectUniform.setData(1.0f);
        
        farNearUniform = new GLUniformData("farNear", 2, farNear);
        
        // raytracerShaderState.ownUniform(raytracerPmvMatrixUniform);
        // raytracerShaderState.uniform(gl, raytracerPmvMatrixUniform);
    }
    
    public float[] getRight() {
        return right;
    }
    
    public float[] getUp() {
        return up;
    }
    
    public float[] getPosInCameraCoords() {
        return pos;
    }
    
    public float[] getPosInWorldCoords() {
        // [ r.x r.y r.z q.x ]
        // [ u.x u.y u.z q.y ]
        // [ -f.x -f.y -f.z q.z ]
        // [ 0 0 0 1 ]
        // eye = -(modelView[3].xyz)*mat3(modelView);
        return new float[] {
                -pos[0] * right[0] - pos[1] * up[0] + pos[2] * forward[0],
                -pos[0] * right[1] - pos[1] * up[1] + pos[2] * forward[1],
                -pos[0] * right[2] - pos[1] * up[2] + pos[2] * forward[2],
        };
    }
    
    public void setUpDir(float[] up) {
        this.up = up;
    }
    
    public void setForward(float[] forward) {
        this.forward = forward;
    }
    
    public void rotateAboutUp(float angle) {
        float[] rot = rotation(angle, up);
        forward = multiply(rot, forward);
        ortogonalize();
    }
    
    public void rotateAboutForward(float angle) {
        float[] rot = rotation(angle, forward);
        up = multiply(rot, up);
        ortogonalize();
    }
    
    public void rotateAboutRight(float angle) {
        float[] rot = rotation(angle, right);
        up = multiply(rot, up);
        forward = multiply(rot, forward);
        ortogonalize();
    }
    
    public static float[] multiply(float[] m, float[] v) {
        return new float[] {
                m[0] * v[0] + m[1 + 0] * v[1] + m[2 + 0] * v[2] + m[3],
                m[0 + 4] * v[0] + m[1 + 4] * v[1] + m[2 + 4] * v[2] + m[3 + 4],
                m[0 + 8] * v[0] + m[1 + 8] * v[1] + m[2 + 8] * v[2] + m[3 + 8],
                m[0 + 12] * v[0] + m[1 + 12] * v[1] + m[2 + 12] * v[2] + m[3 + 12],
        };
    }
    
    public static float[] multiply2(float[] m, float[] v) {
        return new float[] {
                m[0] * v[0] + m[0 + 4] * v[1] + m[0 + 8] * v[2] + m[0 + 12],
                m[1] * v[0] + m[1 + 4] * v[1] + m[1 + 8] * v[2] + m[1 + 12],
                m[2] * v[0] + m[2 + 4] * v[1] + m[2 + 8] * v[2] + m[2 + 12],
                m[3] * v[0] + m[3 + 4] * v[1] + m[3 + 8] * v[2] + m[3 + 12],
        };
    }
    
    public static float[] rotation(double angle, float[] v) {
        float[] m = new float[16];
        
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float omc = 1 - c;
        
        float xs = v[0] * s;
        float ys = v[1] * s;
        float zs = v[2] * s;
        float xyomc = v[0] * v[1] * omc;
        float xzomc = v[0] * v[2] * omc;
        float yzomc = v[1] * v[2] * omc;
        
        m[0] = v[0] * v[0] * omc + c;
        m[1] = xyomc + zs;
        m[2] = xzomc - ys;
        m[3] = 0;
        
        m[4] = xyomc - zs;
        m[5] = v[1] * v[1] * omc + c;
        m[6] = yzomc + xs;
        m[7] = 0;
        
        m[8] = xzomc + ys;
        m[9] = yzomc - xs;
        m[10] = v[2] * v[2] * omc + c;
        m[11] = 0;
        
        m[12] = 0;
        m[13] = 0;
        m[14] = 0;
        m[15] = 1;
        
        return m;
    }
    
    public void ortogonalize() {
        // LxU = F;
        // UxF = L
        normalize(forward);
        normalize(up);
        
        up = subtract(up, mul(dot(forward, up), forward));
        
        normalize(up);
        
        cross(up, forward, right);
        negate(right);
        
        // Logger.log("Up " + toString(up) + " Forward: " + toString(forward) +
        // " Left: " + toString(left));
        
    }
    
    public void negate(float[] v) {
        v[0] = -v[0];
        v[1] = -v[1];
        v[2] = -v[2];
    }
    
    public float dot(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }
    
    public float[] mul(float a, float[] b) {
        return new float[] { a * b[0], a * b[1], a * b[2] };
    }
    
    public float[] subtract(float[] a, float[] b) {
        return new float[] { a[0] - b[0], a[1] - b[1], a[2] - b[2] };
    }
    
    private void normalize(float[] a) {
        float l = (float) Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
        a[0] /= l;
        a[1] /= l;
        a[2] /= l;
    }
    
    private void cross(float[] a, float[] b, float[] out) {
        out[0] = a[1] * b[2] - a[2] * b[1];
        out[1] = -a[0] * b[2] + a[2] * b[0];
        out[2] = a[0] * b[1] - a[1] * b[0];
    }
    
    public void updateMatrix() {
        PMVMatrix m = raytracerMatrixStack;
        m.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        m.glLoadIdentity();
        
        ortogonalize();
        // For an idea of these, see: http://3dengine.org/Right-up-back_from_modelview
        
        // OpenGL is column-major based.
        // So these values:
        
        float[] values = new float[] {
                right[0], up[0], -forward[0], 0,
                right[1], up[1], -forward[1], 0,
                right[2], up[2], -forward[2], 0,
                pos[0], pos[1], pos[2], 1,
        
        };
        
        // Correspond to this matrix:
        // [ r.x r.y r.z p.x ]
        // [ u.x u.y u.z p.y ]
        // [ -f.x -f.y -f.z p.z ]
        // [ 0 0 0 1 ]
        
        m.glLoadMatrixf(values, 0);
    }
    
    public void move(float x, float y, float z) {
        pos[0] += x;
        pos[1] += y;
        pos[2] += z;
        
    }
    
    public float[] getForward() {
        return forward;
    }
    
    public void setUniforms(GL2ES2 gl, ShaderState ss) {
        updateMatrix();
        raytracerMatrixStack.update();
        
        ss.uniform(gl, raytracerPmvMatrixUniform);
        ss.uniform(gl, fovYScaleUniform);
        ss.uniform(gl, aspectUniform);
        ss.uniform(gl, farNearUniform);
        
    }
    
    public void zoom(float f) {
        this.fovY *= 1.0 + f;
        if (fovY > 90) {
            fovY = 90;
        }
        if (fovY < 4) {
            fovY = 4;
        }
        
        updateFOV();
        Logger.log(fovY);
    }
    
    private void updateFOV() {
        // compute projection parameters 'normal' perspective
        aspectUniform.setData(aspect);
        
        farNear.put(0, zFar);
        farNear.put(1, zNear);
        
        float fovYScale = (float) Math.tan(Math.PI * fovY / 360.0);
        fovYScaleUniform.setData(fovYScale);
        Logger.log("FOV: " + fovY + " aspect:" + aspect);
    }
    
    public void reshape(GL2ES2 gl, int x, int y, int width, int height) {
        try {
            gl.setSwapInterval(1);
            aspect = (float) width / (float) height;
            zNear = 0.5f;
            zFar = 100f;
            updateFOV();
        }
        catch (GLException e) {
            Logger.warn(e);
        }
        
    }
}
