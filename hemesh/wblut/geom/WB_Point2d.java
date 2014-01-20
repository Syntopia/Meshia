/**
 * 
 */
package wblut.geom;

import wblut.WB_Epsilon;
import wblut.math.WB_Fast;
import wblut.math.WB_MTRandom;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_Point2d.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_Point2d {
	
	/** The Constant ZERO. */
	public static final WB_Point2d	ZERO	= new WB_Point2d();

	/** Coordinates. */
	public double					x, y;

	/**
	 * Instantiates a new WB_XY.
	 */
	public WB_Point2d() {
		x = y = 0;
	}

	/**
	 * Instantiates a new WB_XY.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public WB_Point2d(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Instantiates a new WB_XY.
	 *
	 * @param v the v
	 */
	public WB_Point2d(final WB_Point2d v) {
		x = v.x;
		y = v.y;
	}

	/**
	 * Instantiates a new WB_XY.
	 *
	 * @param v the v
	 */
	public WB_Point2d(final WB_Point3d v) {
		x = v.x;
		y = v.y;
	}

	/**
	 * Set coordinates.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set coordinates.
	 *
	 * @param v the v
	 */
	public void set(final WB_Point2d v) {
		x = v.x;
		y = v.y;
	}

	/**
	 * Dot product.
	 *
	 * @param p the p
	 * @param q the q
	 * @return dot product
	 */
	public static double dot(final WB_Point2d p, final WB_Point2d q) {
		return (p.x * q.x + p.y * q.y);
	}

	/**
	 * Dot product.
	 *
	 * @param p the p
	 * @return dot product
	 */
	public double dot(final WB_Point2d p) {
		return (p.x * x + p.y * y);
	}

	/**
	 * Perp dot product.
	 *
	 * @param p the p
	 * @param q the q
	 * @return dot product
	 */
	public static double perpDot(final WB_Point2d p, final WB_Point2d q) {
		return (-p.y * q.x + p.x * q.y);
	}

	/**
	 * Perp dot product.
	 *
	 * @param p the p
	 * @return dot product
	 */
	public double perpDot(final WB_Point2d p) {
		return (-p.x * y + p.y * x);
	}

	/**
	 * Angle to vector. Normalized vectors are assumed.
	 *
	 * @param p normalized point, vector or normal
	 * @return angle
	 */
	public double angleNorm(final WB_Point2d p) {
		return Math.acos(p.x * x + p.y * y);
	}

	/**
	 * Absolute value of dot product.
	 *
	 * @param p the p
	 * @param q the q
	 * @return absolute value of dot product
	 */
	public static double absDot(final WB_Point2d p, final WB_Point2d q) {
		return WB_Fast.abs(p.x * q.x + p.y * q.y);
	}

	/**
	 * Absolute value of dot product.
	 *
	 * @param p the p
	 * @return absolute value of dot product
	 */
	public double absDot(final WB_Point2d p) {
		return WB_Fast.abs(p.x * x + p.y * y);
	}

	/**
	 * Get squared magnitude.
	 *
	 * @return squared magnitude
	 */
	public double mag2() {
		return x * x + y * y;
	}

	/**
	 * Get magnitude.
	 *
	 * @return magnitude
	 */
	public double mag() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Checks if vector is zero-vector.
	 *
	 * @return true, if zero
	 */
	public boolean isZero() {
		return (mag2() < WB_Epsilon.SQEPSILON);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/**
	 * Compare to.
	 *
	 * @param otherXY the other xy
	 * @return the int
	 */
	public int compareTo(final WB_Point2d otherXY) {
		int _tmp = WB_Epsilon.compareAbs(x, otherXY.x);
		if (_tmp != 0) {
			return _tmp;
		}
		_tmp = WB_Epsilon.compareAbs(y, otherXY.y);

		return _tmp;
	}

	/**
	 * Compare to y1st.
	 *
	 * @param otherXY the other xy
	 * @return the int
	 */
	public int compareToY1st(final WB_Point2d otherXY) {
		int _tmp = WB_Epsilon.compareAbs(y, otherXY.y);
		if (_tmp != 0) {
			return _tmp;
		}
		_tmp = WB_Epsilon.compareAbs(x, otherXY.x);

		return _tmp;

	}

	/**
	 * Smaller than.
	 *
	 * @param otherXYZ point, vector or normal
	 * @return true, if successful
	 */
	public boolean smallerThan(final WB_Point2d otherXYZ) {
		int _tmp = WB_Epsilon.compareAbs(x, otherXYZ.x);
		if (_tmp != 0) {
			return (_tmp < 0);
		}
		_tmp = WB_Epsilon.compareAbs(y, otherXYZ.y);
		return (_tmp < 0);

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "XY [x=" + x + ", y=" + y + "]";
	}

	/**
	 * Get coordinate from index value.
	 *
	 * @param i 0,1
	 * @return x- or y-coordinate
	 */
	public double get(final int i) {
		if (i == 0) {
			return x;
		}
		if (i == 1) {
			return y;
		}

		return Double.NaN;
	}

	/**
	 * Set coordinate with index value.
	 *
	 * @param i  0,1
	 * @param v x- or y-coordinate
	 */
	public void set(final int i, final double v) {
		if (i == 0) {
			x = v;
		} else if (i == 1) {
			y = v;
		}

	}

	/**
	 * Get x-coordinate as float.
	 *
	 * @return x
	 */
	public float xf() {
		return (float) x;
	}

	/**
	 * Get y-coordinate as float.
	 *
	 * @return y
	 */
	public float yf() {
		return (float) y;
	}

	/**
	 * Cross product. Internal use only.
	 *
	 * @param p the p
	 * @return the w b_ point3d
	 */
	public WB_Point3d cross(final WB_Point2d p) {
		return new WB_Point3d(0, 0, x * p.y - y * p.x);
	}

	/**
	 * Cross product. Internal use only.
	 *
	 * @param p the p
	 * @param q the q
	 * @return the w b_ point3d
	 */
	public static WB_Point3d cross(final WB_Point2d p, final WB_Point2d q) {
		return new WB_Point3d(0, 0, p.x * q.y - p.y * q.x);
	}

	/**
	 * Cross product. Internal use only.
	 *
	 * @param p the p
	 * @param result the result
	 */
	public void crossInto(final WB_Point2d p, final WB_Point3d result) {
		result.x = 0;
		result.y = 0;
		result.z = x * p.y - y * p.x;
	}

	/**
	 * return copy.
	 *
	 * @return copy
	 */
	public WB_Point2d get() {
		return new WB_Point2d(x, y);
	}

	/*
	 */
	/**
	 * Perp.
	 *
	 * @return the w b_ point2d
	 */
	public WB_Point2d perp() {
		return new WB_Point2d(-y, x);
	}

	/**
	 * Perp into.
	 *
	 * @param result the result
	 */
	public void perpInto(final WB_Point2d result) {
		result.set(-y, x);
	}

	/**
	 * Move to position.
	 *
	 * @param x the x
	 * @param y the y
	 * @return self
	 */
	public WB_Point2d moveTo(final double x, final double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Move to position.
	 *
	 * @param p point, vector or normal
	 * @return self
	 */
	public WB_Point2d moveTo(final WB_Point2d p) {
		x = p.x;
		y = p.y;
		return this;
	}

	/**
	 * Move by vector.
	 *
	 * @param x the x
	 * @param y the y
	 * @return self
	 */
	public WB_Point2d moveBy(final double x, final double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Move by vector.
	 *
	 * @param v point, vector or normal
	 * @return self
	 */
	public WB_Point2d moveBy(final WB_Point2d v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * Move by vector.
	 *
	 * @param x the x
	 * @param y the y
	 * @param result WB_XY to store result
	 */
	public void moveByInto(final double x, final double y,
			final WB_Point2d result) {
		result.x = this.x + x;
		result.y = this.y + y;
	}

	/**
	 * Move by vector.
	 *
	 * @param v point, vector or normal
	 * @param result WB_XY to store result
	 */
	public void moveByInto(final WB_Point2d v, final WB_Point2d result) {
		result.x = x + v.x;
		result.y = y + v.y;
	}

	/**
	 * Move by vector.
	 *
	 * @param x the x
	 * @param y the y
	 * @return new WB_XY
	 */
	public WB_Point2d moveByAndCopy(final double x, final double y) {
		return new WB_Point2d(this.x + x, this.y + y);
	}

	/**
	 * Move by vector.
	 *
	 * @param v point, vector or normal
	 * @return new WB_XY
	 */
	public WB_Point2d moveByAndCopy(final WB_Point2d v) {
		return new WB_Point2d(x + v.x, y + v.y);
	}

	/**
	 * Scale.
	 *
	 * @param f scale factor
	 * @return self
	 */
	public WB_Point2d scale(final double f) {
		x *= f;
		y *= f;
		return this;
	}

	/**
	 * Scale .
	 *
	 * @param f scale factor
	 * @param result WB_XY to store result
	 */
	public void scaleInto(final double f, final WB_Point2d result) {
		result.x = x * f;
		result.y = y * f;
	}

	/**
	 * Scale.
	 *
	 * @param fx scale factor
	 * @param fy scale factor
	 * @return self
	 */
	public WB_Point2d scale(final double fx, final double fy) {
		x *= fx;
		y *= fy;
		return this;
	}

	/**
	 * Scale .
	 *
	 * @param fx scale factor
	 * @param fy scale factor
	 * @param result WB_XY to store result
	 */
	public void scaleInto(final double fx, final double fy,
			final WB_Point2d result) {
		result.x = x * fx;
		result.y = y * fy;
	}

	/**
	 * Adds the.
	 *
	 * @param x the x
	 * @param y the y
	 * @return self
	 */
	public WB_Point2d add(final double x, final double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param x the x
	 * @param y the y
	 * @param f the f
	 * @return self
	 */
	public WB_Point2d add(final double x, final double y, final double f) {
		this.x += f * x;
		this.y += f * y;
		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param p the p
	 * @return self
	 */
	public WB_Point2d add(final WB_Point2d p) {
		x += p.x;
		y += p.y;
		return this;
	}

	/**
	 * Adds the.
	 *
	 * @param p the p
	 * @param f the f
	 * @return self
	 */
	public WB_Point2d add(final WB_Point2d p, final double f) {
		x += f * p.x;
		y += f * p.y;
		return this;
	}

	/**
	 * Adds the into.
	 *
	 * @param x the x
	 * @param y the y
	 * @param result the result
	 */
	public void addInto(final double x, final double y, final WB_Point2d result) {
		result.x = (this.x + x);
		result.y = (this.y + y);
	}

	/**
	 * Adds the into.
	 *
	 * @param p the p
	 * @param result the result
	 */
	public void addInto(final WB_Point3d p, final WB_Point2d result) {
		result.x = x + p.x;
		result.y = y + p.y;
	}

	/**
	 * Adds the and copy.
	 *
	 * @param x the x
	 * @param y the y
	 * @param f the f
	 * @return new WB_XY
	 */
	public WB_Point2d addAndCopy(final double x, final double y, final double f) {
		return new WB_Point2d(this.x + f * x, this.y + f * y);
	}

	/**
	 * Adds the and copy.
	 *
	 * @param x the x
	 * @param y the y
	 * @return new WB_XY
	 */
	public WB_Point2d addAndCopy(final double x, final double y) {
		return new WB_Point2d(this.x + x, this.y + y);
	}

	/**
	 * Adds the and copy.
	 *
	 * @param p the p
	 * @return new WB_XY
	 */
	public WB_Point2d addAndCopy(final WB_Point2d p) {
		return new WB_Point2d(x + p.x, y + p.y);
	}

	/**
	 * Adds the and copy.
	 *
	 * @param p the p
	 * @param f the f
	 * @return new WB_XY
	 */
	public WB_Point2d addAndCopy(final WB_Point2d p, final double f) {
		return new WB_Point2d(x + f * p.x, y + f * p.y);
	}

	/**
	 * Sub.
	 *
	 * @param x the x
	 * @param y the y
	 * @return self
	 */
	public WB_Point2d sub(final double x, final double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/**
	 * Sub.
	 *
	 * @param v the v
	 * @return self
	 */
	public WB_Point2d sub(final WB_Point2d v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	/**
	 * Sub into.
	 *
	 * @param x the x
	 * @param y the y
	 * @param result the result
	 */
	public void subInto(final double x, final double y, final WB_Point2d result) {
		result.x = (this.x - x);
		result.y = (this.y - y);
	}

	/**
	 * Sub into.
	 *
	 * @param p the p
	 * @param result the result
	 */
	public void subInto(final WB_Point2d p, final WB_Point2d result) {
		result.x = x - p.x;
		result.y = y - p.y;
	}

	/**
	 * Sub and copy.
	 *
	 * @param x the x
	 * @param y the y
	 * @return new WB_XY
	 */
	public WB_Point2d subAndCopy(final double x, final double y) {
		return new WB_Point2d(this.x - x, this.y - y);
	}

	/**
	 * Sub and copy.
	 *
	 * @param p the p
	 * @return new WB_XY
	 */
	public WB_Point2d subAndCopy(final WB_Point2d p) {
		return new WB_Point2d(x - p.x, y - p.y);
	}

	/**
	 * Mult.
	 *
	 * @param f the f
	 * @return self
	 */
	public WB_Point2d mult(final double f) {
		x *= f;
		y *= f;
		return this;
	}

	/**
	 * Invert.
	 *
	 * @return self
	 */
	public WB_Point2d invert() {
		x *= -1;
		y *= -1;
		return this;
	}

	/**
	 * Normalize.
	 *
	 * @return the double
	 */
	public double normalize() {
		final double d = mag();
		if (WB_Epsilon.isZero(d)) {
			set(0, 0);
		} else {
			set(x / d, y / d);
		}
		return d;
	}

	/**
	 * Trim.
	 *
	 * @param d the d
	 */
	public void trim(final double d) {
		if (mag2() > d * d) {
			normalize();
			mult(d);
		}
	}

	/**
	 * Mult into.
	 *
	 * @param f the f
	 * @param result the result
	 */
	public void multInto(final double f, final WB_Point2d result) {
		result.x = (x * f);
		result.y = (y * f);
	}

	/**
	 * Mult and copy.
	 *
	 * @param f the f
	 * @return new WB_XY
	 */
	public WB_Point2d multAndCopy(final double f) {
		return new WB_Point2d(x * f, y * f);
	}

	/**
	 * Div.
	 *
	 * @param f the f
	 * @return self
	 */
	public WB_Point2d div(final double f) {
		return mult(1.0 / f);
	}

	/**
	 * Div into.
	 *
	 * @param f the f
	 * @param result the result
	 */
	public void divInto(final double f, final WB_Point2d result) {
		multInto(1.0 / f, result);
	}

	/**
	 * Div and copy.
	 *
	 * @param f the f
	 * @return new WB_XY
	 */
	public WB_Point2d divAndCopy(final double f) {
		return multAndCopy(1.0 / f);
	}

	/**
	 * Normalize.
	 *
	 * @param result WB_XY to store result
	 */
	public void normalizeInto(final WB_Point2d result) {
		final double d = mag();
		if (WB_Epsilon.isZero(d)) {
			result.set(0, 0);
		} else {
			result.set(x, y);
			result.div(d);
		}
	}

	/**
	 * Random points.
	 *
	 * @param n the n
	 * @param x the x
	 * @param y the y
	 * @return the w b_ point2d[]
	 */
	public static WB_Point2d[] randomPoints(final int n, final double x,
			final double y) {
		final WB_MTRandom mtr = new WB_MTRandom();
		final WB_Point2d[] points = new WB_Point2d[n];
		for (int i = 0; i < n; i++) {
			points[i] = new WB_Point2d(-x + 2 * mtr.nextDouble() * x, -y + 2
					* mtr.nextDouble() * y);
		}

		return points;
	}

	/**
	 * Random points.
	 *
	 * @param n the n
	 * @param lx the lx
	 * @param ly the ly
	 * @param ux the ux
	 * @param uy the uy
	 * @return the w b_ point2d[]
	 */
	public static WB_Point2d[] randomPoints(final int n, final double lx,
			final double ly, final double ux, final double uy) {
		final WB_MTRandom mtr = new WB_MTRandom();
		final WB_Point2d[] points = new WB_Point2d[n];
		final double dx = ux - lx;
		final double dy = uy - ly;

		for (int i = 0; i < n; i++) {
			points[i] = new WB_Point2d(lx + mtr.nextDouble() * dx, ly
					+ mtr.nextDouble() * dy);
		}

		return points;
	}

	/**
	 * Is vector parallel to other vector.
	 *
	 * @param p the p
	 * @return true, if parallel
	 */
	public boolean isParallel(final WB_Point2d p) {
		return (cross(p).mag2() / (p.mag2() * mag2()) < WB_Epsilon.SQEPSILON);
	}

	/**
	 * Is vector parallel to other vector.
	 *
	 * @param p the p
	 * @param t threshold value = (sin(threshold angle))^2
	 * @return true, if parallel
	 */
	public boolean isParallel(final WB_Point2d p, final double t) {
		return (cross(p).mag2() / (p.mag2() * mag2()) < t
				+ WB_Epsilon.SQEPSILON);
	}

	/**
	 * Is normalized vector parallel to other normalized vector.
	 *
	 * @param p the p
	 * @return true, if parallel
	 */
	public boolean isParallelNorm(final WB_Point2d p) {
		return (cross(p).mag2() < WB_Epsilon.SQEPSILON);
	}

	/**
	 * Is normalized vector parallel to other normalized vector.
	 *
	 * @param p the p
	 * @param t threshold value = (sin(threshold angle))^2
	 * @return true, if parallel
	 */
	public boolean isParallelNorm(final WB_Point2d p, final double t) {
		return (cross(p).mag2() < t + WB_Epsilon.SQEPSILON);
	}

	/**
	 * Calculate hash code.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the int
	 */
	protected static int calculateHashCode(final double x, final double y) {
		int result = 17;

		final long a = Double.doubleToLongBits(x);
		result += 31 * result + (int) (a ^ (a >>> 32));

		final long b = Double.doubleToLongBits(y);
		result += 31 * result + (int) (b ^ (b >>> 32));

		return result;

	}

	/**
	 * Calculate hash code.
	 *
	 * @return the int
	 */
	protected int calculateHashCode() {
		int result = 17;

		final long a = Double.doubleToLongBits(x);
		result += 31 * result + (int) (a ^ (a >>> 32));

		final long b = Double.doubleToLongBits(y);
		result += 31 * result + (int) (b ^ (b >>> 32));

		return result;

	}

	/**
	 * Interpolate.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param t the t
	 * @return the w b_ point
	 */
	public static WB_Point2d interpolate(final WB_Point2d p0,
			final WB_Point2d p1, final double t) {
		return new WB_Point2d(p0.x + t * (p1.x - p0.x), p0.y + t
				* (p1.y - p0.y));

	}

	/**
	 * Rotate.
	 *
	 * @param angle the angle
	 */
	public void rotate(final double angle) {
		final double tmpx = x;
		final double tmpy = y;
		final double ca = Math.cos(angle);
		final double sa = Math.sin(angle);
		x = ca * tmpx - sa * tmpy;
		y = sa * tmpx + ca * tmpy;
	}

	/**
	 * Rotate.
	 *
	 * @param angle the angle
	 * @param origin the origin
	 */
	public void rotate(final double angle, final WB_Point2d origin) {
		final double tmpx = x - origin.x;
		final double tmpy = y - origin.y;
		final double ca = Math.cos(angle);
		final double sa = Math.sin(angle);
		x = origin.x + ca * tmpx - sa * tmpy;
		y = origin.y + sa * tmpx + ca * tmpy;
	}

	/**
	 * Rotate.
	 *
	 * @param cosangle the cosangle
	 * @param sinangle the sinangle
	 */
	public void rotate(final double cosangle, final double sinangle) {
		final double tmpx = x;
		final double tmpy = y;
		x = cosangle * tmpx - sinangle * tmpy;
		y = sinangle * tmpx + cosangle * tmpy;
	}

	/**
	 * Rotate.
	 *
	 * @param cosangle the cosangle
	 * @param sinangle the sinangle
	 * @param origin the origin
	 */
	public void rotate(final double cosangle, final double sinangle,
			final WB_Point2d origin) {
		final double tmpx = x - origin.x;
		final double tmpy = y - origin.y;
		x = origin.x + cosangle * tmpx - sinangle * tmpy;
		y = origin.y + sinangle * tmpx + cosangle * tmpy;
	}
}
