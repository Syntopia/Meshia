package net.hvidtfeldts.meshia.engine3d;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderState;

/**
 * Simple box
 */
public class Box3D extends AbstractObject3D {
    
    private GLArrayDataServer vertices;
    private GLArrayDataServer normals;
    private GLArrayDataServer colors;
    
    protected Box3D(ShaderState shaderState, String name) {
        super(shaderState, name);
    }
    
    @Override
    public void internalInit(final GL2ES2 gl) {
        float[] ver = new float[] {
                0, 0, 0, /* */1, 0, 0, /* */0, 1, 0,
                1, 0, 0, /* */1, 1, 0, /* */0, 1, 0,
                
                0, 0, 1, /* */1, 0, 1, /* */0, 1, 1,
                1, 0, 1, /* */1, 1, 1, /* */0, 1, 1,
                
                /* */
                
                0, 0, 0, /* */0, 1, 0, /* */0, 0, 1,
                0, 1, 0, /* */0, 1, 1, /* */0, 0, 1,
                
                1, 0, 0, /* */1, 1, 0, /* */1, 0, 1,
                1, 1, 0, /* */1, 1, 1, /* */1, 0, 1,
                
                /* */
                
                0, 0, 0, /* */1, 0, 1, /* */0, 0, 1,
                0, 0, 0, /* */1, 0, 0, /* */1, 0, 1,
                
                0, 1, 0, /* */1, 1, 1, /* */0, 1, 1,
                0, 1, 0, /* */1, 1, 0, /* */1, 1, 1,
        };
        
        float[] nor = new float[] {
                0, 0, -1,
                0, 0, 1,
                /* */
                
                -1, 0, 0,
                1, 0, 0,
                
                /* */
                
                0, -1, 0,
                0, 1, 0,
        };
        
        float[] col = new float[] {
                1, 0, 0, 1,/* */0, 1, 0, 1, /* */0, 0, 1, 1,
                1, 1, 1, 1,/* */1, 1, 1, 1, /* */1, 1, 1, 1,
                
                1, 0, 0, 1,/* */0, 1, 0, 1, /* */0, 0, 1, 1,
                1, 1, 1, 1,/* */1, 1, 1, 1, /* */1, 1, 1, 1,
                
                1, 0, 0, 1,/* */0, 1, 0, 1, /* */0, 0, 1, 1,
                1, 1, 1, 1,/* */1, 1, 1, 1, /* */1, 1, 1, 1,
                
                1, 0, 0, 1,/* */0, 1, 0, 1, /* */0, 0, 1, 1,
                1, 1, 1, 1,/* */1, 1, 1, 1, /* */1, 1, 1, 1,
                
                1, 0, 0, 1,/* */0, 1, 0, 1, /* */0, 0, 1, 1,
                1, 0, 0, 1,/* */1, 0, 0, 1, /* */1, 0, 0, 1,
                
                1, 0, 0, 1,/* */0, 1, 0, 1, /* */0, 0, 1, 1,
                1, 0, 0, 1,/* */1, 0, 0, 1, /* */1, 0, 0, 1,
        };
        
        // Allocate Vertex Array
        vertices = GLArrayDataServer.createGLSL("vertex", 3, GL.GL_FLOAT, false, ver.length / 3, GL.GL_STATIC_DRAW);
        
        for (int i = 0; i < ver.length; i++) {
            if (i % 3 == 2) {
                vertices.putf(ver[i] * 6.0f - 3.0f - 5.0f);
            }
            else
                vertices.putf(ver[i] * 0.5f - 0.25f);
        }
        vertices.seal(gl, true);
        shaderState.ownAttribute(vertices, true);
        vertices.enableBuffer(gl, false);
        
        // Allocate Color Array
        colors = GLArrayDataServer.createGLSL("color", 4, GL.GL_FLOAT, false, col.length / 4, GL.GL_STATIC_DRAW);
        
        for (int i = 0; i < col.length; i++) {
            colors.putf(1.0f);
        }
        colors.seal(gl, true);
        shaderState.ownAttribute(colors, true);
        colors.enableBuffer(gl, false);
        
        // Allocate Normal Array
        normals = GLArrayDataServer.createGLSL("normal", 3, GL.GL_FLOAT, false, col.length / 3, GL.GL_STATIC_DRAW);
        
        for (int i = 0; i < nor.length; i += 3) {
            for (int j = 0; j < 6; j++) {
                normals.putf(nor[i]);
                normals.putf(nor[i + 1]);
                normals.putf(nor[i + 2]);
            }
        }
        
        normals.seal(gl, true);
        shaderState.ownAttribute(normals, true);
        normals.enableBuffer(gl, false);
    }
    
    @Override
    public void internalDraw(GL2ES2 gl) {
        vertices.enableBuffer(gl, true);
        colors.enableBuffer(gl, true);
        normals.enableBuffer(gl, true);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.getElementCount());
        vertices.enableBuffer(gl, false);
        colors.enableBuffer(gl, false);
        normals.enableBuffer(gl, false);
    }
}
