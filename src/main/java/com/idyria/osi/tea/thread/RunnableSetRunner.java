/**
 * 
 */
package com.idyria.osi.tea.thread;

import java.util.HashSet;
import java.util.Set;

/**
 * Runs a set of Runnables on the line. with a start and stop action
 * @author Richnou
 *
 */
public abstract class RunnableSetRunner {

	private Set<Runnable> runnables = new HashSet<Runnable>();
	
	/**
	 * Keep track of the failure exceptions
	 */
	private Throwable failureException = null;
	
	/**
	 * 
	 */
	public RunnableSetRunner() {
		// TODO Auto-generated constructor stub
	}
	
	private class RunnableSetRunnerThread extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				startAction();
				for (Runnable runable: runnables) {
					runable.run();
				}			
			} finally {
				endAction();
			}	
		}
		
		
		
	}
	
	/**
	 * Action executed at beginning
	 */
	public abstract void startAction();
	
	/**
	 * Action executed at end
	 */
	public abstract void endAction();

	/**
	 * Runnables and endAction are executed in threads. Start action and end Action avec executed in
	 */
	public void start() {
		RunnableSetRunnerThread thread = new RunnableSetRunnerThread();
		thread.start();
	}
	
	public synchronized void add(Runnable r) {
		runnables.add(r);
	}
	
}
