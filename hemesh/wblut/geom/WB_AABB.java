package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import wblut.WB_Epsilon;



// TODO: Auto-generated Javadoc
/**
 * The Class WB_AABB.
 *
 * @param <T> the generic type
 */
public class WB_AABB<T extends WB_Point3d> {

	/** The _min. */
	protected double[] _min;
	
	/** The _max. */
	protected double[] _max;
	
	/** The _data. */
	private HashMap<String, Object> _data;
	
	/** The _id. */
	int _id;

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param p the p
	 */
	public WB_AABB(final T p) {
		_min = new double[3];
		_max = new double[3];
		setToNull();
		expandToInclude(p);
	}

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param points point cloud
	 */
	public WB_AABB(final T[] points) {
		if (points == null) {
			throw new NullPointerException("Array not initialized.");
		}
		if (points.length == 0) {
			throw new IllegalArgumentException("Array has zero size.");
		}
		WB_Point3d point = points[0];
		if (point == null) {
			throw new NullPointerException("Array point not initialized.");
		}
		init();
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < 3; j++) {
				point = points[i];
				if (point == null) {
					throw new NullPointerException(
							"Array point not initialized.");
				}
				if (_min[j] > point.get(j)) {
					_min[j] = point.get(j);
				}
				if (_max[j] < point.get(j)) {
					_max[j] = point.get(j);
				}
			}
		}
	}

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param points the points
	 */
	public WB_AABB(final Collection<T> points) {
		if (points == null) {
			throw new IllegalArgumentException("Collection not initialized.");
		}
		if (points.size() == 0) {
			throw new IllegalArgumentException("Collection has zero size.");
		}
		WB_Point3d fpoint = points.iterator().next();
		if (fpoint == null) {
			throw new NullPointerException("Collection point not initialized.");
		}
		init();
		for (WB_Point3d point : points) {
			if (point == null) {
				throw new NullPointerException(
						"Collection point not initialized.");
			}
			for (int j = 0; j < 3; j++) {
				if (_min[j] > point.get(j)) {
					_min[j] = point.get(j);
				}
				if (_max[j] < point.get(j)) {
					_max[j] = point.get(j);
				}
			}
		}
	}

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param min minimum values as double[3]
	 * @param max maximum values as double[3]
	 */
	public WB_AABB(final double[] min, final double[] max) {
		_min = new double[3];
		_max = new double[3];
		for (int i = 0; i < 3; i++) {
			if (min[i] < max[i]) {
				_min[i] = min[i];
				_max[i] = max[i];
			} else {
				_min[i] = max[i];
				_max[i] = min[i];
			}
		}
	}

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param min minimum values as float[3]
	 * @param max maximum values as float[3]
	 */
	public WB_AABB(final float[] min, final float[] max) {
		_min = new double[3];
		_max = new double[3];
		for (int i = 0; i < 3; i++) {
			if (min[i] < max[i]) {
				_min[i] = min[i];
				_max[i] = max[i];
			} else {
				_min[i] = max[i];
				_max[i] = min[i];
			}
		}
	}

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param min minimum values as int[3]
	 * @param max maximum values as int[3]
	 */
	public WB_AABB(final int[] min, final int[] max) {
		_min = new double[3];
		_max = new double[3];
		for (int i = 0; i < 3; i++) {
			if (min[i] < max[i]) {
				_min[i] = min[i];
				_max[i] = max[i];
			} else {
				_min[i] = max[i];
				_max[i] = min[i];
			}
		}
	}

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param min minimum values as WB_XY
	 * @param max maximum values as WB_XY
	 */
	public WB_AABB(final WB_Point3d min, final WB_Point3d max) {
		_min = new double[3];
		_max = new double[3];
		for (int i = 0; i < 3; i++) {
			if (min.get(i) < max.get(i)) {
				_min[i] = min.get(i);
				_max[i] = max.get(i);
			} else {
				_min[i] = max.get(i);
				_max[i] = min.get(i);
			}
		}
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @param values the values
	 */
	public WB_AABB(final Double... values) {
		_min = new double[3];
		_max = new double[3];
		for (int i = 0; i < 3; i++) {
			_min[i] = values[i];
			_max[i] = values[i + 3];
		}
	}

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param values the values
	 */
	public WB_AABB(final Integer... values) {
		_min = new double[3];
		_max = new double[3];
		for (int i = 0; i < 3; i++) {
			_min[i] = values[i];
			_max[i] = values[i + 3];
		}
	}

	/**
	 * Instantiates a new wb aabb.
	 *
	 * @param values the values
	 */
	public WB_AABB(final Float... values) {
		_min = new double[3];
		_max = new double[3];
		for (int i = 0; i < 3; i++) {
			_min[i] = values[i];
			_max[i] = values[i + 3];
		}
	}

	/**
	 * Gets the size.
	 *
	 * @param i the i
	 * @return the size
	 */
	public double getSize(final int i) {
		if (isNull()) {
			return 0;
		}
		return _max[i] - _min[i];
	}

	/**
	 * Gets the min.
	 *
	 * @param i the i
	 * @return the min
	 */
	public double getMin(final int i) {
		return _min[i];
	}

	/**
	 * Gets the max.
	 *
	 * @param i the i
	 * @return the max
	 */
	public double getMax(final int i) {
		return _max[i];
	}

	/**
	 * Min ordinate.
	 *
	 * @return the int
	 */
	public int minOrdinate() {
		if (isNull()) {
			return 0;
		}
		double res = Double.POSITIVE_INFINITY;
		int ord = 0;
		for (int i = 0; i < 3; i++) {
			double w = getSize(i);
			if (res > w) {
				res = w;
				ord = i;
			}
		}
		return ord;
	}

	/**
	 * Max ordinate.
	 *
	 * @return the int
	 */
	public int maxOrdinate() {
		if (isNull()) {
			return 0;
		}
		double res = Double.NEGATIVE_INFINITY;
		int ord = 0;
		for (int i = 0; i < 3; i++) {
			double w = getSize(i);
			if (res < w) {
				res = w;
				ord = i;
			}
		}
		return ord;
	}

	/**
	 * Expand to include.
	 *
	 * @param p the p
	 */
	public void expandToInclude(final WB_Point3d p) {
		expandToInclude(p.coords());
	}

	/**
	 * Expand by.
	 *
	 * @param distance the distance
	 */
	public void expandBy(final double distance) {
		if (isNull()) {
			return;
		}
		for (int i = 0; i < 3; i++) {
			_min[i] -= distance;
			_max[i] += distance;
			if (_min[i] > _max[i]) {
				setToNull();
				return;
			}
		}
	}

	/**
	 * Expand by.
	 *
	 * @param delta the delta
	 */
	public void expandBy(final double[] delta) {
		if (isNull()) {
			return;
		}
		for (int i = 0; i < 3; i++) {
			_min[i] -= delta[i];
			_max[i] += delta[i];
			if (_min[i] > _max[i]) {
				setToNull();
				return;
			}
		}
	}

	/**
	 * Expand to include.
	 *
	 * @param p the p
	 */
	public void expandToInclude(final double[] p) {
		if (isNull()) {
			for (int i = 0; i < 3; i++) {
				_min[i] = p[i];
				_max[i] = p[i];
			}
		} else {
			for (int i = 0; i < 3; i++) {
				if (p[i] < _min[i]) {
					_min[i] = p[i];
				}
				if (p[i] > _max[i]) {
					_max[i] = p[i];
				}
			}
		}
	}

	/**
	 * Expand to include.
	 *
	 * @param other the other
	 */
	public void expandToInclude(final WB_AABB<T> other) {
		expandToInclude(other._min);
		expandToInclude(other._max);
	}

	/**
	 * Translate.
	 *
	 * @param d the d
	 */
	public void translate(final double[] d) {
		if (isNull()) {
			return;
		}
		for (int i = 0; i < 3; i++) {
			_min[i] += d[i];
			_max[i] += d[i];
		}
	}

	/**
	 * Center.
	 *
	 * @return the double[]
	 */
	@Deprecated
	public double[] center() {
		return getCenter();
	}

	/**
	 * Intersects.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean intersects(final WB_AABB<T> other) {
		if (isNull() || other.isNull()) {
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (other._min[i] > _max[i]) {
				return false;
			}
			if (other._max[i] < _min[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Intersects.
	 *
	 * @param p the p
	 * @return true, if successful
	 */
	public boolean intersects(final WB_Point3d p) {
		return intersects(p.coords());
	}

	/**
	 * Intersects.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean intersects(final double[] x) {
		if (isNull()) {
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (x[i] > _max[i]) {
				return false;
			}
			if (x[i] < _min[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Contains.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean contains(final WB_AABB<T> other) {
		return covers(other);
	}

	/**
	 * Contains.
	 *
	 * @param p the p
	 * @return true, if successful
	 */
	public boolean contains(final WB_Point3d p) {
		return covers(p);
	}

	/**
	 * Contains.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean contains(final double[] x) {
		return covers(x);
	}

	/**
	 * Covers.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean covers(final double[] x) {
		if (isNull()) {
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (x[i] > _max[i]) {
				return false;
			}
			if (x[i] < _min[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Covers.
	 *
	 * @param p the p
	 * @return true, if successful
	 */
	public boolean covers(final WB_Point3d p) {
		return covers(p.coords());
	}

	/**
	 * Covers.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean covers(final WB_AABB<T> other) {
		if (isNull() || other.isNull()) {
			return false;
		}
		for (int i = 0; i < 3; i++) {
			if (other._max[i] > _max[i]) {
				return false;
			}
			if (other._min[i] < _min[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the distance.
	 *
	 * @param other the other
	 * @return the distance
	 */
	public double getDistance(final WB_AABB<T> other) {
		if (intersects(other)) {
			return 0;
		}
		double dx = 0;
		double sqr = 0;
		for (int i = 0; i < 3; i++) {
			if (_max[i] < other._min[i]) {
				dx = other._min[i] - _max[i];
			} else if (_min[i] > other._max[i]) {
				dx = _min[i] - other._max[i];
			}
			sqr += dx * dx;
		}
		return Math.sqrt(sqr);
	}

	/**
	 * Gets the distance square.
	 *
	 * @param other the other
	 * @return the distance square
	 */
	public double getDistanceSquare(final WB_AABB<T> other) {
		if (intersects(other)) {
			return 0;
		}
		double dx = 0;
		double sqr = 0;
		for (int i = 0; i < 3; i++) {
			if (_max[i] < other._min[i]) {
				dx = other._min[i] - _max[i];
			} else if (_min[i] > other._max[i]) {
				dx = _min[i] - other._max[i];
			}
			sqr += dx * dx;
		}
		return sqr;
	}

	/**
	 * Gets the distance.
	 *
	 * @param tuple the tuple
	 * @return the distance
	 */
	public double getDistance(final WB_Point3d tuple) {
		double dx = 0;
		double sqr = 0;
		for (int i = 0; i < 3; i++) {
			if (_max[i] < tuple.get(i)) {
				sqr += (dx = tuple.get(i) - _max[i]) * dx;
			} else if (_min[i] > tuple.get(i)) {
				sqr += (dx = _min[i] - tuple.get(i)) * dx;
			}
		}
		return Math.sqrt(sqr);
	}

	/**
	 * Gets the distance square.
	 *
	 * @param tuple the tuple
	 * @return the distance square
	 */
	public double getDistanceSquare(final WB_Point3d tuple) {
		double dx = 0;
		double sqr = 0;
		for (int i = 0; i < 3; i++) {
			if (_max[i] < tuple.get(i)) {
				sqr += (dx = tuple.get(i) - _max[i]) * dx;
			} else if (_min[i] > tuple.get(i)) {
				sqr += (dx = _min[i] - tuple.get(i)) * dx;
			}
		}
		return sqr;
	}

	/**
	 * Equals.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean equals(final WB_AABB<T> other) {
		if (isNull()) {
			return other.isNull();
		}
		for (int i = 0; i < 3; i++) {
			if (other._max[i] != _max[i]) {
				return false;
			}
			if (other._min[i] != _min[i]) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String string = "WB_AABB [";
		int i = 0;
		for (i = 0; i < 3 - 1; i++) {
			string += _min[0] + ":" + _max[0] + ", ";
		}
		string += _min[i] + ":" + _max[i] + "]";
		return string;
	}

	/**
	 * Number of points.
	 *
	 * @return the int
	 */
	public int numberOfPoints() {
		if (isNull()) {
			return 0;
		}
		return 8;
	}

	/**
	 * Number of segments.
	 *
	 * @return the int
	 */
	public int numberOfSegments() {
		if (isNull()) {
			return 0;
		}
		return 12;
	}

	/**
	 * Number of triangles.
	 *
	 * @return the int
	 */
	public int numberOfTriangles() {
		if (isNull()) {
			return 0;
		}
		return 12;
	}

	/**
	 * Number of faces.
	 *
	 * @return the int
	 */
	public int numberOfFaces() {
		if (isNull()) {
			return 0;
		}
		return 6;
	}

	/**
	 * Gets the coords.
	 *
	 * @return the coords
	 */
	public List<double[]> getCoords() {
		if (isNull()) {
			return null;
		}
		int n = numberOfPoints();
		List<double[]> points = new ArrayList<double[]>(n);
		double[] values;
		for (int i = 0; i < n; i++) {
			values = new double[3];
			int disc = 1;
			for (int j = 0; j < 3; j++) {
				if (((i / disc) % 2) == 0) {
					values[j] = _min[j];
				} else {
					values[j] = _max[j];
				}
				disc *= 2;
			}
			points.add(values);
		}
		return points;
	}

	/**
	 * Gets the coord.
	 *
	 * @param i the i
	 * @return the coord
	 */
	public double[] getCoord(final int i) {
		double[] values = new double[3];
		int disc = 1;
		for (int j = 0; j < 3; j++) {
			if (((i / disc) % 2) == 0) {
				values[j] = _min[j];
			} else {
				values[j] = _max[j];
			}
			disc *= 2;
		}
		return (values);
	}

	/**
	 * Gets the segments.
	 *
	 * @return the segments
	 */
	public List<int[]> getSegments() {
		List<double[]> points = getCoords();
		List<int[]> segments = new ArrayList<int[]>(numberOfSegments());
		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				int comp = 0;
				for (int k = 0; k < 3; k++) {
					if (points.get(i)[k] != points.get(j)[k]) {
						comp++;
					}
					if (comp > 1) {
						break;
					}
				}
				if (comp == 1) {
					int[] seg = { i, j };
					segments.add(seg);
				}
			}
		}
		return segments;
	}

	/**
	 * Sets the data.
	 *
	 * @param s the s
	 * @param o the o
	 */
	public void setData(final String s, final Object o) {
		if (_data == null) {
			_data = new HashMap<String, Object>();
		}
		_data.put(s, o);
	}

	/**
	 * Gets the data.
	 *
	 * @param s the s
	 * @return the data
	 */
	public Object getData(final String s) {
		return _data.get(s);
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return _id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(final int id) {
		_id = id;
	}

	/**
	 * Checks if is degenerate.
	 *
	 * @return true, if is degenerate
	 */
	public boolean isDegenerate() {
		return ((getTrueDim() < 3) && (getTrueDim() > -1));
	}

	/**
	 * Sets the.
	 *
	 * @param src the src
	 */
	public void set(final WB_AABB<T> src) {
		System.arraycopy(src._min, 0, _min, 0, 3);
		System.arraycopy(src._max, 0, _max, 0, 3);
	}

	/**
	 * Inits the.
	 */
	private void init() {
		_min = new double[3];
		_max = new double[3];
		for (int i = 0; i < 3; i++) {
			_min[i] = Double.POSITIVE_INFINITY;
			_max[i] = Double.NEGATIVE_INFINITY;
		}
	}

	/**
	 * Gets the.
	 *
	 * @return the wb aabb
	 */
	public WB_AABB<T> get() {
		return new WB_AABB<T>(_min, _max);
	}

	/**
	 * Union.
	 *
	 * @param aabb the aabb
	 * @return the wb aabb
	 */
	public WB_AABB union(final WB_AABB<T> aabb) {
		double[] newmin = new double[3];
		double[] newmax = new double[3];
		for (int i = 0; i < 3; i++) {
			newmin[i] = Math.min(_min[i], aabb._min[i]);
			newmax[i] = Math.max(_max[i], aabb._max[i]);
		}
		return new WB_AABB(newmin, newmax);
	}

	/**
	 * Intersection.
	 *
	 * @param other the other
	 * @return the wb aabb
	 */
	public WB_AABB intersection(final WB_AABB<T> other) {
		if (isNull() || other.isNull() || !intersects(other)) {
			return null;
		}
		double[] newmin = new double[3];
		double[] newmax = new double[3];
		for (int i = 0; i < 3; i++) {
			newmin[i] = Math.max(_min[i], other._min[i]);
			newmax[i] = Math.min(_max[i], other._max[i]);
		}
		return new WB_AABB(newmin, newmax);
	}

	/**
	 * Intersects.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param q the q
	 * @return true, if successful
	 */
	public static boolean intersects(final WB_Point3d p1, final WB_Point3d p2,
			final WB_Point3d q) {
		if (((q.x >= (p1.x < p2.x ? p1.x
				: p2.x)) && (q.x <= (p1.x > p2.x ? p1.x
				: p2.x)))
				&& ((q.y >= (p1.y < p2.y ? p1.y
						: p2.y)) && (q.y <= (p1.y > p2.y ? p1.y
						: p2.y)))
				&& ((q.z >= (p1.z < p2.z ? p1.z
						: p2.z)) && (q.z <= (p1.z > p2.z ? p1.y
						: p2.z)))) {
			return true;
		}
		return false;
	}

	/**
	 * Intersects.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param q1 the q1
	 * @param q2 the q2
	 * @return true, if successful
	 */
	public static boolean intersects(final WB_Point3d p1, final WB_Point3d p2,
			final WB_Point3d q1, final WB_Point3d q2) {
		double minq = Math.min(q1.x, q2.x);
		double maxq = Math.max(q1.x, q2.x);
		double minp = Math.min(p1.x, p2.x);
		double maxp = Math.max(p1.x, p2.x);
		if (minp > maxq) {
			return false;
		}
		if (maxp < minq) {
			return false;
		}
		minq = Math.min(q1.y, q2.y);
		maxq = Math.max(q1.y, q2.y);
		minp = Math.min(p1.y, p2.y);
		maxp = Math.max(p1.y, p2.y);
		if (minp > maxq) {
			return false;
		}
		if (maxp < minq) {
			return false;
		}
		minq = Math.min(q1.z, q2.z);
		maxq = Math.max(q1.z, q2.z);
		minp = Math.min(p1.z, p2.z);
		maxp = Math.max(p1.z, p2.z);
		if (minp > maxq) {
			return false;
		}
		if (maxp < minq) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the center.
	 *
	 * @return the center
	 */
	public double[] getCenter() {
		double[] center = new double[3];
		for (int i = 0; i < 3; i++) {
			center[i] = 0.5 * (_min[i] + _max[i]);
		}
		return center;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public double getWidth() {
		return getSize(0);
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public double getHeight() {
		return getSize(1);
	}

	/**
	 * Gets the depth.
	 *
	 * @return the depth
	 */
	public double getDepth() {
		return getSize(2);
	}

	/**
	 * Gets the min x.
	 *
	 * @return the min x
	 */
	public double getMinX() {
		return _min[0];
	}

	/**
	 * Gets the max x.
	 *
	 * @return the max x
	 */
	public double getMaxX() {
		return _max[0];
	}

	/**
	 * Gets the min y.
	 *
	 * @return the min y
	 */
	public double getMinY() {
		return _min[1];
	}

	/**
	 * Gets the max y.
	 *
	 * @return the max y
	 */
	public double getMaxY() {
		return _max[1];
	}

	/**
	 * Gets the min z.
	 *
	 * @return the min z
	 */
	public double getMinZ() {
		return _min[2];
	}

	/**
	 * Gets the max z.
	 *
	 * @return the max z
	 */
	public double getMaxZ() {
		return _max[2];
	}

	/**
	 * Gets the area.
	 *
	 * @return the area
	 */
	public double getArea() {
		return getWidth() * getHeight() * getDepth();
	}

	/**
	 * Min extent.
	 *
	 * @return the double
	 */
	public double minExtent() {
		if (isNull()) {
			return 0.0;
		}
		double w = getWidth();
		double h = getHeight();
		double d = getDepth();
		if (w < h) {
			return (w < d) ? w : d;
		}
		return (h < d) ? h : d;
	}

	/**
	 * Max extent.
	 *
	 * @return the double
	 */
	public double maxExtent() {
		if (isNull()) {
			return 0.0;
		}
		double w = getWidth();
		double h = getHeight();
		double d = getDepth();
		if (w > h) {
			return (w > d) ? w : d;
		}
		return (h > d) ? h : d;
	}

	/**
	 * Translate.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public void translate(final double x, final double y, final double z) {
		if (isNull()) {
			return;
		}
		_min[0] += x;
		_max[0] += x;
		_min[1] += y;
		_max[1] += y;
		_min[2] += z;
		_max[2] += z;
	}

	/**
	 * Gets the triangles.
	 *
	 * @return the triangles
	 */
	public List<int[]> getTriangles() {
		List<int[]> tris = new ArrayList<int[]>();
		int[] tri01 = { 4, 5, 6 };
		int[] tri02 = { 5, 7, 6 };
		tris.add(tri01);
		tris.add(tri02);
		int[] tri11 = { 0, 2, 1 };
		int[] tri12 = { 2, 3, 1 };
		tris.add(tri11);
		tris.add(tri12);
		int[] tri21 = { 0, 1, 4 };
		int[] tri22 = { 1, 5, 4 };
		tris.add(tri21);
		tris.add(tri22);
		int[] tri31 = { 3, 2, 7 };
		int[] tri32 = { 2, 6, 7 };
		tris.add(tri31);
		tris.add(tri32);
		int[] tri41 = { 0, 4, 2 };
		int[] tri42 = { 4, 6, 2 };
		tris.add(tri41);
		tris.add(tri42);
		int[] tri51 = { 1, 3, 5 };
		int[] tri52 = { 3, 7, 5 };
		tris.add(tri51);
		tris.add(tri52);
		return tris;
	}

	/**
	 * Gets the faces.
	 *
	 * @return the faces
	 */
	public List<int[]> getFaces() {
		List<int[]> faces = new ArrayList<int[]>();
		int[] face0 = { 4, 5, 7, 6 };
		faces.add(face0);
		int[] face1 = { 0, 2, 3, 1 };
		faces.add(face1);
		int[] face2 = { 0, 1, 5, 4 };
		faces.add(face2);
		int[] face3 = { 3, 2, 6, 7 };
		faces.add(face3);
		int[] face4 = { 0, 4, 6, 2 };
		faces.add(face4);
		int[] face5 = { 1, 3, 7, 5 };
		faces.add(face5);
		return faces;
	}

	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	public double[] getMin() {
		return _min;
	}

	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public double[] getMax() {
		return _max;
	}

	/**
	 * Gets the dim.
	 *
	 * @return the dim
	 */
	public int getDim() {
		return 3;
	}

	/**
	 * Gets the true dim.
	 *
	 * @return the true dim
	 */
	public int getTrueDim() {
		if (!isValid()) {
			return -1;
		}
		int dim = 0;
		for (int i = 0; i < 3; i++) {
			if ((_max[i] - _min[i]) >= WB_Epsilon.EPSILON) {
				dim++;
			}
		}
		return dim;
	}

	/**
	 * Pad.
	 *
	 * @param factor the factor
	 */
	public void pad(final double factor) {
		final double[] c = getCenter();
		for (int i = 0; i < 3; i++) {
			_min[i] = c[i] + (factor + 1.0) * (_min[i] - c[i]);
			_max[i] = c[i] + (factor + 1.0) * (_max[i] - c[i]);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < 3; i++) {
			result = 37 * result + WB_Point3d.hashCode(_min[i]);
			result = 37 * result + WB_Point3d.hashCode(_max[i]);
		}
		return result;
	}

	/**
	 * Sets the to null.
	 */
	public void setToNull() {
		for (int i = 0; i < 3; i++) {
			_min[i] = 0;
			_max[i] = -1;
		}
	}

	/**
	 * Checks if is null.
	 *
	 * @return true, if is null
	 */
	public boolean isNull() {
		return _max[0] < _min[0];
	}

	/**
	 * Checks if is valid.
	 *
	 * @return true, if is valid
	 */
	public boolean isValid() {
		return !isNull();
	}
}
