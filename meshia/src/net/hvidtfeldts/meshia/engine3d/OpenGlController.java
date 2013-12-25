package net.hvidtfeldts.meshia.engine3d;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLUniformData;

import net.hvidtfeldts.utils.Logger;

import com.jogamp.opengl.JoglVersion;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

public class OpenGlController implements GLEventListener {
    private ShaderState shaderState;
    private PMVMatrix matrixStack;
    private GLUniformData pmvMatrixUniform;
    private long t0;
    private Object3D box;
    private final float aspect = 1.0f;
    
    public OpenGlController() {
    }
    
    @Override
    public void init(GLAutoDrawable glad) {
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
        
        initializeShaders(gl);
        
        setupMatrixStack(gl);
        
        box = new CubeProjectedSphere();
        // box = new Box3D();
        box.init(gl, shaderState);
        
        // OpenGL Render Settings
        gl.glEnable(GL2ES2.GL_DEPTH_TEST);
        shaderState.useProgram(gl, false);
        
        t0 = System.currentTimeMillis();
        Logger.log("Init finished");
        
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
    
    private void initializeShaders(final GL2ES2 gl) {
        final ShaderCode vp0 = ShaderCode.create(gl, GL2ES2.GL_VERTEX_SHADER, this.getClass(), "shaders",
                "shader/bin", "PhongShader", true);
        final ShaderCode fp0 = ShaderCode.create(gl, GL2ES2.GL_FRAGMENT_SHADER, this.getClass(), "shaders",
                "shader/bin", "PhongShader", true);
        vp0.defaultShaderCustomization(gl, true, true);
        fp0.defaultShaderCustomization(gl, true, true);
        final ShaderProgram sp0 = new ShaderProgram();
        sp0.add(gl, vp0, System.err);
        sp0.add(gl, fp0, System.err);
        shaderState.attachShaderProgram(gl, sp0, true);
    }
    
    @Override
    public void display(GLAutoDrawable glad) {
        long t1 = System.currentTimeMillis();
        
        final GL2ES2 gl = glad.getGL().getGL2ES2();
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        shaderState.useProgram(gl, true);
        
        matrixStack.glMatrixMode(PMVMatrix.GL_MODELVIEW);
        matrixStack.glLoadIdentity();
        matrixStack.glTranslatef(0, 0, -10);
        
        float ang = ((t1 - t0) * 360.0F) / 4000.0F;
        matrixStack.glRotatef(ang, 0, 0, 1);
        matrixStack.glRotatef(ang / 1.112f, 0, 1, 0);
        matrixStack.glRotatef(ang / 1.23f, 1, 0, 0);
        matrixStack.glScalef(3, 3, 3);
        matrixStack.update();
        
        // matrixStack.gluUnProject(winx, winy, winz, viewport, viewport_offset, obj_pos, obj_pos_offset)
        
        shaderState.uniform(gl, pmvMatrixUniform);
        box.draw(gl);
        shaderState.useProgram(gl, false);
    }
    
    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
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
    
}
