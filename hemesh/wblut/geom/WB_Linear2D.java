/**
 * 
 */
package wblut.geom;

import wblut.WB_Epsilon;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_Linear2D.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public abstract class WB_Linear2D {
	/** Origin of line. */
	protected WB_Point2d	origin;

	/** Direction of line. */
	protected WB_Point2d	direction;

	/**
	 * Instantiates a new w b_ linear2 d.
	 */
	public WB_Linear2D() {
		origin = new WB_Point2d();
		direction = new WB_Point2d(1, 0);
	}

	/**
	 * Instantiates a new w b_ linear2 d.
	 *
	 * @param o the o
	 * @param d the d
	 * @param asDirection the as direction
	 */
	public WB_Linear2D(final WB_Point2d o, final WB_Point2d d, final boolean asDirection) {
		if (asDirection) {
			origin = o.get();
			direction = d.get();
			direction.normalize();
		} else {
			origin = o.get();
			direction = d.subAndCopy(o);
			direction.normalize();
		}
	}

	/**
	 * Instantiates a new w b_ linear2 d.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 */
	public WB_Linear2D(final WB_Point2d p1, final WB_Point2d p2) {
		origin = p1.get();
		direction = p2.subAndCopy(p1);
		direction.normalize();
	}

	/**
	 * Instantiates a new w b_ linear2 d.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 */
	public WB_Linear2D(final double x1, final double y1, final double x2,
			final double y2) {
		origin = new WB_Point2d(x1, y1);
		direction = new WB_Point2d(x2 - x1, y2 - y1);
		direction.normalize();
	}

	/**
	 * Sets the.
	 *
	 * @param o the o
	 * @param d the d
	 * @param asDirection the as direction
	 */
	public void set(final WB_Point2d o, final WB_Point2d d, final boolean asDirection) {
		if (asDirection) {
			origin.set(o);
			direction.set(d);
			direction.normalize();
		} else {
			set(o, d);
		}
	}

	/**
	 * Sets the.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 */
	public void set(final WB_Point2d p1, final WB_Point2d p2) {
		origin.set(p1);
		direction.set(p2.subAndCopy(p1));
		direction.normalize();
	}

	/**
	 * Sets the no copy.
	 *
	 * @param o the o
	 * @param d the d
	 * @param asDirection the as direction
	 */
	public void setNoCopy(final WB_Point2d o, final WB_Point2d d,
			final boolean asDirection) {
		if (asDirection) {
			origin = o;
			direction = d;
			direction.normalize();
		} else {
			setNoCopy(o, d);
		}
	}

	/**
	 * Sets the no copy.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 */
	public void setNoCopy(final WB_Point2d p1, final WB_Point2d p2) {
		origin = p1;
		direction.set(p2.subAndCopy(p1));
		direction.normalize();
	}

	/**
	 * Get point along line.
	 *
	 * @param t distance from origin
	 * @return point
	 */
	public WB_Point2d getPoint(final double t) {
		final WB_Point2d result = new WB_Point2d(direction);
		result.scale(t);
		result.moveBy(origin);
		return result;
	}

	/**
	 * Get point along line and store it in provided WB_XY.
	 *
	 * @param t distance from origin
	 * @param p WB_XY to store result in
	 * @return the point into
	 */
	public void getPointInto(final double t, final WB_Point2d p) {
		p.moveTo(direction);
		p.scale(t);
		p.moveBy(origin);
	}

	/**
	 * Get origin.
	 *
	 * @return origin
	 */
	public WB_Point2d getOrigin() {
		return origin;
	}

	/**
	 * Get direction.
	 *
	 * @return direction
	 */
	public WB_Point2d getDirection() {
		return direction;
	}

	/**
	 * Gets the normal.
	 *
	 * @return the normal
	 */
	public WB_Point2d getNormal() {

		WB_Point2d n = new WB_Point2d(-direction.y, direction.x);
		final double d = n.normalize();
		if (WB_Epsilon.isZero(d)) {
			n = new WB_Point2d(1, 0);
		}
		return n;
	}

	/**
	 * A.
	 *
	 * @return the double
	 */
	public double a() {
		return -direction.y;
	}

	/**
	 * B.
	 *
	 * @return the double
	 */
	public double b() {
		return direction.x;
	}

	/**
	 * C.
	 *
	 * @return the double
	 */
	public double c() {
		return origin.x * direction.y - origin.y * direction.x;
	}

}
