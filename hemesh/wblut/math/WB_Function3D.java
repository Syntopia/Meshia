/**
 * 
 */
package wblut.math;

// TODO: Auto-generated Javadoc
/**
 * Interface for a function of 3 variables.
 *
 * @param <T> the generic type
 * @author Frederik Vanhoutte, W:Blut
 */
public interface WB_Function3D<T> {
	
	/**
	 * F.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return result
	 */
	public T f(double x, double y, double z);
}
