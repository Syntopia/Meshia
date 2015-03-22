package net.hvidtfeldts.fragapi;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLException;
import javax.media.opengl.GLUniformData;

import net.hvidtfeldts.utils.Logger;

import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import com.jogamp.opengl.util.glsl.ShaderState;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class FrameBufferBase implements FrameBuffer {
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
    private final List<TextureData> textureData = new ArrayList<>();
    
    private class TextureData {
        private final String uniformName;
        private int textureID = -1;
        private FrameBuffer frameBuffer;
        private Texture texture;
        private String fileName;
        
        public TextureData(String uniformName, FrameBuffer fp) {
            this.uniformName = uniformName;
            this.frameBuffer = fp;
        }
        
        public TextureData(String uniformName, String textureFileName) {
            this.uniformName = uniformName;
            this.fileName = textureFileName;
        }
        
        public String getUniformName() {
            return uniformName;
        }
        
        public int getTextureID() {
            if (textureID == -1) {
                if (frameBuffer != null) {
                    textureID = frameBuffer.getTexture();
                    Logger.log("TextureID from framebuffer:" + textureID);
                }
                else if (texture == null) {
                    
                    try {
                        texture = TextureIO.newTexture(new File(fileName), false);
                    }
                    catch (GLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    textureID = texture.getTextureObject();
                    
                    Logger.log("TextureID from texture:" + textureID);
                }
            }
            return textureID;
        }
        
    }
    
    FrameBufferBase() {
    }
    
    public static FrameBuffer create(String fileName) {
        FrameBuffer fb = new FrameBufferBase();
        fb.setVertexShader(Files.read("PostFXShader.vp"));
        fb.setFragmentShader(Files.read(fileName));
        return fb;
    }
    
    public static FrameBuffer create3D(String fileName) {
        FrameBuffer fb = new FrameBufferBase();
        fb.setVertexShader(Files.read("Simple3D.vp"));
        fb.setFragmentShader(Files.read(fileName));
        return fb;
    }
    
    @Override
    public FrameBuffer setRelativeSize(double relativeSize) {
        this.relativeSize = relativeSize;
        absoluteSize = null;
        return this;
    }
    
    @Override
    public FrameBuffer setAbsoluteSize(int x, int y) {
        this.relativeSize = 0;
        absoluteSize = new int[] { x, y };
        return this;
    }
    
    @Override
    public int getWidth() {
        if (absoluteSize != null)
            return absoluteSize[0];
        
        return (int) (Defaults.getWidth() * relativeSize);
    }
    
    @Override
    public int getHeight() {
        if (absoluteSize != null)
            return absoluteSize[1];
        
        return (int) (Defaults.getHeight() * relativeSize);
        
    }
    
    @Override
    public FrameBuffer setVertexShader(CharSequence shader) {
        this.vertexShader = shader;
        return this;
    }
    
    @Override
    public FrameBuffer setFragmentShader(CharSequence shader) {
        this.fragmentShader = shader;
        return this;
    }
    
    private void internalInit(GL2ES2 gl) {
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
    
    @Override
    public void draw(GL2ES2 gl) {
        for (FrameBuffer fb : previousBuffers) {
            fb.draw(gl);
        }
        internalDraw(gl);
    }
    
    private void internalDraw(GL2ES2 gl) {
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, offscreenBuffer ? fbos[0] : 0);
        gl.glViewport(0, 0, getWidth(), getHeight());
        shaderState.useProgram(gl, true);
        
        int textureCounter = 0;
        for (TextureData td : textureData) {
            String uniformName = td.getUniformName();
            int textureID = td.getTextureID();
            gl.glActiveTexture(GL.GL_TEXTURE0 + textureCounter);
            gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
            int i = shaderState.getUniformLocation(gl, uniformName);
            gl.glUniform1i(i, textureCounter);
            textureCounter++;
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
    
    @Override
    public int getTexture() {
        return colors[0];
    }
    
    @Override
    public ShaderState getShaderState() {
        return shaderState;
    }
    
    @Override
    public FrameBuffer setAsOutputBuffer() {
        offscreenBuffer = false;
        return this;
    }
    
    @Override
    public FrameBuffer setSampler2D(String uniformName, FrameBuffer fp) {
        previousBuffers.add(fp);
        textureData.add(new TextureData(uniformName, fp));
        return this;
    }
    
    public FrameBuffer setSampler2D(String uniformName) {
        textureData.add(new TextureData(uniformName, this));
        return this;
    }
    
    @Override
    public FrameBuffer setSampler2D(String uniformName, String textureFileName) {
        textureData.add(new TextureData(uniformName, textureFileName));
        return this;
    }
    
    @Override
    public void init(GL2ES2 gl) {
        for (FrameBuffer fb : previousBuffers) {
            fb.init(gl);
        }
        internalInit(gl);
    }
    
    @Override
    public List<FrameBuffer> getPreviousBuffers() {
        return previousBuffers;
    }
    
    @Override
    public void show() {
        FrameBufferWindow.show(this);
    }
}
