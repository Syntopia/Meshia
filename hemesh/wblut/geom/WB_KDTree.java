package wblut.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wblut.WB_Epsilon;

/**
 * Class WB_KDTree.
 * 
 * @param <T>
 *            generic type
 * @param <V>
 *            value type
 */
public class WB_KDTree<T extends WB_Point3d, V> {

	/** dim. */
	private final int _dim;

	/** maximum bin size. */
	private final int _maximumBinSize;

	/** root. */
	private final WB_KDNode root;

	public WB_KDTree() {
		this._dim = 3;
		this._maximumBinSize = 32;
		this.root = new WB_KDNode();
	}

	public WB_KDTree(final int binsize) {
		this._dim = 3;
		this._maximumBinSize = binsize;
		this.root = new WB_KDNode();
	}

	public List<WB_AABB<T>> getLeafBounds() {
		List<WB_AABB<T>> leafs = new ArrayList();
		root.addLeafBounds(leafs);
		return leafs;
	}

	public List<WB_AABB<T>> getAllBounds() {
		List<WB_AABB<T>> all = new ArrayList();
		root.addBox(all, 0);
		return all;
	}

	/**
	 * Get the leaf regions.
	 * 
	 * @return leaf regions
	 */
	public List<WB_AABB<T>> getLeafRegions() {
		List<WB_AABB<T>> leafs = new ArrayList();
		root.addLeafRegion(leafs);
		return leafs;
	}

	/**
	 * Get all regions.
	 * 
	 * @return all regions
	 */
	public List<WB_AABB<T>> getAllRegions() {
		List<WB_AABB<T>> all = new ArrayList();
		root.addRegion(all, 0);
		return all;
	}

	public V add(final T coord, final V val) {
		return (V) root.add(new WB_KDEntry(coord, val, -1));
	}

	public WB_KDEntry<T, V>[] getRange(final WB_AABB<T> aabb) {
		return root.range(aabb);
	}

	public WB_KDEntry<T, V>[] getRange(final T center, final double radius) {
		double r2 = radius * radius;
		return root.range(center, r2);
	}

	public WB_KDEntry<T, V>[] getRange(final T center, final double lower,
			final double upper) {
		double lower2 = lower * lower;
		double upper2 = upper * upper;
		return root.range(center, lower2, upper2);
	}

	public WB_KDEntry<T, V>[] getNearestNeighbors(final T coord, final int num) {
		QueryResult<T, V> heap = new QueryResult<T, V>(num);
		root.findNearest(heap, coord);
		return heap.entries;
	}

	public WB_KDEntry<T, V> getNearestNeighbor(final T coord) {
		QueryResult<T, V> heap = new QueryResult<T, V>(1);
		root.findNearest(heap, coord);
		return heap.entries[0];
	}

	public static class WB_KDEntry<T extends WB_Point3d, V> {

		public T coord;

		public V value;

		public double d2;

		public WB_KDEntry(final T coord, final V value, final double d2) {
			this.coord = coord;
			this.value = value;
			this.d2 = d2;
		}
	}

	private class WB_KDNode<T extends WB_Point3d, V> {

		private WB_AABB<T> _limits;

		private WB_KDNode _negative, _positive;

		private final WB_AABB<T> _region;

		private WB_KDEntry<T, V>[] _bin;

		private boolean _isLeaf;

		private int _binSize;

		private int _discriminator;

		private double _sliceValue;

		private int _id;

		private WB_KDNode() {
			_bin = new WB_KDEntry[_maximumBinSize];
			_negative = _positive = null;
			_limits = null;
			_isLeaf = true;
			_binSize = 0;
			double[] min = new double[_dim];
			Arrays.fill(min, Double.NEGATIVE_INFINITY);
			double[] max = new double[_dim];
			Arrays.fill(max, Double.POSITIVE_INFINITY);
			_region = new WB_AABB(min, max);
			_id = 0;
		}

		private void addLeafBounds(final List<WB_AABB<T>> leafs) {
			if (_isLeaf) {
				WB_AABB<T> box = _limits.get();
				box.setId(_id);
				leafs.add(box);
			} else {
				_positive.addLeafBounds(leafs);
				_negative.addLeafBounds(leafs);
			}
		}

		private void addLeafRegion(final List<WB_AABB<T>> leafs) {
			if (_isLeaf) {
				WB_AABB<T> box = _region.get();
				box.setId(_id);
				leafs.add(box);
			} else {
				_positive.addLeafRegion(leafs);
				_negative.addLeafRegion(leafs);
			}
		}

		private void addBox(final List<WB_AABB<T>> leafs, final int level) {
			WB_AABB<T> box = _limits.get();
			box.setId(_id);
			box.setData("level", new Integer(level));
			leafs.add(box);
			if (!_isLeaf) {
				_positive.addBox(leafs, level + 1);
				_negative.addBox(leafs, level + 1);
			}
		}

