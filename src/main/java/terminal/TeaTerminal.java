/**
 * 
 */
package com.idyria.osi.tea.terminal;

import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * @author rleys
 *
 */
public abstract class TeaTerminal {

	/**
	 * Wait for last issued command to finish
	 */
	protected Semaphore commandWaitFinished = new Semaphore(0);
	
	
	
	/**
	 * 
	 */
	public TeaTerminal() {
		// TODO Auto-generated constructor stub
	}



	public abstract void close();



	public abstract void waitCommandFinished();



	public abstract void open() throws IOException;

	
	
	
}
