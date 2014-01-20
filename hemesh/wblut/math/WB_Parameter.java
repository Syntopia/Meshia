/**
 * 
 */
package wblut.math;


// TODO: Auto-generated Javadoc
/**
 * Generic interface for parameter classes.
 *
 * @param <T> the generic type
 * @author Frederik Vanhoutte, W:Blut
 */
public interface WB_Parameter<T> {
	
	/**
	 * Value.
	 *
	 * @return default value
	 */
	public T value();

	/**
	 * Value.
	 *
	 * @param x the x
	 * @return value dependent on 1 variable
	 */
	public T value(double x);

	/**
	 * Value.
	 *
	 * @param x the x
	 * @param y the y
	 * @return value dependent on 2 variables
	 */
	public T value(double x, double y);

	/**
	 * Value.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return value dependent on 3 variables
	 */
	public T value(double x, double y, double z);

	/**
	 * Value.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param t the t
	 * @return value dependent on 4 variables
	 */
	public T value(double x, double y, double z, double t);

}