		private void addRegion(final List<WB_AABB<T>> leafs, final int level) {
			WB_AABB<T> box = _region.get();
			box.setId(_id);
			box.setData("level", new Integer(level));
			leafs.add(box);
			if (!_isLeaf) {
				_positive.addRegion(leafs, level + 1);
				_negative.addRegion(leafs, level + 1);
			}
		}

		private V add(final WB_KDEntry<T, V> entry) {
			if (_isLeaf) {
				return addInLeaf(entry);
			} else {
				extendBounds(entry.coord);
				if (entry.coord.get(_discriminator) > _sliceValue) {
					return (V) _positive.add(entry);
				} else {
					return (V) _negative.add(entry);
				}
			}
		}

		private V addInLeaf(final WB_KDEntry<T, V> entry) {
			V lookup = lookup(entry.coord);
			if (lookup == null) {
				extendBounds(entry.coord);
				if (_binSize + 1 > _maximumBinSize) {
					addLevel();
					add(entry);
					return null;
				}
				_bin[_binSize] = entry;
				_binSize++;
				return null;
			}
			return lookup;
		}

		private V lookup(final T point) {
			for (int i = 0; i < _binSize; i++) {
				if (WB_Epsilon.isZeroSq(WB_Distance.sqDistance(point,
						_bin[i].coord))) {
					return _bin[i].value;
				}
			}
			return null;
		}

		private void findNearest(final QueryResult<T, V> heap, final T data) {
			if (_binSize == 0) {
				return;
			}
			if (_isLeaf) {
				for (int i = 0; i < _binSize; i++) {
					double dist = WB_Distance.sqDistance(_bin[i].coord, data);
					heap.tryToAdd(dist, _bin[i]);
				}
			} else {
				if (data.get(_discriminator) > _sliceValue) {
					_positive.findNearest(heap, data);
					if (_negative._binSize == 0) {
						return;
					}
					if ((heap.size < heap.capacity)
							|| _negative._limits.getDistanceSquare(data) < heap
									.getDistanceSquare(heap.size - 1)) {
						_negative.findNearest(heap, data);
					}
				} else {
					_negative.findNearest(heap, data);
					if (_positive._binSize == 0) {
						return;
					}
					if ((heap.size < heap.capacity)
							|| _positive._limits.getDistanceSquare(data) < heap
									.getDistanceSquare(heap.size - 1)) {
						_positive.findNearest(heap, data);
					}
				}
			}
		}

		private WB_KDEntry<T, V>[] range(final WB_AABB<T> range) {
			if (_bin == null) {
				WB_KDEntry<T, V>[] tmp = new WB_KDEntry[0];
				if (_negative._limits.intersects(range)) {
					WB_KDEntry<T, V>[] tmpl = _negative.range(range);
					if (0 == tmp.length) {
						tmp = tmpl;
					}
				}
				if (_positive._limits.intersects(range)) {
					WB_KDEntry<T, V>[] tmpr = _positive.range(range);
					if (0 == tmp.length) {
						tmp = tmpr;
					} else if (0 < tmpr.length) {
						WB_KDEntry<T, V>[] tmp2 = new WB_KDEntry[tmp.length
								+ tmpr.length];
						System.arraycopy(tmp, 0, tmp2, 0, tmp.length);
						System.arraycopy(tmpr, 0, tmp2, tmp.length, tmpr.length);
						tmp = tmp2;
					}
				}
				return tmp;
			}
			WB_KDEntry<T, V>[] tmp = new WB_KDEntry[_binSize];
			int n = 0;
			for (int i = 0; i < _binSize; i++) {
				if (range.contains(_bin[i].coord)) {
					tmp[n++] = _bin[i];
				}
			}
			WB_KDEntry<T, V>[] tmp2 = new WB_KDEntry[n];
			System.arraycopy(tmp, 0, tmp2, 0, n);
			return tmp2;
		}

		private WB_KDEntry<T, V>[] range(final T center, final double r2) {
			if (_bin == null) {
				WB_KDEntry<T, V>[] tmp = new WB_KDEntry[0];
				if (_negative._limits.getDistanceSquare(center) <= r2) {
					WB_KDEntry<T, V>[] tmpl = _negative.range(center, r2);
					if (tmp.length == 0) {
						tmp = tmpl;
					}
				}
				if (_positive._limits.getDistanceSquare(center) <= r2) {
					WB_KDEntry<T, V>[] tmpr = _positive.range(center, r2);
					if (tmp.length == 0) {
						tmp = tmpr;
					} else if (0 < tmpr.length) {
						WB_KDEntry<T, V>[] tmp2 = new WB_KDEntry[tmp.length
								+ tmpr.length];
						System.arraycopy(tmp, 0, tmp2, 0, tmp.length);
						System.arraycopy(tmpr, 0, tmp2, tmp.length, tmpr.length);
						tmp = tmp2;
					}
				}
				return tmp;
			}
			WB_KDEntry<T, V>[] tmp = new WB_KDEntry[_binSize];
			int n = 0;
			for (int i = 0; i < _binSize; i++) {
				double d2 = WB_Distance.sqDistance(center, _bin[i].coord);
				if (d2 <= r2) {
					_bin[i].d2 = d2;
					tmp[n++] = _bin[i];
				}
			}
			WB_KDEntry<T, V>[] tmp2 = new WB_KDEntry[n];
			System.arraycopy(tmp, 0, tmp2, 0, n);
			return tmp2;
		}

