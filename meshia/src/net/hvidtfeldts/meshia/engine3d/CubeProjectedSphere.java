package net.hvidtfeldts.meshia.engine3d;

import net.hvidtfeldts.meshia.math.Vector3;

public class CubeProjectedSphere extends PolygonBuilder {
    
    private static final Vector3 ZERO = new Vector3(0, 0, 0);
    
    public CubeProjectedSphere() {
        final Vector3 offset = new Vector3(0, 0, -5);
        int n = 15;
        float dx = 1.0f / n;
        float dy = 1.0f / n;
        
        Vector3 c1 = new Vector3(1, 1, 0);
        Vector3 c2 = new Vector3(1, 1, 0);
        Vector3 c3 = new Vector3(1, 1, 0);
        Vector3 c4 = new Vector3(1, 1, 0);
        
        int in = 0;
        this.setTransformator(new Transformator() {
            @Override
            public Vector3 transform(Vector3 in) {
                Vector3 v = new Vector3(in);
                v.normalize();
                return v;
            }
        });
        in = addPlane(n, dx, dy, c1, c2, c3, c4, in);
        
        this.setTransformator(new Transformator() {
            @Override
            public Vector3 transform(Vector3 in) {
                Vector3 v = new Vector3(in.getX(), in.getY(), -in.getZ());
                v.normalize();
                return v;
            }
        });
        in = addPlane(n, dx, dy, c1, c2, c3, c4, in);
        
        this.setTransformator(new Transformator() {
            @Override
            public Vector3 transform(Vector3 in) {
                Vector3 v = new Vector3(in.getX(), in.getZ(), in.getY());
                v.normalize();
                return v;
            }
        });
        in = addPlane(n, dx, dy, c1, c2, c3, c4, in);
        
        this.setTransformator(new Transformator() {
            @Override
            public Vector3 transform(Vector3 in) {
                Vector3 v = new Vector3(in.getX(), -in.getZ(), in.getY());
                v.normalize();
                return v;
            }
        });
        in = addPlane(n, dx, dy, c1, c2, c3, c4, in);
        
        this.setTransformator(new Transformator() {
            @Override
            public Vector3 transform(Vector3 in) {
                Vector3 v = new Vector3(in.getZ(), in.getY(), in.getX());
                v.normalize();
                return v;
            }
        });
        in = addPlane(n, dx, dy, c1, c2, c3, c4, in);
        
        this.setTransformator(new Transformator() {
            @Override
            public Vector3 transform(Vector3 in) {
                Vector3 v = new Vector3(-in.getZ(), in.getY(), in.getX());
                v.normalize();
                return v;
            }
        });
        in = addPlane(n, dx, dy, c1, c2, c3, c4, in);
        // x
        this.translate(offset);
    }
    
    private int addPlane(int n, float dx, float dy, Vector3 c1, Vector3 c2, Vector3 c3, Vector3 c4, int in) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                float offsetX = i * dx - 0.5f;
                float offsetY = j * dx - 0.5f;
                
                addVertex(new Vector3(offsetX, offsetY, 0.5f), c1, ZERO);
                addVertex(new Vector3(offsetX + dx, offsetY, 0.5f), c2, ZERO);
                addVertex(new Vector3(offsetX + dx, offsetY + dy, 0.5f), c3, ZERO);
                addVertex(new Vector3(offsetX, offsetY + dy, 0.5f), c4, ZERO);
                addSquare(in++, in++, in++, in++);
            }
        }
        return in;
    }
}
