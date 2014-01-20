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
package wblut.geom;

import wblut.WB_Epsilon;

// TODO: Auto-generated Javadoc
/**
 * Axis-aligned bounding box.
 *
 */
public class WB_AABB3D {

	/** Minimum x, y and z values. */
	protected WB_Point3d	min;

	/** Maximum x, y and z values. */
	protected WB_Point3d	max;

	/**
	 * Instantiates a new WB_AABB.
	 */
	public WB_AABB3D() {
		min = new WB_Point3d();
		max = new WB_Point3d();
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @param src the src
	 */
	public void set(final WB_AABB3D src) {
		min = src.min.get();
		max = src.max.get();
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @return the w b_ aab b3 d
	 */
	public WB_AABB3D get() {
		return new WB_AABB3D(min, max);
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @param points point cloud
	 * @param n number of points
	 */
	public WB_AABB3D(final WB_Point3d[] points, final int n) {
		min = new WB_Point3d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		max = new WB_Point3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
				Double.NEGATIVE_INFINITY);
		for (int i = 0; i < n; i++) {
			if (min.x > points[i].x) {
				min.x = points[i].x;
			}
			if (min.y > points[i].y) {
				min.y = points[i].y;
			}
			if (min.z > points[i].z) {
				min.z = points[i].z;
			}
			if (max.x <= points[i].x) {
				max.x = points[i].x;
			}
			if (max.y <= points[i].y) {
				max.y = points[i].y;
			}
			if (max.z <= points[i].z) {
				max.z = points[i].z;
			}

		}
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @param minx the minx
	 * @param miny the miny
	 * @param minz the minz
	 * @param maxx the maxx
	 * @param maxy the maxy
	 * @param maxz the maxz
	 */
	public WB_AABB3D(final double minx, final double miny, final double minz,
			final double maxx, final double maxy, final double maxz) {
		min = new WB_Point3d(minx, miny, minz);
		max = new WB_Point3d(maxx, maxy, maxz);
		check();
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @param min minimum values as double[3]
	 * @param max maximum values as double[3]
	 */
	public WB_AABB3D(final double[] min, final double[] max) {
		this.min = new WB_Point3d(min[0], min[1], min[2]);
		this.max = new WB_Point3d(max[0], max[1], max[2]);
		check();
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @param min minimum values as float[3]
	 * @param max maximum values as float[3]
	 */
	public WB_AABB3D(final float[] min, final float[] max) {
		this.min = new WB_Point3d(min[0], min[1], min[2]);
		this.max = new WB_Point3d(max[0], max[1], max[2]);
		check();
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @param min minimum values as int[3]
	 * @param max maximum values as int[3]
	 */
	public WB_AABB3D(final int[] min, final int[] max) {
		this.min = new WB_Point3d(min[0], min[1], min[2]);
		this.max = new WB_Point3d(max[0], max[1], max[2]);
		check();
	}

	/**
	 * Instantiates a new WB_AABB.
	 *
	 * @param min minimum values as WB_Point
	 * @param max maximum values as WB_Point
	 */
	public WB_AABB3D(final WB_Point3d min, final WB_Point3d max) {
		this.min = min.get();
		this.max = max.get();
		check();
	}

	/**
	 * Sets the.
	 *
	 * @param min minimum values as WB_Point
	 * @param max maximum values as WB_Point
	 */
	public void set(final WB_Point3d min, final WB_Point3d max) {
		this.min = min.get();
		this.max = max.get();
		check();
	}

	/**
	 * Sets the.
	 *
	 * @param points point cloud
	 * @param n number of points
	 */
	public void set(final WB_Point3d[] points, final int n) {
		min = new WB_Point3d(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		max = new WB_Point3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
				Double.NEGATIVE_INFINITY);
		for (int i = 0; i < n; i++) {
			if (min.x > points[i].x) {
				min.x = points[i].x;
			}
			if (min.y > points[i].y) {
				min.y = points[i].y;
			}
			if (min.z > points[i].z) {
				min.z = points[i].z;
			}
			if (max.x <= points[i].x) {
				max.x = points[i].x;
			}
			if (max.y <= points[i].y) {
				max.y = points[i].y;
			}
			if (max.z <= points[i].z) {
				max.z = points[i].z;
			}

		}
	}

	/**
	 * Sets the.
	 *
	 * @param minx the minx
	 * @param miny the miny
	 * @param minz the minz
	 * @param maxx the maxx
	 * @param maxy the maxy
	 * @param maxz the maxz
	 */
	public void set(final double minx, final double miny, final double minz,
			final double maxx, final double maxy, final double maxz) {
		min = new WB_Point3d(minx, miny, minz);
		max = new WB_Point3d(maxx, maxy, maxz);
		check();
	}

	/**
	 * Sets the.
	 *
	 * @param min minimum values as double[3]
	 * @param max maximum values as double[3]
	 */
	public void set(final double[] min, final double[] max) {
		this.min = new WB_Point3d(min[0], min[1], min[2]);
		this.max = new WB_Point3d(max[0], max[1], max[2]);
		check();
	}

	/**
	 * Sets the.
	 *
	 * @param min minimum values as float[3]
	 * @param max maximum values as float[3]
	 */
	public void set(final float[] min, final float[] max) {
		this.min = new WB_Point3d(min[0], min[1], min[2]);
		this.max = new WB_Point3d(max[0], max[1], max[2]);
		check();
	}

	/**
	 * Sets the.
	 *
	 * @param min minimum values as int[3]
	 * @param max maximum values as int[3]
	 */
	public void set(final int[] min, final int[] max) {
		this.min = new WB_Point3d(min[0], min[1], min[2]);
		this.max = new WB_Point3d(max[0], max[1], max[2]);
		check();
	}

	/**
	 * Check.
	 */
	private void check() {
		double tmp;
		if (min.x > max.x) {
			tmp = min.x;
			min.x = max.x;
			max.x = tmp;
		}
		if (min.y > max.y) {
			tmp = min.y;
			min.y = max.y;
			max.y = tmp;
		}
		if (min.z > max.z) {
			tmp = min.z;
			min.z = max.z;
			max.z = tmp;
		}

	}

	/**
	 * Gets the dimension.
	 *
	 * @return the dimension
	 */
	public int getDimension() {
		int dim = 0;
		if (WB_Epsilon.isEqualAbs(min.x, max.x)) {
			dim++;
		}
		if (WB_Epsilon.isEqualAbs(min.y, max.y)) {
			dim++;
		}
		if (WB_Epsilon.isEqualAbs(min.z, max.z)) {
			dim++;
		}
		return dim;
	}

	/**
	 * Gets the min.
	 *
	 * @return the min
	 */
	public WB_Point3d getMin() {
		return min;

	}

	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public WB_Point3d getMax() {
		return max;

	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public double getWidth() {
		return max.x - min.x;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public double getHeight() {
		return max.y - min.y;
	}

	/**
	 * Gets the depth.
	 *
	 * @return the depth
	 */
	public double getDepth() {
		return max.z - min.z;
	}

	/**
	 * Gets the center.
	 *
	 * @return the center
	 */
	public WB_Point3d getCenter() {
		return new WB_Point3d(0.5 * (max.x + min.x), 0.5 * (max.y + min.y),
				0.5 * (max.z + min.z));

	}

	/**
	 * Union.
	 *
	 * @param aabb the aabb
	 * @return the w b_ aab b3 d
	 */
	public WB_AABB3D union(final WB_AABB3D aabb) {
		final WB_Point3d newmin = new WB_Point3d(Math.min(min.x, aabb.getMin().x),
				Math.min(min.y, aabb.getMin().y), Math.min(min.z,
						aabb.getMin().z));
		final WB_Point3d newmax = new WB_Point3d(Math.max(max.x, aabb.getMax().x),
				Math.max(max.y, aabb.getMax().y), Math.max(max.z,
						aabb.getMax().z));
		return new WB_AABB3D(newmin, newmax);
	}

	/**
	 * Squared distance between point and axis-aligned box.
	 *
	 * @param p point
	 * @return squared distance
	 */
	public double sqDistance(final WB_Point3d p) {
		return WB_Distance.sqDistance(p, this);
	}

	/**
	 * Distance between point and axis-aligned box.
	 *
	 * @param p point
	 * @return distance
	 */
	public double distance(final WB_Point3d p) {
		return WB_Distance.sqDistance(p, this);
	}

	/**
	 * Gets the intersection.
	 *
	 * @param R the r
	 * @return the intersection
	 */
	public WB_IntersectionResult getIntersection(final WB_Ray R) {
		return WB_Intersection.getIntersection(R, this);
	}

	/**
	 * Check intersection.
	 *
	 * @param T the t
	 * @return true, if successful
	 */
	public boolean checkIntersection(final WB_Triangle T) {
		return WB_Intersection.checkIntersection(T, this);
	}

	/**
	 * Check intersection.
	 *
	 * @param S the s
	 * @return true, if successful
	 */
	public boolean checkIntersection(final WB_Segment S) {
		return WB_Intersection.checkIntersection(S, this);
	}

	/**
	 * Check intersection.
	 *
	 * @param R the r
	 * @return true, if successful
	 */
	public boolean checkIntersection(final WB_Ray R) {
		return WB_Intersection.checkIntersection(R, this);
	}

	/**
	 * Check intersection.
	 *
	 * @param L the l
	 * @return true, if successful
	 */
	public boolean checkIntersection(final WB_Line L) {
		return WB_Intersection.checkIntersection(L, this);
	}

	/**
	 * Check intersection.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean checkIntersection(final WB_AABB3D other) {
		return WB_Intersection.checkIntersection(this, other);
	}

	/**
	 * Check intersection.
	 *
	 * @param P the p
	 * @return true, if successful
	 */
	public boolean checkIntersection(final WB_Plane P) {
		return WB_Intersection.checkIntersection(this, P);
	}

	/**
	 * Check intersection.
	 *
	 * @param S the s
	 * @return true, if successful
	 */
	public boolean checkIntersection(final WB_Sphere S) {
		return WB_Intersection.checkIntersection(this, S);
	}

	/**
	 * Contains.
	 *
	 * @param p the p
	 * @return true, if successful
	 */
	public boolean contains(final WB_Point3d p) {
		return WB_Containment.contains(p, this);

	}

}
