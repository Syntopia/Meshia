package wblut.geom;

import wblut.WB_Epsilon;



// TODO: Auto-generated Javadoc
/**
 * The Class WB_CoordinateSystem3D.
 */
public class WB_CoordinateSystem3D {
	
	/** The _parent. */
	private WB_CoordinateSystem3D _parent;

	/**
	 * World.
	 *
	 * @return the w b_ coordinate system3 d
	 */
	protected final static WB_CoordinateSystem3D WORLD() {
		return new WB_CoordinateSystem3D(true);
	}

	/** The _origin. */
	private WB_Point3d _origin;
	
	/** The  x. */
	private WB_Vector3d _X;
	
	/** The  y. */
	private WB_Vector3d _Y;
	
	/** The  z. */
	private WB_Vector3d _Z;
	
	/** The _is world. */
	private boolean _isWorld;

	/**
	 * Instantiates a new w b_ coordinate system3 d.
	 *
	 * @param origin the origin
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param parent the parent
	 */
	protected WB_CoordinateSystem3D(final WB_Point3d origin,
			final WB_Vector3d x, final WB_Vector3d y, final WB_Vector3d z,
			final WB_CoordinateSystem3D parent) {
		_origin = origin.get();
		_X = x.get();
		_Y = y.get();
		_Z = z.get();
		_parent = parent;
		_isWorld = (_parent == null);
	}

	/**
	 * Instantiates a new w b_ coordinate system3 d.
	 *
	 * @param world the world
	 */
	protected WB_CoordinateSystem3D(final boolean world) {
		_origin = WB_Point3d.ZERO();
		_X = WB_Vector3d.X();
		_Y = WB_Vector3d.Y();
		_Z = WB_Vector3d.Z();
		_isWorld = world;
		_parent = (world) ? null : WORLD();
	}

	/**
	 * Instantiates a new w b_ coordinate system3 d.
	 */
	public WB_CoordinateSystem3D() {
		this(false);
	}

	/**
	 * Instantiates a new w b_ coordinate system3 d.
	 *
	 * @param parent the parent
	 */
	public WB_CoordinateSystem3D(final WB_CoordinateSystem3D parent) {
		_origin = WB_Point3d.ZERO();
		_X = WB_Vector3d.X();
		_Y = WB_Vector3d.Y();
		_Z = WB_Vector3d.Z();
		_parent = parent;
		_isWorld = (_parent == null);
	}

