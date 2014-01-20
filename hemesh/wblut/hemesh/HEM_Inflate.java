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

import java.util.Iterator;

import wblut.geom.WB_AABB3D;
import wblut.geom.WB_KDTree;
import wblut.geom.WB_Point3d;
import wblut.geom.WB_Vector3d;

// TODO: Auto-generated Javadoc
/**
 * Simple Laplacian smooth modifier. Does not add new vertices.
 * 
 * @author Frederik Vanhoutte, W:Blut
 * 
 */
public class HEM_Inflate extends HEM_Modifier {

	/** The auto rescale. */
	private boolean autoRescale;

	/** The iter. */
	private int iter;

	private double radius;

	private double factor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.modifiers.HEB_Modifier#modify(wblut.hemesh.HE_Mesh)
	 */

	public HEM_Inflate() {
		radius = 10;
		factor = 0.1;
	}

	/**
	 * Sets the auto rescale.
	 * 
	 * @param b
	 *            the b
	 * @return the hE m_ smooth
	 */
	public HEM_Inflate setAutoRescale(final boolean b) {
		autoRescale = b;
		return this;

	}

	/**
	 * Sets the iterations.
	 * 
	 * @param r
	 *            the r
	 * @return the hE m_ smooth
	 */
	public HEM_Inflate setIterations(final int r) {
		iter = r;
		return this;

	}

	public HEM_Inflate setRadius(final double r) {
		radius = r;
		return this;

	}

	public HEM_Inflate setFactor(final double f) {
		factor = f;
		return this;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.HEM_Modifier#apply(wblut.hemesh.HE_Mesh)
	 */
	@Override
	public HE_Mesh apply(final HE_Mesh mesh) {
		WB_AABB3D box = new WB_AABB3D();
		if (autoRescale) {
			box = mesh.getAABB();
		}

		WB_KDTree<HE_Vertex, Integer> tree = new WB_KDTree<HE_Vertex, Integer>();
		Iterator<HE_Vertex> vItr = mesh.vItr();
		HE_Vertex v;
		int id = 0;
		while (vItr.hasNext()) {
			tree.add(vItr.next(), id++);
		}
		final WB_Point3d[] newPositions = new WB_Point3d[mesh
				.numberOfVertices()];
		if (iter < 1) {
			iter = 1;
		}
		for (int r = 0; r < iter; r++) {
			vItr = mesh.vItr();
			WB_KDTree.WB_KDEntry<HE_Vertex, Integer>[] neighbors;
			id = 0;
			WB_Vector3d dv;
			while (vItr.hasNext()) {
				v = vItr.next();
				dv = new WB_Vector3d(v);
				neighbors = tree.getRange(v, radius);

				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i].coord != v) {
						WB_Vector3d tmp = neighbors[i].coord.subToVector(v);
						tmp.normalize();
						dv.add(tmp);
					}
				}
				dv.normalize();
				dv.mult(factor);
				newPositions[id] = v.addAndCopy(dv);

				id++;

			}
			vItr = mesh.vItr();
			id = 0;
			while (vItr.hasNext()) {
				vItr.next().set(newPositions[id]);
				id++;
			}

		}
		mesh.resetCenter();
		if (autoRescale) {
			mesh.fitInAABBConstrained(box);
		}
		return mesh;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wblut.hemesh.modifiers.HEB_Modifier#modifySelected(wblut.hemesh.HE_Mesh)
	 */
	@Override
	public HE_Mesh apply(final HE_Selection selection) {
		selection.collectVertices();

		WB_AABB3D box = new WB_AABB3D();
		if (autoRescale) {
			box = selection.parent.getAABB();
		}

		WB_KDTree<HE_Vertex, Integer> tree = new WB_KDTree<HE_Vertex, Integer>();
		Iterator<HE_Vertex> vItr = selection.parent.vItr();
		HE_Vertex v;
		int id = 0;
		while (vItr.hasNext()) {
			tree.add(vItr.next(), id++);
		}
		final WB_Point3d[] newPositions = new WB_Point3d[selection
				.numberOfVertices()];
		if (iter < 1) {
			iter = 1;
		}
		for (int r = 0; r < iter; r++) {
			vItr = selection.vItr();
			HE_Vertex n;
			WB_KDTree.WB_KDEntry<HE_Vertex, Integer>[] neighbors;
			id = 0;

			while (vItr.hasNext()) {
				v = vItr.next();
				WB_Vector3d dv = new WB_Vector3d(v);
				neighbors = tree.getRange(v, radius);
				for (int i = 0; i < neighbors.length; i++) {
					if (neighbors[i].coord != v) {
						WB_Vector3d tmp = neighbors[i].coord.subToVector(v);
						tmp.normalize();
						dv.add(tmp);
					}
				}
				dv.normalize();
				dv.mult(factor);
				newPositions[id] = v.addAndCopy(dv);
				id++;
			}
			vItr = selection.vItr();
			id = 0;
			while (vItr.hasNext()) {
				vItr.next().set(newPositions[id]);
				id++;
			}
		}
		selection.parent.resetCenter();
		if (autoRescale) {
			selection.parent.fitInAABBConstrained(box);
		}
		return selection.parent;
	}

}
