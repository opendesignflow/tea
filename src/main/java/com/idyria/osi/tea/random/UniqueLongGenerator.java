/**
 * 
 */
package com.idyria.osi.tea.random;

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

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This generator generates long numbers that will be unique in this generator space
 * @author Richnou
 *
 */
public final class UniqueLongGenerator {

	/**
	 * The generator
	 */
	private Random generator = new Random();
	
	/**
	 * Holds generated numbers
	 */
	private HashSet<Long> generated = new HashSet<Long>();
	
	/**
	 * The static Generator
	 */
	private static UniqueLongGenerator staticGenerator = new UniqueLongGenerator();
	
	/**
	 * The fair lock to synchronized the static instance
	 */
	private static ReentrantLock staticLock = new ReentrantLock(true);
	
	/**
	 * 
	 */
	public UniqueLongGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public final long generate() {
		
		long generated = 0;
		do {
			generated = generator.nextLong();
		} while (this.generated.contains(generated));
		this.generated.add(generated);
		
		return generated;
		
	}
	
	public void reset() {
		
	}
	
	/**
	 * Returns a Static instance synchronized for guaranteed uniqueness over a process.
	 * YOU MUST CALL {@link #freeStaticInstance()} AFTER EACH GET STATIC INSTANCE CALL. OTHERWHISE THE STATIC INSTANCE WILL GET DEAD-LOCKED
	 * @return
	 */
	public static synchronized UniqueLongGenerator getStaticInstance() {
		// First wait for the Generator to be free
		staticLock.lock();
		// Reset
		staticGenerator.reset();
		// Return
		return staticGenerator;
	}
	
	public static synchronized void freeStaticInstance() {
		staticLock.unlock();
	}

}
