/**
 * 
 */
package wblut.hemesh;

import java.util.ArrayList;
import java.util.Iterator;

import wblut.geom.WB_Frame;
import wblut.geom.WB_FrameNode;
import wblut.geom.WB_Plane;
import wblut.geom.WB_Point3d;
import wblut.geom.WB_FrameStrut;
import wblut.geom.WB_Vector3d;
import wblut.math.WB_ConstantParameter;
import wblut.math.WB_Parameter;


import javolution.util.FastMap;

// TODO: Auto-generated Javadoc
/**
 * The Class HEC_FromFrame.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class HEC_FromFrame extends HEC_Creator {
	
	/** The frame. */
	private WB_Frame				frame;
	
	/** The number of struts. */
	private int						numberOfNodes, numberOfStruts;
	
	/** The node types. */
	private NodeType[]				nodeTypes;
	
	/** The strut node connections. */
	private StrutNodeConnection[]	strutNodeConnections;
	
	/** The strut radius. */
	private WB_Parameter<Double>	strutRadius;
	
	/** The strut facets. */
	private int						strutFacets;
	
	/** The fidget. */
	private double					fidget;
	
	/** The fillfactor. */
	private double					fillfactor;
	
	/** The mesh. */
	private HE_Mesh					mesh;
	
	/** The maximum strut length. */
	private double					maximumStrutLength;
	
	/** The minimum balljoint angle. */
	private double					minimumBalljointAngle;
	
	/** The maximum strut offset. */
	private WB_Parameter<Double>	maximumStrutOffset;
	
	/** The taper. */
	private boolean					taper;
	
	/** The cap. */
	private boolean					cap;
	
	/** The use node values. */
	private boolean					useNodeValues;
	
	/** The create isolated nodes. */
	private boolean					createIsolatedNodes;
	
	/** The angle factor. */
	private WB_Parameter<Double>	angleFactor;
	
	/** The suppress balljoint. */
	private boolean					suppressBalljoint;

	/**
	 * The Enum NodeType.
	 */
	enum NodeType {
		
		/** The isolated. */
		ISOLATED, 
 /** The endpoint. */
 ENDPOINT, 
 /** The straight. */
 STRAIGHT, 
 /** The bend. */
 BEND, 
 /** The turn. */
 TURN, 
 /** The star. */
 STAR
	}

	/**
	 * The Class StrutNodeConnection.
	 */
	class StrutNodeConnection {
		
		/** The maxoffset. */
		double					maxoffset;
		
		/** The offset. */
		double					offset;
		
		/** The radius. */
		double					radius;
		
		/** The vertices. */
		ArrayList<HE_Vertex>	vertices;
		
		/** The dir. */
		WB_Vector3d				dir;
		
		/** The node. */
		WB_FrameNode					node;
		
		/** The strut. */
		WB_FrameStrut				strut;

		/**
		 * Instantiates a new strut node connection.
		 *
		 * @param node the node
		 * @param strut the strut
		 * @param mo the mo
		 * @param o the o
		 * @param r the r
		 */
		StrutNodeConnection(final WB_FrameNode node, final WB_FrameStrut strut,
				final double mo, final double o, final double r) {
			maxoffset = mo;
			offset = o;
			radius = r;
			vertices = new ArrayList<HE_Vertex>();
			this.node = node;
			this.strut = strut;

		}

	}

	/**
	 * Instantiates a new hE c_ from frame.
	 */
	public HEC_FromFrame() {
		strutRadius = new WB_ConstantParameter<Double>(10.0);
		strutFacets = 6;
		maximumStrutLength = 100.0;
		override = true;
		fidget = 1.0001;
		fillfactor = 0.99;
		minimumBalljointAngle = 0.55 * Math.PI;
		maximumStrutOffset = new WB_ConstantParameter<Double>(Double.MAX_VALUE);
		angleFactor = new WB_ConstantParameter<Double>(0.0);
		cap = true;
		useNodeValues = true;
	}

	/**
	 * Set the radius of the connections.
	 *
	 * @param r the r
	 * @return self
	 */
	public HEC_FromFrame setStrutRadius(final double r) {
		strutRadius = new WB_ConstantParameter<Double>(r);
		return this;
	}

	/**
	 * Set the radius of the connections.
	 *
	 * @param r the r
	 * @return self
	 */
	public HEC_FromFrame setStrutRadius(final WB_Parameter<Double> r) {
		strutRadius = r;
		return this;
	}

	/**
	 * Set the offset of the connections.
	 *
	 * @param o the o
	 * @return self
	 */
	public HEC_FromFrame setMaximumStrutOffset(final WB_Parameter<Double> o) {
		maximumStrutOffset = o;
		return this;
	}

	/**
	 * Set the offset of the connections.
	 *
	 * @param o the o
	 * @return self
	 */
	public HEC_FromFrame setMaximumStrutOffset(final double o) {
		maximumStrutOffset = new WB_ConstantParameter<Double>(o);
		return this;
	}

	/**
	 * Sets the minimum balljoint angle.
	 *
	 * @param a the a
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setMinimumBalljointAngle(final double a) {
		minimumBalljointAngle = a;
		return this;
	}

	/**
	 * Sets the maximum strut length.
	 *
	 * @param d the d
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setMaximumStrutLength(final double d) {
		maximumStrutLength = d;
		return this;
	}

	/**
	 * Sets the strut facets.
	 *
	 * @param f the f
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setStrutFacets(final int f) {
		strutFacets = f;
		return this;
	}

	/**
	 * Sets the taper.
	 *
	 * @param b the b
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setTaper(final boolean b) {
		taper = b;
		return this;
	}

	/**
	 * Sets the cap.
	 *
	 * @param b the b
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setCap(final boolean b) {
		cap = b;
		return this;
	}

	/**
	 * Sets the suppress balljoint.
	 *
	 * @param b the b
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setSuppressBalljoint(final boolean b) {
		suppressBalljoint = b;
		return this;
	}

	/**
	 * Sets the use node values.
	 *
	 * @param b the b
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setUseNodeValues(final boolean b) {
		useNodeValues = b;
		return this;
	}

	/**
	 * Sets the create isolated nodes.
	 *
	 * @param b the b
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setCreateIsolatedNodes(final boolean b) {
		createIsolatedNodes = b;
		return this;
	}

	/**
	 * Sets the fidget.
	 *
	 * @param f the f
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setFidget(final double f) {
		fidget = f;
		return this;
	}

	/**
	 * Sets the fill factor.
	 *
	 * @param ff the ff
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setFillFactor(final double ff) {
		fillfactor = 0.99;
		return this;
	}

	/**
	 * Sets the angle offset.
	 *
	 * @param af the af
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setAngleOffset(final double af) {
		angleFactor = new WB_ConstantParameter<Double>(af);
		return this;
	}

	/**
	 * Sets the angle offset.
	 *
	 * @param af the af
	 * @return the hE c_ from frame
	 */
	public HEC_FromFrame setAngleOffset(final WB_Parameter<Double> af) {
		angleFactor = af;
		return this;
	}

	/**
	 * Gets the node types.
	 *
	 * @return the node types
	 */
	private void getNodeTypes() {
		int i = 0;
		double minSpan;
		for (final WB_FrameNode node : frame.getNodes()) {
			if (node.getOrder() == 0) {
				nodeTypes[i] = NodeType.ISOLATED;
			} else if (node.getOrder() == 1) {
				nodeTypes[i] = NodeType.ENDPOINT;
			} else if (node.getOrder() == 2) {
				minSpan = node.findSmallestSpan();
				if (minSpan == Math.PI) {
					nodeTypes[i] = NodeType.STRAIGHT;
				} else if (minSpan > minimumBalljointAngle) {
					nodeTypes[i] = NodeType.BEND;
				}

				else {
					nodeTypes[i] = NodeType.TURN;
				}
			} else {
				nodeTypes[i] = NodeType.STAR;
			}
			i++;
		}
	}

	/**
	 * Gets the strut node connections.
	 *
	 * @return the strut node connections
	 */
	private void getStrutNodeConnections() {

		int i = 0;
		for (final WB_FrameNode node : frame.getNodes()) {
			if (nodeTypes[i] == NodeType.ENDPOINT) {
				double r = strutRadius.value(node.x, node.y, node.z);
				if (useNodeValues) {
					r *= node.getValue();
				}
				createNodeStrutConnection(node, 0, 0.0, 0.0, r);
			} else if (nodeTypes[i] == NodeType.STRAIGHT) {
				double r = strutRadius.value(node.x, node.y, node.z);
				if (useNodeValues) {
					r *= node.getValue();
				}
				double o = strutRadius.value(node.x, node.y, node.z);
				if (useNodeValues) {
					o *= node.getValue();
				}
				createNodeStrutConnection(node, 0, o, o, r);
				createNodeStrutConnection(node, 1, o, o, r);
			} else if ((nodeTypes[i] == NodeType.TURN)
					|| (nodeTypes[i] == NodeType.BEND)) {
				final double minSpan = node.findSmallestSpan();
				double r = strutRadius.value(node.x, node.y, node.z);
				double o = fidget * r / Math.min(1.0, Math.tan(0.5 * minSpan));
				if (useNodeValues) {
					r *= node.getValue();
				}
				if (useNodeValues) {
					o *= node.getValue();
				}
				createNodeStrutConnection(node, 0, o, o, r);
				createNodeStrutConnection(node, 1, o, o, r);
			} else if (nodeTypes[i] == NodeType.STAR) {
				final double minSpan = node.findSmallestSpan();

				double r = strutRadius.value(node.x, node.y, node.z);
				double mo = fidget * r / Math.min(1.0, Math.tan(0.5 * minSpan));
				if (useNodeValues) {
					r *= node.getValue();
				}
				if (useNodeValues) {
					mo *= node.getValue();
				}
				for (int j = 0; j < node.getOrder(); j++) {
					final double minLocSpan = node
							.findSmallestSpanAroundStrut(j);
					double o = fidget * r
							/ Math.min(1.0, Math.tan(0.5 * minLocSpan));
					if (useNodeValues) {
						o *= node.getValue();
					}
					double mso = maximumStrutOffset.value(node.x, node.y,
							node.z);
					if (useNodeValues) {
						mso *= node.getValue();
					}
					if (o > mso) {
						createNodeStrutConnection(
								node,
								j,
								mo,
								mso,
								mso
										/ fidget
										* Math.min(1.0,
												Math.tan(0.5 * minLocSpan)));

					} else {
						createNodeStrutConnection(node, j, mo, o, r);
					}
				}
			}
			i++;
		}
		if (!taper) {
			for (i = 0; i < frame.getNumberOfStruts(); i++) {
				final double r = Math.min(strutNodeConnections[2 * i].radius,
						strutNodeConnections[2 * i + 1].radius);
				strutNodeConnections[2 * i].radius = r;
				strutNodeConnections[2 * i + 1].radius = r;
			}

		}

	}

	/**
	 * Creates the node strut connection.
	 *
	 * @param node the node
	 * @param i the i
	 * @param maxoff the maxoff
	 * @param off the off
	 * @param rad the rad
	 */
	private void createNodeStrutConnection(final WB_FrameNode node, final int i,
			final double maxoff, final double off, final double rad) {
		final WB_FrameStrut strut = node.getStrut(i);
		final int id = getStrutIndex(node, strut);
		strutNodeConnections[id] = new StrutNodeConnection(node, strut, maxoff,
				off, rad);
	}

	/**
	 * Creates the vertices.
	 */
	private void createVertices() {
		final double da = 2 * Math.PI / strutFacets;
		for (int id = 0; id < frame.getNumberOfStruts() * 2; id++) {
			final double sr = strutNodeConnections[id].radius;
			final double sgn = (strutNodeConnections[id].node == strutNodeConnections[id].strut
					.start()) ? 1 : -1;
			final double so = strutNodeConnections[id].maxoffset;
			final WB_Vector3d v = strutNodeConnections[id].strut.toVector();
			v.normalize();
			v.mult(sgn);
			strutNodeConnections[id].dir = v.get();
			final WB_Plane P = strutNodeConnections[id].strut.toPlane();
			final WB_Vector3d u = P.getU();
			for (int j = 0; j < strutFacets; j++) {
				final WB_Point3d p = new WB_Point3d(
						strutNodeConnections[id].node);
				final double af = angleFactor.value(
						strutNodeConnections[id].node.x,
						strutNodeConnections[id].node.y,
						strutNodeConnections[id].node.z);
				p.add(v, so);
				final WB_Vector3d localu = u.multAndCopy(sr);
				localu.rotateAboutAxis((j + af) * da, new WB_Point3d(),
						P.getNormal());
				p.add(localu);
				final HE_Vertex vrtx = new HE_Vertex(p);
				vrtx.setLabel(strutNodeConnections[id].node.getIndex());
				strutNodeConnections[id].vertices.add(vrtx);
				mesh.add(vrtx);
			}
		}
	}

	/**
	 * Gets the strut index.
	 *
	 * @param node the node
	 * @param strut the strut
	 * @return the strut index
	 */
	private int getStrutIndex(final WB_FrameNode node, final WB_FrameStrut strut) {
		if (node == strut.start()) {
			return 2 * strut.getIndex();
		} else {
			return 2 * strut.getIndex() + 1;
		}
	}

	/**
	 * Creates the struts.
	 */
	private void createStruts() {
		int i = 0;
		for (final WB_FrameStrut strut : frame.getStruts()) {
			/*
			 * System.out.println("HEC_FromFrame: Creating strut " + (i + 1) +
			 * " of " + frame.getNumberOfStruts() + ".");
			 */
			final int offsets = i * 2;
			final int offsete = i * 2 + 1;

			int ns = (int) Math.round(strut.getLength() / maximumStrutLength);
			ns = Math.max(ns, 1);
			final ArrayList<HE_Halfedge> hes = new ArrayList<HE_Halfedge>();
			final HE_Vertex[][] extraVertices = new HE_Vertex[strutFacets][ns - 1];
			for (int j = 0; j < strutFacets; j++) {
				for (int k = 0; k < ns - 1; k++) {
					extraVertices[j][k] = new HE_Vertex(WB_Point3d.interpolate(
							strutNodeConnections[offsets].vertices.get(j),
							strutNodeConnections[offsete].vertices.get(j),
							(k + 1) / (double) ns));
					mesh.add(extraVertices[j][k]);
				}
			}

			for (int j = 0; j < strutFacets; j++) {
				final int jp = (j + 1) % strutFacets;

				final HE_Vertex s0 = strutNodeConnections[offsets].vertices
						.get(j);
				final HE_Vertex s1 = strutNodeConnections[offsets].vertices
						.get(jp);
				final HE_Vertex e2 = strutNodeConnections[offsete].vertices
						.get(jp);
				final HE_Vertex e3 = strutNodeConnections[offsete].vertices
						.get(j);
				for (int k = 0; k < ns; k++) {
					final HE_Face f = new HE_Face();
					final HE_Halfedge he0 = new HE_Halfedge();
					final HE_Halfedge he1 = new HE_Halfedge();
					final HE_Halfedge he2 = new HE_Halfedge();
					final HE_Halfedge he3 = new HE_Halfedge();
					he0.setVertex((k == 0) ? s0 : extraVertices[j][k - 1]);
					he1.setVertex((k == 0) ? s1 : extraVertices[jp][k - 1]);
					he2.setVertex((k == ns - 1) ? e2 : extraVertices[jp][k]);
					he3.setVertex((k == ns - 1) ? e3 : extraVertices[j][k]);
					he0.setNext(he1);
					he1.setNext(he2);
					he2.setNext(he3);
					he3.setNext(he0);
					he0.setFace(f);
					f.setHalfedge(he0);
					f.setLabel(1);
					he1.setFace(f);
					he2.setFace(f);
					he3.setFace(f);
					he0.getVertex().setHalfedge(he0);
					he1.getVertex().setHalfedge(he1);
					he2.getVertex().setHalfedge(he2);
					he3.getVertex().setHalfedge(he3);
					mesh.add(f);
					mesh.add(he0);
					mesh.add(he1);
					mesh.add(he2);
					mesh.add(he3);
					hes.add(he1);
					hes.add(he3);
					if ((k < ns - 1) && (k > 0)) {
						hes.add(he0);
						hes.add(he2);
					}
				}
			}
			i++;

		}
	}

	/**
	 * Creates the nodes.
	 */
	private void createNodes() {
		int i = 0;
		for (WB_FrameNode node : frame.getNodes()) {
			/*
			 * System.out.println("HEC_FromFrame: Creating node " + (i + 1) +
			 * " of " + frame.getNumberOfNodes() + ".");
			 */
			node = frame.getNode(i);
			final ArrayList<WB_FrameStrut> struts = node.getStruts();
			final ArrayList<HE_Vertex> hullPoints = new ArrayList<HE_Vertex>();
			if (nodeTypes[i] == NodeType.ENDPOINT) {
				if (cap) {
					int offset;
					if (node == struts.get(0).start()) {
						offset = struts.get(0).getIndex() * 2;
					} else {
						offset = struts.get(0).getIndex() * 2 + 1;
					}
					final ArrayList<HE_Halfedge> hes = new ArrayList<HE_Halfedge>(
							strutFacets);
					final HE_Face f = new HE_Face();
					mesh.add(f);
					for (int k = 0; k < strutFacets; k++) {
						final HE_Halfedge he = new HE_Halfedge();
						he.setVertex(strutNodeConnections[offset].vertices
								.get(k));
						he.setFace(f);
						hes.add(he);
						mesh.add(he);
					}
					f.setHalfedge(hes.get(0));
					f.setLabel(3);
					if (node == struts.get(0).start()) {
						for (int k = 0, j = strutFacets - 1; k < strutFacets; j = k, k++) {
							hes.get(k).setNext(hes.get(j));
						}
					} else {
						for (int k = 0, j = strutFacets - 1; k < strutFacets; j = k, k++) {
							hes.get(j).setNext(hes.get(k));
						}

					}
				}
			} else {
				if ((nodeTypes[i] != NodeType.ISOLATED) || createIsolatedNodes) {
					double br = strutRadius.value(node.x, node.y, node.z);
					for (int j = 0; j < struts.size(); j++) {
						int offset;
						if (node == struts.get(j).start()) {
							offset = struts.get(j).getIndex() * 2;
						} else {
							offset = struts.get(j).getIndex() * 2 + 1;
						}
						for (int k = 0; k < strutFacets; k++) {
							hullPoints
									.add(strutNodeConnections[offset].vertices
											.get(k));
							br = Math.min(br,
									strutNodeConnections[offset].radius);
						}

					}
					br *= fillfactor;
					final int n = hullPoints.size();
					if ((nodeTypes[i] != NodeType.STRAIGHT)
							&& (nodeTypes[i] != NodeType.BEND)
							&& (!suppressBalljoint)) {

						final HE_Mesh ball = new HE_Mesh(new HEC_Sphere()
								.setRadius(br).setUFacets(strutFacets)
								.setVFacets(strutFacets).setCenter(node));
						hullPoints.addAll(ball.getVerticesAsList());
					}
					final HEC_ConvexHull ch = new HEC_ConvexHull()
							.setPointsFromVertices(hullPoints).setUseQuickHull(
									true);
					final HE_Mesh tmp = new HE_Mesh(ch);
					final FastMap<Integer, Integer> vertexToPointIndex = ch.vertexToPointIndex;
					tmp.cleanUnusedElementsByFace();
					final Iterator<HE_Face> tmpfItr = tmp.fItr();

					HE_Face f;
					HE_Halfedge tmphe;
					final ArrayList<HE_Face> facesToRemove = new ArrayList<HE_Face>();
					while (tmpfItr.hasNext()) {
						f = tmpfItr.next();
						f.setLabel(2);
						tmphe = f.getHalfedge();
						int initid = vertexToPointIndex.get(tmphe.getVertex()
								.key());

						boolean endface = (initid < n);
						initid = initid / strutFacets;
						do {
							final int id = vertexToPointIndex.get(tmphe
									.getVertex().key());
							endface = (id / strutFacets == initid) && (id < n);
							if (!endface) {
								break;
							}
							tmphe = tmphe.getNextInFace();
						} while (tmphe != f.getHalfedge());
						if (endface) {
							facesToRemove.add(f);
						}
					}

					for (int j = 0; j < facesToRemove.size(); j++) {
						tmp.deleteFace(facesToRemove.get(j));
					}

					tmp.cleanUnusedElementsByFace();
					tmp.uncapHalfedges();

					for (int j = 0; j < struts.size(); j++) {
						int offset;
						if (node == struts.get(j).start()) {
							offset = struts.get(j).getIndex() * 2;
						} else {
							offset = struts.get(j).getIndex() * 2 + 1;
						}
						final WB_Vector3d v = strutNodeConnections[offset].dir;
						v.mult(strutNodeConnections[offset].offset
								- strutNodeConnections[offset].maxoffset);
						for (int k = 0; k < strutFacets; k++) {
							strutNodeConnections[offset].vertices.get(k).add(v);
						}
					}

					final Iterator<HE_Halfedge> tmpheItr = tmp.heItr();
					HE_Vertex tmpv;
					while (tmpheItr.hasNext()) {
						tmphe = tmpheItr.next();
						tmpv = tmphe.getVertex();
						final int j = vertexToPointIndex.get(tmpv.key());
						tmphe.setVertex(hullPoints.get(j));
						if (j >= n) {
							hullPoints.get(j).setHalfedge(tmphe);
						}
					}
					tmp.cleanUnusedElementsByFace();
					mesh.add(tmp);
				}

			}
			i++;
		}

	}

	/*
	 * (non-Javadoc)
	 * @see wblut.hemesh.creators.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		mesh = new HE_Mesh();
		numberOfNodes = frame.getNumberOfNodes();
		// System.out.println(numberOfNodes + " " + frame.getNumberOfStruts());
		nodeTypes = new NodeType[numberOfNodes];
		getNodeTypes();
		numberOfStruts = frame.getNumberOfStruts();
		strutNodeConnections = new StrutNodeConnection[2 * numberOfStruts];
		getStrutNodeConnections();
		createVertices();
		createNodes();
		createStruts();
		mesh.pairHalfedges();
		if (!cap) {
			mesh.capHalfedges();
		}
		return mesh;
	}

	/**
	 * Set frame.
	 *
	 * @param frame the frame
	 * @return self
	 */
	public HEC_FromFrame setFrame(final WB_Frame frame) {
		this.frame = frame;
		return this;
	}

	/**
	 * Set frame.
	 *
	 * @param mesh the mesh
	 * @return self
	 */
	public HEC_FromFrame setFrame(final HE_Mesh mesh) {
		frame = mesh.getFrame();
		return this;
	}

}
