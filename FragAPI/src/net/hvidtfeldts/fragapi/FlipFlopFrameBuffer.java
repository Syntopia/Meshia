package net.hvidtfeldts.fragapi;

import java.util.List;

import javax.media.opengl.GL2ES2;

import com.jogamp.opengl.util.glsl.ShaderState;

public class FlipFlopFrameBuffer implements FrameBuffer {
    private final FrameBuffer fb1;
    private final FrameBuffer fb2;
    private final String previousTextureName;
    private boolean fb1IsCurrent = true;
    
    FlipFlopFrameBuffer(FrameBuffer original, String previousTextureName) {
        fb1 = original;
        fb2 = original.createCopy();
        fb1.setSampler2D(previousTextureName, fb2, false);
        fb2.setSampler2D(previousTextureName, fb1, false);
        this.previousTextureName = previousTextureName;
    }
    
    private FrameBuffer getCurrent() {
        return fb1IsCurrent ? fb1 : fb2;
    }
    
    private FrameBuffer getPrevious() {
        return fb1IsCurrent ? fb2 : fb1;
    }
    
    @Override
    public FrameBuffer setRelativeSize(double relativeSize) {
        fb1.setRelativeSize(relativeSize);
        fb2.setRelativeSize(relativeSize);
        return this;
    }
    
    @Override
    public FrameBuffer setAbsoluteSize(int x, int y) {
        fb1.setAbsoluteSize(x, y);
        fb2.setAbsoluteSize(x, y);
        return this;
    }
    
    @Override
    public int getWidth() {
        return fb1.getWidth();
    }
    
    @Override
    public int getHeight() {
        return fb1.getHeight();
    }
    
    @Override
    public FrameBuffer setVertexShader(CharSequence shader) {
        fb1.setVertexShader(shader);
        fb2.setVertexShader(shader);
        return this;
    }
    
    @Override
    public FrameBuffer setFragmentShader(CharSequence shader) {
        fb1.setFragmentShader(shader);
        fb2.setFragmentShader(shader);
        return this;
    }
    
    @Override
    public void draw(GL2ES2 gl) {
        getCurrent().draw(gl);
        fb1IsCurrent = !fb1IsCurrent;
    }
    
    @Override
    public int getTexture() {
        return getCurrent().getTexture();
    }
    
    @Override
    public ShaderState getShaderState() {
        return getCurrent().getShaderState();
    }
    
    @Override
    public FrameBuffer setAsOutputBuffer() {
        throw new IllegalStateException("A FlipFlopFrameBuffer can not be used as output buffer.");
    }
    
    @Override
    public FrameBuffer setSampler2D(String uniformName, FrameBuffer fp, boolean requireRedraw) {
        fb1.setSampler2D(uniformName, fp, requireRedraw);
        fb2.setSampler2D(uniformName, fp, requireRedraw);
        return this;
    }
    
    @Override
    public FrameBuffer setSampler2D(String uniformName, String textureFileName) {
        fb1.setSampler2D(uniformName, textureFileName);
        fb2.setSampler2D(uniformName, textureFileName);
        
        return this;
    }
    
    @Override
    public void init(GL2ES2 gl) {
        fb1.init(gl);
        fb2.init(gl);
    }
    
    @Override
    public void show() {
        throw new IllegalStateException("A FlipFlopFrameBuffer can not be used as output buffer.");
    }
    
    @Override
    public List<FrameBuffer> getPreviousBuffers() {
        return getCurrent().getPreviousBuffers();
    }
    
    @Override
    public FrameBuffer createCopy() {
        throw new IllegalStateException("A FlipFlopFrameBuffer can not be cloned.");
    }
    
    @Override
    public FrameBuffer createFlipFlop(String string) {
        throw new IllegalStateException("A FlipFlopFrameBuffer can not be cloned.");
    }
    
    @Override
    public FrameBuffer addCamera(Camera c) {
        fb1.addCamera(c);
        fb2.addCamera(c);
        return this;
    }
    
}
