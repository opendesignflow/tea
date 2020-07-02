/**
 * 
 */
package org.odfi.tea.i2n;

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
 * Object containing parameters necessary to find a translation
 * @author rtek
 *
 */
public class TeaI2nTranslate {

	private Class<?> universe = null;
	private String key = null;
	
	/**
	 * 
	 */
	public TeaI2nTranslate() {
		// TODO Auto-generated constructor stub
	}
	
	

	public TeaI2nTranslate(String key, Class<?> universe) {
		super();
		this.key = key;
		this.universe = universe;
	}



	/**
	 * @return the universe
	 */
	public Class<?> getUniverse() {
		return universe;
	}

	/**
	 * @param universe the universe to set
	 */
	public void setUniverse(Class<?> universe) {
		this.universe = universe;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	
	
}
