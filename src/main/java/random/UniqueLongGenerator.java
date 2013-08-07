/**
 * 
 */
package com.idyria.osi.tea.random;

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
