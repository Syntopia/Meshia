/**
 * 
 */
package wblut.geom;


// TODO: Auto-generated Javadoc
/**
 * The Class WB_NurbsSwungSurface.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_NurbsSwungSurface {
	
	/**
	 * Gets the swung surface.
	 *
	 * @param xzprofile the xzprofile
	 * @param xytrajectory the xytrajectory
	 * @param alpha the alpha
	 * @return the swung surface
	 */
	public static WB_BSplineSurface getSwungSurface(final WB_BSpline xzprofile,
			final WB_BSpline xytrajectory, final double alpha) {
		final int n = xzprofile.n();
		final int m = xytrajectory.n();
		final WB_Point3d[][] points = new WB_Point3d[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = new WB_Point3d(alpha * xzprofile.points()[i].x
						* xytrajectory.points()[j].x, alpha
						* xzprofile.points()[i].x * xytrajectory.points()[j].y,
						xzprofile.points()[i].z);

			}
		}
		return new WB_BSplineSurface(points, xzprofile.knot(), xytrajectory
				.knot());
	}

	/**
	 * Gets the swung surface.
	 *
	 * @param xzprofile the xzprofile
	 * @param xytrajectory the xytrajectory
	 * @param alpha the alpha
	 * @return the swung surface
	 */
	public static WB_RBSplineSurface getSwungSurface(
			final WB_RBSpline xzprofile, final WB_RBSpline xytrajectory,
			final double alpha) {
		final int n = xzprofile.n();
		final int m = xytrajectory.n();
		final WB_Point3d[][] points = new WB_Point3d[n + 1][m + 1];
		final double[][] weights = new double[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = new WB_Point3d(alpha * xzprofile.points()[i].x
						* xytrajectory.points()[j].x, alpha
						* xzprofile.points()[i].x * xytrajectory.points()[j].y,
						xzprofile.points()[i].z);
				weights[i][j] = xzprofile.weights()[i]
						* xytrajectory.weights()[j];

			}
		}
		return new WB_RBSplineSurface(points, xzprofile.knot(), xytrajectory
				.knot(), weights);
	}

	/**
	 * Gets the swung surface.
	 *
	 * @param xzprofile the xzprofile
	 * @param xytrajectory the xytrajectory
	 * @param alpha the alpha
	 * @return the swung surface
	 */
	public static WB_RBSplineSurface getSwungSurface(
			final WB_BSpline xzprofile, final WB_RBSpline xytrajectory,
			final double alpha) {
		final int n = xzprofile.n();
		final int m = xytrajectory.n();
		final WB_Point3d[][] points = new WB_Point3d[n + 1][m + 1];
		final double[][] weights = new double[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = new WB_Point3d(alpha * xzprofile.points()[i].x
						* xytrajectory.points()[j].x, alpha
						* xzprofile.points()[i].x * xytrajectory.points()[j].y,
						xzprofile.points()[i].z);
				weights[i][j] = xytrajectory.weights()[j];

			}
		}
		return new WB_RBSplineSurface(points, xzprofile.knot(), xytrajectory
				.knot(), weights);
	}

	/**
	 * Gets the swung surface.
	 *
	 * @param xzprofile the xzprofile
	 * @param xytrajectory the xytrajectory
	 * @param alpha the alpha
	 * @return the swung surface
	 */
	public static WB_RBSplineSurface getSwungSurface(
			final WB_RBSpline xzprofile, final WB_BSpline xytrajectory,
			final double alpha) {
		final int n = xzprofile.n();
		final int m = xytrajectory.n();
		final WB_Point3d[][] points = new WB_Point3d[n + 1][m + 1];
		final double[][] weights = new double[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				points[i][j] = new WB_Point3d(alpha * xzprofile.points()[i].x
						* xytrajectory.points()[j].x, alpha
						* xzprofile.points()[i].x * xytrajectory.points()[j].y,
						xzprofile.points()[i].z);
				weights[i][j] = xzprofile.weights()[i];

			}
		}
		return new WB_RBSplineSurface(points, xzprofile.knot(), xytrajectory
				.knot(), weights);
	}

}