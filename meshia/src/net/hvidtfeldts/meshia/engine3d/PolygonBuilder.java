package net.hvidtfeldts.meshia.engine3d;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import net.hvidtfeldts.meshia.engine3d.Transformator.IdentityTransformator;
import net.hvidtfeldts.meshia.math.Vector3;
import net.hvidtfeldts.utils.Logger;

import org.sunflow.SunflowAPI;
import org.sunflow.core.ParameterList;
import org.sunflow.core.ParameterList.InterpolationType;
import org.sunflow.core.primitive.TriangleMesh;

import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderState;

public class PolygonBuilder implements Object3D, SunflowRenderable {
    private final List<Vector3> positions = new ArrayList<Vector3>();
    private final List<Vector3> normals = new ArrayList<Vector3>();
    private final List<Vector3> colors = new ArrayList<Vector3>();
    private final List<Integer> triangles = new ArrayList<Integer>();
    
    private GLArrayDataServer verticesVBO;
    private GLArrayDataServer normalsVBO;
    private GLArrayDataServer colorsVBO;
    
    private int elementCount;
    
    private Transformator transformator = new IdentityTransformator();
    
    void setTransformator(Transformator transformator) {
        this.transformator = transformator;
    }
    
    void translate(Vector3 t) {
        for (Vector3 v : positions) {
            v.add(t);
        }
    }
    
    /**
     * Adds a vertex with position, normal, and color.
     * 
     * @return the index of the new vertex.
     */
    int addColorVertex(Vector3 pos, Vector3 normal, Vector3 color) {
        positions.add(pos);
        normals.add(normal);
        colors.add(color);
        return positions.size() - 1;
    }
    
    int addVertex(Vector3 pos, Vector3 color, Vector3 c) {
        Vector3 t = transformator.transform(pos);
        positions.add(t);
        Vector3 n = new Vector3(t);
        n.normalize();
        normals.add(n);
        colors.add(color);
        return positions.size() - 1;
    }
    
    void addTriangle(int vertex1, int vertex2, int vertex3) {
        triangles.add(vertex1);
        triangles.add(vertex2);
        triangles.add(vertex3);
    }
    
    void addSquare(int vertex1, int vertex2, int vertex3, int vertex4) {
        addTriangle(vertex1, vertex2, vertex3);
        addTriangle(vertex3, vertex4, vertex1);
    }
    
    @Override
    public void init(GL2ES2 gl, ShaderState st) {
        verticesVBO = GLArrayDataServer.createGLSL("vertex", 3, GL.GL_FLOAT, false, triangles.size(), GL.GL_STATIC_DRAW);
        colorsVBO = GLArrayDataServer.createGLSL("color", 4, GL.GL_FLOAT, false, triangles.size(), GL.GL_STATIC_DRAW);
        normalsVBO = GLArrayDataServer.createGLSL("normal", 3, GL.GL_FLOAT, false, triangles.size(), GL.GL_STATIC_DRAW);
        
        elementCount = triangles.size();
        
        for (int j = 0; j < elementCount; j++) {
            int in = triangles.get(j);
            
            verticesVBO.putf(positions.get(in).getX());
            verticesVBO.putf(positions.get(in).getY());
            verticesVBO.putf(positions.get(in).getZ());
            
            colorsVBO.putf(colors.get(in).getX());
            colorsVBO.putf(colors.get(in).getY());
            colorsVBO.putf(colors.get(in).getZ());
            colorsVBO.putf(1.0f);
            
            normalsVBO.putf(normals.get(in).getX());
            normalsVBO.putf(normals.get(in).getY());
            normalsVBO.putf(normals.get(in).getZ());
        }
        
        verticesVBO.seal(gl, true);
        st.ownAttribute(verticesVBO, true);
        verticesVBO.enableBuffer(gl, false);
        
        colorsVBO.seal(gl, true);
        st.ownAttribute(colorsVBO, true);
        colorsVBO.enableBuffer(gl, false);
        
        normalsVBO.seal(gl, true);
        st.ownAttribute(normalsVBO, true);
        normalsVBO.enableBuffer(gl, false);
        /*
         * positions = null;
         * colors = null;
         * normals = null;
         * triangles = null;
         */
        Logger.log(String.format("Created %s polygons", elementCount));
    }
    
    @Override
    public void draw(GL2ES2 gl) {
        verticesVBO.enableBuffer(gl, true);
        colorsVBO.enableBuffer(gl, true);
        normalsVBO.enableBuffer(gl, true);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, elementCount);
        verticesVBO.enableBuffer(gl, false);
        colorsVBO.enableBuffer(gl, false);
        normalsVBO.enableBuffer(gl, false);
    }
    
    @Override
    public TriangleMesh getTriangleMesh(SunflowAPI api) {
        TriangleMesh tm = new TriangleMesh();
        ParameterList pl = new ParameterList();
        
        float[] points = new float[positions.size() * 3];
        for (int i = 0; i < positions.size(); i++) {
            points[i * 3] = positions.get(i).getX();
            points[i * 3 + 1] = positions.get(i).getY();
            points[i * 3 + 2] = positions.get(i).getZ();
        }
        float[] ns = new float[normals.size() * 3];
        for (int i = 0; i < normals.size(); i++) {
            ns[i * 3] = normals.get(i).getX();
            ns[i * 3 + 1] = normals.get(i).getY();
            ns[i * 3 + 2] = normals.get(i).getZ();
        }
        
        int[] indices = new int[triangles.size()];
        for (int i = 0; i < triangles.size(); i++) {
            indices[i] = triangles.get(i);
        }
        
        pl.addPoints("points", InterpolationType.VERTEX, points);
        pl.addIntegerArray("triangles", indices);
        pl.addVectors("normals", InterpolationType.VERTEX, ns);
        tm.update(pl, api);
        return tm;
    }
}
