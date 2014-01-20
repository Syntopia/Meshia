/**
 * 
 */
package wblut.geom;



import wblut.math.WB_DoubleDouble;

// TODO: Auto-generated Javadoc
/**
 * The Class WB_Predicates.
 *
 * @author Frederik Vanhoutte, W:Blut
 */
public class WB_Predicates {
	
	/** The orient error bound. */
	private static double orientErrorBound = -1;
	
	/** The insphere error bound. */
	private static double insphereErrorBound = -1;

	/**
	 * Find mach epsilon.
	 *
	 * @return the double
	 */
	private static double findMachEpsilon() {
		double epsilon, check, lastcheck;
		epsilon = 1.0;
		check = 1.0;
		do {
			lastcheck = check;
			epsilon *= 0.5;
			check = 1.0 + epsilon;
		} while ((check != 1.0) && (check != lastcheck));
		return epsilon;
	}

	/**
	 * Inits the.
	 */
	private static void init() {
		final double epsilon = findMachEpsilon();

		orientErrorBound = (7.0 + 56.0 * epsilon) * epsilon;
		insphereErrorBound = (16.0 + 224.0 * epsilon) * epsilon;
	}

	// >0 if pd below plane defined by pa,pb,pc
	// <0 if above (pa,pb,pc are ccw viewed from above)
	// = 0 if on plane
	/**
	 * Orient.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @return the double
	 */
	public static double orient(final WB_Point3d pa, final WB_Point3d pb,
			final WB_Point3d pc, final WB_Point3d pd) {
		if (orientErrorBound == -1) {
			init();
		}

		final double adx = pa.x - pd.x, bdx = pb.x - pd.x, cdx = pc.x
				- pd.x;
		final double ady = pa.y - pd.y, bdy = pb.y - pd.y, cdy = pc.y
				- pd.y;
		double adz = pa.z - pb.z, bdz = pb.z - pd.x, cdz = pc.z
				- pd.z;

		double adxbdy = adx * bdy;
		double adybdx = ady * bdx;
		double adxcdy = adx * cdy;
		double adycdx = ady * cdx;
		double bdxcdy = bdx * cdy;
		double bdycdx = bdy * cdx;

		final double m1 = adxbdy - adybdx;
		final double m2 = adxcdy - adycdx;
		final double m3 = bdxcdy - bdycdx;
		final double det = m1 * cdz - m2 * bdz + m3 * adz;

		if (adxbdy < 0) {
			adxbdy = -adxbdy;
		}
		if (adybdx < 0) {
			adybdx = -adybdx;
		}
		if (adxcdy < 0) {
			adxcdy = -adxcdy;
		}
		if (adycdx < 0) {
			adycdx = -adycdx;
		}
		if (bdxcdy < 0) {
			bdxcdy = -bdxcdy;
		}
		if (bdycdx < 0) {
			bdycdx = -bdycdx;
		}
		if (adz < 0) {
			adz = -adz;
		}
		if (bdz < 0) {
			bdz = -bdz;
		}
		if (cdz < 0) {
			cdz = -cdz;
		}

		double errbound = (adxbdy + adybdx) * cdz + (adxcdy + adycdx) * bdz
				+ (bdxcdy + bdycdx) * adz;
		errbound *= orientErrorBound;

		if (det >= errbound) {
			return (det > 0) ? 1 : -1;

		} else if (-det >= errbound) {
			return (det > 0) ? 1 : -1;
		} else {
			return orientExact(pa, pb, pc, pd);
		}

	}

	/**
	 * Orient exact.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @return the double
	 */
	public static double orientExact(final WB_Point3d pa, final WB_Point3d pb,
			final WB_Point3d pc, final WB_Point3d pd) {
		WB_DoubleDouble ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz;
		WB_DoubleDouble adx, bdx, cdx, ady, bdy, cdy, adz, bdz, cdz;
		WB_DoubleDouble m1, m2, m3;
		WB_DoubleDouble det;

		det = WB_DoubleDouble.ZERO;

		ax = new WB_DoubleDouble(pa.x);
		ay = new WB_DoubleDouble(pa.y);
		az = new WB_DoubleDouble(pa.z);
		bx = new WB_DoubleDouble(pb.x);
		by = new WB_DoubleDouble(pb.y);
		bz = new WB_DoubleDouble(pb.z);
		cx = new WB_DoubleDouble(pc.x);
		cy = new WB_DoubleDouble(pc.y);
		cz = new WB_DoubleDouble(pc.z);
		dx = new WB_DoubleDouble(pd.x).negate();
		dy = new WB_DoubleDouble(pd.y).negate();
		dz = new WB_DoubleDouble(pd.z).negate();

		adx = ax.add(dx);
		bdx = bx.add(dx);
		cdx = cx.add(dx);
		ady = ay.add(dy);
		bdy = by.add(dy);
		cdy = cy.add(dy);
		adz = az.add(dz);
		bdz = bz.add(dz);
		cdz = cz.add(dz);

		m1 = adx.multiply(bdy).subtract(ady.multiply(bdx));
		m2 = adx.multiply(cdy).subtract(ady.multiply(cdx));
		m3 = bdx.multiply(cdy).subtract(bdy.multiply(cdx));

		det = m1.multiply(cdz).add(m3.multiply(adz)).subtract(m2.multiply(bdz));

		return det.compareTo(WB_DoubleDouble.ZERO);
	}

	// >0 if pe inside sphere through pa,pb,pc,pd (if orient3d(pa,pb,pc,pd)>0))
	// <0 if pe outside sphere through pa,pb,pc,pd (if orient3d(pa,pb,pc,pd)>0))
	// =0 if on sphere
	/**
	 * Insphere.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @param pe the pe
	 * @return the double
	 */
	public static double insphere(final WB_Point3d pa, final WB_Point3d pb,
			final WB_Point3d pc, final WB_Point3d pd, final WB_Point3d pe) {

		if (insphereErrorBound == -1) {
			init();
		}
		double aex, bex, cex, dex;
		double aey, bey, cey, dey;
		double aez, bez, cez, dez;
		double aexbey, bexaey, bexcey, cexbey, cexdey, dexcey, dexaey, aexdey;
		double aexcey, cexaey, bexdey, dexbey;
		double alift, blift, clift, dlift;
		double ab, bc, cd, da, ac, bd;
		double abc, bcd, cda, dab;
		double aezplus, bezplus, cezplus, dezplus;
		double aexbeyplus, bexaeyplus, bexceyplus, cexbeyplus;
		double cexdeyplus, dexceyplus, dexaeyplus, aexdeyplus;
		double aexceyplus, cexaeyplus, bexdeyplus, dexbeyplus;
		double det;
		double permanent, errbound;

		aex = pa.x - pe.x;
		bex = pb.x - pe.x;
		cex = pc.x - pe.x;
		dex = pd.x - pe.x;
		aey = pa.y - pe.y;
		bey = pb.y - pe.y;
		cey = pc.y - pe.y;
		dey = pd.y - pe.y;
		aez = pa.z - pe.z;
		bez = pb.z - pe.z;
		cez = pc.z - pe.z;
		dez = pd.z - pe.z;

		aexbey = aex * bey;
		bexaey = bex * aey;
		ab = aexbey - bexaey;
		bexcey = bex * cey;
		cexbey = cex * bey;
		bc = bexcey - cexbey;
		cexdey = cex * dey;
		dexcey = dex * cey;
		cd = cexdey - dexcey;
		dexaey = dex * aey;
		aexdey = aex * dey;
		da = dexaey - aexdey;

		aexcey = aex * cey;
		cexaey = cex * aey;
		ac = aexcey - cexaey;
		bexdey = bex * dey;
		dexbey = dex * bey;
		bd = bexdey - dexbey;

		abc = aez * bc - bez * ac + cez * ab;
		bcd = bez * cd - cez * bd + dez * bc;
		cda = cez * da + dez * ac + aez * cd;
		dab = dez * ab + aez * bd + bez * da;

		alift = aex * aex + aey * aey + aez * aez;
		blift = bex * bex + bey * bey + bez * bez;
		clift = cex * cex + cey * cey + cez * cez;
		dlift = dex * dex + dey * dey + dez * dez;

		det = (dlift * abc - clift * dab) + (blift * cda - alift * bcd);

		aezplus = Math.abs(aez);
		bezplus = Math.abs(bez);
		cezplus = Math.abs(cez);
		dezplus = Math.abs(dez);
		aexbeyplus = Math.abs(aexbey);
		bexaeyplus = Math.abs(bexaey);
		bexceyplus = Math.abs(bexcey);
		cexbeyplus = Math.abs(cexbey);
		cexdeyplus = Math.abs(cexdey);
		dexceyplus = Math.abs(dexcey);
		dexaeyplus = Math.abs(dexaey);
		aexdeyplus = Math.abs(aexdey);
		aexceyplus = Math.abs(aexcey);
		cexaeyplus = Math.abs(cexaey);
		bexdeyplus = Math.abs(bexdey);
		dexbeyplus = Math.abs(dexbey);
		permanent = ((cexdeyplus + dexceyplus) * bezplus
				+ (dexbeyplus + bexdeyplus) * cezplus + (bexceyplus + cexbeyplus)
				* dezplus)
				* alift
				+ ((dexaeyplus + aexdeyplus) * cezplus
						+ (aexceyplus + cexaeyplus) * dezplus + (cexdeyplus + dexceyplus)
						* aezplus)
				* blift
				+ ((aexbeyplus + bexaeyplus) * dezplus
						+ (bexdeyplus + dexbeyplus) * aezplus + (dexaeyplus + aexdeyplus)
						* bezplus)
				* clift
				+ ((bexceyplus + cexbeyplus) * aezplus
						+ (cexaeyplus + aexceyplus) * bezplus + (aexbeyplus + bexaeyplus)
						* cezplus) * dlift;
		errbound = insphereErrorBound * permanent;
		if ((det > errbound) || (-det > errbound)) {
			return (det > 0) ? 1 : -1;
		}

		return insphereExact(pa, pb, pc, pd, pe);
	}

	/**
	 * Insphere exact.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @param pe the pe
	 * @return the double
	 */
	public static double insphereExact(final WB_Point3d pa,
			final WB_Point3d pb, final WB_Point3d pc, final WB_Point3d pd,
			final WB_Point3d pe) {
		WB_DoubleDouble ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, ex, ey, ez;
		WB_DoubleDouble aex, bex, cex, dex;
		WB_DoubleDouble aey, bey, cey, dey;
		WB_DoubleDouble aez, bez, cez, dez;
		WB_DoubleDouble aexbey, bexaey, bexcey, cexbey, cexdey, dexcey, dexaey, aexdey;
		WB_DoubleDouble aexcey, cexaey, bexdey, dexbey;
		WB_DoubleDouble alift, blift, clift, dlift;
		WB_DoubleDouble ab, bc, cd, da, ac, bd;
		WB_DoubleDouble abc, bcd, cda, dab;
		WB_DoubleDouble det;

		det = WB_DoubleDouble.ZERO;

		ax = new WB_DoubleDouble(pa.x);
		ay = new WB_DoubleDouble(pa.y);
		az = new WB_DoubleDouble(pa.z);
		bx = new WB_DoubleDouble(pb.x);
		by = new WB_DoubleDouble(pb.y);
		bz = new WB_DoubleDouble(pb.z);
		cx = new WB_DoubleDouble(pc.x);
		cy = new WB_DoubleDouble(pc.y);
		cz = new WB_DoubleDouble(pc.z);
		dx = new WB_DoubleDouble(pd.x);
		dy = new WB_DoubleDouble(pd.y);
		dz = new WB_DoubleDouble(pd.z);
		ex = new WB_DoubleDouble(pe.x).negate();
		ey = new WB_DoubleDouble(pe.y).negate();
		ez = new WB_DoubleDouble(pe.z).negate();

		aex = ax.add(ex);
		bex = bx.add(ex);
		cex = cx.add(ex);
		dex = dx.add(ex);
		aey = ay.add(ey);
		bey = by.add(ey);
		cey = cy.add(ey);
		dey = dy.add(ey);
		aez = az.add(ez);
		bez = bz.add(ez);
		cez = cz.add(ez);
		dez = dz.add(ez);

		aexbey = aex.multiply(bey);
		bexaey = bex.multiply(aey);
		ab = aexbey.subtract(bexaey);
		bexcey = bex.multiply(cey);
		cexbey = cex.multiply(bey);
		bc = bexcey.subtract(cexbey);
		cexdey = cex.multiply(dey);
		dexcey = dex.multiply(cey);
		cd = cexdey.subtract(dexcey);
		dexaey = dex.multiply(aey);
		aexdey = aex.multiply(dey);
		da = dexaey.subtract(aexdey);
		aexcey = aex.multiply(cey);
		cexaey = cex.multiply(aey);
		ac = aexcey.subtract(cexaey);
		bexdey = bex.multiply(dey);
		dexbey = dex.multiply(bey);
		bd = bexdey.subtract(dexbey);

		abc = aez.multiply(bc).add(cez.multiply(ab)).subtract(bez.multiply(ac));
		bcd = bez.multiply(cd).add(dez.multiply(bc)).subtract(cez.multiply(bd));
		cda = cez.multiply(da).add(aez.multiply(cd)).subtract(dez.multiply(ac));
		dab = dez.multiply(ab).add(bez.multiply(da)).subtract(aez.multiply(bd));

		alift = aex.sqr().add(aey.sqr()).add(aez.sqr());
		blift = bex.sqr().add(bey.sqr()).add(bez.sqr());
		clift = cex.sqr().add(cey.sqr()).add(cez.sqr());
		dlift = dex.sqr().add(dey.sqr()).add(dez.sqr());

		det = dlift.multiply(abc).subtract(clift.multiply(dab))
				.add(blift.multiply(cda)).subtract(alift.multiply(bcd));

		return det.compareTo(WB_DoubleDouble.ZERO);
	}

	// >0 if pe inside sphere through pa,pb,pc,pd (regardless of
	// orient3d(pa,pb,pc,pd))
	// <0 if pe outside sphere through pa,pb,pc,pd (regardless of
	// orient3d(pa,pb,pc,pd))
	// =0 if on sphere
	/**
	 * In sphere orient.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @param pe the pe
	 * @return the double
	 */
	public static double inSphereOrient(final WB_Point3d pa,
			final WB_Point3d pb, final WB_Point3d pc, final WB_Point3d pd,
			final WB_Point3d pe) {

		if (orient(pa, pb, pc, pd) > 0) {
			return insphere(pa, pb, pc, pd, pe);
		}
		final double is = insphere(pa, pb, pc, pd, pe);
		if (is > 0) {
			return -1;
		}
		if (is < 0) {
			return 1;
		}
		return 0;

	}

	// diffsides returns true if q1 and q2 are NOT on the same side of the plane
	// expanded by p1,p2, and p3.
	/**
	 * Diff sides.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param p3 the p3
	 * @param q1 the q1
	 * @param q2 the q2
	 * @return true, if successful
	 */
	public static boolean diffSides(final WB_Point3d p1, final WB_Point3d p2,
			final WB_Point3d p3, final WB_Point3d q1, final WB_Point3d q2) {
		double a, b;
		a = orient(p1, p2, p3, q1);
		b = orient(p1, p2, p3, q2);
		return ((a > 0 && b < 0) || (a < 0 && b > 0));
	}

	/**
	 * Inside tetrahedron.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param p3 the p3
	 * @param p4 the p4
	 * @param q the q
	 * @return true, if successful
	 */
	public static boolean insideTetrahedron(final WB_Point3d p1,
			final WB_Point3d p2, final WB_Point3d p3, final WB_Point3d p4,
			final WB_Point3d q) {

		return (!diffSides(p1, p2, p3, q, p4) && !diffSides(p2, p3, p4, q, p1)
				&& !diffSides(p1, p2, p4, q, p3) && !diffSides(p1, p3, p4, q,
					p2));
	}
	
	/**
	 * Instantiates a new w b_ predicates.
	 */
	public WB_Predicates() {
		_exactinit();
	}

	/**
	 * Circumradius tetra.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param p3 the p3
	 * @return the double
	 */
	public double circumradiusTetra(double[] p0, double[] p1, double[] p2,
			double[] p3) {
		double t1, t2, t3;
		double[] circumcenter = circumcenterTetra(p0, p1, p2, p3, null, null,
				null);
		t1 = circumcenter[0] - p0[0];
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0[1];
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0[2];
		t3 = t3 * t3;
		return (Math.sqrt(t1 + t2 + t3));
	}

	/**
	 * Circumradius tri.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return the double
	 */
	public double circumradiusTri(double[] p0, double[] p1, double[] p2) {

		double t1, t2, t3;
		double[] circumcenter = circumcenterTri(p0, p1, p2);
		t1 = circumcenter[0] - p0[0];
		t1 = t1 * t1;
		t2 = circumcenter[1] - p0[1];
		t2 = t2 * t2;
		t3 = circumcenter[2] - p0[2];
		t3 = t3 * t3;
		return (Math.sqrt(t1 + t2 + t3));
	}

	/**
	 * Orient tetra.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param p3 the p3
	 * @return the double
	 */
	public double orientTetra(double[] p0, double[] p1, double[] p2, double[] p3) {
		double adx, bdx, cdx, ady, bdy, cdy, adz, bdz, cdz;
		double bdxcdy, cdxbdy, cdxady, adxcdy, adxbdy, bdxady;
		double det;
		double permanent, errbound;

		adx = p0[0] - p3[0];
		bdx = p1[0] - p3[0];
		cdx = p2[0] - p3[0];
		ady = p0[1] - p3[1];
		bdy = p1[1] - p3[1];
		cdy = p2[1] - p3[1];
		adz = p0[2] - p3[2];
		bdz = p1[2] - p3[2];
		cdz = p2[2] - p3[2];

		bdxcdy = bdx * cdy;
		cdxbdy = cdx * bdy;

		cdxady = cdx * ady;
		adxcdy = adx * cdy;

		adxbdy = adx * bdy;
		bdxady = bdx * ady;

		det = adz * (bdxcdy - cdxbdy) + bdz * (cdxady - adxcdy) + cdz
				* (adxbdy - bdxady);

		permanent = (((bdxcdy) >= 0.0 ? (bdxcdy) : -(bdxcdy)) + ((cdxbdy) >= 0.0 ? (cdxbdy)
				: -(cdxbdy)))
				* ((adz) >= 0.0 ? (adz) : -(adz))
				+ (((cdxady) >= 0.0 ? (cdxady) : -(cdxady)) + ((adxcdy) >= 0.0 ? (adxcdy)
						: -(adxcdy)))
				* ((bdz) >= 0.0 ? (bdz) : -(bdz))
				+ (((adxbdy) >= 0.0 ? (adxbdy) : -(adxbdy)) + ((bdxady) >= 0.0 ? (bdxady)
						: -(bdxady))) * ((cdz) >= 0.0 ? (cdz) : -(cdz));
		errbound = o3derrboundA * permanent;
		if ((det > errbound) || (-det > errbound)) {
			return det;
		}
		return _orientTetraAdapt(p0, p1, p2, p3, permanent);
	}

