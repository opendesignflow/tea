/**
 * 
 */
package org.odfi.tea.io;

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
