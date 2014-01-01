package net.hvidtfeldts.meshia.engine3d;

import com.jogamp.opengl.util.PMVMatrix;

public class Camera {
    private final float[] right = new float[] { 1, 0, 0 };
    
    private float[] up = new float[] { 0, 1, 0 };
    
    private float[] forward = new float[] { 0, 0, -1 };
    
    private final float[] pos = new float[] { 0, 0, 0 };
    
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
    
    public void setMatrix(PMVMatrix m) {
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
}
