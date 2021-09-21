/**
 * 
 */
package org.odfi.tea.collections;

/*-
 * #%L
 * Tea Scala Utils Library
 * %%
 * Copyright (C) 2006 - 2017 Open Design Flow
 * %%
 * This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

/**
 * Like the pair interface of C++
 * @author rleys
 *
 */
public class Pair<L, R> {

	
	private L left = null;
	private R right = null;
	
	/**
	 * 
	 */
	public Pair() {
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * @param left
	 * @param right
	 */
	public Pair(L left, R right) {
		super();
		this.left = left;
		this.right = right;
	}



	/**
	 * @return the left
	 */
	public L getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(L left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public R getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(R right) {
		this.right = right;
	}

	
	
	
}
