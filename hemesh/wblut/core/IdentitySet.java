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

// TODO: Auto-generated Javadoc
/**
 * The Class IdentitySet.
 *
 * @param <T> the generic type
 * @author <a href="mailto:hal.hildebrand@gmail.com">Hal Hildebrand</a>
 */
public class IdentitySet<T> extends OpenAddressingSet<T> {

	/**
	 * Instantiates a new identity set.
	 */
	public IdentitySet() {
		super(4);
	}

	/**
	 * Instantiates a new identity set.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public IdentitySet(final int initialCapacity) {
		super(initialCapacity);
	}

	/* (non-Javadoc)
	 * @see wblut.core.OpenAddressingSet#equals(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected boolean equals(final Object key, final Object ob) {
		return ob == key;
	}

	/* (non-Javadoc)
	 * @see wblut.core.OpenAddressingSet#getHash(java.lang.Object)
	 */
	@Override
	protected int getHash(final Object key) {
		return System.identityHashCode(key);
	}
}