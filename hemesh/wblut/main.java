package wblut;

import java.util.List;

import wblut.hemesh.HEC_Geodesic;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_Vertex;

public class main {
    
    public static void main(String[] args) {
        // HEC_Box b = new HEC_Box(1, 1, 1, 10, 10, 10);
        HE_Mesh mesh = (new HEC_Geodesic(1, 5)).create();
        List<HE_Face> facesAsList = mesh.getFacesAsList();
        for (HE_Face f : facesAsList) {
            // System.out.println("..");
            List<HE_Vertex> faceVertices = f.getFaceVertices();
            for (HE_Vertex v : faceVertices) {
                // System.out.println(v);
            }
        }
        System.out.println("" + facesAsList.size());
    }
    
}
