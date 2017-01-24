/**
 * 
 */
package com.idyria.osi.tea.launch;

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
