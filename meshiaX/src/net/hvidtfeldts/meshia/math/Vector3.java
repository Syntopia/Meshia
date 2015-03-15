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
    
    public void subtract(Vector3 offset) {
        x -= offset.x;
        y -= offset.y;
        z -= offset.z;
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
    
    public void add(Vector3 offset) {
        x += offset.x;
        y += offset.y;
        z += offset.z;
    }
    
    /**
     * Linear interpolation between p1 (m=0) and p2 (m=1)
     */
    public static Vector3 interpolate(Vector3 p1, Vector3 p2, float m) {
        return new Vector3(
                p1.x * (1 - m) + p2.x * m,
                p1.y * (1 - m) + p2.y * m,
                p1.z * (1 - m) + p2.z * m);
    }
    
    /**
     * Linear component-wise interpolation between p1 (c=0) and p2 (c=1)
     */
    public static Vector3 interpolate(Vector3 p1, Vector3 p2, float x, float y, float z) {
        return new Vector3(
                p1.x * (1 - x) + p2.x * x,
                p1.y * (1 - y) + p2.y * y,
                p1.z * (1 - z) + p2.z * z);
    }
    
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.y * v2.z - v2.y * v1.z,
                v2.x * v1.z - v1.x * v2.z,
                v1.x * v2.y - v2.x * v1.y);
    }
    
    @Override
    public String toString() {
        return "Vector3 [x=" + x + ", y=" + y + ", z=" + z + "]";
    }
    
    public static Vector3 add(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }
    
    public static Vector3 sub(Vector3 a, Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }
    
    public static Vector3 getPlaneNormal(Vector3 p1, Vector3 p2, Vector3 p3) {
        return cross(sub(p2, p1), sub(p3, p1));
    }
    
    public void multiply(float f) {
        x *= f;
        y *= f;
        z *= f;
    }
    
    public void setX(float x) {
        this.x = x;
    }
    
    public void setY(float y) {
        this.y = y;
    }
    
    public void setZ(float z) {
        this.z = z;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector3 other = (Vector3) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
            return false;
        if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
            return false;
        return true;
    }
    
}
