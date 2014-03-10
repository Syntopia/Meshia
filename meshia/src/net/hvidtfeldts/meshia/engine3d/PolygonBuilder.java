package net.hvidtfeldts.meshia.engine3d;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
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

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.GLArrayDataServer;
import com.jogamp.opengl.util.glsl.ShaderState;

public class PolygonBuilder extends AbstractObject3D implements SunflowRenderable {
    private final List<Vector3> positions = new ArrayList<Vector3>();
    private final List<Vector3> normals = new ArrayList<Vector3>();
    private final List<Double> weights = new ArrayList<Double>();
    private final List<Vector3> colors = new ArrayList<Vector3>();
    private final List<Integer> triangles = new ArrayList<Integer>();
    private final int[] indexBuffer = new int[] { -1 };
    
    private GLArrayDataServer verticesVBO;
    private GLArrayDataServer normalsVBO;
    private GLArrayDataServer colorsVBO;
    
    private int elementCount;
    
    private Transformator transformator = new IdentityTransformator();
    
    protected PolygonBuilder(ShaderState shaderState, String name) {
        super(shaderState, name);
    }
    
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
        weights.add(0d);
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
    public void internalInit(GL2ES2 gl) {
        verticesVBO = GLArrayDataServer.createGLSL("vertex", 3, GL.GL_FLOAT, false, triangles.size(), GL.GL_STATIC_DRAW);
        colorsVBO = GLArrayDataServer.createGLSL("color", 4, GL.GL_FLOAT, false, triangles.size(), GL.GL_STATIC_DRAW);
        normalsVBO = GLArrayDataServer.createGLSL("normal", 3, GL.GL_FLOAT, false, triangles.size(), GL.GL_STATIC_DRAW);
        
        elementCount = triangles.size();
        
        for (int j = 0; j < normals.size(); j++) {
            normals.get(j).normalize();
        }
        
        for (int j = 0; j < positions.size(); j++) {
            int in = j;
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
        shaderState.ownAttribute(verticesVBO, true);
        verticesVBO.enableBuffer(gl, false);
        
        colorsVBO.seal(gl, true);
        shaderState.ownAttribute(colorsVBO, true);
        colorsVBO.enableBuffer(gl, false);
        
        normalsVBO.seal(gl, true);
        shaderState.ownAttribute(normalsVBO, true);
        normalsVBO.enableBuffer(gl, false);
        /*
         * positions = null;
         * colors = null;
         * normals = null;
         * triangles = null;
         */
        gl.glGenBuffers(1, indexBuffer, 0);
        
        int[] indexArray = new int[triangles.size()];
        for (int i = 0; i < triangles.size(); i++) {
            indexArray[i] = triangles.get(i);
        }
        
        IntBuffer intBuffer = Buffers.newDirectIntBuffer(indexArray.length);
        intBuffer.put(indexArray);
        intBuffer.rewind();
        
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER,
                indexArray.length * 4, intBuffer, GL.GL_STATIC_DRAW);
        
        Logger.log("Points: " + positions.size() + " normals: " + normals.size() + " indices: " + indexArray.length);
        
        Logger.log(String.format("Created %s triangles, %s edges", elementCount / 3, elementCount));
        
        try {
            File file = new File("e:\\out.obj");
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file);
            
            BufferedWriter bw = new BufferedWriter(fw);
            // toOBJ(bw);
            // toSTL("e:\\out.stl");
            bw.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void internalDraw(GL2ES2 gl) {
        verticesVBO.enableBuffer(gl, true);
        colorsVBO.enableBuffer(gl, true);
        normalsVBO.enableBuffer(gl, true);
        
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);
        gl.glDrawElements(
                GL.GL_TRIANGLES, // mode
                elementCount, // count
                GL.GL_UNSIGNED_INT, // type
                0 // element array buffer offset
        );
        
        verticesVBO.enableBuffer(gl, false);
        colorsVBO.enableBuffer(gl, false);
        normalsVBO.enableBuffer(gl, false);
    }
    
    public void toSTL(String filename) throws FileNotFoundException, IOException {
        /*
         * UINT8[80] – Header
         * UINT32 – Number of triangles
         * 
         * foreach triangle
         * REAL32[3] – Normal vector
         * REAL32[3] – Vertex 1
         * REAL32[3] – Vertex 2
         * REAL32[3] – Vertex 3
         * UINT16 – Attribute byte count
         * end
         */
        try (@SuppressWarnings("resource")
        FileChannel ch = new RandomAccessFile(filename, "rw").getChannel())
        {
            Logger.startTime();
            ByteBuffer bb = ByteBuffer.allocate(1000).order(ByteOrder.LITTLE_ENDIAN);
            for (int i = 0; i < 80; i++) {
                bb.put((byte) 32); // Header with spaces
            }
            bb.putInt(triangles.size() / 3); // Triangle count
            for (int i = 0; i < triangles.size(); i += 3) {
                int i1 = triangles.get(i);
                int i2 = triangles.get(i + 1);
                int i3 = triangles.get(i + 2);
                // Per-face Normal
                Vector3 n = normals.get(i1);
                n.normalize();
                bb.putFloat(n.getX()).putFloat(n.getY()).putFloat(n.getZ());
                
                // Vertices
                bb.putFloat(positions.get(i1).getX()).putFloat(positions.get(i1).getY()).putFloat(positions.get(i1).getZ());
                bb.putFloat(positions.get(i2).getX()).putFloat(positions.get(i2).getY()).putFloat(positions.get(i2).getZ());
                bb.putFloat(positions.get(i3).getX()).putFloat(positions.get(i3).getY()).putFloat(positions.get(i3).getZ());
                // Two zeroes
                bb.putShort((short) 0);
                // Write
                bb.flip();
                ch.write(bb);
                bb.clear();
            }
            Logger.endTime("Wrote STL");
        }
    }
    
    public void toOBJ(Writer sb) throws IOException {
        sb.append(String.format("// Vertices %n"));
        for (Vector3 v : positions) {
            sb.append(String.format("v %s %s %s%n", v.getX(), v.getY(), v.getZ()));
        }
        
        sb.append(String.format("%n// Normals %n"));
        for (Vector3 v : normals) {
            sb.append(String.format("vn %s %s %s%n", v.getX(), v.getY(), v.getZ()));
        }
        
        sb.append(String.format("%n// Faces %n"));
        for (int i = 0; i < triangles.size(); i += 3) {
            int i1 = triangles.get(i) + 1;
            int i2 = triangles.get(i + 1) + 1;
            int i3 = triangles.get(i + 2) + 1;
            sb.append(String.format("f %s//%s %s//%s %s//%s%n", i1, i1, i2, i2, i3, i3));
        }
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
        Logger.log("Points: " + positions.size() + " normals: " + normals.size() + " indices: " + indices.length);
        pl.addPoints("points", InterpolationType.VERTEX, points);
        pl.addIntegerArray("triangles", indices);
        pl.addVectors("normals", InterpolationType.VERTEX, ns);
        tm.update(pl, api);
        return tm;
    }
    
    public void setNormal(int i1, Vector3 normal) {
        Vector3 old = normals.get(i1);
        if (old != null) {
            // normals.set(i1, Vector3.add(normal, old));
        }
        else {
            normals.set(i1, normal);
        }
        
    }
}
