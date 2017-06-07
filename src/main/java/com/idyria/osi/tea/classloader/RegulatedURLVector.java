/**
 * 
 */
package com.idyria.osi.tea.classloader;

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

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

/**
 * @author Richnou
 *
 */
public class RegulatedURLVector extends Vector<URL> {

	
	
	public RegulatedURLVector() {
		super();
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4933854320969526531L;
	private HashSet<String> comparison = new HashSet<String>();
	
	@Override
	public synchronized boolean add(URL url) {
		// Check not already existing
		if (comparison.contains(url.toExternalForm())) {
			return false;
		}
		comparison.add(url.toExternalForm());	
		return super.add(url);
	}

	@Override
	public synchronized boolean addAll(Collection<? extends URL> c) {
		for (URL url : c)
			add(url);
		return true;
	}

	
	
	
	
}
