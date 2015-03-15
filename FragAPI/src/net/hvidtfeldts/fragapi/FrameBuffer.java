package net.hvidtfeldts.fragapi;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLUniformData;

import net.hvidtfeldts.utils.Logger;

import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;

public class FrameBuffer {
    private int[] colors;
    private int[] depths;
    private int[] fbos;
    private GLArrayDataServer vertices;
    private ShaderState shaderState;
    private double relativeSize = 1.0;
    private int[] absoluteSize;
    private CharSequence vertexShader;
    private CharSequence fragmentShader;
    private boolean offscreenBuffer = true;
    private GLUniformData tilesUniform;
    private final List<FrameBuffer> previousBuffers = new ArrayList<>();
    private final FloatBuffer fb = FloatBuffer.allocate(2);
    private int counter;
    private int lastTexture;
    
    FrameBuffer() {
    }
    
    public FrameBuffer setRelativeSize(double relativeSize) {
        this.relativeSize = relativeSize;
        absoluteSize = null;
        return this;
    }
    
    public FrameBuffer setAbsoluteSize(int x, int y) {
        this.relativeSize = 0;
        absoluteSize = new int[] { x, y };
        return this;
    }
    
    public int getWidth() {
        if (absoluteSize != null)
            return absoluteSize[0];
        
        return (int) (Defaults.getWidth() * relativeSize);
    }
    
    public int getHeight() {
        if (absoluteSize != null)
            return absoluteSize[1];
        
        return (int) (Defaults.getHeight() * relativeSize);
        
    }
    
    public FrameBuffer setVertexShader(CharSequence shader) {
        this.vertexShader = shader;
        return this;
    }
    
    public FrameBuffer setFragmentShader(CharSequence shader) {
        this.fragmentShader = shader;
        return this;
    }
    
    public void init(GL2ES2 gl) {
        colors = new int[] { -1 };
        depths = new int[] { -1 };
        fbos = new int[] { -1 };
        
        if (offscreenBuffer) {
            createFBO(gl);
        }
        
        float[] ver = new float[] {
                -1, -1, 0, /* */1, -1, 0, /* */1, 1, 0,
                1, 1, 0, /* */-1, 1, 0, /* */-1, -1, 0,
        };
        shaderState = new ShaderState();
        shaderState.setVerbose(true);
        
        // Allocate Vertex Array
        vertices = GLArrayDataServer.createGLSL("vertex", 3, GL.GL_FLOAT, false, ver.length / 3, GL.GL_STATIC_DRAW);
        for (int i = 0; i < ver.length; i++) {
            vertices.putf(ver[i]);
        }
        vertices.seal(gl, true);
        this.shaderState.ownAttribute(vertices, true);
        
        CharSequence[][] vertexSource = new CharSequence[1][1];
        vertexSource[0][0] = vertexShader;
        final ShaderCode vp1 = new ShaderCode(GL2ES2.GL_VERTEX_SHADER, 1, vertexSource);
        
        CharSequence[][] fragmentSource = new CharSequence[1][1];
        fragmentSource[0][0] = fragmentShader;
        final ShaderCode fp1 = new ShaderCode(GL2ES2.GL_FRAGMENT_SHADER, 1, fragmentSource);
        vp1.defaultShaderCustomization(gl, true, true);
        fp1.defaultShaderCustomization(gl, true, true);
        final ShaderProgram sp1 = new ShaderProgram();
        check(sp1.add(gl, vp1, Logger.getLoggerWarnStream()));
        check(sp1.add(gl, fp1, Logger.getLoggerWarnStream()));
        check(shaderState.attachShaderProgram(gl, sp1, false));
        
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
        
    }
    
    private void createFBO(GL2ES2 gl) {
        gl.glGenTextures(1, colors, 0);
        
        int width = getWidth();
        int height = getHeight();
        gl.glBindTexture(GL.GL_TEXTURE_2D, colors[0]);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, width, height, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE, null);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        
        gl.glGenRenderbuffers(1, depths, 0);
        gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depths[0]);
        gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL2ES2.GL_DEPTH_COMPONENT, width, height);
        gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
        
        gl.glGenFramebuffers(1, fbos, 0);
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbos[0]);
        gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, colors[0], 0);
        gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depths[0]);
        
        Logger.log("Created FBO: Color=" + colors[0] + " depth=" + depths[0] + " fbo=" + fbos[0]);
    }
    
    private void check(boolean returnValue) {
        if (!returnValue) {
            throw new IllegalStateException();
        }
    }
    
    public void draw(GL2ES2 gl) {
        for (FrameBuffer fb : previousBuffers) {
            fb.draw(gl);
            lastTexture = fb.getTexture();
        }
        internalDraw(gl);
    }
    
    public void internalDraw(GL2ES2 gl) {
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, offscreenBuffer ? fbos[0] : 0);
        gl.glViewport(0, 0, getWidth(), getHeight());
        shaderState.useProgram(gl, true);
        
        if (!offscreenBuffer) {
            gl.glActiveTexture(GL.GL_TEXTURE0);
            gl.glBindTexture(GL.GL_TEXTURE_2D, lastTexture);
            int i = shaderState.getUniformLocation(gl, "bufferx");
            gl.glUniform1i(i, 0);
        }
        
        if (tilesUniform == null) {
            // tilesUniform = new GLUniformData("tiles", 2, fb);
            // tilesUniform.setData(fb);
        }
        fb.array()[0] = counter++ % 9;
        fb.array()[1] = 3;
        // shaderState.uniform(gl, tilesUniform);
        gl.glClearColor(0, 0, 1, 1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glDisable(GL.GL_DEPTH_TEST);
        vertices.enableBuffer(gl, true);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.getElementCount());
        vertices.enableBuffer(gl, false);
        
        shaderState.useProgram(gl, false);
    }
    
    public int getTexture() {
        return colors[0];
    }
    
    public ShaderState getShaderState() {
        return shaderState;
    }
    
    public FrameBuffer setAsOutputBuffer() {
        offscreenBuffer = false;
        return this;
    }
    
    public FrameBuffer setSampler2D(FrameBuffer fp, String textureName) {
        previousBuffers.add(fp);
        return this;
    }
    
    public void initRecursive(GL2ES2 gl) {
        for (FrameBuffer fb : previousBuffers) {
            fb.initRecursive(gl);
        }
        init(gl);
    }
    
    public List<FrameBuffer> getPreviousBuffers() {
        return previousBuffers;
    }
}
