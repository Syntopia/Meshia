/**
 * 
 */
package wblut.hemesh;

// TODO: Auto-generated Javadoc
/**
 * The Interface HE_Machine.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public interface HE_Machine {
	
	/**
	 * Apply.
	 *
	 * @param mesh the mesh
	 * @return the h e_ mesh
	 */
	public HE_Mesh apply(HE_Mesh mesh);

	/**
	 * Apply.
	 *
	 * @param selection the selection
	 * @return the h e_ mesh
	 */
	public HE_Mesh apply(HE_Selection selection);

}
