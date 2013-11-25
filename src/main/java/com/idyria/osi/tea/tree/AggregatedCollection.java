/**
 * 
 */
package com.idyria.osi.tea.tree;

import java.util.Iterator;
import java.util.List;

/**
 * This class is designed to help count the number of elements and find the
 * elements at a position in a collection that is an aggregate of one
 * 
 * @author Richnou
 * 
 */
public class AggregatedCollection<E extends Object> implements Iterable<E> {

	private List<E>[] collections = null;

	/**
	 * 
	 */
	public AggregatedCollection(List<E>... collections) {
		this.collections = collections;
	}

	private class AggregatedCollectionIterator implements Iterator<E>{

		private int count = 0;
		
		@Override
		public boolean hasNext() {
			if (count<size())
				return true;
			return false;
		}

		@Override
		public E next() {
			E res = AggregatedCollection.this.get(count);
			count++;
//			System.out.println("AggCollection output is: "+res.getClass());
			return res;
		}

		@Override
		public void remove() {
			// Not Implemented
			
		}
		
	}
	
	public int size() {
		int total = 0;

		if (this.collections != null && this.collections.length > 0)
			for (List<?> collection : collections)
				total += collection.size();

		return total;
	}

	public E get(int index) {
		// Result
		E res = null;
		// Total counted
		int totalCounted = 0;

		if (this.collections != null && this.collections.length > 0)
			for (List<E> collection : collections) {
				// If the index is < than the size of this collection (+ total counted so far) -> it's in
				// We must remove the total counted elements of previous lists to drop on to the current list correct index
				if (index<totalCounted+collection.size()) {
					res = collection.get(index-totalCounted);
					break;
				}
				// Or it's in the following. So update the total count and pass away
				totalCounted+=collection.size();
			}

		return res;

	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return this.new AggregatedCollectionIterator();
	}

	
}
