/**
 * 
 */
package wblut.geom;

import java.util.Map.Entry;
import java.util.Set;

import javolution.util.FastMap;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_HashGrid.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_HashGrid {
	
	/** The values. */
	private final FastMap<Integer, Double>	values;
	
	/** The default value. */
	private final double					defaultValue;
	
	/** The kl. */
	private final int						K, L, M, KL;

	/**
	 * Instantiates a new w b_ hash grid.
	 *
	 * @param K the k
	 * @param L the l
	 * @param M the m
	 * @param defaultValue the default value
	 */
	public WB_HashGrid(final int K, final int L, final int M,
			final double defaultValue) {
		this.K = K;
		this.L = L;
		this.M = M;
		KL = K * L;
		values = new FastMap<Integer, Double>();
		this.defaultValue = defaultValue;
	}

	/**
	 * Instantiates a new w b_ hash grid.
	 *
	 * @param K the k
	 * @param L the l
	 * @param M the m
	 */
	public WB_HashGrid(final int K, final int L, final int M) {
		this.K = K;
		this.L = L;
		this.M = M;
		KL = K * L;
		values = new FastMap<Integer, Double>();
		defaultValue = -10000000;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the value
	 * @param i the i
	 * @param j the j
	 * @param k the k
	 * @return true, if successful
	 */
	public boolean setValue(final double value, final int i, final int j,
			final int k) {
		final int id = safeIndex(i, j, k);
		if (id > 0) {
			values.put(id, value);
			return true;
		}
		return false;
	}

	/**
	 * Adds the value.
	 *
	 * @param value the value
	 * @param i the i
	 * @param j the j
	 * @param k the k
	 * @return true, if successful
	 */
	public boolean addValue(final double value, final int i, final int j,
			final int k) {
		final int id = safeIndex(i, j, k);
		if (id > 0) {
			final Double v = values.get(id);
			if (v == null) {
				values.put(id, value);
			} else {
				values.put(id, v + value);
			}
			return true;
		}
		return false;
	}

	/**
	 * Clear value.
	 *
	 * @param i the i
	 * @param j the j
	 * @param k the k
	 * @return true, if successful
	 */
	public boolean clearValue(final int i, final int j, final int k) {
		final int id = safeIndex(i, j, k);
		if (id > 0) {
			values.remove(id);
			return true;
		}
		return false;
	}

	/**
	 * Gets the value.
	 *
	 * @param i the i
	 * @param j the j
	 * @param k the k
	 * @return the value
	 */
	public double getValue(final int i, final int j, final int k) {
		final int id = safeIndex(i, j, k);
		if (id == -1) {
			return defaultValue;
		}
		if (id > 0) {
			final Double val = values.get(id);
			if (val != null) {
				return val.doubleValue();
			}

		}
		return defaultValue;

	}

	/**
	 * Safe index.
	 *
	 * @param i the i
	 * @param j the j
	 * @param k the k
	 * @return the int
	 */
	private int safeIndex(final int i, final int j, final int k) {
		if (i < 0) {
			return -1;
		}
		if (i > K - 1) {
			return -1;
		}
		if (j < 0) {
			return -1;
		}
		if (j > L - 1) {
			return -1;
		}

		if (k < 0) {
			return -1;
		}
		if (k > M - 1) {
			return -1;
		}
		return i + j * K + k * KL;
	}

	/**
	 * Gets the w.
	 *
	 * @return the w
	 */
	public int getW() {
		return K;
	}

	/**
	 * Gets the h.
	 *
	 * @return the h
	 */
	public int getH() {
		return L;
	}

	/**
	 * Gets the d.
	 *
	 * @return the d
	 */
	public int getD() {
		return M;
	}

	/**
	 * Gets the default value.
	 *
	 * @return the default value
	 */
	public double getDefaultValue() {

		return defaultValue;
	}

	/**
	 * Gets the values.
	 *
	 * @return the values
	 */
	public Set<Entry<Integer, Double>> getValues() {
		return values.entrySet();
	}

}
