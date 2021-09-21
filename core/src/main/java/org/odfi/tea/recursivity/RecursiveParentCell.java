/**
 * 
 */
package org.odfi.tea.recursivity;

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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A cell to hold parent/child values when performing recurisivty
 * @author Richnou
 *
 */
public class RecursiveParentCell<P,C> {

	private C child = null;
	private P parent = null;
	
	private int depth = 0;
	
	/**
	 * 
	 */
	public RecursiveParentCell() {
		// TODO Auto-generated constructor stub
	}
	
	

	public RecursiveParentCell( P parent,C child) {
		super();
		this.child = child;
		this.parent = parent;
	}



	/**
	 * @return the child
	 */
	public C getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(C child) {
		this.child = child;
	}

	/**
	 * @return the parent
	 */
	public P getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(P parent) {
		this.parent = parent;
	}
	
	
	
	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}



	/**
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}


	/**
	 * Creates a new cell and propagates depth incrementation
	 * @param parent
	 * @param child
	 * @return
	 */
	public RecursiveParentCell<P,C> createSubLevel(P parent,C child) {
		RecursiveParentCell<P,C> res = new RecursiveParentCell<P,C>(parent,child);
		res.setDepth(this.getDepth()+1);
		return res;
		
	}

	public static <E,F> Collection<RecursiveParentCell<E,F>> asRecursiveRoots(Collection<F> collection) {
		// Prepare result
		List<RecursiveParentCell<E,F>> res = new LinkedList<RecursiveParentCell<E,F>>();
		// Foreach
		if (collection!=null)
			for (F obj : collection) {
				res.add(new RecursiveParentCell<E, F>(null,obj));
			}
		
		return res;
	}
	
	

}
