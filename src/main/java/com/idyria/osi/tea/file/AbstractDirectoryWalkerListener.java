/**
 * 
 */
package com.idyria.osi.tea.file;

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
