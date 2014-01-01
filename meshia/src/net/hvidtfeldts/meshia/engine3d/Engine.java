package net.hvidtfeldts.meshia.engine3d;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLUniformData;

import net.hvidtfeldts.utils.Logger;

import com.jogamp.opengl.JoglVersion;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

public class Engine implements GLEventListener {
    private final Camera camera = new Camera();
    
    private ShaderState shaderState;
    private PMVMatrix matrixStack;
    private GLUniformData pmvMatrixUniform;
    
    private PMVMatrix raytracerMatrixStack;
    private ShaderState raytracerShaderState;
    private GLUniformData raytracerPmvMatrixUniform;
    
    private Object3D sphere;
    private Object3D box;
    private RaytracerObject raytracerObject;
    
    private final FloatBuffer farNear = FloatBuffer.allocate(2);
    
    private GLUniformData fovYScaleUniform;
    private final float fovY = 45.0f;
    private GLUniformData aspectUniform;
    
    private GLUniformData farNearUniform;
    
    private long lastTime;
    private int frames;
    
    private boolean inError;
    
    public Engine() {
    }
    
    @Override
    public void init(GLAutoDrawable glad) {
        // glad.setGL(new DebugGL2((GL2) glad.getGL()));
        
        try {
            final GL2ES2 gl = glad.getGL().getGL2ES2();
            
            Logger.log("Chosen GLCapabilities: " + glad.getChosenGLCapabilities());
            Logger.log("INIT GL IS: " + gl.getClass().getName());
            Logger.log(JoglVersion.getGLStrings(gl, null, false).toString());
            if (!gl.hasGLSL()) {
                Logger.warn("No GLSL available, no rendering.");
                return;
            }
            
            shaderState = new ShaderState();
            shaderState.setVerbose(true);
            
            raytracerShaderState = new ShaderState();
            raytracerShaderState.setVerbose(true);
            
            initializeShaders(gl);
            
            sphere = new CubeProjectedSphere();
            box = new Box3D();
            sphere.init(gl, shaderState);
            box.init(gl, shaderState);
            
            raytracerObject = new RaytracerObject();
            raytracerObject.init(gl, raytracerShaderState);
            
            // OpenGL Render Settings
            gl.glEnable(GL2ES2.GL_DEPTH_TEST);
            shaderState.useProgram(gl, false);
            raytracerShaderState.useProgram(gl, false);
            
            Logger.log("Init finished");
        }
        catch (GLException e) {
            Logger.warn(e);
            check(false);
        }
    }
    
    private void setupMatrixStack(final GL2ES2 gl) {
        matrixStack = new PMVMatrix();
        matrixStack.glMatrixMode(PMVMatrix.GL_PROJECTION);
        matrixStack.glLoadIdentity();
        matrixStack.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        matrixStack.glLoadIdentity();
        pmvMatrixUniform = new GLUniformData("pmvMatrix", 4, 4, matrixStack.glGetPMvMvitMatrixf()); // P, Mv
        shaderState.ownUniform(pmvMatrixUniform);
        shaderState.uniform(gl, pmvMatrixUniform);
    }
    
    private void setupRaytracerMatrixStack(final GL2ES2 gl) {
        raytracerMatrixStack = new PMVMatrix();
        raytracerMatrixStack.glMatrixMode(PMVMatrix.GL_PROJECTION);
        raytracerMatrixStack.glLoadIdentity();
        raytracerMatrixStack.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        raytracerMatrixStack.glLoadIdentity();
        raytracerPmvMatrixUniform = new GLUniformData("pmvMatrix", 4, 4, raytracerMatrixStack.glGetPMvMvitMatrixf()); // P, Mv
        
        fovYScaleUniform = new GLUniformData("fov_y_scale", 1);
        aspectUniform = new GLUniformData("aspect", 1);
        
        // scale = tan(radians(fov/2.0));
        float fovYScale = (float) Math.tan((Math.PI * fovY / 360.0));
        fovYScaleUniform.setData(fovYScale);
        
        aspectUniform.setData(1.0f);
        
        farNearUniform = new GLUniformData("farNear", 2, farNear);
        
        raytracerShaderState.ownUniform(raytracerPmvMatrixUniform);
        raytracerShaderState.uniform(gl, raytracerPmvMatrixUniform);
    }
    
