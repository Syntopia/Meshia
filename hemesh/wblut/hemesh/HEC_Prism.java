/**
 * 
 */
package wblut.hemesh;

import wblut.WB_Epsilon;
import wblut.geom.WB_ExplicitPolygon;
import wblut.geom.WB_Normal3d;
import wblut.geom.WB_Point3d;

// TODO: Auto-generated Javadoc
/**
 * The Class HEC_Prism.
 * 
 * @author Frederik Vanhoutte, W:Blut
 * 
 *         Creates a rectangular prism. If a thickness is specified the result
 *         is a solid, otherwise it's a surface, a regular polygon.
 */
public class HEC_Prism extends HEC_Creator {

	/** The facets. */
	private int facets;

	/** The thickness. */
	private double thickness;

	/** The radius. */
	private double radius;

	/**
	 * Instantiates a new hE c_ prism.
	 */
	public HEC_Prism() {
		super();
	}

	/**
	 * Instantiates a new hE c_ prism.
	 * 
	 * @param n
	 *            the n
	 * @param r
	 *            the r
	 * @param d
	 *            the d
	 */
	public HEC_Prism(final int n, final double r, final double d) {
		this();
		facets = n;
		thickness = d;
		radius = r;
	}

	/**
	 * Sets the facets.
	 * 
	 * @param n
	 *            the n
	 * @return the hE c_ prism
	 */
	public HEC_Prism setFacets(final int n) {
		facets = n;
		return this;
	}

	/**
	 * Sets the height.
	 * 
	 * @param d
	 *            the d
	 * @return the hE c_ prism
	 */
	public HEC_Prism setHeight(final double d) {
		thickness = d;
		return this;
	}

	/**
	 * Sets the radius.
	 * 
	 * @param r
	 *            the r
	 * @return the hE c_ prism
	 */
	public HEC_Prism setRadius(final double r) {
		radius = r;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.creators.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if ((facets < 3) || (WB_Epsilon.isZero(radius))) {
			return null;
		}

		final WB_Point3d[] ppoints = new WB_Point3d[facets];
		for (int i = 0; i < facets; i++) {
			final double x = radius * Math.cos(Math.PI * 2.0 / facets * i);
			final double y = radius * Math.sin(Math.PI * 2.0 / facets * i);
			ppoints[i] = new WB_Point3d(x, y, 0.0);

		}
		final WB_ExplicitPolygon polygon = new WB_ExplicitPolygon(ppoints,
				facets);
		final WB_Normal3d norm = polygon.getPlane().getNormal();
		final int n = polygon.getN();
		final boolean surf = WB_Epsilon.isZero(thickness);
		final WB_Point3d[] points = new WB_Point3d[surf ? n : 2 * n];
		for (int i = 0; i < n; i++) {
			points[i] = polygon.getPoint(i);

		}
		if (!surf) {
			for (int i = 0; i < n; i++) {
				points[n + i] = points[i].addAndCopy(norm, thickness);
			}
		}
		int[][] faces;
		if (surf) {
			faces = new int[1][n];
			for (int i = 0; i < n; i++) {
				faces[0][i] = i;
			}

		} else {
			faces = new int[n + 2][];
			faces[n] = new int[n];
			faces[n + 1] = new int[n];
			for (int i = 0; i < n; i++) {

				faces[n][i] = i;
				faces[n + 1][i] = 2 * n - 1 - i;
				faces[i] = new int[4];
				faces[i][0] = i;
				faces[i][3] = (i + 1) % n;
				faces[i][2] = n + (i + 1) % n;
				faces[i][1] = n + i;

			}

		}
		final HEC_FromFacelist fl = new HEC_FromFacelist();
		fl.setVertices(points).setFaces(faces).setDuplicate(false);
		return fl.createBase().flipAllFaces();

	}

}
