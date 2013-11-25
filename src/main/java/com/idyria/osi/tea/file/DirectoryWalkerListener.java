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
