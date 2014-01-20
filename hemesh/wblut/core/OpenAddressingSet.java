/**
 * Copyright (C) 2010 Hal Hildebrand. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package wblut.core;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

// TODO: Auto-generated Javadoc
/**
 * The Class OpenAddressingSet.
 *
 * @param <T> the generic type
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 */
public abstract class OpenAddressingSet<T> extends AbstractSet<T> {

	/** The Constant DELETED. */
	private static final Object	DELETED		= new Object();
	
	/** The Constant PRIME. */
	private static final int	PRIME		= -1640531527;
	
	/** The Constant THRESHOLD. */
	private static final float	THRESHOLD	= 0.75f;
	
	/** The load. */
	int							load;
	
	/** The size. */
	int							size		= 0;
	
	/** The table. */
	Object						table[];

	/**
	 * Instantiates a new open addressing set.
	 */
	public OpenAddressingSet() {
		this(4);
	}

	/**
	 * Instantiates a new open addressing set.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public OpenAddressingSet(final int initialCapacity) {
		init(initialCapacity);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public final boolean add(final Object key) {
		if (key == null) {
			throw new IllegalArgumentException("Null key");
		}
		if (table == null) {
			init(1);
		} else if (size >= table.length * THRESHOLD) {
			rehash();
		}
		return insert(key);
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#clear()
	 */
	@Override
	public void clear() {
		table = null;
		size = 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public OpenAddressingSet<T> clone() {
		try {
			@SuppressWarnings("unchecked")
			final OpenAddressingSet<T> t = (OpenAddressingSet<T>) super.clone();
			if (table != null) {
				t.table = new Object[table.length];
				for (int i = table.length; i-- > 0;) {
					t.table[i] = table[i];
				}
			}
			return t;
		} catch (final CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(final Object key) {
		if (key == null || size == 0) {
			return false;
		}
		final int hash = PRIME * getHash(key) >>> load;
		int index = hash;
		do {
			final Object ob = table[index];
			if (ob == null) {
				return false;
			}
			if (equals(key, ob)) {
				return true;
			}
			index = index + (hash | 1) & table.length - 1;
		} while (index != hash);
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	@Override
	public final boolean isEmpty() {
		return size() == 0;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int	next	= 0;

			public boolean hasNext() {
				while (next < table.length) {
					if (table[next] != null && table[next] != DELETED) {
						return true;
					}
					next++;
				}
				return false;
			}

			@SuppressWarnings("unchecked")
			public T next() {
				while (next < table.length) {
					if (table[next] != null && table[next] != DELETED) {
						return (T) table[next++];
					}
					next++;
				}
				throw new NoSuchElementException("Enumerator");
			}


			public void remove() {
				throw new UnsupportedOperationException(
						"Remove is not supported");
			}
		};
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public final boolean remove(final Object key) {
		if (key == null) {
			throw new IllegalArgumentException("Null key");
		}
		if (!isEmpty()) {
			final int hash = PRIME * getHash(key) >>> load;
			int index = hash;
			do {
				final Object ob = table[index];
				if (ob == null) {
					return false;
				}
				if (equals(key, ob)) {
					table[index] = DELETED;
					size -= 1;
					return true;
				}
				index = index + (hash | 1) & table.length - 1;
			} while (index != hash);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public final int size() {
		return size;
	}

	/**
	 * Insert.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean insert(final Object key) {
		final int hash = PRIME * getHash(key) >>> load;
		int index = hash;
		do {
			final Object ob = table[index];
			if (ob == null || ob == DELETED) {
				table[index] = key;
				size += 1;
				return true;
			}
			if (equals(key, ob)) {
				table[index] = key;
				return false;
			}
			index = index + (hash | 1) & table.length - 1;
		} while (index != hash);
		rehash();
		return insert(key);
	}

	/**
	 * Rehash.
	 */
	private void rehash() {
		final Object[] oldMap = table;
		final int oldCapacity = oldMap.length;
		load -= 1;
		table = new Object[oldCapacity * 2];
		size = 0;
		for (int i = oldCapacity - 1; i >= 0; i -= 1) {
			final Object ob = oldMap[i];
			if (ob != null && ob != DELETED) {
				insert(ob);
			}
		}
	}

	/**
	 * Equals.
	 *
	 * @param key the key
	 * @param ob the ob
	 * @return true, if successful
	 */
	abstract protected boolean equals(Object key, Object ob);

	/**
	 * Gets the hash.
	 *
	 * @param key the key
	 * @return the hash
	 */
	abstract protected int getHash(Object key);

	/**
	 * Inits the.
	 *
	 * @param initialCapacity the initial capacity
	 */
	protected void init(int initialCapacity) {
		if (initialCapacity < 4) {
			initialCapacity = 4;
		}
		int cap = 4;
		load = 2;
		while (cap < initialCapacity) {
			load += 1;
			cap += cap;
		}
		table = new Object[cap];
		load = 32 - load;
	}

}