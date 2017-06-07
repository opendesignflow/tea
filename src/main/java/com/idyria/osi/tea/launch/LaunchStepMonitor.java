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
