package net.hvidtfeldts.meshia.engine3d;

import org.sunflow.SunflowAPI;
import org.sunflow.core.primitive.TriangleMesh;

public interface SunflowRenderable extends Object3D {
    
    public abstract TriangleMesh getTriangleMesh(SunflowAPI api);
    
}