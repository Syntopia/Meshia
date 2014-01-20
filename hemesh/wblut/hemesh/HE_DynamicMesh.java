/**
 * 
 */
package wblut.hemesh;

import java.util.ArrayList;


// TODO: Auto-generated Javadoc
/**
 * The Class HE_DynamicMesh.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class HE_DynamicMesh extends HE_Mesh {
	
	/** The modifier stack. */
	private final ArrayList<HE_Machine>	modifierStack;
	
	/** The bkp. */
	private HE_Mesh							bkp;

	/**
	 * Instantiates a new h e_ dynamic mesh.
	 *
	 * @param baseMesh the base mesh
	 */
	public HE_DynamicMesh(final HE_Mesh baseMesh) {
		this.set(baseMesh);
		bkp = get();
		modifierStack = new ArrayList<HE_Machine>();
	}

	/**
	 * Update.
	 */
	public void update() {
		this.set(bkp);
		applyStack();
	}

	/**
	 * Apply stack.
	 */
	private void applyStack() {
		for (int i = 0; i < modifierStack.size(); i++) {
			modifierStack.get(i).apply(this);
		}

	}

	/**
	 * Adds the.
	 *
	 * @param mod the mod
	 */
	public void add(final HE_Machine mod) {
		modifierStack.add(mod);
	}

	/**
	 * Removes the.
	 *
	 * @param mod the mod
	 */
	public void remove(final HE_Machine mod) {
		modifierStack.remove(mod);
	}

	/* (non-Javadoc)
	 * @see wblut.hemesh.HE_MeshStructure#clear()
	 */
	@Override
	public void clear() {
		modifierStack.clear();
		set(bkp);
	}

	/**
	 * Sets the base mesh.
	 *
	 * @param baseMesh the base mesh
	 * @return the h e_ dynamic mesh
	 */
	public HE_DynamicMesh setBaseMesh(final HE_Mesh baseMesh) {
		set(baseMesh);
		bkp = get();
		applyStack();
		return this;
	}

}
