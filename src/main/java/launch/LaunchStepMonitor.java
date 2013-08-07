/**
 * 
 */
package com.idyria.osi.tea.launch;

/**
 * Monitor used by a launch step to indicate change in states/advancement
 * @author Richnou
 *
 */
public interface LaunchStepMonitor {

	/**
	 * Notify of subname change
	 * @param subname 
	 */
	public void setStepSubname(String subname);
	
	/**
	 * Notify of percent change
	 * @param percent 
	 */
	public void setStepPercent(float percent);
	
	public void signalStart();
	
	public void signalEnd();
	
	public void signalFailed(Throwable e);
	
}
