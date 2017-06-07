/**
 * 
 */
package com.idyria.osi.tea.timer;

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

import java.lang.management.ManagementFactory;
import java.util.Calendar;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.timer.Timer;

/**
 * @author Richnou
 *
 */
public class TeaTimer extends Timer implements TeaTimerMBean{

	/**
	 * The ID of the second notification
	 */
	private Integer SecondNotificationID = null;
	
	private Integer MinuteNotificationID = null;
	
	/**
	 * 
	 */
	public TeaTimer() {
		super();
	}

	/**
	 * @return the secondNotificationID
	 */
	public Integer getSecondNotificationID() {
		
		if (SecondNotificationID==null) {	
			SecondNotificationID = this.addNotification("Second", "Second",null, Calendar.getInstance().getTime(), Timer.ONE_SECOND);
		}
		
		return SecondNotificationID;
	}
	
	
	/**
	 * @param secondNotificationID the secondNotificationID to set
	 */
	public void setSecondNotificationID(Integer secondNotificationID) {
		this.SecondNotificationID = secondNotificationID;
	}

	/**
	 * Autoregisters in local server
	 */
	public void autoRegister() {
		try {
			ManagementFactory.getPlatformMBeanServer().registerMBean(this, new ObjectName("com.idyria.java.utils.teatimer:type=Default"));
			
			this.start();
		} catch (InstanceAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sayHello() {
		System.out.println("Hello");
		
	}

	@Override
	public Integer getMinuteNotificationID() {
		if (MinuteNotificationID==null) {	
			MinuteNotificationID = this.addNotification("Minute", "Minute",null, Calendar.getInstance().getTime(), Timer.ONE_MINUTE);
		}
		
		return MinuteNotificationID;
	}

	@Override
	public void setMinuteNotificationID(Integer minuteNotificationID) {
		this.MinuteNotificationID = minuteNotificationID;
		
	}

	
	
	
}
