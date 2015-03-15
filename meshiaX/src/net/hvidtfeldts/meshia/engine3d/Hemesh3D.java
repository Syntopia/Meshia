package net.hvidtfeldts.meshia.engine3d;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import net.hvidtfeldts.utils.Logger;

import org.sunflow.SunflowAPI;
import org.sunflow.core.ParameterList;
import org.sunflow.core.ParameterList.InterpolationType;
import org.sunflow.core.primitive.TriangleMesh;

import wblut.geom.WB_Normal3d;
import wblut.hemesh.HEC_Icosahedron;
import wblut.hemesh.HE_Mesh;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderState;

public class Hemesh3D extends AbstractObject3D implements SunflowRenderable {
    protected Hemesh3D(ShaderState shaderState, String name) {
        super(shaderState, name);
    }
    
    private GLArrayDataServer vertices;
    private GLArrayDataServer normals;
    private GLArrayDataServer colors;
    private final int[] indexBuffer = new int[] { -1 };
    private int indexCount = -1;
    private static boolean WIRE = false;
    
    float[] vertexArray;
    float[] normalArray;
    float[] colorArray;
    int[] indexArray;
    
    @Override
    public void internalInit(final GL2ES2 gl) {
        
        HE_Mesh mesh;
        
        if (true) {
            
            mesh = (new HEC_Icosahedron()).create();
            // mesh = (new HEC_Cube(0.1, 2, 2, 2)).create();
            // double a = 1;
            // double factor = 1.5;
            // for (int i = 0; i < 0; i++) {
            // List<HE_Face> facesAsList = mesh.getFacesAsList();
            // for (HE_Face hf : facesAsList) {
            // WB_Point3d faceCenter = hf.getFaceCenter();
            // WB_Normal3d faceNormal = hf.getFaceNormal();
            // HE_Selection triSplitFace = mesh.triSplitFace(hf, a);
            // }
            
            // a /= -factor;
            // }
            mesh.clean();
            
            // mesh = (new HEC_Johnson(24, 0.5)).create();
            // mesh.modify(new HEM_ChamferCorners().setDistance(0.172678));
            // mesh.modify(new HEM_Extrude().setDistance(0.52372678));
            // mesh.subdivide(new HES_CatmullClark2(), 2);
            // mesh.subdivide(new HES_CatmullClark(), 2);
        }
        
        gl.glGenBuffers(1, indexBuffer, 0);
        float[][] v = mesh.getVerticesAsFloat();
        
        int[][] facesAsInt = mesh.getFacesAsInt();
        Logger.log("Faces: " + facesAsInt.length);
        
        indexArray = null;
        if (WIRE) {
            int vCount = 0;
            for (int i = 0; i < facesAsInt.length; i++) {
                vCount += facesAsInt[i].length;
            }
            indexArray = new int[vCount * 2];
            int c = 0;
            for (int i = 0; i < facesAsInt.length; i++) {
                for (int j = 0; j < facesAsInt[i].length - 1; j++) {
                    indexArray[c++] = facesAsInt[i][j];
                    indexArray[c++] = facesAsInt[i][j + 1];
                }
            }
        }
        else {
            int tris = 0;
            for (int i = 0; i < facesAsInt.length; i++) {
                tris += (facesAsInt[i].length - 2);
            }
            indexArray = new int[tris * 3];
            int c = 0;
            for (int i = 0; i < facesAsInt.length; i++) {
                for (int j = 1; j < facesAsInt[i].length - 1; j++) {
                    indexArray[c++] = facesAsInt[i][0];
                    indexArray[c++] = facesAsInt[i][j];
                    indexArray[c++] = facesAsInt[i][j + 1];
                }
            }
        }
        
        // // IntBuffer intBuffer = ByteBuffer.allocateDirect(indices.length * 4).asIntBuffer();
        IntBuffer intBuffer = Buffers.newDirectIntBuffer(indexArray.length * 4);
        
        intBuffer.put(indexArray);
        intBuffer.rewind();
        indexCount = indexArray.length;
        
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER,
                indexArray.length * 4, intBuffer, GL.GL_STATIC_DRAW);
        // gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        WB_Normal3d[] vertexNormals = mesh.getVertexNormals();
        vertexArray = new float[v.length * 3];
        normalArray = new float[v.length * 3];
        colorArray = new float[v.length * 4];
        int count = 0;
        int colorCount = 0;
        int verCount = 0;
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < 3; j++) {
                vertexArray[count++] = v[i][j];
            }
            colorArray[colorCount++] = 1.0f;
            colorArray[colorCount++] = 1.0f;
            colorArray[colorCount++] = 1.0f;
            colorArray[colorCount++] = 1.0f;
            normalArray[verCount++] = (float) vertexNormals[i].x;
            normalArray[verCount++] = (float) vertexNormals[i].y;
            normalArray[verCount++] = (float) vertexNormals[i].z;
            
        }
        
        // Allocate Vertex Array
        vertices = GLArrayDataServer.createGLSL("vertex", 3, GL.GL_FLOAT, false, vertexArray.length / 3, GL.GL_STATIC_DRAW);
        for (int i = 0; i < vertexArray.length; i++) {
            vertices.putf(vertexArray[i]);
        }
        vertices.seal(gl, true);
        shaderState.ownAttribute(vertices, true);
        vertices.enableBuffer(gl, false);
        
        // Allocate Color Array
        colors = GLArrayDataServer.createGLSL("color", 4, GL.GL_FLOAT, false, colorArray.length / 4, GL.GL_STATIC_DRAW);
        for (int i = 0; i < colorArray.length; i++) {
            colors.putf(colorArray[i]);
        }
        colors.seal(gl, true);
        shaderState.ownAttribute(colors, true);
        colors.enableBuffer(gl, false);
        
        // Allocate Normal Array
        normals = GLArrayDataServer.createGLSL("normal", 3, GL.GL_FLOAT, false, normalArray.length / 3, GL.GL_STATIC_DRAW);
        
        for (int i = 0; i < normalArray.length; i += 1) {
            normals.putf(normalArray[i]);
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
    
    /*
     * (non-Javadoc)
     * 
     * @see net.hvidtfeldts.meshia.engine3d.SunflowRenderable#getTriangleMesh(org.sunflow.SunflowAPI)
     */
    @Override
    public TriangleMesh getTriangleMesh(SunflowAPI api) {
        TriangleMesh tm = new TriangleMesh();
        ParameterList pl = new ParameterList();
        
        pl.addPoints("points", InterpolationType.VERTEX, vertexArray);
        pl.addIntegerArray("triangles", indexArray);
        pl.addVectors("normals", InterpolationType.VERTEX, normalArray);
        tm.update(pl, api);
        return tm;
    }
}
