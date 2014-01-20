/**
 * 
 */
package wblut.geom;

import java.util.ArrayList;

import wblut.WB_Epsilon;
import wblut.math.WB_Fast;


import javolution.util.FastList;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_FrameNode.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_FrameNode extends WB_Point3d {
	
	/** The struts. */
	private final FastList<WB_FrameStrut>	struts;
	
	/** The index. */
	protected final int					index;
	
	/** The value. */
	private double						value;

	/**
	 * Instantiates a new w b_ frame node.
	 *
	 * @param pos the pos
	 * @param id the id
	 * @param v the v
	 */
	public WB_FrameNode(final WB_Point3d pos, final int id, final double v) {
		super(pos);
		index = id;
		struts = new FastList<WB_FrameStrut>();
		value = v;
	}

	/**
	 * Adds the strut.
	 *
	 * @param strut the strut
	 * @return true, if successful
	 */
	public boolean addStrut(final WB_FrameStrut strut) {
		if ((strut.start() != this) && (strut.end() != this)) {
			return false;
		}
		for (int i = 0; i < struts.size(); i++) {
			if ((struts.get(i).start() == strut.start())
					&& (struts.get(i).end() == strut.end())) {
				return false;
			}

		}
		struts.add(strut);

		return true;
	}

	/**
	 * Removes the strut.
	 *
	 * @param strut the strut
	 * @return true, if successful
	 */
	public boolean removeStrut(final WB_FrameStrut strut) {
		if ((strut.start() != this) && (strut.end() != this)) {
			return false;
		}

		struts.remove(strut);

		return true;
	}

	/**
	 * Gets the struts.
	 *
	 * @return the struts
	 */
	public ArrayList<WB_FrameStrut> getStruts() {
		final ArrayList<WB_FrameStrut> result = new ArrayList<WB_FrameStrut>();
		result.addAll(struts);
		return result;
	}

	/**
	 * Gets the neighbors.
	 *
	 * @return the neighbors
	 */
	public ArrayList<WB_FrameNode> getNeighbors() {
		final ArrayList<WB_FrameNode> result = new ArrayList<WB_FrameNode>();
		for (int i = 0; i < struts.size(); i++) {
			if (struts.get(i).start() == this) {
				result.add(struts.get(i).end());
			} else {
				result.add(struts.get(i).start());
			}
		}
		return result;
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
	 * Find smallest span.
	 *
	 * @return the double
	 */
	public double findSmallestSpan() {
		double minAngle = Double.MAX_VALUE;
		for (int i = 0; i < getOrder(); i++) {
			minAngle = Math.min(minAngle, findSmallestSpanAroundStrut(i));
		}
		return minAngle;

	}

	/**
	 * Find smallest span around strut.
	 *
	 * @param strut the strut
	 * @return the double
	 */
	public double findSmallestSpanAroundStrut(final WB_FrameStrut strut) {
		return findSmallestSpanAroundStrut(struts.indexOf(strut));

	}

	/**
	 * Find smallest span around strut.
	 *
	 * @param i the i
	 * @return the double
	 */
	public double findSmallestSpanAroundStrut(final int i) {
		final int n = struts.size();
		if ((i < 0) || (i >= n)) {
			throw new IllegalArgumentException("Index beyond strut range.");
		}
		final ArrayList<WB_FrameNode> nnodes = getNeighbors();
		if (n == 1) {
			return 2 * Math.PI;

		} else if (n == 2) {
			final WB_Vector3d u = nnodes.get(0).subToVector(this);
			final WB_Vector3d w = nnodes.get(1).subToVector(this);
			u.normalize();
			w.normalize();

			final double udw = u.dot(w);
			if (udw < WB_Epsilon.EPSILON - 1) {
				return Math.PI;
			} else {
				return Math.acos(udw);
			}
		} else {
			double minAngle = Double.MAX_VALUE;

			final WB_Vector3d u = nnodes.get(i).subToVector(this);
			u.normalize();
			for (int j = 0; j < n; j++) {
				if (i != j) {
					final WB_Vector3d w = nnodes.get(j).subToVector(this);
					w.normalize();
					final double a = Math.acos(u.dot(w));

					minAngle = WB_Fast.min(minAngle, a);
				}
			}

			return minAngle;
		}

	}

	/**
	 * Find shortest strut.
	 *
	 * @return the double
	 */
	public double findShortestStrut() {
		double minLength = Double.MAX_VALUE;
		for (int i = 0; i < struts.size(); i++) {
			minLength = Math.min(minLength, struts.get(i).getSqLength());
		}
		return Math.sqrt(minLength);

	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return struts.size();
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param v the new value
	 */
	public void setValue(final double v) {
		value = v;
	}

	/**
	 * Gets the strut.
	 *
	 * @param index the index
	 * @return the strut
	 */
	public WB_FrameStrut getStrut(final int index) {
		if ((index < 0) || (index >= struts.size())) {
			throw new IllegalArgumentException("Index outside of strut range.");
		}
		return struts.get(index);
	}

	/**
	 * Removes the strut.
	 *
	 * @param index the index
	 */
	public void removeStrut(final int index) {
		if ((index < 0) || (index >= struts.size())) {
			throw new IllegalArgumentException("Index outside of strut range.");
		}
		struts.remove(index);
	}

	/**
	 * Gets the neighbor.
	 *
	 * @param index the index
	 * @return the neighbor
	 */
	public WB_FrameNode getNeighbor(final int index) {
		if ((index < 0) || (index >= struts.size())) {
			throw new IllegalArgumentException("Index outside of strut range.");
		}

		if (struts.get(index).start() == this) {
			return struts.get(index).end();
		}
		return struts.get(index).start();
	}

	/**
	 * To point.
	 *
	 * @return the w b_ point3d
	 */
	public WB_Point3d toPoint() {
		return new WB_Point3d(x, y, z);
	}

}