	/**
	 * Gets the.
	 *
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D get() {
		return new WB_CoordinateSystem3D(_origin, _X, _Y, _Z, _parent);
	}

	/**
	 * Sets the.
	 *
	 * @param origin the origin
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	protected void set(final WB_Point3d origin, final WB_Vector3d x,
			final WB_Vector3d y, final WB_Vector3d z) {
		_origin = origin.get();
		_X = x.get();
		_Y = y.get();
		_Z = z.get();
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent the parent
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setParent(final WB_CoordinateSystem3D parent) {
		_parent = parent;
		_isWorld = (_parent == null);
		return this;
	}

	/**
	 * Sets the origin.
	 *
	 * @param o the o
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setOrigin(final WB_Point3d o) {
		_origin.set(o);
		return this;
	}

	/**
	 * Sets the origin.
	 *
	 * @param ox the ox
	 * @param oy the oy
	 * @param oz the oz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setOrigin(final double ox, final double oy,
			final double oz) {
		_origin.set(ox, oy, oz);
		return this;
	}

	/**
	 * Sets the xy.
	 *
	 * @param X the x
	 * @param Y the y
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setXY(final WB_Vector3d X, final WB_Vector3d Y) {
		_X.set(X);
		_X.normalize();
		_Y.set(Y);
		_Y.normalize();
		_Z.set(WB_Vector3d.cross(_X, _Y));
		if (WB_Epsilon.isZeroSq(_Z.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Z.normalize();
		_Y.set(WB_Vector3d.cross(_Z, _X));
		_Y.normalize();
		return this;
	}

	/**
	 * Sets the yx.
	 *
	 * @param Y the y
	 * @param X the x
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setYX(final WB_Vector3d Y, final WB_Vector3d X) {
		_X.set(X);
		_X.normalize();
		_Y.set(Y);
		_Y.normalize();
		_Z.set(WB_Vector3d.cross(_X, _Y));
		if (WB_Epsilon.isZeroSq(_Z.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Z.normalize();
		_X.set(WB_Vector3d.cross(_Y, _Z));
		_X.normalize();
		return this;
	}

	/**
	 * Sets the xz.
	 *
	 * @param X the x
	 * @param Z the z
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setXZ(final WB_Vector3d X, final WB_Vector3d Z) {
		_X.set(X);
		_X.normalize();
		_Z.set(Z);
		_Z.normalize();
		_Y.set(WB_Vector3d.cross(_Z, _X));
		if (WB_Epsilon.isZeroSq(_Y.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Y.normalize();
		_Z.set(WB_Vector3d.cross(_X, _Y));
		_Z.normalize();
		return this;
	}

	/**
	 * Sets the zx.
	 *
	 * @param Z the z
	 * @param X the x
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setZX(final WB_Vector3d Z, final WB_Vector3d X) {
		_X.set(X);
		_X.normalize();
		_Z.set(Z);
		_Z.normalize();
		_Y.set(WB_Vector3d.cross(_Z, _X));
		if (WB_Epsilon.isZeroSq(_Y.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Y.normalize();
		_X.set(WB_Vector3d.cross(_Y, _Z));
		_X.normalize();
		return this;
	}

	/**
	 * Sets the yz.
	 *
	 * @param Y the y
	 * @param Z the z
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setYZ(final WB_Vector3d Y, final WB_Vector3d Z) {
		_Y.set(Y);
		_Y.normalize();
		_Z.set(Z);
		_Z.normalize();
		_X.set(WB_Vector3d.cross(_Y, _Z));
		if (WB_Epsilon.isZeroSq(_X.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_X.normalize();
		_Z.set(WB_Vector3d.cross(_X, _Y));
		_Z.normalize();
		return this;
	}

	/**
	 * Sets the zy.
	 *
	 * @param Z the z
	 * @param Y the y
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setZY(final WB_Vector3d Z, final WB_Vector3d Y) {
		_Y.set(Y);
		_Y.normalize();
		_Z.set(Z);
		_Z.normalize();
		_X.set(WB_Vector3d.cross(_Y, _Z));
		if (WB_Epsilon.isZeroSq(_X.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_X.normalize();
		_Y.set(WB_Vector3d.cross(_Z, _X));
		_Y.normalize();
		return this;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public WB_Vector3d getX() {
		return _X.get();
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public WB_Vector3d getY() {
		return _Y.get();
	}

	/**
	 * Gets the z.
	 *
	 * @return the z
	 */
	public WB_Vector3d getZ() {
		return _Z.get();
	}

	/**
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public WB_Point3d getOrigin() {
		return _origin.get();
	}

	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public WB_CoordinateSystem3D getParent() {
		return _parent;
	}

	/**
	 * Checks if is world.
	 *
	 * @return true, if is world
	 */
	public boolean isWorld() {
		return _isWorld;
	}

