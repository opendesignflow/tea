/**
 * 
 */
package com.idyria.osi.tea.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rtek
 *
 */
public class AutoInstanceMap<K, V> extends HashMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4105734513524700674L;

	private Class<V> valueBaseClass = null;
	
	/**
	 * 
	 */
	public AutoInstanceMap(Class<V> valueBaseClass) {
		this.valueBaseClass = valueBaseClass;
	}

	@Override
	public V get(Object key) {
		// If not in map, instanciate, add and return
		if (!this.containsKey(key)) {
			try {
				this.put((K) key,this.valueBaseClass.newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.get(key);
	}

	
	
}
