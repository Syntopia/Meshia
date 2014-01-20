/**
 * 
 */
package wblut.core;

// TODO: Auto-generated Javadoc
/**
 * The Class Flag.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class Flag {
	
	/** The _flags. */
	private int	_flags;

	/**
	 * Instantiates a new flag.
	 */
	public Flag() {

	}

	/**
	 * Sets the flag.
	 *
	 * @param i the i
	 * @param flag the flag
	 */
	public void setFlag(final int i, final boolean flag) {
		int mask = 1 << (i - 1);
		if (flag) {
			_flags |= mask;
		} else {
			mask = ~mask;
			_flags &= mask;
		}

	}

	/**
	 * Gets the flag.
	 *
	 * @param i the i
	 * @return the flag
	 */
	public boolean getFlag(final int i) {
		final int mask = 1 << (i - 1);
		return ((_flags & mask) >> (i - 1)) == 1;
	}
}