	/**
	 * Sets the xy.
	 *
	 * @param xx the xx
	 * @param xy the xy
	 * @param xz the xz
	 * @param yx the yx
	 * @param yy the yy
	 * @param yz the yz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setXY(final double xx, final double xy,
			final double xz, final double yx, final double yy, final double yz) {
		_X.set(xx, xy, xz);
		_X.normalize();
		_Y.set(yx, yy, yz);
		_Y.normalize();
		_Z.set(WB_Vector3d.cross(_X, _Y));
		if (WB_Epsilon.isZeroSq(_Z.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Z.normalize();
		_Y.set(WB_Vector3d.cross(_Z, _X));
		_Y.normalize();
		return this;
	}

	/**
	 * Sets the yx.
	 *
	 * @param yx the yx
	 * @param yy the yy
	 * @param yz the yz
	 * @param xx the xx
	 * @param xy the xy
	 * @param xz the xz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setYX(final double yx, final double yy,
			final double yz, final double xx, final double xy, final double xz) {
		_X.set(xx, xy, xz);
		_X.normalize();
		_Y.set(yx, yy, yz);
		_Y.normalize();
		_Z.set(WB_Vector3d.cross(_X, _Y));
		if (WB_Epsilon.isZeroSq(_Z.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Z.normalize();
		_X.set(WB_Vector3d.cross(_Y, _Z));
		_X.normalize();
		return this;
	}

	/**
	 * Sets the xz.
	 *
	 * @param xx the xx
	 * @param xy the xy
	 * @param xz the xz
	 * @param zx the zx
	 * @param zy the zy
	 * @param zz the zz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setXZ(final double xx, final double xy,
			final double xz, final double zx, final double zy, final double zz) {
		_X.set(xx, xy, xz);
		_X.normalize();
		_Z.set(zx, zy, zz);
		_Z.normalize();
		_Y.set(WB_Vector3d.cross(_Z, _X));
		if (WB_Epsilon.isZeroSq(_Y.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Y.normalize();
		_Z.set(WB_Vector3d.cross(_X, _Y));
		_Z.normalize();
		return this;
	}

	/**
	 * Sets the zx.
	 *
	 * @param zx the zx
	 * @param zy the zy
	 * @param zz the zz
	 * @param xx the xx
	 * @param xy the xy
	 * @param xz the xz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setZX(final double zx, final double zy,
			final double zz, final double xx, final double xy, final double xz) {
		_X.set(xx, xy, xz);
		_X.normalize();
		_Z.set(zx, zy, zz);
		_Z.normalize();
		_Y.set(WB_Vector3d.cross(_Z, _X));
		if (WB_Epsilon.isZeroSq(_Y.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_Y.normalize();
		_X.set(WB_Vector3d.cross(_Y, _Z));
		_X.normalize();
		return this;
	}

	/**
	 * Sets the yz.
	 *
	 * @param yx the yx
	 * @param yy the yy
	 * @param yz the yz
	 * @param zx the zx
	 * @param zy the zy
	 * @param zz the zz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setYZ(final double yx, final double yy,
			final double yz, final double zx, final double zy, final double zz) {
		_Y.set(yx, yy, yz);
		_Y.normalize();
		_Z.set(zx, zy, zz);
		_Z.normalize();
		_X.set(WB_Vector3d.cross(_Y, _Z));
		if (WB_Epsilon.isZeroSq(_X.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_X.normalize();
		_Z.set(WB_Vector3d.cross(_X, _Y));
		_Z.normalize();
		return this;
	}

	/**
	 * Sets the zy.
	 *
	 * @param zx the zx
	 * @param zy the zy
	 * @param zz the zz
	 * @param yx the yx
	 * @param yy the yy
	 * @param yz the yz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setZY(final double zx, final double zy,
			final double zz, final double yx, final double yy, final double yz) {
		_Y.set(yx, yy, yz);
		_Y.normalize();
		_Z.set(zx, zy, zz);
		_Z.normalize();
		_X.set(WB_Vector3d.cross(_Y, _Z));
		if (WB_Epsilon.isZeroSq(_X.mag2())) {
			throw new IllegalArgumentException("Vectors can not be parallel.");
		}
		_X.normalize();
		_Y.set(WB_Vector3d.cross(_Z, _X));
		_Y.normalize();
		return this;
	}

	/**
	 * Sets the x.
	 *
	 * @param X the x
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setX(final WB_Vector3d X) {
		final WB_Vector3d lX = X.get();
		lX.normalize();
		final WB_Vector3d tmp = lX.cross(_X);
		if (!WB_Epsilon.isZeroSq(tmp.mag2())) {
			rotate(-Math.acos(_X.dot(lX)), tmp);
		} else if (_X.dot(lX) < -1 + WB_Epsilon.EPSILON) {
			flipX();
		}
		return this;
	}

	/**
	 * Sets the y.
	 *
	 * @param Y the y
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setY(final WB_Vector3d Y) {
		final WB_Vector3d lY = Y.get();
		lY.normalize();
		final WB_Vector3d tmp = lY.cross(_Y);
		if (!WB_Epsilon.isZeroSq(tmp.mag2())) {
			rotate(-Math.acos(_Y.dot(lY)), tmp);
		} else if (_Y.dot(lY) < -1 + WB_Epsilon.EPSILON) {
			flipY();
		}
		return this;
	}

	/**
	 * Sets the z.
	 *
	 * @param Z the z
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setZ(final WB_Vector3d Z) {
		final WB_Vector3d lZ = Z.get();
		lZ.normalize();
		final WB_Vector3d tmp = lZ.cross(_Z);
		if (!WB_Epsilon.isZeroSq(tmp.mag2())) {
			rotate(-Math.acos(_Z.dot(lZ)), tmp);
		} else if (_Z.dot(lZ) < -1 + WB_Epsilon.EPSILON) {
			flipZ();
		}
		return this;
	}

	/**
	 * Rotate x.
	 *
	 * @param a the a
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D rotateX(final double a) {
		_Y.rotateAboutOrigin(a, _X);
		_Z.rotateAboutOrigin(a, _X);
		return this;
	}

	/**
	 * Rotate y.
	 *
	 * @param a the a
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D rotateY(final double a) {
		_X.rotateAboutOrigin(a, _Y);
		_Z.rotateAboutOrigin(a, _Y);
		return this;
	}

	/**
	 * Rotate z.
	 *
	 * @param a the a
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D rotateZ(final double a) {
		_X.rotateAboutOrigin(a, _Z);
		_Y.rotateAboutOrigin(a, _Z);
		return this;
	}

	/**
	 * Rotate.
	 *
	 * @param a the a
	 * @param v the v
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D rotate(final double a, final WB_Vector3d v) {
		final WB_Vector3d lv = v.get();
		lv.normalize();
		_X.rotateAboutOrigin(a, lv);
		_Y.rotateAboutOrigin(a, lv);
		_Z.rotateAboutOrigin(a, lv);
		return this;
	}

	/**
	 * Gets the transform from parent.
	 *
	 * @return the transform from parent
	 */
	public WB_Transform getTransformFromParent() {
		final WB_Transform result = new WB_Transform();
		result.addFromParentToCS(this);
		return result;
	}

