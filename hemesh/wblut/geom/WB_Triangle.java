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

// TODO: Auto-generated Javadoc
/**
 *  3D Triangle.
 */
public interface WB_Triangle {

	/**
	 * Gets the plane.
	 *
	 * @return the plane
	 */
	public WB_Plane getPlane();

	/**
	 * Gets the center.
	 *
	 * @return the center
	 */
	public WB_Point3d getCenter();

	/**
	 * Gets the centroid.
	 *
	 * @return the centroid
	 */
	public WB_Point3d getCentroid();

	/**
	 * Gets the circumcenter.
	 *
	 * @return the circumcenter
	 */
	public WB_Point3d getCircumcenter();

	/**
	 * Gets the orthocenter.
	 *
	 * @return the orthocenter
	 */
	public WB_Point3d getOrthocenter();

	/**
	 * Gets the point from trilinear.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the point from trilinear
	 */
	public WB_Point3d getPointFromTrilinear(final double x, final double y,
			final double z);

	/**
	 * Gets the point from barycentric.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the point from barycentric
	 */
	public WB_Point3d getPointFromBarycentric(final double x, final double y,
			final double z);

	/**
	 * Gets the barycentric.
	 *
	 * @param p the p
	 * @return the barycentric
	 */
	public WB_Point3d getBarycentric(final WB_Point3d p);

	/**
	 * P1.
	 *
	 * @return the w b_ point3d
	 */
	public WB_Point3d p1();

	/**
	 * P2.
	 *
	 * @return the w b_ point3d
	 */
	public WB_Point3d p2();

	/**
	 * P3.
	 *
	 * @return the w b_ point3d
	 */
	public WB_Point3d p3();

}
