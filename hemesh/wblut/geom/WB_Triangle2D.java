package wblut.geom;

// TODO: Auto-generated Javadoc
/**
 *  3D Triangle.
 */
public interface WB_Triangle2D {

	/**
	 * Gets the center.
	 *
	 * @return the center
	 */
	public WB_Point2d getCenter();

	/**
	 * Gets the centroid.
	 *
	 * @return the centroid
	 */
	public WB_Point2d getCentroid();

	/**
	 * Gets the circumcenter.
	 *
	 * @return the circumcenter
	 */
	public WB_Point2d getCircumcenter();

	/**
	 * Gets the orthocenter.
	 *
	 * @return the orthocenter
	 */
	public WB_Point2d getOrthocenter();

	/**
	 * Gets the incenter.
	 *
	 * @return the incenter
	 */
	public WB_Point2d getIncenter();

	/**
	 * Gets the circumcircle.
	 *
	 * @return the circumcircle
	 */
	public WB_Circle getCircumcircle();

	/**
	 * Gets the incircle.
	 *
	 * @return the incircle
	 */
	public WB_Circle getIncircle();

	/**
	 * Gets the point from trilinear.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the point from trilinear
	 */
	public WB_Point2d getPointFromTrilinear(final double x, final double y,
			final double z);

	/**
	 * Gets the point from barycentric.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the point from barycentric
	 */
	public WB_Point2d getPointFromBarycentric(final double x, final double y,
			final double z);

	/**
	 * Gets the barycentric.
	 *
	 * @param p the p
	 * @return the barycentric
	 */
	public WB_Point3d getBarycentric(final WB_Point2d p);

	/**
	 * P1.
	 *
	 * @return the w b_ point2d
	 */
	public WB_Point2d p1();

	/**
	 * P2.
	 *
	 * @return the w b_ point2d
	 */
	public WB_Point2d p2();

	/**
	 * P3.
	 *
	 * @return the w b_ point2d
	 */
	public WB_Point2d p3();

}
