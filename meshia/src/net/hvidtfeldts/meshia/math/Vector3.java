package net.hvidtfeldts.meshia.math;

public class Vector3 {
    private float x;
    private float y;
    private float z;
    
    public Vector3(Vector3 m) {
        this.x = m.x;
        this.y = m.y;
        this.z = m.z;
    }
    
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void normalize() {
        float r = (float) Math.sqrt(x * x + y * y + z * z);
        x /= r;
        y /= r;
        z /= r;
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public float getZ() {
        return z;
    }
}
