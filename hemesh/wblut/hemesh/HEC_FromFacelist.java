/*
 * Copyright (c) 2010, Frederik Vanhoutte This library is free software; you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * http://creativecommons.org/licenses/LGPL/2.1/ This library is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javolution.util.FastMap;
import wblut.WB_Epsilon;
import wblut.geom.WB_KDNeighbor;
import wblut.geom.WB_KDTree3Dold;
import wblut.geom.WB_Point3d;

// TODO: Auto-generated Javadoc
/**
 * Creates a new mesh from a list of vertices and faces. Vertices can be
 * duplicate.
 * 
 * @author Frederik Vanhoutte (W:Blut)
 * 
 */
public class HEC_FromFacelist extends HEC_Creator {

	/** Vertices. */
	private WB_Point3d[] vertices;

	private boolean useUV;
	private double[][] uv;
	/** Face indices. */
	private int[][] faces;

	/** Duplicate vertices?. */
	private boolean duplicate;

	/** Check face noraml consistency?. */
	private boolean normalcheck;

	/**
	 * Instantiates a new HEC_Facelist.
	 * 
	 */
	public HEC_FromFacelist() {
		super();
		override = true;
		duplicate = true;
		normalcheck = true;
	}

	/**
	 * Set vertex coordinates from an array of WB_point. No copies are made.
	 * 
	 * @param vs
	 *            vertices
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final WB_Point3d[] vs) {
		vertices = vs;
		return this;
	}

	public HEC_FromFacelist setUseUV(boolean b) {
		useUV = b;
		return this;
	}

	public HEC_FromFacelist setUV(double[][] uv) {
		this.uv = uv;
		return this;
	}

	/**
	 * Set vertex coordinates from an arraylist of WB_point.
	 * 
	 * @param vs
	 *            vertices
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final Collection<WB_Point3d> vs) {

		final int n = vs.size();
		final Iterator<WB_Point3d> itr = vs.iterator();
		vertices = new WB_Point3d[n];
		int i = 0;
		while (itr.hasNext()) {
			vertices[i] = itr.next();
			i++;
		}
		return this;
	}

	/**
	 * Set vertex coordinates from an array of WB_point.
	 * 
	 * @param vs
	 *            vertices
	 * @param copy
	 *            copy points?
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final WB_Point3d[] vs,
			final boolean copy) {
		if (copy) {
			final int n = vs.length;

			vertices = new WB_Point3d[n];
			for (int i = 0; i < n; i++) {
				vertices[i] = new WB_Point3d(vs[i]);

			}
		} else {
			vertices = vs;
		}
		return this;
	}

	/**
	 * Set vertex coordinates from a 2D array of double: 1st index=point, 2nd
	 * index (0..2) coordinates
	 * 
	 * @param vs
	 *            Nx3 2D array of coordinates
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final double[][] vs) {
		final int n = vs.length;
		vertices = new WB_Point3d[n];
		for (int i = 0; i < n; i++) {
			vertices[i] = new WB_Point3d(vs[i][0], vs[i][1], vs[i][2]);

		}
		return this;
	}

	/**
	 * Set vertex coordinates from array of double: x0, y0 ,z0 ,x1 ,y1 ,z1 ,...
	 * 
	 * @param vs
	 *            array of coordinates
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final double[] vs) {
		final int n = vs.length;
		vertices = new WB_Point3d[n / 3];
		for (int i = 0; i < n; i += 3) {
			vertices[i] = new WB_Point3d(vs[i], vs[i + 1], vs[i + 2]);

		}
		return this;
	}

	/**
	 * Set vertex coordinates from a 2D array of float: 1st index=point, 2nd
	 * index (0..2) coordinates
	 * 
	 * @param vs
	 *            Nx3 2D array of coordinates
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final float[][] vs) {
		final int n = vs.length;
		vertices = new WB_Point3d[n];
		for (int i = 0; i < n; i++) {
			vertices[i] = new WB_Point3d(vs[i][0], vs[i][1], vs[i][2]);

		}
		return this;
	}

	/**
	 * Set vertex coordinates from array of float: x0, y0 ,z0 ,x1 ,y1 ,z1 ,...
	 * 
	 * @param vs
	 *            array of coordinates
	 * @return self
	 */
	public HEC_FromFacelist setVertices(final float[] vs) {
		final int n = vs.length;
		vertices = new WB_Point3d[n / 3];
		for (int i = 0; i < n; i += 3) {
			vertices[i] = new WB_Point3d(vs[i], vs[i + 1], vs[i + 2]);

		}
		return this;
	}

