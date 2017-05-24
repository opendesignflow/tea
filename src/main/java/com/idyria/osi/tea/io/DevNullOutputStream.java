/**
 * 
 */
package com.idyria.osi.tea.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Stream leading to nowhere
 * @author rleys
 *
 */
public class DevNullOutputStream extends OutputStream {

	/**
	 * 
	 */
	public DevNullOutputStream() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub

	}

}
