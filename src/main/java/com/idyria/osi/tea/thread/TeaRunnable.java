/**
 * 
 */
package com.idyria.osi.tea.thread;

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
 * @author Richnou
 *
 */
public abstract class TeaRunnable<T> implements Runnable {

	/**
	 * The object to carry
	 */
	protected T object = null;
	
	
	/**
	 * 
	 */
	public TeaRunnable() {
		// TODO Auto-generated constructor stub
	}

	
	

	/**
	 * @param object
	 */
	public TeaRunnable(T object) {
		super();
		this.object = object;
	}




	/**
	 * @return the object
	 */
	public T getObject() {
		return this.object;
	}


	/**
	 * @param object the object to set
	 */
	public void setObject(T object) {
		this.object = object;
	}

	
	
}
