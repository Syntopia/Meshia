/**
 * 
 */
package wblut.geom;

import java.util.Map;


// TODO: Auto-generated Javadoc
/**
 * WB_KDNeighbor stores entries from a nearest-neighbor search. It contains the
 * point, value and squared distance from the query point.
 *
 * @param <V> the value type
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_KDNeighbor<V> implements Comparable<WB_KDNeighbor<V>> {
	
	/** The sq distance. */
	private final double	sqDistance;
	
	/** The neighbor. */
	private final WB_Point3d	neighbor;
	
	/** The value. */
	private final V			value;

	/**
	 * Instantiates a new w b_ kd neighbor.
	 *
	 * @param d2 the d2
	 * @param neighbor the neighbor
	 */
	WB_KDNeighbor(final double d2, final Map.Entry<WB_Point3d, V> neighbor) {
		sqDistance = d2;
		this.neighbor = neighbor.getKey();
		this.value = neighbor.getValue();
	}

	/**
	 * Squared distance to the query point.
	 *
	 * @return squared distance
	 */
	public double sqDistance() {
		return sqDistance;
	}

	/**
	 * Value of neighbor.
	 *
	 * @return value
	 */
	public V value() {
		return value;
	}

	/**
	 * Position of neighbor.
	 *
	 * @return WB_Point
	 */
	public WB_Point3d point() {
		return neighbor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final WB_KDNeighbor<V> obj) {
		final double d = obj.sqDistance();
		if (sqDistance < d) {
			return -1;
		} else if (sqDistance > d) {
			return 1;
		}

		return 0;
	}
}