    private void initializeShaders(final GL2ES2 gl) {
        final ShaderCode vp0 = ShaderCode.create(gl, GL2ES2.GL_VERTEX_SHADER, this.getClass(), "shaders",
                "shader/bin", "PhongShader", true);
        final ShaderCode fp0 = ShaderCode.create(gl, GL2ES2.GL_FRAGMENT_SHADER, this.getClass(), "shaders",
                "shader/bin", "PhongShader", true);
        vp0.defaultShaderCustomization(gl, true, true);
        fp0.defaultShaderCustomization(gl, true, true);
        final ShaderProgram sp0 = new ShaderProgram();
        check(sp0.add(gl, vp0, Logger.getLoggerWarnStream()));
        check(sp0.add(gl, fp0, Logger.getLoggerWarnStream()));
        check(shaderState.attachShaderProgram(gl, sp0, true));
        setupMatrixStack(gl);
        
        final ShaderCode vp1 = ShaderCode.create(gl, GL2ES2.GL_VERTEX_SHADER, this.getClass(), "shaders",
                "shader/bin", "RaytracerShader", true);
        final ShaderCode fp1 = ShaderCode.create(gl, GL2ES2.GL_FRAGMENT_SHADER, this.getClass(), "shaders",
                "shader/bin", "RaytracerShader", true);
        vp1.defaultShaderCustomization(gl, true, true);
        fp1.defaultShaderCustomization(gl, true, true);
        final ShaderProgram sp1 = new ShaderProgram();
        check(sp1.add(gl, vp1, Logger.getLoggerWarnStream()));
        check(sp1.add(gl, fp1, Logger.getLoggerWarnStream()));
        check(raytracerShaderState.attachShaderProgram(gl, sp1, true));
        setupRaytracerMatrixStack(gl);
        
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
        
        if (OpenGlWindow.MEASURE_FPS) {
            showFPS();
        }
        
        final GL2ES2 gl = glad.getGL().getGL2ES2();
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        shaderState.useProgram(gl, true);
        
        setCamera(matrixStack, false);
        matrixStack.update();
        shaderState.uniform(gl, pmvMatrixUniform);
        
        sphere.draw(gl);
        box.draw(gl);
        shaderState.useProgram(gl, false);
        
        raytracerShaderState.useProgram(gl, true);
        
        setCamera(raytracerMatrixStack, true);
        raytracerMatrixStack.update();
        raytracerShaderState.uniform(gl, raytracerPmvMatrixUniform);
        raytracerShaderState.uniform(gl, fovYScaleUniform);
        raytracerShaderState.uniform(gl, aspectUniform);
        raytracerShaderState.uniform(gl, farNearUniform);
        
        raytracerObject.draw(gl);
        raytracerShaderState.useProgram(gl, false);
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
    
    private void setCamera(PMVMatrix m, boolean full) {
        m.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        m.glLoadIdentity();
        camera.setMatrix(m);
    }
    
    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        if (inError) {
            return;
        }
        
        try {
            final GL2ES2 gl = glad.getGL().getGL2ES2();
            gl.setSwapInterval(1);
            
            shaderState.useProgram(gl, true);
            // Set location in front of camera
            matrixStack.glMatrixMode(PMVMatrix.GL_PROJECTION);
            matrixStack.glLoadIdentity();
            
            // Setup frustum (as in RedSquareES2 JOGL demo)
            
            // compute projection parameters 'normal' perspective
            final float aspect = (float) width / (float) height;
            final float zNear = 1f;
            final float zFar = 100f;
            
            aspectUniform.setData(aspect);
            
            farNear.put(0, zFar);
            farNear.put(1, zNear);
            
            // compute projection parameters 'normal' frustum
            final float top = (float) Math.tan(fovY * ((float) Math.PI) / 360.0f) * zNear;
            final float bottom = -1.0f * top;
            final float left = aspect * bottom;
            final float right = aspect * top;
            
            matrixStack.glFrustumf(left, right, bottom, top, zNear, zFar);
            shaderState.uniform(gl, pmvMatrixUniform);
            shaderState.useProgram(gl, false);
            
            raytracerShaderState.useProgram(gl, true);
            raytracerMatrixStack.glMatrixMode(PMVMatrix.GL_PROJECTION);
            raytracerMatrixStack.glLoadIdentity();
            
            raytracerMatrixStack.glFrustumf(left, right, bottom, top, zNear, zFar);
            raytracerShaderState.uniform(gl, raytracerPmvMatrixUniform);
            raytracerShaderState.useProgram(gl, false);
            
        }
        catch (GLException e) {
            Logger.warn(e);
            check(false);
        }
    }
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        Logger.log("JOGL Dispose");
        final GL2ES2 gl = glad.getGL().getGL2ES2();
        if (!gl.hasGLSL()) {
            return;
        }
        shaderState.destroy(gl);
        matrixStack.destroy();
    }
    
    public void rotate(int i, int j) {
        camera.rotateAboutRight(j / 360.0f);
        
        camera.rotateAboutUp(i / 360.0f);
    }
    
    public void moveCamera(float x, float y, float z) {
        camera.move(x / 3.0f, y / 3.0f, z / 3.0f);
    }
    
}
