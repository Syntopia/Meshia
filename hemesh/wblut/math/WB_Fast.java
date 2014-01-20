/**
 * 
 */
package wblut.math;

// TODO: Auto-generated Javadoc
/**
 * A collection of fast and approximated math functions. Not as robust
 * as the JAVA implementations when dealing with special values (NaN etc). 
 * Some functions only give an approximation.
 * 
 * @author Frederik Vanhoutte, W:Blut
 *
 */
public class WB_Fast {
	
	/**
	 * Fast absolute value.
	 *
	 * @param x the x
	 * @return abs(x)
	 */
	public static double abs(final double x) {
		return (x > 0) ? x : -x;
	}

	/**
	 * Fast max.
	 *
	 * @param x the x
	 * @param y the y
	 * @return max(x,y)
	 */
	public static double max(final double x, final double y) {
		return (y > x) ? y : x;
	}

	/**
	 * Fast min.
	 *
	 * @param x the x
	 * @param y the y
	 * @return min(x,y)
	 */
	public static double min(final double x, final double y) {
		return (y < x) ? y : x;
	}

	/**
	 * Fast max.
	 *
	 * @param x the x
	 * @param y the y
	 * @return max(x,y)
	 */
	public static int max(final int x, final int y) {
		return (y > x) ? y : x;
	}

	/**
	 * Fast min.
	 *
	 * @param x the x
	 * @param y the y
	 * @return min(x,y)
	 */
	public static int min(final int x, final int y) {
		return (y < x) ? y : x;
	}

	/**
	 * Clamp value to range.
	 *
	 * @param x the x
	 * @param min the min
	 * @param max the max
	 * @return clamped value
	 */
	public static double clamp(final double x, final double min,
			final double max) {
		if (x < min) {
			return min;
		}
		if (x > max) {
			return max;
		}
		return x;
	}

	/**
	 * Maximum of three values.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return max(x,y,z)
	 */
	public static double max(final double x, final double y, final double z) {
		return (y > x) ? ((z > y) ? z : y) : ((z > x) ? z : x);
	}

	/**
	 * Minimum of three values.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return min(x,y,z)
	 */
	public static double min(final double x, final double y, final double z) {
		return (y < x) ? ((z < y) ? z : y) : ((z < x) ? z : x);
	}

	/**
	 * Largest integer smaller than value.
	 *
	 * @param x the x
	 * @return result
	 */
	public static final int floor(final float x) {
		return (x >= 0) ? (int) x : (int) x - 1;
	}

	/**
	 * Fast log2 approximation for floats.
	 *
	 * @param i the i
	 * @return result
	 */
	public static final float log2(final float i) {
		float x = Float.floatToRawIntBits(i);
		x *= 1.0f / (1 << 23);
		x -= 127;
		float y = x - floor(x);
		y = (y - y * y) * 0.346607f;
		return x + y;
	}

	/**
	 * Fast square power approximation for floats.
	 *
	 * @param i the i
	 * @return result
	 */
	public static final float pow2(final float i) {
		float x = i - floor(i);
		x = (x - x * x) * 0.33971f;
		return Float.intBitsToFloat((int) ((i + 127 - x) * (1 << 23)));
	}

	/**
	 * Fast power approximation for floats.
	 *
	 * @param a the a
	 * @param b exponent
	 * @return result
	 */
	public static final float pow(final float a, final float b) {
		return pow2(b * log2(a));
	}

	/**
	 * Fast inverse sqrt approximation for floats.
	 *
	 * @param x the x
	 * @return result
	 */
	public static final float inverseSqrt(float x) {
		final float half = 0.5F * x;
		int i = Float.floatToIntBits(x);
		i = 0x5f375a86 - (i >> 1);
		x = Float.intBitsToFloat(i);
		return x * (1.5F - half * x * x);
	}

	/**
	 * Fast sqrt approximation for floats.
	 *
	 * @param x the x
	 * @return result
	 */
	public static final float sqrt(final float x) {
		return 1f / inverseSqrt(x);
	}

}
