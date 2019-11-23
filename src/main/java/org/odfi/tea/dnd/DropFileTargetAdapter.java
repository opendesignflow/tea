/**
 * 
 */
package org.odfi.tea.dnd;

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
import java.util.Collection;

/**
 * Adaptation class to implement DropFileTargetListener.
 * Does nothing, just used to be subclassed
 * @author rtek
 *
 */
public abstract class DropFileTargetAdapter implements DropFileTargetListener {

	/**
	 * 
	 */
	public DropFileTargetAdapter() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.idyria.utils.java.dnd.DropFileTargetListener#dropped(java.util.Collection)
	 */
	@Override
	public void dropped(Collection<File> file) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.idyria.utils.java.dnd.DropFileTargetListener#droppedDirectory(java.io.File)
	 */
	@Override
	public void droppedDirectory(File file) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.idyria.utils.java.dnd.DropFileTargetListener#droppedFile(java.io.File)
	 */
	@Override
	public void droppedFile(File file) {
		// TODO Auto-generated method stub

	}

}
