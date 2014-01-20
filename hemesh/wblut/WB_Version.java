package wblut;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_Version.
 */
public class WB_Version {

	/** The Constant CURRENT_VERSION. */
	public static final WB_Version CURRENT_VERSION = new WB_Version();

	/** The Constant MAJOR. */
	public static final int MAJOR = 2;

	/** The Constant MINOR. */
	public static final int MINOR = 0;

	/** The Constant PATCH. */
	public static final int PATCH = 0;

	/** The Constant releaseInfo. */
	private static final String releaseInfo = "Alexander";

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		System.out.println(CURRENT_VERSION);
	}

	/**
	 * Instantiates a new w b_ version.
	 */
	private WB_Version() {
	}

	/**
	 * Gets the major.
	 * 
	 * @return the major
	 */
	public int getMajor() {
		return MAJOR;
	}

	/**
	 * Gets the minor.
	 * 
	 * @return the minor
	 */
	public int getMinor() {
		return MINOR;
	}

	/**
	 * Gets the patch.
	 * 
	 * @return the patch
	 */
	public int getPatch() {
		return PATCH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String ver = "W:Blut HE_Mesh " + MAJOR + "." + MINOR + "." + PATCH;
		if (releaseInfo != null && releaseInfo.length() > 0)
			return ver + " " + releaseInfo;
		return ver;
	}
}