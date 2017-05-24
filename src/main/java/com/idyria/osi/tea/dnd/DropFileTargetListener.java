/**
 * 
 */
package com.idyria.osi.tea.dnd;

import java.io.File;
import java.util.Collection;

/**
 * @author rtek
 *
 */
public interface DropFileTargetListener {

	/**
	 * A file was dropped
	 * @param file
	 */
	public void droppedFile(File file);
	
	/**
	 * A Directory was dropped
	 * @param file
	 */
	public void droppedDirectory(File file);
	
	/**
	 * All datas dropped at onece
	 * @param file
	 */
	public void dropped(Collection<File> file);
	
	
	
}
