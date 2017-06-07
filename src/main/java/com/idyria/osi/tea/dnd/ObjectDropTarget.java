/**
 * 
 */
package com.idyria.osi.tea.dnd;

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

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.LinkedList;

/**
 * @author rtek
 *
 */
public class ObjectDropTarget extends TeaSpecificDropTarget<ObjectDropTargetListener> {

	/**
	 * Following object classes will be accepted
	 */
	public LinkedList<Class<?>> acceptedObjectClasses = new LinkedList<Class<?>>();
	
	
	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTarget#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public synchronized void dragOver(DropTargetDragEvent dtde) {
		
		super.dragOver(dtde);
	}
	
	

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTarget#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public synchronized void drop(DropTargetDropEvent dtde) {
		// TODO Auto-generated method stub
		super.drop(dtde);
	}

	
	
}
