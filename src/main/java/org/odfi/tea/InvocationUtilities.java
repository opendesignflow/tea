/**
 * 
 */
package org.odfi.tea;

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
public class InvocationUtilities {

	/**
	 * 
	 */
	public InvocationUtilities() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param <T>
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public static <T> void validateArgumentThrowing(T value,String name)
			throws IllegalArgumentException {

		if (value == null) {
			// Throw
			throw new IllegalArgumentException("The Argument "+name+" is null");
		} else if (value instanceof String && ((String) value).length() == 0) {
			// Throw
			throw new IllegalArgumentException("The String argument "+name+" is null");
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param value
	 * @return
	 */
	public static <T> boolean validateArgumentBoolean(T value) {
		boolean res = true;
		try {
			InvocationUtilities.validateArgumentThrowing(value,"");
		} catch (IllegalArgumentException ex) {
			res = false;
		}
		return res;
	}

}
