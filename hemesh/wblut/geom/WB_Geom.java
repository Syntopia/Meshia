/**
 * 
 */
package wblut.geom;

import wblut.WB_Epsilon;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_Geom.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_Geom {

	/**
	 * Between2 d.
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @return true, if successful
	 */
	public static boolean between2D(final WB_Point2d a, final WB_Point2d b,
			final WB_Point2d c) {
		if (WB_Geom.coincident2D(a, c)) {
			return true;
		} else if (WB_Geom.coincident2D(b, c)) {
			return true;
		} else {
			if (WB_Distance2D.sqDistanceToLine(c, a, b) < WB_Epsilon.SQEPSILON) {
				final double d = projectedDistanceNorm(c, a, b);
				if (0 < d && d < 1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Between strict2 d.
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @return true, if successful
	 */
	public static boolean betweenStrict2D(final WB_Point2d a,
			final WB_Point2d b, final WB_Point2d c) {
		if (WB_Geom.coincident2D(a, c)) {
			return true;
		} else if (WB_Geom.coincident2D(b, c)) {
			return true;
		} else {
			if (WB_Distance2D.sqDistanceToLine(c, a, b) < WB_Epsilon.SQEPSILON) {
				final double d = projectedDistanceNorm(c, a, b);
				if (0 < d && d < 1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Coincident2 d.
	 *
	 * @param a the a
	 * @param b the b
	 * @return true, if successful
	 */
	public static boolean coincident2D(final WB_Point2d a, final WB_Point2d b) {
		if (WB_Distance2D.sqDistance(a, b) < WB_Epsilon.SQEPSILON) {
			return true;
		}
		return false;
	}

	/**
	 * Projected distance norm.
	 *
	 * @param a the a
	 * @param b the b
	 * @param p the p
	 * @return the double
	 */
	public static double projectedDistanceNorm(final WB_Point2d a,
			final WB_Point2d b, final WB_Point2d p) {
		double x1, x2, y1, y2;
		x1 = b.x - a.x;
		x2 = p.x - a.x;
		y1 = b.y - a.y;
		y2 = p.y - a.y;
		return (x1 * x2 + y1 * y2) / (x1 * x1 + y1 * y1);
	}

	/**
	 * Point along line.
	 *
	 * @param p the p
	 * @param L the l
	 * @return the double
	 */
	public static double pointAlongLine(final WB_Point3d p, final WB_Line L) {
		final WB_Vector3d ab = L.getDirection();
		final WB_Vector3d ac = new WB_Vector3d(p);
		ac.sub(L.getOrigin());
		return ac.dot(ab);
	}

}
