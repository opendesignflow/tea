/**
 * 
 */
package com.idyria.osi.tea.string;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import com.idyria.osi.tea.logging.TeaLogging;

/**
 * 
 * This Class Finds in a group of string, the common components in the beginning
 * of the names. For example:
 * 
 * a_b_c a_b_d a_f
 * 
 * b_1_2
 * 
 * string
 * 
 * will be sorted as:
 * 
 * a_ -> a_b_c -> a_b_d -> a_f
 * 
 * a_b_ -> a_b_c -> a_b_d
 * 
 * usw...
 * 
 * 
 * 
 * The map is typed and works with objects T.toString() methods for strings.
 * This allows to have objects sorted directly
 * 
 * @author rleys
 * 
 */
public class StringsCommonComponentsSorter<T> {

	/**
	 * The common components keys
	 */
	private TreeMap<String, LinkedList<T>> sortedObjects = new TreeMap<String, LinkedList<T>>();

	/**
	 * The objects used for sort algorithm, with their String representation as
	 * map
	 */
	private TreeMap<String, T> sourceObjects = new TreeMap<String, T>();

	/**
	 * After finding a common string between to, use this regexp to remove some
	 * characters or things
	 */
	private String commonStringFilter = null;

	/**
	 * replacement used when filtering Defaults to "" (means nothing)
	 */
	private String commonStringReplacement = "";

	/**
	 * 
	 */
	public StringsCommonComponentsSorter(Collection<T> sourceObjects) {

		for (T object : sourceObjects) {
			this.sourceObjects.put(object.toString(), object);
		}

	}

	/**
	 * Performs sorting
	 */
	public void sort() {

		// -- Get All keys to be sorted
		LinkedList<String> objectKeys = new LinkedList<String>(
				this.sourceObjects.keySet());

		while (!objectKeys.isEmpty()) {

			// -- Get key
			String objectStr = objectKeys.poll();
			T sourceObject = this.sourceObjects.get(objectStr);

			// -- Compare with all others
			for (String anotherObjectKey : objectKeys) {

				// -- Compare with longest string of two first
				LcsString lcs = new LcsString(objectStr, anotherObjectKey);
				if (objectStr.length() < anotherObjectKey.length()) {
					lcs = new LcsString(anotherObjectKey, objectStr);
				}

				// -- If found something, create group, or add Object to if not
				// already in it
				String lcsRes = lcs.getLcs();
				if (lcs.isLcsStart()) {

					// -- Filter Group name
					if (this.commonStringFilter != null)
						lcsRes = lcsRes.replaceAll(commonStringFilter, commonStringReplacement);

					// TeaLogging.teaLogInfo(objectStr + " and " +
					// anotherObjectKey+" in group "+lcsRes);

					// -- Get Group
					LinkedList<T> groupObjects = this.sortedObjects.get(lcsRes);
					if (groupObjects == null) {
						groupObjects = new LinkedList<T>();
						this.sortedObjects.put(lcsRes, groupObjects);
					}

					// -- Add objects to group if not already
					T comparedObject = this.sourceObjects.get(anotherObjectKey);
					if (!groupObjects.contains(sourceObject)) {
						groupObjects.add(sourceObject);
					}
					if (!groupObjects.contains(comparedObject)) {
						groupObjects.add(comparedObject);
					}

				}

			}

		}
		
		//-- Clean after sort
		clean();

	}

	/**
	 * Ensure Objects are only sorted down to the lowest level, and remove empty groups
	 */
	public void clean() {
		
		
		// -- Get All keys to be cleaned
		LinkedList<String> sortedKeys = new LinkedList<String>(
				this.sortedObjects.keySet());
		
		// -- Foreach
		while (!sortedKeys.isEmpty()) {

			// -- Get key
			String sortedKey = sortedKeys.poll();
			LinkedList<T> sortedKeyObjects = this.sortedObjects.get(sortedKey);
			
			//-- For each other key that starts with this one, delete all common objects from current
			Iterator<String> it = sortedKeys.iterator();
			while ( it.hasNext()) {
				String anotherSortedKey = it.next();
				
				if (anotherSortedKey.startsWith(sortedKey)) {
					
					//-- All objects in anotherKey are removed from current
					for (T objectToClean : this.sortedObjects.get(anotherSortedKey) ) {
						if(sortedKeyObjects.contains(objectToClean)) {
							sortedKeyObjects.remove(objectToClean);
						}
					}
				}
				
			}
			
			//-- If current key is empty, remove it
			if (sortedKeyObjects.isEmpty()) {
				this.sortedObjects.remove(sortedKey);
			}
			
			
		}
		
		
	}
	
	/**
	 * The merge operations merges all leaf groups with their parent at one
	 * level, but parents that are then leafs won't be merged again
	 */
	public void merge() {

		// -- Get All keys to be merged
		LinkedList<String> sortedKeys = new LinkedList<String>(
				this.sortedObjects.keySet());
		
		// -- Foreach
		while (!sortedKeys.isEmpty()) {

			// -- Get key
			String sortedKey = sortedKeys.poll();
			LinkedList<T> sortedKeyObjects = this.sortedObjects.get(sortedKey);
			
			//-- For each other key that starts with this one, merge the proposed key,
			//-- If someting merged, don't merge, otherwise, merge into this
			// -- Compare with all others
			Iterator<String> it = sortedKeys.iterator();
			while ( it.hasNext()) {
				String anotherSortedKey = it.next();
				
				if (anotherSortedKey.startsWith(sortedKey)) {
					//TeaLogging.teaLogInfo("Maybe merge "+sortedKey+" with "+anotherSortedKey+": "+this.willMerge(anotherSortedKey));
				}
				if (anotherSortedKey.startsWith(sortedKey) && !this.willMerge(anotherSortedKey)) {
					
					
					
					//-- merge and delete anotherSortedKey
					//-- Take object of another SortedKey and add them to this ones
					for (T objectToMerge : this.sortedObjects.get(anotherSortedKey) ) {
						if(!sortedKeyObjects.contains(objectToMerge)) {
							sortedKeyObjects.add(objectToMerge);
						}
					}
					it.remove();
					this.sortedObjects.remove(anotherSortedKey);
				}
				
			}
			
			
		}

	}

	private boolean willMerge(String key) {

		//-- If any other key starts with this one, then it might merge
		Iterator<String> it = this.sortedObjects.keySet().iterator();
		while ( it.hasNext()) {
			String anotherSortedKey = it.next();
			
			if (!anotherSortedKey.equals(key) && anotherSortedKey.startsWith(key)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Returns found common components
	 * 
	 * @return
	 */
	public Set<String> getCommonComponents() {
		return this.sortedObjects.keySet();
	}

	/**
	 * For a found component, returns all objects found
	 * 
	 * @param component
	 * @return
	 */
	public Collection<T> getObjectsForComponent(String component) {
		return this.sortedObjects.get(component);
	}

	/**
	 * @return the commonStringFilter
	 */
	public String getCommonStringFilter() {
		return commonStringFilter;
	}

	/**
	 * @param commonStringFilter
	 *            the commonStringFilter to set
	 */
	public void setCommonStringFilter(String commonStringFilter,
			String commonStringReplacement) {
		this.commonStringFilter = commonStringFilter;
		this.commonStringReplacement = commonStringReplacement;
	}

}