		private WB_KDEntry<T, V>[] range(final T center, final double lower2,
				final double upper2) {
			if (_bin == null) {
				WB_KDEntry<T, V>[] tmp = new WB_KDEntry[0];
				if (_negative._limits.getDistanceSquare(center) <= upper2) {
					WB_KDEntry<T, V>[] tmpl = _negative.range(center, lower2,
							upper2);
					if (tmp.length == 0) {
						tmp = tmpl;
					}
				}
				if (_positive._limits.getDistanceSquare(center) <= upper2) {
					WB_KDEntry<T, V>[] tmpr = _positive.range(center, lower2,
							upper2);
					if (tmp.length == 0) {
						tmp = tmpr;
					} else if (0 < tmpr.length) {
						WB_KDEntry<T, V>[] tmp2 = new WB_KDEntry[tmp.length
								+ tmpr.length];
						System.arraycopy(tmp, 0, tmp2, 0, tmp.length);
						System.arraycopy(tmpr, 0, tmp2, tmp.length, tmpr.length);
						tmp = tmp2;
					}
				}
				return tmp;
			}
			WB_KDEntry<T, V>[] tmp = new WB_KDEntry[_binSize];
			int n = 0;
			for (int i = 0; i < _binSize; i++) {
				double d2 = WB_Distance.sqDistance(center, _bin[i].coord);
				if ((d2 <= upper2) && (d2 >= lower2)) {
					_bin[i].d2 = d2;
					tmp[n++] = _bin[i];
				}
			}
			WB_KDEntry<T, V>[] tmp2 = new WB_KDEntry[n];
			System.arraycopy(tmp, 0, tmp2, 0, n);
			return tmp2;
		}

		/**
		 * Adds the level.
		 */
		private void addLevel() {
			_discriminator = _limits.maxOrdinate();
			_negative = new WB_KDNode();
			_positive = new WB_KDNode();
			_negative._id = 2 * _id;
			_positive._id = 2 * _id + 1;
			_sliceValue = (_limits.getMax(_discriminator) + _limits
					.getMin(_discriminator)) * 0.5;
			for (int i = 0; i < _dim; i++) {
				_negative._region._min[i] = _region._min[i];
				_positive._region._max[i] = _region._max[i];
				if (i == _discriminator) {
					_negative._region._max[i] = _sliceValue;
					_positive._region._min[i] = _sliceValue;
				} else {
					_negative._region._max[i] = _region._max[i];
					_positive._region._min[i] = _region._min[i];
				}
			}
			for (int i = 0; i < _binSize; ++i) {
				if (_bin[i].coord.get(_discriminator) > _sliceValue) {
					_positive.addInLeaf(_bin[i]);
				} else {
					_negative.addInLeaf(_bin[i]);
				}
			}
			_bin = null;
			_isLeaf = false;
		}

		private void extendBounds(final T coord) {
			if (_limits == null) {
				_limits = new WB_AABB<T>(coord);
			} else {
				_limits.expandToInclude(coord);
			}
		}
	}

	public class QueryResult<T extends WB_Point3d, V> {

		/** The entries. */
		private final WB_KDEntry<T, V>[] entries;

		/** The dist sqs. */
		private final double[] distSqs;

		/** The capacity. */
		private final int capacity;

		/** The size. */
		private int size;

		protected QueryResult(final int capacity) {
			this.entries = new WB_KDEntry[capacity];
			this.distSqs = new double[capacity];
			this.capacity = capacity;
			this.size = 0;
		}

		protected void tryToAdd(final double dist, final WB_KDEntry<T, V> entry) {
			int i = size;
			for (; i > 0 && distSqs[i - 1] > dist; --i) {
				;
			}
			if (i >= capacity) {
				return;
			}
			if (size < capacity) {
				size++;
			}
			int j = i + 1;
			System.arraycopy(distSqs, i, distSqs, j, size - j);
			distSqs[i] = dist;
			System.arraycopy(entries, i, entries, j, size - j);
			entry.d2 = dist;
			entries[i] = entry;
		}

		/**
		 * Gets the entry.
		 * 
		 * @param i
		 *            the i
		 * @return the entry
		 */
		public WB_KDEntry<T, V> getEntry(final int i) {
			if (size == 0) {
				return null;
			}
			return entries[i];
		}

		/**
		 * Gets the distance square.
		 * 
		 * @param i
		 *            the i
		 * @return the distance square
		 */
		public double getDistanceSquare(final int i) {
			if (size == 0) {
				return Double.NaN;
			}
			return distSqs[i];
		}

		/**
		 * Size.
		 * 
		 * @return the int
		 */
		public int size() {
			return size;
		}
	}
}