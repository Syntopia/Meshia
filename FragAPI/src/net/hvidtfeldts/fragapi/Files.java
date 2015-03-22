package net.hvidtfeldts.fragapi;

import java.io.IOException;

import com.jogamp.opengl.util.glsl.ShaderCode;

public final class Files {
    private Files() {
    };
    
    public static StringBuilder read(String fileName) {
        try {
            return (StringBuilder) ShaderCode.readShaderSource(FrameBufferBase.class, "Shaders/" + fileName, true);
        }
        catch (IOException e) {
            throw new RuntimeException(fileName);
        }
    }
    
}
