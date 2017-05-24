/**
 * 
 */
package com.idyria.osi.tea.dnd;

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
