package net.hvidtfeldts.meshia.engine3d;

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

public class OpenGlController implements GLEventListener {
    private final Camera camera = new Camera();
    private ShaderState shaderState;
    private ShaderState shaderStateFullscreen;
    private PMVMatrix matrixStack;
    private PMVMatrix fullviewPortMatrixStack;
    private GLUniformData pmvMatrixUniform;
    private GLUniformData fullviewPortPmvMatrixUniform;
    private long t0;
    private Object3D sphere;
    private Object3D box;
    private FullviewportObject fullviewPortObject;
    private final float aspect = 1.0f;
    
    private long lastTime;
    private int frames;
    
    private boolean inError;
    
    public OpenGlController() {
    }
    
    @Override
    public void init(GLAutoDrawable glad) {
        // glad.setGL(new DebugGL2((GL2) glad.getGL()));
        
        try {
            final GL2ES2 gl = glad.getGL().getGL2ES2();
            checkOpenGL(gl, "At init start");
            
            Logger.log("Chosen GLCapabilities: " + glad.getChosenGLCapabilities());
            Logger.log("INIT GL IS: " + gl.getClass().getName());
            Logger.log(JoglVersion.getGLStrings(gl, null, false).toString());
            if (!gl.hasGLSL()) {
                Logger.warn("No GLSL available, no rendering.");
                return;
            }
            
            shaderState = new ShaderState();
            shaderState.setVerbose(true);
            
            shaderStateFullscreen = new ShaderState();
            shaderStateFullscreen.setVerbose(true);
            
            checkOpenGL(gl, "Before shaders");
            
            initializeShaders(gl);
            
            checkOpenGL(gl, "After shaders");
            
            sphere = new CubeProjectedSphere();
            box = new Box3D();
            sphere.init(gl, shaderState);
            box.init(gl, shaderState);
            
            fullviewPortObject = new FullviewportObject();
            fullviewPortObject.init(gl, shaderStateFullscreen);
            
            // OpenGL Render Settings
            gl.glEnable(GL2ES2.GL_DEPTH_TEST);
            shaderState.useProgram(gl, false);
            shaderStateFullscreen.useProgram(gl, false);
            
            t0 = System.currentTimeMillis();
            checkOpenGL(gl, "At init finished");
            
            Logger.log("Init finished");
        }
        catch (GLException e) {
            Logger.warn(e);
            check(false);
        }
    }
    
    private void checkOpenGL(GL2ES2 gl, String string) {
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
    
    private void setupFullviewportMatrixStack(final GL2ES2 gl) {
        fullviewPortMatrixStack = new PMVMatrix();
        fullviewPortMatrixStack.glMatrixMode(PMVMatrix.GL_PROJECTION);
        fullviewPortMatrixStack.glLoadIdentity();
        fullviewPortMatrixStack.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        fullviewPortMatrixStack.glLoadIdentity();
        fullviewPortPmvMatrixUniform = new GLUniformData("pmvMatrix", 4, 4, fullviewPortMatrixStack.glGetPMvMvitMatrixf()); // P, Mv
        shaderStateFullscreen.ownUniform(fullviewPortPmvMatrixUniform);
        shaderStateFullscreen.uniform(gl, fullviewPortPmvMatrixUniform);
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
                "shader/bin", "FullviewportShader", true);
        final ShaderCode fp1 = ShaderCode.create(gl, GL2ES2.GL_FRAGMENT_SHADER, this.getClass(), "shaders",
                "shader/bin", "FullviewportShader", true);
        vp1.defaultShaderCustomization(gl, true, true);
        fp1.defaultShaderCustomization(gl, true, true);
        final ShaderProgram sp1 = new ShaderProgram();
        check(sp1.add(gl, vp1, Logger.getLoggerWarnStream()));
        check(sp1.add(gl, fp1, Logger.getLoggerWarnStream()));
        check(shaderStateFullscreen.attachShaderProgram(gl, sp1, true));
        setupFullviewportMatrixStack(gl);
        
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
        
        long t1 = System.currentTimeMillis();
        
        if (lastTime == 0) {
            lastTime = t1;
        }
        
        if (t1 - lastTime > 5000) {
            float fps = (frames * 1000.0f) / (t1 - lastTime);
            lastTime = t1;
            frames = 0;
            Logger.log("FPS: " + fps);
        }
        frames++;
        
        final GL2ES2 gl = glad.getGL().getGL2ES2();
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        shaderState.useProgram(gl, true);
        
        float ang = ((t1 - t0) * 360.0F) / 4000.0F;
        
        setCamera(matrixStack, ang, false);
        matrixStack.update();
        shaderState.uniform(gl, pmvMatrixUniform);
        
        sphere.draw(gl);
        box.draw(gl);
        shaderState.useProgram(gl, false);
        
        shaderStateFullscreen.useProgram(gl, true);
        
        setCamera(fullviewPortMatrixStack, ang, true);
        fullviewPortMatrixStack.update();
        shaderStateFullscreen.uniform(gl, fullviewPortPmvMatrixUniform);
        
        fullviewPortObject.draw(gl);
        shaderStateFullscreen.useProgram(gl, false);
    }
    
    private void setCamera(PMVMatrix m, float ang, boolean full) {
        m.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        m.glLoadIdentity();
        // camera.setForward(new float[] { (float) Math.cos(ang / 200.0f), 0, (float) Math.sin(ang / 200.0f) });
        // camera.setUp(new float[] { (float) Math.cos(ang / 100.0f), (float) Math.sin(ang / 100.0f), 0 });
        
        if (!full) {
            camera.setMatrixFullShader(m);
            
        }
        else {
            camera.setMatrixFullShader(m);
            
        }
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
            final float fovy = 45f;
            final float aspect2 = ((float) width / (float) height) / aspect;
            final float zNear = 1f;
            final float zFar = 100f;
            
            // compute projection parameters 'normal' frustum
            final float top = (float) Math.tan(fovy * ((float) Math.PI) / 360.0f) * zNear;
            final float bottom = -1.0f * top;
            final float left = aspect2 * bottom;
            final float right = aspect2 * top;
            
            matrixStack.glFrustumf(left, right, bottom, top, zNear, zFar);
            shaderState.uniform(gl, pmvMatrixUniform);
            shaderState.useProgram(gl, false);
            
            shaderStateFullscreen.useProgram(gl, true);
            fullviewPortMatrixStack.glMatrixMode(PMVMatrix.GL_PROJECTION);
            fullviewPortMatrixStack.glLoadIdentity();
            
            fullviewPortMatrixStack.glFrustumf(left, right, bottom, top, zNear, zFar);
            shaderStateFullscreen.uniform(gl, fullviewPortPmvMatrixUniform);
            shaderStateFullscreen.useProgram(gl, false);
            
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
        camera.rotateAboutLeft(j / 360.0f);
        
        camera.rotateAboutUp(i / 360.0f);
    }
    
}
