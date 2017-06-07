/**
 * 
 */
package com.idyria.osi.tea.thread;

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
import java.util.Set;

/**
 * Runs a set of Runnables on the line. with a start and stop action
 * @author Richnou
 *
 */
public abstract class RunnableRunner {

	private Set<Runnable> runnables = new HashSet<Runnable>();
	
	/**
	 * Keep track of the failure exceptions
	 */
	private Throwable failureException = null;
	
	/**
	 * 
	 */
	public RunnableRunner() {
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
