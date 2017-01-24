/**
 * 
 */
package com.idyria.osi.tea.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Vector;

/**
 * This PIPE reads from input stream and forwards to the output streams
 * registered
 * 
 * @author rleys
 * 
 */
public class TeaIOPipe extends Thread {

	/**
	 * 
	 */
	private InputStream source = null;

	/**
	 * 
	 */
	private Vector<OutputStream> destinations = new Vector<OutputStream>();

	/**
	 * 
	 */
	public TeaIOPipe(InputStream source, OutputStream... destinations) {
		this.source = source;
		if (destinations != null)
			this.destinations.addAll(Arrays.asList(destinations));

		this.setDaemon(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		try {

			// Read
			byte[] bytes = new byte[TeaIOUtils.BUFF_SIZE];
			int readBytes = 0;
			while ((readBytes = this.source.read(bytes)) >= 0) {

				// Forward
				
					for (OutputStream destination : destinations) {
						destination.write(bytes, 0, readBytes);
						destination.flush();
					}
				
			}

			// End
			System.out.println("!!!!EOS!!!!!!");
			this.source.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Add an output stream in destinations
	 * 
	 * @param sink
	 */
	public synchronized void addDestination(OutputStream sink) {
		this.destinations.add(sink);
	}

}
