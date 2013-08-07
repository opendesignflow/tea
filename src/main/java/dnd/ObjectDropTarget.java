/**
 * 
 */
package com.idyria.osi.tea.dnd;

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
