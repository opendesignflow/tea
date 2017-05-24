/**
 * 
 */
package com.idyria.osi.tea.file;

import java.io.File;

/**
 * Listens to events notified by DirectoryWalker
 * @author Richnou
 *
 */
public interface DirectoryCellWalkerListener {

	
	/**
	 * A file has been found
	 * @param file
	 * @return true to continue, false to stop there
	 */
	public boolean fileFound(File file);
	
	/**
	 * A directory has been found
	 * @param file
	 * @return true to continue, false to stop there
	 */
	public boolean directoryFound(File file);
	
}
