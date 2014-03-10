package net.hvidtfeldts.meshia.engine3d;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import net.hvidtfeldts.meshia.math.Vector3;

import com.jogamp.opengl.util.glsl.ShaderState;

public class SimpleMarchingCubes extends MarchingCubes {
    private final Vector3 from;
    private final Vector3 to;
    private PolygonBuilder builder;
    private int count;
    private final float delta;
    
    public SimpleMarchingCubes(double isolevel, Vector3 from, Vector3 to, int cellsX, int cellsY, int cellsZ, Component parentComponent) {
        super(isolevel, cellsX + 1, cellsY + 1, cellsZ + 1, parentComponent);
        this.from = from;
        this.to = to;
        delta = (to.getX() - from.getX()) / (cellsX * 120.0f);
    }
    
    public SunflowRenderable getObject3D(ShaderState shaderState) {
        builder = new PolygonBuilder(shaderState, String.format("MC %S,%S,%S", nx, ny, nz));
        polygonise();
        return builder;
    }
    
    Map<Vector3, Integer> vectorCache = new HashMap<>();
    
    Vector3 color = new Vector3(1, 1, 1);
    
    private int reuseVector(Vector3 p1) {
        Integer integer = vectorCache.get(p1);
        // integer = null;
        if (integer != null) {
            return integer;
        }
        int i = builder.addColorVertex(p1, null, color);
        vectorCache.put(new Vector3(p1), i);
        return i;
    }
    
    @Override
    protected void createPolygon(Vector3 p1, Vector3 p2, Vector3 p3) {
        boolean faceNormals = true;
        
        boolean reuse = true;
        if (faceNormals) {
            if (reuse) {
                Vector3 normal = Vector3.getPlaneNormal(p1, p2, p3);
                normal.normalize();
                int i1 = reuseVector(p1);
                int i2 = reuseVector(p2);
                int i3 = reuseVector(p3);
                builder.setNormal(i1, normal);
                builder.setNormal(i2, normal);
                builder.setNormal(i3, normal);
                builder.addTriangle(i1, i2, i3);
            }
            else {
                Vector3 normal = Vector3.getPlaneNormal(p1, p2, p3);
                normal.normalize();
                builder.addColorVertex(p1, normal, color);
                builder.addColorVertex(p2, normal, color);
                builder.addColorVertex(p3, normal, color);
                
                builder.addTriangle(count++, count++, count++);
            }
        }
        else {
            builder.addColorVertex(p1, getNormal(p1), color);
            builder.addColorVertex(p2, getNormal(p2), color);
            builder.addColorVertex(p3, getNormal(p3), color);
        }
        
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
    
    protected double getValuexx(Vector3 p) {
        return p.getLength() - 0.8;
    }
    
    @Override
    protected double getValue(Vector3 p) {
        return p.getLength() - 0.5;
    }
    
    protected double getValue2e(Vector3 p) {
        
        double power = 6;
        p.multiply(1.15f);
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        
        double r = 0;
        double dr = 1;
        
        for (int j = 0; j < 10; j++) {
            if (x * x + y * y + z * z > 10000)
                break;
            
            double phi = Math.atan2(y, x);
            r = Math.sqrt(x * x + y * y + z * z);
            double theta = Math.acos(z / r);
            dr = Math.pow(r, power - 1.0) * power * dr + 1.0;
            r = Math.pow(r, power);
            double sin = Math.sin(power * theta);
            x = r * sin * Math.cos(power * phi);
            y = r * sin * Math.sin(power * phi);
            z = r * Math.cos(power * theta);
            
            x += p.getX();
            y += p.getY();
            z += p.getZ();
            // Logger.log("J p" + j + " " + p);
        }
        double d = (0.5 * Math.log(r) * r / dr) - 0.0019;
        double co = 0.1;
        if (p.getZ() < 0) {
            co = 0.2;
        }
        double rr = -0.2 + co * Math.abs(p.getZ());
        if (Math.abs(p.getZ()) < 0.5) {
            rr += -0.7 * (0.5 - Math.abs(p.getZ()));
        }
        double d2 = -Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY()) - rr;
        
        return Math.max(d2, d);
    }
    
    protected double getValue2(Vector3 p) {
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
            double f = p.getLength() - 0.9;
            
            p.multiply(12.0f);
            double d = Math.cos(p.getX()) * Math.sin(p.getY()) + Math.cos(p.getY()) * Math.sin(p.getZ())
                    + Math.cos(p.getZ()) * Math.sin(p.getX());
            
            return Math.max(f, Math.abs(d) - 0.2);
        }
        
    }
    
}
