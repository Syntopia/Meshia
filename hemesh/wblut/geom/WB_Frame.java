/**
 * 
 */
package wblut.geom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javolution.util.FastList;
import wblut.hemesh.HEC_Geodesic;
import wblut.hemesh.HE_Mesh;
import wblut.math.WB_Fast;
import wblut.math.WB_RandomSphere;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_Frame.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_Frame {
	
	/** The struts. */
	private FastList<WB_FrameStrut> struts;
	
	/** The nodes. */
	private FastList<WB_FrameNode> nodes;

	/**
	 * Instantiates a new w b_ frame.
	 */
	public WB_Frame() {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public WB_Frame(final WB_Point3d[] points,
			final WB_IndexedSegment[] connections) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final WB_Point3d point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment connection : connections) {
			addStrut(connection.i1(), connection.i2());
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public WB_Frame(final WB_Point3d[] points,
			final Collection<WB_IndexedSegment> connections) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final WB_Point3d point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment connection : connections) {
			addStrut(connection.i1(), connection.i2());
		}
	}

	/**
	 * Adds the.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public void add(final WB_Point3d[] points,
			final Collection<WB_IndexedSegment> connections) {

		if (struts == null) {
			struts = new FastList<WB_FrameStrut>();
		}
		if (nodes == null) {
			nodes = new FastList<WB_FrameNode>();
		}
		final int nodeoffset = nodes.size();

		for (final WB_Point3d point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment connection : connections) {
			addStrut(connection.i1() + nodeoffset, connection.i2() + nodeoffset);
		}
	}

	/**
	 * Adds the.
	 *
	 * @param frame the frame
	 */
	public void add(final WB_Frame frame) {

		if (struts == null) {
			struts = new FastList<WB_FrameStrut>();
		}
		if (nodes == null) {
			nodes = new FastList<WB_FrameNode>();
		}
		final int nodeoffset = nodes.size();

		for (final WB_FrameNode node : frame.nodes) {
			addNode(node, node.getValue());
		}
		for (final WB_IndexedSegment connection : frame.getIndexedSegments()) {
			addStrut(connection.i1() + nodeoffset, connection.i2() + nodeoffset);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public WB_Frame(final Collection<WB_Point3d> points,
			final Collection<WB_IndexedSegment> connections) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final WB_Point3d point : points) {
			addNode(point, 1);
		}
		for (final WB_IndexedSegment connection : connections) {
			addStrut(connection.i1(), connection.i2());
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public WB_Frame(final WB_Point3d[] points, final int[][] connections) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final WB_Point3d point : points) {
			addNode(point.x, point.y, point.z, 1);
		}
		for (final int[] connection : connections) {
			addStrut(connection[0], connection[1]);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public WB_Frame(final Collection<WB_Point3d> points,
			final int[][] connections) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final WB_Point3d point : points) {
			addNode(point.x, point.y, point.z, 1);
		}
		for (final int[] connection : connections) {
			addStrut(connection[0], connection[1]);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public WB_Frame(final double[][] points, final int[][] connections) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final double[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] connection : connections) {
			addStrut(connection[0], connection[1]);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public WB_Frame(final float[][] points, final int[][] connections) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final float[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] connection : connections) {
			addStrut(connection[0], connection[1]);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 * @param connections the connections
	 */
	public WB_Frame(final int[][] points, final int[][] connections) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final int[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
		for (final int[] connection : connections) {
			addStrut(connection[0], connection[1]);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 */
	public WB_Frame(final WB_Point3d[] points) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final WB_Point3d point : points) {
			addNode(point.x, point.y, point.z, 1);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 */
	public WB_Frame(final Collection<WB_Point3d> points) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final WB_Point3d point : points) {
			addNode(point.x, point.y, point.z, 1);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 */
	public WB_Frame(final double[][] points) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final double[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}
	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 */
	public WB_Frame(final float[][] points) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final float[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}

	}

	/**
	 * Instantiates a new w b_ frame.
	 *
	 * @param points the points
	 */
	public WB_Frame(final int[][] points) {
		struts = new FastList<WB_FrameStrut>();
		nodes = new FastList<WB_FrameNode>();
		for (final int[] point : points) {
			addNode(point[0], point[1], point[2], 1);
		}

	}

	/**
	 * Adds the node.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param v the v
	 * @return the int
	 */
	public int addNode(final double x, final double y, final double z,
			final double v) {
		final int n = nodes.size();
		nodes.add(new WB_FrameNode(new WB_Point3d(x, y, z), n, v));
		return n;
	}

	/**
	 * Adds the node.
	 *
	 * @param pos the pos
	 * @param v the v
	 * @return the int
	 */
	public int addNode(final WB_Point3d pos, final double v) {
		final int n = nodes.size();
		nodes.add(new WB_FrameNode(pos, n, v));
		return n;
	}

	/**
	 * Removes the node.
	 *
	 * @param node the node
	 */
	public void removeNode(final WB_FrameNode node) {
		for (final WB_FrameStrut strut : node.getStruts()) {
			removeStrut(strut);
		}
		nodes.remove(node);
	}

	/**
	 * Adds the nodes.
	 *
	 * @param pos the pos
	 * @return the int
	 */
	public int addNodes(final Collection<WB_Point3d> pos) {
		int n = nodes.size();
		final Iterator<WB_Point3d> pItr = pos.iterator();
		while (pItr.hasNext()) {
			nodes.add(new WB_FrameNode(pItr.next(), n, 1));
			n++;
		}
		return n;
	}

	/**
	 * Adds the strut.
	 *
	 * @param i the i
	 * @param j the j
	 * @return the int
	 */
	public int addStrut(final int i, final int j) {
		if (i == j) {
			throw new IllegalArgumentException(
					"Strut can't connect a node to itself: " + i + " " + j
							+ ".");
		}
		final int nn = nodes.size();
		if ((i < 0) || (j < 0) || (i >= nn) || (j >= nn)) {
			throw new IllegalArgumentException(
					"Strut indices outside node range.");
		}
		final int n = struts.size();
		WB_FrameStrut strut;
		if (i <= j) {
			strut = new WB_FrameStrut(nodes.get(i), nodes.get(j), n);
		} else {
			strut = new WB_FrameStrut(nodes.get(j), nodes.get(i), n);
		}
		if (!nodes.get(i).addStrut(strut)) {
			System.out.println("WB_Frame : Strut " + i + "-" + j
					+ " already added.");
		} else if (!nodes.get(j).addStrut(strut)) {
			System.out.println("WB_Frame : Strut " + i + "-" + j
					+ " already added.");
		} else {

			struts.add(strut);
		}
		return n;
	}

	/**
	 * Removes the strut.
	 *
	 * @param strut the strut
	 */
	public void removeStrut(final WB_FrameStrut strut) {
		nodes.get(strut.getStartIndex()).removeStrut(strut);
		nodes.get(strut.getEndIndex()).removeStrut(strut);
		struts.remove(strut);
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
	 * Gets the segments.
	 *
	 * @return the segments
	 */
	public ArrayList<WB_ExplicitSegment> getSegments() {
		final ArrayList<WB_ExplicitSegment> result = new ArrayList<WB_ExplicitSegment>();
		for (final WB_FrameStrut strut : struts) {
			result.add(strut.toSegment());
		}
		return result;
	}

	/**
	 * Gets the indexed segments.
	 *
	 * @return the indexed segments
	 */
	public ArrayList<WB_IndexedSegment> getIndexedSegments() {
		final ArrayList<WB_Point3d> apoints = getPoints();
		WB_Point3d[] ipoints = new WB_Point3d[apoints.size()];
		ipoints = apoints.toArray(ipoints);
		final ArrayList<WB_IndexedSegment> result = new ArrayList<WB_IndexedSegment>();
		for (final WB_FrameStrut strut : struts) {
			result.add(new WB_IndexedSegment(strut.getStartIndex(), strut
					.getEndIndex(), ipoints));
		}
		return result;
	}

	/**
	 * Gets the number of struts.
	 *
	 * @return the number of struts
	 */
	public int getNumberOfStruts() {
		return struts.size();
	}

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public ArrayList<WB_FrameNode> getNodes() {
		final ArrayList<WB_FrameNode> result = new ArrayList<WB_FrameNode>();
		result.addAll(nodes);
		return result;
	}

	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public ArrayList<WB_Point3d> getPoints() {
		final ArrayList<WB_Point3d> result = new ArrayList<WB_Point3d>();
		result.addAll(nodes);
		return result;
	}

	/**
	 * Gets the points as array.
	 *
	 * @return the points as array
	 */
	public WB_Point3d[] getPointsAsArray() {
		final ArrayList<WB_Point3d> result = new ArrayList<WB_Point3d>();
		result.addAll(nodes);
		final ArrayList<WB_Point3d> apoints = getPoints();
		final WB_Point3d[] ipoints = new WB_Point3d[apoints.size()];
		return apoints.toArray(ipoints);

	}

	/**
	 * Gets the number of nodes.
	 *
	 * @return the number of nodes
	 */
	public int getNumberOfNodes() {
		return nodes.size();
	}

	/**
	 * Gets the node.
	 *
	 * @param i the i
	 * @return the node
	 */
	public WB_FrameNode getNode(final int i) {
		if ((i < 0) || (i >= nodes.size())) {
			throw new IllegalArgumentException("Index outside of node range.");
		}
		return nodes.get(i);

	}

	/**
	 * Gets the strut.
	 *
	 * @param i the i
	 * @return the strut
	 */
	public WB_FrameStrut getStrut(final int i) {
		if ((i < 0) || (i >= struts.size())) {
			throw new IllegalArgumentException("Index outside of strut range.");
		}
		return struts.get(i);

	}

	/**
	 * Gets the distance to frame.
	 *
	 * @param p the p
	 * @return the distance to frame
	 */
	public double getDistanceToFrame(final WB_Point3d p) {
		double d = Double.POSITIVE_INFINITY;
		for (int i = 0; i < struts.size(); i++) {
			final WB_FrameStrut strut = struts.get(i);
			final WB_ExplicitSegment S = new WB_ExplicitSegment(strut.start(),
					strut.end());
			d = Math.min(d, WB_Distance.distance(p, S));
		}
		return d;
	}

	/**
	 * Gets the closest node on frame.
	 *
	 * @param p the p
	 * @return the closest node on frame
	 */
	public int getClosestNodeOnFrame(final WB_Point3d p) {
		double mind = Double.POSITIVE_INFINITY;
		int q = -1;
		for (int i = 0; i < nodes.size(); i++) {

			final double d = WB_Distance.sqDistance(p, nodes.get(i));
			if (d < mind) {
				mind = d;
				q = i;
			}

		}
		return q;
	}

	/**
	 * Gets the closest point on frame.
	 *
	 * @param p the p
	 * @return the closest point on frame
	 */
	public WB_Point3d getClosestPointOnFrame(final WB_Point3d p) {
		double mind = Double.POSITIVE_INFINITY;
		WB_Point3d q = new WB_Point3d(p);
		for (int i = 0; i < struts.size(); i++) {
			final WB_FrameStrut strut = struts.get(i);
			final WB_ExplicitSegment S = new WB_ExplicitSegment(strut.start(),
					strut.end());

			final double d = WB_Distance.distance(p, S);
			if (d < mind) {
				mind = d;
				q = WB_Intersection.closestPoint(S, p);
			}

		}
		return q;
	}

	/**
	 * Gets the distance to frame.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the distance to frame
	 */
	public double getDistanceToFrame(final double x, final double y,
			final double z) {
		double d = Double.POSITIVE_INFINITY;
		for (int i = 0; i < struts.size(); i++) {
			final WB_FrameStrut strut = struts.get(i);
			final WB_ExplicitSegment S = new WB_ExplicitSegment(strut.start(),
					strut.end());
			d = Math.min(d, WB_Distance.distance(new WB_Point3d(x, y, z), S));
		}
		return d;
	}

	/**
	 * Gets the closest point on frame.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the closest point on frame
	 */
	public WB_Point3d getClosestPointOnFrame(final double x, final double y,
			final double z) {
		double mind = Double.POSITIVE_INFINITY;
		WB_Point3d q = new WB_Point3d(x, y, z);
		for (int i = 0; i < struts.size(); i++) {
			final WB_FrameStrut strut = struts.get(i);
			final WB_ExplicitSegment S = new WB_ExplicitSegment(strut.start(),
					strut.end());

			final double d = WB_Distance.distance(new WB_Point3d(x, y, z), S);
			if (d < mind) {
				mind = d;
				q = WB_Intersection.closestPoint(S, new WB_Point3d(x, y, z));
			}

		}
		return q;
	}

	/**
	 * Smooth bi nodes.
	 */
	public void smoothBiNodes() {
		final WB_Point3d[] newPos = new WB_Point3d[nodes.size()];
		int id = 0;
		for (final WB_FrameNode node : nodes) {
			if (node.getOrder() == 2) {
				newPos[id] = node.getNeighbor(0)
						.addAndCopy(node.getNeighbor(1));

				newPos[id].mult(0.5);
				newPos[id].add(node);
				newPos[id].mult(0.5);
			}
			id++;
		}
		id = 0;
		for (final WB_FrameNode node : nodes) {
			if (node.getOrder() == 2) {
				node.set(newPos[id]);
			}
			id++;
		}
	}

	/**
	 * Refine.
	 *
	 * @param threshold the threshold
	 * @return the w b_ frame
	 */
	public WB_Frame refine(final double threshold) {

		final FastList<WB_Point3d> npoints = new FastList<WB_Point3d>();
		for (final WB_FrameNode node : nodes) {
			npoints.add(node);
		}
		for (final WB_FrameStrut strut : struts) {
			if (strut.getLength() > threshold) {
				final WB_Point3d start = strut.start();
				final WB_Point3d end = strut.end();
				final WB_Point3d mid = WB_Point3d.interpolate(start, end, 0.5);
				npoints.add(mid);
			}
		}
		final int n = getNumberOfNodes();
		int id = 0;
		final WB_Frame result = new WB_Frame(npoints);
		for (final WB_FrameStrut strut : struts) {
			if (strut.getLength() > threshold) {
				final int start = strut.getStartIndex();
				final int end = strut.getEndIndex();
				result.addStrut(start, n + id);
				result.addStrut(n + id, end);
				id++;
			} else {
				final int start = strut.getStartIndex();
				final int end = strut.getEndIndex();
				result.addStrut(start, end);
			}
		}

		return result;

	}

	/**
	 * To point cloud.
	 *
	 * @param n the n
	 * @param r the r
	 * @param d the d
	 * @param l the l
	 * @param rr the rr
	 * @param dr the dr
	 * @return the list
	 */
	public List<WB_Point3d> toPointCloud(int n, double r, double d, int l,
			double rr, double dr) {
		List<WB_Point3d> points = new FastList<WB_Point3d>();

		double sl, dsl;
		int divs;
		WB_Plane P;
		WB_Vector3d u, localu, v;
		WB_Point3d offset;
		WB_Point3d p;
		WB_RandomSphere rnd = new WB_RandomSphere();
		double da = 2.0 * Math.PI / n;
		for (WB_FrameStrut strut : struts) {
			sl = strut.getLength() - 2 * rr;

			if (sl > 0) {
				divs = (int) WB_Fast.max(1, Math.round(sl / d));
				dsl = sl / divs;
				P = strut.toPlane();
				u = P.getU().multAndCopy(r);
				v = strut.toNormVector().get();
				offset = strut.start().addAndCopy(v, rr);
				v.mult(dsl);
				for (int i = 0; i <= divs; i++) {
					for (int j = 0; j < n; j++) {
						p = strut.start().addAndCopy(v, i);
						localu = u.get();
						localu.rotateAboutAxis(j * da, new WB_Point3d(),
								P.getNormal());
						p.add(localu);
						p.add(rnd.nextVector().mult(dr));
						points.add(p);
					}
				}
			}
		}
		for (WB_FrameNode node : nodes) {
			final HE_Mesh ball = new HE_Mesh(new HEC_Geodesic().setRadius(rr)
					.setLevel(l).setCenter(node));

			for (WB_Point3d q : ball.getVerticesAsPoint()) {
				points.add(q.add(rnd.nextVector().mult(dr)));
			}

		}
		return points;

	}
}
