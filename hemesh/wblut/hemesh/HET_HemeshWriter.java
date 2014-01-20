package wblut.hemesh;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;


// TODO: Auto-generated Javadoc
/**
 * Helper class for HE_Export.saveToHemesh.
 * 
 * @author Frederik Vanhoutte, W:Blut
 *
 */

public class HET_HemeshWriter {

	/** The hemesh stream. */
	protected OutputStream	hemeshStream;
	
	/** The hemesh writer. */
	protected PrintWriter	hemeshWriter;

	/**
	 * Begin save.
	 *
	 * @param stream the stream
	 */
	public void beginSave(final OutputStream stream) {
		try {
			hemeshStream = stream;
			handleBeginSave();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Begin save.
	 *
	 * @param fn the fn
	 */
	public void beginSave(final String fn) {
		try {
			hemeshStream = new FileOutputStream(fn);
			handleBeginSave();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * End save.
	 */
	public void endSave() {
		try {
			hemeshWriter.flush();
			hemeshWriter.close();
			hemeshStream.flush();
			hemeshStream.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle begin save.
	 */
	protected void handleBeginSave() {
		hemeshWriter = new PrintWriter(hemeshStream);
	}

	/**
	 * Vertex.
	 *
	 * @param v the v
	 * @param heid the heid
	 */
	public void vertex(final HE_Vertex v, final int heid) {
		hemeshWriter.println(v.x + " " + v.y + " " + v.z + " " + heid);
	}

	/**
	 * Halfedge.
	 *
	 * @param vid the vid
	 * @param henextid the henextid
	 * @param hepairid the hepairid
	 * @param edgeid the edgeid
	 * @param faceid the faceid
	 */
	public void halfedge(final int vid, final int henextid, final int hepairid,
			final int edgeid, final int faceid) {
		hemeshWriter.println(vid + " " + henextid + " " + hepairid + " "
				+ edgeid + " " + faceid);
	}

	/**
	 * Edge.
	 *
	 * @param heid the heid
	 */
	public void edge(final int heid) {
		hemeshWriter.println(heid);
	}

	/**
	 * Face.
	 *
	 * @param heid the heid
	 */
	public void face(final int heid) {
		hemeshWriter.println(heid);
	}

	/**
	 * Sizes.
	 *
	 * @param v1 the v1
	 * @param v2 the v2
	 * @param v3 the v3
	 * @param v4 the v4
	 */
	public void sizes(final int v1, final int v2, final int v3, final int v4) {
		hemeshWriter.println(v1 + " " + v2 + " " + v3 + " " + v4);
	}
}
