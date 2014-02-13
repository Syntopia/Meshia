package net.hvidtfeldts.meshia.engine3d;

import net.hvidtfeldts.meshia.math.Vector3;

public class SimpleMarchingCubes extends MarchingCubes {
    private final Vector3 from;
    private final Vector3 to;
    private final PolygonBuilder builder = new PolygonBuilder();
    private int count;
    private final float delta;
    
    public SimpleMarchingCubes(double isolevel, Vector3 from, Vector3 to, int cellsX, int cellsY, int cellsZ) {
        super(isolevel, cellsX + 1, cellsY + 1, cellsZ + 1);
        this.from = from;
        this.to = to;
        delta = (to.getX() - from.getX()) / (cellsX * 20.0f);
        polygonise();
    }
    
    public SunflowRenderable getObject3D() {
        return builder;
    }
    
    @Override
    protected void createPolygon(Vector3 p1, Vector3 p2, Vector3 p3) {
        boolean faceNormals = true;
        
        Vector3 color = new Vector3(1, 1, 1);
        
        if (faceNormals) {
            Vector3 normal = Vector3.getPlaneNormal(p1, p2, p3);
            normal.normalize();
            builder.addColorVertex(p1, normal, color);
            builder.addColorVertex(p2, normal, color);
            builder.addColorVertex(p3, normal, color);
        }
        else {
            builder.addColorVertex(p1, getNormal(p1), color);
            builder.addColorVertex(p2, getNormal(p2), color);
            builder.addColorVertex(p3, getNormal(p3), color);
        }
        builder.addTriangle(count++, count++, count++);
    }
    
    protected final Vector3 getNormal(Vector3 p) {
        Vector3 pp = new Vector3(p);
        
        pp.setX(p.getX() - delta);
        double dx = getValue(pp);
        pp.setX(p.getX() + delta);
        dx -= getValue(pp);
        pp.setX(p.getX());
        
        pp.setY(p.getY() - delta);
        double dy = getValue(pp);
        pp.setY(p.getY() + delta);
        dy -= getValue(pp);
        pp.setY(p.getY());
        
        pp.setZ(p.getZ() - delta);
        double dz = getValue(pp);
        pp.setZ(p.getZ() + delta);
        dz -= getValue(pp);
        pp.setZ(p.getZ());
        
        pp.setX((float) dx);
        pp.setY((float) dy);
        pp.setZ((float) dz);
        pp.normalize();
        pp.multiply(-1.0f);
        return pp;
    }
    
    @Override
    protected final Vector3 getPosition(int i, int j, int k) {
        return Vector3.interpolate(from, to, ((float) i / nx), ((float) j / ny), ((float) k / nz));
    }
    
    @Override
    protected double getValue(int i, int j, int k) {
        Vector3 p = getPosition(i, j, k);
        double d = getValue(p);
        
        return d;
    }
    
    private double getValue(Vector3 p) {
        boolean simple = false;
        
        if (simple) {
            double d = p.getLength() - 0.3;
            Vector3 p2 = new Vector3(p);
            p2.setX(p2.getX() + 0.3f);
            
            double d2 = p2.getLength() - 0.4;
            p2.setY(p2.getY() + 0.2f);
            
            double d3 = p2.getLength() - 0.3;
            return Math.max(-d3, Math.min(d, d2));
        }
        else {
            p.multiply(6.0f);
            double d = Math.cos(p.getX()) * Math.sin(p.getY()) + Math.cos(p.getY()) * Math.sin(p.getZ())
                    + Math.cos(p.getZ()) * Math.sin(p.getX());
            
            return d;
        }
        
    }
    
}
