/**
 * 
 */
package wblut.geom;

import javolution.util.FastList;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_BSPNode2D.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_BSPNode2D {
	
	/** The partition. */
	protected WB_Line2D							partition;
	
	/** The segments. */
	protected FastList<WB_ExplicitSegment2D>	segments;
	
	/** The pos. */
	protected WB_BSPNode2D						pos	= null;
	
	/** The neg. */
	protected WB_BSPNode2D						neg	= null;

	/**
	 * Instantiates a new w b_ bsp node2 d.
	 */
	public WB_BSPNode2D() {
		segments = new FastList<WB_ExplicitSegment2D>();
	}

}