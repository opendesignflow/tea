/**
 * 
 */
package com.idyria.osi.tea.launch;

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

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.TimeZone;

/**
 * @author Richnou
 * 
 */
public class LaunchManager {

	/**
	 * Singleton reference
	 */
	private static LaunchManager ref = null;

	/**
	 * Ordered list of steps
	 */
	private LinkedList<LaunchStep> steps = new LinkedList<LaunchStep>();

	/**
	 * Ordered list of done steps
	 */
	private LinkedList<LaunchStep> doneSteps = new LinkedList<LaunchStep>();

	private Long startTime = null;

	private Long stopTime = null;

	/**
	 * 
	 */
	public LaunchManager() {
		super();
	}

	/**
	 * Processes next step Doesn't do anything if no more steps
	 * 
	 * @throws Throwable
	 */
	public synchronized void step() throws Throwable {

		// If first, record time
		if (doneSteps.size() == 0) {
			startTime = GregorianCalendar.getInstance(
					TimeZone.getTimeZone("UTC")).getTimeInMillis();
		}

		// Take the first element
		LaunchStep step = steps.getFirst();
		if (step != null && !step.isIgnore()) {

			step.setPercent(0.0f);

			// Do step
			if (step.getMonitor() != null)
				step.getMonitor().signalStart();

			try {
				step.start();

				if (step.getMonitor() != null)
					step.getMonitor().signalEnd();

			} catch (Throwable e) {
				e.printStackTrace();
				if (step.getMonitor() != null)
					step.getMonitor().signalFailed(e);
				step.setIgnore(true);
				throw e;
			}
			step.setPercent(100.0f);

			// Record in done
			doneSteps.addLast(step);
			steps.remove(step);

		}

		// If last, record end time
		if (steps.size() == 0) {
			stopTime = GregorianCalendar.getInstance(
					TimeZone.getTimeZone("UTC")).getTimeInMillis();
		}
	}

	/**
	 * Passes over the next step
	 */
	public synchronized void stepThrough() {
		try {
			steps.poll();
		} catch (Throwable e) {

		}
	}

	/**
	 * Do the load
	 * @throws Throwable 
	 */
	public synchronized void stepAll() throws Throwable {

//		try {
			while (this.steps.size() > 0) {
				this.step();
			}
//		} catch (Throwable e) {
//
//		}
	}

	/**
	 * Stop the next step in reverse order
	 */
	public synchronized void stopStep() {

		// Get the step
		LaunchStep step = this.doneSteps.pollLast();
		if (step != null && !step.isIgnore()) {
			// Stop
			step.stop();
		}

	}

	/**
	 * Stop all
	 */
	public synchronized void stopStepAll() {

		while (this.doneSteps.size() > 0) {
			this.stopStep();
		}

	}

	/**
	 * Add a step
	 * 
	 * @param step
	 */
	public void addStep(LaunchStep step) {
		if (step != null)
			steps.addLast(step);
	}

	/**
	 * @return the steps
	 */
	public LinkedList<LaunchStep> getSteps() {
		return steps;
	}

	/**
	 * @return the doneSteps
	 */
	public LinkedList<LaunchStep> getDoneSteps() {
		return this.doneSteps;
	}

	/**
	 * Get the instance
	 * 
	 * @return
	 */
	public static synchronized LaunchManager getInstance() {

		if (ref == null)
			ref = new LaunchManager();
		return ref;

	}

	/**
	 * @return the startTime
	 */
	public Long getStartTime() {
		return this.startTime;
	}

	/**
	 * @return the stopTime
	 */
	public Long getStopTime() {
		return this.stopTime;
	}

}
