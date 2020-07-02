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

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A list maintained by WeakReferenced to its content, so that to discharge user
 * from perfect Object references cleaning in memory management optmization
 * 
 * FIXME implement correctly
 * 
 * @author rtek
 * 
 */
public class WeakReferencedList<T> implements Collection<T> {

	private LinkedList<WeakReference<T>> backend = new LinkedList<WeakReference<T>>();

	/**
	 * 
	 */
	public WeakReferencedList() {
		// TODO Auto-generated constructor stub
	}

	private abstract class IterateFoundElementListener {

		/**
		 * Search results information
		 */
		private boolean found = false;

		public abstract <T> void found(T elt) throws Throwable;

		/**
		 * @return the found
		 */
		public boolean isFound() {
			return found;
		}

		/**
		 * @param found
		 *            the found to set
		 */
		public void setFound(boolean found) {
			this.found = found;
		}

	}
	
	/**
	 * Iterator delegating to backend
	 * @author rtek
	 *
	 */
	private class BehalfIterator implements Iterator<T> {

		private Iterator<WeakReference<T>> backendIterator = backend.iterator();
		
		
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public T next() {
			// Prepare output
			T res = null;
			while(res==null) {
			// Take next
			WeakReference<T> ref = backendIterator.next();
			// Clean if
			}
			return res;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}

	/**
	 * Iterates the backend and clean it if necessary. When we have something,
	 * say that to an iterator
	 */
	private void iterate(IterateFoundElementListener list) {
		// Go through
		Iterator<WeakReference<T>> it = this.backend.iterator();
		while (it.hasNext()) {

			// Get Reference
			WeakReference<T> ref = it.next();

			// Clean
			if (ref.get() == null) {
				it.remove();
				continue;
			}

			// We have something, signal listener if we can
			if (list != null) {
				try {
					list.found(ref.get());
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public synchronized boolean add(T elt) {
		return this.backend.add(new WeakReference<T>(elt));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean addAll(Collection<? extends T> elts) {
		for (T elt : elts) {
			this.add(elt);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#clear()
	 */
	@Override
	public synchronized void clear() {
		this.backend.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public synchronized boolean contains(final Object elt) {
		IterateFoundElementListener list = new IterateFoundElementListener() {

			@Override
			public <T> void found(T felt) throws Throwable {
				if (elt == felt) {
					this.setFound(true);
				}

			}

		};
		this.iterate(list);
		return list.isFound();
	}

	/**
	 * NOT IMPLEMENTED
	 * 
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public synchronized boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.backend.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public synchronized Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public synchronized boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public synchronized boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#size()
	 */
	@Override
	public synchronized int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public synchronized Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#toArray(T[])
	 */
	@Override
	public synchronized <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
