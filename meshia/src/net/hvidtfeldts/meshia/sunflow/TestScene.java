package net.hvidtfeldts.meshia.sunflow;

import net.hvidtfeldts.meshia.engine3d.Hemesh3D;

import org.sunflow.SunflowAPI;
import org.sunflow.core.camera.PinholeLens;
import org.sunflow.core.primitive.Plane;
import org.sunflow.core.shader.AmbientOcclusionShader;
import org.sunflow.math.Point3;
import org.sunflow.math.Vector3;

public class TestScene extends SunflowAPI {
    
    public Point3 eye = new Point3(2.0f, 2.0f, -5.0f);
    public Point3 target = new Point3(0, 0, 0);
    public Vector3 up = new Vector3(0.0f, 1.0f, 0.0f);
    public float fovY = 45.0f;
    public float aspect = 2.0f;
    public Hemesh3D mesh;
    
    // Change settings here
    int depth = 3;
    boolean preview = false;
    public int width;
    public int height;
    
    @Override
    public void build() {
        parameter("eye", eye);
        parameter("target", target);
        parameter("up", up);
        float aspect = ((float) width) / height;
        parameter("fov", fovY * aspect);
        parameter("aspect", aspect);
        camera("camera_outside", new PinholeLens());
        
        parameter("maxdist", 0.5f);
        parameter("samples", 128);
        shader("ao_mesh", new AmbientOcclusionShader());
        
        parameter("maxdist", 2.5f);
        parameter("samples", 128);
        shader("ao_ground", new AmbientOcclusionShader());
        
        // geometry("sponge", new MengerSponge(depth));
        // parameter("shaders", "ao_sponge");
        // /instance("sponge.instance", "sponge");
        
        // parameter("shaders", "ao_mesh");
        
        parameter("shaders", new String[] { "ao_mesh" });
        
        geometry("mesh", mesh.getTriangleMesh(this));
        parameter("shaders", new String[] { "ao_mesh" });
        instance("mesh.instance", "mesh");
        
        parameter("center", new Point3(0, -1.25f, 0.0f));
        parameter("normal", new Vector3(0.0f, 1.0f, 0.0f));
        geometry("ground", new Plane());
        parameter("shaders", "ao_ground");
        // instance("ground.instance", "ground");
        
        // rendering options
        parameter("camera", "camera_outside");
        parameter("resolutionX", width);
        parameter("resolutionY", height);
        if (preview) {
            parameter("aa.min", 0);
            parameter("aa.max", 1);
            parameter("bucket.order", "spiral");
        }
        else {
            parameter("aa.min", 1);
            parameter("aa.max", 2);
            parameter("bucket.order", "column");
            parameter("filter", "mitchell");
        }
        options(DEFAULT_OPTIONS);
    }
}