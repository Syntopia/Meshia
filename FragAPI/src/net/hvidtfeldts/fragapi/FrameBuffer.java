package net.hvidtfeldts.fragapi;

import java.util.List;

import javax.media.opengl.GL2ES2;

import com.jogamp.opengl.util.glsl.ShaderState;

public interface FrameBuffer {
    
    FrameBuffer setRelativeSize(double relativeSize);
    
    FrameBuffer setAbsoluteSize(int x, int y);
    
    int getWidth();
    
    int getHeight();
    
    FrameBuffer setVertexShader(CharSequence shader);
    
    FrameBuffer setFragmentShader(CharSequence shader);
    
    void draw(GL2ES2 gl);
    
    int getTexture();
    
    ShaderState getShaderState();
    
    FrameBuffer setAsOutputBuffer();
    
    FrameBuffer setSampler2D(String uniformName, String textureFilename);
    
    void init(GL2ES2 gl);
    
    void show();
    
    List<FrameBuffer> getPreviousBuffers();
    
    FrameBuffer addCamera(Camera c);
    
    static FrameBuffer create(String fileName) {
        return FrameBufferBase.create(fileName);
    }
    
    static FrameBuffer create3D(String fileName) {
        return FrameBufferBase.create3D(fileName);
    }
    
    default FrameBuffer setSampler2D(String uniformName, FrameBuffer fp) {
        return setSampler2D(uniformName, fp, true);
    }
    
    FrameBuffer createCopy();
    
    default FrameBuffer createFlipFlop(String previousTextureName) {
        return new FlipFlopFrameBuffer(this, previousTextureName);
    }
    
    FrameBuffer setSampler2D(String uniformName, FrameBuffer fp, boolean requireRedraw);
    
}