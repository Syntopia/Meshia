/**
 * 
 */
package wblut.geom;

import java.util.ArrayList;


// TODO: Auto-generated Javadoc
/**
 * The Class WB_GeomGridCell.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_GeomGridCell {
	
	/** The index. */
	protected int					index;
	
	/** The aabb. */
	protected WB_AABB3D				aabb;
	
	/** The points. */
	protected ArrayList<WB_Point3d>	points;
	
	/** The segments. */
	protected ArrayList<WB_Segment>	segments;

	/**
	 * Instantiates a new w b_ geom grid cell.
	 *
	 * @param index the index
	 * @param min the min
	 * @param max the max
	 */
	public WB_GeomGridCell(final int index, final WB_Point3d min,
			final WB_Point3d max) {
		this.index = index;
		points = new ArrayList<WB_Point3d>();
		segments = new ArrayList<WB_Segment>();
		aabb = new WB_AABB3D(min, max);

	}

	/**
	 * Adds the point.
	 *
	 * @param p the p
	 */
	public void addPoint(final WB_Point3d p) {
		points.add(p);
	}

	/**
	 * Removes the point.
	 *
	 * @param p the p
	 */
	public void removePoint(final WB_Point3d p) {
		points.remove(p);
	}

	/**
	 * Adds the segment.
	 *
	 * @param seg the seg
	 */
	public void addSegment(final WB_Segment seg) {
		segments.add(seg);
	}

	/**
	 * Removes the segment.
	 *
	 * @param seg the seg
	 */
	public void removeSegment(final WB_Segment seg) {
		segments.remove(seg);
	}

	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public ArrayList<WB_Point3d> getPoints() {
		return points;
	}

	/**
	 * Gets the segments.
	 *
	 * @return the segments
	 */
	public ArrayList<WB_Segment> getSegments() {
		return segments;
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the aabb.
	 *
	 * @return the aabb
	 */
	public WB_AABB3D getAABB() {
		return aabb;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return points.isEmpty() && segments.isEmpty();

	}
}
