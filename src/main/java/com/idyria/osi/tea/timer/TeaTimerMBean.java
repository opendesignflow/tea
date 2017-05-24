/**
 * 
 */
package com.idyria.osi.tea.timer;

import javax.management.NotificationBroadcaster;


/**
 * @author Richnou
 *
 */
public interface TeaTimerMBean extends NotificationBroadcaster {

	/**
	 * @return the secondNotificationID
	 */
	public Integer getSecondNotificationID();
	
	public void setSecondNotificationID(Integer secondNotificationID);
	
	public Integer getMinuteNotificationID();
	
	public void setMinuteNotificationID(Integer secondNotificationID);

	/**
	 * Autoregisters in local server
	 */
	public void autoRegister();
	
	public void sayHello();

}