	/**
	 * _orient tetra adapt.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @param permanent the permanent
	 * @return the double
	 */
	private double _orientTetraAdapt(double[] pa, double[] pb, double[] pc,
			double[] pd, double permanent) {
		double adx, bdx, cdx, ady, bdy, cdy, adz, bdz, cdz;
		double det, errbound;

		double bdxcdy1, cdxbdy1, cdxady1, adxcdy1, adxbdy1, bdxady1;
		double bdxcdy0, cdxbdy0, cdxady0, adxcdy0, adxbdy0, bdxady0;
		double[] bc = new double[4], ca = new double[4], ab = new double[4];
		double bc3, ca3, ab3;
		double[] adet = new double[8], bdet = new double[8], cdet = new double[8];
		int alen, blen, clen;
		double[] abdet = new double[16];
		int ablen;
		double[] finnow, finother, finswap;
		double[] fin1 = new double[192], fin2 = new double[192];
		int finlength;

		double adxtail, bdxtail, cdxtail;
		double adytail, bdytail, cdytail;
		double adztail, bdztail, cdztail;
		double at_blarge, at_clarge;
		double bt_clarge, bt_alarge;
		double ct_alarge, ct_blarge;
		double[] at_b = new double[4], at_c = new double[4], bt_c = new double[4], bt_a = new double[4], ct_a = new double[4], ct_b = new double[4];
		int at_blen, at_clen, bt_clen, bt_alen, ct_alen, ct_blen;
		double bdxt_cdy1, cdxt_bdy1, cdxt_ady1;
		double adxt_cdy1, adxt_bdy1, bdxt_ady1;
		double bdxt_cdy0, cdxt_bdy0, cdxt_ady0;
		double adxt_cdy0, adxt_bdy0, bdxt_ady0;
		double bdyt_cdx1, cdyt_bdx1, cdyt_adx1;
		double adyt_cdx1, adyt_bdx1, bdyt_adx1;
		double bdyt_cdx0, cdyt_bdx0, cdyt_adx0;
		double adyt_cdx0, adyt_bdx0, bdyt_adx0;
		double[] bct = new double[8], cat = new double[8], abt = new double[8];
		int bctlen, catlen, abtlen;
		double bdxt_cdyt1, cdxt_bdyt1, cdxt_adyt1;
		double adxt_cdyt1, adxt_bdyt1, bdxt_adyt1;
		double bdxt_cdyt0, cdxt_bdyt0, cdxt_adyt0;
		double adxt_cdyt0, adxt_bdyt0, bdxt_adyt0;
		double[] u = new double[4], v = new double[12], w = new double[16];
		double u3;
		int vlength, wlength;
		double negate;

		double bvirt;
		double avirt, bround, around;
		double c;
		double abig;
		double ahi, alo, bhi, blo;
		double err1, err2, err3;
		double _i, _j, _k;
		double _0;

		adx = (double) (pa[0] - pd[0]);
		bdx = (double) (pb[0] - pd[0]);
		cdx = (double) (pc[0] - pd[0]);
		ady = (double) (pa[1] - pd[1]);
		bdy = (double) (pb[1] - pd[1]);
		cdy = (double) (pc[1] - pd[1]);
		adz = (double) (pa[2] - pd[2]);
		bdz = (double) (pb[2] - pd[2]);
		cdz = (double) (pc[2] - pd[2]);

		bdxcdy1 = (double) (bdx * cdy);
		c = (double) (splitter * bdx);
		abig = (double) (c - bdx);
		ahi = c - abig;
		alo = bdx - ahi;
		c = (double) (splitter * cdy);
		abig = (double) (c - cdy);
		bhi = c - abig;
		blo = cdy - bhi;
		err1 = bdxcdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bdxcdy0 = (alo * blo) - err3;
		cdxbdy1 = (double) (cdx * bdy);
		c = (double) (splitter * cdx);
		abig = (double) (c - cdx);
		ahi = c - abig;
		alo = cdx - ahi;
		c = (double) (splitter * bdy);
		abig = (double) (c - bdy);
		bhi = c - abig;
		blo = bdy - bhi;
		err1 = cdxbdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cdxbdy0 = (alo * blo) - err3;
		_i = (double) (bdxcdy0 - cdxbdy0);
		bvirt = (double) (bdxcdy0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cdxbdy0;
		around = bdxcdy0 - avirt;
		bc[0] = around + bround;
		_j = (double) (bdxcdy1 + _i);
		bvirt = (double) (_j - bdxcdy1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = bdxcdy1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - cdxbdy1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cdxbdy1;
		around = _0 - avirt;
		bc[1] = around + bround;
		bc3 = (double) (_j + _i);
		bvirt = (double) (bc3 - _j);
		avirt = bc3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		bc[2] = around + bround;
		bc[3] = bc3;
		alen = _scale_expansion_zeroelim(4, bc, adz, adet);

		cdxady1 = (double) (cdx * ady);
		c = (double) (splitter * cdx);
		abig = (double) (c - cdx);
		ahi = c - abig;
		alo = cdx - ahi;
		c = (double) (splitter * ady);
		abig = (double) (c - ady);
		bhi = c - abig;
		blo = ady - bhi;
		err1 = cdxady1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cdxady0 = (alo * blo) - err3;
		adxcdy1 = (double) (adx * cdy);
		c = (double) (splitter * adx);
		abig = (double) (c - adx);
		ahi = c - abig;
		alo = adx - ahi;
		c = (double) (splitter * cdy);
		abig = (double) (c - cdy);
		bhi = c - abig;
		blo = cdy - bhi;
		err1 = adxcdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		adxcdy0 = (alo * blo) - err3;
		_i = (double) (cdxady0 - adxcdy0);
		bvirt = (double) (cdxady0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - adxcdy0;
		around = cdxady0 - avirt;
		ca[0] = around + bround;
		_j = (double) (cdxady1 + _i);
		bvirt = (double) (_j - cdxady1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = cdxady1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - adxcdy1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - adxcdy1;
		around = _0 - avirt;
		ca[1] = around + bround;
		ca3 = (double) (_j + _i);
		bvirt = (double) (ca3 - _j);
		avirt = ca3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ca[2] = around + bround;
		ca[3] = ca3;
		blen = _scale_expansion_zeroelim(4, ca, bdz, bdet);

		adxbdy1 = (double) (adx * bdy);
		c = (double) (splitter * adx);
		abig = (double) (c - adx);
		ahi = c - abig;
		alo = adx - ahi;
		c = (double) (splitter * bdy);
		abig = (double) (c - bdy);
		bhi = c - abig;
		blo = bdy - bhi;
		err1 = adxbdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		adxbdy0 = (alo * blo) - err3;
		bdxady1 = (double) (bdx * ady);
		c = (double) (splitter * bdx);
		abig = (double) (c - bdx);
		ahi = c - abig;
		alo = bdx - ahi;
		c = (double) (splitter * ady);
		abig = (double) (c - ady);
		bhi = c - abig;
		blo = ady - bhi;
		err1 = bdxady1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bdxady0 = (alo * blo) - err3;
		_i = (double) (adxbdy0 - bdxady0);
		bvirt = (double) (adxbdy0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bdxady0;
		around = adxbdy0 - avirt;
		ab[0] = around + bround;
		_j = (double) (adxbdy1 + _i);
		bvirt = (double) (_j - adxbdy1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = adxbdy1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - bdxady1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bdxady1;
		around = _0 - avirt;
		ab[1] = around + bround;
		ab3 = (double) (_j + _i);
		bvirt = (double) (ab3 - _j);
		avirt = ab3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ab[2] = around + bround;
		ab[3] = ab3;
		clen = _scale_expansion_zeroelim(4, ab, cdz, cdet);

		ablen = _fast_expansion_sum_zeroelim(alen, adet, blen, bdet, abdet);
		finlength = _fast_expansion_sum_zeroelim(ablen, abdet, clen, cdet, fin1);

		det = _estimate(finlength, fin1);
		errbound = o3derrboundB * permanent;
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		bvirt = (double) (pa[0] - adx);
		avirt = adx + bvirt;
		bround = bvirt - pd[0];
		around = pa[0] - avirt;
		adxtail = around + bround;
		bvirt = (double) (pb[0] - bdx);
		avirt = bdx + bvirt;
		bround = bvirt - pd[0];
		around = pb[0] - avirt;
		bdxtail = around + bround;
		bvirt = (double) (pc[0] - cdx);
		avirt = cdx + bvirt;
		bround = bvirt - pd[0];
		around = pc[0] - avirt;
		cdxtail = around + bround;
		bvirt = (double) (pa[1] - ady);
		avirt = ady + bvirt;
		bround = bvirt - pd[1];
		around = pa[1] - avirt;
		adytail = around + bround;
		bvirt = (double) (pb[1] - bdy);
		avirt = bdy + bvirt;
		bround = bvirt - pd[1];
		around = pb[1] - avirt;
		bdytail = around + bround;
		bvirt = (double) (pc[1] - cdy);
		avirt = cdy + bvirt;
		bround = bvirt - pd[1];
		around = pc[1] - avirt;
		cdytail = around + bround;
		bvirt = (double) (pa[2] - adz);
		avirt = adz + bvirt;
		bround = bvirt - pd[2];
		around = pa[2] - avirt;
		adztail = around + bround;
		bvirt = (double) (pb[2] - bdz);
		avirt = bdz + bvirt;
		bround = bvirt - pd[2];
		around = pb[2] - avirt;
		bdztail = around + bround;
		bvirt = (double) (pc[2] - cdz);
		avirt = cdz + bvirt;
		bround = bvirt - pd[2];
		around = pc[2] - avirt;
		cdztail = around + bround;

		if ((adxtail == 0.0) && (bdxtail == 0.0) && (cdxtail == 0.0)
				&& (adytail == 0.0) && (bdytail == 0.0) && (cdytail == 0.0)
				&& (adztail == 0.0) && (bdztail == 0.0) && (cdztail == 0.0)) {
			return det;
		}

		errbound = o3derrboundC * permanent + resulterrbound
				* ((det) >= 0.0 ? (det) : -(det));
		det += (adz
				* ((bdx * cdytail + cdy * bdxtail) - (bdy * cdxtail + cdx
						* bdytail)) + adztail * (bdx * cdy - bdy * cdx))
				+ (bdz
						* ((cdx * adytail + ady * cdxtail) - (cdy * adxtail + adx
								* cdytail)) + bdztail * (cdx * ady - cdy * adx))
				+ (cdz
						* ((adx * bdytail + bdy * adxtail) - (ady * bdxtail + bdx
								* adytail)) + cdztail * (adx * bdy - ady * bdx));
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		finnow = fin1;
		finother = fin2;

		if (adxtail == 0.0) {
			if (adytail == 0.0) {
				at_b[0] = 0.0;
				at_blen = 1;
				at_c[0] = 0.0;
				at_clen = 1;
			} else {
				negate = -adytail;
				at_blarge = (double) (negate * bdx);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * bdx);
				abig = (double) (c - bdx);
				bhi = c - abig;
				blo = bdx - bhi;
				err1 = at_blarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				at_b[0] = (alo * blo) - err3;
				at_b[1] = at_blarge;
				at_blen = 2;
				at_clarge = (double) (adytail * cdx);
				c = (double) (splitter * adytail);
				abig = (double) (c - adytail);
				ahi = c - abig;
				alo = adytail - ahi;
				c = (double) (splitter * cdx);
				abig = (double) (c - cdx);
				bhi = c - abig;
				blo = cdx - bhi;
				err1 = at_clarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				at_c[0] = (alo * blo) - err3;
				at_c[1] = at_clarge;
				at_clen = 2;
			}
		} else {
			if (adytail == 0.0) {
				at_blarge = (double) (adxtail * bdy);
				c = (double) (splitter * adxtail);
				abig = (double) (c - adxtail);
				ahi = c - abig;
				alo = adxtail - ahi;
				c = (double) (splitter * bdy);
				abig = (double) (c - bdy);
				bhi = c - abig;
				blo = bdy - bhi;
				err1 = at_blarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				at_b[0] = (alo * blo) - err3;
				at_b[1] = at_blarge;
				at_blen = 2;
				negate = -adxtail;
				at_clarge = (double) (negate * cdy);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * cdy);
				abig = (double) (c - cdy);
				bhi = c - abig;
				blo = cdy - bhi;
				err1 = at_clarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				at_c[0] = (alo * blo) - err3;
				at_c[1] = at_clarge;
				at_clen = 2;
			} else {
				adxt_bdy1 = (double) (adxtail * bdy);
				c = (double) (splitter * adxtail);
				abig = (double) (c - adxtail);
				ahi = c - abig;
				alo = adxtail - ahi;
				c = (double) (splitter * bdy);
				abig = (double) (c - bdy);
				bhi = c - abig;
				blo = bdy - bhi;
				err1 = adxt_bdy1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				adxt_bdy0 = (alo * blo) - err3;
				adyt_bdx1 = (double) (adytail * bdx);
				c = (double) (splitter * adytail);
				abig = (double) (c - adytail);
				ahi = c - abig;
				alo = adytail - ahi;
				c = (double) (splitter * bdx);
				abig = (double) (c - bdx);
				bhi = c - abig;
				blo = bdx - bhi;
				err1 = adyt_bdx1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				adyt_bdx0 = (alo * blo) - err3;
				_i = (double) (adxt_bdy0 - adyt_bdx0);
				bvirt = (double) (adxt_bdy0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - adyt_bdx0;
				around = adxt_bdy0 - avirt;
				at_b[0] = around + bround;
				_j = (double) (adxt_bdy1 + _i);
				bvirt = (double) (_j - adxt_bdy1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = adxt_bdy1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - adyt_bdx1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - adyt_bdx1;
				around = _0 - avirt;
				at_b[1] = around + bround;
				at_blarge = (double) (_j + _i);
				bvirt = (double) (at_blarge - _j);
				avirt = at_blarge - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				at_b[2] = around + bround;

				at_b[3] = at_blarge;
				at_blen = 4;
				adyt_cdx1 = (double) (adytail * cdx);
				c = (double) (splitter * adytail);
				abig = (double) (c - adytail);
				ahi = c - abig;
				alo = adytail - ahi;
				c = (double) (splitter * cdx);
				abig = (double) (c - cdx);
				bhi = c - abig;
				blo = cdx - bhi;
				err1 = adyt_cdx1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				adyt_cdx0 = (alo * blo) - err3;
				adxt_cdy1 = (double) (adxtail * cdy);
				c = (double) (splitter * adxtail);
				abig = (double) (c - adxtail);
				ahi = c - abig;
				alo = adxtail - ahi;
				c = (double) (splitter * cdy);
				abig = (double) (c - cdy);
				bhi = c - abig;
				blo = cdy - bhi;
				err1 = adxt_cdy1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				adxt_cdy0 = (alo * blo) - err3;
				_i = (double) (adyt_cdx0 - adxt_cdy0);
				bvirt = (double) (adyt_cdx0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - adxt_cdy0;
				around = adyt_cdx0 - avirt;
				at_c[0] = around + bround;
				_j = (double) (adyt_cdx1 + _i);
				bvirt = (double) (_j - adyt_cdx1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = adyt_cdx1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - adxt_cdy1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - adxt_cdy1;
				around = _0 - avirt;
				at_c[1] = around + bround;
				at_clarge = (double) (_j + _i);
				bvirt = (double) (at_clarge - _j);
				avirt = at_clarge - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				at_c[2] = around + bround;

				at_c[3] = at_clarge;
				at_clen = 4;
			}
		}
		if (bdxtail == 0.0) {
			if (bdytail == 0.0) {
				bt_c[0] = 0.0;
				bt_clen = 1;
				bt_a[0] = 0.0;
				bt_alen = 1;
			} else {
				negate = -bdytail;
				bt_clarge = (double) (negate * cdx);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * cdx);
				abig = (double) (c - cdx);
				bhi = c - abig;
				blo = cdx - bhi;
				err1 = bt_clarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bt_c[0] = (alo * blo) - err3;
				bt_c[1] = bt_clarge;
				bt_clen = 2;
				bt_alarge = (double) (bdytail * adx);
				c = (double) (splitter * bdytail);
				abig = (double) (c - bdytail);
				ahi = c - abig;
				alo = bdytail - ahi;
				c = (double) (splitter * adx);
				abig = (double) (c - adx);
				bhi = c - abig;
				blo = adx - bhi;
				err1 = bt_alarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bt_a[0] = (alo * blo) - err3;
				bt_a[1] = bt_alarge;
				bt_alen = 2;
			}
		} else {
			if (bdytail == 0.0) {
				bt_clarge = (double) (bdxtail * cdy);
				c = (double) (splitter * bdxtail);
				abig = (double) (c - bdxtail);
				ahi = c - abig;
				alo = bdxtail - ahi;
				c = (double) (splitter * cdy);
				abig = (double) (c - cdy);
				bhi = c - abig;
				blo = cdy - bhi;
				err1 = bt_clarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bt_c[0] = (alo * blo) - err3;
				bt_c[1] = bt_clarge;
				bt_clen = 2;
				negate = -bdxtail;
				bt_alarge = (double) (negate * ady);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * ady);
				abig = (double) (c - ady);
				bhi = c - abig;
				blo = ady - bhi;
				err1 = bt_alarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bt_a[0] = (alo * blo) - err3;
				bt_a[1] = bt_alarge;
				bt_alen = 2;
			} else {
				bdxt_cdy1 = (double) (bdxtail * cdy);
				c = (double) (splitter * bdxtail);
				abig = (double) (c - bdxtail);
				ahi = c - abig;
				alo = bdxtail - ahi;
				c = (double) (splitter * cdy);
				abig = (double) (c - cdy);
				bhi = c - abig;
				blo = cdy - bhi;
				err1 = bdxt_cdy1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bdxt_cdy0 = (alo * blo) - err3;
				bdyt_cdx1 = (double) (bdytail * cdx);
				c = (double) (splitter * bdytail);
				abig = (double) (c - bdytail);
				ahi = c - abig;
				alo = bdytail - ahi;
				c = (double) (splitter * cdx);
				abig = (double) (c - cdx);
				bhi = c - abig;
				blo = cdx - bhi;
				err1 = bdyt_cdx1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bdyt_cdx0 = (alo * blo) - err3;
				_i = (double) (bdxt_cdy0 - bdyt_cdx0);
				bvirt = (double) (bdxt_cdy0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - bdyt_cdx0;
				around = bdxt_cdy0 - avirt;
				bt_c[0] = around + bround;
				_j = (double) (bdxt_cdy1 + _i);
				bvirt = (double) (_j - bdxt_cdy1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = bdxt_cdy1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - bdyt_cdx1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - bdyt_cdx1;
				around = _0 - avirt;
				bt_c[1] = around + bround;
				bt_clarge = (double) (_j + _i);
				bvirt = (double) (bt_clarge - _j);
				avirt = bt_clarge - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				bt_c[2] = around + bround;

				bt_c[3] = bt_clarge;
				bt_clen = 4;
				bdyt_adx1 = (double) (bdytail * adx);
				c = (double) (splitter * bdytail);
				abig = (double) (c - bdytail);
				ahi = c - abig;
				alo = bdytail - ahi;
				c = (double) (splitter * adx);
				abig = (double) (c - adx);
				bhi = c - abig;
				blo = adx - bhi;
				err1 = bdyt_adx1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bdyt_adx0 = (alo * blo) - err3;
				bdxt_ady1 = (double) (bdxtail * ady);
				c = (double) (splitter * bdxtail);
				abig = (double) (c - bdxtail);
				ahi = c - abig;
				alo = bdxtail - ahi;
				c = (double) (splitter * ady);
				abig = (double) (c - ady);
				bhi = c - abig;
				blo = ady - bhi;
				err1 = bdxt_ady1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bdxt_ady0 = (alo * blo) - err3;
				_i = (double) (bdyt_adx0 - bdxt_ady0);
				bvirt = (double) (bdyt_adx0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - bdxt_ady0;
				around = bdyt_adx0 - avirt;
				bt_a[0] = around + bround;
				_j = (double) (bdyt_adx1 + _i);
				bvirt = (double) (_j - bdyt_adx1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = bdyt_adx1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - bdxt_ady1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - bdxt_ady1;
				around = _0 - avirt;
				bt_a[1] = around + bround;
				bt_alarge = (double) (_j + _i);
				bvirt = (double) (bt_alarge - _j);
				avirt = bt_alarge - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				bt_a[2] = around + bround;

				bt_a[3] = bt_alarge;
				bt_alen = 4;
			}
		}
		if (cdxtail == 0.0) {
			if (cdytail == 0.0) {
				ct_a[0] = 0.0;
				ct_alen = 1;
				ct_b[0] = 0.0;
				ct_blen = 1;
			} else {
				negate = -cdytail;
				ct_alarge = (double) (negate * adx);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * adx);
				abig = (double) (c - adx);
				bhi = c - abig;
				blo = adx - bhi;
				err1 = ct_alarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ct_a[0] = (alo * blo) - err3;
				ct_a[1] = ct_alarge;
				ct_alen = 2;
				ct_blarge = (double) (cdytail * bdx);
				c = (double) (splitter * cdytail);
				abig = (double) (c - cdytail);
				ahi = c - abig;
				alo = cdytail - ahi;
				c = (double) (splitter * bdx);
				abig = (double) (c - bdx);
				bhi = c - abig;
				blo = bdx - bhi;
				err1 = ct_blarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ct_b[0] = (alo * blo) - err3;
				ct_b[1] = ct_blarge;
				ct_blen = 2;
			}
		} else {
			if (cdytail == 0.0) {
				ct_alarge = (double) (cdxtail * ady);
				c = (double) (splitter * cdxtail);
				abig = (double) (c - cdxtail);
				ahi = c - abig;
				alo = cdxtail - ahi;
				c = (double) (splitter * ady);
				abig = (double) (c - ady);
				bhi = c - abig;
				blo = ady - bhi;
				err1 = ct_alarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ct_a[0] = (alo * blo) - err3;
				ct_a[1] = ct_alarge;
				ct_alen = 2;
				negate = -cdxtail;
				ct_blarge = (double) (negate * bdy);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * bdy);
				abig = (double) (c - bdy);
				bhi = c - abig;
				blo = bdy - bhi;
				err1 = ct_blarge - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ct_b[0] = (alo * blo) - err3;
				ct_b[1] = ct_blarge;
				ct_blen = 2;
			} else {
				cdxt_ady1 = (double) (cdxtail * ady);
				c = (double) (splitter * cdxtail);
				abig = (double) (c - cdxtail);
				ahi = c - abig;
				alo = cdxtail - ahi;
				c = (double) (splitter * ady);
				abig = (double) (c - ady);
				bhi = c - abig;
				blo = ady - bhi;
				err1 = cdxt_ady1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				cdxt_ady0 = (alo * blo) - err3;
				cdyt_adx1 = (double) (cdytail * adx);
				c = (double) (splitter * cdytail);
				abig = (double) (c - cdytail);
				ahi = c - abig;
				alo = cdytail - ahi;
				c = (double) (splitter * adx);
				abig = (double) (c - adx);
				bhi = c - abig;
				blo = adx - bhi;
				err1 = cdyt_adx1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				cdyt_adx0 = (alo * blo) - err3;
				_i = (double) (cdxt_ady0 - cdyt_adx0);
				bvirt = (double) (cdxt_ady0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - cdyt_adx0;
				around = cdxt_ady0 - avirt;
				ct_a[0] = around + bround;
				_j = (double) (cdxt_ady1 + _i);
				bvirt = (double) (_j - cdxt_ady1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = cdxt_ady1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - cdyt_adx1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - cdyt_adx1;
				around = _0 - avirt;
				ct_a[1] = around + bround;
				ct_alarge = (double) (_j + _i);
				bvirt = (double) (ct_alarge - _j);
				avirt = ct_alarge - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				ct_a[2] = around + bround;

				ct_a[3] = ct_alarge;
				ct_alen = 4;
				cdyt_bdx1 = (double) (cdytail * bdx);
				c = (double) (splitter * cdytail);
				abig = (double) (c - cdytail);
				ahi = c - abig;
				alo = cdytail - ahi;
				c = (double) (splitter * bdx);
				abig = (double) (c - bdx);
				bhi = c - abig;
				blo = bdx - bhi;
				err1 = cdyt_bdx1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				cdyt_bdx0 = (alo * blo) - err3;
				cdxt_bdy1 = (double) (cdxtail * bdy);
				c = (double) (splitter * cdxtail);
				abig = (double) (c - cdxtail);
				ahi = c - abig;
				alo = cdxtail - ahi;
				c = (double) (splitter * bdy);
				abig = (double) (c - bdy);
				bhi = c - abig;
				blo = bdy - bhi;
				err1 = cdxt_bdy1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				cdxt_bdy0 = (alo * blo) - err3;
				_i = (double) (cdyt_bdx0 - cdxt_bdy0);
				bvirt = (double) (cdyt_bdx0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - cdxt_bdy0;
				around = cdyt_bdx0 - avirt;
				ct_b[0] = around + bround;
				_j = (double) (cdyt_bdx1 + _i);
				bvirt = (double) (_j - cdyt_bdx1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = cdyt_bdx1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - cdxt_bdy1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - cdxt_bdy1;
				around = _0 - avirt;
				ct_b[1] = around + bround;
				ct_blarge = (double) (_j + _i);
				bvirt = (double) (ct_blarge - _j);
				avirt = ct_blarge - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				ct_b[2] = around + bround;

				ct_b[3] = ct_blarge;
				ct_blen = 4;
			}
		}

		bctlen = _fast_expansion_sum_zeroelim(bt_clen, bt_c, ct_blen, ct_b, bct);
		wlength = _scale_expansion_zeroelim(bctlen, bct, adz, w);
		finlength = _fast_expansion_sum_zeroelim(finlength, finnow, wlength, w,
				finother);
		finswap = finnow;
		finnow = finother;
		finother = finswap;

		catlen = _fast_expansion_sum_zeroelim(ct_alen, ct_a, at_clen, at_c, cat);
		wlength = _scale_expansion_zeroelim(catlen, cat, bdz, w);
		finlength = _fast_expansion_sum_zeroelim(finlength, finnow, wlength, w,
				finother);
		finswap = finnow;
		finnow = finother;
		finother = finswap;

		abtlen = _fast_expansion_sum_zeroelim(at_blen, at_b, bt_alen, bt_a, abt);
		wlength = _scale_expansion_zeroelim(abtlen, abt, cdz, w);
		finlength = _fast_expansion_sum_zeroelim(finlength, finnow, wlength, w,
				finother);
		finswap = finnow;
		finnow = finother;
		finother = finswap;

		if (adztail != 0.0) {
			vlength = _scale_expansion_zeroelim(4, bc, adztail, v);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					vlength, v, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (bdztail != 0.0) {
			vlength = _scale_expansion_zeroelim(4, ca, bdztail, v);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					vlength, v, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (cdztail != 0.0) {
			vlength = _scale_expansion_zeroelim(4, ab, cdztail, v);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					vlength, v, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}

		if (adxtail != 0.0) {
			if (bdytail != 0.0) {
				adxt_bdyt1 = (double) (adxtail * bdytail);
				c = (double) (splitter * adxtail);
				abig = (double) (c - adxtail);
				ahi = c - abig;
				alo = adxtail - ahi;
				c = (double) (splitter * bdytail);
				abig = (double) (c - bdytail);
				bhi = c - abig;
				blo = bdytail - bhi;
				err1 = adxt_bdyt1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				adxt_bdyt0 = (alo * blo) - err3;
				c = (double) (splitter * cdz);
				abig = (double) (c - cdz);
				bhi = c - abig;
				blo = cdz - bhi;
				_i = (double) (adxt_bdyt0 * cdz);
				c = (double) (splitter * adxt_bdyt0);
				abig = (double) (c - adxt_bdyt0);
				ahi = c - abig;
				alo = adxt_bdyt0 - ahi;
				err1 = _i - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				u[0] = (alo * blo) - err3;
				_j = (double) (adxt_bdyt1 * cdz);
				c = (double) (splitter * adxt_bdyt1);
				abig = (double) (c - adxt_bdyt1);
				ahi = c - abig;
				alo = adxt_bdyt1 - ahi;
				err1 = _j - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				_0 = (alo * blo) - err3;
				_k = (double) (_i + _0);
				bvirt = (double) (_k - _i);
				avirt = _k - bvirt;
				bround = _0 - bvirt;
				around = _i - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _k);
				bvirt = u3 - _j;
				u[2] = _k - bvirt;
				u[3] = u3;
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow, 4,
						u, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (cdztail != 0.0) {
					c = (double) (splitter * cdztail);
					abig = (double) (c - cdztail);
					bhi = c - abig;
					blo = cdztail - bhi;
					_i = (double) (adxt_bdyt0 * cdztail);
					c = (double) (splitter * adxt_bdyt0);
					abig = (double) (c - adxt_bdyt0);
					ahi = c - abig;
					alo = adxt_bdyt0 - ahi;
					err1 = _i - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					u[0] = (alo * blo) - err3;
					_j = (double) (adxt_bdyt1 * cdztail);
					c = (double) (splitter * adxt_bdyt1);
					abig = (double) (c - adxt_bdyt1);
					ahi = c - abig;
					alo = adxt_bdyt1 - ahi;
					err1 = _j - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					_0 = (alo * blo) - err3;
					_k = (double) (_i + _0);
					bvirt = (double) (_k - _i);
					avirt = _k - bvirt;
					bround = _0 - bvirt;
					around = _i - avirt;
					u[1] = around + bround;
					u3 = (double) (_j + _k);
					bvirt = u3 - _j;
					u[2] = _k - bvirt;
					u[3] = u3;
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							4, u, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
			}
			if (cdytail != 0.0) {
				negate = -adxtail;
				adxt_cdyt1 = (double) (negate * cdytail);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * cdytail);
				abig = (double) (c - cdytail);
				bhi = c - abig;
				blo = cdytail - bhi;
				err1 = adxt_cdyt1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				adxt_cdyt0 = (alo * blo) - err3;
				c = (double) (splitter * bdz);
				abig = (double) (c - bdz);
				bhi = c - abig;
				blo = bdz - bhi;
				_i = (double) (adxt_cdyt0 * bdz);
				c = (double) (splitter * adxt_cdyt0);
				abig = (double) (c - adxt_cdyt0);
				ahi = c - abig;
				alo = adxt_cdyt0 - ahi;
				err1 = _i - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				u[0] = (alo * blo) - err3;
				_j = (double) (adxt_cdyt1 * bdz);
				c = (double) (splitter * adxt_cdyt1);
				abig = (double) (c - adxt_cdyt1);
				ahi = c - abig;
				alo = adxt_cdyt1 - ahi;
				err1 = _j - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				_0 = (alo * blo) - err3;
				_k = (double) (_i + _0);
				bvirt = (double) (_k - _i);
				avirt = _k - bvirt;
				bround = _0 - bvirt;
				around = _i - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _k);
				bvirt = u3 - _j;
				u[2] = _k - bvirt;
				u[3] = u3;
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow, 4,
						u, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (bdztail != 0.0) {
					c = (double) (splitter * bdztail);
					abig = (double) (c - bdztail);
					bhi = c - abig;
					blo = bdztail - bhi;
					_i = (double) (adxt_cdyt0 * bdztail);
					c = (double) (splitter * adxt_cdyt0);
					abig = (double) (c - adxt_cdyt0);
					ahi = c - abig;
					alo = adxt_cdyt0 - ahi;
					err1 = _i - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					u[0] = (alo * blo) - err3;
					_j = (double) (adxt_cdyt1 * bdztail);
					c = (double) (splitter * adxt_cdyt1);
					abig = (double) (c - adxt_cdyt1);
					ahi = c - abig;
					alo = adxt_cdyt1 - ahi;
					err1 = _j - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					_0 = (alo * blo) - err3;
					_k = (double) (_i + _0);
					bvirt = (double) (_k - _i);
					avirt = _k - bvirt;
					bround = _0 - bvirt;
					around = _i - avirt;
					u[1] = around + bround;
					u3 = (double) (_j + _k);
					bvirt = u3 - _j;
					u[2] = _k - bvirt;
					u[3] = u3;
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							4, u, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
			}
		}
		if (bdxtail != 0.0) {
			if (cdytail != 0.0) {
				bdxt_cdyt1 = (double) (bdxtail * cdytail);
				c = (double) (splitter * bdxtail);
				abig = (double) (c - bdxtail);
				ahi = c - abig;
				alo = bdxtail - ahi;
				c = (double) (splitter * cdytail);
				abig = (double) (c - cdytail);
				bhi = c - abig;
				blo = cdytail - bhi;
				err1 = bdxt_cdyt1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bdxt_cdyt0 = (alo * blo) - err3;
				c = (double) (splitter * adz);
				abig = (double) (c - adz);
				bhi = c - abig;
				blo = adz - bhi;
				_i = (double) (bdxt_cdyt0 * adz);
				c = (double) (splitter * bdxt_cdyt0);
				abig = (double) (c - bdxt_cdyt0);
				ahi = c - abig;
				alo = bdxt_cdyt0 - ahi;
				err1 = _i - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				u[0] = (alo * blo) - err3;
				_j = (double) (bdxt_cdyt1 * adz);
				c = (double) (splitter * bdxt_cdyt1);
				abig = (double) (c - bdxt_cdyt1);
				ahi = c - abig;
				alo = bdxt_cdyt1 - ahi;
				err1 = _j - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				_0 = (alo * blo) - err3;
				_k = (double) (_i + _0);
				bvirt = (double) (_k - _i);
				avirt = _k - bvirt;
				bround = _0 - bvirt;
				around = _i - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _k);
				bvirt = u3 - _j;
				u[2] = _k - bvirt;
				u[3] = u3;
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow, 4,
						u, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (adztail != 0.0) {
					c = (double) (splitter * adztail);
					abig = (double) (c - adztail);
					bhi = c - abig;
					blo = adztail - bhi;
					_i = (double) (bdxt_cdyt0 * adztail);
					c = (double) (splitter * bdxt_cdyt0);
					abig = (double) (c - bdxt_cdyt0);
					ahi = c - abig;
					alo = bdxt_cdyt0 - ahi;
					err1 = _i - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					u[0] = (alo * blo) - err3;
					_j = (double) (bdxt_cdyt1 * adztail);
					c = (double) (splitter * bdxt_cdyt1);
					abig = (double) (c - bdxt_cdyt1);
					ahi = c - abig;
					alo = bdxt_cdyt1 - ahi;
					err1 = _j - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					_0 = (alo * blo) - err3;
					_k = (double) (_i + _0);
					bvirt = (double) (_k - _i);
					avirt = _k - bvirt;
					bround = _0 - bvirt;
					around = _i - avirt;
					u[1] = around + bround;
					u3 = (double) (_j + _k);
					bvirt = u3 - _j;
					u[2] = _k - bvirt;
					u[3] = u3;
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							4, u, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
			}
			if (adytail != 0.0) {
				negate = -bdxtail;
				bdxt_adyt1 = (double) (negate * adytail);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * adytail);
				abig = (double) (c - adytail);
				bhi = c - abig;
				blo = adytail - bhi;
				err1 = bdxt_adyt1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				bdxt_adyt0 = (alo * blo) - err3;
				c = (double) (splitter * cdz);
				abig = (double) (c - cdz);
				bhi = c - abig;
				blo = cdz - bhi;
				_i = (double) (bdxt_adyt0 * cdz);
				c = (double) (splitter * bdxt_adyt0);
				abig = (double) (c - bdxt_adyt0);
				ahi = c - abig;
				alo = bdxt_adyt0 - ahi;
				err1 = _i - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				u[0] = (alo * blo) - err3;
				_j = (double) (bdxt_adyt1 * cdz);
				c = (double) (splitter * bdxt_adyt1);
				abig = (double) (c - bdxt_adyt1);
				ahi = c - abig;
				alo = bdxt_adyt1 - ahi;
				err1 = _j - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				_0 = (alo * blo) - err3;
				_k = (double) (_i + _0);
				bvirt = (double) (_k - _i);
				avirt = _k - bvirt;
				bround = _0 - bvirt;
				around = _i - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _k);
				bvirt = u3 - _j;
				u[2] = _k - bvirt;
				u[3] = u3;
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow, 4,
						u, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (cdztail != 0.0) {
					c = (double) (splitter * cdztail);
					abig = (double) (c - cdztail);
					bhi = c - abig;
					blo = cdztail - bhi;
					_i = (double) (bdxt_adyt0 * cdztail);
					c = (double) (splitter * bdxt_adyt0);
					abig = (double) (c - bdxt_adyt0);
					ahi = c - abig;
					alo = bdxt_adyt0 - ahi;
					err1 = _i - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					u[0] = (alo * blo) - err3;
					_j = (double) (bdxt_adyt1 * cdztail);
					c = (double) (splitter * bdxt_adyt1);
					abig = (double) (c - bdxt_adyt1);
					ahi = c - abig;
					alo = bdxt_adyt1 - ahi;
					err1 = _j - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					_0 = (alo * blo) - err3;
					_k = (double) (_i + _0);
					bvirt = (double) (_k - _i);
					avirt = _k - bvirt;
					bround = _0 - bvirt;
					around = _i - avirt;
					u[1] = around + bround;
					u3 = (double) (_j + _k);
					bvirt = u3 - _j;
					u[2] = _k - bvirt;
					u[3] = u3;
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							4, u, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
			}
		}
		if (cdxtail != 0.0) {
			if (adytail != 0.0) {
				cdxt_adyt1 = (double) (cdxtail * adytail);
				c = (double) (splitter * cdxtail);
				abig = (double) (c - cdxtail);
				ahi = c - abig;
				alo = cdxtail - ahi;
				c = (double) (splitter * adytail);
				abig = (double) (c - adytail);
				bhi = c - abig;
				blo = adytail - bhi;
				err1 = cdxt_adyt1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				cdxt_adyt0 = (alo * blo) - err3;
				c = (double) (splitter * bdz);
				abig = (double) (c - bdz);
				bhi = c - abig;
				blo = bdz - bhi;
				_i = (double) (cdxt_adyt0 * bdz);
				c = (double) (splitter * cdxt_adyt0);
				abig = (double) (c - cdxt_adyt0);
				ahi = c - abig;
				alo = cdxt_adyt0 - ahi;
				err1 = _i - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				u[0] = (alo * blo) - err3;
				_j = (double) (cdxt_adyt1 * bdz);
				c = (double) (splitter * cdxt_adyt1);
				abig = (double) (c - cdxt_adyt1);
				ahi = c - abig;
				alo = cdxt_adyt1 - ahi;
				err1 = _j - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				_0 = (alo * blo) - err3;
				_k = (double) (_i + _0);
				bvirt = (double) (_k - _i);
				avirt = _k - bvirt;
				bround = _0 - bvirt;
				around = _i - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _k);
				bvirt = u3 - _j;
				u[2] = _k - bvirt;
				u[3] = u3;
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow, 4,
						u, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (bdztail != 0.0) {
					c = (double) (splitter * bdztail);
					abig = (double) (c - bdztail);
					bhi = c - abig;
					blo = bdztail - bhi;
					_i = (double) (cdxt_adyt0 * bdztail);
					c = (double) (splitter * cdxt_adyt0);
					abig = (double) (c - cdxt_adyt0);
					ahi = c - abig;
					alo = cdxt_adyt0 - ahi;
					err1 = _i - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					u[0] = (alo * blo) - err3;
					_j = (double) (cdxt_adyt1 * bdztail);
					c = (double) (splitter * cdxt_adyt1);
					abig = (double) (c - cdxt_adyt1);
					ahi = c - abig;
					alo = cdxt_adyt1 - ahi;
					err1 = _j - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					_0 = (alo * blo) - err3;
					_k = (double) (_i + _0);
					bvirt = (double) (_k - _i);
					avirt = _k - bvirt;
					bround = _0 - bvirt;
					around = _i - avirt;
					u[1] = around + bround;
					u3 = (double) (_j + _k);
					bvirt = u3 - _j;
					u[2] = _k - bvirt;
					u[3] = u3;
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							4, u, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
			}
			if (bdytail != 0.0) {
				negate = -cdxtail;
				cdxt_bdyt1 = (double) (negate * bdytail);
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				ahi = c - abig;
				alo = negate - ahi;
				c = (double) (splitter * bdytail);
				abig = (double) (c - bdytail);
				bhi = c - abig;
				blo = bdytail - bhi;
				err1 = cdxt_bdyt1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				cdxt_bdyt0 = (alo * blo) - err3;
				c = (double) (splitter * adz);
				abig = (double) (c - adz);
				bhi = c - abig;
				blo = adz - bhi;
				_i = (double) (cdxt_bdyt0 * adz);
				c = (double) (splitter * cdxt_bdyt0);
				abig = (double) (c - cdxt_bdyt0);
				ahi = c - abig;
				alo = cdxt_bdyt0 - ahi;
				err1 = _i - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				u[0] = (alo * blo) - err3;
				_j = (double) (cdxt_bdyt1 * adz);
				c = (double) (splitter * cdxt_bdyt1);
				abig = (double) (c - cdxt_bdyt1);
				ahi = c - abig;
				alo = cdxt_bdyt1 - ahi;
				err1 = _j - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				_0 = (alo * blo) - err3;
				_k = (double) (_i + _0);
				bvirt = (double) (_k - _i);
				avirt = _k - bvirt;
				bround = _0 - bvirt;
				around = _i - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _k);
				bvirt = u3 - _j;
				u[2] = _k - bvirt;
				u[3] = u3;
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow, 4,
						u, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (adztail != 0.0) {
					c = (double) (splitter * adztail);
					abig = (double) (c - adztail);
					bhi = c - abig;
					blo = adztail - bhi;
					_i = (double) (cdxt_bdyt0 * adztail);
					c = (double) (splitter * cdxt_bdyt0);
					abig = (double) (c - cdxt_bdyt0);
					ahi = c - abig;
					alo = cdxt_bdyt0 - ahi;
					err1 = _i - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					u[0] = (alo * blo) - err3;
					_j = (double) (cdxt_bdyt1 * adztail);
					c = (double) (splitter * cdxt_bdyt1);
					abig = (double) (c - cdxt_bdyt1);
					ahi = c - abig;
					alo = cdxt_bdyt1 - ahi;
					err1 = _j - (ahi * bhi);
					err2 = err1 - (alo * bhi);
					err3 = err2 - (ahi * blo);
					_0 = (alo * blo) - err3;
					_k = (double) (_i + _0);
					bvirt = (double) (_k - _i);
					avirt = _k - bvirt;
					bround = _0 - bvirt;
					around = _i - avirt;
					u[1] = around + bround;
					u3 = (double) (_j + _k);
					bvirt = u3 - _j;
					u[2] = _k - bvirt;
					u[3] = u3;
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							4, u, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
			}
		}

		if (adztail != 0.0) {
			wlength = _scale_expansion_zeroelim(bctlen, bct, adztail, w);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					wlength, w, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (bdztail != 0.0) {
			wlength = _scale_expansion_zeroelim(catlen, cat, bdztail, w);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					wlength, w, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (cdztail != 0.0) {
			wlength = _scale_expansion_zeroelim(abtlen, abt, cdztail, w);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					wlength, w, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}

		return finnow[finlength - 1];
	}

	/**
	 * Orient tri.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return the double
	 */
	public double orientTri(double[] p0, double[] p1, double[] p2) {
		double detleft, detright, det;
		double detsum, errbound;

		detleft = (p0[0] - p2[0]) * (p1[1] - p2[1]);
		detright = (p0[1] - p2[1]) * (p1[0] - p2[0]);
		det = detleft - detright;

		if (detleft > 0.0) {
			if (detright <= 0.0) {
				return det;
			} else {
				detsum = detleft + detright;
			}
		} else if (detleft < 0.0) {
			if (detright >= 0.0) {
				return det;
			} else {
				detsum = -detleft - detright;
			}
		} else {
			return det;
		}

		errbound = ccwerrboundA * detsum;
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		return _orientTriAdapt(p0, p1, p2, detsum);
	}

	/**
	 * _orient tri adapt.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param detsum the detsum
	 * @return the double
	 */
	private double _orientTriAdapt(double[] p0, double[] p1, double[] p2,
			double detsum) {
		double acx, acy, bcx, bcy;
		double acxtail, acytail, bcxtail, bcytail;
		double detleft, detright;
		double detlefttail, detrighttail;
		double det, errbound;
		double[] B = new double[4], C1 = new double[8], C2 = new double[12], D = new double[16];
		double B3;
		int C1length, C2length, Dlength;
		double[] u = new double[4];
		double u3;
		double s1, t1;
		double s0, t0;

		double bvirt;
		double avirt, bround, around;
		double c;
		double abig;
		double ahi, alo, bhi, blo;
		double err1, err2, err3;
		double _i, _j;
		double _0;

		acx = (double) (p0[0] - p2[0]);
		bcx = (double) (p1[0] - p2[0]);
		acy = (double) (p0[1] - p2[1]);
		bcy = (double) (p1[1] - p2[1]);

		detleft = (double) (acx * bcy);
		c = (double) (splitter * acx);
		abig = (double) (c - acx);
		ahi = c - abig;
		alo = acx - ahi;
		c = (double) (splitter * bcy);
		abig = (double) (c - bcy);
		bhi = c - abig;
		blo = bcy - bhi;
		err1 = detleft - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		detlefttail = (alo * blo) - err3;
		detright = (double) (acy * bcx);
		c = (double) (splitter * acy);
		abig = (double) (c - acy);
		ahi = c - abig;
		alo = acy - ahi;
		c = (double) (splitter * bcx);
		abig = (double) (c - bcx);
		bhi = c - abig;
		blo = bcx - bhi;
		err1 = detright - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		detrighttail = (alo * blo) - err3;

		_i = (double) (detlefttail - detrighttail);
		bvirt = (double) (detlefttail - _i);
		avirt = _i + bvirt;
		bround = bvirt - detrighttail;
		around = detlefttail - avirt;
		B[0] = around + bround;
		_j = (double) (detleft + _i);
		bvirt = (double) (_j - detleft);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = detleft - avirt;
		_0 = around + bround;
		_i = (double) (_0 - detright);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - detright;
		around = _0 - avirt;
		B[1] = around + bround;
		B3 = (double) (_j + _i);
		bvirt = (double) (B3 - _j);
		avirt = B3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		B[2] = around + bround;

		B[3] = B3;

		det = _estimate(4, B);
		errbound = ccwerrboundB * detsum;
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		bvirt = (double) (p0[0] - acx);
		avirt = acx + bvirt;
		bround = bvirt - p2[0];
		around = p0[0] - avirt;
		acxtail = around + bround;
		bvirt = (double) (p1[0] - bcx);
		avirt = bcx + bvirt;
		bround = bvirt - p2[0];
		around = p1[0] - avirt;
		bcxtail = around + bround;
		bvirt = (double) (p0[1] - acy);
		avirt = acy + bvirt;
		bround = bvirt - p2[1];
		around = p0[1] - avirt;
		acytail = around + bround;
		bvirt = (double) (p1[1] - bcy);
		avirt = bcy + bvirt;
		bround = bvirt - p2[1];
		around = p1[1] - avirt;
		bcytail = around + bround;

		if ((acxtail == 0.0) && (acytail == 0.0) && (bcxtail == 0.0)
				&& (bcytail == 0.0)) {
			return det;
		}

		errbound = ccwerrboundC * detsum + resulterrbound
				* ((det) >= 0.0 ? (det) : -(det));
		det += (acx * bcytail + bcy * acxtail)
				- (acy * bcxtail + bcx * acytail);
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		s1 = (double) (acxtail * bcy);
		c = (double) (splitter * acxtail);
		abig = (double) (c - acxtail);
		ahi = c - abig;
		alo = acxtail - ahi;
		c = (double) (splitter * bcy);
		abig = (double) (c - bcy);
		bhi = c - abig;
		blo = bcy - bhi;
		err1 = s1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		s0 = (alo * blo) - err3;
		t1 = (double) (acytail * bcx);
		c = (double) (splitter * acytail);
		abig = (double) (c - acytail);
		ahi = c - abig;
		alo = acytail - ahi;
		c = (double) (splitter * bcx);
		abig = (double) (c - bcx);
		bhi = c - abig;
		blo = bcx - bhi;
		err1 = t1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		t0 = (alo * blo) - err3;
		_i = (double) (s0 - t0);
		bvirt = (double) (s0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - t0;
		around = s0 - avirt;
		u[0] = around + bround;
		_j = (double) (s1 + _i);
		bvirt = (double) (_j - s1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = s1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - t1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - t1;
		around = _0 - avirt;
		u[1] = around + bround;
		u3 = (double) (_j + _i);
		bvirt = (double) (u3 - _j);
		avirt = u3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		u[2] = around + bround;
		u[3] = u3;
		C1length = _fast_expansion_sum_zeroelim(4, B, 4, u, C1);

		s1 = (double) (acx * bcytail);
		c = (double) (splitter * acx);
		abig = (double) (c - acx);
		ahi = c - abig;
		alo = acx - ahi;
		c = (double) (splitter * bcytail);
		abig = (double) (c - bcytail);
		bhi = c - abig;
		blo = bcytail - bhi;
		err1 = s1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		s0 = (alo * blo) - err3;
		t1 = (double) (acy * bcxtail);
		c = (double) (splitter * acy);
		abig = (double) (c - acy);
		ahi = c - abig;
		alo = acy - ahi;
		c = (double) (splitter * bcxtail);
		abig = (double) (c - bcxtail);
		bhi = c - abig;
		blo = bcxtail - bhi;
		err1 = t1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		t0 = (alo * blo) - err3;
		_i = (double) (s0 - t0);
		bvirt = (double) (s0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - t0;
		around = s0 - avirt;
		u[0] = around + bround;
		_j = (double) (s1 + _i);
		bvirt = (double) (_j - s1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = s1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - t1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - t1;
		around = _0 - avirt;
		u[1] = around + bround;
		u3 = (double) (_j + _i);
		bvirt = (double) (u3 - _j);
		avirt = u3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		u[2] = around + bround;
		u[3] = u3;
		C2length = _fast_expansion_sum_zeroelim(C1length, C1, 4, u, C2);

		s1 = (double) (acxtail * bcytail);
		c = (double) (splitter * acxtail);
		abig = (double) (c - acxtail);
		ahi = c - abig;
		alo = acxtail - ahi;
		c = (double) (splitter * bcytail);
		abig = (double) (c - bcytail);
		bhi = c - abig;
		blo = bcytail - bhi;
		err1 = s1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		s0 = (alo * blo) - err3;
		t1 = (double) (acytail * bcxtail);
		c = (double) (splitter * acytail);
		abig = (double) (c - acytail);
		ahi = c - abig;
		alo = acytail - ahi;
		c = (double) (splitter * bcxtail);
		abig = (double) (c - bcxtail);
		bhi = c - abig;
		blo = bcxtail - bhi;
		err1 = t1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		t0 = (alo * blo) - err3;
		_i = (double) (s0 - t0);
		bvirt = (double) (s0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - t0;
		around = s0 - avirt;
		u[0] = around + bround;
		_j = (double) (s1 + _i);
		bvirt = (double) (_j - s1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = s1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - t1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - t1;
		around = _0 - avirt;
		u[1] = around + bround;
		u3 = (double) (_j + _i);
		bvirt = (double) (u3 - _j);
		avirt = u3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		u[2] = around + bround;
		u[3] = u3;
		Dlength = _fast_expansion_sum_zeroelim(C2length, C2, 4, u, D);

		return (D[Dlength - 1]);
	}

	/**
	 * Insphere tetra.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param p3 the p3
	 * @param q the q
	 * @return the w b_ classify
	 */
	public WB_Classify insphereTetra(double[] p0, double[] p1, double[] p2,
			double[] p3, double[] q) {
		double orient = orientTetra(p0, p1, p2, p3);
		if (orient == 0) {
			return WB_Classify.COPLANAR;
		}

		double result = _insphereTetra(p0, p1, p2, p3, q);

		if (result == 0)
			return WB_Classify.ON;
		if (Math.signum(result) * Math.signum(orient) < 0)// Signum for faster
															// multiplication
			return WB_Classify.OUTSIDE;
		else
			return WB_Classify.INSIDE;
	}

	/**
	 * _insphere tetra.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @param pe the pe
	 * @return the double
	 */
	private double _insphereTetra(double[] pa, double[] pb, double[] pc,
			double[] pd, double[] pe) {
		double aex, bex, cex, dex;
		double aey, bey, cey, dey;
		double aez, bez, cez, dez;
		double aexbey, bexaey, bexcey, cexbey, cexdey, dexcey, dexaey, aexdey;
		double aexcey, cexaey, bexdey, dexbey;
		double alift, blift, clift, dlift;
		double ab, bc, cd, da, ac, bd;
		double abc, bcd, cda, dab;
		double aezplus, bezplus, cezplus, dezplus;
		double aexbeyplus, bexaeyplus, bexceyplus, cexbeyplus;
		double cexdeyplus, dexceyplus, dexaeyplus, aexdeyplus;
		double aexceyplus, cexaeyplus, bexdeyplus, dexbeyplus;
		double det;
		double permanent, errbound;

		aex = pa[0] - pe[0];
		bex = pb[0] - pe[0];
		cex = pc[0] - pe[0];
		dex = pd[0] - pe[0];
		aey = pa[1] - pe[1];
		bey = pb[1] - pe[1];
		cey = pc[1] - pe[1];
		dey = pd[1] - pe[1];
		aez = pa[2] - pe[2];
		bez = pb[2] - pe[2];
		cez = pc[2] - pe[2];
		dez = pd[2] - pe[2];

		aexbey = aex * bey;
		bexaey = bex * aey;
		ab = aexbey - bexaey;
		bexcey = bex * cey;
		cexbey = cex * bey;
		bc = bexcey - cexbey;
		cexdey = cex * dey;
		dexcey = dex * cey;
		cd = cexdey - dexcey;
		dexaey = dex * aey;
		aexdey = aex * dey;
		da = dexaey - aexdey;

		aexcey = aex * cey;
		cexaey = cex * aey;
		ac = aexcey - cexaey;
		bexdey = bex * dey;
		dexbey = dex * bey;
		bd = bexdey - dexbey;

		abc = aez * bc - bez * ac + cez * ab;
		bcd = bez * cd - cez * bd + dez * bc;
		cda = cez * da + dez * ac + aez * cd;
		dab = dez * ab + aez * bd + bez * da;

		alift = aex * aex + aey * aey + aez * aez;
		blift = bex * bex + bey * bey + bez * bez;
		clift = cex * cex + cey * cey + cez * cez;
		dlift = dex * dex + dey * dey + dez * dez;

		det = (dlift * abc - clift * dab) + (blift * cda - alift * bcd);

		aezplus = ((aez) >= 0.0 ? (aez) : -(aez));
		bezplus = ((bez) >= 0.0 ? (bez) : -(bez));
		cezplus = ((cez) >= 0.0 ? (cez) : -(cez));
		dezplus = ((dez) >= 0.0 ? (dez) : -(dez));
		aexbeyplus = ((aexbey) >= 0.0 ? (aexbey) : -(aexbey));
		bexaeyplus = ((bexaey) >= 0.0 ? (bexaey) : -(bexaey));
		bexceyplus = ((bexcey) >= 0.0 ? (bexcey) : -(bexcey));
		cexbeyplus = ((cexbey) >= 0.0 ? (cexbey) : -(cexbey));
		cexdeyplus = ((cexdey) >= 0.0 ? (cexdey) : -(cexdey));
		dexceyplus = ((dexcey) >= 0.0 ? (dexcey) : -(dexcey));
		dexaeyplus = ((dexaey) >= 0.0 ? (dexaey) : -(dexaey));
		aexdeyplus = ((aexdey) >= 0.0 ? (aexdey) : -(aexdey));
		aexceyplus = ((aexcey) >= 0.0 ? (aexcey) : -(aexcey));
		cexaeyplus = ((cexaey) >= 0.0 ? (cexaey) : -(cexaey));
		bexdeyplus = ((bexdey) >= 0.0 ? (bexdey) : -(bexdey));
		dexbeyplus = ((dexbey) >= 0.0 ? (dexbey) : -(dexbey));
		permanent = ((cexdeyplus + dexceyplus) * bezplus
				+ (dexbeyplus + bexdeyplus) * cezplus + (bexceyplus + cexbeyplus)
				* dezplus)
				* alift
				+ ((dexaeyplus + aexdeyplus) * cezplus
						+ (aexceyplus + cexaeyplus) * dezplus + (cexdeyplus + dexceyplus)
						* aezplus)
				* blift
				+ ((aexbeyplus + bexaeyplus) * dezplus
						+ (bexdeyplus + dexbeyplus) * aezplus + (dexaeyplus + aexdeyplus)
						* bezplus)
				* clift
				+ ((bexceyplus + cexbeyplus) * aezplus
						+ (cexaeyplus + aexceyplus) * bezplus + (aexbeyplus + bexaeyplus)
						* cezplus) * dlift;
		errbound = isperrboundA * permanent;
		if ((det > errbound) || (-det > errbound)) {
			return det;
		}

		return _insphereTetraAdapt(pa, pb, pc, pd, pe, permanent);
	}

	/**
	 * _insphere tetra adapt.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @param pe the pe
	 * @param permanent the permanent
	 * @return the double
	 */
	private double _insphereTetraAdapt(double[] pa, double[] pb, double[] pc,
			double[] pd, double[] pe, double permanent)

	{
		double aex, bex, cex, dex, aey, bey, cey, dey, aez, bez, cez, dez;
		double det, errbound;

		double aexbey1, bexaey1, bexcey1, cexbey1;
		double cexdey1, dexcey1, dexaey1, aexdey1;
		double aexcey1, cexaey1, bexdey1, dexbey1;
		double aexbey0, bexaey0, bexcey0, cexbey0;
		double cexdey0, dexcey0, dexaey0, aexdey0;
		double aexcey0, cexaey0, bexdey0, dexbey0;
		double[] ab = new double[4], bc = new double[4], cd = new double[4], da = new double[4], ac = new double[4], bd = new double[4];
		double ab3, bc3, cd3, da3, ac3, bd3;
		double abeps, bceps, cdeps, daeps, aceps, bdeps;
		double[] temp8a = new double[8], temp8b = new double[8], temp8c = new double[8], temp16 = new double[16], temp24 = new double[24], temp48 = new double[48];
		int temp8alen, temp8blen, temp8clen, temp16len, temp24len, temp48len;
		double[] xdet = new double[96], ydet = new double[96], zdet = new double[96], xydet = new double[192];
		int xlen, ylen, zlen, xylen;
		double[] adet = new double[288], bdet = new double[288], cdet = new double[288], ddet = new double[288];
		int alen, blen, clen, dlen;
		double[] abdet = new double[576], cddet = new double[576];
		int ablen, cdlen;
		double[] fin1 = new double[1152];
		int finlength;

		double aextail, bextail, cextail, dextail;
		double aeytail, beytail, ceytail, deytail;
		double aeztail, beztail, ceztail, deztail;

		double bvirt;
		double avirt, bround, around;
		double c;
		double abig;
		double ahi, alo, bhi, blo;
		double err1, err2, err3;
		double _i, _j;
		double _0;

		aex = (double) (pa[0] - pe[0]);
		bex = (double) (pb[0] - pe[0]);
		cex = (double) (pc[0] - pe[0]);
		dex = (double) (pd[0] - pe[0]);
		aey = (double) (pa[1] - pe[1]);
		bey = (double) (pb[1] - pe[1]);
		cey = (double) (pc[1] - pe[1]);
		dey = (double) (pd[1] - pe[1]);
		aez = (double) (pa[2] - pe[2]);
		bez = (double) (pb[2] - pe[2]);
		cez = (double) (pc[2] - pe[2]);
		dez = (double) (pd[2] - pe[2]);

		aexbey1 = (double) (aex * bey);
		c = (double) (splitter * aex);
		abig = (double) (c - aex);
		ahi = c - abig;
		alo = aex - ahi;
		c = (double) (splitter * bey);
		abig = (double) (c - bey);
		bhi = c - abig;
		blo = bey - bhi;
		err1 = aexbey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		aexbey0 = (alo * blo) - err3;
		bexaey1 = (double) (bex * aey);
		c = (double) (splitter * bex);
		abig = (double) (c - bex);
		ahi = c - abig;
		alo = bex - ahi;
		c = (double) (splitter * aey);
		abig = (double) (c - aey);
		bhi = c - abig;
		blo = aey - bhi;
		err1 = bexaey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bexaey0 = (alo * blo) - err3;
		_i = (double) (aexbey0 - bexaey0);
		bvirt = (double) (aexbey0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bexaey0;
		around = aexbey0 - avirt;
		ab[0] = around + bround;
		_j = (double) (aexbey1 + _i);
		bvirt = (double) (_j - aexbey1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = aexbey1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - bexaey1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bexaey1;
		around = _0 - avirt;
		ab[1] = around + bround;
		ab3 = (double) (_j + _i);
		bvirt = (double) (ab3 - _j);
		avirt = ab3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ab[2] = around + bround;
		ab[3] = ab3;

		bexcey1 = (double) (bex * cey);
		c = (double) (splitter * bex);
		abig = (double) (c - bex);
		ahi = c - abig;
		alo = bex - ahi;
		c = (double) (splitter * cey);
		abig = (double) (c - cey);
		bhi = c - abig;
		blo = cey - bhi;
		err1 = bexcey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bexcey0 = (alo * blo) - err3;
		cexbey1 = (double) (cex * bey);
		c = (double) (splitter * cex);
		abig = (double) (c - cex);
		ahi = c - abig;
		alo = cex - ahi;
		c = (double) (splitter * bey);
		abig = (double) (c - bey);
		bhi = c - abig;
		blo = bey - bhi;
		err1 = cexbey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cexbey0 = (alo * blo) - err3;
		_i = (double) (bexcey0 - cexbey0);
		bvirt = (double) (bexcey0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cexbey0;
		around = bexcey0 - avirt;
		bc[0] = around + bround;
		_j = (double) (bexcey1 + _i);
		bvirt = (double) (_j - bexcey1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = bexcey1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - cexbey1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cexbey1;
		around = _0 - avirt;
		bc[1] = around + bround;
		bc3 = (double) (_j + _i);
		bvirt = (double) (bc3 - _j);
		avirt = bc3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		bc[2] = around + bround;
		bc[3] = bc3;

		cexdey1 = (double) (cex * dey);
		c = (double) (splitter * cex);
		abig = (double) (c - cex);
		ahi = c - abig;
		alo = cex - ahi;
		c = (double) (splitter * dey);
		abig = (double) (c - dey);
		bhi = c - abig;
		blo = dey - bhi;
		err1 = cexdey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cexdey0 = (alo * blo) - err3;
		dexcey1 = (double) (dex * cey);
		c = (double) (splitter * dex);
		abig = (double) (c - dex);
		ahi = c - abig;
		alo = dex - ahi;
		c = (double) (splitter * cey);
		abig = (double) (c - cey);
		bhi = c - abig;
		blo = cey - bhi;
		err1 = dexcey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		dexcey0 = (alo * blo) - err3;
		_i = (double) (cexdey0 - dexcey0);
		bvirt = (double) (cexdey0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - dexcey0;
		around = cexdey0 - avirt;
		cd[0] = around + bround;
		_j = (double) (cexdey1 + _i);
		bvirt = (double) (_j - cexdey1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = cexdey1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - dexcey1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - dexcey1;
		around = _0 - avirt;
		cd[1] = around + bround;
		cd3 = (double) (_j + _i);
		bvirt = (double) (cd3 - _j);
		avirt = cd3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		cd[2] = around + bround;
		cd[3] = cd3;

		dexaey1 = (double) (dex * aey);
		c = (double) (splitter * dex);
		abig = (double) (c - dex);
		ahi = c - abig;
		alo = dex - ahi;
		c = (double) (splitter * aey);
		abig = (double) (c - aey);
		bhi = c - abig;
		blo = aey - bhi;
		err1 = dexaey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		dexaey0 = (alo * blo) - err3;
		aexdey1 = (double) (aex * dey);
		c = (double) (splitter * aex);
		abig = (double) (c - aex);
		ahi = c - abig;
		alo = aex - ahi;
		c = (double) (splitter * dey);
		abig = (double) (c - dey);
		bhi = c - abig;
		blo = dey - bhi;
		err1 = aexdey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		aexdey0 = (alo * blo) - err3;
		_i = (double) (dexaey0 - aexdey0);
		bvirt = (double) (dexaey0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - aexdey0;
		around = dexaey0 - avirt;
		da[0] = around + bround;
		_j = (double) (dexaey1 + _i);
		bvirt = (double) (_j - dexaey1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = dexaey1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - aexdey1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - aexdey1;
		around = _0 - avirt;
		da[1] = around + bround;
		da3 = (double) (_j + _i);
		bvirt = (double) (da3 - _j);
		avirt = da3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		da[2] = around + bround;
		da[3] = da3;

		aexcey1 = (double) (aex * cey);
		c = (double) (splitter * aex);
		abig = (double) (c - aex);
		ahi = c - abig;
		alo = aex - ahi;
		c = (double) (splitter * cey);
		abig = (double) (c - cey);
		bhi = c - abig;
		blo = cey - bhi;
		err1 = aexcey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		aexcey0 = (alo * blo) - err3;
		cexaey1 = (double) (cex * aey);
		c = (double) (splitter * cex);
		abig = (double) (c - cex);
		ahi = c - abig;
		alo = cex - ahi;
		c = (double) (splitter * aey);
		abig = (double) (c - aey);
		bhi = c - abig;
		blo = aey - bhi;
		err1 = cexaey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cexaey0 = (alo * blo) - err3;
		_i = (double) (aexcey0 - cexaey0);
		bvirt = (double) (aexcey0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cexaey0;
		around = aexcey0 - avirt;
		ac[0] = around + bround;
		_j = (double) (aexcey1 + _i);
		bvirt = (double) (_j - aexcey1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = aexcey1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - cexaey1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cexaey1;
		around = _0 - avirt;
		ac[1] = around + bround;
		ac3 = (double) (_j + _i);
		bvirt = (double) (ac3 - _j);
		avirt = ac3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ac[2] = around + bround;
		ac[3] = ac3;

		bexdey1 = (double) (bex * dey);
		c = (double) (splitter * bex);
		abig = (double) (c - bex);
		ahi = c - abig;
		alo = bex - ahi;
		c = (double) (splitter * dey);
		abig = (double) (c - dey);
		bhi = c - abig;
		blo = dey - bhi;
		err1 = bexdey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bexdey0 = (alo * blo) - err3;
		dexbey1 = (double) (dex * bey);
		c = (double) (splitter * dex);
		abig = (double) (c - dex);
		ahi = c - abig;
		alo = dex - ahi;
		c = (double) (splitter * bey);
		abig = (double) (c - bey);
		bhi = c - abig;
		blo = bey - bhi;
		err1 = dexbey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		dexbey0 = (alo * blo) - err3;
		_i = (double) (bexdey0 - dexbey0);
		bvirt = (double) (bexdey0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - dexbey0;
		around = bexdey0 - avirt;
		bd[0] = around + bround;
		_j = (double) (bexdey1 + _i);
		bvirt = (double) (_j - bexdey1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = bexdey1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - dexbey1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - dexbey1;
		around = _0 - avirt;
		bd[1] = around + bround;
		bd3 = (double) (_j + _i);
		bvirt = (double) (bd3 - _j);
		avirt = bd3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		bd[2] = around + bround;
		bd[3] = bd3;

		temp8alen = _scale_expansion_zeroelim(4, cd, bez, temp8a);
		temp8blen = _scale_expansion_zeroelim(4, bd, -cez, temp8b);
		temp8clen = _scale_expansion_zeroelim(4, bc, dez, temp8c);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp24len = _fast_expansion_sum_zeroelim(temp8clen, temp8c, temp16len,
				temp16, temp24);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, aex, temp48);
		xlen = _scale_expansion_zeroelim(temp48len, temp48, -aex, xdet);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, aey, temp48);
		ylen = _scale_expansion_zeroelim(temp48len, temp48, -aey, ydet);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, aez, temp48);
		zlen = _scale_expansion_zeroelim(temp48len, temp48, -aez, zdet);
		xylen = _fast_expansion_sum_zeroelim(xlen, xdet, ylen, ydet, xydet);
		alen = _fast_expansion_sum_zeroelim(xylen, xydet, zlen, zdet, adet);

		temp8alen = _scale_expansion_zeroelim(4, da, cez, temp8a);
		temp8blen = _scale_expansion_zeroelim(4, ac, dez, temp8b);
		temp8clen = _scale_expansion_zeroelim(4, cd, aez, temp8c);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp24len = _fast_expansion_sum_zeroelim(temp8clen, temp8c, temp16len,
				temp16, temp24);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, bex, temp48);
		xlen = _scale_expansion_zeroelim(temp48len, temp48, bex, xdet);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, bey, temp48);
		ylen = _scale_expansion_zeroelim(temp48len, temp48, bey, ydet);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, bez, temp48);
		zlen = _scale_expansion_zeroelim(temp48len, temp48, bez, zdet);
		xylen = _fast_expansion_sum_zeroelim(xlen, xdet, ylen, ydet, xydet);
		blen = _fast_expansion_sum_zeroelim(xylen, xydet, zlen, zdet, bdet);

		temp8alen = _scale_expansion_zeroelim(4, ab, dez, temp8a);
		temp8blen = _scale_expansion_zeroelim(4, bd, aez, temp8b);
		temp8clen = _scale_expansion_zeroelim(4, da, bez, temp8c);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp24len = _fast_expansion_sum_zeroelim(temp8clen, temp8c, temp16len,
				temp16, temp24);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, cex, temp48);
		xlen = _scale_expansion_zeroelim(temp48len, temp48, -cex, xdet);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, cey, temp48);
		ylen = _scale_expansion_zeroelim(temp48len, temp48, -cey, ydet);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, cez, temp48);
		zlen = _scale_expansion_zeroelim(temp48len, temp48, -cez, zdet);
		xylen = _fast_expansion_sum_zeroelim(xlen, xdet, ylen, ydet, xydet);
		clen = _fast_expansion_sum_zeroelim(xylen, xydet, zlen, zdet, cdet);

		temp8alen = _scale_expansion_zeroelim(4, bc, aez, temp8a);
		temp8blen = _scale_expansion_zeroelim(4, ac, -bez, temp8b);
		temp8clen = _scale_expansion_zeroelim(4, ab, cez, temp8c);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp24len = _fast_expansion_sum_zeroelim(temp8clen, temp8c, temp16len,
				temp16, temp24);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, dex, temp48);
		xlen = _scale_expansion_zeroelim(temp48len, temp48, dex, xdet);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, dey, temp48);
		ylen = _scale_expansion_zeroelim(temp48len, temp48, dey, ydet);
		temp48len = _scale_expansion_zeroelim(temp24len, temp24, dez, temp48);
		zlen = _scale_expansion_zeroelim(temp48len, temp48, dez, zdet);
		xylen = _fast_expansion_sum_zeroelim(xlen, xdet, ylen, ydet, xydet);
		dlen = _fast_expansion_sum_zeroelim(xylen, xydet, zlen, zdet, ddet);

		ablen = _fast_expansion_sum_zeroelim(alen, adet, blen, bdet, abdet);
		cdlen = _fast_expansion_sum_zeroelim(clen, cdet, dlen, ddet, cddet);
		finlength = _fast_expansion_sum_zeroelim(ablen, abdet, cdlen, cddet,
				fin1);

		det = _estimate(finlength, fin1);
		errbound = isperrboundB * permanent;
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		bvirt = (double) (pa[0] - aex);
		avirt = aex + bvirt;
		bround = bvirt - pe[0];
		around = pa[0] - avirt;
		aextail = around + bround;
		bvirt = (double) (pa[1] - aey);
		avirt = aey + bvirt;
		bround = bvirt - pe[1];
		around = pa[1] - avirt;
		aeytail = around + bround;
		bvirt = (double) (pa[2] - aez);
		avirt = aez + bvirt;
		bround = bvirt - pe[2];
		around = pa[2] - avirt;
		aeztail = around + bround;
		bvirt = (double) (pb[0] - bex);
		avirt = bex + bvirt;
		bround = bvirt - pe[0];
		around = pb[0] - avirt;
		bextail = around + bround;
		bvirt = (double) (pb[1] - bey);
		avirt = bey + bvirt;
		bround = bvirt - pe[1];
		around = pb[1] - avirt;
		beytail = around + bround;
		bvirt = (double) (pb[2] - bez);
		avirt = bez + bvirt;
		bround = bvirt - pe[2];
		around = pb[2] - avirt;
		beztail = around + bround;
		bvirt = (double) (pc[0] - cex);
		avirt = cex + bvirt;
		bround = bvirt - pe[0];
		around = pc[0] - avirt;
		cextail = around + bround;
		bvirt = (double) (pc[1] - cey);
		avirt = cey + bvirt;
		bround = bvirt - pe[1];
		around = pc[1] - avirt;
		ceytail = around + bround;
		bvirt = (double) (pc[2] - cez);
		avirt = cez + bvirt;
		bround = bvirt - pe[2];
		around = pc[2] - avirt;
		ceztail = around + bround;
		bvirt = (double) (pd[0] - dex);
		avirt = dex + bvirt;
		bround = bvirt - pe[0];
		around = pd[0] - avirt;
		dextail = around + bround;
		bvirt = (double) (pd[1] - dey);
		avirt = dey + bvirt;
		bround = bvirt - pe[1];
		around = pd[1] - avirt;
		deytail = around + bround;
		bvirt = (double) (pd[2] - dez);
		avirt = dez + bvirt;
		bround = bvirt - pe[2];
		around = pd[2] - avirt;
		deztail = around + bround;
		if ((aextail == 0.0) && (aeytail == 0.0) && (aeztail == 0.0)
				&& (bextail == 0.0) && (beytail == 0.0) && (beztail == 0.0)
				&& (cextail == 0.0) && (ceytail == 0.0) && (ceztail == 0.0)
				&& (dextail == 0.0) && (deytail == 0.0) && (deztail == 0.0)) {
			return det;
		}

		errbound = isperrboundC * permanent + resulterrbound
				* ((det) >= 0.0 ? (det) : -(det));
		abeps = (aex * beytail + bey * aextail)
				- (aey * bextail + bex * aeytail);
		bceps = (bex * ceytail + cey * bextail)
				- (bey * cextail + cex * beytail);
		cdeps = (cex * deytail + dey * cextail)
				- (cey * dextail + dex * ceytail);
		daeps = (dex * aeytail + aey * dextail)
				- (dey * aextail + aex * deytail);
		aceps = (aex * ceytail + cey * aextail)
				- (aey * cextail + cex * aeytail);
		bdeps = (bex * deytail + dey * bextail)
				- (bey * dextail + dex * beytail);
		det += (((bex * bex + bey * bey + bez * bez)
				* ((cez * daeps + dez * aceps + aez * cdeps) + (ceztail * da3
						+ deztail * ac3 + aeztail * cd3)) + (dex * dex + dey
				* dey + dez * dez)
				* ((aez * bceps - bez * aceps + cez * abeps) + (aeztail * bc3
						- beztail * ac3 + ceztail * ab3))) - ((aex * aex + aey
				* aey + aez * aez)
				* ((bez * cdeps - cez * bdeps + dez * bceps) + (beztail * cd3
						- ceztail * bd3 + deztail * bc3)) + (cex * cex + cey
				* cey + cez * cez)
				* ((dez * abeps + aez * bdeps + bez * daeps) + (deztail * ab3
						+ aeztail * bd3 + beztail * da3))))
				+ 2.0
				* (((bex * bextail + bey * beytail + bez * beztail)
						* (cez * da3 + dez * ac3 + aez * cd3) + (dex * dextail
						+ dey * deytail + dez * deztail)
						* (aez * bc3 - bez * ac3 + cez * ab3)) - ((aex
						* aextail + aey * aeytail + aez * aeztail)
						* (bez * cd3 - cez * bd3 + dez * bc3) + (cex * cextail
						+ cey * ceytail + cez * ceztail)
						* (dez * ab3 + aez * bd3 + bez * da3)));
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		return _insphereTetraExact(pa, pb, pc, pd, pe);
	}

	/**
	 * _insphere tetra exact.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @param pe the pe
	 * @return the double
	 */
	private double _insphereTetraExact(double[] pa, double[] pb, double[] pc,
			double[] pd, double[] pe) {
		double axby1, bxcy1, cxdy1, dxey1, exay1;
		double bxay1, cxby1, dxcy1, exdy1, axey1;
		double axcy1, bxdy1, cxey1, dxay1, exby1;
		double cxay1, dxby1, excy1, axdy1, bxey1;
		double axby0, bxcy0, cxdy0, dxey0, exay0;
		double bxay0, cxby0, dxcy0, exdy0, axey0;
		double axcy0, bxdy0, cxey0, dxay0, exby0;
		double cxay0, dxby0, excy0, axdy0, bxey0;
		double[] ab = new double[4], bc = new double[4], cd = new double[4], de = new double[4], ea = new double[4];
		double[] ac = new double[4], bd = new double[4], ce = new double[4], da = new double[4], eb = new double[4];
		double[] temp8a = new double[8], temp8b = new double[8], temp16 = new double[16];
		int temp8alen, temp8blen, temp16len;
		double[] abc = new double[24], bcd = new double[24], cde = new double[24], dea = new double[24], eab = new double[24];
		double[] abd = new double[24], bce = new double[24], cda = new double[24], deb = new double[24], eac = new double[24];
		int abclen, bcdlen, cdelen, dealen, eablen;
		int abdlen, bcelen, cdalen, deblen, eaclen;
		double[] temp48a = new double[48], temp48b = new double[48];
		int temp48alen, temp48blen;
		double[] abcd = new double[96], bcde = new double[96], cdea = new double[96], deab = new double[96], eabc = new double[96];
		int abcdlen, bcdelen, cdealen, deablen, eabclen;
		double[] temp192 = new double[192];
		double[] det384x = new double[384], det384y = new double[384], det384z = new double[384];
		int xlen, ylen, zlen;
		double[] detxy = new double[768];
		int xylen;
		double[] adet = new double[1152], bdet = new double[1152], cdet = new double[1152], ddet = new double[1152], edet = new double[1152];
		int alen, blen, clen, dlen, elen;
		double[] abdet = new double[2304], cddet = new double[2304], cdedet = new double[3456];
		int ablen, cdlen;
		double[] deter = new double[5760];
		int deterlen;
		int i;

		double bvirt;
		double avirt, bround, around;
		double c;
		double abig;
		double ahi, alo, bhi, blo;
		double err1, err2, err3;
		double _i, _j;
		double _0;

		axby1 = (double) (pa[0] * pb[1]);
		c = (double) (splitter * pa[0]);
		abig = (double) (c - pa[0]);
		ahi = c - abig;
		alo = pa[0] - ahi;
		c = (double) (splitter * pb[1]);
		abig = (double) (c - pb[1]);
		bhi = c - abig;
		blo = pb[1] - bhi;
		err1 = axby1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		axby0 = (alo * blo) - err3;
		bxay1 = (double) (pb[0] * pa[1]);
		c = (double) (splitter * pb[0]);
		abig = (double) (c - pb[0]);
		ahi = c - abig;
		alo = pb[0] - ahi;
		c = (double) (splitter * pa[1]);
		abig = (double) (c - pa[1]);
		bhi = c - abig;
		blo = pa[1] - bhi;
		err1 = bxay1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bxay0 = (alo * blo) - err3;
		_i = (double) (axby0 - bxay0);
		bvirt = (double) (axby0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bxay0;
		around = axby0 - avirt;
		ab[0] = around + bround;
		_j = (double) (axby1 + _i);
		bvirt = (double) (_j - axby1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = axby1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - bxay1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bxay1;
		around = _0 - avirt;
		ab[1] = around + bround;
		ab[3] = (double) (_j + _i);
		bvirt = (double) (ab[3] - _j);
		avirt = ab[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ab[2] = around + bround;

		bxcy1 = (double) (pb[0] * pc[1]);
		c = (double) (splitter * pb[0]);
		abig = (double) (c - pb[0]);
		ahi = c - abig;
		alo = pb[0] - ahi;
		c = (double) (splitter * pc[1]);
		abig = (double) (c - pc[1]);
		bhi = c - abig;
		blo = pc[1] - bhi;
		err1 = bxcy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bxcy0 = (alo * blo) - err3;
		cxby1 = (double) (pc[0] * pb[1]);
		c = (double) (splitter * pc[0]);
		abig = (double) (c - pc[0]);
		ahi = c - abig;
		alo = pc[0] - ahi;
		c = (double) (splitter * pb[1]);
		abig = (double) (c - pb[1]);
		bhi = c - abig;
		blo = pb[1] - bhi;
		err1 = cxby1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cxby0 = (alo * blo) - err3;
		_i = (double) (bxcy0 - cxby0);
		bvirt = (double) (bxcy0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cxby0;
		around = bxcy0 - avirt;
		bc[0] = around + bround;
		_j = (double) (bxcy1 + _i);
		bvirt = (double) (_j - bxcy1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = bxcy1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - cxby1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cxby1;
		around = _0 - avirt;
		bc[1] = around + bround;
		bc[3] = (double) (_j + _i);
		bvirt = (double) (bc[3] - _j);
		avirt = bc[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		bc[2] = around + bround;

		cxdy1 = (double) (pc[0] * pd[1]);
		c = (double) (splitter * pc[0]);
		abig = (double) (c - pc[0]);
		ahi = c - abig;
		alo = pc[0] - ahi;
		c = (double) (splitter * pd[1]);
		abig = (double) (c - pd[1]);
		bhi = c - abig;
		blo = pd[1] - bhi;
		err1 = cxdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cxdy0 = (alo * blo) - err3;
		dxcy1 = (double) (pd[0] * pc[1]);
		c = (double) (splitter * pd[0]);
		abig = (double) (c - pd[0]);
		ahi = c - abig;
		alo = pd[0] - ahi;
		c = (double) (splitter * pc[1]);
		abig = (double) (c - pc[1]);
		bhi = c - abig;
		blo = pc[1] - bhi;
		err1 = dxcy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		dxcy0 = (alo * blo) - err3;
		_i = (double) (cxdy0 - dxcy0);
		bvirt = (double) (cxdy0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - dxcy0;
		around = cxdy0 - avirt;
		cd[0] = around + bround;
		_j = (double) (cxdy1 + _i);
		bvirt = (double) (_j - cxdy1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = cxdy1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - dxcy1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - dxcy1;
		around = _0 - avirt;
		cd[1] = around + bround;
		cd[3] = (double) (_j + _i);
		bvirt = (double) (cd[3] - _j);
		avirt = cd[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		cd[2] = around + bround;

		dxey1 = (double) (pd[0] * pe[1]);
		c = (double) (splitter * pd[0]);
		abig = (double) (c - pd[0]);
		ahi = c - abig;
		alo = pd[0] - ahi;
		c = (double) (splitter * pe[1]);
		abig = (double) (c - pe[1]);
		bhi = c - abig;
		blo = pe[1] - bhi;
		err1 = dxey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		dxey0 = (alo * blo) - err3;
		exdy1 = (double) (pe[0] * pd[1]);
		c = (double) (splitter * pe[0]);
		abig = (double) (c - pe[0]);
		ahi = c - abig;
		alo = pe[0] - ahi;
		c = (double) (splitter * pd[1]);
		abig = (double) (c - pd[1]);
		bhi = c - abig;
		blo = pd[1] - bhi;
		err1 = exdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		exdy0 = (alo * blo) - err3;
		_i = (double) (dxey0 - exdy0);
		bvirt = (double) (dxey0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - exdy0;
		around = dxey0 - avirt;
		de[0] = around + bround;
		_j = (double) (dxey1 + _i);
		bvirt = (double) (_j - dxey1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = dxey1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - exdy1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - exdy1;
		around = _0 - avirt;
		de[1] = around + bround;
		de[3] = (double) (_j + _i);
		bvirt = (double) (de[3] - _j);
		avirt = de[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		de[2] = around + bround;

		exay1 = (double) (pe[0] * pa[1]);
		c = (double) (splitter * pe[0]);
		abig = (double) (c - pe[0]);
		ahi = c - abig;
		alo = pe[0] - ahi;
		c = (double) (splitter * pa[1]);
		abig = (double) (c - pa[1]);
		bhi = c - abig;
		blo = pa[1] - bhi;
		err1 = exay1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		exay0 = (alo * blo) - err3;
		axey1 = (double) (pa[0] * pe[1]);
		c = (double) (splitter * pa[0]);
		abig = (double) (c - pa[0]);
		ahi = c - abig;
		alo = pa[0] - ahi;
		c = (double) (splitter * pe[1]);
		abig = (double) (c - pe[1]);
		bhi = c - abig;
		blo = pe[1] - bhi;
		err1 = axey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		axey0 = (alo * blo) - err3;
		_i = (double) (exay0 - axey0);
		bvirt = (double) (exay0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - axey0;
		around = exay0 - avirt;
		ea[0] = around + bround;
		_j = (double) (exay1 + _i);
		bvirt = (double) (_j - exay1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = exay1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - axey1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - axey1;
		around = _0 - avirt;
		ea[1] = around + bround;
		ea[3] = (double) (_j + _i);
		bvirt = (double) (ea[3] - _j);
		avirt = ea[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ea[2] = around + bround;

		axcy1 = (double) (pa[0] * pc[1]);
		c = (double) (splitter * pa[0]);
		abig = (double) (c - pa[0]);
		ahi = c - abig;
		alo = pa[0] - ahi;
		c = (double) (splitter * pc[1]);
		abig = (double) (c - pc[1]);
		bhi = c - abig;
		blo = pc[1] - bhi;
		err1 = axcy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		axcy0 = (alo * blo) - err3;
		cxay1 = (double) (pc[0] * pa[1]);
		c = (double) (splitter * pc[0]);
		abig = (double) (c - pc[0]);
		ahi = c - abig;
		alo = pc[0] - ahi;
		c = (double) (splitter * pa[1]);
		abig = (double) (c - pa[1]);
		bhi = c - abig;
		blo = pa[1] - bhi;
		err1 = cxay1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cxay0 = (alo * blo) - err3;
		_i = (double) (axcy0 - cxay0);
		bvirt = (double) (axcy0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cxay0;
		around = axcy0 - avirt;
		ac[0] = around + bround;
		_j = (double) (axcy1 + _i);
		bvirt = (double) (_j - axcy1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = axcy1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - cxay1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cxay1;
		around = _0 - avirt;
		ac[1] = around + bround;
		ac[3] = (double) (_j + _i);
		bvirt = (double) (ac[3] - _j);
		avirt = ac[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ac[2] = around + bround;

		bxdy1 = (double) (pb[0] * pd[1]);
		c = (double) (splitter * pb[0]);
		abig = (double) (c - pb[0]);
		ahi = c - abig;
		alo = pb[0] - ahi;
		c = (double) (splitter * pd[1]);
		abig = (double) (c - pd[1]);
		bhi = c - abig;
		blo = pd[1] - bhi;
		err1 = bxdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bxdy0 = (alo * blo) - err3;
		dxby1 = (double) (pd[0] * pb[1]);
		c = (double) (splitter * pd[0]);
		abig = (double) (c - pd[0]);
		ahi = c - abig;
		alo = pd[0] - ahi;
		c = (double) (splitter * pb[1]);
		abig = (double) (c - pb[1]);
		bhi = c - abig;
		blo = pb[1] - bhi;
		err1 = dxby1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		dxby0 = (alo * blo) - err3;
		_i = (double) (bxdy0 - dxby0);
		bvirt = (double) (bxdy0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - dxby0;
		around = bxdy0 - avirt;
		bd[0] = around + bround;
		_j = (double) (bxdy1 + _i);
		bvirt = (double) (_j - bxdy1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = bxdy1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - dxby1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - dxby1;
		around = _0 - avirt;
		bd[1] = around + bround;
		bd[3] = (double) (_j + _i);
		bvirt = (double) (bd[3] - _j);
		avirt = bd[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		bd[2] = around + bround;

		cxey1 = (double) (pc[0] * pe[1]);
		c = (double) (splitter * pc[0]);
		abig = (double) (c - pc[0]);
		ahi = c - abig;
		alo = pc[0] - ahi;
		c = (double) (splitter * pe[1]);
		abig = (double) (c - pe[1]);
		bhi = c - abig;
		blo = pe[1] - bhi;
		err1 = cxey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cxey0 = (alo * blo) - err3;
		excy1 = (double) (pe[0] * pc[1]);
		c = (double) (splitter * pe[0]);
		abig = (double) (c - pe[0]);
		ahi = c - abig;
		alo = pe[0] - ahi;
		c = (double) (splitter * pc[1]);
		abig = (double) (c - pc[1]);
		bhi = c - abig;
		blo = pc[1] - bhi;
		err1 = excy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		excy0 = (alo * blo) - err3;
		_i = (double) (cxey0 - excy0);
		bvirt = (double) (cxey0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - excy0;
		around = cxey0 - avirt;
		ce[0] = around + bround;
		_j = (double) (cxey1 + _i);
		bvirt = (double) (_j - cxey1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = cxey1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - excy1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - excy1;
		around = _0 - avirt;
		ce[1] = around + bround;
		ce[3] = (double) (_j + _i);
		bvirt = (double) (ce[3] - _j);
		avirt = ce[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ce[2] = around + bround;

		dxay1 = (double) (pd[0] * pa[1]);
		c = (double) (splitter * pd[0]);
		abig = (double) (c - pd[0]);
		ahi = c - abig;
		alo = pd[0] - ahi;
		c = (double) (splitter * pa[1]);
		abig = (double) (c - pa[1]);
		bhi = c - abig;
		blo = pa[1] - bhi;
		err1 = dxay1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		dxay0 = (alo * blo) - err3;
		axdy1 = (double) (pa[0] * pd[1]);
		c = (double) (splitter * pa[0]);
		abig = (double) (c - pa[0]);
		ahi = c - abig;
		alo = pa[0] - ahi;
		c = (double) (splitter * pd[1]);
		abig = (double) (c - pd[1]);
		bhi = c - abig;
		blo = pd[1] - bhi;
		err1 = axdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		axdy0 = (alo * blo) - err3;
		_i = (double) (dxay0 - axdy0);
		bvirt = (double) (dxay0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - axdy0;
		around = dxay0 - avirt;
		da[0] = around + bround;
		_j = (double) (dxay1 + _i);
		bvirt = (double) (_j - dxay1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = dxay1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - axdy1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - axdy1;
		around = _0 - avirt;
		da[1] = around + bround;
		da[3] = (double) (_j + _i);
		bvirt = (double) (da[3] - _j);
		avirt = da[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		da[2] = around + bround;

		exby1 = (double) (pe[0] * pb[1]);
		c = (double) (splitter * pe[0]);
		abig = (double) (c - pe[0]);
		ahi = c - abig;
		alo = pe[0] - ahi;
		c = (double) (splitter * pb[1]);
		abig = (double) (c - pb[1]);
		bhi = c - abig;
		blo = pb[1] - bhi;
		err1 = exby1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		exby0 = (alo * blo) - err3;
		bxey1 = (double) (pb[0] * pe[1]);
		c = (double) (splitter * pb[0]);
		abig = (double) (c - pb[0]);
		ahi = c - abig;
		alo = pb[0] - ahi;
		c = (double) (splitter * pe[1]);
		abig = (double) (c - pe[1]);
		bhi = c - abig;
		blo = pe[1] - bhi;
		err1 = bxey1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bxey0 = (alo * blo) - err3;
		_i = (double) (exby0 - bxey0);
		bvirt = (double) (exby0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bxey0;
		around = exby0 - avirt;
		eb[0] = around + bround;
		_j = (double) (exby1 + _i);
		bvirt = (double) (_j - exby1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = exby1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - bxey1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bxey1;
		around = _0 - avirt;
		eb[1] = around + bround;
		eb[3] = (double) (_j + _i);
		bvirt = (double) (eb[3] - _j);
		avirt = eb[3] - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		eb[2] = around + bround;

		temp8alen = _scale_expansion_zeroelim(4, bc, pa[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, ac, -pb[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, ab, pc[2], temp8a);
		abclen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, abc);

		temp8alen = _scale_expansion_zeroelim(4, cd, pb[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, bd, -pc[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, bc, pd[2], temp8a);
		bcdlen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, bcd);

		temp8alen = _scale_expansion_zeroelim(4, de, pc[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, ce, -pd[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, cd, pe[2], temp8a);
		cdelen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, cde);

		temp8alen = _scale_expansion_zeroelim(4, ea, pd[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, da, -pe[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, de, pa[2], temp8a);
		dealen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, dea);

		temp8alen = _scale_expansion_zeroelim(4, ab, pe[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, eb, -pa[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, ea, pb[2], temp8a);
		eablen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, eab);

		temp8alen = _scale_expansion_zeroelim(4, bd, pa[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, da, pb[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, ab, pd[2], temp8a);
		abdlen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, abd);

		temp8alen = _scale_expansion_zeroelim(4, ce, pb[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, eb, pc[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, bc, pe[2], temp8a);
		bcelen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, bce);

		temp8alen = _scale_expansion_zeroelim(4, da, pc[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, ac, pd[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, cd, pa[2], temp8a);
		cdalen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, cda);

		temp8alen = _scale_expansion_zeroelim(4, eb, pd[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, bd, pe[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, de, pb[2], temp8a);
		deblen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, deb);

		temp8alen = _scale_expansion_zeroelim(4, ac, pe[2], temp8a);
		temp8blen = _scale_expansion_zeroelim(4, ce, pa[2], temp8b);
		temp16len = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp8blen,
				temp8b, temp16);
		temp8alen = _scale_expansion_zeroelim(4, ea, pc[2], temp8a);
		eaclen = _fast_expansion_sum_zeroelim(temp8alen, temp8a, temp16len,
				temp16, eac);

		temp48alen = _fast_expansion_sum_zeroelim(cdelen, cde, bcelen, bce,
				temp48a);
		temp48blen = _fast_expansion_sum_zeroelim(deblen, deb, bcdlen, bcd,
				temp48b);
		for (i = 0; i < temp48blen; i++) {
			temp48b[i] = -temp48b[i];
		}
		bcdelen = _fast_expansion_sum_zeroelim(temp48alen, temp48a, temp48blen,
				temp48b, bcde);
		xlen = _scale_expansion_zeroelim(bcdelen, bcde, pa[0], temp192);
		xlen = _scale_expansion_zeroelim(xlen, temp192, pa[0], det384x);
		ylen = _scale_expansion_zeroelim(bcdelen, bcde, pa[1], temp192);
		ylen = _scale_expansion_zeroelim(ylen, temp192, pa[1], det384y);
		zlen = _scale_expansion_zeroelim(bcdelen, bcde, pa[2], temp192);
		zlen = _scale_expansion_zeroelim(zlen, temp192, pa[2], det384z);
		xylen = _fast_expansion_sum_zeroelim(xlen, det384x, ylen, det384y,
				detxy);
		alen = _fast_expansion_sum_zeroelim(xylen, detxy, zlen, det384z, adet);

		temp48alen = _fast_expansion_sum_zeroelim(dealen, dea, cdalen, cda,
				temp48a);
		temp48blen = _fast_expansion_sum_zeroelim(eaclen, eac, cdelen, cde,
				temp48b);
		for (i = 0; i < temp48blen; i++) {
			temp48b[i] = -temp48b[i];
		}
		cdealen = _fast_expansion_sum_zeroelim(temp48alen, temp48a, temp48blen,
				temp48b, cdea);
		xlen = _scale_expansion_zeroelim(cdealen, cdea, pb[0], temp192);
		xlen = _scale_expansion_zeroelim(xlen, temp192, pb[0], det384x);
		ylen = _scale_expansion_zeroelim(cdealen, cdea, pb[1], temp192);
		ylen = _scale_expansion_zeroelim(ylen, temp192, pb[1], det384y);
		zlen = _scale_expansion_zeroelim(cdealen, cdea, pb[2], temp192);
		zlen = _scale_expansion_zeroelim(zlen, temp192, pb[2], det384z);
		xylen = _fast_expansion_sum_zeroelim(xlen, det384x, ylen, det384y,
				detxy);
		blen = _fast_expansion_sum_zeroelim(xylen, detxy, zlen, det384z, bdet);

		temp48alen = _fast_expansion_sum_zeroelim(eablen, eab, deblen, deb,
				temp48a);
		temp48blen = _fast_expansion_sum_zeroelim(abdlen, abd, dealen, dea,
				temp48b);
		for (i = 0; i < temp48blen; i++) {
			temp48b[i] = -temp48b[i];
		}
		deablen = _fast_expansion_sum_zeroelim(temp48alen, temp48a, temp48blen,
				temp48b, deab);
		xlen = _scale_expansion_zeroelim(deablen, deab, pc[0], temp192);
		xlen = _scale_expansion_zeroelim(xlen, temp192, pc[0], det384x);
		ylen = _scale_expansion_zeroelim(deablen, deab, pc[1], temp192);
		ylen = _scale_expansion_zeroelim(ylen, temp192, pc[1], det384y);
		zlen = _scale_expansion_zeroelim(deablen, deab, pc[2], temp192);
		zlen = _scale_expansion_zeroelim(zlen, temp192, pc[2], det384z);
		xylen = _fast_expansion_sum_zeroelim(xlen, det384x, ylen, det384y,
				detxy);
		clen = _fast_expansion_sum_zeroelim(xylen, detxy, zlen, det384z, cdet);

		temp48alen = _fast_expansion_sum_zeroelim(abclen, abc, eaclen, eac,
				temp48a);
		temp48blen = _fast_expansion_sum_zeroelim(bcelen, bce, eablen, eab,
				temp48b);
		for (i = 0; i < temp48blen; i++) {
			temp48b[i] = -temp48b[i];
		}
		eabclen = _fast_expansion_sum_zeroelim(temp48alen, temp48a, temp48blen,
				temp48b, eabc);
		xlen = _scale_expansion_zeroelim(eabclen, eabc, pd[0], temp192);
		xlen = _scale_expansion_zeroelim(xlen, temp192, pd[0], det384x);
		ylen = _scale_expansion_zeroelim(eabclen, eabc, pd[1], temp192);
		ylen = _scale_expansion_zeroelim(ylen, temp192, pd[1], det384y);
		zlen = _scale_expansion_zeroelim(eabclen, eabc, pd[2], temp192);
		zlen = _scale_expansion_zeroelim(zlen, temp192, pd[2], det384z);
		xylen = _fast_expansion_sum_zeroelim(xlen, det384x, ylen, det384y,
				detxy);
		dlen = _fast_expansion_sum_zeroelim(xylen, detxy, zlen, det384z, ddet);

		temp48alen = _fast_expansion_sum_zeroelim(bcdlen, bcd, abdlen, abd,
				temp48a);
		temp48blen = _fast_expansion_sum_zeroelim(cdalen, cda, abclen, abc,
				temp48b);
		for (i = 0; i < temp48blen; i++) {
			temp48b[i] = -temp48b[i];
		}
		abcdlen = _fast_expansion_sum_zeroelim(temp48alen, temp48a, temp48blen,
				temp48b, abcd);
		xlen = _scale_expansion_zeroelim(abcdlen, abcd, pe[0], temp192);
		xlen = _scale_expansion_zeroelim(xlen, temp192, pe[0], det384x);
		ylen = _scale_expansion_zeroelim(abcdlen, abcd, pe[1], temp192);
		ylen = _scale_expansion_zeroelim(ylen, temp192, pe[1], det384y);
		zlen = _scale_expansion_zeroelim(abcdlen, abcd, pe[2], temp192);
		zlen = _scale_expansion_zeroelim(zlen, temp192, pe[2], det384z);
		xylen = _fast_expansion_sum_zeroelim(xlen, det384x, ylen, det384y,
				detxy);
		elen = _fast_expansion_sum_zeroelim(xylen, detxy, zlen, det384z, edet);

		ablen = _fast_expansion_sum_zeroelim(alen, adet, blen, bdet, abdet);
		cdlen = _fast_expansion_sum_zeroelim(clen, cdet, dlen, ddet, cddet);
		cdelen = _fast_expansion_sum_zeroelim(cdlen, cddet, elen, edet, cdedet);
		deterlen = _fast_expansion_sum_zeroelim(ablen, abdet, cdelen, cdedet,
				deter);

		return deter[deterlen - 1];
	}

	/**
	 * Insphere tri.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param q the q
	 * @return the w b_ classify
	 */
	public WB_Classify insphereTri(double[] p0, double[] p1, double[] p2,
			double[] q) {
		double result = _insphereTri(p0[0], p0[1], p0[2], p1[0], p1[1], p1[2],
				p2[0], p2[1], p2[2], q[0], q[1], q[2]);

		if (result > 0) {
			return WB_Classify.INSIDE;
		} else if (result < 0) {
			return WB_Classify.OUTSIDE;
		} else {
			return WB_Classify.ON;
		}
	}

	/**
	 * _insphere tri.
	 *
	 * @param a1 the a1
	 * @param a2 the a2
	 * @param a3 the a3
	 * @param b1 the b1
	 * @param b2 the b2
	 * @param b3 the b3
	 * @param c1 the c1
	 * @param c2 the c2
	 * @param c3 the c3
	 * @param q1 the q1
	 * @param q2 the q2
	 * @param q3 the q3
	 * @return the double
	 */
	private double _insphereTri(double a1, double a2, double a3, double b1,
			double b2, double b3, double c1, double c2, double c3, double q1,
			double q2, double q3) {
		double[] a = new double[3], b = new double[3], c = new double[3], q = new double[3], circumcenter = new double[3];
		double t1, t2, t3, r;
		double s1, s2, s3, u;
		a[0] = a1;
		a[1] = a2;
		a[2] = a3;
		b[0] = b1;
		b[1] = b2;
		b[2] = b3;
		c[0] = c1;
		c[1] = c2;
		c[2] = c3;
		q[0] = q1;
		q[1] = q2;
		q[2] = q3;

		circumcenter = circumcenterTri(a, b, c);

		t1 = circumcenter[0] - a[0];
		t1 = t1 * t1;
		t2 = circumcenter[1] - a[1];
		t2 = t2 * t2;
		t3 = circumcenter[2] - a[2];
		t3 = t3 * t3;
		r = t1 + t2 + t3;

		s1 = circumcenter[0] - q[0];
		s1 = s1 * s1;
		s2 = circumcenter[1] - q[1];
		s2 = s2 * s2;
		s3 = circumcenter[2] - q[2];
		s3 = s3 * s3;
		u = s1 + s2 + s3;

		return (r - u);
	}

	/**
	 * Incircle tri.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param q the q
	 * @return the double
	 */
	public double incircleTri(double[] p0, double[] p1, double[] p2, double[] q) {
		double adx, bdx, cdx, ady, bdy, cdy;
		double bdxcdy, cdxbdy, cdxady, adxcdy, adxbdy, bdxady;
		double alift, blift, clift;
		double det;
		double permanent, errbound;

		adx = p0[0] - q[0];
		bdx = p1[0] - q[0];
		cdx = p2[0] - q[0];
		ady = p0[1] - q[1];
		bdy = p1[1] - q[1];
		cdy = p2[1] - q[1];

		bdxcdy = bdx * cdy;
		cdxbdy = cdx * bdy;
		alift = adx * adx + ady * ady;

		cdxady = cdx * ady;
		adxcdy = adx * cdy;
		blift = bdx * bdx + bdy * bdy;

		adxbdy = adx * bdy;
		bdxady = bdx * ady;
		clift = cdx * cdx + cdy * cdy;

		det = alift * (bdxcdy - cdxbdy) + blift * (cdxady - adxcdy) + clift
				* (adxbdy - bdxady);

		permanent = (((bdxcdy) >= 0.0 ? (bdxcdy) : -(bdxcdy)) + ((cdxbdy) >= 0.0 ? (cdxbdy)
				: -(cdxbdy)))
				* alift
				+ (((cdxady) >= 0.0 ? (cdxady) : -(cdxady)) + ((adxcdy) >= 0.0 ? (adxcdy)
						: -(adxcdy)))
				* blift
				+ (((adxbdy) >= 0.0 ? (adxbdy) : -(adxbdy)) + ((bdxady) >= 0.0 ? (bdxady)
						: -(bdxady))) * clift;
		errbound = iccerrboundA * permanent;
		if ((det > errbound) || (-det > errbound)) {
			return det;
		}

		return _incircleTriAdapt(p0, p1, p2, q, permanent);
	}

	/**
	 * _incircle tri adapt.
	 *
	 * @param pa the pa
	 * @param pb the pb
	 * @param pc the pc
	 * @param pd the pd
	 * @param permanent the permanent
	 * @return the double
	 */
	private double _incircleTriAdapt(double[] pa, double[] pb, double[] pc,
			double[] pd, double permanent) {
		double adx, bdx, cdx, ady, bdy, cdy;
		double det, errbound;

		double bdxcdy1, cdxbdy1, cdxady1, adxcdy1, adxbdy1, bdxady1;
		double bdxcdy0, cdxbdy0, cdxady0, adxcdy0, adxbdy0, bdxady0;
		double[] bc = new double[4], ca = new double[4], ab = new double[4];
		double bc3, ca3, ab3;
		double[] axbc = new double[8], axxbc = new double[16], aybc = new double[8], ayybc = new double[16], adet = new double[32];
		int axbclen, axxbclen, aybclen, ayybclen, alen;
		double[] bxca = new double[8], bxxca = new double[16], byca = new double[8], byyca = new double[16], bdet = new double[32];
		int bxcalen, bxxcalen, bycalen, byycalen, blen;
		double[] cxab = new double[8], cxxab = new double[16], cyab = new double[8], cyyab = new double[16], cdet = new double[32];
		int cxablen, cxxablen, cyablen, cyyablen, clen;
		double[] abdet = new double[64];
		int ablen;
		double[] fin1 = new double[1152], fin2 = new double[1152];
		double[] finnow, finother, finswap;
		int finlength;

		double adxtail, bdxtail, cdxtail, adytail, bdytail, cdytail;
		double adxadx1, adyady1, bdxbdx1, bdybdy1, cdxcdx1, cdycdy1;
		double adxadx0, adyady0, bdxbdx0, bdybdy0, cdxcdx0, cdycdy0;
		double[] aa = new double[4], bb = new double[4], cc = new double[4];
		double aa3, bb3, cc3;
		double ti1, tj1;
		double ti0, tj0;
		double[] u = new double[4], v = new double[4];
		double u3, v3;
		double[] temp8 = new double[8], temp16a = new double[16], temp16b = new double[16], temp16c = new double[16];
		double[] temp32a = new double[32], temp32b = new double[32], temp48 = new double[48], temp64 = new double[64];
		int temp8len, temp16alen, temp16blen, temp16clen;
		int temp32alen, temp32blen, temp48len, temp64len;
		double[] axtbb = new double[8], axtcc = new double[8], aytbb = new double[8], aytcc = new double[8];
		int axtbblen, axtcclen, aytbblen, aytcclen;
		double[] bxtaa = new double[8], bxtcc = new double[8], bytaa = new double[8], bytcc = new double[8];
		int bxtaalen, bxtcclen, bytaalen, bytcclen;
		double[] cxtaa = new double[8], cxtbb = new double[8], cytaa = new double[8], cytbb = new double[8];
		int cxtaalen, cxtbblen, cytaalen, cytbblen;
		double[] axtbc = new double[8], aytbc = new double[8], bxtca = new double[8], bytca = new double[8], cxtab = new double[8], cytab = new double[8];
		int axtbclen = 0, aytbclen = 0, bxtcalen = 0, bytcalen = 0, cxtablen = 0, cytablen = 0;
		double[] axtbct = new double[16], aytbct = new double[16], bxtcat = new double[16], bytcat = new double[16], cxtabt = new double[16], cytabt = new double[16];
		int axtbctlen, aytbctlen, bxtcatlen, bytcatlen, cxtabtlen, cytabtlen;
		double[] axtbctt = new double[8], aytbctt = new double[8], bxtcatt = new double[8];
		double[] bytcatt = new double[8], cxtabtt = new double[8], cytabtt = new double[8];
		int axtbcttlen, aytbcttlen, bxtcattlen, bytcattlen, cxtabttlen, cytabttlen;
		double[] abt = new double[8], bct = new double[8], cat = new double[8];
		int abtlen, bctlen, catlen;
		double[] abtt = new double[4], bctt = new double[4], catt = new double[4];
		int abttlen, bcttlen, cattlen;
		double abtt3, bctt3, catt3;
		double negate;

		double bvirt;
		double avirt, bround, around;
		double c;
		double abig;
		double ahi, alo, bhi, blo;
		double err1, err2, err3;
		double _i, _j;
		double _0;

		adx = (double) (pa[0] - pd[0]);
		bdx = (double) (pb[0] - pd[0]);
		cdx = (double) (pc[0] - pd[0]);
		ady = (double) (pa[1] - pd[1]);
		bdy = (double) (pb[1] - pd[1]);
		cdy = (double) (pc[1] - pd[1]);

		bdxcdy1 = (double) (bdx * cdy);
		c = (double) (splitter * bdx);
		abig = (double) (c - bdx);
		ahi = c - abig;
		alo = bdx - ahi;
		c = (double) (splitter * cdy);
		abig = (double) (c - cdy);
		bhi = c - abig;
		blo = cdy - bhi;
		err1 = bdxcdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bdxcdy0 = (alo * blo) - err3;
		cdxbdy1 = (double) (cdx * bdy);
		c = (double) (splitter * cdx);
		abig = (double) (c - cdx);
		ahi = c - abig;
		alo = cdx - ahi;
		c = (double) (splitter * bdy);
		abig = (double) (c - bdy);
		bhi = c - abig;
		blo = bdy - bhi;
		err1 = cdxbdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cdxbdy0 = (alo * blo) - err3;
		_i = (double) (bdxcdy0 - cdxbdy0);
		bvirt = (double) (bdxcdy0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cdxbdy0;
		around = bdxcdy0 - avirt;
		bc[0] = around + bround;
		_j = (double) (bdxcdy1 + _i);
		bvirt = (double) (_j - bdxcdy1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = bdxcdy1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - cdxbdy1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - cdxbdy1;
		around = _0 - avirt;
		bc[1] = around + bround;
		bc3 = (double) (_j + _i);
		bvirt = (double) (bc3 - _j);
		avirt = bc3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		bc[2] = around + bround;
		bc[3] = bc3;
		axbclen = _scale_expansion_zeroelim(4, bc, adx, axbc);
		axxbclen = _scale_expansion_zeroelim(axbclen, axbc, adx, axxbc);
		aybclen = _scale_expansion_zeroelim(4, bc, ady, aybc);
		ayybclen = _scale_expansion_zeroelim(aybclen, aybc, ady, ayybc);
		alen = _fast_expansion_sum_zeroelim(axxbclen, axxbc, ayybclen, ayybc,
				adet);

		cdxady1 = (double) (cdx * ady);
		c = (double) (splitter * cdx);
		abig = (double) (c - cdx);
		ahi = c - abig;
		alo = cdx - ahi;
		c = (double) (splitter * ady);
		abig = (double) (c - ady);
		bhi = c - abig;
		blo = ady - bhi;
		err1 = cdxady1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		cdxady0 = (alo * blo) - err3;
		adxcdy1 = (double) (adx * cdy);
		c = (double) (splitter * adx);
		abig = (double) (c - adx);
		ahi = c - abig;
		alo = adx - ahi;
		c = (double) (splitter * cdy);
		abig = (double) (c - cdy);
		bhi = c - abig;
		blo = cdy - bhi;
		err1 = adxcdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		adxcdy0 = (alo * blo) - err3;
		_i = (double) (cdxady0 - adxcdy0);
		bvirt = (double) (cdxady0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - adxcdy0;
		around = cdxady0 - avirt;
		ca[0] = around + bround;
		_j = (double) (cdxady1 + _i);
		bvirt = (double) (_j - cdxady1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = cdxady1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - adxcdy1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - adxcdy1;
		around = _0 - avirt;
		ca[1] = around + bround;
		ca3 = (double) (_j + _i);
		bvirt = (double) (ca3 - _j);
		avirt = ca3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ca[2] = around + bround;
		ca[3] = ca3;
		bxcalen = _scale_expansion_zeroelim(4, ca, bdx, bxca);
		bxxcalen = _scale_expansion_zeroelim(bxcalen, bxca, bdx, bxxca);
		bycalen = _scale_expansion_zeroelim(4, ca, bdy, byca);
		byycalen = _scale_expansion_zeroelim(bycalen, byca, bdy, byyca);
		blen = _fast_expansion_sum_zeroelim(bxxcalen, bxxca, byycalen, byyca,
				bdet);

		adxbdy1 = (double) (adx * bdy);
		c = (double) (splitter * adx);
		abig = (double) (c - adx);
		ahi = c - abig;
		alo = adx - ahi;
		c = (double) (splitter * bdy);
		abig = (double) (c - bdy);
		bhi = c - abig;
		blo = bdy - bhi;
		err1 = adxbdy1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		adxbdy0 = (alo * blo) - err3;
		bdxady1 = (double) (bdx * ady);
		c = (double) (splitter * bdx);
		abig = (double) (c - bdx);
		ahi = c - abig;
		alo = bdx - ahi;
		c = (double) (splitter * ady);
		abig = (double) (c - ady);
		bhi = c - abig;
		blo = ady - bhi;
		err1 = bdxady1 - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		bdxady0 = (alo * blo) - err3;
		_i = (double) (adxbdy0 - bdxady0);
		bvirt = (double) (adxbdy0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bdxady0;
		around = adxbdy0 - avirt;
		ab[0] = around + bround;
		_j = (double) (adxbdy1 + _i);
		bvirt = (double) (_j - adxbdy1);
		avirt = _j - bvirt;
		bround = _i - bvirt;
		around = adxbdy1 - avirt;
		_0 = around + bround;
		_i = (double) (_0 - bdxady1);
		bvirt = (double) (_0 - _i);
		avirt = _i + bvirt;
		bround = bvirt - bdxady1;
		around = _0 - avirt;
		ab[1] = around + bround;
		ab3 = (double) (_j + _i);
		bvirt = (double) (ab3 - _j);
		avirt = ab3 - bvirt;
		bround = _i - bvirt;
		around = _j - avirt;
		ab[2] = around + bround;
		ab[3] = ab3;
		cxablen = _scale_expansion_zeroelim(4, ab, cdx, cxab);
		cxxablen = _scale_expansion_zeroelim(cxablen, cxab, cdx, cxxab);
		cyablen = _scale_expansion_zeroelim(4, ab, cdy, cyab);
		cyyablen = _scale_expansion_zeroelim(cyablen, cyab, cdy, cyyab);
		clen = _fast_expansion_sum_zeroelim(cxxablen, cxxab, cyyablen, cyyab,
				cdet);

		ablen = _fast_expansion_sum_zeroelim(alen, adet, blen, bdet, abdet);
		finlength = _fast_expansion_sum_zeroelim(ablen, abdet, clen, cdet, fin1);

		det = _estimate(finlength, fin1);
		errbound = iccerrboundB * permanent;
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		bvirt = (double) (pa[0] - adx);
		avirt = adx + bvirt;
		bround = bvirt - pd[0];
		around = pa[0] - avirt;
		adxtail = around + bround;
		bvirt = (double) (pa[1] - ady);
		avirt = ady + bvirt;
		bround = bvirt - pd[1];
		around = pa[1] - avirt;
		adytail = around + bround;
		bvirt = (double) (pb[0] - bdx);
		avirt = bdx + bvirt;
		bround = bvirt - pd[0];
		around = pb[0] - avirt;
		bdxtail = around + bround;
		bvirt = (double) (pb[1] - bdy);
		avirt = bdy + bvirt;
		bround = bvirt - pd[1];
		around = pb[1] - avirt;
		bdytail = around + bround;
		bvirt = (double) (pc[0] - cdx);
		avirt = cdx + bvirt;
		bround = bvirt - pd[0];
		around = pc[0] - avirt;
		cdxtail = around + bround;
		bvirt = (double) (pc[1] - cdy);
		avirt = cdy + bvirt;
		bround = bvirt - pd[1];
		around = pc[1] - avirt;
		cdytail = around + bround;
		if ((adxtail == 0.0) && (bdxtail == 0.0) && (cdxtail == 0.0)
				&& (adytail == 0.0) && (bdytail == 0.0) && (cdytail == 0.0)) {
			return det;
		}

		errbound = iccerrboundC * permanent + resulterrbound
				* ((det) >= 0.0 ? (det) : -(det));
		det += ((adx * adx + ady * ady)
				* ((bdx * cdytail + cdy * bdxtail) - (bdy * cdxtail + cdx
						* bdytail)) + 2.0 * (adx * adxtail + ady * adytail)
				* (bdx * cdy - bdy * cdx))
				+ ((bdx * bdx + bdy * bdy)
						* ((cdx * adytail + ady * cdxtail) - (cdy * adxtail + adx
								* cdytail)) + 2.0
						* (bdx * bdxtail + bdy * bdytail)
						* (cdx * ady - cdy * adx))
				+ ((cdx * cdx + cdy * cdy)
						* ((adx * bdytail + bdy * adxtail) - (ady * bdxtail + bdx
								* adytail)) + 2.0
						* (cdx * cdxtail + cdy * cdytail)
						* (adx * bdy - ady * bdx));
		if ((det >= errbound) || (-det >= errbound)) {
			return det;
		}

		finnow = fin1;
		finother = fin2;

		if ((bdxtail != 0.0) || (bdytail != 0.0) || (cdxtail != 0.0)
				|| (cdytail != 0.0)) {
			adxadx1 = (double) (adx * adx);
			c = (double) (splitter * adx);
			abig = (double) (c - adx);
			ahi = c - abig;
			alo = adx - ahi;
			err1 = adxadx1 - (ahi * ahi);
			err3 = err1 - ((ahi + ahi) * alo);
			adxadx0 = (alo * alo) - err3;
			adyady1 = (double) (ady * ady);
			c = (double) (splitter * ady);
			abig = (double) (c - ady);
			ahi = c - abig;
			alo = ady - ahi;
			err1 = adyady1 - (ahi * ahi);
			err3 = err1 - ((ahi + ahi) * alo);
			adyady0 = (alo * alo) - err3;
			_i = (double) (adxadx0 + adyady0);
			bvirt = (double) (_i - adxadx0);
			avirt = _i - bvirt;
			bround = adyady0 - bvirt;
			around = adxadx0 - avirt;
			aa[0] = around + bround;
			_j = (double) (adxadx1 + _i);
			bvirt = (double) (_j - adxadx1);
			avirt = _j - bvirt;
			bround = _i - bvirt;
			around = adxadx1 - avirt;
			_0 = around + bround;
			_i = (double) (_0 + adyady1);
			bvirt = (double) (_i - _0);
			avirt = _i - bvirt;
			bround = adyady1 - bvirt;
			around = _0 - avirt;
			aa[1] = around + bround;
			aa3 = (double) (_j + _i);
			bvirt = (double) (aa3 - _j);
			avirt = aa3 - bvirt;
			bround = _i - bvirt;
			around = _j - avirt;
			aa[2] = around + bround;
			aa[3] = aa3;
		}
		if ((cdxtail != 0.0) || (cdytail != 0.0) || (adxtail != 0.0)
				|| (adytail != 0.0)) {
			bdxbdx1 = (double) (bdx * bdx);
			c = (double) (splitter * bdx);
			abig = (double) (c - bdx);
			ahi = c - abig;
			alo = bdx - ahi;
			err1 = bdxbdx1 - (ahi * ahi);
			err3 = err1 - ((ahi + ahi) * alo);
			bdxbdx0 = (alo * alo) - err3;
			bdybdy1 = (double) (bdy * bdy);
			c = (double) (splitter * bdy);
			abig = (double) (c - bdy);
			ahi = c - abig;
			alo = bdy - ahi;
			err1 = bdybdy1 - (ahi * ahi);
			err3 = err1 - ((ahi + ahi) * alo);
			bdybdy0 = (alo * alo) - err3;
			_i = (double) (bdxbdx0 + bdybdy0);
			bvirt = (double) (_i - bdxbdx0);
			avirt = _i - bvirt;
			bround = bdybdy0 - bvirt;
			around = bdxbdx0 - avirt;
			bb[0] = around + bround;
			_j = (double) (bdxbdx1 + _i);
			bvirt = (double) (_j - bdxbdx1);
			avirt = _j - bvirt;
			bround = _i - bvirt;
			around = bdxbdx1 - avirt;
			_0 = around + bround;
			_i = (double) (_0 + bdybdy1);
			bvirt = (double) (_i - _0);
			avirt = _i - bvirt;
			bround = bdybdy1 - bvirt;
			around = _0 - avirt;
			bb[1] = around + bround;
			bb3 = (double) (_j + _i);
			bvirt = (double) (bb3 - _j);
			avirt = bb3 - bvirt;
			bround = _i - bvirt;
			around = _j - avirt;
			bb[2] = around + bround;
			bb[3] = bb3;
		}
		if ((adxtail != 0.0) || (adytail != 0.0) || (bdxtail != 0.0)
				|| (bdytail != 0.0)) {
			cdxcdx1 = (double) (cdx * cdx);
			c = (double) (splitter * cdx);
			abig = (double) (c - cdx);
			ahi = c - abig;
			alo = cdx - ahi;
			err1 = cdxcdx1 - (ahi * ahi);
			err3 = err1 - ((ahi + ahi) * alo);
			cdxcdx0 = (alo * alo) - err3;
			cdycdy1 = (double) (cdy * cdy);
			c = (double) (splitter * cdy);
			abig = (double) (c - cdy);
			ahi = c - abig;
			alo = cdy - ahi;
			err1 = cdycdy1 - (ahi * ahi);
			err3 = err1 - ((ahi + ahi) * alo);
			cdycdy0 = (alo * alo) - err3;
			_i = (double) (cdxcdx0 + cdycdy0);
			bvirt = (double) (_i - cdxcdx0);
			avirt = _i - bvirt;
			bround = cdycdy0 - bvirt;
			around = cdxcdx0 - avirt;
			cc[0] = around + bround;
			_j = (double) (cdxcdx1 + _i);
			bvirt = (double) (_j - cdxcdx1);
			avirt = _j - bvirt;
			bround = _i - bvirt;
			around = cdxcdx1 - avirt;
			_0 = around + bround;
			_i = (double) (_0 + cdycdy1);
			bvirt = (double) (_i - _0);
			avirt = _i - bvirt;
			bround = cdycdy1 - bvirt;
			around = _0 - avirt;
			cc[1] = around + bround;
			cc3 = (double) (_j + _i);
			bvirt = (double) (cc3 - _j);
			avirt = cc3 - bvirt;
			bround = _i - bvirt;
			around = _j - avirt;
			cc[2] = around + bround;
			cc[3] = cc3;
		}

		if (adxtail != 0.0) {
			axtbclen = _scale_expansion_zeroelim(4, bc, adxtail, axtbc);
			temp16alen = _scale_expansion_zeroelim(axtbclen, axtbc, 2.0 * adx,
					temp16a);

			axtcclen = _scale_expansion_zeroelim(4, cc, adxtail, axtcc);
			temp16blen = _scale_expansion_zeroelim(axtcclen, axtcc, bdy,
					temp16b);

			axtbblen = _scale_expansion_zeroelim(4, bb, adxtail, axtbb);
			temp16clen = _scale_expansion_zeroelim(axtbblen, axtbb, -cdy,
					temp16c);

			temp32alen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
					temp16blen, temp16b, temp32a);
			temp48len = _fast_expansion_sum_zeroelim(temp16clen, temp16c,
					temp32alen, temp32a, temp48);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					temp48len, temp48, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (adytail != 0.0) {
			aytbclen = _scale_expansion_zeroelim(4, bc, adytail, aytbc);
			temp16alen = _scale_expansion_zeroelim(aytbclen, aytbc, 2.0 * ady,
					temp16a);

			aytbblen = _scale_expansion_zeroelim(4, bb, adytail, aytbb);
			temp16blen = _scale_expansion_zeroelim(aytbblen, aytbb, cdx,
					temp16b);

			aytcclen = _scale_expansion_zeroelim(4, cc, adytail, aytcc);
			temp16clen = _scale_expansion_zeroelim(aytcclen, aytcc, -bdx,
					temp16c);

			temp32alen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
					temp16blen, temp16b, temp32a);
			temp48len = _fast_expansion_sum_zeroelim(temp16clen, temp16c,
					temp32alen, temp32a, temp48);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					temp48len, temp48, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (bdxtail != 0.0) {
			bxtcalen = _scale_expansion_zeroelim(4, ca, bdxtail, bxtca);
			temp16alen = _scale_expansion_zeroelim(bxtcalen, bxtca, 2.0 * bdx,
					temp16a);

			bxtaalen = _scale_expansion_zeroelim(4, aa, bdxtail, bxtaa);
			temp16blen = _scale_expansion_zeroelim(bxtaalen, bxtaa, cdy,
					temp16b);

			bxtcclen = _scale_expansion_zeroelim(4, cc, bdxtail, bxtcc);
			temp16clen = _scale_expansion_zeroelim(bxtcclen, bxtcc, -ady,
					temp16c);

			temp32alen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
					temp16blen, temp16b, temp32a);
			temp48len = _fast_expansion_sum_zeroelim(temp16clen, temp16c,
					temp32alen, temp32a, temp48);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					temp48len, temp48, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (bdytail != 0.0) {
			bytcalen = _scale_expansion_zeroelim(4, ca, bdytail, bytca);
			temp16alen = _scale_expansion_zeroelim(bytcalen, bytca, 2.0 * bdy,
					temp16a);

			bytcclen = _scale_expansion_zeroelim(4, cc, bdytail, bytcc);
			temp16blen = _scale_expansion_zeroelim(bytcclen, bytcc, adx,
					temp16b);

			bytaalen = _scale_expansion_zeroelim(4, aa, bdytail, bytaa);
			temp16clen = _scale_expansion_zeroelim(bytaalen, bytaa, -cdx,
					temp16c);

			temp32alen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
					temp16blen, temp16b, temp32a);
			temp48len = _fast_expansion_sum_zeroelim(temp16clen, temp16c,
					temp32alen, temp32a, temp48);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					temp48len, temp48, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (cdxtail != 0.0) {
			cxtablen = _scale_expansion_zeroelim(4, ab, cdxtail, cxtab);
			temp16alen = _scale_expansion_zeroelim(cxtablen, cxtab, 2.0 * cdx,
					temp16a);

			cxtbblen = _scale_expansion_zeroelim(4, bb, cdxtail, cxtbb);
			temp16blen = _scale_expansion_zeroelim(cxtbblen, cxtbb, ady,
					temp16b);

			cxtaalen = _scale_expansion_zeroelim(4, aa, cdxtail, cxtaa);
			temp16clen = _scale_expansion_zeroelim(cxtaalen, cxtaa, -bdy,
					temp16c);

			temp32alen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
					temp16blen, temp16b, temp32a);
			temp48len = _fast_expansion_sum_zeroelim(temp16clen, temp16c,
					temp32alen, temp32a, temp48);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					temp48len, temp48, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}
		if (cdytail != 0.0) {
			cytablen = _scale_expansion_zeroelim(4, ab, cdytail, cytab);
			temp16alen = _scale_expansion_zeroelim(cytablen, cytab, 2.0 * cdy,
					temp16a);

			cytaalen = _scale_expansion_zeroelim(4, aa, cdytail, cytaa);
			temp16blen = _scale_expansion_zeroelim(cytaalen, cytaa, bdx,
					temp16b);

			cytbblen = _scale_expansion_zeroelim(4, bb, cdytail, cytbb);
			temp16clen = _scale_expansion_zeroelim(cytbblen, cytbb, -adx,
					temp16c);

			temp32alen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
					temp16blen, temp16b, temp32a);
			temp48len = _fast_expansion_sum_zeroelim(temp16clen, temp16c,
					temp32alen, temp32a, temp48);
			finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
					temp48len, temp48, finother);
			finswap = finnow;
			finnow = finother;
			finother = finswap;
		}

		if ((adxtail != 0.0) || (adytail != 0.0)) {
			if ((bdxtail != 0.0) || (bdytail != 0.0) || (cdxtail != 0.0)
					|| (cdytail != 0.0)) {
				ti1 = (double) (bdxtail * cdy);
				c = (double) (splitter * bdxtail);
				abig = (double) (c - bdxtail);
				ahi = c - abig;
				alo = bdxtail - ahi;
				c = (double) (splitter * cdy);
				abig = (double) (c - cdy);
				bhi = c - abig;
				blo = cdy - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				tj1 = (double) (bdx * cdytail);
				c = (double) (splitter * bdx);
				abig = (double) (c - bdx);
				ahi = c - abig;
				alo = bdx - ahi;
				c = (double) (splitter * cdytail);
				abig = (double) (c - cdytail);
				bhi = c - abig;
				blo = cdytail - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 + tj0);
				bvirt = (double) (_i - ti0);
				avirt = _i - bvirt;
				bround = tj0 - bvirt;
				around = ti0 - avirt;
				u[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 + tj1);
				bvirt = (double) (_i - _0);
				avirt = _i - bvirt;
				bround = tj1 - bvirt;
				around = _0 - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _i);
				bvirt = (double) (u3 - _j);
				avirt = u3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				u[2] = around + bround;
				u[3] = u3;
				negate = -bdy;
				ti1 = (double) (cdxtail * negate);
				c = (double) (splitter * cdxtail);
				abig = (double) (c - cdxtail);
				ahi = c - abig;
				alo = cdxtail - ahi;
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				bhi = c - abig;
				blo = negate - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				negate = -bdytail;
				tj1 = (double) (cdx * negate);
				c = (double) (splitter * cdx);
				abig = (double) (c - cdx);
				ahi = c - abig;
				alo = cdx - ahi;
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				bhi = c - abig;
				blo = negate - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 + tj0);
				bvirt = (double) (_i - ti0);
				avirt = _i - bvirt;
				bround = tj0 - bvirt;
				around = ti0 - avirt;
				v[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 + tj1);
				bvirt = (double) (_i - _0);
				avirt = _i - bvirt;
				bround = tj1 - bvirt;
				around = _0 - avirt;
				v[1] = around + bround;
				v3 = (double) (_j + _i);
				bvirt = (double) (v3 - _j);
				avirt = v3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				v[2] = around + bround;
				v[3] = v3;
				bctlen = _fast_expansion_sum_zeroelim(4, u, 4, v, bct);

				ti1 = (double) (bdxtail * cdytail);
				c = (double) (splitter * bdxtail);
				abig = (double) (c - bdxtail);
				ahi = c - abig;
				alo = bdxtail - ahi;
				c = (double) (splitter * cdytail);
				abig = (double) (c - cdytail);
				bhi = c - abig;
				blo = cdytail - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				tj1 = (double) (cdxtail * bdytail);
				c = (double) (splitter * cdxtail);
				abig = (double) (c - cdxtail);
				ahi = c - abig;
				alo = cdxtail - ahi;
				c = (double) (splitter * bdytail);
				abig = (double) (c - bdytail);
				bhi = c - abig;
				blo = bdytail - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 - tj0);
				bvirt = (double) (ti0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - tj0;
				around = ti0 - avirt;
				bctt[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - tj1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - tj1;
				around = _0 - avirt;
				bctt[1] = around + bround;
				bctt3 = (double) (_j + _i);
				bvirt = (double) (bctt3 - _j);
				avirt = bctt3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				bctt[2] = around + bround;
				bctt[3] = bctt3;
				bcttlen = 4;
			} else {
				bct[0] = 0.0;
				bctlen = 1;
				bctt[0] = 0.0;
				bcttlen = 1;
			}

			if (adxtail != 0.0) {
				temp16alen = _scale_expansion_zeroelim(axtbclen, axtbc,
						adxtail, temp16a);
				axtbctlen = _scale_expansion_zeroelim(bctlen, bct, adxtail,
						axtbct);
				temp32alen = _scale_expansion_zeroelim(axtbctlen, axtbct,
						2.0 * adx, temp32a);
				temp48len = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp32alen, temp32a, temp48);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp48len, temp48, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (bdytail != 0.0) {
					temp8len = _scale_expansion_zeroelim(4, cc, adxtail, temp8);
					temp16alen = _scale_expansion_zeroelim(temp8len, temp8,
							bdytail, temp16a);
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							temp16alen, temp16a, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
				if (cdytail != 0.0) {
					temp8len = _scale_expansion_zeroelim(4, bb, -adxtail, temp8);
					temp16alen = _scale_expansion_zeroelim(temp8len, temp8,
							cdytail, temp16a);
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							temp16alen, temp16a, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}

				temp32alen = _scale_expansion_zeroelim(axtbctlen, axtbct,
						adxtail, temp32a);
				axtbcttlen = _scale_expansion_zeroelim(bcttlen, bctt, adxtail,
						axtbctt);
				temp16alen = _scale_expansion_zeroelim(axtbcttlen, axtbctt,
						2.0 * adx, temp16a);
				temp16blen = _scale_expansion_zeroelim(axtbcttlen, axtbctt,
						adxtail, temp16b);
				temp32blen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp16blen, temp16b, temp32b);
				temp64len = _fast_expansion_sum_zeroelim(temp32alen, temp32a,
						temp32blen, temp32b, temp64);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp64len, temp64, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
			}
			if (adytail != 0.0) {
				temp16alen = _scale_expansion_zeroelim(aytbclen, aytbc,
						adytail, temp16a);
				aytbctlen = _scale_expansion_zeroelim(bctlen, bct, adytail,
						aytbct);
				temp32alen = _scale_expansion_zeroelim(aytbctlen, aytbct,
						2.0 * ady, temp32a);
				temp48len = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp32alen, temp32a, temp48);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp48len, temp48, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;

				temp32alen = _scale_expansion_zeroelim(aytbctlen, aytbct,
						adytail, temp32a);
				aytbcttlen = _scale_expansion_zeroelim(bcttlen, bctt, adytail,
						aytbctt);
				temp16alen = _scale_expansion_zeroelim(aytbcttlen, aytbctt,
						2.0 * ady, temp16a);
				temp16blen = _scale_expansion_zeroelim(aytbcttlen, aytbctt,
						adytail, temp16b);
				temp32blen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp16blen, temp16b, temp32b);
				temp64len = _fast_expansion_sum_zeroelim(temp32alen, temp32a,
						temp32blen, temp32b, temp64);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp64len, temp64, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
			}
		}
		if ((bdxtail != 0.0) || (bdytail != 0.0)) {
			if ((cdxtail != 0.0) || (cdytail != 0.0) || (adxtail != 0.0)
					|| (adytail != 0.0)) {
				ti1 = (double) (cdxtail * ady);
				c = (double) (splitter * cdxtail);
				abig = (double) (c - cdxtail);
				ahi = c - abig;
				alo = cdxtail - ahi;
				c = (double) (splitter * ady);
				abig = (double) (c - ady);
				bhi = c - abig;
				blo = ady - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				tj1 = (double) (cdx * adytail);
				c = (double) (splitter * cdx);
				abig = (double) (c - cdx);
				ahi = c - abig;
				alo = cdx - ahi;
				c = (double) (splitter * adytail);
				abig = (double) (c - adytail);
				bhi = c - abig;
				blo = adytail - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 + tj0);
				bvirt = (double) (_i - ti0);
				avirt = _i - bvirt;
				bround = tj0 - bvirt;
				around = ti0 - avirt;
				u[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 + tj1);
				bvirt = (double) (_i - _0);
				avirt = _i - bvirt;
				bround = tj1 - bvirt;
				around = _0 - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _i);
				bvirt = (double) (u3 - _j);
				avirt = u3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				u[2] = around + bround;
				u[3] = u3;
				negate = -cdy;
				ti1 = (double) (adxtail * negate);
				c = (double) (splitter * adxtail);
				abig = (double) (c - adxtail);
				ahi = c - abig;
				alo = adxtail - ahi;
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				bhi = c - abig;
				blo = negate - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				negate = -cdytail;
				tj1 = (double) (adx * negate);
				c = (double) (splitter * adx);
				abig = (double) (c - adx);
				ahi = c - abig;
				alo = adx - ahi;
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				bhi = c - abig;
				blo = negate - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 + tj0);
				bvirt = (double) (_i - ti0);
				avirt = _i - bvirt;
				bround = tj0 - bvirt;
				around = ti0 - avirt;
				v[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 + tj1);
				bvirt = (double) (_i - _0);
				avirt = _i - bvirt;
				bround = tj1 - bvirt;
				around = _0 - avirt;
				v[1] = around + bround;
				v3 = (double) (_j + _i);
				bvirt = (double) (v3 - _j);
				avirt = v3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				v[2] = around + bround;
				v[3] = v3;
				catlen = _fast_expansion_sum_zeroelim(4, u, 4, v, cat);

				ti1 = (double) (cdxtail * adytail);
				c = (double) (splitter * cdxtail);
				abig = (double) (c - cdxtail);
				ahi = c - abig;
				alo = cdxtail - ahi;
				c = (double) (splitter * adytail);
				abig = (double) (c - adytail);
				bhi = c - abig;
				blo = adytail - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				tj1 = (double) (adxtail * cdytail);
				c = (double) (splitter * adxtail);
				abig = (double) (c - adxtail);
				ahi = c - abig;
				alo = adxtail - ahi;
				c = (double) (splitter * cdytail);
				abig = (double) (c - cdytail);
				bhi = c - abig;
				blo = cdytail - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 - tj0);
				bvirt = (double) (ti0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - tj0;
				around = ti0 - avirt;
				catt[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - tj1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - tj1;
				around = _0 - avirt;
				catt[1] = around + bround;
				catt3 = (double) (_j + _i);
				bvirt = (double) (catt3 - _j);
				avirt = catt3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				catt[2] = around + bround;
				catt[3] = catt3;
				cattlen = 4;
			} else {
				cat[0] = 0.0;
				catlen = 1;
				catt[0] = 0.0;
				cattlen = 1;
			}

			if (bdxtail != 0.0) {
				temp16alen = _scale_expansion_zeroelim(bxtcalen, bxtca,
						bdxtail, temp16a);
				bxtcatlen = _scale_expansion_zeroelim(catlen, cat, bdxtail,
						bxtcat);
				temp32alen = _scale_expansion_zeroelim(bxtcatlen, bxtcat,
						2.0 * bdx, temp32a);
				temp48len = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp32alen, temp32a, temp48);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp48len, temp48, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (cdytail != 0.0) {
					temp8len = _scale_expansion_zeroelim(4, aa, bdxtail, temp8);
					temp16alen = _scale_expansion_zeroelim(temp8len, temp8,
							cdytail, temp16a);
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							temp16alen, temp16a, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
				if (adytail != 0.0) {
					temp8len = _scale_expansion_zeroelim(4, cc, -bdxtail, temp8);
					temp16alen = _scale_expansion_zeroelim(temp8len, temp8,
							adytail, temp16a);
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							temp16alen, temp16a, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}

				temp32alen = _scale_expansion_zeroelim(bxtcatlen, bxtcat,
						bdxtail, temp32a);
				bxtcattlen = _scale_expansion_zeroelim(cattlen, catt, bdxtail,
						bxtcatt);
				temp16alen = _scale_expansion_zeroelim(bxtcattlen, bxtcatt,
						2.0 * bdx, temp16a);
				temp16blen = _scale_expansion_zeroelim(bxtcattlen, bxtcatt,
						bdxtail, temp16b);
				temp32blen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp16blen, temp16b, temp32b);
				temp64len = _fast_expansion_sum_zeroelim(temp32alen, temp32a,
						temp32blen, temp32b, temp64);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp64len, temp64, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
			}
			if (bdytail != 0.0) {
				temp16alen = _scale_expansion_zeroelim(bytcalen, bytca,
						bdytail, temp16a);
				bytcatlen = _scale_expansion_zeroelim(catlen, cat, bdytail,
						bytcat);
				temp32alen = _scale_expansion_zeroelim(bytcatlen, bytcat,
						2.0 * bdy, temp32a);
				temp48len = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp32alen, temp32a, temp48);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp48len, temp48, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;

				temp32alen = _scale_expansion_zeroelim(bytcatlen, bytcat,
						bdytail, temp32a);
				bytcattlen = _scale_expansion_zeroelim(cattlen, catt, bdytail,
						bytcatt);
				temp16alen = _scale_expansion_zeroelim(bytcattlen, bytcatt,
						2.0 * bdy, temp16a);
				temp16blen = _scale_expansion_zeroelim(bytcattlen, bytcatt,
						bdytail, temp16b);
				temp32blen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp16blen, temp16b, temp32b);
				temp64len = _fast_expansion_sum_zeroelim(temp32alen, temp32a,
						temp32blen, temp32b, temp64);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp64len, temp64, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
			}
		}
		if ((cdxtail != 0.0) || (cdytail != 0.0)) {
			if ((adxtail != 0.0) || (adytail != 0.0) || (bdxtail != 0.0)
					|| (bdytail != 0.0)) {
				ti1 = (double) (adxtail * bdy);
				c = (double) (splitter * adxtail);
				abig = (double) (c - adxtail);
				ahi = c - abig;
				alo = adxtail - ahi;
				c = (double) (splitter * bdy);
				abig = (double) (c - bdy);
				bhi = c - abig;
				blo = bdy - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				tj1 = (double) (adx * bdytail);
				c = (double) (splitter * adx);
				abig = (double) (c - adx);
				ahi = c - abig;
				alo = adx - ahi;
				c = (double) (splitter * bdytail);
				abig = (double) (c - bdytail);
				bhi = c - abig;
				blo = bdytail - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 + tj0);
				bvirt = (double) (_i - ti0);
				avirt = _i - bvirt;
				bround = tj0 - bvirt;
				around = ti0 - avirt;
				u[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 + tj1);
				bvirt = (double) (_i - _0);
				avirt = _i - bvirt;
				bround = tj1 - bvirt;
				around = _0 - avirt;
				u[1] = around + bround;
				u3 = (double) (_j + _i);
				bvirt = (double) (u3 - _j);
				avirt = u3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				u[2] = around + bround;
				u[3] = u3;
				negate = -ady;
				ti1 = (double) (bdxtail * negate);
				c = (double) (splitter * bdxtail);
				abig = (double) (c - bdxtail);
				ahi = c - abig;
				alo = bdxtail - ahi;
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				bhi = c - abig;
				blo = negate - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				negate = -adytail;
				tj1 = (double) (bdx * negate);
				c = (double) (splitter * bdx);
				abig = (double) (c - bdx);
				ahi = c - abig;
				alo = bdx - ahi;
				c = (double) (splitter * negate);
				abig = (double) (c - negate);
				bhi = c - abig;
				blo = negate - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 + tj0);
				bvirt = (double) (_i - ti0);
				avirt = _i - bvirt;
				bround = tj0 - bvirt;
				around = ti0 - avirt;
				v[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 + tj1);
				bvirt = (double) (_i - _0);
				avirt = _i - bvirt;
				bround = tj1 - bvirt;
				around = _0 - avirt;
				v[1] = around + bround;
				v3 = (double) (_j + _i);
				bvirt = (double) (v3 - _j);
				avirt = v3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				v[2] = around + bround;
				v[3] = v3;
				abtlen = _fast_expansion_sum_zeroelim(4, u, 4, v, abt);

				ti1 = (double) (adxtail * bdytail);
				c = (double) (splitter * adxtail);
				abig = (double) (c - adxtail);
				ahi = c - abig;
				alo = adxtail - ahi;
				c = (double) (splitter * bdytail);
				abig = (double) (c - bdytail);
				bhi = c - abig;
				blo = bdytail - bhi;
				err1 = ti1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				ti0 = (alo * blo) - err3;
				tj1 = (double) (bdxtail * adytail);
				c = (double) (splitter * bdxtail);
				abig = (double) (c - bdxtail);
				ahi = c - abig;
				alo = bdxtail - ahi;
				c = (double) (splitter * adytail);
				abig = (double) (c - adytail);
				bhi = c - abig;
				blo = adytail - bhi;
				err1 = tj1 - (ahi * bhi);
				err2 = err1 - (alo * bhi);
				err3 = err2 - (ahi * blo);
				tj0 = (alo * blo) - err3;
				_i = (double) (ti0 - tj0);
				bvirt = (double) (ti0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - tj0;
				around = ti0 - avirt;
				abtt[0] = around + bround;
				_j = (double) (ti1 + _i);
				bvirt = (double) (_j - ti1);
				avirt = _j - bvirt;
				bround = _i - bvirt;
				around = ti1 - avirt;
				_0 = around + bround;
				_i = (double) (_0 - tj1);
				bvirt = (double) (_0 - _i);
				avirt = _i + bvirt;
				bround = bvirt - tj1;
				around = _0 - avirt;
				abtt[1] = around + bround;
				abtt3 = (double) (_j + _i);
				bvirt = (double) (abtt3 - _j);
				avirt = abtt3 - bvirt;
				bround = _i - bvirt;
				around = _j - avirt;
				abtt[2] = around + bround;
				abtt[3] = abtt3;
				abttlen = 4;
			} else {
				abt[0] = 0.0;
				abtlen = 1;
				abtt[0] = 0.0;
				abttlen = 1;
			}

			if (cdxtail != 0.0) {
				temp16alen = _scale_expansion_zeroelim(cxtablen, cxtab,
						cdxtail, temp16a);
				cxtabtlen = _scale_expansion_zeroelim(abtlen, abt, cdxtail,
						cxtabt);
				temp32alen = _scale_expansion_zeroelim(cxtabtlen, cxtabt,
						2.0 * cdx, temp32a);
				temp48len = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp32alen, temp32a, temp48);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp48len, temp48, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
				if (adytail != 0.0) {
					temp8len = _scale_expansion_zeroelim(4, bb, cdxtail, temp8);
					temp16alen = _scale_expansion_zeroelim(temp8len, temp8,
							adytail, temp16a);
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							temp16alen, temp16a, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}
				if (bdytail != 0.0) {
					temp8len = _scale_expansion_zeroelim(4, aa, -cdxtail, temp8);
					temp16alen = _scale_expansion_zeroelim(temp8len, temp8,
							bdytail, temp16a);
					finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
							temp16alen, temp16a, finother);
					finswap = finnow;
					finnow = finother;
					finother = finswap;
				}

				temp32alen = _scale_expansion_zeroelim(cxtabtlen, cxtabt,
						cdxtail, temp32a);
				cxtabttlen = _scale_expansion_zeroelim(abttlen, abtt, cdxtail,
						cxtabtt);
				temp16alen = _scale_expansion_zeroelim(cxtabttlen, cxtabtt,
						2.0 * cdx, temp16a);
				temp16blen = _scale_expansion_zeroelim(cxtabttlen, cxtabtt,
						cdxtail, temp16b);
				temp32blen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp16blen, temp16b, temp32b);
				temp64len = _fast_expansion_sum_zeroelim(temp32alen, temp32a,
						temp32blen, temp32b, temp64);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp64len, temp64, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
			}
			if (cdytail != 0.0) {
				temp16alen = _scale_expansion_zeroelim(cytablen, cytab,
						cdytail, temp16a);
				cytabtlen = _scale_expansion_zeroelim(abtlen, abt, cdytail,
						cytabt);
				temp32alen = _scale_expansion_zeroelim(cytabtlen, cytabt,
						2.0 * cdy, temp32a);
				temp48len = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp32alen, temp32a, temp48);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp48len, temp48, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;

				temp32alen = _scale_expansion_zeroelim(cytabtlen, cytabt,
						cdytail, temp32a);
				cytabttlen = _scale_expansion_zeroelim(abttlen, abtt, cdytail,
						cytabtt);
				temp16alen = _scale_expansion_zeroelim(cytabttlen, cytabtt,
						2.0 * cdy, temp16a);
				temp16blen = _scale_expansion_zeroelim(cytabttlen, cytabtt,
						cdytail, temp16b);
				temp32blen = _fast_expansion_sum_zeroelim(temp16alen, temp16a,
						temp16blen, temp16b, temp32b);
				temp64len = _fast_expansion_sum_zeroelim(temp32alen, temp32a,
						temp32blen, temp32b, temp64);
				finlength = _fast_expansion_sum_zeroelim(finlength, finnow,
						temp64len, temp64, finother);
				finswap = finnow;
				finnow = finother;
				finother = finswap;
			}
		}

		return finnow[finlength - 1];
	}

	/**
	 * Diffsides.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param q0 the q0
	 * @param q1 the q1
	 * @return the w b_ classify
	 */
	public WB_Classify diffsides(double[] p0, double[] p1, double[] p2,
			double[] q0, double[] q1) {
		double a, b;
		a = orientTetra(p0, p1, p2, q0);
		b = orientTetra(p0, p1, p2, q1);
		if (a == 0 && b == 0) {
			return WB_Classify.COPLANAR;
		}
		if ((a > 0 && b < 0) || (a < 0 && b > 0)) {
			if (a == 0 || b == 0) {
				return WB_Classify.DIFF;
			}
			return WB_Classify.DIFFEXCL;
		}
		if ((a > 0 && b > 0) || (a < 0 && b < 0)) {
			if (a == 0 || b == 0) {
				return WB_Classify.SAME;
			}
			return WB_Classify.SAMEEXCL;
		}

		return null;
	}

	/**
	 * Inplane.
	 *
	 * @param p0 the p0
	 * @param p1 the p1
	 * @param p2 the p2
	 * @param p3 the p3
	 * @return true, if successful
	 */
	public boolean inplane(double[] p0, double[] p1, double[] p2, double[] p3) {
		if (orientTetra(p0, p1, p2, p3) == 0)
			return true;
		else
			return false;
	}

	/** The splitter. */
	private double splitter;
	
	/** The epsilon. */
	private double epsilon;

	/** The resulterrbound. */
	private double resulterrbound;
	
	/** The ccwerrbound c. */
	private double ccwerrboundA, ccwerrboundB, ccwerrboundC;
	
	/** The o3derrbound c. */
	private double o3derrboundA, o3derrboundB, o3derrboundC;
	
	/** The iccerrbound c. */
	private double iccerrboundA, iccerrboundB, iccerrboundC;
	
	/** The isperrbound c. */
	private double isperrboundA, isperrboundB, isperrboundC;

	/**
	 * _exactinit.
	 */
	private void _exactinit() {
		double half;
		double check, lastcheck;
		int every_other;

		every_other = 1;
		half = 0.5;
		epsilon = 1.0;
		splitter = 1.0;
		check = 1.0;

		do {
			lastcheck = check;
			epsilon *= half;
			if (every_other != 0) {
				splitter *= 2.0;
			}
			every_other = every_other == 0 ? 1 : 0;
			check = 1.0 + epsilon;
		} while ((check != 1.0) && (check != lastcheck));
		splitter += 1.0;

		resulterrbound = (3.0 + 8.0 * epsilon) * epsilon;
		ccwerrboundA = (3.0 + 16.0 * epsilon) * epsilon;
		ccwerrboundB = (2.0 + 12.0 * epsilon) * epsilon;
		ccwerrboundC = (9.0 + 64.0 * epsilon) * epsilon * epsilon;
		o3derrboundA = (7.0 + 56.0 * epsilon) * epsilon;
		o3derrboundB = (3.0 + 28.0 * epsilon) * epsilon;
		o3derrboundC = (26.0 + 288.0 * epsilon) * epsilon * epsilon;
		iccerrboundA = (10.0 + 96.0 * epsilon) * epsilon;
		iccerrboundB = (4.0 + 48.0 * epsilon) * epsilon;
		iccerrboundC = (44.0 + 576.0 * epsilon) * epsilon * epsilon;
		isperrboundA = (16.0 + 224.0 * epsilon) * epsilon;
		isperrboundB = (5.0 + 72.0 * epsilon) * epsilon;
		isperrboundC = (71.0 + 1408.0 * epsilon) * epsilon * epsilon;
	}

	/**
	 * _fast_expansion_sum_zeroelim.
	 *
	 * @param elen the elen
	 * @param e the e
	 * @param flen the flen
	 * @param f the f
	 * @param h the h
	 * @return the int
	 */
	private int _fast_expansion_sum_zeroelim(int elen, double[] e, int flen,
			double[] f, double[] h) {
		double Q;
		double Qnew;
		double hh;
		double bvirt;
		double avirt, bround, around;
		int eindex, findex, hindex;
		double enow, fnow;

		enow = e[0];
		fnow = f[0];
		eindex = findex = 0;
		if ((fnow > enow) == (fnow > -enow)) {
			Q = enow;
			enow = e[++eindex];
		} else {
			Q = fnow;
			fnow = f[++findex];
		}
		hindex = 0;
		if ((eindex < elen) && (findex < flen)) {
			if ((fnow > enow) == (fnow > -enow)) {
				Qnew = (double) (enow + Q);
				bvirt = Qnew - enow;
				hh = Q - bvirt;
				enow = e[++eindex];
			} else {
				Qnew = (double) (fnow + Q);
				bvirt = Qnew - fnow;
				hh = Q - bvirt;
				fnow = f[++findex];
			}
			Q = Qnew;
			if (hh != 0.0) {
				h[hindex++] = hh;
			}
			while ((eindex < elen) && (findex < flen)) {
				if ((fnow > enow) == (fnow > -enow)) {
					Qnew = (double) (Q + enow);
					bvirt = (double) (Qnew - Q);
					avirt = Qnew - bvirt;
					bround = enow - bvirt;
					around = Q - avirt;
					hh = around + bround;
					enow = e[++eindex];
				} else {
					Qnew = (double) (Q + fnow);
					bvirt = (double) (Qnew - Q);
					avirt = Qnew - bvirt;
					bround = fnow - bvirt;
					around = Q - avirt;
					hh = around + bround;
					fnow = f[++findex];
				}
				Q = Qnew;
				if (hh != 0.0) {
					h[hindex++] = hh;
				}
			}
		}
		while (eindex < elen) {
			Qnew = (double) (Q + enow);
			bvirt = (double) (Qnew - Q);
			avirt = Qnew - bvirt;
			bround = enow - bvirt;
			around = Q - avirt;
			hh = around + bround;
			enow = e[++eindex];
			Q = Qnew;
			if (hh != 0.0) {
				h[hindex++] = hh;
			}
		}
		while (findex < flen) {
			Qnew = (double) (Q + fnow);
			bvirt = (double) (Qnew - Q);
			avirt = Qnew - bvirt;
			bround = fnow - bvirt;
			around = Q - avirt;
			hh = around + bround;
			fnow = f[++findex];
			Q = Qnew;
			if (hh != 0.0) {
				h[hindex++] = hh;
			}
		}
		if ((Q != 0.0) || (hindex == 0)) {
			h[hindex++] = Q;
		}
		return hindex;
	}

	/**
	 * _scale_expansion_zeroelim.
	 *
	 * @param elen the elen
	 * @param e the e
	 * @param b the b
	 * @param h the h
	 * @return the int
	 */
	private int _scale_expansion_zeroelim(int elen, double[] e, double b,
			double[] h) {
		double Q, sum;
		double hh;
		double product1;
		double product0;
		int eindex, hindex;
		double enow;
		double bvirt;
		double avirt, bround, around;
		double c;
		double abig;
		double ahi, alo, bhi, blo;
		double err1, err2, err3;

		c = (double) (splitter * b);
		abig = (double) (c - b);
		bhi = c - abig;
		blo = b - bhi;
		Q = (double) (e[0] * b);
		c = (double) (splitter * e[0]);
		abig = (double) (c - e[0]);
		ahi = c - abig;
		alo = e[0] - ahi;
		err1 = Q - (ahi * bhi);
		err2 = err1 - (alo * bhi);
		err3 = err2 - (ahi * blo);
		hh = (alo * blo) - err3;
		hindex = 0;
		if (hh != 0) {
			h[hindex++] = hh;
		}
		for (eindex = 1; eindex < elen; eindex++) {
			enow = e[eindex];
			product1 = (double) (enow * b);
			c = (double) (splitter * enow);
			abig = (double) (c - enow);
			ahi = c - abig;
			alo = enow - ahi;
			err1 = product1 - (ahi * bhi);
			err2 = err1 - (alo * bhi);
			err3 = err2 - (ahi * blo);
			product0 = (alo * blo) - err3;
			sum = (double) (Q + product0);
			bvirt = (double) (sum - Q);
			avirt = sum - bvirt;
			bround = product0 - bvirt;
			around = Q - avirt;
			hh = around + bround;
			if (hh != 0) {
				h[hindex++] = hh;
			}
			Q = (double) (product1 + sum);
			bvirt = Q - product1;
			hh = sum - bvirt;
			if (hh != 0) {
				h[hindex++] = hh;
			}
		}
		if ((Q != 0.0) || (hindex == 0)) {
			h[hindex++] = Q;
		}
		return hindex;
	}

	/**
	 * _estimate.
	 *
	 * @param elen the elen
	 * @param e the e
	 * @return the double
	 */
	private double _estimate(int elen, double[] e) {
		double Q;
		int eindex;

		Q = e[0];
		for (eindex = 1; eindex < elen; eindex++) {
			Q += e[eindex];
		}
		return Q;
	}

	/**
	 * Circumcenter tetra.
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @param d the d
	 * @param xi the xi
	 * @param eta the eta
	 * @param zeta the zeta
	 * @return the double[]
	 */
	public double[] circumcenterTetra(double[] a, double[] b, double[] c,
			double[] d, double[] xi, double[] eta, double[] zeta) {
		double xba, yba, zba, xca, yca, zca, xda, yda, zda;
		double balength, calength, dalength;
		double xcrosscd, ycrosscd, zcrosscd;
		double xcrossdb, ycrossdb, zcrossdb;
		double xcrossbc, ycrossbc, zcrossbc;
		double denominator;
		double xcirca, ycirca, zcirca;

		xba = b[0] - a[0];
		yba = b[1] - a[1];
		zba = b[2] - a[2];
		xca = c[0] - a[0];
		yca = c[1] - a[1];
		zca = c[2] - a[2];
		xda = d[0] - a[0];
		yda = d[1] - a[1];
		zda = d[2] - a[2];

		balength = xba * xba + yba * yba + zba * zba;
		calength = xca * xca + yca * yca + zca * zca;
		dalength = xda * xda + yda * yda + zda * zda;

		xcrosscd = yca * zda - yda * zca;
		ycrosscd = zca * xda - zda * xca;
		zcrosscd = xca * yda - xda * yca;
		xcrossdb = yda * zba - yba * zda;
		ycrossdb = zda * xba - zba * xda;
		zcrossdb = xda * yba - xba * yda;
		xcrossbc = yba * zca - yca * zba;
		ycrossbc = zba * xca - zca * xba;
		zcrossbc = xba * yca - xca * yba;

		denominator = 0.5 / (xba * xcrosscd + yba * ycrosscd + zba * zcrosscd);// Inexact
		// denominator = 0.5 / orient3d(b,c,d,a);//Exact

		xcirca = (balength * xcrosscd + calength * xcrossdb + dalength
				* xcrossbc)
				* denominator;
		ycirca = (balength * ycrosscd + calength * ycrossdb + dalength
				* ycrossbc)
				* denominator;
		zcirca = (balength * zcrosscd + calength * zcrossdb + dalength
				* zcrossbc)
				* denominator;
		double[] circumcenter = new double[3];
		circumcenter[0] = xcirca + a[0];
		circumcenter[1] = ycirca + a[1];
		circumcenter[2] = zcirca + a[2];

		if (xi != null) {
			xi[0] = (xcirca * xcrosscd + ycirca * ycrosscd + zcirca * zcrosscd)
					* (2.0 * denominator);
			eta[0] = (xcirca * xcrossdb + ycirca * ycrossdb + zcirca * zcrossdb)
					* (2.0 * denominator);
			zeta[0] = (xcirca * xcrossbc + ycirca * ycrossbc + zcirca
					* zcrossbc)
					* (2.0 * denominator);
		}
		return circumcenter;
	}

	/**
	 * Circumcenter tri.
	 *
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @return the double[]
	 */
	public double[] circumcenterTri(double[] a, double[] b, double[] c) {
		double xba, yba, zba, xca, yca, zca;
		double balength, calength;
		double xcrossbc, ycrossbc, zcrossbc;
		double denominator;
		double xcirca, ycirca, zcirca;

		xba = b[0] - a[0];
		yba = b[1] - a[1];
		zba = b[2] - a[2];
		xca = c[0] - a[0];
		yca = c[1] - a[1];
		zca = c[2] - a[2];

		balength = xba * xba + yba * yba + zba * zba;
		calength = xca * xca + yca * yca + zca * zca;

		xcrossbc = yba * zca - yca * zba;
		ycrossbc = zba * xca - zca * xba;
		zcrossbc = xba * yca - xca * yba;

		denominator = 0.5 / (xcrossbc * xcrossbc + ycrossbc * ycrossbc + zcrossbc
				* zcrossbc);

		xcirca = ((balength * yca - calength * yba) * zcrossbc - (balength
				* zca - calength * zba)
				* ycrossbc)
				* denominator;
		ycirca = ((balength * zca - calength * zba) * xcrossbc - (balength
				* xca - calength * xba)
				* zcrossbc)
				* denominator;
		zcirca = ((balength * xca - calength * xba) * ycrossbc - (balength
				* yca - calength * yba)
				* xcrossbc)
				* denominator;
		double[] circumcenter = new double[3];
		circumcenter[0] = xcirca + a[0];
		circumcenter[1] = ycirca + a[1];
		circumcenter[2] = zcirca + a[2];
		return circumcenter;

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		WB_Predicates predicates = new WB_Predicates();
		System.out.println(predicates.incircleTri(new double[] { 0,
				1.0000000000033 }, new double[] { 0, -1.0000000000033 },
				new double[] { 1.0000000000033, 0 }, new double[] {
						-1.0000000000033, 0 }));
	}


}
