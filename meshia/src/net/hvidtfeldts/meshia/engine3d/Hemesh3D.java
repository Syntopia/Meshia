package net.hvidtfeldts.meshia.engine3d;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import net.hvidtfeldts.utils.Logger;
import wblut.geom.WB_Normal3d;
import wblut.hemesh.HEC_Dodecahedron;
import wblut.hemesh.HEM_Extrude;
import wblut.hemesh.HES_CatmullClark;
import wblut.hemesh.HE_Mesh;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderState;

public class Hemesh3D implements Object3D {
    private GLArrayDataServer vertices;
    private GLArrayDataServer normals;
    private GLArrayDataServer colors;
    private final int[] indexBuffer = new int[] { -1 };
    private int indexCount = -1;
    private static boolean WIRE = false;
    
    @Override
    public void init(final GL2ES2 gl, ShaderState st) {
        
        // HE_Mesh mesh = (new HEC_Geodesic(1, 1)).create();
        HE_Mesh mesh = (new HEC_Dodecahedron(1)).create();
        HES_CatmullClark cc = new HES_CatmullClark();
        // HES_PlanarMidEdge cc = new HES_PlanarMidEdge();
        // HES_DooSabin cc = new HES_DooSabin();
        
        HEM_Extrude extrude = new HEM_Extrude().setDistance(0.72678);
        // mesh.modify(extrude);
        
        // mesh.subdivide(cc, 1);
        // mesh.modify(new HEM_Lattice().setDepth(0.1).setWidth(0.6));
        // HE_Mesh mesh = (new HEC_Box(1, 1, 1, 1, 1, 1)).create();
        extrude = new HEM_Extrude().setDistance(0.0123178);
        // mesh.modify(extrude);
        // mesh.modify(new HEM_Lattice().setDepth(0.1).setWidth(0.6));
        
        mesh.subdivide(cc, 2);
        
        // mesh.modify(si);
        // mesh.subdivide(cc, 1);
        // extrude = new HEM_Extrude().setDistance(0.08);
        // mesh.modify(extrude);
        // mesh.subdivide(cc, 1);
        
        // mesh.subdivide(cc, 1);
        
        gl.glGenBuffers(1, indexBuffer, 0);
        float[][] v = mesh.getVerticesAsFloat();
        
        int[][] facesAsInt = mesh.getFacesAsInt();
        Logger.log("Faces: " + facesAsInt.length);
        
        int[] indices = null;
        if (WIRE) {
            int vCount = 0;
            for (int i = 0; i < facesAsInt.length; i++) {
                vCount += facesAsInt[i].length;
            }
            indices = new int[vCount * 2];
            int c = 0;
            for (int i = 0; i < facesAsInt.length; i++) {
                for (int j = 0; j < facesAsInt[i].length - 1; j++) {
                    indices[c++] = facesAsInt[i][j];
                    indices[c++] = facesAsInt[i][j + 1];
                }
            }
        }
        else {
            int tris = 0;
            for (int i = 0; i < facesAsInt.length; i++) {
                tris += (facesAsInt[i].length - 2);
            }
            indices = new int[tris * 3];
            int c = 0;
            for (int i = 0; i < facesAsInt.length; i++) {
                for (int j = 1; j < facesAsInt[i].length - 1; j++) {
                    indices[c++] = facesAsInt[i][0];
                    indices[c++] = facesAsInt[i][j];
                    indices[c++] = facesAsInt[i][j + 1];
                }
            }
        }
        
        // // IntBuffer intBuffer = ByteBuffer.allocateDirect(indices.length * 4).asIntBuffer();
        IntBuffer intBuffer = Buffers.newDirectIntBuffer(indices.length * 4);
        
        intBuffer.put(indices);
        intBuffer.rewind();
        indexCount = indices.length;
        
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER,
                indices.length * 4, intBuffer, GL.GL_STATIC_DRAW);
        // gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        WB_Normal3d[] vertexNormals = mesh.getVertexNormals();
        float[] ver = new float[v.length * 3];
        float[] nor = new float[v.length * 3];
        float[] col = new float[v.length * 4];
        int count = 0;
        int colorCount = 0;
        int verCount = 0;
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < 3; j++) {
                ver[count++] = v[i][j];
            }
            col[colorCount++] = 1.0f;
            col[colorCount++] = 1.0f;
            col[colorCount++] = 1.0f;
            col[colorCount++] = 1.0f;
            nor[verCount++] = (float) vertexNormals[i].x;
            nor[verCount++] = (float) vertexNormals[i].y;
            nor[verCount++] = (float) vertexNormals[i].z;
            
        }
        
        // Allocate Vertex Array
        vertices = GLArrayDataServer.createGLSL("vertex", 3, GL.GL_FLOAT, false, ver.length / 3, GL.GL_STATIC_DRAW);
        for (int i = 0; i < ver.length; i++) {
            vertices.putf(ver[i]);
        }
        vertices.seal(gl, true);
        st.ownAttribute(vertices, true);
        vertices.enableBuffer(gl, false);
        
        // Allocate Color Array
        colors = GLArrayDataServer.createGLSL("color", 4, GL.GL_FLOAT, false, col.length / 4, GL.GL_STATIC_DRAW);
        for (int i = 0; i < col.length; i++) {
            colors.putf(col[i]);
        }
        colors.seal(gl, true);
        st.ownAttribute(colors, true);
        colors.enableBuffer(gl, false);
        
        // Allocate Normal Array
        normals = GLArrayDataServer.createGLSL("normal", 3, GL.GL_FLOAT, false, nor.length / 3, GL.GL_STATIC_DRAW);
        
        for (int i = 0; i < nor.length; i += 1) {
            normals.putf(nor[i]);
        }
        
        normals.seal(gl, true);
        st.ownAttribute(normals, true);
        normals.enableBuffer(gl, false);
    }
    
    @Override
    public void draw(GL2ES2 gl) {
        vertices.enableBuffer(gl, true);
        colors.enableBuffer(gl, true);
        normals.enableBuffer(gl, true);
        
        // gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertices.getElementCount());
        
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
        
        if (WIRE) {
            gl.glDrawElements(
                    // GL.GL_TRIANGLES, // mode
                    GL.GL_LINES, // mode
                    indexCount, // count
                    GL.GL_UNSIGNED_INT, // type
                    0 // element array buffer offset
            );
        }
        else {
            gl.glDrawElements(
                    GL.GL_TRIANGLES, // mode
                    // GL.GL_LINES, // mode
                    indexCount, // count
                    GL.GL_UNSIGNED_INT, // type
                    0 // element array buffer offset
            );
        }
        
        vertices.enableBuffer(gl, false);
        colors.enableBuffer(gl, false);
        normals.enableBuffer(gl, false);
    }
}
