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
 * Random generator for vectors uniformly distributed on the unit sphere.
 * 
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_RandomSphere {
	
	/** The random gen. */
	private final WB_MTRandom	randomGen;

	/**
	 * Instantiates a new w b_ random sphere.
	 */
	public WB_RandomSphere() {
		randomGen = new WB_MTRandom();
	}

	/**
	 * Set random seed.
	 *
	 * @param seed seed
	 * @return self
	 */
	public WB_RandomSphere setSeed(final long seed) {
		randomGen.setSeed(seed);
		return this;
	}

	/**
	 * Next point.
	 *
	 * @return next random WB_Normal on unit sphere
	 */
	public WB_Point3d nextPoint() {
		final double eps = randomGen.nextDouble();
		final double z = 1.0 - 2.0 * eps;
		final double r = Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Point3d(r * Math.cos(t), r * Math.sin(t), z);
	}

	/**
	 * Next vector.
	 *
	 * @return next random WB_Normal on unit sphere
	 */
	public WB_Vector3d nextVector() {
		final double eps = randomGen.nextDouble();
		final double z = 1.0 - 2.0 * eps;
		final double r = Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Vector3d(r * Math.cos(t), r * Math.sin(t), z);
	}

	/**
	 * Next normal.
	 *
	 * @return next random WB_Normal on unit sphere
	 */
	public WB_Normal3d nextNormal() {
		final double eps = randomGen.nextDouble();
		final double z = 1.0 - 2.0 * eps;
		final double r = Math.sqrt(1.0 - z * z);
		final double t = 2 * Math.PI * randomGen.nextDouble();
		return new WB_Normal3d(r * Math.cos(t), r * Math.sin(t), z);
	}

}
