/**
 * 
 */
package wblut.hemesh;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import wblut.geom.WB_ExplicitTriangle;
import wblut.geom.WB_Point3d;

// TODO: Auto-generated Javadoc
/**
 * The Class HEC_FromObjFile.
 *
 * @author Corneel Cannaerts, Intorspekto, 2012
 */

public class HEC_FromObjFile extends HEC_Creator {
	
	/** The path. */
	private String path;
	
	/** The scale. */
	private double scale;

	/**
	 * Instantiates a new hE c_ from obj file.
	 */
	public HEC_FromObjFile() {
		super();
		scale = 1;
		path = null;
		override = true;
	}

	/**
	 * Instantiates a new hE c_ from obj file.
	 *
	 * @param path the path
	 */
	public HEC_FromObjFile(final String path) {
		super();
		this.path = path;
		scale = 1;
		override = true;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the path
	 * @return the hE c_ from obj file
	 */
	public HEC_FromObjFile setPath(final String path) {
		this.path = path;
		return this;
	}

	/**
	 * Sets the scale.
	 *
	 * @param f the f
	 * @return the hE c_ from obj file
	 */
	public HEC_FromObjFile setScale(final double f) {
		scale = f;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wblut.hemesh.creators.HEC_Creator#createBase()
	 */
	@Override
	protected HE_Mesh createBase() {
		if (path == null)
			return new HE_Mesh();
		final File file = new File(path);
		InputStream is = createInputStream(file);
		if (is == null)
			return new HE_Mesh();
		final ArrayList<WB_ExplicitTriangle> triangles = new ArrayList<WB_ExplicitTriangle>();
		ArrayList<WB_Point3d> vertexList = new ArrayList();
		ArrayList<int[]> faceList = new ArrayList();
		int faceCount = 0;
	
		// load OBJ file as an array of strings
		String objStrings[] = loadStrings(is);
		for (int i = 0; i < objStrings.length; i++) {
	
			// split every line in parts divided by spaces
			String[] parts = objStrings[i].split("\\s+");
	
			// the first part indicates the kind of data that is in that line
			// v stands for vertex data
			if (parts[0].equals("v")) {
				double x1 =scale* Double.parseDouble(parts[1]);
				double y1 =scale* Double.parseDouble(parts[2]);
				double z1 = scale*Double.parseDouble(parts[3]);
				WB_Point3d pointLoc = new WB_Point3d(x1, y1, z1);
				vertexList.add(pointLoc);
			}
			// f stands for facelist data
			// should work for non triangular faces
			if (parts[0].equals("f")) {
				int[] tempFace = new int[parts.length - 1];
				for (int j = 0; j < parts.length - 1; j++) {
					String[] num = parts[j + 1].split("/");
					tempFace[j] = Integer.parseInt(num[0]) - 1;
				}
				faceList.add(tempFace);
				faceCount++;
			}
		}
	
		// the HEC_FromFacelist wants the face data as int[][]
		int[][] faceArray = new int[faceCount][];
		for (int i = 0; i < faceCount; i++) {
			int[] tempFace = faceList.get(i);
			faceArray[i] = tempFace;
		}
		// et voila... add to the creator
		HEC_FromFacelist creator = new HEC_FromFacelist();
		creator.setVertices(vertexList);
		creator.setFaces(faceArray);
		creator.setDuplicate(true);
		return new HE_Mesh(creator);
	
	}
	
	//Code excerpts form processing.core

	/**
	 * Creates the input stream.
	 *
	 * @param file the file
	 * @return the input stream
	 */
	private InputStream createInputStream(final File file) {
		if (file == null) {
			throw new IllegalArgumentException("file can't be null");
		}
		try {
			InputStream stream = new FileInputStream(file);
			if (file.getName().toLowerCase().endsWith(".gz")) {
				stream = new GZIPInputStream(stream);
			}
			return stream;
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Load strings.
	 *
	 * @param input the input
	 * @return the string[]
	 */
	private String[] loadStrings(InputStream input) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input, "UTF-8"));
			return loadStrings(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Load strings.
	 *
	 * @param reader the reader
	 * @return the string[]
	 */
	private String[] loadStrings(BufferedReader reader) {
		try {
			String lines[] = new String[100];
			int lineCount = 0;
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (lineCount == lines.length) {
					String temp[] = new String[lineCount << 1];
					System.arraycopy(lines, 0, temp, 0, lineCount);
					lines = temp;
				}
				lines[lineCount++] = line;
			}
			reader.close();

			if (lineCount == lines.length) {
				return lines;
			}
			String output[] = new String[lineCount];
			System.arraycopy(lines, 0, output, 0, lineCount);
			return output;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	

}
