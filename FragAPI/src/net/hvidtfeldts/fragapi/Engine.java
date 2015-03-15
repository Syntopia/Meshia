package net.hvidtfeldts.fragapi;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;

import net.hvidtfeldts.utils.Logger;

import com.jogamp.opengl.JoglVersion;
import com.jogamp.opengl.util.glsl.ShaderState;

public class Engine implements GLEventListener {
    private final Camera camera = new Camera();
    
    private long lastTime;
    private int frames;
    
    private boolean inError;
    
    private FrameBuffer outputBuffer;
    
    public Engine() {
    }
    
    @Override
    public void init(GLAutoDrawable glad) {
        glad.setGL(new DebugGL2((GL2) glad.getGL()));
        
        try {
            final GL2ES2 gl = glad.getGL().getGL2ES2();
            Logger.log("Chosen GLCapabilities: " + glad.getChosenGLCapabilities());
            Logger.log("INIT GL IS: " + gl.getClass().getName());
            Logger.log(JoglVersion.getGLStrings(gl, null, false).toString());
            if (!gl.hasGLSL()) {
                Logger.warn("No GLSL available, no rendering.");
                return;
            }
            
            camera.setupRaytracerMatrixStack(gl);
            
            outputBuffer.initRecursive(gl);
            
            // OpenGL Render Settings
            gl.glEnable(GL2ES2.GL_DEPTH_TEST);
            Logger.log("Init finished");
        }
        catch (GLException e) {
            e.printStackTrace();
            Logger.warn(e);
            check(false);
        }
    }
    
    private void check(boolean returnValue) {
        if (!returnValue) {
            inError = true;
            Logger.warn("Failed");
        }
    }
    
    @Override
    public void display(GLAutoDrawable glad) {
        if (inError) {
            return;
        }
        
        if (Defaults.getMeasureFPS()) {
            showFPS();
        }
        
        final GL2ES2 gl = glad.getGL().getGL2ES2();
        
        for (FrameBuffer fb : outputBuffer.getPreviousBuffers()) {
            ShaderState ss = fb.getShaderState();
            
            ss.useProgram(gl, true);
            camera.setUniforms(gl, ss);
            ss.useProgram(gl, false);
        }
        outputBuffer.draw(gl);
    }
    
    private void showFPS() {
        long time = System.currentTimeMillis();
        
        if (lastTime == 0) {
            lastTime = time;
        }
        
        if (time - lastTime > 5000) {
            float fps = (frames * 1000.0f) / (time - lastTime);
            lastTime = time;
            frames = 0;
            Logger.log("FPS: " + fps);
        }
        frames++;
    }
    
    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        if (inError) {
            return;
        }
        final GL2ES2 gl = glad.getGL().getGL2ES2();
        camera.reshape(gl, x, y, width, height);
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        Logger.log("JOGL Dispose");
        final GL2ES2 gl = glad.getGL().getGL2ES2();
        if (!gl.hasGLSL()) {
            return;
        }
    }
    
    public void rotate(int i, int j) {
        camera.rotateAboutRight(j / 100.0f);
        camera.rotateAboutUp(i / 100.0f);
    }
    
    public void moveCamera(float x, float y, float z) {
        camera.move(x / 3.0f, y / 3.0f, z / 3.0f);
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public void setOutputBuffer(FrameBuffer fp) {
        this.outputBuffer = fp;
    }
    
}