	/**
	 * Set faces from 2D array of int: 1st index=face, 2nd=index of vertex.
	 * 
	 * @param fs
	 *            2D array of vertex indices
	 * @return self
	 */
	public HEC_FromFacelist setFaces(final int[][] fs) {
		faces = fs;
		return this;
	}

	/**
	 * Set faces from 2D array of int: 1st index=face, 2nd=index of vertex.
	 * 
	 * @param fs
	 *            2D array of vertex indices
	 * @return self
	 */
	public HEC_FromFacelist setFaces(final List<int[]> fs) {
		faces = new int[fs.size()][];
		int i = 0;
		for (final int[] indices : fs) {
			faces[i] = indices;
			i++;
		}
		return this;
	}

	/**
	 * Duplicate vertices in input?.
	 * 
	 * @param b
	 *            true/false
	 * @return self
	 */
	public HEC_FromFacelist setDuplicate(final boolean b) {
		duplicate = b;
		return this;
	}

	/**
	 * Check face normals?.
	 * 
	 * @param b
	 *            true/false
	 * @return self
	 */
	public HEC_FromFacelist setCheckNormals(final boolean b) {
		normalcheck = b;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.HE_Creator#create()
	 */
	@Override
	protected HE_Mesh createBase() {
		final HE_Mesh mesh = new HE_Mesh();

		if ((faces != null) && (vertices != null)) {
			final HE_Vertex[] uniqueVertices = new HE_Vertex[vertices.length];
			if (duplicate) {
				final WB_KDTree3Dold<Integer> kdtree = new WB_KDTree3Dold<Integer>();
				WB_KDNeighbor<Integer>[] neighbors;
				HE_Vertex v = (useUV) ? new HE_Vertex(vertices[0].x,
						vertices[0].y, vertices[0].z, uv[0][0], uv[0][1])
						: new HE_Vertex(vertices[0]);
				kdtree.put(v, 0);
				uniqueVertices[0] = v;
				mesh.add(v);
				int nuv = 1;
				for (int i = 1; i < vertices.length; i++) {
					v = (useUV) ? new HE_Vertex(vertices[i].x, vertices[i].y,
							vertices[i].z, uv[i][0], uv[i][1]) : new HE_Vertex(
							vertices[i]);
					neighbors = kdtree.getNearestNeighbors(v, 1, false);
					if (neighbors[0].sqDistance() < WB_Epsilon.SQEPSILON) {
						uniqueVertices[i] = uniqueVertices[neighbors[0].value()];
					} else {
						kdtree.put(v, i);
						uniqueVertices[i] = v;
						mesh.add(uniqueVertices[i]);
						nuv++;
					}

				}
			} else {
				HE_Vertex v;
				for (int i = 0; i < vertices.length; i++) {
					v = (useUV) ? new HE_Vertex(vertices[i].x, vertices[i].y,
							vertices[i].z, uv[i][0], uv[i][1]) : new HE_Vertex(
							vertices[i]);
					v.setLabel(i);
					uniqueVertices[i] = v;
					mesh.add(uniqueVertices[i]);
				}

			}
			int id = 0;
			HE_Halfedge he;
			if (normalcheck) {

				FastMap<Long, int[]> edges = new FastMap<Long, int[]>();
				for (int i = 0; i < faces.length; i++) {
					int[] face = faces[i];
					int fl = face.length;
					for (int j = 0; j < fl; j++) {
						long ohash = ohash(face[j], face[(j + 1) % fl]);
						int[] faces = edges.get(ohash);
						if (faces == null) {
							edges.put(ohash, new int[] { i, 0 });

						} else {
							faces[1] = i;
						}
					}

				}
				boolean[] visited = new boolean[faces.length];
				LinkedList<Integer> queue = new LinkedList<Integer>();
				boolean facesleft = false;
				int starti = 0;
				do {
					queue.add(starti);
					int temp;
					while (!queue.isEmpty()) {
						facesleft = false;
						Integer index = queue.poll();
						int[] face = faces[index];
						int fl = face.length;
						visited[index] = true;
						for (int j = 0; j < fl; j++) {
							long ohash = ohash(face[j], face[(j + 1) % fl]);
							int[] ns = edges.get(ohash);
							int neighbor;
							if (ns[0] == index) {
								neighbor = ns[1];

							} else {
								neighbor = ns[0];
							}
							if (visited[neighbor] == false) {
								queue.add(neighbor);
								if (consistentOrder(j, (j + 1) % fl, face,
										faces[neighbor]) == -1) {
									int fln = faces[neighbor].length;
									for (int k = 0; k < fln / 2; k++) {
										temp = faces[neighbor][k];
										faces[neighbor][k] = faces[neighbor][fln
												- k - 1];
										faces[neighbor][fln - k - 1] = temp;
									}
								}
							}

						}
					}

					for (; starti < faces.length; starti++) {
						if (!visited[starti]) {
							facesleft = true;
							break;
						}
					}

				} while (facesleft);
			}
			for (final int[] face : faces) {
				final ArrayList<HE_Halfedge> faceEdges = new ArrayList<HE_Halfedge>();
				final HE_Face hef = new HE_Face();
				hef.setLabel(id);
				id++;
				final int fl = face.length;
				int[] locface = new int[fl];
				int li = 0;
				locface[li++] = face[0];
				for (int i = 1; i < fl - 1; i++) {
					if (uniqueVertices[face[i]] != uniqueVertices[face[i - 1]]) {
						locface[li++] = face[i];
					}
				}
				if ((uniqueVertices[face[fl - 1]] != uniqueVertices[face[fl - 2]])
						&& (uniqueVertices[face[fl - 1]] != uniqueVertices[face[0]])) {
					locface[li++] = face[fl - 1];
				}

				if (li > 2) {
					for (int i = 0; i < li; i++) {
						he = new HE_Halfedge();
						faceEdges.add(he);
						he.setFace(hef);
						if (hef.getHalfedge() == null) {
							hef.setHalfedge(he);
						}
						he.setVertex(uniqueVertices[locface[i]]);
						he.getVertex().setHalfedge(he);
					}
					mesh.add(hef);
					HE_Mesh.cycleHalfedges(faceEdges);
					mesh.addHalfedges(faceEdges);
				}
			}
			mesh.pairHalfedges();
			mesh.capHalfedges();
			// mesh.resolvePinchPoints();

		}
		return mesh;
	}

	/**
	 * Hash.
	 * 
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 * @return the long
	 */
	private Long hash(int u, int v) {
		long A = (u >= 0) ? 2 * u : -2 * u - 1;
		long B = (v >= 0) ? 2 * v : -2 * v - 1;
		return (A >= B) ? A * A + A + B : A + B * B;
	}

	/**
	 * Ohash.
	 * 
	 * @param u
	 *            the u
	 * @param v
	 *            the v
	 * @return the long
	 */
	private Long ohash(int u, int v) {
		int lu = u;
		int lv = v;
		if (u > v) {
			lu = v;
			lv = u;
		}

		long A = (lu >= 0) ? 2 * lu : -2 * lu - 1;
		long B = (lv >= 0) ? 2 * lv : -2 * lv - 1;
		return (A >= B) ? A * A + A + B : A + B * B;
	}

	/**
	 * Consistent order.
	 * 
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param face
	 *            the face
	 * @param neighbor
	 *            the neighbor
	 * @return the int
	 */
	private int consistentOrder(int i, int j, int[] face, int[] neighbor) {
		for (int k = 0; k < neighbor.length; k++) {
			if ((neighbor[k] == face[i])
					&& (neighbor[(k + 1) % neighbor.length] == face[j]))
				return -1;
			if ((neighbor[k] == face[j])
					&& (neighbor[(k + 1) % neighbor.length] == face[i]))
				return 1;
		}
		return 0;
	}
}
