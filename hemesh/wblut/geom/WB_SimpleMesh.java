/**
 * 
 */
package wblut.geom;

import java.util.Collection;
import java.util.Iterator;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_SimpleMesh.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_SimpleMesh {
	
	/** The vertices. */
	private WB_Point3d[]	vertices;
	
	/** The faces. */
	private int[][]		faces;

	/**
	 * Instantiates a new w b_ simple mesh.
	 *
	 * @param vs the vs
	 * @param fs the fs
	 */
	public WB_SimpleMesh(final WB_Point3d[] vs, final int[][] fs) {
		setVertices(vs);
		setFaces(fs);

	}

	/**
	 * Instantiates a new w b_ simple mesh.
	 *
	 * @param vs the vs
	 * @param fs the fs
	 */
	public WB_SimpleMesh(final Collection<WB_Point3d> vs, final int[][] fs) {
		setVertices(vs);
		setFaces(fs);

	}

	/**
	 * Instantiates a new w b_ simple mesh.
	 *
	 * @param vs the vs
	 * @param fs the fs
	 */
	public WB_SimpleMesh(final double[][] vs, final int[][] fs) {
		setVertices(vs);
		setFaces(fs);

	}

	/**
	 * Instantiates a new w b_ simple mesh.
	 *
	 * @param vs the vs
	 * @param fs the fs
	 */
	public WB_SimpleMesh(final float[][] vs, final int[][] fs) {
		setVertices(vs);
		setFaces(fs);

	}

	/**
	 * Instantiates a new w b_ simple mesh.
	 *
	 * @param vs the vs
	 * @param fs the fs
	 */
	public WB_SimpleMesh(final float[] vs, final int[][] fs) {
		setVertices(vs);
		setFaces(fs);

	}

	/**
	 * Instantiates a new w b_ simple mesh.
	 *
	 * @param vs the vs
	 * @param fs the fs
	 */
	public WB_SimpleMesh(final double[] vs, final int[][] fs) {
		setVertices(vs);
		setFaces(fs);

	}

	/**
	 * Sets the vertices.
	 *
	 * @param vs the new vertices
	 */
	public void setVertices(final WB_Point3d[] vs) {
		vertices = vs;
	}

	/**
	 * Sets the vertices.
	 *
	 * @param vs the new vertices
	 */
	public void setVertices(final Collection<WB_Point3d> vs) {

		final int n = vs.size();
		final Iterator<WB_Point3d> itr = vs.iterator();
		vertices = new WB_Point3d[n];
		int i = 0;
		while (itr.hasNext()) {
			vertices[i] = itr.next();
			i++;
		}
	}

	/**
	 * Sets the vertices.
	 *
	 * @param vs the vs
	 * @param copy the copy
	 */
	public void setVertices(final WB_Point3d[] vs, final boolean copy) {
		if (copy) {
			final int n = vs.length;

			vertices = new WB_Point3d[n];
			for (int i = 0; i < n; i++) {
				vertices[i] = new WB_Point3d(vs[i]);

			}
		} else {
			vertices = vs;
		}
	}

	/**
	 * Sets the vertices.
	 *
	 * @param vs the new vertices
	 */
	public void setVertices(final double[][] vs) {
		final int n = vs.length;
		vertices = new WB_Point3d[n];
		for (int i = 0; i < n; i++) {
			vertices[i] = new WB_Point3d(vs[i][0], vs[i][1], vs[i][2]);

		}
	}

	/**
	 * Sets the vertices.
	 *
	 * @param vs the new vertices
	 */
	public void setVertices(final double[] vs) {
		final int n = vs.length;
		vertices = new WB_Point3d[n / 3];
		for (int i = 0; i < n; i += 3) {
			vertices[i] = new WB_Point3d(vs[i], vs[i + 1], vs[i + 2]);

		}
	}

	/**
	 * Sets the vertices.
	 *
	 * @param vs the new vertices
	 */
	public void setVertices(final float[][] vs) {
		final int n = vs.length;
		vertices = new WB_Point3d[n];
		for (int i = 0; i < n; i++) {
			vertices[i] = new WB_Point3d(vs[i][0], vs[i][1], vs[i][2]);

		}
	}

	/**
	 * Sets the vertices.
	 *
	 * @param vs the new vertices
	 */
	public void setVertices(final float[] vs) {
		final int n = vs.length;
		vertices = new WB_Point3d[n / 3];
		for (int i = 0; i < n; i += 3) {
			vertices[i] = new WB_Point3d(vs[i], vs[i + 1], vs[i + 2]);

		}
	}

	/**
	 * Sets the faces.
	 *
	 * @param fs the new faces
	 */
	public void setFaces(final int[][] fs) {
		faces = fs;
	}

	/**
	 * Gets the faces.
	 *
	 * @return the faces
	 */
	public int[][] getFaces() {
		return faces;
	}

	/**
	 * Gets the vertices.
	 *
	 * @return the vertices
	 */
	public WB_Point3d[] getVertices() {
		return vertices;
	}

	/**
	 * Gets the face.
	 *
	 * @param i the i
	 * @return the face
	 */
	public int[] getFace(final int i) {
		return faces[i];
	}

	/**
	 * Gets the vertex.
	 *
	 * @param i the i
	 * @return the vertex
	 */
	public WB_Point3d getVertex(final int i) {
		return vertices[i];
	}

}
