/**
 * 
 */
package wblut.geom;

import java.util.ArrayList;

import wblut.hemesh.HE_Face;



// TODO: Auto-generated Javadoc
/**
 * The Class WB_AABBNode.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_AABBNode {
	
	/** The level. */
	protected int					level;
	
	/** The aabb. */
	protected WB_AABB3D				aabb;
	
	/** The positive. */
	protected WB_AABBNode			positive;
	
	/** The negative. */
	protected WB_AABBNode			negative;
	
	/** The mid. */
	protected WB_AABBNode			mid;
	
	/** The separator. */
	protected WB_Plane				separator;
	
	/** The faces. */
	protected ArrayList<HE_Face>	faces;
	
	/** The is leaf. */
	protected boolean				isLeaf;

	/**
	 * Instantiates a new w b_ aabb node.
	 */
	public WB_AABBNode() {
		level = -1;
		faces = new ArrayList<HE_Face>();
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
	 * Gets the separator.
	 *
	 * @return the separator
	 */
	public WB_Plane getSeparator() {
		return separator;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Checks if is leaf.
	 *
	 * @return true, if is leaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * Gets the faces.
	 *
	 * @return the faces
	 */
	public ArrayList<HE_Face> getFaces() {
		return faces;
	}

	/**
	 * Gets the pos child.
	 *
	 * @return the pos child
	 */
	public WB_AABBNode getPosChild() {
		return positive;

	}

	/**
	 * Gets the neg child.
	 *
	 * @return the neg child
	 */
	public WB_AABBNode getNegChild() {
		return negative;

	}

	/**
	 * Gets the mid child.
	 *
	 * @return the mid child
	 */
	public WB_AABBNode getMidChild() {
		return mid;

	}

}
