/**
 * 
 */
package wblut.hemesh;

import java.util.Collection;
import java.util.Iterator;

import wblut.geom.WB_Point3d;


import javolution.util.FastList;

// TODO: Auto-generated Javadoc
/**
 * The Class HEC_FromVoronoiCells.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class HEC_FromVoronoiCells extends HEC_Creator {
	
	/** The cells. */
	private HE_Mesh[]	cells;
	
	/** The on. */
	private boolean[]	on;

	/**
	 * Instantiates a new hE c_ from voronoi cells.
	 */
	public HEC_FromVoronoiCells() {
		super();
		override = true;
		cells = null;
		on = null;
	}

	/**
	 * Sets the cells.
	 *
	 * @param cells the cells
	 * @return the hE c_ from voronoi cells
	 */
	public HEC_FromVoronoiCells setCells(final HE_Mesh[] cells) {
		this.cells = cells;
		return this;
	}

	/**
	 * Sets the cells.
	 *
	 * @param cells the cells
	 * @return the hE c_ from voronoi cells
	 */
	public HEC_FromVoronoiCells setCells(final Collection<HE_Mesh> cells) {
		this.cells = new HE_Mesh[cells.size()];
		final int i = 0;
		for (final HE_Mesh cell : cells) {
			this.cells[i] = cell;
		}
		return this;
	}

	/**
	 * Sets the active.
	 *
	 * @param on the on
	 * @return the hE c_ from voronoi cells
	 */
	public HEC_FromVoronoiCells setActive(final boolean[] on) {
		this.on = on;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.creators.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (cells == null) {
			return new HE_Mesh();
		}
		if (on == null) {
			return new HE_Mesh();
		}
		if (on.length > cells.length) {
			return new HE_Mesh();
		}
		final int n = on.length;
		final FastList<HE_Face> tmpfaces = new FastList<HE_Face>();
		int nv = 0;
		for (int i = 0; i < n; i++) {
			final HE_Mesh m = cells[i];
			if (on[i]) {
				final Iterator<HE_Face> fItr = m.fItr();
				while (fItr.hasNext()) {

					final HE_Face f = fItr.next();
					if (f.getLabel() == -1) {
						tmpfaces.add(f);
						nv += f.getFaceOrder();
					} else if (!on[f.getLabel()]) {
						tmpfaces.add(f);
						nv += f.getFaceOrder();
					}

				}
			}
		}
		final WB_Point3d[] vertices = new WB_Point3d[nv];
		final int[][] faces = new int[tmpfaces.size()][];
		final int[] labels = new int[tmpfaces.size()];
		int cid = 0;
		for (int i = 0; i < tmpfaces.size(); i++) {
			final HE_Face f = tmpfaces.get(i);
			faces[i] = new int[f.getFaceOrder()];
			labels[i] = f.getLabel();
			HE_Halfedge he = f.getHalfedge();
			for (int j = 0; j < f.getFaceOrder(); j++) {
				vertices[cid] = he.getVertex();
				faces[i][j] = cid;
				he = he.getNextInFace();
				cid++;
			}
		}
		final HEC_FromFacelist ffl = new HEC_FromFacelist()
				.setVertices(vertices).setFaces(faces).setDuplicate(true);
		final HE_Mesh result = ffl.createBase();
		final Iterator<HE_Face> fItr = result.fItr();
		int i = 0;
		while (fItr.hasNext()) {
			fItr.next().setLabel(labels[i]);
			i++;
		}
		return result;
	}

}