	/**
	 * Gets the transform to parent.
	 *
	 * @return the transform to parent
	 */
	public WB_Transform getTransformToParent() {
		final WB_Transform result = new WB_Transform();
		result.addFromCSToParent(this);
		return result;
	}

	/**
	 * Gets the transform from world.
	 *
	 * @return the transform from world
	 */
	public WB_Transform getTransformFromWorld() {
		final WB_Transform result = new WB_Transform();
		result.addFromWorldToCS(this);
		return result;
	}

	/**
	 * Gets the transform to world.
	 *
	 * @return the transform to world
	 */
	public WB_Transform getTransformToWorld() {
		final WB_Transform result = new WB_Transform();
		result.addFromCSToWorld(this);
		return result;
	}

	/**
	 * Gets the transform from.
	 *
	 * @param CS the cs
	 * @return the transform from
	 */
	public WB_Transform getTransformFrom(final WB_CoordinateSystem3D CS) {
		final WB_Transform result = new WB_Transform();
		result.addFromCSToCS(CS, this);
		return result;
	}

	/**
	 * Gets the transform to.
	 *
	 * @param CS the cs
	 * @return the transform to
	 */
	public WB_Transform getTransformTo(final WB_CoordinateSystem3D CS) {
		final WB_Transform result = new WB_Transform();
		result.addFromCSToCS(this, CS);
		return result;
	}

	/**
	 * Sets the x.
	 *
	 * @param xx the xx
	 * @param xy the xy
	 * @param xz the xz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setX(final double xx, final double xy,
			final double xz) {
		final WB_Vector3d lX = new WB_Vector3d(xx, xy, xz);
		lX.normalize();
		final WB_Vector3d tmp = lX.cross(_X);
		if (!WB_Epsilon.isZeroSq(tmp.mag2())) {
			rotate(-Math.acos(_X.dot(lX)), tmp);
		} else if (_X.dot(lX) < -1 + WB_Epsilon.EPSILON) {
			flipX();
		}
		return this;
	}

	/**
	 * Sets the y.
	 *
	 * @param yx the yx
	 * @param yy the yy
	 * @param yz the yz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setY(final double yx, final double yy,
			final double yz) {
		final WB_Vector3d lY = new WB_Vector3d(yx, yy, yz);
		lY.normalize();
		final WB_Vector3d tmp = lY.cross(_Y);
		if (!WB_Epsilon.isZeroSq(tmp.mag2())) {
			rotate(-Math.acos(_Y.dot(lY)), tmp);
		} else if (_Y.dot(lY) < -1 + WB_Epsilon.EPSILON) {
			flipY();
		}
		return this;
	}

	/**
	 * Sets the z.
	 *
	 * @param zx the zx
	 * @param zy the zy
	 * @param zz the zz
	 * @return the w b_ coordinate system3 d
	 */
	public WB_CoordinateSystem3D setZ(final double zx, final double zy,
			final double zz) {
		final WB_Vector3d lZ = new WB_Vector3d(zx, zy, zz);
		lZ.normalize();
		final WB_Vector3d tmp = lZ.cross(_Z);
		if (!WB_Epsilon.isZeroSq(tmp.mag2())) {
			rotate(-Math.acos(_Z.dot(lZ)), tmp);
		} else if (_Z.dot(lZ) < -1 + WB_Epsilon.EPSILON) {
			flipZ();
		}
		return this;
	}

	/**
	 * Flip x.
	 */
	public void flipX() {
		_X.mult(-1);
		_Y.mult(-1);
	}

	/**
	 * Flip y.
	 */
	public void flipY() {
		_X.mult(-1);
		_Y.mult(-1);
	}

	/**
	 * Flip z.
	 */
	public void flipZ() {
		_Z.mult(-1);
		_Y.mult(-1);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WB_CoordinateSystem3d: origin: " + _origin + " [X=" + _X
				+ ", Y=" + _Y + ", Z=" + _Z + "]";
	}
}
