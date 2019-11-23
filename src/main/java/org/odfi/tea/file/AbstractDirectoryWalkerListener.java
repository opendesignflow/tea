/**
 * 
 */
package org.odfi.tea.file;

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

import java.io.File;

/**
 * @author Richnou
 *
 */
public abstract class AbstractDirectoryWalkerListener<FR,DR,C> implements DirectoryWalkerListener<FR,DR,C> {

	protected File initialDirectory = null;
	
	/**
	 * 
	 */
	public AbstractDirectoryWalkerListener() {
		// TODO Auto-generated constructor stub
	}

	public AbstractDirectoryWalkerListener(File initialDirectory) {
		super();
		this.initialDirectory = initialDirectory;
	}

	
	
	

}
