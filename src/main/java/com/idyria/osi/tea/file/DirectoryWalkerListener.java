/**
 * 
 */
package com.idyria.osi.tea.file;

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
 * Listens to events notified by DirectoryWalker
 * @author Richnou
 *
 */
public interface DirectoryWalkerListener<FRES,DRES,CONTENT> {

	
	/**
	 * A file has been found
	 * @param file
	 * @return true to continue, false to stop there
	 */
	public FRES fileFound(CONTENT file);
	
	/**
	 * A directory has been found
	 * @param file
	 * @return true to continue, false to stop there
	 */
	public DRES directoryFound(CONTENT file);
	
}
