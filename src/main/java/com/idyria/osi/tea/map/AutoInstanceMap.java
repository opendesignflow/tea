/**
 * 
 */
package com.idyria.osi.tea.map;

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

import java.util.HashMap;

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
