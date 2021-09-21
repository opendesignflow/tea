/**
 * 
 */
package org.odfi.tea.timer;

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

/**
 * @author rtek
 *
 */
public class TeaTiming {

	private long start = 0;
	
	private long stop = 0;
	
	private long elapsedTime = 0;
	
	/**
	 * 
	 */
	public TeaTiming() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @return
	 */
	public long start() {
		this.start = System.currentTimeMillis();
		return this.getStart();
	}
	
	/**
	 * 
	 * @return The elapsed time
	 */
	public long stop() {
		this.stop = System.currentTimeMillis();
		this.elapsedTime = this.getStop()-this.getStart();
		return this.getElapsedTime();
	}

	/**
	 * @return the start
	 */
	public long getStart() {
		return start;
	}

	/**
	 * @return the stop
	 */
	public long getStop() {
		return stop;
	}

	/**
	 * @return the elapsedTime
	 */
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	

	
}
