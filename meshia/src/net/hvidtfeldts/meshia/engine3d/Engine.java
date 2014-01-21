package net.hvidtfeldts.meshia.engine3d;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLUniformData;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.hvidtfeldts.utils.Logger;

import com.jogamp.opengl.JoglVersion;
import com.jogamp.opengl.util.GLPixelStorageModes;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.awt.ImageUtil;
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
    private Hemesh3D hemesh;
    private Object3D crossSection;
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
        glad.setGL(new DebugGL2((GL2) glad.getGL()));
        
        try {
            final GL2ES2 gl = glad.getGL().getGL2ES2();
            this.gl = gl;
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
            // box = new Box3D();
            hemesh = new Hemesh3D();
            sphere.init(gl, shaderState);
            hemesh.init(gl, shaderState);
            
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
    
    private void initializeFlatShaders(final GL2ES2 gl, ShaderState shaderState) {
        final ShaderCode vp0 = ShaderCode.create(gl, GL2ES2.GL_VERTEX_SHADER, this.getClass(), "shaders",
                "shader/bin", "CrossSectionShader", true);
        final ShaderCode fp0 = ShaderCode.create(gl, GL2ES2.GL_FRAGMENT_SHADER, this.getClass(), "shaders",
                "shader/bin", "CrossSectionShader", true);
        vp0.defaultShaderCustomization(gl, true, true);
        fp0.defaultShaderCustomization(gl, true, true);
        final ShaderProgram sp0 = new ShaderProgram();
        check(sp0.add(gl, vp0, Logger.getLoggerWarnStream()));
        check(sp0.add(gl, fp0, Logger.getLoggerWarnStream()));
        check(shaderState.attachShaderProgram(gl, sp0, true));
        
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
        this.gl = gl;
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        shaderState.useProgram(gl, true);
        
        setCamera(matrixStack, false);
        matrixStack.update();
        shaderState.uniform(gl, pmvMatrixUniform);
        
        sphere.draw(gl);
        hemesh.draw(gl);
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
            
            matrixStack.gluPerspective(fovY, aspect, zNear, zFar);
            System.out.println(matrixStack.toString());
            shaderState.uniform(gl, pmvMatrixUniform);
            shaderState.useProgram(gl, false);
            
            raytracerShaderState.useProgram(gl, true);
            raytracerMatrixStack.glMatrixMode(PMVMatrix.GL_PROJECTION);
            raytracerMatrixStack.glLoadIdentity();
            
            raytracerMatrixStack.gluPerspective(fovY, aspect, zNear, zFar);
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
    
    GL2ES2 gl;
    
    public void takeSnapshot() {
        
        // GL gl = GLContext.getCurrentGL();
        
        int[] color = new int[] { -1 };
        int[] depth = new int[] { -1 };
        int[] fbo = new int[] { -1 };
        gl.getContext().makeCurrent();
        gl.glGenTextures(1, color, 0);
        Logger.log(color[0]);
        int width = 500;
        int height = 500;
        gl.glBindTexture(GL.GL_TEXTURE_2D, color[0]);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, null);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        
        gl.glGenRenderbuffers(1, depth, 0);
        gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depth[0]);
        gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL2ES2.GL_DEPTH_COMPONENT, width, height);
        gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
        
        gl.glGenFramebuffers(1, fbo, 0);
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
        gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, color[0], 0);
        gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depth[0]);
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
        
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
        Logger.log("Created FBO: Color=" + color[0] + " depth=" + depth[0] + " fbo=" + fbo[0]);
        // Do some drawing here.
        gl.glViewport(0, 0, width, height);
        gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        ShaderState shaderState = new ShaderState();
        shaderState.setVerbose(true);
        initializeFlatShaders(gl, shaderState);
        crossSection = new CrossSection2D();
        crossSection.init(gl, shaderState);
        
        shaderState.useProgram(gl, true);
        crossSection.draw(gl);
        shaderState.useProgram(gl, false);
        
        // Based on JOGL Screenshot.java
        boolean alpha = false;
        int bufImgType = (alpha ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR);
        int readbackType = (alpha ? GL2.GL_ABGR_EXT : GL2GL3.GL_BGR);
        BufferedImage image = new BufferedImage(width, height, bufImgType);
        
        // Set up pixel storage modes
        GLPixelStorageModes psm = new GLPixelStorageModes();
        psm.setPackAlignment(gl, 1);
        
        // read the BGR values into the image
        gl.glReadPixels(0, 0, width, height, readbackType,
                GL.GL_UNSIGNED_BYTE,
                ByteBuffer.wrap(((DataBufferByte) image.getRaster().getDataBuffer()).getData()));
        
        // Restore pixel storage modes
        psm.restore(gl);
        
        if (gl.getContext().getGLDrawable().isGLOriented()) {
            // Must flip BufferedImage vertically for correct results
            ImageUtil.flipImageVertically(image);
        }
        
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
        
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
        
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public Hemesh3D getMesh() {
        return hemesh;
    }
}
