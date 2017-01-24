/**
 * 
 */
package com.idyria.osi.tea.timer;

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
