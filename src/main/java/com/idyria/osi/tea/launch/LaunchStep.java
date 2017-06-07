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

/**
 * @author Richnou
 *
 */
public abstract class LaunchStep {

	/**
	 * Name of the step
	 */
	private String name = "Unnamed";
	
	/**
	 * A subname to identity subteps
	 */
	private String subname = null;
	
	/**
	 * Percentage of completition
	 */
	private float percent = 0.0f;
	
	/**
	 * Ignore the set??
	 */
	private boolean ignore = false;
	
	/**
	 * Monitor
	 */
	private LaunchStepMonitor monitor = null;
	
	/**
	 * 
	 */
	public LaunchStep() {
		super();
		init();
	}
	
	
	
	public LaunchStep(String name) {
		super();
		this.name = name;
		init();
	}



	/**
	 * Called from constructor to init the step
	 */
	protected void init() {
		
	}
	
	/**
	 * Start action
	 */
	public abstract void start() throws Throwable;
	
	/**
	 * Stop Action
	 */
	public void stop() {
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the subname
	 */
	public String getSubname() {
		return subname;
	}

	/**
	 * @param subname the subname to set
	 */
	protected void setSubname(String subname) {
		this.subname = subname;
		if (this.monitor!=null)
			this.monitor.setStepSubname(subname);
	}

	/**
	 * @return the percent
	 */
	public float getPercent() {
		return percent;
	}

	/**
	 * @param percent the percent to set
	 */
	protected void setPercent(float percent) {
		this.percent = percent;
		if (this.monitor!=null)
			this.monitor.setStepPercent(percent);
	}

	/**
	 * @return the monitor
	 */
	protected LaunchStepMonitor getMonitor() {
		return monitor;
	}

	/**
	 * @param monitor the monitor to set
	 */
	public void setMonitor(LaunchStepMonitor monitor) {
		this.monitor = monitor;
	}

	/**
	 * @return the ignore
	 */
	public boolean isIgnore() {
		return ignore;
	}

	/**
	 * @param ignore the ignore to set
	 */
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
	
	

}
