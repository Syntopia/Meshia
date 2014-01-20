/**
 * 
 */
package wblut.math;

import wblut.geom.WB_Normal3d;
import wblut.geom.WB_Point3d;
import wblut.geom.WB_Vector3d;

// TODO: Auto-generated Javadoc
/**
 * 
 * Random generator for vectors uniformly distributed in the unit cube.
 * 
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomBox {
	
	/** The random gen. */
	private final WB_MTRandom	randomGen;

	/**
	 * Instantiates a new w b_ random box.
	 */
	public WB_RandomBox() {
		randomGen = new WB_MTRandom();
	}

	/**
	 * Set random seed.
	 *
	 * @param seed seed
	 * @return self
	 */
	public WB_RandomBox setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 * Next point.
	 *
	 * @return the w b_ point3d
	 */
	public WB_Point3d nextPoint() {
		return new WB_Point3d(randomGen.nextDouble(), randomGen.nextDouble(),
				randomGen.nextDouble());
	}

	/**
	 * Next vector.
	 *
	 * @return the w b_ vector3d
	 */
	public WB_Vector3d nextVector() {
		return new WB_Vector3d(randomGen.nextDouble(), randomGen.nextDouble(),
				randomGen.nextDouble());
	}

	/**
	 * Next normal.
	 *
	 * @return the w b_ normal3d
	 */
	public WB_Normal3d nextNormal() {
		return new WB_Normal3d(randomGen.nextDouble(), randomGen.nextDouble(),
				randomGen.nextDouble());
	}

}
