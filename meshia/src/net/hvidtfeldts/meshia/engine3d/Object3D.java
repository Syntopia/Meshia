package net.hvidtfeldts.meshia.engine3d;

import javax.media.opengl.GL2ES2;

import com.jogamp.opengl.util.glsl.ShaderState;

public interface Object3D {
    
    void init(GL2ES2 gl, ShaderState st);
    
    void draw(GL2ES2 gl);
}