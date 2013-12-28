package net.hvidtfeldts.meshia.engine3d;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderState;

public class FullviewportObject {
    private GLArrayDataServer vertices;
    
    public void init(final GL2ES2 gl, ShaderState st) {
        float[] ver = new float[] {
                -1, -1, 0, /* */1, -1, 0, /* */1, 1, 0,
                1, 1, 0, /* */-1, 1, 0, /* */-1, -1, 0,
        };
        
        // Allocate Vertex Array
        vertices = GLArrayDataServer.createGLSL("vertex", 3, GL.GL_FLOAT, false, ver.length / 3, GL.GL_STATIC_DRAW);
        for (int i = 0; i < ver.length; i++) {
            vertices.putf(ver[i]);
        }
        vertices.seal(gl, true);
        st.ownAttribute(vertices, true);
        vertices.enableBuffer(gl, false);
    }
    
    public void draw(GL2ES2 gl) {
        vertices.enableBuffer(gl, true);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.getElementCount());
        vertices.enableBuffer(gl, false);
    }
}
