/**
 * 
 */
package wblut.geom;


// TODO: Auto-generated Javadoc
/**
 * The Class WB_FrameStrut.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_FrameStrut {
	
	/** The start. */
	private final WB_FrameNode	start;
	
	/** The end. */
	private final WB_FrameNode	end;
	
	/** The index. */
	private final int		index;
	
	/** The radiuss. */
	private double			radiuss;
	
	/** The radiuse. */
	private double			radiuse;
	
	/** The offsets. */
	private double			offsets;
	
	/** The offsete. */
	private double			offsete;

	/**
	 * Instantiates a new w b_ frame strut.
	 *
	 * @param s the s
	 * @param e the e
	 * @param id the id
	 */
	public WB_FrameStrut(final WB_FrameNode s, final WB_FrameNode e, final int id) {
		start = s;
		end = e;
		index = id;
	}

	/**
	 * Instantiates a new w b_ frame strut.
	 *
	 * @param s the s
	 * @param e the e
	 * @param id the id
	 * @param r the r
	 */
	public WB_FrameStrut(final WB_FrameNode s, final WB_FrameNode e, final int id,
			final double r) {
		start = s;
		end = e;
		index = id;
		radiuss = radiuse = r;
	}

	/**
	 * Instantiates a new w b_ frame strut.
	 *
	 * @param s the s
	 * @param e the e
	 * @param id the id
	 * @param rs the rs
	 * @param re the re
	 */
	public WB_FrameStrut(final WB_FrameNode s, final WB_FrameNode e, final int id,
			final double rs, final double re) {
		start = s;
		end = e;
		index = id;
		radiuss = rs;
		radiuse = re;
	}

	/**
	 * Start.
	 *
	 * @return the w b_ frame node
	 */
	public WB_FrameNode start() {
		return start;
	}

	/**
	 * End.
	 *
	 * @return the w b_ frame node
	 */
	public WB_FrameNode end() {
		return end;
	}

	/**
	 * Gets the start index.
	 *
	 * @return the start index
	 */
	public int getStartIndex() {
		return start.getIndex();
	}

	/**
	 * Gets the end index.
	 *
	 * @return the end index
	 */
	public int getEndIndex() {
		return end.getIndex();
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
	 * To vector.
	 *
	 * @return the w b_ vector3d
	 */
	public WB_Vector3d toVector() {
		return end().subToVector(start());
	}

	/**
	 * To norm vector.
	 *
	 * @return the w b_ vector3d
	 */
	public WB_Vector3d toNormVector() {
		final WB_Vector3d v = end().subToVector(start());
		v.normalize();
		return v;
	}

	/**
	 * Gets the sq length.
	 *
	 * @return the sq length
	 */
	public double getSqLength() {
		return WB_Distance.sqDistance(end(), start());
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public double getLength() {
		return WB_Distance.distance(end(), start());
	}

	/**
	 * Gets the radius start.
	 *
	 * @return the radius start
	 */
	public double getRadiusStart() {
		return radiuss;
	}

	/**
	 * Gets the radius end.
	 *
	 * @return the radius end
	 */
	public double getRadiusEnd() {
		return radiuse;
	}

	/**
	 * Sets the radius start.
	 *
	 * @param r the new radius start
	 */
	public void setRadiusStart(final double r) {
		radiuss = r;
	}

	/**
	 * Sets the radius end.
	 *
	 * @param r the new radius end
	 */
	public void setRadiusEnd(final double r) {
		radiuse = r;
	}

	/**
	 * Gets the offset start.
	 *
	 * @return the offset start
	 */
	public double getOffsetStart() {
		return offsets;
	}

	/**
	 * Gets the offset end.
	 *
	 * @return the offset end
	 */
	public double getOffsetEnd() {
		return offsete;
	}

	/**
	 * Sets the offset start.
	 *
	 * @param o the new offset start
	 */
	public void setOffsetStart(final double o) {
		offsets = o;
	}

	/**
	 * Sets the offset end.
	 *
	 * @param o the new offset end
	 */
	public void setOffsetEnd(final double o) {
		offsete = o;
	}

	/**
	 * Gets the center.
	 *
	 * @return the center
	 */
	public WB_Point3d getCenter() {
		return end().addAndCopy(start()).mult(0.5);
	}

	/**
	 * To segment.
	 *
	 * @return the w b_ explicit segment
	 */
	public WB_ExplicitSegment toSegment() {
		return new WB_ExplicitSegment(start, end);
	}

	/**
	 * To plane.
	 *
	 * @return the w b_ plane
	 */
	public WB_Plane toPlane() {
		return new WB_Plane(start().toPoint(), toVector());
	}

}
